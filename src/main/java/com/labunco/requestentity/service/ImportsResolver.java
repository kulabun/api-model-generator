package com.labunco.requestentity.service;

import com.labunco.requestentity.model.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author kulabun
 * @since 3/26/17
 */
public class ImportsResolver {
    
    public List<String> resolve(Clazz clazz) {
        Set<String> classes = new HashSet<>();
        classes.add(clazz.getName().getQualifiedName());
        clazz.getFields()
                .stream()
                .map(Field::getType)
                .forEach(it -> fillClasses(it, classes));

        String currentPackage = clazz.getName().getPackageName();
        List<String> ignored = Arrays.asList(currentPackage, "java.lang");

        return classes.stream()
                .filter(it -> !ignored.contains(extractPackageName(it)))
                .collect(Collectors.toList());
    }

    private String extractPackageName(String canonicalName) {
        int packageNameEndIndex = canonicalName.lastIndexOf(".");
        return packageNameEndIndex != -1 ?
                canonicalName.substring(0, packageNameEndIndex) :
                "";
    }

    private static void fillClasses(Type type, Set<String> classes) {
        if (classes.contains(type.getQualifiedName())) return;

        if (type instanceof TypeDeclared) {
            TypeDeclared typeDeclared = (TypeDeclared) type;
            classes.add(typeDeclared.getQualifiedName());
            typeDeclared.getTypeArgs().forEach(it -> fillClasses(it, classes));
        } else if (type instanceof TypeVar) {
            TypeVar typeVar = (TypeVar) type;
            if (typeVar.getLowerBound() != null)
                fillClasses(typeVar.getLowerBound(), classes);
            if (typeVar.getUpperBound() != null)
                fillClasses(typeVar.getUpperBound(), classes);
        } else {
            throw new IllegalArgumentException("Unknown subtype of Type: " + type.getClass().getCanonicalName());
        }
    }
}
