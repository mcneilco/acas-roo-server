package com.labsynch.labseer.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@Entity
@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(sequenceName = "DDICT_VALUE_PKSEQ", finders = { })
public class DDictValue {

    private static final Logger logger = LoggerFactory.getLogger(DDictValue.class);

    @NotNull
	@org.hibernate.annotations.Index(name="DD_VALUE_TYPE_IDX")
    @Size(max = 255)
    private String lsType;

    @NotNull
	@org.hibernate.annotations.Index(name="DD_VALUE_KIND_IDX")
    @Size(max = 255)
    private String lsKind;
    
	@Size(max = 255)
	@org.hibernate.annotations.Index(name="DD_VALUE_TK_IDX")
	private String lsTypeAndKind;
    
    @NotNull
    @Size(max = 512)
    private String lsValue;


    @Size(max = 512)
    private String description;
    
    @Size(max = 512)
    private String comments;
    
	@NotNull
	private boolean ignored;

	private Integer displayOrder;

}
