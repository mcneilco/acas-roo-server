// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.exceptions;

import com.labsynch.labseer.exceptions.ErrorMessage;

privileged aspect ErrorMessage_Roo_JavaBean {
    
    public String ErrorMessage.getErrorLevel() {
        return this.errorLevel;
    }
    
    public void ErrorMessage.setErrorLevel(String errorLevel) {
        this.errorLevel = errorLevel;
    }
    
    public String ErrorMessage.getMessage() {
        return this.message;
    }
    
    public void ErrorMessage.setMessage(String message) {
        this.message = message;
    }
    
}
