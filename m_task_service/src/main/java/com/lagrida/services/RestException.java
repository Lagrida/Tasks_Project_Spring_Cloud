package com.lagrida.services;

import org.springframework.http.HttpStatus;

public class RestException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	private String message;
	private HttpStatus httpStatus;
	public RestException() {
		
	}
	public RestException(String message, HttpStatus httpStatus) {
		super();
		this.message = message;
		this.httpStatus = httpStatus;
	}

	public RestException(String message) {
		super(message);
		this.message = message;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}
}
