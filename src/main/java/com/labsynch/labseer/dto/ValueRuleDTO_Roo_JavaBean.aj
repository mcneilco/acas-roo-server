// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.dto;

import com.labsynch.labseer.dto.ValuePathDTO;
import com.labsynch.labseer.dto.ValueRuleDTO;
import java.math.BigDecimal;

privileged aspect ValueRuleDTO_Roo_JavaBean {
    
    public String ValueRuleDTO.getComparisonMethod() {
        return this.comparisonMethod;
    }
    
    public void ValueRuleDTO.setComparisonMethod(String comparisonMethod) {
        this.comparisonMethod = comparisonMethod;
    }
    
    public BigDecimal ValueRuleDTO.getComparisonRange() {
        return this.comparisonRange;
    }
    
    public void ValueRuleDTO.setComparisonRange(BigDecimal comparisonRange) {
        this.comparisonRange = comparisonRange;
    }
    
    public ValuePathDTO ValueRuleDTO.getValue() {
        return this.value;
    }
    
    public void ValueRuleDTO.setValue(ValuePathDTO value) {
        this.value = value;
    }
    
}
