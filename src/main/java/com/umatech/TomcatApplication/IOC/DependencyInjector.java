package com.umatech.TomcatApplication.IOC;

import com.umatech.TomcatApplication.annotation.Resource;

import java.lang.reflect.Field;


public class DependencyInjector {
    public void injectDependencies(BeanFactory beanFactory) throws IllegalAccessException {
        for (Class<?> clazz : beanFactory.getBeans().keySet()) {
            Object bean = beanFactory.getBean(clazz);

            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Resource.class)) {
                    Object resourceBean = beanFactory.getBean(field.getType());
                    if (resourceBean != null) {
                        field.setAccessible(true);
                        field.set(bean, resourceBean);
                    }
                }
            }
        }
    }
}
