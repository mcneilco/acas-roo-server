// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.dto;

import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ItxProtocolProtocol;
import com.labsynch.labseer.domain.LsTag;
import com.labsynch.labseer.domain.ProtocolLabel;
import com.labsynch.labseer.domain.ProtocolState;
import com.labsynch.labseer.domain.ThingPage;
import com.labsynch.labseer.dto.ProtocolDTO;
import java.util.Date;
import java.util.Set;

privileged aspect ProtocolDTO_Roo_JavaBean {
    
    public String ProtocolDTO.getShortDescription() {
        return this.shortDescription;
    }
    
    public void ProtocolDTO.setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }
    
    public Long ProtocolDTO.getId() {
        return this.id;
    }
    
    public void ProtocolDTO.setId(Long id) {
        this.id = id;
    }
    
    public Integer ProtocolDTO.getVersion() {
        return this.version;
    }
    
    public void ProtocolDTO.setVersion(Integer version) {
        this.version = version;
    }
    
    public String ProtocolDTO.getLsType() {
        return this.lsType;
    }
    
    public void ProtocolDTO.setLsType(String lsType) {
        this.lsType = lsType;
    }
    
    public String ProtocolDTO.getLsKind() {
        return this.lsKind;
    }
    
    public void ProtocolDTO.setLsKind(String lsKind) {
        this.lsKind = lsKind;
    }
    
    public String ProtocolDTO.getCodeName() {
        return this.codeName;
    }
    
    public void ProtocolDTO.setCodeName(String codeName) {
        this.codeName = codeName;
    }
    
    public String ProtocolDTO.getRecordedBy() {
        return this.recordedBy;
    }
    
    public void ProtocolDTO.setRecordedBy(String recordedBy) {
        this.recordedBy = recordedBy;
    }
    
    public Date ProtocolDTO.getRecordedDate() {
        return this.recordedDate;
    }
    
    public void ProtocolDTO.setRecordedDate(Date recordedDate) {
        this.recordedDate = recordedDate;
    }
    
    public String ProtocolDTO.getModifiedBy() {
        return this.modifiedBy;
    }
    
    public void ProtocolDTO.setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
    
    public Date ProtocolDTO.getModifiedDate() {
        return this.modifiedDate;
    }
    
    public void ProtocolDTO.setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
    
    public boolean ProtocolDTO.isIgnored() {
        return this.ignored;
    }
    
    public void ProtocolDTO.setIgnored(boolean ignored) {
        this.ignored = ignored;
    }
    
    public boolean ProtocolDTO.isDeleted() {
        return this.deleted;
    }
    
    public void ProtocolDTO.setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    
    public Long ProtocolDTO.getLsTransaction() {
        return this.lsTransaction;
    }
    
    public void ProtocolDTO.setLsTransaction(Long lsTransaction) {
        this.lsTransaction = lsTransaction;
    }
    
    public Integer ProtocolDTO.getExperimentCount() {
        return this.experimentCount;
    }
    
    public void ProtocolDTO.setExperimentCount(Integer experimentCount) {
        this.experimentCount = experimentCount;
    }
    
    public Set<ThingPage> ProtocolDTO.getThingPage() {
        return this.thingPage;
    }
    
    public void ProtocolDTO.setThingPage(Set<ThingPage> thingPage) {
        this.thingPage = thingPage;
    }
    
    public Set<ProtocolState> ProtocolDTO.getLsStates() {
        return this.lsStates;
    }
    
    public void ProtocolDTO.setLsStates(Set<ProtocolState> lsStates) {
        this.lsStates = lsStates;
    }
    
    public Set<Experiment> ProtocolDTO.getExperiments() {
        return this.experiments;
    }
    
    public void ProtocolDTO.setExperiments(Set<Experiment> experiments) {
        this.experiments = experiments;
    }
    
    public Set<ProtocolLabel> ProtocolDTO.getLsLabels() {
        return this.lsLabels;
    }
    
    public void ProtocolDTO.setLsLabels(Set<ProtocolLabel> lsLabels) {
        this.lsLabels = lsLabels;
    }
    
    public Set<LsTag> ProtocolDTO.getLsTags() {
        return this.lsTags;
    }
    
    public void ProtocolDTO.setLsTags(Set<LsTag> lsTags) {
        this.lsTags = lsTags;
    }
    
    public Set<ItxProtocolProtocol> ProtocolDTO.getFirstProtocols() {
        return this.firstProtocols;
    }
    
    public void ProtocolDTO.setFirstProtocols(Set<ItxProtocolProtocol> firstProtocols) {
        this.firstProtocols = firstProtocols;
    }
    
    public Set<ItxProtocolProtocol> ProtocolDTO.getSecondProtocols() {
        return this.secondProtocols;
    }
    
    public void ProtocolDTO.setSecondProtocols(Set<ItxProtocolProtocol> secondProtocols) {
        this.secondProtocols = secondProtocols;
    }
    
}