package wtf.per.project.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = {
      ElementType.ANNOTATION_TYPE,
      ElementType.CONSTRUCTOR,
      ElementType.FIELD,
      ElementType.LOCAL_VARIABLE,
      ElementType.METHOD,
      ElementType.PARAMETER,
      ElementType.TYPE})

/**
 * WTF Annotation
 *
 * Creation Date: 5/19/12, 7:41 PM
 *
 * @author Alexander Zagniotov (azagniotov@gmail.com)
 * @version 1.0
 */
public @interface WTF {
   String value() default "Dude.. WTF?!";
}
