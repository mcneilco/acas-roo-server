// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.LsSeqItxLsThingLsThing;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

privileged aspect LsSeqItxLsThingLsThing_Roo_Jpa_Entity {
    
    declare @type: LsSeqItxLsThingLsThing: @Entity;
    
    @Id
    @SequenceGenerator(name = "lsSeqItxLsThingLsThingGen", sequenceName = "LSSEQ_ITXTHGTHG_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "lsSeqItxLsThingLsThingGen")
    @Column(name = "id")
    private Long LsSeqItxLsThingLsThing.id;
    
    @Version
    @Column(name = "version")
    private Integer LsSeqItxLsThingLsThing.version;
    
    public Long LsSeqItxLsThingLsThing.getId() {
        return this.id;
    }
    
    public void LsSeqItxLsThingLsThing.setId(Long id) {
        this.id = id;
    }
    
    public Integer LsSeqItxLsThingLsThing.getVersion() {
        return this.version;
    }
    
    public void LsSeqItxLsThingLsThing.setVersion(Integer version) {
        this.version = version;
    }
    
}
