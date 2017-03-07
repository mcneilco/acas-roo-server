package com.labsynch.labseer.domain;

import java.util.Date;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(sequenceName = "LS_TRANSACTION_PKSEQ")
@RooJson
public class LsTransaction {

    @Size(max = 255)
    private String comments;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "MM")
    private Date recordedDate;
    
    @Size(max = 255)
    private String recordedBy;
    
    @Enumerated(EnumType.STRING)
	private LsTransactionStatus status;
    
    @Enumerated(EnumType.STRING)
	private LsTransactionType type;
}