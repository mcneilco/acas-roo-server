package com.labsynch.labseer.domain;

import java.util.Date;

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
@RooJson
@RooJpaActiveRecord(sequenceName = "LABEL_SEQUENCE_PKSEQ", finders = { "findLabelSequencesByThingTypeAndKindEqualsAndLabelTypeAndKindEquals" })
public class LabelSequence {

    @NotNull
    @Size(max = 255)
    private String thingTypeAndKind;

    @NotNull
    @Size(max = 255)
    private String labelTypeAndKind;

    @NotNull
    @Size(max = 50)
    private String labelPrefix;

    @Size(max = 10)
    private String labelSeparator;

    private boolean groupDigits;

    private Integer digits;

    @NotNull
    private Long latestNumber;

    @NotNull
    private boolean ignored;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "MM")
    private Date modifiedDate;
    
    @Size(max = 10)
    private String batchSeparator;
    
    private Integer batchNumber;
}
