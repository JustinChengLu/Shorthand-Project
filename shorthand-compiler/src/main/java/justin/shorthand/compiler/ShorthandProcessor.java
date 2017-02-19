package justin.shorthand.compiler;

import com.google.auto.common.SuperficialValidation;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import justin.shorthand.annotations.JsonObject;

/**
 * @author justin on 2017/02/16 09:21
 * @version V1.0
 */
@AutoService(Processor.class)
public class ShorthandProcessor extends AbstractProcessor {
    private final static Set<String> sSupportedAnnotationTypes = new HashSet<>();

    static {
        sSupportedAnnotationTypes.add(JsonObject.class.getCanonicalName());
    }

    private static String getClassName(TypeElement type, String packageName) {
        int packageLen = packageName.length() + 1;
        return type.getQualifiedName().toString().substring(packageLen).replace('.', '$');
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return sSupportedAnnotationTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        findAndParseTargets(roundEnv);
        return false;
    }

    private void findAndParseTargets(RoundEnvironment env) {

        for (Element element : env.getElementsAnnotatedWith(JsonObject.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parseJsonObject(element);
            } catch (Exception e) {
                printError(element + e.getMessage());
            }
        }
    }

    private void parseJsonObject(Element element) {
        debug(element.getClass().toString());
        debug(element.getEnclosedElements());
        if (element instanceof TypeElement) {
            String pkg = getPackageName((TypeElement) element);
            String className = getClassName((TypeElement) element, pkg);
            JavaFile file = new ParseCreator(processingEnv, (TypeElement) element, pkg, className).create();
            try {
                file.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                printError(String.format("Unable to write binding for type %s: %s", element, e.getMessage()));
            }
        }
    }

    private String getPackageName(TypeElement type) {
        return processingEnv.getElementUtils().getPackageOf(type).getQualifiedName().toString();
    }

    private void debug(Object msg) {
        print(msg.toString());
    }

    private void print(Diagnostic.Kind kind, String msg) {
        if (isInitialized()) {
            processingEnv.getMessager().printMessage(kind,
                    "JUSTIN:" + msg);
        }
    }

    private void print(String msg) {
        print(Diagnostic.Kind.NOTE, msg);
    }

    private void printError(String msg) {
        print(Diagnostic.Kind.ERROR, msg);
    }

    private void printWarning(String msg) {
        print(Diagnostic.Kind.WARNING, msg);
    }
}
