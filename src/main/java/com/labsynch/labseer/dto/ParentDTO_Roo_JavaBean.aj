// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.dto;

import com.labsynch.labseer.domain.CompoundType;
import com.labsynch.labseer.domain.ParentAnnotation;
import com.labsynch.labseer.domain.StereoCategory;
import com.labsynch.labseer.dto.ParentAliasDTO;
import com.labsynch.labseer.dto.ParentDTO;
import com.labsynch.labseer.dto.SaltFormDTO;
import java.util.Set;

privileged aspect ParentDTO_Roo_JavaBean {
    
    public Long ParentDTO.getId() {
        return this.id;
    }
    
    public void ParentDTO.setId(Long id) {
        this.id = id;
    }
    
    public String ParentDTO.getCorpName() {
        return this.corpName;
    }
    
    public void ParentDTO.setCorpName(String corpName) {
        this.corpName = corpName;
    }
    
    public String ParentDTO.getChemist() {
        return this.chemist;
    }
    
    public void ParentDTO.setChemist(String chemist) {
        this.chemist = chemist;
    }
    
    public String ParentDTO.getCommonName() {
        return this.commonName;
    }
    
    public void ParentDTO.setCommonName(String commonName) {
        this.commonName = commonName;
    }
    
    public StereoCategory ParentDTO.getStereoCategory() {
        return this.stereoCategory;
    }
    
    public void ParentDTO.setStereoCategory(StereoCategory stereoCategory) {
        this.stereoCategory = stereoCategory;
    }
    
    public String ParentDTO.getStereoComment() {
        return this.stereoComment;
    }
    
    public void ParentDTO.setStereoComment(String stereoComment) {
        this.stereoComment = stereoComment;
    }
    
    public String ParentDTO.getMolStructure() {
        return this.molStructure;
    }
    
    public void ParentDTO.setMolStructure(String molStructure) {
        this.molStructure = molStructure;
    }
    
    public Double ParentDTO.getExactMass() {
        return this.exactMass;
    }
    
    public void ParentDTO.setExactMass(Double exactMass) {
        this.exactMass = exactMass;
    }
    
    public String ParentDTO.getMolFormula() {
        return this.molFormula;
    }
    
    public void ParentDTO.setMolFormula(String molFormula) {
        this.molFormula = molFormula;
    }
    
    public int ParentDTO.getCdId() {
        return this.CdId;
    }
    
    public void ParentDTO.setCdId(int CdId) {
        this.CdId = CdId;
    }
    
    public Double ParentDTO.getMolWeight() {
        return this.molWeight;
    }
    
    public void ParentDTO.setMolWeight(Double molWeight) {
        this.molWeight = molWeight;
    }
    
    public Boolean ParentDTO.getIgnore() {
        return this.ignore;
    }
    
    public void ParentDTO.setIgnore(Boolean ignore) {
        this.ignore = ignore;
    }
    
    public ParentAnnotation ParentDTO.getParentAnnotation() {
        return this.parentAnnotation;
    }
    
    public void ParentDTO.setParentAnnotation(ParentAnnotation parentAnnotation) {
        this.parentAnnotation = parentAnnotation;
    }
    
    public CompoundType ParentDTO.getCompoundType() {
        return this.compoundType;
    }
    
    public void ParentDTO.setCompoundType(CompoundType compoundType) {
        this.compoundType = compoundType;
    }
    
    public String ParentDTO.getComment() {
        return this.comment;
    }
    
    public void ParentDTO.setComment(String comment) {
        this.comment = comment;
    }
    
    public Boolean ParentDTO.getIsMixture() {
        return this.isMixture;
    }
    
    public void ParentDTO.setIsMixture(Boolean isMixture) {
        this.isMixture = isMixture;
    }
    
    public Set<SaltFormDTO> ParentDTO.getSaltForms() {
        return this.saltForms;
    }
    
    public void ParentDTO.setSaltForms(Set<SaltFormDTO> saltForms) {
        this.saltForms = saltForms;
    }
    
    public Set<ParentAliasDTO> ParentDTO.getParentAliases() {
        return this.parentAliases;
    }
    
    public void ParentDTO.setParentAliases(Set<ParentAliasDTO> parentAliases) {
        this.parentAliases = parentAliases;
    }
    
}