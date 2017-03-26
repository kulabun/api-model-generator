package com.labunco.requestentity.service;

import com.labunco.requestentity.annotation.RequestEntity;
import com.labunco.requestentity.annotation.RequestField;
import com.labunco.requestentity.model.*;
import org.apache.commons.collections.CollectionUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import java.util.*;
import java.util.stream.Collectors;

import static com.labunco.requestentity.util.ElementUtil.*;

/**
 * @author kulabun
 * @since 3/25/17
 */
public class EntityConversionService {
    public Clazz toClazz(TypeElement element) {
        Clazz clazz = new Clazz();
        TypeDeclared typeDeclared = (TypeDeclared) toType(element);
        clazz.setName(typeDeclared);

        List<Field> fields = extractTypeFields(element);
        clazz.setFields(fields);
        return clazz;
    }

    private Type toType(TypeVariable typeVariable) {
        return toType(typeVariable, new LinkedList<>());
    }

    private Type toType(TypeVariable typeVariable, Deque<Type> stack) {
        TypeVar typeVar = new TypeVar();
        String typeName = typeVariable.asElement().getSimpleName().toString();
        typeVar.setName(typeName);

        if (stack.stream().anyMatch(it -> it.getQualifiedName().equals(typeName))) {
            return typeVar;
        }

        if (!typeVariable.getLowerBound().getKind().equals(TypeKind.NULL)) {
            stack.add(typeVar);
            typeVar.setLowerBound(resolveType(typeVariable.getLowerBound(), stack));
        }

        if (!typeVariable.getUpperBound().getKind().equals(TypeKind.NULL)) {
            stack.add(typeVar);
            typeVar.setUpperBound(resolveType(typeVariable.getUpperBound(), stack));
        }

        return typeVar;
    }

    private List<Field> extractTypeFields(Element element) {
        return getRequestParameters(element)
                .stream()
                .map(this::toField)
                .collect(Collectors.toList());
    }

    public Field toField(Element element) {
        if (!element.getKind().isField())
            throw new IllegalArgumentException("Element of kind Field expected, got " + element.getKind());

        Field field = new Field();
        field.setType(toType(element));
        field.setName(element.getSimpleName().toString());
        return field;
    }


    private Type toType(Element element) {
        if (element.asType() instanceof DeclaredType) {
            return toType((DeclaredType) element.asType());
        } else if (element.asType() instanceof TypeVariable) {
            return toType((TypeVariable) element.asType());
        }
        throw new RuntimeException("Unknown type:" + element.getClass().getCanonicalName()
                + " ,kind: " + element.getKind());
    }

    private Type toType(DeclaredType declaredType) {
        return toType(declaredType, new LinkedList<>());
    }

    private TypeDeclared toType(DeclaredType declaredType, Deque<Type> stack) {
        TypeElement typeElement = (TypeElement) declaredType.asElement();

        RequestEntity annotation = typeElement.getAnnotation(RequestEntity.class);
        String prefix = annotation != null ? annotation.prefix() : "";
        String postfix = annotation != null ? annotation.postfix() : "";

        TypeDeclared typeDeclared = new TypeDeclared();
        typeDeclared.setPackageName(extractPackageName(typeElement));
        typeDeclared.setName(prefix + typeElement.getSimpleName() + postfix);
        typeDeclared.setTypeArgs(extractTypeArguments(declaredType, typeDeclared, stack));
        return typeDeclared;
    }

    private List<Type> extractTypeArguments(DeclaredType declaredType,
                                            TypeDeclared typeDeclared,
                                            Deque<Type> stack) {
        List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();

        return CollectionUtils.isNotEmpty(typeArguments) ?
                typeArguments.stream()
                        .map(it -> it.equals(declaredType) ? typeDeclared : resolveType(it, stack))
                        .collect(Collectors.toList()) :
                Collections.emptyList();
    }

    private Type resolveType(TypeMirror type, Deque<Type> stack) {
        if (type instanceof DeclaredType)
            return toType((DeclaredType) type, stack);
        else if (type instanceof TypeVariable) {
            TypeVariable typeVar = (TypeVariable) type;
            return toType(typeVar, stack);
        } else {
            throw new RuntimeException("Unknown type:" + type.getClass().getCanonicalName()
                    + " ,kind: " + type.getKind());
        }
    }

    private Set<? extends Element> getRequestParameters(Element targetClass) {
        Set<? extends Element> fields = filterByKind(targetClass.getEnclosedElements(), ElementKind.FIELD);
        return filterByAnnotation(fields, RequestField.class);
    }
}