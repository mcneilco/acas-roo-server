package com.labsynch.labseer.domain;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
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
@RooJpaActiveRecord(sequenceName = "THING_PAGE_PKSEQ")
@RooJson
public class ThingPage {

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

    @NotNull
    @Size(max = 255)
    private String currentEditor;

    @NotNull
    private boolean ignored;

    @NotNull
    private boolean archived;

    @ManyToOne
    private LsTransaction lsTransaction;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "thing_id")
    private AbstractThing thing;
}
