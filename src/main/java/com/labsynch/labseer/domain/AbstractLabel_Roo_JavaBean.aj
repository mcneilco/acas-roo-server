// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.AbstractLabel;
import java.util.Date;

privileged aspect AbstractLabel_Roo_JavaBean {
    
    public String AbstractLabel.getLabelText() {
        return this.labelText;
    }
    
    public void AbstractLabel.setLabelText(String labelText) {
        this.labelText = labelText;
    }
    
    public String AbstractLabel.getRecordedBy() {
        return this.recordedBy;
    }
    
    public void AbstractLabel.setRecordedBy(String recordedBy) {
        this.recordedBy = recordedBy;
    }
    
    public Date AbstractLabel.getRecordedDate() {
        return this.recordedDate;
    }
    
    public void AbstractLabel.setRecordedDate(Date recordedDate) {
        this.recordedDate = recordedDate;
    }
    
    public Date AbstractLabel.getModifiedDate() {
        return this.modifiedDate;
    }
    
    public void AbstractLabel.setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
    
    public boolean AbstractLabel.isPhysicallyLabled() {
        return this.physicallyLabled;
    }
    
    public void AbstractLabel.setPhysicallyLabled(boolean physicallyLabled) {
        this.physicallyLabled = physicallyLabled;
    }
    
    public String AbstractLabel.getImageFile() {
        return this.imageFile;
    }
    
    public void AbstractLabel.setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }
    
    public String AbstractLabel.getLsType() {
        return this.lsType;
    }
    
    public void AbstractLabel.setLsType(String lsType) {
        this.lsType = lsType;
    }
    
    public String AbstractLabel.getLsKind() {
        return this.lsKind;
    }
    
    public void AbstractLabel.setLsKind(String lsKind) {
        this.lsKind = lsKind;
    }
    
    public String AbstractLabel.getLsTypeAndKind() {
        return this.lsTypeAndKind;
    }
    
    public void AbstractLabel.setLsTypeAndKind(String lsTypeAndKind) {
        this.lsTypeAndKind = lsTypeAndKind;
    }
    
    public boolean AbstractLabel.isPreferred() {
        return this.preferred;
    }
    
    public void AbstractLabel.setPreferred(boolean preferred) {
        this.preferred = preferred;
    }
    
    public boolean AbstractLabel.isIgnored() {
        return this.ignored;
    }
    
    public void AbstractLabel.setIgnored(boolean ignored) {
        this.ignored = ignored;
    }
    
    public boolean AbstractLabel.isDeleted() {
        return this.deleted;
    }
    
    public void AbstractLabel.setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    
    public Long AbstractLabel.getLsTransaction() {
        return this.lsTransaction;
    }
    
    public void AbstractLabel.setLsTransaction(Long lsTransaction) {
        this.lsTransaction = lsTransaction;
    }
    
}