// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.TempSelectTable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

privileged aspect TempSelectTable_Roo_Jpa_Entity {
    
    declare @type: TempSelectTable: @Entity;
    
    @Id
    @SequenceGenerator(name = "tempSelectTableGen", sequenceName = "TEMP_SELECT_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "tempSelectTableGen")
    @Column(name = "id")
    private Long TempSelectTable.id;
    
    @Version
    @Column(name = "version")
    private Integer TempSelectTable.version;
    
    public Long TempSelectTable.getId() {
        return this.id;
    }
    
    public void TempSelectTable.setId(Long id) {
        this.id = id;
    }
    
    public Integer TempSelectTable.getVersion() {
        return this.version;
    }
    
    public void TempSelectTable.setVersion(Integer version) {
        this.version = version;
    }
    
}
