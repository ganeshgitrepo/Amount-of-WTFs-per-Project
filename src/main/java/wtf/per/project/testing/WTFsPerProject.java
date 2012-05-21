package wtf.per.project.testing;


import junit.framework.Assert;
import org.junit.Test;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.reflections.Reflections;
import org.reflections.Store;
import org.reflections.scanners.*;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import wtf.per.project.annotation.WTF;
import wtf.per.project.testing.annotation.ScanPackage;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Custom jUnit Runner class that counts all WTFs across the whole project.
 * The class asserts the results and intentionally fails the assertion if WTFs present.
 * <p/>
 * Creation Date: 5/19/12, 11:41 PM
 *
 * @author Alexander Zagniotov (azagniotov@gmail.com)
 * @version 1.0
 */
public class WTFsPerProject extends BlockJUnit4ClassRunner {

   public WTFsPerProject(final Class<?> klass) throws InitializationError {
      super(scanForWTFMetaDataAndPrepareTestClass(klass));
   }

   private static Class<?> scanForWTFMetaDataAndPrepareTestClass(final Class<?> klass) {

      sanityCheck(klass);
      final ScanPackage scanPackageAnnotation = extractScanPackageAnnotationFromClass(klass);
      final Store metadata = gatherWTFAnnotationMetaDataInPackage(scanPackageAnnotation.value());
      final SortedSet<String> annotatedFindings = consolidateAnnotatedFindings(metadata, WTF.class.getCanonicalName());

      return populateActualValuesForAssertionInTestClass(annotatedFindings, scanPackageAnnotation.value());
   }

   private static void sanityCheck(final Class<?> klass) {
      final Method[] declaredMethods = klass.getDeclaredMethods();

      if (declaredMethods != null && declaredMethods.length > 0) {
         throw new WTFsPerProjectException(
               "\n\n" +
                     "Classes annotated with @ScanPackage(WTFsPerProject.class) must not contain any\n" +
                     "methods. These class sole purpose is to serve as an entry point for JUnit.\n" +
                     "Offending class: " + klass.getCanonicalName() + "\n"
         );
      }
   }

   private static ScanPackage extractScanPackageAnnotationFromClass(final Class<?> klass) {
      final Annotation annotation = klass.getAnnotation(ScanPackage.class);

      if (!(annotation instanceof ScanPackage)) {
         throw new WTFsPerProjectException(
               "\n\n" +
                     "Classes annotated with @ScanPackage(WTFsPerProject.class) must be annotated\n" +
                     "with @ScanPackage annotation.\n" +
                     "Offending class: " + klass.getCanonicalName() + "\n"
         );
      }

      return (ScanPackage) annotation;
   }

   private static Store gatherWTFAnnotationMetaDataInPackage(final String packageRoot) {

      final Reflections reflections = new Reflections(new ConfigurationBuilder()
            .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(packageRoot)))
            .setUrls(ClasspathHelper.forPackage(packageRoot))
            .setScanners(new TypesScanner(),
                  new TypeAnnotationsScanner(),
                  new ResourcesScanner(),
                  new SubTypesScanner(),
                  new MethodAnnotationsScanner(),
                  new FieldAnnotationsScanner()));

      return reflections.getStore();
   }

   private static SortedSet<String> consolidateAnnotatedFindings(final Store store, final String wtfAnnotationClassName) {

      final Set<String> annotatedTopLevelTypes = store.getTypesAnnotatedWith(wtfAnnotationClassName);
      final SortedSet<String> annotatedFindings = new TreeSet<String>(new Comparator<String>() {
         public int compare(final String a, final String b) {
            return a.compareTo(b);
         }
      });

      annotatedFindings.addAll(annotatedTopLevelTypes);
      final boolean honorInheritedClasses = true;
      annotatedFindings.addAll(store.getInheritedSubTypes(annotatedTopLevelTypes, wtfAnnotationClassName, honorInheritedClasses));
      annotatedFindings.addAll(store.getMethodsAnnotatedWith(wtfAnnotationClassName));
      annotatedFindings.addAll(store.getFieldsAnnotatedWith(wtfAnnotationClassName));

      return annotatedFindings;
   }

   private static Class<WTFsPerProjectTest> populateActualValuesForAssertionInTestClass(final Set<String> annotatedFindings, final String packageRoot) {
      if (annotatedFindings.size() > 0) {
         WTFsPerProjectTest.packageRoot = packageRoot;
         WTFsPerProjectTest.totalFoundWTFs = annotatedFindings.size();
         WTFsPerProjectTest.locationsOfWTFs = buildAssertMessage(annotatedFindings);
      }

      return WTFsPerProjectTest.class;
   }

   private static String buildAssertMessage(final Set<String> foundAnnotatedFindings) {

      final StringBuilder builder = new StringBuilder();
      for (final String annotatedFind : foundAnnotatedFindings) {
         builder.append(annotatedFind).append("\n");
      }
      return builder.append("\n").toString();
   }

   /**
    * Given to BlockJUnit4ClassRunner as a test Class
    */
   public final static class WTFsPerProjectTest {

      private static int totalFoundWTFs = 0;
      private static String packageRoot = "";
      private static String locationsOfWTFs = "";

      private final static int totalExpectedWTFs = 0;
      private final static String ASSERT_MSG_TEMPLATE = "\n\nDude.. WTF!? Sources in package [%s] are infested with [%s] WTFs:\n\n%s";

      public WTFsPerProjectTest() {

      }

      @Test
      public final void checkWhetherWTFsPresent() {
         final String errorMessage = String.format(ASSERT_MSG_TEMPLATE, packageRoot, totalFoundWTFs, locationsOfWTFs);
         Assert.assertEquals(errorMessage, totalExpectedWTFs, totalFoundWTFs);
      }
   }
}
