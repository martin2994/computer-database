package com.excilys.cdb.configpersistence;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = { "com.excilys.cdb.dao.impl" })
public class SpringConfigDB {
	
    
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
