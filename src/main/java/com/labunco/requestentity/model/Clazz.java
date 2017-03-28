package com.labunco.requestentity.model;

import java.util.Collections;
import java.util.List;

/**
 * @author kulabun
 */
public class Clazz {
    private TypeDeclared type;
    private List<Field> fields;
    private List<Clazz> innerClazzes;

    public TypeDeclared getType() {
        return type;
    }

    public void setType(TypeDeclared type) {
        this.type = type;
    }

    public List<Field> getFields() {
        return fields != null ? fields : Collections.emptyList();
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public List<Clazz> getInnerClazzes() {
        return innerClazzes;
    }

    public void setInnerClazzes(List<Clazz> innerClazzes) {
        this.innerClazzes = innerClazzes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Clazz clazz = (Clazz) o;

        if (type != null ? !type.equals(clazz.type) : clazz.type != null) return false;
        return fields != null ? fields.equals(clazz.fields) : clazz.fields == null;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (fields != null ? fields.hashCode() : 0);
        return result;
    }
}
