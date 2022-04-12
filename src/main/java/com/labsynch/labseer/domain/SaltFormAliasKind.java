package com.labsynch.labseer.domain;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord()
public class SaltFormAliasKind {
	
    @NotNull
    @ManyToOne
    @JoinColumn(name = "ls_type")
    private SaltFormAliasType lsType;

    @Size(max = 255)
    private String kindName;
    
}
