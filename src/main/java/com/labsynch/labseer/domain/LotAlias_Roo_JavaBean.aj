// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.domain.LotAlias;

privileged aspect LotAlias_Roo_JavaBean {
    
    public Lot LotAlias.getLot() {
        return this.lot;
    }
    
    public void LotAlias.setLot(Lot lot) {
        this.lot = lot;
    }
    
    public String LotAlias.getLsType() {
        return this.lsType;
    }
    
    public void LotAlias.setLsType(String lsType) {
        this.lsType = lsType;
    }
    
    public String LotAlias.getLsKind() {
        return this.lsKind;
    }
    
    public void LotAlias.setLsKind(String lsKind) {
        this.lsKind = lsKind;
    }
    
    public String LotAlias.getAliasName() {
        return this.aliasName;
    }
    
    public void LotAlias.setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }
    
    public boolean LotAlias.isPreferred() {
        return this.preferred;
    }
    
    public void LotAlias.setPreferred(boolean preferred) {
        this.preferred = preferred;
    }
    
    public boolean LotAlias.isIgnored() {
        return this.ignored;
    }
    
    public void LotAlias.setIgnored(boolean ignored) {
        this.ignored = ignored;
    }
    
    public boolean LotAlias.isDeleted() {
        return this.deleted;
    }
    
    public void LotAlias.setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    
}