// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.DDictType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

privileged aspect DDictType_Roo_Jpa_Entity {
    
    declare @type: DDictType: @Entity;
    
    @Id
    @SequenceGenerator(name = "dDictTypeGen", sequenceName = "DDICT_TYPE_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "dDictTypeGen")
    @Column(name = "id")
    private Long DDictType.id;
    
    @Version
    @Column(name = "version")
    private Integer DDictType.version;
    
    public Long DDictType.getId() {
        return this.id;
    }
    
    public void DDictType.setId(Long id) {
        this.id = id;
    }
    
    public Integer DDictType.getVersion() {
        return this.version;
    }
    
    public void DDictType.setVersion(Integer version) {
        this.version = version;
    }
    
}
