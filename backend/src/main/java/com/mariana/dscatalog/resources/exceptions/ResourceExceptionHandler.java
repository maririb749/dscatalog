package com.mariana.dscatalog.resources.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.mariana.dscatalog.services.exceptions.DatabaseException;
import com.mariana.dscatalog.services.exceptions.ResouceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResourceExceptionHandler {
	
   @ExceptionHandler(ResouceNotFoundException.class)
	public ResponseEntity<StandardError> entityNotFound(ResouceNotFoundException e, HttpServletRequest request){
	  HttpStatus status = HttpStatus.NOT_FOUND;
	  StandardError err = new StandardError();
	  err.setTimestamp(Instant.now());
	  err.setStatus(status.value());
	  err.setError("Resource not found");
	  err.setMessage(e.getMessage());
	  err.setPath(request.getRequestURI());
	  return ResponseEntity.status(status).body(err);
	  

 }
   

   @ExceptionHandler(DatabaseException.class)
 	public ResponseEntity<StandardError> dataBase(DatabaseException e, HttpServletRequest request){
	  HttpStatus status = HttpStatus.BAD_REQUEST;
	  StandardError err = new StandardError();
 	  err.setTimestamp(Instant.now());
 	  err.setStatus(status.value());
 	  err.setError("Database Exception");
 	  err.setMessage(e.getMessage());
 	  err.setPath(request.getRequestURI());
 	  return ResponseEntity.status(status).body(err);
   
   
 }
   @ExceptionHandler(MethodArgumentNotValidException.class)
  	public ResponseEntity<ValidationError> validation(MethodArgumentNotValidException e, HttpServletRequest request){
	  HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
	  ValidationError err = new ValidationError();
  	  err.setTimestamp(Instant.now());
  	  err.setStatus(status.value());
  	  err.setError("validation Exception");
  	  err.setMessage(e.getMessage());
  	  err.setPath(request.getRequestURI());
  	  
  	  for(FieldError f : e.getBindingResult().getFieldErrors()) {
  		  err.addError(f.getField(),f.getDefaultMessage());
  		  
  	  }
  	  
  	  
  	  return ResponseEntity.status(status).body(err);
  	  

   }
}

