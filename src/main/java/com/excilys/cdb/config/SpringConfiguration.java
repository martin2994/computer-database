package com.excilys.cdb.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "com.excilys.cdb.controller", "com.excilys.cdb.dao.impl", "com.excilys.cdb.services" })
public class SpringConfiguration implements WebMvcConfigurer {

    /**
     * LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringConfiguration.class);

    /**
     * Bean pour initialiser la connexion via Hikari.
     * @return la dataSource
     */
    @Bean
    public DataSource dataSource() {
        Properties prop = new Properties();
        try (InputStream input = ClassLoader.getSystemClassLoader().getResourceAsStream("config.properties")) {
            prop.load(input);
            Class.forName("com.mysql.jdbc.Driver");
        } catch (IOException e) {
            LOGGER.warn("PROBLEME DE CONNEXION A LA BD " + e.getMessage());
        } catch (ClassNotFoundException e) {
            LOGGER.warn("PROBLEME DE DRIVER MYSQL " + e.getMessage());
        }
        HikariConfig hikariConfig = new HikariConfig(prop);
        return new HikariDataSource(hikariConfig);
    }

    /**
     * Initialise le matching des vues.
     * @return Le viewResolver
     */
    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/pages/");
        viewResolver.setSuffix(".jsp");

        return viewResolver;
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("/");
    }

}
