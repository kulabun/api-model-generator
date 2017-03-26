package com.labunco.requestentity.model;

/**
 * @author kulabun
 * @since 3/25/17
 */
public class Field {
    private Type type;
    private String name;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
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

        if (type != null ? !type.equals(field.type) : field.type != null) return false;
        return name != null ? name.equals(field.name) : field.name == null;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
