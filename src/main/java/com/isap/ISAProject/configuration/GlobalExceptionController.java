package com.isap.ISAProject.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@ExceptionHandler(value = ResponseStatusException.class)
	public ResponseEntity<String> handleException(ResponseStatusException ex) {
		logger.error("Caught exception : " + ex.getMessage());
		ResponseEntity<String> response = new ResponseEntity<String>(ex.getStatus());
		return response;
	}
	
}
