package wtf.per.project.metadata.scanners;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;

/**
 * Constructors parameter scanner.
 * <p/>
 * Creation Date: 5/19/12, 11:41 PM
 *
 * @author Alexander Zagniotov (azagniotov@gmail.com)
 * @version 1.0
 */
public final class ConstructorParameterMetadataScanner extends AbstractMetadataScanner implements Scanner {

   public ConstructorParameterMetadataScanner() {

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
         final Constructor[] constructors;
         try {
            constructors = clazzor.getDeclaredConstructors();
         } catch (NoClassDefFoundError e) {
            System.out.println("Could not find class definitions for constructors parameters in  " + clazzor + ", skipping..");
            continue;
         }

         for (final Constructor constructor : constructors) {
            metadata.addAll(getAnnotatedReflectableParameters(ElementTypes.CONSTRUCTOR_PARAMETER.name(), constructor.toString(), constructor.getParameterAnnotations(), targetAnnotation));
         }
      }
      return metadata;
   }
}