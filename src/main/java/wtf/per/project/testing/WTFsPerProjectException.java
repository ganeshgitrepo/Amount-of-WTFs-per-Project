package wtf.per.project.testing;

/**
 * Thrown if classes annotated with @Grep(WTFsPerProject.class) contain @WTF/@Test annotations.
 * <p/>
 * Creation Date: 5/19/12, 11:41 PM
 *
 * @author Alexander Zagniotov (azagniotov@gmail.com)
 * @version 1.0
 */
final class WTFsPerProjectException extends RuntimeException {
   private static final long serialVersionUID = 1L;

   /**
    * @param message
    */
   public WTFsPerProjectException(final String message) {
      super(message);
   }
}
