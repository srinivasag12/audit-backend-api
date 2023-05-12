package com.api.central.config;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice(basePackages = {"com.central.aduit"} )
public class AppExceptionHandler extends ResponseEntityExceptionHandler {
	
   @Override
   protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return super.handleExceptionInternal(exception, fromBindingErrors(exception.getBindingResult()), headers, HttpStatus.BAD_REQUEST, request);
   }
   
   @ExceptionHandler(IOException.class)
   protected ResponseEntity<ErrorMessage> ioExceptionHandler(RuntimeException ex) {
	   ErrorMessage errorMessage = new ErrorMessage("");
			   errorMessage.addErrorMessage("Unable to process the file, Please try again");
	return new ResponseEntity<ErrorMessage>(fromConflitingErrors(ex), HttpStatus.BAD_REQUEST);
   }
	
   @ExceptionHandler(Exception.class)
   protected ResponseEntity<ErrorMessage> exceptionHandler(RuntimeException ex) {
	return new ResponseEntity<ErrorMessage>(fromConflitingErrors(ex), HttpStatus.CONFLICT);
   }

   public static ErrorMessage fromBindingErrors(Errors errors) {
   ErrorMessage error = new ErrorMessage("Input validation failed : " + errors.getErrorCount() + " error(s)");
   for (ObjectError objectError : errors.getAllErrors()) {
      error.addErrorMessage(objectError.getDefaultMessage());
   }        
   return error;
   }

   public static ErrorMessage fromConflitingErrors(RuntimeException ex) {
      ErrorMessage error = new ErrorMessage("Unable to procecss your request, Escalate to HelpDesk.");
      error.addErrorMessage(org.apache.commons.lang.exception.ExceptionUtils.getFullStackTrace(ex));
   return error;
   }	  
}
