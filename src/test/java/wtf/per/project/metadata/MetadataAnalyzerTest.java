package wtf.per.project.metadata;

import org.junit.Assert;
import org.junit.Test;
import wtf.per.project.WTFsPerProjectRunner;
import wtf.per.project.annotation.WTF;

import java.util.Set;
import java.util.SortedSet;

/**
 * Creation Date: 5/21/12, 2:07 PM
 */
public final class MetadataAnalyzerTest {

   @Test
   public final void testGetSortedMetadataFor() throws Exception {
      final SortedSet<String> metadata = MetadataAnalyzer.getSortedMetadataFor(WTFsPerProjectRunner.PACKAGE_TO_SCAN, WTF.class);
      Assert.assertTrue(metadata.size() > 0);
   }
}
