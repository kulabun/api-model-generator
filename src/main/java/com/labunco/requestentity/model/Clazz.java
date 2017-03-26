package com.labunco.requestentity.model;

import java.util.Collections;
import java.util.List;

/**
 * @author kulabun
 * @since 3/25/17
 */
public class Clazz {
    private TypeDeclared name;
    private List<Field> fields;

    public TypeDeclared getName() {
        return name;
    }

    public void setName(TypeDeclared name) {
        this.name = name;
    }

    public List<Field> getFields() {
        return fields != null ? fields : Collections.emptyList();
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Clazz clazz = (Clazz) o;

        if (name != null ? !name.equals(clazz.name) : clazz.name != null) return false;
        return fields != null ? fields.equals(clazz.fields) : clazz.fields == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (fields != null ? fields.hashCode() : 0);
        return result;
    }
}
