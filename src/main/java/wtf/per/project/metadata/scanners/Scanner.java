package wtf.per.project.metadata.scanners;

import java.util.Set;

public interface Scanner {

   /**
    * @param targetClasses
    * @param annotation
    * @return
    */
   Set<String> getMetadataFor(final Set<Class<?>> targetClasses, final Class<?> annotation);
}
