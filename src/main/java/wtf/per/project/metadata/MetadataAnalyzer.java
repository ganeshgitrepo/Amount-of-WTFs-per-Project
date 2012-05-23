package wtf.per.project.metadata;

import wtf.per.project.metadata.filters.ClassNameFilter;
import wtf.per.project.metadata.scanners.*;

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
 * <p/>
 * The analyzer returns total amount of annotated elements and their canonical names.
 * <p/>
 * Creation Date: 5/19/12, 11:41 PM
 *
 * @author Alexander Zagniotov (azagniotov@gmail.com)
 * @version 1.0
 */
public final class MetadataAnalyzer {

   /**
    * @param targetPackageName
    * @param annotationClass
    * @return
    */
   public static final SortedSet<String> getMetadataFor(final String targetPackageName, final Class<?> annotationClass) {
      return MetadataAnalyzer.getMetadataFor(targetPackageName, annotationClass, (ClassNameFilter) null, new Scanner[]{});
   }

   /**
    * @param targetPackageName
    * @param annotationClass
    * @param scanners
    * @return
    */
   public static final SortedSet<String> getMetadataFor(final String targetPackageName, final Class<?> annotationClass, final Scanner... scanners) {
      return MetadataAnalyzer.getMetadataFor(targetPackageName, annotationClass, (ClassNameFilter) null, scanners);
   }

   /**
    * @param targetPackageName
    * @param annotationClass
    * @param classNamePattern
    * @return
    */
   public static final SortedSet<String> getMetadataFor(final String targetPackageName, final Class<?> annotationClass, final String classNamePattern) {
      final ClassNameFilter filter = new ClassNameFilter(classNamePattern);
      return MetadataAnalyzer.getMetadataFor(targetPackageName, annotationClass, filter, new Scanner[]{});
   }

   /**
    * @param targetPackageName
    * @param annotationClass
    * @param classNamePattern
    * @param scanners
    * @return
    */
   public static final SortedSet<String> getMetadataFor(final String targetPackageName, final Class<?> annotationClass, final String classNamePattern, final Scanner... scanners) {
      final ClassNameFilter filter = new ClassNameFilter(classNamePattern);
      return MetadataAnalyzer.getMetadataFor(targetPackageName, annotationClass, filter, scanners);
   }

   /**
    * @param targetPackageName
    * @param annotationClass
    * @param classNameFilter
    * @param scanners
    * @return
    */
   private static final SortedSet<String> getMetadataFor(final String targetPackageName, final Class<?> annotationClass, final ClassNameFilter classNameFilter, final Scanner... scanners) {

      try {
         final Set<Class<?>> foundClasses = ClassFinderHelper.forPackage(targetPackageName, classNameFilter);
         if (scanners != null && scanners.length > 0) {
            return runScannersToGatherMetadata(foundClasses, annotationClass, scanners);
         }

         return runScannersToGatherMetadata(foundClasses, annotationClass,
               new TypeMetadataScanner(),
               new ConstructorMetadataScanner(),
               new ConstructorParameterMetadataScanner(),
               new MethodMetadataScanner(),
               new MethodParameterMetadataScanner(),
               new FieldMetadataScanner());

      } catch (IOException e) {
         e.printStackTrace();
      } catch (URISyntaxException e) {
         e.printStackTrace();
      } catch (ClassNotFoundException e) {
         e.printStackTrace();
      }

      return initSet();
   }

   private static final SortedSet<String> runScannersToGatherMetadata(final Set<Class<?>> foundClasses, final Class<?> annotationClass, final Scanner... scanners) {

      final SortedSet<String> metadata = initSet();
      for (Scanner scanner : scanners) {
         metadata.addAll(scanner.getMetadataFor(foundClasses, annotationClass));
      }
      if (metadata.contains("")) {
         metadata.remove("");
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
