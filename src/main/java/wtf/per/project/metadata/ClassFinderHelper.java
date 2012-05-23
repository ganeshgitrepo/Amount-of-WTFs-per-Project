package wtf.per.project.metadata;

import wtf.per.project.metadata.filters.ClassNameFilter;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
    * @param filter
    * @return
    * @throws IOException
    * @throws URISyntaxException
    * @throws ClassNotFoundException
    */
   public static final Set<Class<?>> forPackage(final String packageName, final ClassNameFilter filter) throws IOException, URISyntaxException, ClassNotFoundException {

      final String systemPath = cleansePackageIntoSystemPath(packageName);
      final Enumeration<URL> resources = loadResourcesFromSystemPath(systemPath);
      final List<File> directories = loadFilesFromResources(resources);

      return findClassesInDirectories(packageName, directories, filter);
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
      final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
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
    * @param filter
    * @return
    * @throws ClassNotFoundException
    */
   protected static final Set<Class<?>> findClassesInDirectories(final String packageName, final List<File> directories, final ClassNameFilter filter) throws ClassNotFoundException, IOException {

      final Set<Class<?>> foundClasses = new HashSet<Class<?>>();

      for (final File directory : directories) {
         if (directory.exists()) {
            foundClasses.addAll(findClassesInDirectory(packageName, directory, filter));
         }
      }
      return foundClasses;
   }

   /**
    * @param packageName
    * @param directory
    * @param filter
    * @return
    * @throws ClassNotFoundException
    */
   protected static final Set<Class<?>> findClassesInDirectory(final String packageName, final File directory, final ClassNameFilter filter) throws ClassNotFoundException, IOException {

      final Set<Class<?>> foundClasses = new HashSet<Class<?>>();

      final File[] files = directory.listFiles();
      for (final File file : files) {

         if (file.isDirectory() && !file.getName().contains(".")) {
            foundClasses.addAll(findClassesInDirectory(packageName + "." + file.getName(), file, filter));

         } else if (file.getName().endsWith(".class")) {
            foundClasses.addAll(loadClassesFromDirectory(packageName, filter, file));

         } else if (file.getName().endsWith(".jar")) {
            foundClasses.addAll(loadClassesFromJar(file, filter));
         }
      }

      return foundClasses;
   }

   /**
    * @param packageName
    * @param filter
    * @param file
    * @return
    * @throws ClassNotFoundException
    */
   private static final Set<Class<?>> loadClassesFromDirectory(final String packageName, final ClassNameFilter filter, final File file) throws ClassNotFoundException {
      final Set<Class<?>> foundClasses = new HashSet<Class<?>>();
      final String cleansedClassName = getClassNameFromPackageAndFile(packageName, file);
      if (filter == null || filter.match(cleansedClassName)) {
         foundClasses.add(Class.forName(cleansedClassName));
      }
      return foundClasses;
   }

   /**
    * @param file
    * @param filter
    * @return
    * @throws IOException
    * @throws ClassNotFoundException
    */
   private static final Set<Class<?>> loadClassesFromJar(final File file, final ClassNameFilter filter) throws IOException, ClassNotFoundException {
      final Set<Class<?>> foundClasses = new HashSet<Class<?>>();

      final ZipInputStream zip = new ZipInputStream(file.toURI().toURL().openStream());
      final ClassLoader urlClassLoader = new URLClassLoader(new URL[]{file.toURI().toURL()});

      while (zip.getNextEntry() != null) {
         final ZipEntry entry = zip.getNextEntry();
         if (entry.getName().endsWith(".class")) {
            final String cleansedArchiveClassName = getClassNameFromZipEntry(entry);
            try {
               if (filter == null || filter.match(cleansedArchiveClassName)) {
                  foundClasses.add(urlClassLoader.loadClass(cleansedArchiveClassName));
               }
            } catch (NoClassDefFoundError e) {
               System.out.println("Could not find class definitions for: " + cleansedArchiveClassName + " from JAR " + file.getName() + ", skipping..");
            }
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
      final String className = packageName + '.' + file.getName().replaceAll("[.]class", "");
      return className;
   }

   protected static final String getClassNameFromZipEntry(final ZipEntry entry) {
      final String className = entry.getName().replaceAll("[.]class", "").replace("/", ".");
      return className;
   }
}
