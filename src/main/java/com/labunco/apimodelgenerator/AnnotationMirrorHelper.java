package com.labunco.apimodelgenerator;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.lang.annotation.Annotation;
import java.util.Map;

public class AnnotationMirrorHelper {

    private Types types;
    private Elements elements;

    public AnnotationMirrorHelper(Types types, Elements elements) {
        this.types = types;
        this.elements = elements;
    }

    public boolean hasAnnotation(Element element, Class<? extends Annotation> annotation) {
        return getAnnotationMirror(element, annotation) != null;
    }

    public AnnotationMirror getAnnotationMirror(Element element, Class<? extends Annotation> annotation) {
        TypeElement annotationElement = elements.getTypeElement(annotation.getCanonicalName());
        if (annotationElement == null) {
            // This can happen if the annotation is on the -processorpath but not on the -classpath.
            return null;
        }
        TypeMirror annotationMirror = annotationElement.asType();

        return element.getAnnotationMirrors()
                .stream().filter(it -> types.isSameType(annotationMirror, it.getAnnotationType()))
                .findFirst().orElse(null);
    }

    public <T> T getAnnotationFieldValue(Element element,
                                         Class<? extends Annotation> annotation,
                                         String fieldName) {
        AnnotationMirror annotationMirror = getAnnotationMirror(element, annotation);
        if (annotationMirror == null) return null;
        return getFieldValue(annotationMirror, fieldName);
    }

    public <T> T getFieldValue(AnnotationMirror annotationMirror, String fieldName) {
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotationMirror.getElementValues().entrySet()) {
            if (fieldName.contentEquals(entry.getKey().getSimpleName())) {
                return (T) entry.getValue().getValue();
            }
        }
        return null;
    }
}
