package wtf.per.project.model;

import wtf.per.project.annotation.WTF;

import javax.security.auth.login.AccountLockedException;

/**
 * Test model
 * <p/>
 * Creation Date: 5/19/12, 11:41 PM
 *
 * @author Alexander Zagniotov (azagniotov@gmail.com)
 * @version 1.0
 */
@WTF("Are you for real?! This naming convention is bad!")
public abstract class DummyPojoImpl implements DummyPojo {

   @WTF
   public enum Names {

      ALEX, TOM, MATT;
   }

   @WTF("What is this non-descriptive name?")
   private static final String SOME_CONSTANT = "This is a constant value";

   @WTF
   private String name;
   private String surname;
   private Integer age;

   @WTF
   public DummyPojoImpl() {

   }

   @WTF
   private DummyPojoImpl(final Integer age, @WTF final String name) {

   }

   @WTF
   public DummyPojoImpl(final String name) {
      this.name = name;
   }

   @WTF
   public DummyPojoImpl(final Integer age) {

   }

   @WTF
   public String getName() {
      return name;
   }

   @WTF("Really? Is this how the methods should be named??")
   private void somePrivateMethod() {

   }

   @WTF
   public void setName(final @WTF String name, final @WTF Integer age) {
      this.name = name;
   }

   @Override
   public void someInterfaceMethod() {

   }

   @WTF
   public abstract String someAbstractMethod();
}

