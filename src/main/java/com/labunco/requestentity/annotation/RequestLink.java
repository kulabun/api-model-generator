package com.labunco.requestentity.annotation;

/**
 * Created by klabun on 3/26/17.
 */
public @interface RequestLink {
    String prefix() default "";
    String postfix() default "Link";
    String[] fields() default {"id"}; 
}
