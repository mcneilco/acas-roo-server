// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.dto;

import com.labsynch.labseer.dto.MultiContainerSubjectSearchRequest;
import com.labsynch.labseer.dto.ValueQueryDTO;
import java.util.Collection;

privileged aspect MultiContainerSubjectSearchRequest_Roo_JavaBean {
    
    public Integer MultiContainerSubjectSearchRequest.getMaxResults() {
        return this.maxResults;
    }
    
    public void MultiContainerSubjectSearchRequest.setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }
    
    public String MultiContainerSubjectSearchRequest.getProtocolLabelLike() {
        return this.protocolLabelLike;
    }
    
    public void MultiContainerSubjectSearchRequest.setProtocolLabelLike(String protocolLabelLike) {
        this.protocolLabelLike = protocolLabelLike;
    }
    
    public String MultiContainerSubjectSearchRequest.getExperimentLabelLike() {
        return this.experimentLabelLike;
    }
    
    public void MultiContainerSubjectSearchRequest.setExperimentLabelLike(String experimentLabelLike) {
        this.experimentLabelLike = experimentLabelLike;
    }
    
    public Collection<String> MultiContainerSubjectSearchRequest.getContainerCodes() {
        return this.containerCodes;
    }
    
    public void MultiContainerSubjectSearchRequest.setContainerCodes(Collection<String> containerCodes) {
        this.containerCodes = containerCodes;
    }
    
    public String MultiContainerSubjectSearchRequest.getSubjectType() {
        return this.subjectType;
    }
    
    public void MultiContainerSubjectSearchRequest.setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }
    
    public String MultiContainerSubjectSearchRequest.getSubjectKind() {
        return this.subjectKind;
    }
    
    public void MultiContainerSubjectSearchRequest.setSubjectKind(String subjectKind) {
        this.subjectKind = subjectKind;
    }
    
    public Collection<ValueQueryDTO> MultiContainerSubjectSearchRequest.getValues() {
        return this.values;
    }
    
    public void MultiContainerSubjectSearchRequest.setValues(Collection<ValueQueryDTO> values) {
        this.values = values;
    }
    
}