// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.AbstractState;
import java.util.Date;

privileged aspect AbstractState_Roo_JavaBean {
    
    public String AbstractState.getRecordedBy() {
        return this.recordedBy;
    }
    
    public void AbstractState.setRecordedBy(String recordedBy) {
        this.recordedBy = recordedBy;
    }
    
    public Date AbstractState.getRecordedDate() {
        return this.recordedDate;
    }
    
    public void AbstractState.setRecordedDate(Date recordedDate) {
        this.recordedDate = recordedDate;
    }
    
    public String AbstractState.getModifiedBy() {
        return this.modifiedBy;
    }
    
    public void AbstractState.setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
    
    public Date AbstractState.getModifiedDate() {
        return this.modifiedDate;
    }
    
    public void AbstractState.setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
    
    public String AbstractState.getLsType() {
        return this.lsType;
    }
    
    public void AbstractState.setLsType(String lsType) {
        this.lsType = lsType;
    }
    
    public String AbstractState.getLsKind() {
        return this.lsKind;
    }
    
    public void AbstractState.setLsKind(String lsKind) {
        this.lsKind = lsKind;
    }
    
    public String AbstractState.getLsTypeAndKind() {
        return this.lsTypeAndKind;
    }
    
    public void AbstractState.setLsTypeAndKind(String lsTypeAndKind) {
        this.lsTypeAndKind = lsTypeAndKind;
    }
    
    public String AbstractState.getComments() {
        return this.comments;
    }
    
    public void AbstractState.setComments(String comments) {
        this.comments = comments;
    }
    
    public boolean AbstractState.isIgnored() {
        return this.ignored;
    }
    
    public void AbstractState.setIgnored(boolean ignored) {
        this.ignored = ignored;
    }
    
    public boolean AbstractState.isDeleted() {
        return this.deleted;
    }
    
    public void AbstractState.setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    
    public Long AbstractState.getLsTransaction() {
        return this.lsTransaction;
    }
    
    public void AbstractState.setLsTransaction(Long lsTransaction) {
        this.lsTransaction = lsTransaction;
    }
    
}
