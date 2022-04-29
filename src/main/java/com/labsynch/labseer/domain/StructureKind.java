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
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
public class StructureKind {

    private static final Logger logger = LoggerFactory.getLogger(StructureKind.class);

    @NotNull
    @ManyToOne
    @JoinColumn(name = "ls_type")
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

	public static Long countFindStructureKindsByKindNameEquals(String kindName) {
        if (kindName == null || kindName.length() == 0) throw new IllegalArgumentException("The kindName argument is required");
        EntityManager em = StructureKind.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM StructureKind AS o WHERE o.kindName = :kindName", Long.class);
        q.setParameter("kindName", kindName);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindStructureKindsByKindNameEqualsAndLsType(String kindName, StructureType lsType) {
        if (kindName == null || kindName.length() == 0) throw new IllegalArgumentException("The kindName argument is required");
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = StructureKind.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM StructureKind AS o WHERE o.kindName = :kindName  AND o.lsType = :lsType", Long.class);
        q.setParameter("kindName", kindName);
        q.setParameter("lsType", lsType);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindStructureKindsByLsType(StructureType lsType) {
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = StructureKind.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM StructureKind AS o WHERE o.lsType = :lsType", Long.class);
        q.setParameter("lsType", lsType);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<StructureKind> findStructureKindsByKindNameEquals(String kindName) {
        if (kindName == null || kindName.length() == 0) throw new IllegalArgumentException("The kindName argument is required");
        EntityManager em = StructureKind.entityManager();
        TypedQuery<StructureKind> q = em.createQuery("SELECT o FROM StructureKind AS o WHERE o.kindName = :kindName", StructureKind.class);
        q.setParameter("kindName", kindName);
        return q;
    }

	public static TypedQuery<StructureKind> findStructureKindsByKindNameEquals(String kindName, String sortFieldName, String sortOrder) {
        if (kindName == null || kindName.length() == 0) throw new IllegalArgumentException("The kindName argument is required");
        EntityManager em = StructureKind.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM StructureKind AS o WHERE o.kindName = :kindName");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<StructureKind> q = em.createQuery(queryBuilder.toString(), StructureKind.class);
        q.setParameter("kindName", kindName);
        return q;
    }

	public static TypedQuery<StructureKind> findStructureKindsByKindNameEqualsAndLsType(String kindName, StructureType lsType) {
        if (kindName == null || kindName.length() == 0) throw new IllegalArgumentException("The kindName argument is required");
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = StructureKind.entityManager();
        TypedQuery<StructureKind> q = em.createQuery("SELECT o FROM StructureKind AS o WHERE o.kindName = :kindName  AND o.lsType = :lsType", StructureKind.class);
        q.setParameter("kindName", kindName);
        q.setParameter("lsType", lsType);
        return q;
    }

	public static TypedQuery<StructureKind> findStructureKindsByKindNameEqualsAndLsType(String kindName, StructureType lsType, String sortFieldName, String sortOrder) {
        if (kindName == null || kindName.length() == 0) throw new IllegalArgumentException("The kindName argument is required");
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = StructureKind.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM StructureKind AS o WHERE o.kindName = :kindName  AND o.lsType = :lsType");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<StructureKind> q = em.createQuery(queryBuilder.toString(), StructureKind.class);
        q.setParameter("kindName", kindName);
        q.setParameter("lsType", lsType);
        return q;
    }

	public static TypedQuery<StructureKind> findStructureKindsByLsType(StructureType lsType) {
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = StructureKind.entityManager();
        TypedQuery<StructureKind> q = em.createQuery("SELECT o FROM StructureKind AS o WHERE o.lsType = :lsType", StructureKind.class);
        q.setParameter("lsType", lsType);
        return q;
    }

	public static TypedQuery<StructureKind> findStructureKindsByLsType(StructureType lsType, String sortFieldName, String sortOrder) {
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = StructureKind.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM StructureKind AS o WHERE o.lsType = :lsType");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<StructureKind> q = em.createQuery(queryBuilder.toString(), StructureKind.class);
        q.setParameter("lsType", lsType);
        return q;
    }

	public StructureType getLsType() {
        return this.lsType;
    }

	public void setLsType(StructureType lsType) {
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

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "lsType", "kindName", "lsTypeAndKind", "id", "version", "entityManager");

	public static List<StructureKind> findAllStructureKinds(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM StructureKind o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, StructureKind.class).getResultList();
    }

	public static List<StructureKind> findStructureKindEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM StructureKind o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, StructureKind.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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

	public static StructureKind fromJsonToStructureKind(String json) {
        return new JSONDeserializer<StructureKind>()
        .use(null, StructureKind.class).deserialize(json);
    }

	public static String toJsonArray(Collection<StructureKind> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<StructureKind> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<StructureKind> fromJsonArrayToStructureKinds(String json) {
        return new JSONDeserializer<List<StructureKind>>()
        .use("values", StructureKind.class).deserialize(json);
    }
}
