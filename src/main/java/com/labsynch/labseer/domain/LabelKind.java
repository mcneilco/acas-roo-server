package com.labsynch.labseer.domain;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
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
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(sequenceName = "LABEL_KIND_PKSEQ", finders = { "findLabelKindsByLsType", "findLabelKindsByKindNameEquals", "findLabelKindsByKindNameEqualsAndLsType" })
public class LabelKind {
	
	private static final Logger logger = LoggerFactory.getLogger(LabelKind.class);


    @ManyToOne
    @JoinColumn(name = "ls_type")
    private LabelType lsType;

    @NotNull
    @Size(max = 255)
    private String kindName;

    @Column(unique = true)
    @Size(max = 255)
    private String lsTypeAndKind;

    @Id
    @SequenceGenerator(name = "labelKindGen", sequenceName = "LABEL_KIND_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "labelKindGen")
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
        EntityManager em = new LabelKind().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countLabelKinds() {
        return entityManager().createQuery("SELECT COUNT(o) FROM LabelKind o", Long.class).getSingleResult();
    }

    public static List<com.labsynch.labseer.domain.LabelKind> findAllLabelKinds() {
        return entityManager().createQuery("SELECT o FROM LabelKind o", LabelKind.class).getResultList();
    }

    public static com.labsynch.labseer.domain.LabelKind findLabelKind(Long id) {
        if (id == null) return null;
        return entityManager().find(LabelKind.class, id);
    }

    public static List<com.labsynch.labseer.domain.LabelKind> findLabelKindEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM LabelKind o", LabelKind.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.lsType = LabelType.findLabelType(this.getLsType().getId());
        this.lsTypeAndKind = new StringBuilder().append(this.getLsType().getTypeName()).append('_').append(this.getKindName()).toString();
        this.entityManager.persist(this);
    }

    @Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            LabelKind attached = LabelKind.findLabelKind(this.id);
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
    public com.labsynch.labseer.domain.LabelKind merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.lsTypeAndKind = new StringBuilder().append(this.getLsType().getTypeName()).append('_').append(this.getKindName()).toString();
        LabelKind merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    

