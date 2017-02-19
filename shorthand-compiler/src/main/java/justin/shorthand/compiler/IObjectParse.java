package justin.shorthand.compiler;

import com.squareup.javapoet.CodeBlock;

import javax.lang.model.element.ExecutableElement;

/**
 * @author justin on 2017/02/17 09:54
 * @version V1.0
 */
public interface IObjectParse {

    CodeBlock parseInt(ExecutableElement setter, String keyName, boolean isWrapper);

    CodeBlock parseBoolean(ExecutableElement setter, String keyName, boolean isWrapper);

    CodeBlock parseDouble(ExecutableElement setter, String keyName, boolean isWrapper);

    CodeBlock parseLong(ExecutableElement setter, String keyName, boolean isWrapper);

    CodeBlock parseString(ExecutableElement setter, String keyName);

}
