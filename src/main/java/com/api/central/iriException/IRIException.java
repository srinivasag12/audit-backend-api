package com.api.central.iriException;



public class IRIException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String errorMessage;
	
   
    
   private String errorCode;
   
   

    
    public IRIException(String errorMessage, Throwable error, String errorcode) {
    	 super(errorMessage, error);
    }




	public String getErrorMessage() {
		return errorMessage;
	}




	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}




	public String getErrorCode() {
		return errorCode;
	}




	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
    
    
    
    

	

    
    
    

}