	public static LabelKind getOrCreate(LabelType lsType, String kindName) {
		
		LabelKind lsKind = null;
		List<LabelKind> lsKinds = LabelKind.findLabelKindsByKindNameEqualsAndLsType(kindName, lsType).getResultList();
		
		if (lsKinds.size() == 0){
			lsKind = new LabelKind();
			lsKind.setKindName(kindName);
			lsKind.setLsType(lsType);
			lsKind.persist();
		} else if (lsKinds.size() == 1){
			lsKind = lsKinds.get(0);
		} else if (lsKinds.size() > 1){
			logger.error("ERROR: multiple label kinds with the same name and type");
		}
		
		return lsKind;
	}

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "lsType", "kindName", "lsTypeAndKind", "id", "version", "entityManager");

	public static List<LabelKind> findAllLabelKinds(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM LabelKind o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, LabelKind.class).getResultList();
    }

	public static List<LabelKind> findLabelKindEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM LabelKind o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, LabelKind.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static LabelKind fromJsonToLabelKind(String json) {
        return new JSONDeserializer<LabelKind>()
        .use(null, LabelKind.class).deserialize(json);
    }

	public static String toJsonArray(Collection<LabelKind> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<LabelKind> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<LabelKind> fromJsonArrayToLabelKinds(String json) {
        return new JSONDeserializer<List<LabelKind>>()
        .use("values", LabelKind.class).deserialize(json);
    }

	public static Long countFindLabelKindsByKindNameEquals(String kindName) {
        if (kindName == null || kindName.length() == 0) throw new IllegalArgumentException("The kindName argument is required");
        EntityManager em = LabelKind.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM LabelKind AS o WHERE o.kindName = :kindName", Long.class);
        q.setParameter("kindName", kindName);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindLabelKindsByKindNameEqualsAndLsType(String kindName, LabelType lsType) {
        if (kindName == null || kindName.length() == 0) throw new IllegalArgumentException("The kindName argument is required");
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = LabelKind.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM LabelKind AS o WHERE o.kindName = :kindName  AND o.lsType = :lsType", Long.class);
        q.setParameter("kindName", kindName);
        q.setParameter("lsType", lsType);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindLabelKindsByLsType(LabelType lsType) {
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = LabelKind.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM LabelKind AS o WHERE o.lsType = :lsType", Long.class);
        q.setParameter("lsType", lsType);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<LabelKind> findLabelKindsByKindNameEquals(String kindName) {
        if (kindName == null || kindName.length() == 0) throw new IllegalArgumentException("The kindName argument is required");
        EntityManager em = LabelKind.entityManager();
        TypedQuery<LabelKind> q = em.createQuery("SELECT o FROM LabelKind AS o WHERE o.kindName = :kindName", LabelKind.class);
        q.setParameter("kindName", kindName);
        return q;
    }

	public static TypedQuery<LabelKind> findLabelKindsByKindNameEquals(String kindName, String sortFieldName, String sortOrder) {
        if (kindName == null || kindName.length() == 0) throw new IllegalArgumentException("The kindName argument is required");
        EntityManager em = LabelKind.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM LabelKind AS o WHERE o.kindName = :kindName");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<LabelKind> q = em.createQuery(queryBuilder.toString(), LabelKind.class);
        q.setParameter("kindName", kindName);
        return q;
    }

	public static TypedQuery<LabelKind> findLabelKindsByKindNameEqualsAndLsType(String kindName, LabelType lsType) {
        if (kindName == null || kindName.length() == 0) throw new IllegalArgumentException("The kindName argument is required");
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = LabelKind.entityManager();
        TypedQuery<LabelKind> q = em.createQuery("SELECT o FROM LabelKind AS o WHERE o.kindName = :kindName  AND o.lsType = :lsType", LabelKind.class);
        q.setParameter("kindName", kindName);
        q.setParameter("lsType", lsType);
        return q;
    }

	public static TypedQuery<LabelKind> findLabelKindsByKindNameEqualsAndLsType(String kindName, LabelType lsType, String sortFieldName, String sortOrder) {
        if (kindName == null || kindName.length() == 0) throw new IllegalArgumentException("The kindName argument is required");
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = LabelKind.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM LabelKind AS o WHERE o.kindName = :kindName  AND o.lsType = :lsType");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<LabelKind> q = em.createQuery(queryBuilder.toString(), LabelKind.class);
        q.setParameter("kindName", kindName);
        q.setParameter("lsType", lsType);
        return q;
    }

	public static TypedQuery<LabelKind> findLabelKindsByLsType(LabelType lsType) {
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = LabelKind.entityManager();
        TypedQuery<LabelKind> q = em.createQuery("SELECT o FROM LabelKind AS o WHERE o.lsType = :lsType", LabelKind.class);
        q.setParameter("lsType", lsType);
        return q;
    }

	public static TypedQuery<LabelKind> findLabelKindsByLsType(LabelType lsType, String sortFieldName, String sortOrder) {
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = LabelKind.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM LabelKind AS o WHERE o.lsType = :lsType");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<LabelKind> q = em.createQuery(queryBuilder.toString(), LabelKind.class);
        q.setParameter("lsType", lsType);
        return q;
    }

	public LabelType getLsType() {
        return this.lsType;
    }

	public void setLsType(LabelType lsType) {
        this.lsType = lsType;
    }

	public String getKindName() {
        return this.kindName;
    }

	public void setKindName(String kindName) {
        this.kindName = kindName;
    }

	public String getLsTypeAndKind() {
        return this.lsTypeAndKind;
    }

	public void setLsTypeAndKind(String lsTypeAndKind) {
        this.lsTypeAndKind = lsTypeAndKind;
    }
}
