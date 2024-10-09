package com.umatech.TomcatApplication.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// To label for a injectable component
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {
}

