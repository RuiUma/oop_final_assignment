package com.umatech.TomcatApplication.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
    String value() default "";  // URL of the method
    String method() default "POST";  // Request method, "POST" by default
}