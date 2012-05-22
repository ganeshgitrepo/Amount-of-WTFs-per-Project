package wtf.per.project.metadata.scanners;

import java.util.Set;

interface Scanner {
   /**
    * @param annotation
    * @return
    */
   Set<String> getMetadataFor(final Class<?> annotation);
}
