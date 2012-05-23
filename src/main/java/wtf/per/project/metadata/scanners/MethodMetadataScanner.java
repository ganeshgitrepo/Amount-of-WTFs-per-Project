package wtf.per.project.metadata.scanners;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * Methods scanner.
 * <p/>
 * Creation Date: 5/19/12, 11:41 PM
 *
 * @author Alexander Zagniotov (azagniotov@gmail.com)
 * @version 1.0
 */
public final class MethodMetadataScanner extends AbstractMetadataScanner implements Scanner {

   public MethodMetadataScanner() {

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

         metadata.addAll(getAnnotatedReflectableElements(ElementTypes.METHOD.name(), methods, targetAnnotation));
      }

      return metadata;
   }
}
