// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.ThingPage;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

privileged aspect ThingPage_Roo_Jpa_Entity {
    
    declare @type: ThingPage: @Entity;
    
    @Id
    @SequenceGenerator(name = "thingPageGen", sequenceName = "THING_PAGE_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "thingPageGen")
    @Column(name = "id")
    private Long ThingPage.id;
    
    @Version
    @Column(name = "version")
    private Integer ThingPage.version;
    
    public Long ThingPage.getId() {
        return this.id;
    }
    
    public void ThingPage.setId(Long id) {
        this.id = id;
    }
    
    public Integer ThingPage.getVersion() {
        return this.version;
    }
    
    public void ThingPage.setVersion(Integer version) {
        this.version = version;
    }
    
}