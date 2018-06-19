package com.excilys.cdb.security;

import java.io.Serializable;

public class JWTAuthentificationResponse implements Serializable {

	    private static final long serialVersionUID = 1250166508152483573L;

	    private final String token;

	    public JWTAuthentificationResponse(String token) {
	        this.token = token;
	    }

	    public String getToken() {
	        return this.token;
	}
}
