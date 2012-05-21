package wtf.per.project.model;

import wtf.per.project.annotation.WTF;

/**
 * Test model
 * <p/>
 * Creation Date: 5/19/12, 11:41 PM
 *
 * @author Alexander Zagniotov (azagniotov@gmail.com)
 * @version 1.0
 */
public final class DummyPojoChild extends DummyPojoImpl {


   public DummyPojoChild() {
      super();
   }

   @WTF
   public DummyPojoChild(final String name) {
      super(name);
   }

   @WTF
   @Override
   public String someAbstractMethod() {
      return null;
   }

   @WTF
   private static void thisIsPrivateStaticMethod() {

   }
}
