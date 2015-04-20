package com.labsynch.labseer.exceptions;

@SuppressWarnings("serial")
public class UniqueNameException extends Exception {
	
	public UniqueNameException() {
    }
 
    public UniqueNameException(String message) {
	super(message);
    }
 
    public UniqueNameException(Throwable cause) {
	super(cause);
    }
 
    public UniqueNameException(String message, Throwable cause) {
	super(message, cause);
    }


}
