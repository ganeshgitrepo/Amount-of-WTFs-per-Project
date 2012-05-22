package wtf.per.project.testing;


import junit.framework.Assert;
import org.junit.Test;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import wtf.per.project.annotation.WTF;
import wtf.per.project.metadata.MetadataAnalyzer;
import wtf.per.project.testing.annotation.ScanPackage;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.SortedSet;

/**
 * Custom jUnit Runner class that counts all WTFs within given package and its sub-packages.
 * The class asserts the results and intentionally fails the assertion if WTFs are present.
 * <p/>
 * Creation Date: 5/19/12, 11:41 PM
 *
 * @author Alexander Zagniotov (azagniotov@gmail.com)
 * @version 1.0
 */
public final class WTFsPerProject extends BlockJUnit4ClassRunner {

   /**
    * @param klass
    * @throws InitializationError
    */
   public WTFsPerProject(final Class<?> klass) throws InitializationError {
      super(scanForWTFMetaDataAndPrepareTestClass(klass));
   }

   /**
    * @param klass
    * @return
    */
   private static final Class<?> scanForWTFMetaDataAndPrepareTestClass(final Class<?> klass) {

      sanityCheck(klass);
      final ScanPackage scanPackageAnnotation = extractScanPackageAnnotationFromClass(klass);
      final SortedSet<String> annotatedFindings = gatherMetadataForAnnotationClass(scanPackageAnnotation.value(), WTF.class);
      return populateActualValuesForAssertionInTestClass(annotatedFindings, scanPackageAnnotation.value());
   }

   /**
    * @param klass
    */
   private static final void sanityCheck(final Class<?> klass) {
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

   /**
    * @param klass
    * @return
    */
   private static final ScanPackage extractScanPackageAnnotationFromClass(final Class<?> klass) {
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

   /**
    * @param packageName
    * @param annotationClass
    * @return
    */
   private static final SortedSet<String> gatherMetadataForAnnotationClass(final String packageName, final Class<?> annotationClass) {
      return MetadataAnalyzer.getSortedMetadataFor(packageName, annotationClass);
   }

   private static final Class<WTFsPerProjectTest> populateActualValuesForAssertionInTestClass(final Set<String> annotatedFindings, final String packageRoot) {
      if (annotatedFindings.size() > 0) {
         WTFsPerProjectTest.packageRoot = packageRoot;
         WTFsPerProjectTest.totalFoundWTFs = annotatedFindings.size();
         WTFsPerProjectTest.locationsOfWTFs = buildAssertMessage(annotatedFindings);
      }

      return WTFsPerProjectTest.class;
   }

   private static final String buildAssertMessage(final Set<String> foundAnnotatedFindings) {

      final StringBuilder builder = new StringBuilder();
      for (final String annotatedFind : foundAnnotatedFindings) {
         builder.append(annotatedFind).append("\n");
      }
      return builder.append("\n").toString().trim();
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
