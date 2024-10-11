package com.umatech.TomcatApplication.IOC;
import com.umatech.TomcatApplication.TomcatApplicationRunner;
import com.umatech.TomcatApplication.annotation.Component;
import com.umatech.TomcatApplication.annotation.RestController;
import com.umatech.TomcatApplication.annotation.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class BeanFactory {

    private final Map<Class<?>, Object> beanMap = new HashMap<>();
    private final Logger logger = LogManager.getLogger(this.getClass());


    public void init(String packageName) throws Exception {
        ClassScanner scanner = new ClassScanner(packageName);

        Set<Class<?>> controllerClasses = scanner.getClassesWithAnnotation(RestController.class);

        Set<Class<?>> serviceClasses = scanner.getClassesWithAnnotation(Service.class);

        Set<Class<?>> componentClasses = scanner.getClassesWithAnnotation(Component.class);

        for (Class<?> clazz : controllerClasses) {
            Object instance = clazz.getDeclaredConstructor().newInstance();
            beanMap.put(clazz, instance);
        }

        for (Class<?> clazz : serviceClasses) {
            Object instance = clazz.getDeclaredConstructor().newInstance();
            beanMap.put(clazz, instance);
        }

        for (Class<?> clazz : componentClasses) {
            Object instance = clazz.getDeclaredConstructor().newInstance();
            beanMap.put(clazz, instance);
        }

    }

    public Object getBean(Class<?> clazz) {
        return beanMap.get(clazz);
    }

    public Map<Class<?>, Object> getBeans() {
        return beanMap;
    }

}
