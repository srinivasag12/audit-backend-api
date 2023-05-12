/*package com.api.central.iriException;

import org.hibernate.engine.jdbc.spi.SqlExceptionHelper.WarningHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;

import com.api.central.typeahead.dao.TypeaheadDaoImpl;

@ControllerAdvice  
public class IRIhandler {
	private static final Logger log = LoggerFactory.getLogger(IRIhandler.class);
	
	
	
	
	 @ExceptionHandler(IRIException.class)
	    public ResponseEntity<IRIErrorResponse> handleUserNotFoundException(Exception ex) {
	        IRIErrorResponse errorResponse = new IRIErrorResponse();
	        errorResponse.setErrorCode(HttpStatus.PRECONDITION_FAILED.value());
	        errorResponse.setErrorMessage(ex.getMessage());
	        return new ResponseEntity<IRIErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
	    }
	   
	    @ExceptionHandler(Exception.class)
	    public ResponseEntity<IRIErrorResponse> handleGenericException(Exception ex) {
	    	IRIErrorResponse errorResponse = new IRIErrorResponse();
	        errorResponse.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	        errorResponse.setErrorMessage("There is some technical issue");
	      return new ResponseEntity<IRIErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
	    }

}
*/