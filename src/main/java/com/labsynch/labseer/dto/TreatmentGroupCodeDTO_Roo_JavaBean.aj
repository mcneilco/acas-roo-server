// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.dto;

import com.labsynch.labseer.dto.AnalysisGroupCodeDTO;
import com.labsynch.labseer.dto.TreatmentGroupCodeDTO;
import java.util.Collection;

privileged aspect TreatmentGroupCodeDTO_Roo_JavaBean {
    
    public String TreatmentGroupCodeDTO.getTreatmentGroupCode() {
        return this.treatmentGroupCode;
    }
    
    public void TreatmentGroupCodeDTO.setTreatmentGroupCode(String treatmentGroupCode) {
        this.treatmentGroupCode = treatmentGroupCode;
    }
    
    public Collection<AnalysisGroupCodeDTO> TreatmentGroupCodeDTO.getAnalysisGroupCodes() {
        return this.analysisGroupCodes;
    }
    
    public void TreatmentGroupCodeDTO.setAnalysisGroupCodes(Collection<AnalysisGroupCodeDTO> analysisGroupCodes) {
        this.analysisGroupCodes = analysisGroupCodes;
    }
    
}