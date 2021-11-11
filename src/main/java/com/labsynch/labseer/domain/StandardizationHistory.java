package com.labsynch.labseer.domain;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord
public class StandardizationHistory {

    @Id
    @SequenceGenerator(name = "stndznHistGen", sequenceName = "STNDZN_HIST_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "stndznHistGen")
    @Column(name = "id")
    private Long id;

    private Date recordedDate;

	private String settings;

	private int settingsHash;

	private String dryRunStatus;

	private Date dryRunStart;

	private Date dryRunComplete;

	private String standardizationStatus;

	private String standardizationUser;

	private String standardizationReason;

	private Date standardizationStart;

	private Date standardizationComplete;

	private Integer structuresStandardizedCount;

	private Integer structuresUpdatedCount;

	private Integer newDuplicateCount;

	private Integer existingDuplicateCount;

	private Integer displayChangeCount;

	private Integer asDrawnDisplayChangeCount;

	private Integer changedStructureCount;

	public StandardizationHistory() {
	}

}