package com.labsynch.labseer.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;

@ControllerAdvice
public class GlobalErrorHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalErrorHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        // Log the exception
        logger.error("An error occurred: ", ex);

        // Create an ErrorResponse object with the error message and stack trace
        ErrorResponse errorResponse;
        if (ex instanceof NullPointerException) {
            errorResponse = new ErrorResponse("NullPointerException occurred", getStackTraceAsString(ex));
        } else {
            errorResponse = new ErrorResponse(ex.getMessage(), getStackTraceAsString(ex));
        }
        
        // Return the ErrorResponse as a JSON response with an appropriate HTTP status
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String getStackTraceAsString(Exception ex) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        ex.printStackTrace(printWriter);
        return stringWriter.toString();
    }

    // Define the ErrorResponse class
    private static class ErrorResponse {
        private String errorMessage;
        private String stackTrace;

        public ErrorResponse(String errorMessage, String stackTrace) {
            this.errorMessage = errorMessage;
            this.stackTrace = stackTrace;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getStackTrace() {
            return stackTrace;
        }

        public void setStackTrace(String stackTrace) {
            this.stackTrace = stackTrace;
        }
    }
}
