package com.labunco.requestentity.service;

import com.labunco.requestentity.annotation.RequestEntity;
import com.labunco.requestentity.annotation.RequestField;
import com.labunco.requestentity.annotation.RequestLink;
import com.labunco.requestentity.model.*;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

import static com.labunco.requestentity.util.ElementUtil.*;

/**
 * @author kulabun
 */
public class EntityConversionService {

    public Clazz toClazz(TypeElement element) {
        Clazz clazz = new Clazz();
        clazz.setType((TypeDeclared) resolveType(element.asType()));
        clazz.setFields(extractFields(element));
        clazz.setInnerClazzes(extractLinkEntities(element));
        return clazz;
    }

    private List<Clazz> extractLinkEntities(TypeElement element) {
        return filterByAnnotation(getFields(element), RequestLink.class).stream()
                .map(this::createInnerClazz)
                .collect(Collectors.toList());
    }

    private Clazz createInnerClazz(Element requestLinkField) {
        TypeDeclared type = new TypeDeclared();
        TypeElement fieldTypeElement = (TypeElement) ((DeclaredType) requestLinkField.asType()).asElement();
        type.setPackageName(extractPackageName(fieldTypeElement));

        RequestLink annotation = requestLinkField.getAnnotation(RequestLink.class);
        TypeElement targetElement = fieldTypeElement;
        TypeDeclared fieldType = (TypeDeclared) resolveType(requestLinkField.asType(), new Context(annotation));
        if (!hasAnnotation(fieldTypeElement, RequestEntity.class)) {
            DeclaredType typeArgument = ((DeclaredType) requestLinkField.asType())
                    .getTypeArguments().stream()
                    .filter(it -> it instanceof DeclaredType)
                    .map(it -> (DeclaredType) it)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Failed to resolve inner class for " +
                            requestLinkField.getEnclosingElement().asType().toString() + "."
                            + requestLinkField.getSimpleName().toString()));

            fieldType = (TypeDeclared) resolveType(typeArgument, new Context(annotation));
            targetElement = (TypeElement) typeArgument.asElement();
        }

        Clazz resultClazz = new Clazz();
        resultClazz.setType(fieldType);
        List<String> fieldNames = Arrays.asList(annotation.fields());
        resultClazz.setFields(getFields(targetElement).stream()
                .filter(it -> fieldNames.contains(it.getSimpleName().toString()))
                .map(it -> toField((VariableElement) it))
                .collect(Collectors.toList()));
        resultClazz.setInnerClazzes(extractLinkEntities(targetElement));
        return resultClazz;
    }

    private Type toType(TypeVariable typeVariable, Context ctx) {
        TypeVar typeVar = new TypeVar();
        String typeName = typeVariable.asElement().getSimpleName().toString();
        typeVar.setName(typeName);

        if (ctx.existsInStack(typeVar)) {
            return typeVar;
        }

        if (!typeVariable.getLowerBound().getKind().equals(TypeKind.NULL)) {
            ctx.pushToStack(typeVar);
            typeVar.setLowerBound(resolveType(typeVariable.getLowerBound(), ctx));
        }

        if (!typeVariable.getUpperBound().getKind().equals(TypeKind.NULL)) {
            ctx.pushToStack(typeVar);
            typeVar.setUpperBound(resolveType(typeVariable.getUpperBound(), ctx));
        }

        return typeVar;
    }

    private List<Field> extractFields(Element element) {
        return getFields(element)
                .stream()
                .filter(it -> hasAnnotation(it, RequestField.class)
                        || hasAnnotation(it, RequestLink.class))
                .map(it -> toField((VariableElement) it))
                .collect(Collectors.toList());
    }

    public Field toField(VariableElement element) {
        RequestLink annotation = element.getAnnotation(RequestLink.class);

        Field field = new Field();
        if (annotation != null) {
            field.setType(resolveType(element.asType(), new Context(annotation)));
        } else {
            field.setType(resolveType(element.asType()));
        }
        field.setName(element.getSimpleName().toString());
        return field;
    }

    private Type resolveType(TypeMirror type) {
        return resolveType(type, new Context());
    }

    private Type resolveType(TypeMirror type, Context ctx) {
        if (type instanceof DeclaredType)
            return toType((DeclaredType) type, ctx);
        else if (type instanceof TypeVariable) {
            return toType((TypeVariable) type, ctx);
        }
        throw new RuntimeException("Unknown type:" + type.getClass().getCanonicalName()
                + " ,kind: " + type.getKind());
    }

    private TypeDeclared toType(DeclaredType declaredType, Context ctx) {
        TypeElement typeElement = (TypeElement) declaredType.asElement();

        String typeName = typeElement.getSimpleName().toString();
        if (hasAnnotation(typeElement, RequestEntity.class))
            typeName = ctx.getAnnotation(RequestLink.class)
                    .map(it -> it.prefix() + typeElement.getSimpleName() + it.postfix())
                    .orElseGet(() -> {
                        RequestEntity annotation = typeElement.getAnnotation(RequestEntity.class);
                        String prefix = annotation != null ? annotation.prefix() : "";
                        String postfix = annotation != null ? annotation.postfix() : "";

                        return prefix + typeElement.getSimpleName() + postfix;
                    });

        TypeDeclared typeDeclared = new TypeDeclared();
        typeDeclared.setPackageName(extractPackageName(typeElement));
        typeDeclared.setName(typeName);
        typeDeclared.setTypeArgs(extractTypeArguments(declaredType, typeDeclared, ctx));
        return typeDeclared;
    }

    private List<Type> extractTypeArguments(DeclaredType declaredType,
                                            TypeDeclared typeDeclared,
                                            Context ctx) {
        return declaredType.getTypeArguments().stream()
                .map(it -> it.equals(declaredType) ? typeDeclared : resolveType(it, ctx))
                .collect(Collectors.toList());
    }

    private Set<? extends Element> getFields(Element targetClass) {
        return filterByKind(targetClass.getEnclosedElements(), ElementKind.FIELD);
    }

    private static class Context {
        private List<String> stack;
        private List<Annotation> annotations;

        public Context(Annotation... annotations) {
            this.annotations = annotations.length > 0 ? Arrays.asList(annotations) : Collections.emptyList();
            this.stack = new ArrayList<>();
        }

        public boolean existsInStack(Type type) {
            return stack.contains(type.getQualifiedName());
        }

        public void pushToStack(Type type) {
            stack.add(type.getQualifiedName());
        }

        public <T extends Annotation> Optional<T> getAnnotation(Class<T> annotation) {
            return (Optional<T>) annotations.stream()
                    .filter(it -> it.annotationType().equals(annotation))
                    .findFirst();
        }
    }
}