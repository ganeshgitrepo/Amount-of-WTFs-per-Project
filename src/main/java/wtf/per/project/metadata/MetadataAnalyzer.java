package wtf.per.project.metadata;

import wtf.per.project.metadata.scanners.ConstructorMetadataScanner;
import wtf.per.project.metadata.scanners.FieldMetadataScanner;
import wtf.per.project.metadata.scanners.MethodMetadataScanner;
import wtf.per.project.metadata.scanners.TypeMetadataScanner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Scan classes within given package and its sub-packages and returns information about all occurances
 * for a given annotation using reflection. Archives such as JAR or ZIP are not scanned. Only top level
 * elements (classes, enums etc.), fields, methods and their parameters, constructors and their parameters.
 * <p/>
 * Creation Date: 5/19/12, 11:41 PM
 *
 * @author Alexander Zagniotov (azagniotov@gmail.com)
 * @version 1.0
 */
public final class MetadataAnalyzer {

   private MetadataAnalyzer() {

   }

   /**
    * @param targetPackageName
    * @param annotationClass
    * @return
    */
   public static final SortedSet<String> getSortedMetadataFor(final String targetPackageName, final Class<?> annotationClass) {
      final SortedSet<String> metadata = initSet();

      try {
         final Set<Class<?>> foundClasses = ClassFinderHelper.forPackage(targetPackageName);

         metadata.addAll(new TypeMetadataScanner(foundClasses).getMetadataFor(annotationClass));
         metadata.addAll(new ConstructorMetadataScanner(foundClasses).getMetadataFor(annotationClass));
         metadata.addAll(new MethodMetadataScanner(foundClasses).getMetadataFor(annotationClass));
         metadata.addAll(new FieldMetadataScanner(foundClasses).getMetadataFor(annotationClass));

      } catch (IOException e) {
         e.printStackTrace();
      } catch (URISyntaxException e) {
         e.printStackTrace();
      } catch (ClassNotFoundException e) {
         e.printStackTrace();
      }

      return metadata;
   }

   /**
    * @return
    */
   private static final SortedSet<String> initSet() {
      final SortedSet<String> metadata = new TreeSet<String>(new Comparator<String>() {
         public int compare(final String a, final String b) {
            return a.compareTo(b);
         }
      });
      return metadata;
   }

}
