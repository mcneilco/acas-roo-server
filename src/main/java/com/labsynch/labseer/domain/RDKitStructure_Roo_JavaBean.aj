// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.RDKitStructure;
import java.util.Date;
import java.util.HashMap;

privileged aspect RDKitStructure_Roo_JavaBean {
    
    public String RDKitStructure.getPreReg() {
        return this.preReg;
    }
    
    public void RDKitStructure.setPreReg(String preReg) {
        this.preReg = preReg;
    }
    
    public String RDKitStructure.getReg() {
        return this.reg;
    }
    
    public void RDKitStructure.setReg(String reg) {
        this.reg = reg;
    }
    
    public String RDKitStructure.getMol() {
        return this.mol;
    }
    
    public void RDKitStructure.setMol(String mol) {
        this.mol = mol;
    }
    
    public Double RDKitStructure.getExactMolWeight() {
        return this.exactMolWeight;
    }
    
    public void RDKitStructure.setExactMolWeight(Double exactMolWeight) {
        this.exactMolWeight = exactMolWeight;
    }
    
    public Double RDKitStructure.getAverageMolWeight() {
        return this.averageMolWeight;
    }
    
    public void RDKitStructure.setAverageMolWeight(Double averageMolWeight) {
        this.averageMolWeight = averageMolWeight;
    }
    
    public Integer RDKitStructure.getTotalCharge() {
        return this.totalCharge;
    }
    
    public void RDKitStructure.setTotalCharge(Integer totalCharge) {
        this.totalCharge = totalCharge;
    }
    
    public String RDKitStructure.getSmiles() {
        return this.smiles;
    }
    
    public void RDKitStructure.setSmiles(String smiles) {
        this.smiles = smiles;
    }
    
    public String RDKitStructure.getMolecularFormula() {
        return this.molecularFormula;
    }
    
    public void RDKitStructure.setMolecularFormula(String molecularFormula) {
        this.molecularFormula = molecularFormula;
    }
    
    public HashMap<String, String> RDKitStructure.getProperties() {
        return this.properties;
    }
    
    public void RDKitStructure.setProperties(HashMap<String, String> properties) {
        this.properties = properties;
    }
    
    public Date RDKitStructure.getRecordedDate() {
        return this.recordedDate;
    }
    
    public void RDKitStructure.setRecordedDate(Date recordedDate) {
        this.recordedDate = recordedDate;
    }
    
}
