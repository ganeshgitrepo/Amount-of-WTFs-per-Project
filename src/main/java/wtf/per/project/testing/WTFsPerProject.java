package wtf.per.project.testing;


import com.initbinder.metadata.analyzer.MetadataAnalyzer;
import com.initbinder.metadata.scanners.*;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import wtf.per.project.testing.annotation.Grep;

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
   private static Class<?> scanForWTFMetaDataAndPrepareTestClass(final Class<?> klass) {

      sanityCheck(klass);
      final Grep grepAnnotation = extractGrepAnnotationFromRunnerClass(klass);

      final String packageName = grepAnnotation.packageName();
      final String classNameFilter = grepAnnotation.classNameFilter();
      final Class<?> annotationClass = grepAnnotation.annotationClass();

      final SortedSet<String> annotatedFindings = gatherMetadataInPackageForAnnotation(packageName, classNameFilter, annotationClass);
      return populateActualValuesForAssertionInTestClass(annotatedFindings, packageName);
   }

   /**
    * @param klass
    */
   private static void sanityCheck(final Class<?> klass) {
      final Method[] declaredMethods = klass.getDeclaredMethods();

      if (declaredMethods != null && declaredMethods.length > 0) {
         throw new WTFsPerProjectException(
               "\n\n" +
                     "Classes annotated with @Grep(WTFsPerProject.class) must not contain any\n" +
                     "methods. These class sole purpose is to serve as an entry point for JUnit.\n" +
                     "Offending class: " + klass.getCanonicalName() + "\n"
         );
      }
   }

   /**
    * @param klass
    * @return
    */
   private static Grep extractGrepAnnotationFromRunnerClass(final Class<?> klass) {
      final Annotation annotation = klass.getAnnotation(Grep.class);

      if (!(annotation instanceof Grep)) {
         throw new WTFsPerProjectException(
               "\n\n" +
                     "Classes annotated with @Grep(WTFsPerProject.class) must be annotated\n" +
                     "with @Grep annotation.\n" +
                     "Offending class: " + klass.getCanonicalName() + "\n"
         );
      }

      return (Grep) annotation;
   }

   /**
    * @param packageName
    * @param classNameFilter
    * @param annotationClass @return
    */
   private static SortedSet<String> gatherMetadataInPackageForAnnotation(final String packageName, final String classNameFilter, final Class<?> annotationClass) {

      //This is the shorter version
      //return MetadataAnalyzer.getMetadataFor(packageName, annotationClass, classNameFilter);

      return MetadataAnalyzer.getMetadataFor(packageName, annotationClass,
            new TypeMetadataScanner(),
            new ConstructorMetadataScanner(),
            new ConstructorParameterMetadataScanner(),
            new MethodMetadataScanner(),
            new MethodParameterMetadataScanner(),
            new FieldMetadataScanner());

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
