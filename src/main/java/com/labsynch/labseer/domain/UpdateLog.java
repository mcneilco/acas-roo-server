package com.labsynch.labseer.domain;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(sequenceName = "UPDATE_LOG_PKSEQ")
public class UpdateLog {

	@org.hibernate.annotations.Index(name="UPDATELOG_THING_IDX")
    private Long thing;

    @Size(max = 255)
    private String updateAction;
    
	@Size(max = 512)
	private String comments;
	
	@org.hibernate.annotations.Index(name="UPDATELOG_TRXN_IDX")
	private Long lsTransaction;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "MM")
	private Date recordedDate;
	
	@Size(max = 255)
	private String recordedBy;
    
    
}
