package com.umatech.TomcatApplication.AutoScan;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RestController {
    String value();
}