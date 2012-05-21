package wtf.per.project.testing;

/**
 * Thrown if classes annotated with @ScanPackage(WTFsPerProject.class) contain @WTF/@Test annotations.
 * <p/>
 * Creation Date: 5/19/12, 11:41 PM
 *
 * @author Alexander Zagniotov (azagniotov@gmail.com)
 * @version 1.0
 */
public final class WTFsPerProjectException extends RuntimeException {
   public WTFsPerProjectException(final String message) {
      super(message);
   }
}
