package com.labunco.requestentity.model;

/**
 * @author kulabun
 * @since 3/25/17
 */
public class TypeName {
    private String packageName;
    private String name;

    public TypeName() {
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

    public void setName(String name) {
        this.name = name;
    }

    public String getCanonicalName() {
        return packageName + "." + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TypeName typeName = (TypeName) o;

        if (packageName != null ? !packageName.equals(typeName.packageName) : typeName.packageName != null)
            return false;
        return name != null ? name.equals(typeName.name) : typeName.name == null;
    }

    @Override
    public int hashCode() {
        int result = packageName != null ? packageName.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
