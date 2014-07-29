package com.labsynch.labseer.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.PersistenceContext;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(sequenceName = "VALUE_PKSEQ", inheritanceType = "TABLE_PER_CLASS")
@RooJson
@Transactional
public abstract class AbstractValue {

	@NotNull
	@Size(max = 64)
	@org.hibernate.annotations.Index(name="_TYPE_IDX")
	private String lsType;

	@NotNull
	@Size(max = 255)
	@org.hibernate.annotations.Index(name="_KIND_IDX")
	private String lsKind;

	@Size(max = 350)
	@org.hibernate.annotations.Index(name="_TK_IDX")
	private String lsTypeAndKind;


	@Size(max = 64)
	@org.hibernate.annotations.Index(name="_CDTYPE_IDX")
	private String codeType;

	@Size(max = 255)
	@org.hibernate.annotations.Index(name="_CDKIND_IDX")
	private String codeKind;

	@Size(max = 350)
	@org.hibernate.annotations.Index(name="_CDTK_IDX")
	private String codeTypeAndKind;
	
	@Size(max = 255)
	protected String stringValue;

	@Size(max = 255)
	@org.hibernate.annotations.Index(name="_CODE_IDX")
	protected String codeValue;
	
	@Size(max = 512)
	private String fileValue;

	@Size(max = 2000)
	private String urlValue;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "MM")
	private Date dateValue;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String clobValue;
    
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] blobValue;
    
	@Size(max = 25)
	private String operatorType;

    @Size(max = 10)
	private String operatorKind;
    
	@Size(max = 50)
	@org.hibernate.annotations.Index(name="_OPTK_IDX")
	private String operatorTypeAndKind;

	@Column(precision=38, scale=18)
	private BigDecimal numericValue;

	private Integer sigFigs;
	
	@Column(precision=38, scale=18)
	private BigDecimal uncertainty;
	
	private Integer numberOfReplicates;
	
	@Size(max = 255)
	private String uncertaintyType;

	@Size(max = 25)
	private String unitType;

	@Size(max = 25)
	private String unitKind;
	
	@Size(max = 55)
	@org.hibernate.annotations.Index(name="_UNTK_IDX")
	private String unitTypeAndKind;
	
	@Size(max = 512)
	private String comments;

	@NotNull
	@org.hibernate.annotations.Index(name="_IGN_IDX")
	private boolean ignored;
	
	@org.hibernate.annotations.Index(name="_TRXN_IDX")
	private Long lsTransaction;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "MM")
	private Date recordedDate;
	
	@NotNull
	@Size(max = 255)
	private String recordedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "MM")
	private Date modifiedDate;
	
	@Size(max = 255)
	private String modifiedBy;
	

	@NotNull
	private boolean publicData;

	@Id
	@SequenceGenerator(name = "abstractValueGen", sequenceName = "VALUE_PKSEQ", allocationSize=50)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "abstractValueGen")
	@Column(name = "id")
	private Long id;
  
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

    public void setLsTypeAndKind(String lsTypeAndKind) {
        this.lsTypeAndKind = lsTypeAndKind;
    }
    
    public void setOperatorTypeAndKind(String operatorTypeAndKind) {
    	this.operatorTypeAndKind = operatorTypeAndKind;
    }
    
    public void setUnitTypeAndKind(String unitTypeAndKind) {
        this.unitTypeAndKind = unitTypeAndKind;
    }
    
    public void setNumericValue(BigDecimal numericValue) {
        this.numericValue = numericValue;
    }
    
    
	@PersistenceContext
	transient EntityManager entityManager;

	public static final EntityManager entityManager() {
		EntityManager em = new AbstractValue() {
		}.entityManager;
		if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	@Transactional
	public void persist() {
		if (this.entityManager == null) this.entityManager = entityManager();
		this.setLsTypeAndKind(new StringBuilder().append(this.lsType).append("_").append(this.lsKind).toString());
		this.setCodeTypeAndKind(new StringBuilder().append(this.codeType).append("_").append(this.codeKind).toString());
		this.setUnitTypeAndKind(new StringBuilder().append(this.unitType).append("_").append(this.unitKind).toString());
		this.setOperatorTypeAndKind(new StringBuilder().append(this.operatorType).append("_").append(this.operatorKind).toString());
		if (this.recordedDate == null) this.recordedDate = new Date();
		this.entityManager.persist(this);
	}

	@Transactional
	public AbstractValue merge() {
		if (this.entityManager == null) this.entityManager = entityManager();
		this.setLsTypeAndKind(new StringBuilder().append(this.lsType).append("_").append(this.lsKind).toString());
		this.setCodeTypeAndKind(new StringBuilder().append(this.codeType).append("_").append(this.codeKind).toString());
		this.setUnitTypeAndKind(new StringBuilder().append(this.unitType).append("_").append(this.unitKind).toString());
		this.setOperatorTypeAndKind(new StringBuilder().append(this.operatorType).append("_").append(this.operatorKind).toString());
		this.modifiedDate = new Date();
		AbstractValue merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}

	public static String[] getColumns(){
		String[] headerColumns = new String[] {
				"id",
				"lsType",
				"lsKind",
				"codeType",
				"codeKind",
				"codeValue",
				"stringValue",
				
				"fileValue",
				"urlValue",
				"dateValue",
				"clobValue",
				"operatorType",
				"operatorKind",
				"numericValue",
				"sigFigs",
				"uncertainty",
				"numberOfReplicates",
				
				"uncertaintyType",
				"unitType",
				"unitKind",
				"comments",
				"ignored",
				"lsTransaction",
				"recordedDate",
				"recordedBy",
				"modifiedDate",
				"modifiedBy",
				
				"publicData"
		};
//31 columns
		return headerColumns;

	}

	public static CellProcessor[] getProcessors() {
		final CellProcessor[] processors = new CellProcessor[] { 
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),

				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),

				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),

				new Optional()


		};

		return processors;
	}
	
	
	
}
