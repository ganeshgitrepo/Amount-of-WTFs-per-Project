package wtf.per.project.metadata;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * Gets all classes within given package and its sub-packages
 * <p/>
 * <p/>
 * Creation Date: 5/19/12, 11:41 PM
 *
 * @author Alexander Zagniotov (azagniotov@gmail.com)
 * @version 1.0
 */
public final class ClassFinderHelper {

   /**
    * @param packageName
    * @return
    * @throws IOException
    * @throws URISyntaxException
    * @throws ClassNotFoundException
    */
   public static final Set<Class<?>> forPackage(final String packageName) throws IOException, URISyntaxException, ClassNotFoundException {

      final String systemPath = cleansePackageIntoSystemPath(packageName);
      final Enumeration<URL> resources = loadResourcesFromSystemPath(systemPath);
      final List<File> directories = loadFilesFromResources(resources);

      return findClassesInDirectories(packageName, directories);
   }

   /**
    * @param packageName
    * @return
    */
   protected static final String cleansePackageIntoSystemPath(final String packageName) {
      if (packageName == null || packageName.equals("")) {
         throw new IllegalArgumentException("Given package name must not be null or empty");
      }
      return packageName.replace(".", "/");
   }

   /**
    * @param systemPath
    * @return
    * @throws IOException
    */
   protected static final Enumeration<URL> loadResourcesFromSystemPath(final String systemPath) throws IOException {
      final ClassLoader classLoader = ClassFinderHelper.class.getClassLoader();
      return classLoader.getResources(systemPath);
   }

   /**
    * @param resources
    * @return
    * @throws URISyntaxException
    */
   protected static final List<File> loadFilesFromResources(final Enumeration<URL> resources) throws URISyntaxException {
      if (resources == null) {
         throw new IllegalArgumentException("Given Enumeration must not be null");
      }

      final List<File> directories = new LinkedList<File>();
      while (resources.hasMoreElements()) {
         final URL resource = resources.nextElement();
         directories.add(new File(resource.toURI()));
      }
      return directories;
   }

   /**
    * @param packageName
    * @param directories
    * @return
    * @throws ClassNotFoundException
    */
   protected static final Set<Class<?>> findClassesInDirectories(final String packageName, final List<File> directories) throws ClassNotFoundException {

      final Set<Class<?>> foundClasses = new HashSet<Class<?>>();

      for (final File directory : directories) {
         if (directory.exists()) {
            foundClasses.addAll(findClassesInDirectory(packageName, directory));
         }
      }
      return foundClasses;
   }

   /**
    * @param packageName
    * @param directory
    * @return
    * @throws ClassNotFoundException
    */
   protected static final Set<Class<?>> findClassesInDirectory(final String packageName, final File directory) throws ClassNotFoundException {

      final Set<Class<?>> foundClasses = new HashSet<Class<?>>();

      final File[] files = directory.listFiles();
      for (final File file : files) {
         if (file.isDirectory()) {
            if (!file.getName().contains(".")) {
               foundClasses.addAll(findClassesInDirectory(packageName + "." + file.getName(), file));
            }
         } else if (file.getName().endsWith(".class")) {
            foundClasses.add(Class.forName(getClassNameFromPackageAndFile(packageName, file)));
         }
      }

      return foundClasses;
   }

   /**
    * @param packageName
    * @param file
    * @return
    */
   protected static final String getClassNameFromPackageAndFile(final String packageName, final File file) {
      final int lastIndexOfSlash = file.getName().lastIndexOf("/") < 0 ? 0 : file.getName().lastIndexOf("/");
      final String className = packageName + '.' + file.getName().substring(lastIndexOfSlash, file.getName().lastIndexOf("."));
      return className;
   }
}
