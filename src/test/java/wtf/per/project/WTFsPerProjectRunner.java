package wtf.per.project;

import org.junit.runner.RunWith;
import wtf.per.project.testing.WTFsPerProject;
import wtf.per.project.testing.annotation.ScanPackage;

/**
 * Sample runner
 * <p/>
 * Creation Date: 5/19/12, 11:41 PM
 *
 * @author Alexander Zagniotov (azagniotov@gmail.com)
 * @version 1.0
 */
@RunWith(WTFsPerProject.class)
@ScanPackage(WTFsPerProjectRunner.PACKAGE_TO_SCAN)
public final class WTFsPerProjectRunner {
   public static final String PACKAGE_TO_SCAN = "wtf.per.project.model";
}