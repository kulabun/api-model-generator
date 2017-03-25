package com.labunco.requestentity.annotation;

import java.lang.annotation.*;

/**
 * @author kulabun
 * @since 3/16/17
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@Documented
public @interface RequestEntity {
    String prefix() default "";
    String postfix() default "Request";
}
