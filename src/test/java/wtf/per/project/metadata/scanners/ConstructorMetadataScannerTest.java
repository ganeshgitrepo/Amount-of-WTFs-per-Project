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
 * Creation Date: 5/21/12, 4:44 PM
 */
public final class ConstructorMetadataScannerTest {

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
   public void testGetConstructorMetadata() throws Exception {
      final ConstructorMetadataScanner scanner = new ConstructorMetadataScanner(foundClasses);
      Set<String> foundMetadata = scanner.getMetadataFor(WTF.class);
      Assert.assertTrue(foundMetadata.size() > 0);
   }
}
