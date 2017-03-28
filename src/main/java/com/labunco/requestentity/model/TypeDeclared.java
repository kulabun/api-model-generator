package com.labunco.requestentity.model;

import org.apache.commons.collections.CollectionUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author kulabun
 */
public class TypeDeclared implements Type {
    private String packageName;
    private String name;
    private List<Type> typeArgs;

    public TypeDeclared() {
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getQualifiedName() {
        return packageName + "." + name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Type> getTypeArgs() {
        return typeArgs != null ? typeArgs : Collections.emptyList();
    }

    public void setTypeArgs(List<Type> typeArgs) {
        this.typeArgs = typeArgs;
    }

    public String asString() {
        if (CollectionUtils.isEmpty(getTypeArgs())) return getName();
        return getTypeArgs().stream()
                .map(Type::asString)
                .collect(Collectors.joining(",", getName() + "<", ">"));
    }

    @Override
    public String asStringWithBounds() {
        if (CollectionUtils.isEmpty(getTypeArgs())) return getName();
        return getTypeArgs().stream()
                .map(Type::asStringWithBounds)
                .collect(Collectors.joining(",", getName() + "<", ">"));

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TypeDeclared that = (TypeDeclared) o;

        if (packageName != null ? !packageName.equals(that.packageName) : that.packageName != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return typeArgs != null ? typeArgs.equals(that.typeArgs) : that.typeArgs == null;
    }

    @Override
    public int hashCode() {
        int result = packageName != null ? packageName.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (typeArgs != null ? typeArgs.hashCode() : 0);
        return result;
    }
}
