package com.labsynch.labseer.exceptions;

import javax.validation.constraints.Size;


public class ErrorMessage {

    @Size(max = 50)
    private String errorLevel;
    
    @Size(max = 255)
    private String message;
    
}
