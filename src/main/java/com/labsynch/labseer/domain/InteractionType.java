package com.labsynch.labseer.domain;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(sequenceName = "INTERACTION_TYPE_PKSEQ")
@RooJson
public class InteractionType {

    @NotNull
    @Column(unique = true)
    @Size(max = 128)
    private String typeName;

    @NotNull
    @Size(max = 128)
    private String typeVerb;
}
