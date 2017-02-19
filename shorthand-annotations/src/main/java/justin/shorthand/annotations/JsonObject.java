package justin.shorthand.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * @author justin on 2017/02/16 09:17
 * @version V1.0
 */
@Retention(CLASS)
@Target(TYPE)
public @interface JsonObject {
}
