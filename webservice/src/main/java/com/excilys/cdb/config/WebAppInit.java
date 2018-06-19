package com.excilys.cdb.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebAppInit implements WebApplicationInitializer {

    private static final String SERVLET_NAME = "dispatcher";
    private static final String DISPATCHER_START_MAPPING = "/";

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(SpringConfigWeb.class);
        context.register(SecurityWebApplicationInit.class);

        ServletRegistration.Dynamic servlet = servletContext.addServlet(SERVLET_NAME, new DispatcherServlet(context));
        servlet.setLoadOnStartup(1);
        servlet.addMapping(DISPATCHER_START_MAPPING);
    }

}