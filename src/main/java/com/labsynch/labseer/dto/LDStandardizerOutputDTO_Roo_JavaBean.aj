// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.dto;

import com.labsynch.labseer.dto.LDStandardizerOutputDTO;
import com.labsynch.labseer.dto.LDStandardizerOutputStructureDTO;
import java.util.HashMap;

privileged aspect LDStandardizerOutputDTO_Roo_JavaBean {
    
    public HashMap<String, LDStandardizerOutputStructureDTO> LDStandardizerOutputDTO.getStructures() {
        return this.structures;
    }
    
    public void LDStandardizerOutputDTO.setStructures(HashMap<String, LDStandardizerOutputStructureDTO> structures) {
        this.structures = structures;
    }
    
    public String LDStandardizerOutputDTO.getJob_status() {
        return this.job_status;
    }
    
    public void LDStandardizerOutputDTO.setJob_status(String job_status) {
        this.job_status = job_status;
    }
    
}
