// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.AnalysisGroupState;
import com.labsynch.labseer.domain.AnalysisGroupValue;
import java.util.Set;

privileged aspect AnalysisGroupState_Roo_JavaBean {
    
    public AnalysisGroup AnalysisGroupState.getAnalysisGroup() {
        return this.analysisGroup;
    }
    
    public void AnalysisGroupState.setAnalysisGroup(AnalysisGroup analysisGroup) {
        this.analysisGroup = analysisGroup;
    }
    
    public Set<AnalysisGroupValue> AnalysisGroupState.getLsValues() {
        return this.lsValues;
    }
    
    public void AnalysisGroupState.setLsValues(Set<AnalysisGroupValue> lsValues) {
        this.lsValues = lsValues;
    }
    
}