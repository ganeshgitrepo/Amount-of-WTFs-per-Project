package wtf.per.project.metadata.scanners;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * Methods parameter scanner.
 * <p/>
 * Creation Date: 5/19/12, 11:41 PM
 *
 * @author Alexander Zagniotov (azagniotov@gmail.com)
 * @version 1.0
 */
public final class MethodParameterMetadataScanner extends AbstractMetadataScanner implements Scanner {

   public MethodParameterMetadataScanner() {

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
         final Method[] methods;
         try {
            methods = clazzor.getDeclaredMethods();
         } catch (NoClassDefFoundError e) {
            System.out.println("Could not find class definitions for methods parameters in  " + clazzor + ", skipping..");
            continue;
         }

         for (final Method method : methods) {
            metadata.addAll(getAnnotatedReflectableParameters(ElementTypes.METHOD_PARAMETER.name(), method.toString(), method.getParameterAnnotations(), targetAnnotation));
         }
      }

      return metadata;
   }
}
