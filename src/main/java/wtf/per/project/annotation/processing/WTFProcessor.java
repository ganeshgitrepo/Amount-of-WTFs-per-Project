package wtf.per.project.annotation.processing;

import wtf.per.project.annotation.WTF;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Arrays;
import java.util.List;
import java.util.Set;


/**
 * WTF Annotation processor
 * <p/>
 * Creation Date: 5/19/12, 7:43 PM
 *
 * @author Alexander Zagniotov (azagniotov@gmail.com)
 * @version 1.0
 */
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedAnnotationTypes("wtf.per.project.annotation.WTF")
public final class WTFProcessor extends AbstractProcessor {

   private ProcessingEnvironment processingEnvironment;

   /**
    * @param processingEnvironment
    */
   @Override
   public final synchronized void init(final ProcessingEnvironment processingEnvironment) {
      this.processingEnvironment = processingEnvironment;
   }

   /**
    * @param typeElements
    * @param roundEnvironment
    * @return
    */
   @Override
   public final boolean process(final Set<? extends TypeElement> typeElements, final RoundEnvironment roundEnvironment) {
      if (!roundEnvironment.processingOver()) {
         for (final TypeElement typeElement : typeElements) {
            final Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(typeElement);
            for (final Element element : elements) {
               final String compilerMessage = buildMessage(element);
               processingEnvironment.getMessager().printMessage(Diagnostic.Kind.WARNING, compilerMessage, typeElement);
            }
         }
      }
      return true;
   }

   /**
    * @param element
    * @return
    */
   private String buildMessage(final Element element) {

      final WTF annotation = element.getAnnotation(WTF.class);
      final String annotationMessage = annotation.value();
      final String canonicalClassName = element.getEnclosingElement().toString();
      final List<ElementKind> topLevelKinds = Arrays.asList(ElementKind.CLASS, ElementKind.ENUM, ElementKind.INTERFACE);
      final String messageTemplateSuffix = " => WTF?! %s";

      if (topLevelKinds.contains(element.getKind())) {
         return String.format("In %s [%s.%s] : %s level".concat(messageTemplateSuffix),
               element.getKind(),
               canonicalClassName,
               element.getSimpleName(),
               element.getKind(),
               annotationMessage);
      }

      final String annotatedTarget = element.toString();
      final String annotatedKind = element.getKind().name();

      return String.format("In %s [%s] : %s '%s'".concat(messageTemplateSuffix),
            element.getEnclosingElement().getKind(),
            canonicalClassName,
            annotatedKind,
            annotatedTarget,
            annotationMessage);
   }
}
