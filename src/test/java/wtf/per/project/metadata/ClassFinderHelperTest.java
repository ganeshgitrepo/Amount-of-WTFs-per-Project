package wtf.per.project.metadata;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import wtf.per.project.WTFsPerProjectRunner;

import java.io.File;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

/**
 * Creation Date: 5/21/12, 11:18 AM
 */
public class ClassFinderHelperTest {

   private static final String packageName = WTFsPerProjectRunner.PACKAGE_TO_SCAN;
   private static final String systemPath = WTFsPerProjectRunner.PACKAGE_TO_SCAN.replace(".", "/");

   @Test(expected = IllegalArgumentException.class)
   public void shouldThrowWhenEmptyPackageGivenToCleansePackageIntoSystemPath() throws Exception {
      ClassFinderHelper.cleansePackageIntoSystemPath("");
   }

   @Test(expected = IllegalArgumentException.class)
   public void shouldThrowWhenNullPackageGivenToCleansePackageIntoSystemPath() throws Exception {
      ClassFinderHelper.cleansePackageIntoSystemPath(null);
   }

   @Test
   public void shouldCleansePackageIntoSystemPath() throws Exception {
      final String cleansedSystemPath = ClassFinderHelper.cleansePackageIntoSystemPath(packageName);
      Assert.assertEquals(systemPath, cleansedSystemPath);
   }

   @Test
   public void shouldNotLoadResourcesFromPackageName() throws Exception {
      final Enumeration<URL> resources = ClassFinderHelper.loadResourcesFromSystemPath(packageName);
      Assert.assertFalse("The enumeration must not contain any element", resources.hasMoreElements());
   }

   @Test
   public void shouldLoadResourcesFromSystemPath() throws Exception {
      final Enumeration<URL> resources = ClassFinderHelper.loadResourcesFromSystemPath(systemPath);
      Assert.assertTrue("The enumeration must contain at least one element", resources.hasMoreElements());
   }

   @Test
   public void shouldLoadFilesFromResources() throws Exception {
      final Enumeration<URL> resources = ClassFinderHelper.loadResourcesFromSystemPath(systemPath);
      final List<File> directories = ClassFinderHelper.loadFilesFromResources(resources);
      Assert.assertTrue("The directories must contain at least one element", directories.size() > 0);
   }

   @Test
   public void shouldThrowWhenEmptyEnumerationGivenToLoadFilesFromResources() throws Exception {
      final Enumeration<URL> resources = new Enumeration<URL>() {
         @Override
         public boolean hasMoreElements() {
            return false;
         }

         @Override
         public URL nextElement() {
            return null;
         }
      };
      final List<File> directories = ClassFinderHelper.loadFilesFromResources(resources);
      Assert.assertTrue("The directories must not contain elements", directories.size() == 0);
   }

   @Test(expected = IllegalArgumentException.class)
   public void shouldThrowWhenNullEnumerationGivenToLoadFilesFromResources() throws Exception {
      ClassFinderHelper.loadFilesFromResources(null);
   }

   @Test
   public void shouldGetClassNameFromPackageAndFile() throws Exception {
      final File file = new File("SomeClass.class");
      final String className = ClassFinderHelper.getClassNameFromPackageAndFile(packageName, file);
      Assert.assertEquals(packageName + ".SomeClass", className);
   }

   @Test
   public void shouldGetClassNameFromPackageAndSystemPathFileName() throws Exception {
      final File file = new File(systemPath + "/SomeClass.class");
      final String className = ClassFinderHelper.getClassNameFromPackageAndFile(packageName, file);
      Assert.assertEquals(packageName + ".SomeClass", className);
   }

   @Test
   public void shouldFindClassesInDirectory() throws Exception {
      final File packageSystemAbsoluteDir =  getAbsolutePathFromCurrentLocationAndPackage(systemPath);
      final Set<Class<?>> foundClasses = ClassFinderHelper.findClassesInDirectory(packageName, packageSystemAbsoluteDir);
      Assert.assertTrue("The " + packageSystemAbsoluteDir + " surely contains classes", foundClasses.size() > 0);
   }

   @Test
   public void shouldFindClassesForPackage() throws Exception {
      final Set<Class<?>> foundClasses = ClassFinderHelper.forPackage(packageName);
      Assert.assertTrue("The " + packageName + " surely contains classes", foundClasses.size() > 0);
   }

   private File getAbsolutePathFromCurrentLocationAndPackage(final String systemPath) {
      final File dot = new File(".");
      final String absolutePath = dot.getAbsolutePath();
      final String packageSystemAbsolutePath = absolutePath.replace(".", "target/classes/" + systemPath);
      return new File(packageSystemAbsolutePath);
   }
}
