// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.AbstractState;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Version;

privileged aspect AbstractState_Roo_Jpa_Entity {
    
    declare @type: AbstractState: @Entity;
    
    declare @type: AbstractState: @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS);
    
    @Version
    @Column(name = "version")
    private Integer AbstractState.version;
    
    public Integer AbstractState.getVersion() {
        return this.version;
    }
    
    public void AbstractState.setVersion(Integer version) {
        this.version = version;
    }
    
}