package com.labunco.requestentity.model;

/**
 * @author kulabun
 * @since 3/25/17
 */
public class Field {
    private TypeName typeName;
    private String name;

    public TypeName getTypeName() {
        return typeName;
    }

    public void setTypeName(TypeName typeName) {
        this.typeName = typeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Field field = (Field) o;

        if (typeName != null ? !typeName.equals(field.typeName) : field.typeName != null) return false;
        return name != null ? name.equals(field.name) : field.name == null;
    }

    @Override
    public int hashCode() {
        int result = typeName != null ? typeName.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
