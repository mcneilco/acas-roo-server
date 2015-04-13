package com.labsynch.labseer.exceptions;

@SuppressWarnings("serial")
public class UniqueInteractionsException extends Exception {
	
	public UniqueInteractionsException() {
    }
 
    public UniqueInteractionsException(String message) {
	super(message);
    }
 
    public UniqueInteractionsException(Throwable cause) {
	super(cause);
    }
 
    public UniqueInteractionsException(String message, Throwable cause) {
	super(message, cause);
    }


}
