// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.dto;

import com.labsynch.labseer.dto.Metalot;
import com.labsynch.labseer.dto.MetalotReturn;
import com.labsynch.labseer.service.ErrorMessage;
import java.util.ArrayList;

privileged aspect MetalotReturn_Roo_JavaBean {
    
    public Metalot MetalotReturn.getMetalot() {
        return this.metalot;
    }
    
    public void MetalotReturn.setMetalot(Metalot metalot) {
        this.metalot = metalot;
    }
    
    public ArrayList<ErrorMessage> MetalotReturn.getErrors() {
        return this.errors;
    }
    
    public void MetalotReturn.setErrors(ArrayList<ErrorMessage> errors) {
        this.errors = errors;
    }
    
}
