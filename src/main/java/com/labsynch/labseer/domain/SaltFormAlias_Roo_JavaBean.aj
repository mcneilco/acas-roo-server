// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.SaltForm;
import com.labsynch.labseer.domain.SaltFormAlias;

privileged aspect SaltFormAlias_Roo_JavaBean {
    
    public SaltForm SaltFormAlias.getSaltForm() {
        return this.saltForm;
    }
    
    public void SaltFormAlias.setSaltForm(SaltForm saltForm) {
        this.saltForm = saltForm;
    }
    
    public String SaltFormAlias.getLsType() {
        return this.lsType;
    }
    
    public void SaltFormAlias.setLsType(String lsType) {
        this.lsType = lsType;
    }
    
    public String SaltFormAlias.getLsKind() {
        return this.lsKind;
    }
    
    public void SaltFormAlias.setLsKind(String lsKind) {
        this.lsKind = lsKind;
    }
    
    public String SaltFormAlias.getAliasName() {
        return this.aliasName;
    }
    
    public void SaltFormAlias.setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }
    
    public boolean SaltFormAlias.isPreferred() {
        return this.preferred;
    }
    
    public void SaltFormAlias.setPreferred(boolean preferred) {
        this.preferred = preferred;
    }
    
    public boolean SaltFormAlias.isIgnored() {
        return this.ignored;
    }
    
    public void SaltFormAlias.setIgnored(boolean ignored) {
        this.ignored = ignored;
    }
    
    public boolean SaltFormAlias.isDeleted() {
        return this.deleted;
    }
    
    public void SaltFormAlias.setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    
}