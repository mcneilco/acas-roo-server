// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ExperimentState;
import com.labsynch.labseer.domain.ExperimentValue;
import java.util.Set;

privileged aspect ExperimentState_Roo_JavaBean {
    
    public Experiment ExperimentState.getExperiment() {
        return this.experiment;
    }
    
    public void ExperimentState.setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }
    
    public Set<ExperimentValue> ExperimentState.getLsValues() {
        return this.lsValues;
    }
    
    public void ExperimentState.setLsValues(Set<ExperimentValue> lsValues) {
        this.lsValues = lsValues;
    }
    
}
