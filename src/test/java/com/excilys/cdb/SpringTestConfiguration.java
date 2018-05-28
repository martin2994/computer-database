package com.excilys.cdb;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ComponentScan(basePackages = {"com.excilys.cdb.dao.impl", "com.excilys.cdb.services" })
public class SpringTestConfiguration {
    /**
     * LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringTestConfiguration.class);

    /**
     * .
     * @return .
     */
    @Bean
    public DataSource dataSource() {
        Properties prop = new Properties();
        try (InputStream input = ClassLoader.getSystemClassLoader().getResourceAsStream("config.properties")) {
            prop.load(input);
            LOGGER.info("Base de donnée utilisée : " + prop.getProperty("jdbcUrl"));
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
     * Bean to automatically load changes in the source properties file.
     * @return  ReloadableResourceBundleMessageSource
     */
    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    /**
     * Write a cookie with the user locale.
     * @return  CookieLocaleResolver
     */
    @Bean
    public CookieLocaleResolver localeResolver() {
        CookieLocaleResolver localeResolver = new CookieLocaleResolver();
        localeResolver.setDefaultLocale(Locale.ENGLISH);
        localeResolver.setCookieName("my-locale-cookie");
        localeResolver.setCookieMaxAge(3600);
        return localeResolver;
    }

    /**
     * Allows to change the current locale on every request via a lang request parameter.
     * @return LocaleChangeInterceptor
     */
    @Bean
    public LocaleChangeInterceptor localeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        return interceptor;
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


}
