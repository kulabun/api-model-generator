package com.labunco.apimodelgenerator.annotation;

import java.lang.annotation.*;

/**
 * @author kulabun
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
@Documented
public @interface ApiField {
    Type type() default Type.Value;
    Model referenceModel() default @Model(prefix = "", postfix = "", fields = {});
    
    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.TYPE)
    @Documented
    @interface Model {
        String prefix() default "";
        String postfix() default "Model";
        String[] fields();
    }
    
    enum Type {Value, Reference}
}
