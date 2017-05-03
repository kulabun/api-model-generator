package com.labunco.apimodelgenerator;

import com.labunco.apimodelgenerator.annotation.ApiField;
import com.squareup.javapoet.*;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
