package com.umatech.TomcatApplication.IOC;

import com.umatech.TomcatApplication.annotation.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;


public class DependencyInjector {

    private final Logger logger = LogManager.getLogger(this.getClass());


    public void injectDependencies(BeanFactory beanFactory) throws IllegalAccessException {
        logger.info("Dependency injection start.");
        for (Class<?> clazz : beanFactory.getBeans().keySet()) {
            Object bean = beanFactory.getBean(clazz);


            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Resource.class)) {
                    Object resourceBean = beanFactory.getBean(field.getType());
                    if (resourceBean != null) {
                        field.setAccessible(true);
                        field.set(bean, resourceBean);
                        logger.info("class: " + bean.getClass().getName() + ", Field: " + field.getName());
                    }
                }
            }
        }
    }
}
