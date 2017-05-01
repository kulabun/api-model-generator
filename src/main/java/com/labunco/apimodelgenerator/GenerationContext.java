package com.labunco.apimodelgenerator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;

/**
 * @author kulabun
 */
public class GenerationContext {
    private ClassName className;
    private TypeSpec.Builder typeSpec;

    public GenerationContext(ClassName className, TypeSpec.Builder typeSpec) {
        this.className = className;
        this.typeSpec = typeSpec;
    }

    public TypeSpec.Builder getTypeSpec() {
        return typeSpec;
    }

    public ClassName getClassName() {
        return className;
    }

    public String getClassSimpleName() {
        return className.simpleName();
    }

    public String getClassPackageName() {
        return className.packageName();
    }
}
