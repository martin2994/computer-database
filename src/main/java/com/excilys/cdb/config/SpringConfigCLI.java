package com.excilys.cdb.config;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan(basePackages = { "com.excilys.cdb.dao.impl", "com.excilys.cdb.services" })
public class SpringConfigCLI implements WebMvcConfigurer {

    /**
     * Permet d'injecter la sessionFacotry pour les DAO.
     * @return la sessionFactory
     */
    @Bean
    public SessionFactory sessionFactory() {
        SessionFactory sessionFactory = new org.hibernate.cfg.Configuration().configure().buildSessionFactory();
        return sessionFactory;
    }
}
