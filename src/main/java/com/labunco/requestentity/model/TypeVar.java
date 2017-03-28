package com.labunco.requestentity.model;

import java.util.Collections;
import java.util.List;

/**
 * @author kulabun
 */
public class TypeVar implements Type {
    private String name;
    private Type upperBound;
    private Type lowerBound;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getQualifiedName() {
        return name;
    }

    @Override
    public String asString() {
        return name;
    }

    @Override
    public String asStringWithBounds() {
        if (upperBound != null) {
            return name + " extends " + upperBound.asStringWithBounds();
        } else if (lowerBound != null) {
            return name + " super " + upperBound.asStringWithBounds();
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Type> getTypeArgs() {
        return Collections.emptyList();
    }

    public Type getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(Type upperBound) {
        this.upperBound = upperBound;
    }

    public Type getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(Type lowerBound) {
        this.lowerBound = lowerBound;
    }
}
