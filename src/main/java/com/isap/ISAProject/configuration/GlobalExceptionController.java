package com.isap.ISAProject.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@ExceptionHandler(value = {ResponseStatusException.class, DataIntegrityViolationException.class})
	public ResponseEntity<?> handleException(Exception ex) {
		logger.error("Caught exception : " + ex.getMessage());
		if(ex instanceof ResponseStatusException) {
			if(((ResponseStatusException) ex).getStatus().equals(HttpStatus.NOT_FOUND)) return ResponseEntity.notFound().header("message", ((ResponseStatusException) ex).getReason()).build();
			if(((ResponseStatusException) ex).getStatus().equals(HttpStatus.NO_CONTENT)) return ResponseEntity.noContent().header("message", ((ResponseStatusException) ex).getReason()).build();
		}
		if(ex instanceof DataIntegrityViolationException) return ResponseEntity.badRequest().header("message", "Value already exists.").build();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
}
