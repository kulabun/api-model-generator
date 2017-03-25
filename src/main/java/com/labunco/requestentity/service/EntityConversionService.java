package com.labunco.requestentity.service;

import com.google.common.base.Objects;
import com.labunco.requestentity.annotation.RequestEntity;
import com.labunco.requestentity.annotation.RequestField;
import com.labunco.requestentity.model.Field;
import com.labunco.requestentity.model.Type;
import com.labunco.requestentity.model.TypeName;
import com.labunco.requestentity.util.ClassNameUtil;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.labunco.requestentity.util.ClassNameUtil.extractClassName;
import static com.labunco.requestentity.util.ElementUtil.*;

/**
 * @author kulabun
 * @since 3/25/17
 */
public class EntityConversionService {
    private Set<? extends Element> requestModelClasses;

    public EntityConversionService(Set<? extends Element> requestModelClasses) {
        this.requestModelClasses = requestModelClasses;
    }

    public Type toType(Element element) {
        if (!ElementKind.CLASS.equals(element.getKind()))
            throw new IllegalArgumentException("Element of kind Class expected, got " + element.getKind());

        Type type = new Type();
        TypeName typeName = extractTypeName(element);
        type.setName(typeName);

        List<Field> fields = extractTypeFields(element);
        type.setFields(fields);

        type.setImports(prepareImports(typeName, fields));
        return type;
    }

    private List<TypeName> prepareImports(TypeName typeName, List<Field> fields) {
        return fields.stream()
                .map(Field::getTypeName)
                .filter(it -> !Objects.equal(typeName.getPackageName(), it.getPackageName()))
                .distinct()
                .collect(Collectors.toList());
    }

    private List<Field> extractTypeFields(Element element) {
        return getRequestParameters(element)
                .stream()
                .map(this::toField)
                .collect(Collectors.toList());
    }

    public Field toField(Element element) {
        if (!ElementKind.FIELD.equals(element.getKind()))
            throw new IllegalArgumentException("Element of kind Parameter expected, got " + element.getKind());

        Field field = new Field();
        field.setTypeName(extractTypeName(element.asType()));
        field.setName(element.getSimpleName().toString());
        return field;
    }

    private TypeName extractTypeName(TypeMirror typeMirror) {
        Element typeElement = getRequestModelClassElement(typeMirror);
        if (typeElement != null) return extractTypeName(typeElement);

        TypeName typeName = new TypeName();
        String canonicalName = typeMirror.toString();
        typeName.setPackageName(ClassNameUtil.extractPackageName(canonicalName));
        typeName.setName(extractClassName(canonicalName));
        return typeName;
    }

    private Element getRequestModelClassElement(TypeMirror typeMirror) {
        return requestModelClasses.stream()
                .filter(it -> typeMirror.equals(it.asType()))
                .findFirst()
                .orElse(null);
    }

    private TypeName extractTypeName(Element element) {
        if (!ElementKind.CLASS.equals(element.getKind()))
            throw new IllegalArgumentException("Element of kind Class expected, got " + element.getKind());

        RequestEntity annotation = element.getAnnotation(RequestEntity.class);
        String prefix = annotation != null ? annotation.prefix() : "";
        String postfix = annotation != null ? annotation.postfix() : "";

        TypeName typeName = new TypeName();
        typeName.setPackageName(extractPackageName((TypeElement) element));
        typeName.setName(prefix + element.getSimpleName() + postfix);
        return typeName;
    }


    private Set<? extends Element> getRequestParameters(Element targetClass) {
        Set<? extends Element> fields = filterByKind(targetClass.getEnclosedElements(), ElementKind.FIELD);
        return filterByAnnotation(fields, RequestField.class);
    }
}