package wtf.per.project.metadata.scanners;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * Top level elements (classes, enums etc.) scanner.
 * <p/>
 * Creation Date: 5/19/12, 11:41 PM
 *
 * @author Alexander Zagniotov (azagniotov@gmail.com)
 * @version 1.0
 */
public final class TypeMetadataScanner extends AbstractMetadataScanner implements Scanner {

   public TypeMetadataScanner() {

   }

   /**
    * @param targetClasses
    * @param targetAnnotation
    * @return
    */
   @Override
   public final Set<String> getMetadataFor(final Set<Class<?>> targetClasses, final Class<?> targetAnnotation) {
      final Set<String> metadata = new HashSet<String>();

      for (final Class<?> clazzor : targetClasses) {
         final Annotation[] annotations = clazzor.getDeclaredAnnotations();

         for (Annotation annotation : annotations) {
            final Class<?> annotationType = annotation.annotationType();
            if (annotationType.equals(targetAnnotation)) {

               final String thisClassType = decideOnElementType(clazzor);
               final String metaInfo = String.format(MSG_PREFIX_TEMPLATE, thisClassType, clazzor.toString());
               metadata.add(metaInfo);
               break;
            }
         }
      }
      return metadata;
   }

   /**
    * @param clazzor
    * @return
    */
   private final String decideOnElementType(final Class<?> clazzor) {

      if (clazzor.isEnum()) {
         return ElementTypes.ENUM.name();
      } else if (clazzor.isAnnotation()) {
         return ElementTypes.ANNOTATION.name();
      } else if (clazzor.isInterface()) {
         return ElementTypes.INTERFACE.name();
      } else if (clazzor.isLocalClass()) {
         return ElementTypes.LOCAL_CLASS.name();
      } else if (clazzor.isMemberClass()) {
         return ElementTypes.MEMBER_CLASS.name();
      } else if (clazzor.isAnonymousClass()) {
         return ElementTypes.ANONYMOUS_CLASS.name();
      }

      return ElementTypes.CLASS.name();
   }
}
