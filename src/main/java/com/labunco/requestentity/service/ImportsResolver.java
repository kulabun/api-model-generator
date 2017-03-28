package com.labunco.requestentity.service;

import com.labunco.requestentity.model.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author kulabun
 */
public class ImportsResolver {

    public List<String> resolve(Clazz clazz) {
        Set<String> classes = new HashSet<>();
        classes.add(clazz.getType().getQualifiedName());
        clazz.getFields()
                .stream()
                .map(Field::getType)
                .forEach(it -> fillClasses(it, classes));

        String currentPackage = clazz.getType().getPackageName();
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

        if (type instanceof TypeDeclared)
            fillClasses((TypeDeclared) type, classes);
        else if (type instanceof TypeVar) {
            fillClasses((TypeVar) type, classes);
        } else throw new IllegalArgumentException("Unknown subtype of Type: " + type.getClass().getCanonicalName());
    }

    private static void fillClasses(TypeVar type, Set<String> classes) {
        if (type.getLowerBound() != null)
            fillClasses(type.getLowerBound(), classes);
        if (type.getUpperBound() != null)
            fillClasses(type.getUpperBound(), classes);
    }

    private static void fillClasses(TypeDeclared type, Set<String> classes) {
        classes.add(type.getQualifiedName());
        type.getTypeArgs().forEach(it -> fillClasses(it, classes));
    }
}
