package wtf.per.project.metadata.filters;

import java.util.regex.Pattern;

/**
 * Creation Date: 5/22/12, 9:29 AM
 */
public final class ClassNameFilter {

   private final Pattern pattern;
   private final String regexPattern;

   public ClassNameFilter(final String regexPattern) {
      this.regexPattern = regexPattern;
      this.pattern = Pattern.compile(regexPattern);
   }

   public final boolean match(final String toMatch) {
      return pattern.matcher(toMatch).matches();
   }
}
