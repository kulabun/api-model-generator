package com.labunco.requestentity.util;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author kulabun
 * @since 3/25/17
 */
public class ElementUtil {

    private ElementUtil() {
    }

    public static <A extends Annotation> boolean hasAnnotation(Element element, Class<A> annotationType) {
        return element.getAnnotation(annotationType) != null;
    }

    public static Set<? extends Element> filterByKind(Collection<? extends Element> collection, ElementKind kind) {
        return collection.stream()
                .filter(it -> kind.equals(it.getKind()))
                .collect(Collectors.toSet());
    }

    public static <A extends Annotation> Set<? extends Element> filterByAnnotation(Set<? extends Element> fields, Class<A> annotation) {
        return fields.stream()
                .filter(it -> hasAnnotation(it, annotation))
                .collect(Collectors.toSet());
    }

    public static String extractPackageName(TypeElement element) {
        PackageElement packageElement = (PackageElement) element.getEnclosingElement();
        return packageElement.getQualifiedName().toString();
    }

    public static String extractCanonicalName(TypeElement element) {
        TypeElement typeElement = element;
        return typeElement.getQualifiedName().toString();
    }
}
