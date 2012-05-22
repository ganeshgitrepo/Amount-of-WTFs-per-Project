package wtf.per.project.metadata.scanners;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.util.HashSet;
import java.util.Set;

/**
 * Abstract scanner
 * <p/>
 * Creation Date: 5/19/12, 11:41 PM
 *
 * @author Alexander Zagniotov (azagniotov@gmail.com)
 * @version 1.0
 */
abstract class AbstractMetadataScanner {

   protected static final String MSG_PREFIX_TEMPLATE = "[%s] %s";

   protected enum ElementTypes {
      FIELD, METHOD, PARAMETER, CONSTRUCTOR, ENUM, INTERFACE, CLASS, MEMBER_CLASS, LOCAL_CLASS, ANNOTATION, ANONYMOUS_CLASS
   }

   /**
    * @param elementType
    * @param relfectableObjects
    * @param targetAnnotation
    * @return
    */
   protected final Set<String> getAnnotatedReflectableElements(final String elementType, final AccessibleObject[] relfectableObjects, final Class<?> targetAnnotation) {

      final Set<String> metadata = new HashSet<String>();

      for (final AccessibleObject relfectableObject : relfectableObjects)
         for (final Annotation annotation : relfectableObject.getDeclaredAnnotations()) {

            final Class<?> annotationType = annotation.annotationType();
            if (annotationType.equals(targetAnnotation)) {
               final String metaInfo = String.format(MSG_PREFIX_TEMPLATE, elementType, relfectableObject.toString());
               metadata.add(metaInfo);
               break;
            }
         }

      return metadata;
   }

   /**
    * @param elementType
    * @param relfectableObject
    * @param parameterAnnotations
    * @param targetAnnotation
    * @return
    */
   protected final Set<String> getAnnotatedReflectableParameters(final String elementType, final AccessibleObject relfectableObject, final Annotation[][] parameterAnnotations, final Class<?> targetAnnotation) {
      final Set<String> metadata = new HashSet<String>();

      for (final Annotation[] annotations : parameterAnnotations) {
         for (final Annotation annotation : annotations) {
            final Class<?> annotationType = annotation.annotationType();
            if (annotationType.equals(targetAnnotation)) {
               final String metaInfo = String.format(MSG_PREFIX_TEMPLATE, elementType, relfectableObject.toString());
               metadata.add(metaInfo);
               break;
            }
         }
      }

      return metadata;
   }
}
