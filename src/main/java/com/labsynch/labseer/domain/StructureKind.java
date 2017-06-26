package com.labsynch.labseer.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
@RooJpaActiveRecord(sequenceName = "Structure_KIND_PKSEQ", finders = { "findStructureKindsByLsType", 
		"findStructureKindsByKindNameEquals", "findStructureKindsByKindNameEqualsAndLsType" })
public class StructureKind {

    private static final Logger logger = LoggerFactory.getLogger(StructureKind.class);

    @NotNull
    @ManyToOne
    private StructureType lsType;

    @NotNull
    @Size(max = 64)
    private String kindName;

    @Size(max = 255)
    @Column(unique = true)
    private String lsTypeAndKind;

    @Id
    @SequenceGenerator(name = "StructureKindGen", sequenceName = "Structure_KIND_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "StructureKindGen")
    @Column(name = "id")
    private Long id;

    @Version
    @Column(name = "version")
    private Integer version;

    @PersistenceContext
    transient EntityManager entityManager;

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

    public static final EntityManager entityManager() {
        EntityManager em = new StructureKind().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countStructureKinds() {
        return entityManager().createQuery("SELECT COUNT(o) FROM StructureKind o", Long.class).getSingleResult();
    }

    public static List<com.labsynch.labseer.domain.StructureKind> findAllStructureKinds() {
        return entityManager().createQuery("SELECT o FROM StructureKind o", StructureKind.class).getResultList();
    }

    public static com.labsynch.labseer.domain.StructureKind findStructureKind(Long id) {
        if (id == null) return null;
        return entityManager().find(StructureKind.class, id);
    }

    public static List<com.labsynch.labseer.domain.StructureKind> findStructureKindEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM StructureKind o", StructureKind.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.lsType = StructureType.findStructureType(this.getLsType().getId());
        this.lsTypeAndKind = new StringBuilder().append(this.getLsType().getTypeName()).append('_').append(this.getKindName()).toString();
        this.entityManager.persist(this);
    }

    @Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            StructureKind attached = StructureKind.findStructureKind(this.id);
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
    public com.labsynch.labseer.domain.StructureKind merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.lsTypeAndKind = new StringBuilder().append(this.getLsType().getTypeName()).append('_').append(this.getKindName()).toString();
        StructureKind merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    public static StructureKind getOrCreate(StructureType lsType, String kindName) {
    	StructureKind lsKind = null;
		List<StructureKind> lsKinds = StructureKind.findStructureKindsByKindNameEqualsAndLsType(kindName, lsType).getResultList();
		
		if (lsKinds.size() == 0){
			lsKind = new StructureKind();
			lsKind.setKindName(kindName);
			lsKind.setLsType(lsType);
			lsKind.persist();
		} else if (lsKinds.size() == 1){
			lsKind = lsKinds.get(0);
		} else if (lsKinds.size() > 1){
			logger.error("ERROR: multiple Structure kinds with the same name and type");
		}
		
		return lsKind;
	}
}
