// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.Isotope;

privileged aspect Isotope_Roo_JavaBean {
    
    public String Isotope.getName() {
        return this.name;
    }
    
    public void Isotope.setName(String name) {
        this.name = name;
    }
    
    public String Isotope.getAbbrev() {
        return this.abbrev;
    }
    
    public void Isotope.setAbbrev(String abbrev) {
        this.abbrev = abbrev;
    }
    
    public Double Isotope.getMassChange() {
        return this.massChange;
    }
    
    public void Isotope.setMassChange(Double massChange) {
        this.massChange = massChange;
    }
    
    public Boolean Isotope.getIgnore() {
        return this.ignore;
    }
    
    public void Isotope.setIgnore(Boolean ignore) {
        this.ignore = ignore;
    }
    
}