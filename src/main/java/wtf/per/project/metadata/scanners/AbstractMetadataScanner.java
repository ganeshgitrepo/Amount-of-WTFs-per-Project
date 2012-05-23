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
      FIELD, METHOD, CONSTRUCTOR_PARAMETER, CONSTRUCTOR, ENUM, INTERFACE, CLASS, MEMBER_CLASS, LOCAL_CLASS, ANNOTATION, METHOD_PARAMETER, ANONYMOUS_CLASS
   }

   /**
    * @param elementType
    * @param accessibleObjects
    * @param targetAnnotation
    * @return
    */
   protected final Set<String> getAnnotatedReflectableElements(final String elementType, final AccessibleObject[] accessibleObjects, final Class<?> targetAnnotation) {

      final Set<String> metadata = new HashSet<String>();

      for (final AccessibleObject accessibleObject : accessibleObjects) {
         metadata.add(checkReflectableElementForAnnotation(elementType, accessibleObject, targetAnnotation));
      }
      return metadata;
   }

   /**
    *
    * @param elementType
    * @param accessibleObjectName
    * @param parameterAnnotations
    * @param targetAnnotation
    * @return
    */
   protected final Set<String> getAnnotatedReflectableParameters(final String elementType, final String accessibleObjectName, final Annotation[][] parameterAnnotations, final Class<?> targetAnnotation) {
      final Set<String> metadata = new HashSet<String>();

      for (int idx = 0; idx < parameterAnnotations.length; idx++) {
         final Annotation[] annotations = parameterAnnotations[idx];
         final String message = String.format("#%s %s", (idx + 1), accessibleObjectName);
         metadata.add(checkReflectableElementForAnnotation(elementType, message, annotations, targetAnnotation));
      }
      return metadata;
   }


   /**
    * @param elementType
    * @param accessibleObject
    * @param targetAnnotation @return
    */
   private final String checkReflectableElementForAnnotation(final String elementType, final AccessibleObject accessibleObject, final Class<?> targetAnnotation) {
      final Annotation[] annotations = accessibleObject.getAnnotations();
      return checkReflectableElementForAnnotation(elementType, accessibleObject.toString(), annotations, targetAnnotation);
   }

   /**
    *
    * @param elementType
    * @param accessibleObjectName
    * @param annotations
    * @param targetAnnotation
    * @return
    */
   private final String checkReflectableElementForAnnotation(final String elementType, final String accessibleObjectName, final Annotation[] annotations, final Class<?> targetAnnotation) {

      for (final Annotation annotation : annotations) {
         final Class<?> annotationType = annotation.annotationType();
         if (annotationType.equals(targetAnnotation)) {
            return String.format(MSG_PREFIX_TEMPLATE, elementType, accessibleObjectName);
         }
      }
      return "";
   }
}