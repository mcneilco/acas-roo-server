package com.labsynch.labseer.domain;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Lob;
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
@RooJpaActiveRecord(sequenceName = "THING_PAGE_ARCHIVE_PKSEQ")
@RooJson
public class ThingPageArchive {

    @Size(max = 255)
    private String pageName;

    @NotNull
    @Size(max = 255)
    private String recordedBy;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "MM")
    private Date recordedDate;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String pageContent;

    @NotNull
    @Size(max = 255)
    private String modifiedBy;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "MM")
    private Date modifiedDate;

    @Size(max = 255)
    private String currentEditor;

    @NotNull
    private boolean ignored;

    private boolean archived;

    private Long lsTransaction;

    @Column(name = "thing_id")
    private Long thing;

    private Integer pageVersion;
}
