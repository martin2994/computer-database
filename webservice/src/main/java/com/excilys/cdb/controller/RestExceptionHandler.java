package com.excilys.cdb.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.excilys.cdb.exceptions.AuthentificationException;
import com.excilys.cdb.exceptions.InvalidIdException;
import com.excilys.cdb.exceptions.NoObjectException;
import com.excilys.cdb.exceptions.company.InvalidCompanyException;
import com.excilys.cdb.exceptions.computer.InvalidComputerException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(value = { NoObjectException.class, InvalidIdException.class })
	protected ResponseEntity<Object> handleNotFound(Exception exception, WebRequest request) {
		String reponse = "Not found";
		System.out.println(exception.toString());
		return handleExceptionInternal(exception, reponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
	
	@ExceptionHandler(value = { InvalidComputerException.class , InvalidCompanyException.class, AuthentificationException.class })
	protected ResponseEntity<Object> handleNotAcceptable(Exception exception, WebRequest request) {
		String reponse = "Invalid informations";
		System.out.println(exception.toString());
		return handleExceptionInternal(exception, reponse, new HttpHeaders(), HttpStatus.NOT_ACCEPTABLE, request);
	}
}
