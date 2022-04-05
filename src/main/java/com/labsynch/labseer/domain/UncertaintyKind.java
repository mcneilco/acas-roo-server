package com.labsynch.labseer.domain;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(sequenceName = "UNCERTAINTY_KIND_PKSEQ")
@RooJson
public class UncertaintyKind {

    @NotNull
    @Column(unique = true)
    @Size(max = 255)
    @JoinColumn(name = "ls_type")
    private String kindName;
}
