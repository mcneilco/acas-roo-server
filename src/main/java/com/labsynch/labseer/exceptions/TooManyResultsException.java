package com.labsynch.labseer.exceptions;

@SuppressWarnings("serial")
public class TooManyResultsException extends Exception {

    public TooManyResultsException() {
    }

    public TooManyResultsException(String message) {
        super(message);
    }

    public TooManyResultsException(Throwable cause) {
        super(cause);
    }

    public TooManyResultsException(String message, Throwable cause) {
        super(message, cause);
    }

}
