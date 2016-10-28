package com.labsynch.labseer.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@Entity
@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(sequenceName = "DDICT_KIND_PKSEQ", finders = { "findDDictKindsByLsTypeEqualsAndNameEquals"})
public class DDictKind {
	
    private static final Logger logger = LoggerFactory.getLogger(DDictKind.class);

    @Size(max = 255)
    private String lsType;
    
    @NotNull
    @Size(max = 255)
    private String name;
    
    @Column(unique = true)
    @Size(max = 255)
	private String lsTypeAndKind;
    
    @Size(max = 512)
    private String description;

    @Size(max = 512)
    private String comments;
    
	@NotNull
	private boolean ignored;

	private Integer displayOrder;

	

	@Id
    @SequenceGenerator(name = "dDictKindGen", sequenceName = "DDICT_KIND_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "dDictKindGen")
    @Column(name = "id")
    private Long id;

	@Version
    @Column(name = "version")
    private Integer version;

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
        EntityManager em = new DDictKind().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countDDictKinds() {
        return entityManager().createQuery("SELECT COUNT(o) FROM DDictKind o", Long.class).getSingleResult();
    }

	public static List<DDictKind> findAllDDictKinds() {
        return entityManager().createQuery("SELECT o FROM DDictKind o", DDictKind.class).getResultList();
    }

	public static DDictKind findDDictKind(Long id) {
        if (id == null) return null;
        return entityManager().find(DDictKind.class, id);
    }

	public static List<DDictKind> findDDictKindEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM DDictKind o", DDictKind.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public void persist() {
		this.setLsTypeAndKind(new StringBuilder().append(this.lsType).append("_").append(this.name).toString());
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

	@Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            DDictKind attached = DDictKind.findDDictKind(this.id);
            this.entityManager.remove(attached);
        }
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

	@Transactional
    public DDictKind merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
		this.setLsTypeAndKind(new StringBuilder().append(this.lsType).append("_").append(this.name).toString());
        DDictKind merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
