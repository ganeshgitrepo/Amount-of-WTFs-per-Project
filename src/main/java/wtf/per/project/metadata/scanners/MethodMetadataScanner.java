package wtf.per.project.metadata.scanners;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * Methods and their parameters scanner.
 * <p/>
 * Creation Date: 5/19/12, 11:41 PM
 *
 * @author Alexander Zagniotov (azagniotov@gmail.com)
 * @version 1.0
 */
public final class MethodMetadataScanner extends AbstractMetadataScanner implements Scanner {

   private final Set<Class<?>> targetClasses;

   /**
    * @param targetClasses
    */
   public MethodMetadataScanner(final Set<Class<?>> targetClasses) {
      this.targetClasses = targetClasses;
   }

   /**
    * @param targetAnnotation
    * @return
    */
   @Override
   public final Set<String> getMetadataFor(final Class<?> targetAnnotation) {
      final Set<String> metadata = new HashSet<String>();

      for (final Class<?> clazzor : targetClasses) {
         final Method[] methods = clazzor.getDeclaredMethods();

         metadata.addAll(getAnnotatedReflectableElements(ElementTypes.METHOD.name(), methods, targetAnnotation));
         for (final Method method : methods) {
            metadata.addAll(getAnnotatedReflectableParameters(ElementTypes.PARAMETER.name(), method, method.getParameterAnnotations(), targetAnnotation));
         }
      }

      return metadata;
   }
}