package justin.shorthand.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * @author justin on 2017/02/16 09:17
 * @version V1.0
 */
@Retention(CLASS)
@Target(FIELD)
public @interface JsonAlias {
    String value();
}
