package com.labunco.requestentity.model;

import java.util.List;

/**
 * @author kulabun
 * @since 3/25/17
 */
public class Type {
    private TypeName name;
    private List<TypeName> imports;
    private List<Field> fields;


    public TypeName getName() {
        return name;
    }

    public void setName(TypeName name) {
        this.name = name;
    }

    public List<TypeName> getImports() {
        return imports;
    }

    public void setImports(List<TypeName> imports) {
        this.imports = imports;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Type type = (Type) o;

        if (name != null ? !name.equals(type.name) : type.name != null) return false;
        if (imports != null ? !imports.equals(type.imports) : type.imports != null) return false;
        return fields != null ? fields.equals(type.fields) : type.fields == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (imports != null ? imports.hashCode() : 0);
        result = 31 * result + (fields != null ? fields.hashCode() : 0);
        return result;
    }
}
