package justin.shorthand.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

import justin.shorthand.annotations.Ignore;
import justin.shorthand.annotations.JsonAlias;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;

/**
 * @author justin on 2017/02/16 14:37
 * @version V1.0
 */
public class ParseCreator {
    private static final String ParameterJsonObject = "jsonObject";
    private static final TypeName JSONObject = ClassName.get("org.json", "JSONObject");
    private static final TypeName JSONException = ClassName.get("org.json", "JSONException");
    private static final TypeName ShorthandClass = ClassName.get("justin.shorthand", "Shorthand");
    private static Class<?> IJsonParse;

    static {
        try {
            IJsonParse = Class.forName("justin.shorthand.IJsonParse");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private final TypeElement element;
    private final String packageName;
    private final String className;
    private ProcessingEnvironment processingEnv;

    public ParseCreator(ProcessingEnvironment processingEnv, TypeElement element, String pkg, String className) {
        this.processingEnv = processingEnv;
        this.element = element;
        this.packageName = pkg;
        this.className = className;
    }

    private TypeSpec createJoinClass() {
        ClassName bindingClassName = ClassName.get(packageName, className + "$Parser");

        TypeSpec.Builder result = TypeSpec.classBuilder(bindingClassName.simpleName())
                .addModifiers(PUBLIC)
                .addModifiers(FINAL);

        result.addSuperinterface(IJsonParse);
        MethodSpec.Builder parseCode = MethodSpec.methodBuilder("parse");
        parseCode.addException(JSONException)
                .addAnnotation(Override.class)
                .addModifiers(PUBLIC)
                .addParameter(JSONObject, ParameterJsonObject)
                .returns(Object.class);
        CodeBlock codeBlock = CodeBlock.of("$T object = new $T();\n",
                element, element);
        parseCode.addCode(codeBlock);

        List<? extends Element> elements = element.getEnclosedElements();
        for (Element var : elements) {
            if (var instanceof VariableElement) {
                ExecutableElement setter = getSetter((VariableElement) var, elements);
                if (setter != null) {
                    codeBlock = getParseValueCode((VariableElement) var, setter);
                    if (codeBlock != null)
                        parseCode.addCode(codeBlock);

                }
            }
        }
        codeBlock = CodeBlock.of("return object;\n");
        parseCode.addCode(codeBlock);
        result.addMethod(parseCode.build());
        return result.build();
    }

    private CodeBlock getParseValueCode(VariableElement element, ExecutableElement setter) {
        JsonAlias jsonAlias = element.getAnnotation(JsonAlias.class);
        String key = element.getSimpleName().toString();
        if (jsonAlias != null) {
            key = jsonAlias.value();
        }

        switch (element.asType().toString()) {
            case "int":
                return parseInt(setter, key, false);
            case "java.lang.Integer":
                return parseInt(setter, key, true);
            case "boolean":
                return parseBoolean(setter, key, false);
            case "java.lang.Boolean":
                return parseBoolean(setter, key, true);
            case "double":
                return parseDouble(setter, key, false);
            case "java.lang.Double":
                return parseDouble(setter, key, true);
            case "long":
                return parseLong(setter, key, false);
            case "java.lang.Long":
                return parseLong(setter, key, true);
            case "java.lang.String":
                return parseString(setter, key);
            default:
                Types types = processingEnv.getTypeUtils();
                TypeElement element1 = (TypeElement) types.asElement(element.asType());
                if (element1.toString().equals("java.util.List")) {
                    return parseList(element, setter, key);
                } else {
                    return parseObject(element1, setter, key);
                }
        }
    }

    public CodeBlock parseValue(StringBuilder code, ExecutableElement setter, String keyName, boolean isWrapper) {
        if (!isWrapper) {
            code.append("if( $L != null) {\n")
                    .append("\tobject.$L($L);\n")
                    .append("}\n");
            return CodeBlock.of(code.toString(), keyName, ParameterJsonObject, keyName, keyName,
                    setter.getSimpleName(), keyName);
        } else {
            code.append("object.$L($L);\n\n");
            return CodeBlock.of(code.toString(), keyName, ParameterJsonObject, keyName,
                    setter.getSimpleName(), keyName);
        }
    }

    public CodeBlock parseInt(ExecutableElement setter, String keyName, boolean isWrapper) {
        StringBuilder code = new StringBuilder();
        code.append("Integer $L = $L.optInt(\"$L\");\n");
        return parseValue(code, setter, keyName, isWrapper);
    }

    public CodeBlock parseBoolean(ExecutableElement setter, String keyName, boolean isWrapper) {
        StringBuilder code = new StringBuilder();
        code.append("Boolean $L = $L.optBoolean(\"$L\");\n");
        return parseValue(code, setter, keyName, isWrapper);
    }

    public CodeBlock parseDouble(ExecutableElement setter, String keyName, boolean isWrapper) {
        StringBuilder code = new StringBuilder();
        code.append("Double $L = $L.optDouble(\"$L\");\n");
        return parseValue(code, setter, keyName, isWrapper);
    }

    public CodeBlock parseLong(ExecutableElement setter, String keyName, boolean isWrapper) {
        StringBuilder code = new StringBuilder();
        code.append("Long $L = $L.optLong(\"$L\");\n");
        return parseValue(code, setter, keyName, isWrapper);
    }

    public CodeBlock parseString(ExecutableElement setter, String keyName) {
        StringBuilder code = new StringBuilder();
        code.append("String $L = $L.optString(\"$L\");\n");
        return parseValue(code, setter, keyName, true);
    }

    public CodeBlock parseList(VariableElement element, ExecutableElement setter, String keyName) {

        TypeMirror typeMirror = ((DeclaredType) element.asType()).getTypeArguments().get(0);

        CodeBlock getCode = CodeBlock.of("java.util.List<$L> $L = $L.parseList($L.getJSONArray(\"$L\"),$L.class);\n",
                typeMirror,
                keyName,
                ShorthandClass,
                ParameterJsonObject,
                keyName,
                typeMirror);
        CodeBlock setCode = CodeBlock.of("object.$L($L);\n\n", setter.getSimpleName(), keyName);

        return CodeBlock.builder().add(getCode).add(setCode).build();
    }

    public CodeBlock parseObject(TypeElement element, ExecutableElement setter, String keyName) {
        return CodeBlock.of("object.$L($L.parseObject($L.getJSONObject($S),$L.class));\n\n",
                setter.getSimpleName(),
                ShorthandClass,
                ParameterJsonObject,
                keyName,
                element);
    }

    private ExecutableElement getSetter(VariableElement variableElement, List<? extends Element> elements) {
        Ignore ignore = variableElement.getAnnotation(Ignore.class);
        if (ignore != null) {
            return null;
        }
        String variableName = variableElement.getSimpleName().toString();

        String setterName = "set" + Character.toUpperCase(variableName.charAt(0)) + variableName.substring(1);
        for (Element var : elements) {
            if (var instanceof ExecutableElement) {
                if (var.getSimpleName().contentEquals(setterName)) {
                    return (ExecutableElement) var;
                }
            }
        }
        return null;
    }

    public JavaFile create() {
        return JavaFile.builder(packageName, createJoinClass())
                .addFileComment("Generated code from Shorthand. Do not modify!")
                .build();
    }

    private void debug(Object msg) {
        System.out.println("-File-:" + msg.toString());
    }

}
