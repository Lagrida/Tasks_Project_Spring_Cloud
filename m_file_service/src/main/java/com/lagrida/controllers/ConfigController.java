package com.lagrida.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.lagrida.services.RestException;
import com.lagrida.services.RestExceptionResponse;

@ControllerAdvice
@RestController
public class ConfigController extends ResponseEntityExceptionHandler{
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		HttpStatus status2 = HttpStatus.BAD_REQUEST;
		
		Set<Map<String, Object>> errors2 = new HashSet<>();
		Map<String, Object> errors = new HashMap<>();
		Map<String, Object> error;
		
    	List<FieldError> errorList = ex.getBindingResult().getFieldErrors();
    	
		for(FieldError fieldError : errorList) {
			error = new HashMap<>();
			error.put("source", fieldError.getField());
			error.put("message", fieldError.getDefaultMessage());
			errors2.add(error);
		}
		errors.put("status", status2.value());
		errors.put("error", status2.name());
		errors.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
		errors.put("errors", errors2);
		return handleExceptionInternal(ex, errors,
			new HttpHeaders(), status2, request);
	}
    @ExceptionHandler(RestException.class)
    public ResponseEntity<RestExceptionResponse> handleSecurityException(RestException se) {
    	HttpStatus httpStatus = se.getHttpStatus();
    	RestExceptionResponse response = new RestExceptionResponse(httpStatus.name(), httpStatus.value(), LocalDateTime.now(), se.getMessage(), se.getAdditionalMessage());
        return new ResponseEntity<RestExceptionResponse>(response, httpStatus);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<RestExceptionResponse> handleAccessDeniedException(AccessDeniedException se) {
    	HttpStatus httpStatus = HttpStatus.FORBIDDEN;
    	RestExceptionResponse response = new RestExceptionResponse(httpStatus.name(), httpStatus.value(), LocalDateTime.now(), se.getMessage(), "");
        return new ResponseEntity<RestExceptionResponse>(response, httpStatus);
    }
    @ExceptionHandler(SizeLimitExceededException.class)
    public ResponseEntity<RestExceptionResponse> handleSizeLimitExceededException(SizeLimitExceededException se) {
    	HttpStatus httpStatus = HttpStatus.FORBIDDEN;
    	RestExceptionResponse response = new RestExceptionResponse(httpStatus.name(), httpStatus.value(), LocalDateTime.now(), se.getMessage(), "");
        return new ResponseEntity<RestExceptionResponse>(response, httpStatus);
    }
}
