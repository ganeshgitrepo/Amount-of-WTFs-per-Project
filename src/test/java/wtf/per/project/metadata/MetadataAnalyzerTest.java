package wtf.per.project.metadata;

import org.junit.Assert;
import org.junit.Test;
import wtf.per.project.WTFsPerProjectRunner;
import wtf.per.project.annotation.WTF;
import wtf.per.project.metadata.scanners.*;

import java.util.SortedSet;

/**
 * Creation Date: 5/21/12, 2:07 PM
 */
public final class MetadataAnalyzerTest {

   @Test
   public final void testGetMetadataFor() throws Exception {
      final SortedSet<String> metadata = MetadataAnalyzer.getMetadataFor(WTFsPerProjectRunner.PACKAGE_TO_SCAN, WTF.class);
      Assert.assertEquals(27, metadata.size());
   }

   @Test
   public final void testGetMetadataForConstructors() throws Exception {
      final SortedSet<String> metadata = MetadataAnalyzer.getMetadataFor(
            WTFsPerProjectRunner.PACKAGE_TO_SCAN,
            WTF.class,
            new ConstructorMetadataScanner());
      Assert.assertEquals(5, metadata.size());
   }

   @Test
   public final void testGetMetadataForMethods() throws Exception {
      final SortedSet<String> metadata = MetadataAnalyzer.getMetadataFor(
            WTFsPerProjectRunner.PACKAGE_TO_SCAN,
            WTF.class,
            new MethodMetadataScanner());
      Assert.assertEquals(8, metadata.size());
   }

   @Test
   public final void testGetMetadataForFields() throws Exception {
      final SortedSet<String> metadata = MetadataAnalyzer.getMetadataFor(
            WTFsPerProjectRunner.PACKAGE_TO_SCAN,
            WTF.class,
            new FieldMetadataScanner());
      Assert.assertEquals(3, metadata.size());
   }

   @Test
   public final void testGetMetadataForFieldsAndConstructors() throws Exception {
      final SortedSet<String> metadata = MetadataAnalyzer.getMetadataFor(
            WTFsPerProjectRunner.PACKAGE_TO_SCAN,
            WTF.class,
            new FieldMetadataScanner(),
            new ConstructorMetadataScanner());
      Assert.assertEquals(8, metadata.size());
   }

   @Test
   public final void testGetMetadataForMethodAndConstructorsParams() throws Exception {
      final SortedSet<String> metadata = MetadataAnalyzer.getMetadataFor(
            WTFsPerProjectRunner.PACKAGE_TO_SCAN,
            WTF.class,
            new MethodParameterMetadataScanner(),
            new ConstructorParameterMetadataScanner());
      Assert.assertEquals(3, metadata.size());
   }
}
