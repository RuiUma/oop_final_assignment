package com.umatech.TomcatApplication.IOC;


import org.reflections.Reflections;

import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.FilterBuilder;

import java.lang.annotation.Annotation;
import java.util.Set;

public class ClassScanner {

    private Reflections reflections;

    public ClassScanner(String packageName) {
        // 创建 Reflections 配置，指定要扫描的包
        reflections = new Reflections(
                new ConfigurationBuilder()
                        .setUrls(ClasspathHelper.forPackage(packageName))
                        .filterInputsBy(new FilterBuilder().includePackage(packageName))
                        .setScanners(Scanners.TypesAnnotated)
        );
    }

    public Set<Class<?>> getClassesWithAnnotation(Class<?> annotation) {
        // 查找带有特定注解的类
        return reflections.getTypesAnnotatedWith((Class<? extends Annotation>) annotation);
    }
}