package wtf.per.project.metadata;

import wtf.per.project.metadata.filters.ClassNameFilter;
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
 * Scan .class files within given package, its sub-packages and any JAR files found.
 * If class name filter pattern provided, only the matched classes scanned for annotation presence.
 * The following elements are checked for annotation using reflection:
 * classes (and inner classes), enums, interfaces, methods (and parameters), constructors
 * (and parameters), fields.
 *
 * The analyzer returns total amount of annotated elements and their canonical names.
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
   public static final SortedSet<String> getMetadataFor(final String targetPackageName, final Class<?> annotationClass) {
      return MetadataAnalyzer.getMetadataFor(targetPackageName, annotationClass, (ClassNameFilter) null);
   }

   /**
    * @param targetPackageName
    * @param annotationClass
    * @return
    */
   public static final SortedSet<String> getMetadataFor(final String targetPackageName, final Class<?> annotationClass, final String classNamePattern) {
      final ClassNameFilter filter = new ClassNameFilter(classNamePattern);
      return MetadataAnalyzer.getMetadataFor(targetPackageName, annotationClass, filter);
   }

   /**
    *
    * @param targetPackageName
    * @param annotationClass
    * @param classNameFilter
    * @return
    */
   private static final SortedSet<String> getMetadataFor(final String targetPackageName, final Class<?> annotationClass, final ClassNameFilter classNameFilter) {
      final SortedSet<String> metadata = initSet();

      try {
         final Set<Class<?>> foundClasses = ClassFinderHelper.forPackage(targetPackageName, classNameFilter);

         metadata.addAll(new TypeMetadataScanner(foundClasses).getMetadataFor(annotationClass));
         metadata.addAll(new ConstructorMetadataScanner(foundClasses).getMetadataFor(annotationClass));
         metadata.addAll(new MethodMetadataScanner(foundClasses).getMetadataFor(annotationClass));
         metadata.addAll(new FieldMetadataScanner(foundClasses).getMetadataFor(annotationClass));

         if (metadata.contains("")) {
            metadata.remove("");
         }
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
