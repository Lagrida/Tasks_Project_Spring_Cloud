package com.lagrida.services;

import java.time.LocalDateTime;

public class RestExceptionResponse {
	private String title;
	private int errorNumber;
	private LocalDateTime date;
    private String message;
    private String additionalMessage;
    
	public RestExceptionResponse() {
		
	}
	public RestExceptionResponse(String title, int errorNumber, LocalDateTime date, String message, String additionalMessage) {
		this.title = title;
		this.errorNumber = errorNumber;
		this.date = date;
		this.message = message;
		this.additionalMessage = additionalMessage;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getErrorNumber() {
		return errorNumber;
	}
	public void setErrorNumber(int errorNumber) {
		this.errorNumber = errorNumber;
	}
	public LocalDateTime getDate() {
		return date;
	}
	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getAdditionalMessage() {
		return additionalMessage;
	}
	public void setAdditionalMessage(String additionalMessage) {
		this.additionalMessage = additionalMessage;
	}
}
