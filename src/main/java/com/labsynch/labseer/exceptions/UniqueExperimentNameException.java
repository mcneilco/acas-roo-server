package com.labsynch.labseer.exceptions;

@SuppressWarnings("serial")
public class UniqueExperimentNameException extends Exception {
	
	public UniqueExperimentNameException() {
    }
 
    public UniqueExperimentNameException(String message) {
	super(message);
    }
 
    public UniqueExperimentNameException(Throwable cause) {
	super(cause);
    }
 
    public UniqueExperimentNameException(String message, Throwable cause) {
	super(message, cause);
    }


}
