// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.dto;

import com.labsynch.labseer.domain.TreatmentGroupValue;
import com.labsynch.labseer.dto.TreatmentGroupValuePathDTO;
import java.util.Collection;

privileged aspect TreatmentGroupValuePathDTO_Roo_JavaBean {
    
    public String TreatmentGroupValuePathDTO.getIdOrCodeName() {
        return this.idOrCodeName;
    }
    
    public void TreatmentGroupValuePathDTO.setIdOrCodeName(String idOrCodeName) {
        this.idOrCodeName = idOrCodeName;
    }
    
    public String TreatmentGroupValuePathDTO.getStateType() {
        return this.stateType;
    }
    
    public void TreatmentGroupValuePathDTO.setStateType(String stateType) {
        this.stateType = stateType;
    }
    
    public String TreatmentGroupValuePathDTO.getStateKind() {
        return this.stateKind;
    }
    
    public void TreatmentGroupValuePathDTO.setStateKind(String stateKind) {
        this.stateKind = stateKind;
    }
    
    public String TreatmentGroupValuePathDTO.getValueType() {
        return this.valueType;
    }
    
    public void TreatmentGroupValuePathDTO.setValueType(String valueType) {
        this.valueType = valueType;
    }
    
    public String TreatmentGroupValuePathDTO.getValueKind() {
        return this.valueKind;
    }
    
    public void TreatmentGroupValuePathDTO.setValueKind(String valueKind) {
        this.valueKind = valueKind;
    }
    
    public Collection<TreatmentGroupValue> TreatmentGroupValuePathDTO.getValues() {
        return this.values;
    }
    
    public void TreatmentGroupValuePathDTO.setValues(Collection<TreatmentGroupValue> values) {
        this.values = values;
    }
    
}
