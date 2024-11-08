package com.umatech.TomcatApplication;

import com.umatech.Controller.HelloController;
import com.umatech.DB.DatabaseManager;
import com.umatech.Service.TestService;
import com.umatech.Service.TestService2;
import com.umatech.TomcatApplication.IOC.BeanFactory;
import com.umatech.TomcatApplication.IOC.ClassScanner;
import com.umatech.TomcatApplication.IOC.DependencyInjector;
import com.umatech.TomcatApplication.annotation.RequestMapping;
import com.umatech.TomcatApplication.annotation.RestController;
import com.umatech.TomcatApplication.annotation.TomcatApplication;
import jakarta.servlet.Servlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.Context;

import org.apache.catalina.startup.Tomcat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;


import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Set;

public class TomcatApplicationRunner {

    private static final Logger logger = LogManager.getLogger(TomcatApplicationRunner.class);

    public static void run(Class<?> starterClass, String[] args) throws Exception {

        if (! starterClass.isAnnotationPresent(TomcatApplication.class)){
            throw new RuntimeException("Starter class must have TomcatApplication annotation");
        }
        String packageName = starterClass.getPackageName();


        BeanFactory beanFactory = new BeanFactory();
        beanFactory.init(packageName);

        DependencyInjector dependencyInjector = new DependencyInjector();
        dependencyInjector.injectDependencies(beanFactory);

//        DatabaseManager dbm = (DatabaseManager) beanFactory.getBean(DatabaseManager.class);
//        logger.info(dbm.getConnection());
//
//        HelloController hc = (HelloController) beanFactory.getBean(HelloController.class);
//        logger.info(hc.databaseManager);


        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);

        String contextPath = "";
        String docBase = System.getProperty("java.io.tmpdir");


        Context ctx = tomcat.addContext(contextPath, docBase);


        scanServlet(ctx,packageName);

        scanControllers(ctx, packageName, beanFactory);


        tomcat.getConnector();

        logger.info("Starting embedded Tomcat server...");

        tomcat.start();
        tomcat.getServer().await();
    }

    private static void scanServlet(Context ctx, String packageName) throws Exception {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> servletClasses = reflections.getTypesAnnotatedWith(WebServlet.class);

        for (Class<?> servletClass : servletClasses) {

            if (Servlet.class.isAssignableFrom(servletClass)) {
                WebServlet webServlet = servletClass.getAnnotation(WebServlet.class);
                String servletName = webServlet.name().isEmpty() ? servletClass.getSimpleName() : webServlet.name();
                String[] urlPatterns = webServlet.urlPatterns().length > 0 ? webServlet.urlPatterns() : webServlet.value();


                Tomcat.addServlet(ctx, servletName, (Servlet) servletClass.getDeclaredConstructor().newInstance());
                for (String urlPattern : urlPatterns) {
                    ctx.addServletMappingDecoded(urlPattern, servletName);
                }
            }
        }
    }

    private static void scanControllers(Context ctx, String packageName, BeanFactory beanFactory) throws Exception {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(RestController.class);
        for (Class<?> controller: controllers) {
            RestController restController = controller.getAnnotation(RestController.class);
            String baseURL = restController.value();

            Object controllerInstance = beanFactory.getBean(controller);

            for (Method method: controller.getDeclaredMethods()){
                if (method.isAnnotationPresent(RequestMapping.class)) {
                   RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                   String requestURL = requestMapping.value();
                   String requestMethod = requestMapping.method();

                   String fullURL = baseURL+requestURL;

                   String servletName = controller.getName() + "_" + method.getName();
                   Tomcat.addServlet(ctx, servletName, new HttpServlet() {



                       @Override
                       public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
                           try {
                               if (!req.getMethod().equalsIgnoreCase(requestMethod)) {
                                   resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Method Not Allowed");
                                   return;
                               }

                               Object result = method.invoke(controllerInstance);
                               resp.getWriter().write(result.toString());
                           } catch (Exception e) {
                               logger.error(e.getMessage());
                               resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                           }
                       }
                   });
                    ctx.addServletMappingDecoded(fullURL, servletName);

                }
            }
        }
    }
}
