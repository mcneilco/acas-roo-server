package com.labsynch.labseer.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)

@Transactional
public abstract class AbstractThing {

	@Id
	@SequenceGenerator(name = "abstractThingGen", sequenceName = "THING_PKSEQ", allocationSize=10)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "abstractThingGen")
	@Column(name = "id")
	private Long id;
	
    @Version
    @Column(name = "version")
    private Integer version;
	
	@NotNull
	@Size(max = 255)
	private String lsType;
	
	@NotNull
	@Size(max = 255)
	private String lsKind;	

	@Size(max = 255)
	private String lsTypeAndKind;
	
    @Column(unique = true)
	@Size(max = 255)
	private String codeName;

	@NotNull
	@Size(max = 255)
	private String recordedBy;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "MM")   
	private Date recordedDate;

	@Size(max = 255)
	private String modifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "MM")
	private Date modifiedDate;

	@NotNull
	private boolean ignored;
	
	@NotNull
	private boolean deleted;

	private Long lsTransaction;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "thing")
	private Set<ThingPage> thingPage = new HashSet<ThingPage>();

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}


    
    public Integer getVersion() {
        return this.version;
    }
    
    public void setVersion(Integer version) {
        this.version = version;
    }

	@PersistenceContext
	transient EntityManager entityManager;

	public static final EntityManager entityManager() {
		EntityManager em = new AbstractThing() {
		}.entityManager;
		if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	@Transactional
	public void persist() {
		if (this.entityManager == null) this.entityManager = entityManager();
		this.setLsTypeAndKind(new StringBuilder().append(this.lsType).append("_").append(this.lsKind).toString());
		if (this.recordedDate == null) this.recordedDate = new Date();
		this.entityManager.persist(this);
	}
		
	@Transactional
	public AbstractThing merge() {
		if (this.entityManager == null) this.entityManager = entityManager();
		this.setLsTypeAndKind(new StringBuilder().append(this.lsType).append("_").append(this.lsKind).toString());
		this.modifiedDate = new Date();
		AbstractThing merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}


	public static long countAbstractThings() {
        return entityManager().createQuery("SELECT COUNT(o) FROM AbstractThing o", Long.class).getSingleResult();
    }

	public static List<AbstractThing> findAllAbstractThings() {
        return entityManager().createQuery("SELECT o FROM AbstractThing o", AbstractThing.class).getResultList();
    }

	public static AbstractThing findAbstractThing(Long id) {
        if (id == null) return null;
        return entityManager().find(AbstractThing.class, id);
    }

	public static List<AbstractThing> findAbstractThingEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM AbstractThing o", AbstractThing.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            AbstractThing attached = AbstractThing.findAbstractThing(this.id);
            this.entityManager.remove(attached);
        }
    }
	
    @Transactional
    public void logicalDelete() {
    	this.setIgnored(true);
    	this.setDeleted(true);
    }


	

	public String getLsType() {
        return this.lsType;
    }

	public void setLsType(String lsType) {
        this.lsType = lsType;
    }

	public String getLsKind() {
        return this.lsKind;
    }

	public void setLsKind(String lsKind) {
        this.lsKind = lsKind;
    }

	public String getLsTypeAndKind() {
        return this.lsTypeAndKind;
    }

	public void setLsTypeAndKind(String lsTypeAndKind) {
        this.lsTypeAndKind = lsTypeAndKind;
    }

	public String getCodeName() {
        return this.codeName;
    }

	public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

	public String getRecordedBy() {
        return this.recordedBy;
    }

	public void setRecordedBy(String recordedBy) {
        this.recordedBy = recordedBy;
    }

	public Date getRecordedDate() {
        return this.recordedDate;
    }

	public void setRecordedDate(Date recordedDate) {
        this.recordedDate = recordedDate;
    }

	public String getModifiedBy() {
        return this.modifiedBy;
    }

	public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

	public Date getModifiedDate() {
        return this.modifiedDate;
    }

	public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

	public boolean isIgnored() {
        return this.ignored;
    }

	public void setIgnored(boolean ignored) {
        this.ignored = ignored;
    }

	public boolean isDeleted() {
        return this.deleted;
    }

	public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

	public Long getLsTransaction() {
        return this.lsTransaction;
    }

	public void setLsTransaction(Long lsTransaction) {
        this.lsTransaction = lsTransaction;
    }

	public Set<ThingPage> getThingPage() {
        return this.thingPage;
    }

	public void setThingPage(Set<ThingPage> thingPage) {
        this.thingPage = thingPage;
    }

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("id", "version", "lsType", "lsKind", "lsTypeAndKind", "codeName", "recordedBy", "recordedDate", "modifiedBy", "modifiedDate", "ignored", "deleted", "lsTransaction", "thingPage", "entityManager");

	public static List<AbstractThing> findAllAbstractThings(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM AbstractThing o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, AbstractThing.class).getResultList();
    }

	public static List<AbstractThing> findAbstractThingEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM AbstractThing o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, AbstractThing.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

	@Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
