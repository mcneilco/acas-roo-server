package com.labsynch.labseer.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(sequenceName = "THING_PKSEQ", inheritanceType = "TABLE_PER_CLASS")
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


	
}
