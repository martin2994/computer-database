package com.excilys.cdb.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class SpringMain extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { SpringConfigurationWeb.class, WebSecurityConfig.class };
	}

	// Load spring web configuration
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] { SpringConfigurationWeb.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}
}