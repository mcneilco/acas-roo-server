package com.labsynch.labseer.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@RooJpaActiveRecord(finders = { "findCodeKindsByLsTypeAndKindEquals", "findCodeKindsByLsType", "findCodeKindsByKindNameEqualsAndLsType" })
public class CodeKind {

	private static final Logger logger = LoggerFactory.getLogger(CodeKind.class);

    @NotNull
    @ManyToOne
    @JoinColumn(name = "ls_type")
    private CodeType lsType;

    @NotNull
    @Size(max = 255)
    private String kindName;

    @Column(unique = true)
    @Size(max = 255)
    private String lsTypeAndKind;

    @Id
    @SequenceGenerator(name = "codeKindGen", sequenceName = "CODE_KIND_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "codeKindGen")
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
        EntityManager em = new CodeKind().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countCodeKinds() {
        return entityManager().createQuery("SELECT COUNT(o) FROM CodeKind o", Long.class).getSingleResult();
    }

    public static List<com.labsynch.labseer.domain.CodeKind> findAllCodeKinds() {
        return entityManager().createQuery("SELECT o FROM CodeKind o", CodeKind.class).getResultList();
    }

    public static com.labsynch.labseer.domain.CodeKind findCodeKind(Long id) {
        if (id == null) return null;
        return entityManager().find(CodeKind.class, id);
    }

    public static List<com.labsynch.labseer.domain.CodeKind> findCodeKindEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM CodeKind o", CodeKind.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.lsType = CodeType.findCodeType(this.getLsType().getId());
        this.lsTypeAndKind = new StringBuilder().append(this.getLsType().getTypeName()).append('_').append(this.getKindName()).toString();
        this.entityManager.persist(this);
    }

    @Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            CodeKind attached = CodeKind.findCodeKind(this.id);
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
    public com.labsynch.labseer.domain.CodeKind merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.lsTypeAndKind = new StringBuilder().append(this.getLsType().getTypeName()).append('_').append(this.getKindName()).toString();
        CodeKind merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static CodeKind getOrCreate(CodeType codeType, String kindName) {
		
		CodeKind codeKind = null;
		List<CodeKind> codeKinds = CodeKind.findCodeKindsByKindNameEqualsAndLsType(kindName, codeType).getResultList();
		
		if (codeKinds.size() == 0){
			codeKind = new CodeKind();
			codeKind.setKindName(kindName);
			codeKind.setLsType(codeType);
			codeKind.persist();
		} else if (codeKinds.size() == 1){
			codeKind = codeKinds.get(0);
		} else if (codeKinds.size() > 1){
			logger.error("ERROR: multiple code kinds with the same name and type");
		}
		
		return codeKind;
	}
}
