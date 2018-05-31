package com.excilys.cdb;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.excilys.cdb.dao.impl" })
public class SpringTestConfigurationDAO {
    /**
     * LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringTestConfigurationDAO.class);

    /**
     * Permet d'injecter la sessionFacotry pour les DAO.
     * @return la sessionFactory
     */
    @Bean
    public SessionFactory sessionFactory() {
        SessionFactory sessionFactory = new org.hibernate.cfg.Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        LOGGER.debug(sessionFactory.getProperties().toString());
        return sessionFactory;
    }

}
