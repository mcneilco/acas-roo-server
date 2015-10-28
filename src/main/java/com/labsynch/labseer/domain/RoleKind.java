package com.labsynch.labseer.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@Entity
@RooJavaBean
@RooToString
@RooJpaActiveRecord(sequenceName = "ROLE_KIND_PKSEQ", finders={"findRoleKindsByLsType", 
		"findRoldKindsByKindNameEquals", "findRoleKindsByKindNameEqualsAndLsType"})
@RooJson
public class RoleKind {
	
    @NotNull
    @ManyToOne
    private RoleType lsType;

    @NotNull
    @Size(max = 255)
    private String kindName;
 
}
