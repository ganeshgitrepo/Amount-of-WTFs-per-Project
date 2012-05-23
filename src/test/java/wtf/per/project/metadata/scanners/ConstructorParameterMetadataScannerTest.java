package wtf.per.project.metadata.scanners;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import wtf.per.project.WTFsPerProjectRunner;
import wtf.per.project.annotation.WTF;
import wtf.per.project.metadata.ClassFinderHelper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;

/**
 * Creation Date: 5/22/12, 6:47 PM
 */
public class ConstructorParameterMetadataScannerTest {
   private static Set<Class<?>> foundClasses;

   @BeforeClass
   public static void runBeforeClass() {
      try {
         foundClasses = ClassFinderHelper.forPackage(WTFsPerProjectRunner.PACKAGE_TO_SCAN, null);
      } catch (IOException e) {
         e.printStackTrace();
      } catch (URISyntaxException e) {
         e.printStackTrace();
      } catch (ClassNotFoundException e) {
         e.printStackTrace();
      }
   }

   @Test
   public void testGetConstructorParametersMetadata() throws Exception {
      final ConstructorParameterMetadataScanner scanner = new ConstructorParameterMetadataScanner();
      Set<String> foundMetadata = scanner.getMetadataFor(foundClasses, WTF.class);
      Assert.assertEquals(2, foundMetadata.size());
   }
}
