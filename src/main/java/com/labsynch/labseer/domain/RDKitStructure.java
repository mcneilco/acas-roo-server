package com.labsynch.labseer.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(sequenceName = "RDKIT_STRUCTURE_PKSEQ", inheritanceType = "TABLE_PER_CLASS", finders = { "findRDKitStructuresByRegEquals"})
public class RDKitStructure {

    @Id
    @SequenceGenerator(name = "rdkitStructureGen", sequenceName = "RDKIT_STRUCTURE_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "rdkitStructureGen")
    private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

    @NotNull
    @Column(columnDefinition = "CHAR(40)")
    private String preReg;

    @NotNull
    @Column(columnDefinition = "CHAR(40)")
    private String reg;

    @NotNull
    @Column(columnDefinition = "text")
    private String mol;

    @NotNull
    @DateTimeFormat(style="M-")
    private Date recordedDate;

}
