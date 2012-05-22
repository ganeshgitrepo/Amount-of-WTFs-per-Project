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
    * @param accessibleObjects
    * @param targetAnnotation
    * @return
    */
   protected final Set<String> getAnnotatedReflectableElements(final String elementType, final AccessibleObject[] accessibleObjects, Class<?> targetAnnotation) {

      final Set<String> metadata = new HashSet<String>();

      for (final AccessibleObject accessibleObject : accessibleObjects) {
         metadata.add(checkReflectableElementForAnnotation(elementType, accessibleObject, targetAnnotation));
      }

      return metadata;
   }

   /**
    * @param elementType
    * @param accessibleObject
    * @param parameterAnnotations
    * @param targetAnnotation
    * @return
    */
   protected final Set<String> getAnnotatedReflectableParameters(final String elementType, final AccessibleObject accessibleObject, final Annotation[][] parameterAnnotations, final Class<?> targetAnnotation) {
      final Set<String> metadata = new HashSet<String>();

      for (final Annotation[] annotations : parameterAnnotations) {
         metadata.add(checkReflectableElementForAnnotation(elementType, accessibleObject, annotations, targetAnnotation));
      }

      return metadata;
   }

   /**
    * @param elementType
    * @param accessibleObject
    * @param annotations
    * @return
    */
   private final String checkReflectableElementForAnnotation(final String elementType, final AccessibleObject accessibleObject, final Annotation[] annotations, final Class<?> targetAnnotation) {

      for (final Annotation annotation : annotations) {
         final Class<?> annotationType = annotation.annotationType();
         if (annotationType.equals(targetAnnotation)) {
            return String.format(MSG_PREFIX_TEMPLATE, elementType, accessibleObject.toString());
         }
      }
      return "";
   }

   /**
    * @param elementType
    * @param accessibleObject
    * @param targetAnnotation @return
    */
   private final String checkReflectableElementForAnnotation(final String elementType, final AccessibleObject accessibleObject, final Class<?> targetAnnotation) {
      final Annotation[] annotations = accessibleObject.getAnnotations();
      return checkReflectableElementForAnnotation(elementType, accessibleObject, annotations, targetAnnotation);
   }
}