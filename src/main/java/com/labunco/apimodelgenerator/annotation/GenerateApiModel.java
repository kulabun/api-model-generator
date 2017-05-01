package com.labunco.apimodelgenerator.annotation;

import java.lang.annotation.*;

/**
 * @author kulabun
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@Documented
public @interface GenerateApiModel {
    String prefix() default "";
    String postfix() default "Model";
}
