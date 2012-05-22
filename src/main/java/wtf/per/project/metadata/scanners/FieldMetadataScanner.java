package wtf.per.project.metadata.scanners;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * Field scanner.
 * <p/>
 * <p/>
 * Creation Date: 5/19/12, 11:41 PM
 *
 * @author Alexander Zagniotov (azagniotov@gmail.com)
 * @version 1.0
 */
public final class FieldMetadataScanner extends AbstractMetadataScanner implements Scanner {

   private final Set<Class<?>> targetClasses;

   /**
    * @param targetClasses
    */
   public FieldMetadataScanner(final Set<Class<?>> targetClasses) {
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
         final Field[] fields;
         try {
            fields = clazzor.getDeclaredFields();
         } catch (NoClassDefFoundError e) {
            System.out.println("Could not find class definitions for fields in  " + clazzor + ", skipping..");
            continue;
         }
         metadata.addAll(getAnnotatedReflectableElements(ElementTypes.FIELD.name(), fields, targetAnnotation));
      }

      return metadata;
   }
}
