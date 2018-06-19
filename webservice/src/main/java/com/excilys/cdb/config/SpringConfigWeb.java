package com.excilys.cdb.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.excilys.cdb.configpersistence", "com.excilys.cdb.controller", "com.excilys.cdb.services", "com.excilys.cdb.config", "com.excilys.cdb.security" })
public class SpringConfigWeb implements WebMvcConfigurer {


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("OPTIONS", "GET", "PUT", "POST", "DELETE")
            .allowCredentials(true).maxAge(3600);
    }
    
}