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

public class ThingKind {

	private static final Logger logger = LoggerFactory.getLogger(ThingKind.class);

    @NotNull
    @ManyToOne
    @JoinColumn(name = "ls_type")
    private ThingType lsType;

    @NotNull
    @Size(max = 255)
    private String kindName;

    @Column(unique = true)
    @Size(max = 255)
    private String lsTypeAndKind;

    @Id
    @SequenceGenerator(name = "thingKindGen", sequenceName = "THING_KIND_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "thingKindGen")
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
        EntityManager em = new ThingKind().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countThingKinds() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ThingKind o", Long.class).getSingleResult();
    }

    public static List<com.labsynch.labseer.domain.ThingKind> findAllThingKinds() {
        return entityManager().createQuery("SELECT o FROM ThingKind o", ThingKind.class).getResultList();
    }

    public static com.labsynch.labseer.domain.ThingKind findThingKind(Long id) {
        if (id == null) return null;
        return entityManager().find(ThingKind.class, id);
    }

    public static List<com.labsynch.labseer.domain.ThingKind> findThingKindEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ThingKind o", ThingKind.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.lsType = ThingType.findThingType(this.getLsType().getId());
        this.lsTypeAndKind = new StringBuilder().append(this.getLsType().getTypeName()).append('_').append(this.getKindName()).toString();
        this.entityManager.persist(this);
    }

    @Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            ThingKind attached = ThingKind.findThingKind(this.id);
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
    public com.labsynch.labseer.domain.ThingKind merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.lsTypeAndKind = new StringBuilder().append(this.getLsType().getTypeName()).append('_').append(this.getKindName()).toString();
        ThingKind merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static ThingKind getOrCreate(ThingType thingType, String kindName) {
		
		ThingKind thingKind = null;
		List<ThingKind> thingKinds = ThingKind.findThingKindsByKindNameEqualsAndLsType(kindName, thingType).getResultList();
		
		if (thingKinds.size() == 0){
			thingKind = new ThingKind();
			thingKind.setKindName(kindName);
			thingKind.setLsType(thingType);
			thingKind.persist();
		} else if (thingKinds.size() == 1){
			thingKind = thingKinds.get(0);
		} else if (thingKinds.size() > 1){
			logger.error("ERROR: multiple thing kinds with the same name and type");
		}
		
		return thingKind;
	}

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static ThingKind fromJsonToThingKind(String json) {
        return new JSONDeserializer<ThingKind>()
        .use(null, ThingKind.class).deserialize(json);
    }

	public static String toJsonArray(Collection<ThingKind> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<ThingKind> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<ThingKind> fromJsonArrayToThingKinds(String json) {
        return new JSONDeserializer<List<ThingKind>>()
        .use("values", ThingKind.class).deserialize(json);
    }

	public ThingType getLsType() {
        return this.lsType;
    }

	public void setLsType(ThingType lsType) {
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

	public static List<ThingKind> findAllThingKinds(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ThingKind o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ThingKind.class).getResultList();
    }

	public static List<ThingKind> findThingKindEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ThingKind o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ThingKind.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public static Long countFindThingKindsByKindNameEqualsAndLsType(String kindName, ThingType lsType) {
        if (kindName == null || kindName.length() == 0) throw new IllegalArgumentException("The kindName argument is required");
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = ThingKind.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ThingKind AS o WHERE o.kindName = :kindName  AND o.lsType = :lsType", Long.class);
        q.setParameter("kindName", kindName);
        q.setParameter("lsType", lsType);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindThingKindsByLsType(ThingType lsType) {
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = ThingKind.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ThingKind AS o WHERE o.lsType = :lsType", Long.class);
        q.setParameter("lsType", lsType);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindThingKindsByLsTypeAndKindEquals(String lsTypeAndKind) {
        if (lsTypeAndKind == null || lsTypeAndKind.length() == 0) throw new IllegalArgumentException("The lsTypeAndKind argument is required");
        EntityManager em = ThingKind.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ThingKind AS o WHERE o.lsTypeAndKind = :lsTypeAndKind", Long.class);
        q.setParameter("lsTypeAndKind", lsTypeAndKind);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<ThingKind> findThingKindsByKindNameEqualsAndLsType(String kindName, ThingType lsType) {
        if (kindName == null || kindName.length() == 0) throw new IllegalArgumentException("The kindName argument is required");
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = ThingKind.entityManager();
        TypedQuery<ThingKind> q = em.createQuery("SELECT o FROM ThingKind AS o WHERE o.kindName = :kindName  AND o.lsType = :lsType", ThingKind.class);
        q.setParameter("kindName", kindName);
        q.setParameter("lsType", lsType);
        return q;
    }

	public static TypedQuery<ThingKind> findThingKindsByKindNameEqualsAndLsType(String kindName, ThingType lsType, String sortFieldName, String sortOrder) {
        if (kindName == null || kindName.length() == 0) throw new IllegalArgumentException("The kindName argument is required");
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = ThingKind.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ThingKind AS o WHERE o.kindName = :kindName  AND o.lsType = :lsType");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ThingKind> q = em.createQuery(queryBuilder.toString(), ThingKind.class);
        q.setParameter("kindName", kindName);
        q.setParameter("lsType", lsType);
        return q;
    }

	public static TypedQuery<ThingKind> findThingKindsByLsType(ThingType lsType) {
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = ThingKind.entityManager();
        TypedQuery<ThingKind> q = em.createQuery("SELECT o FROM ThingKind AS o WHERE o.lsType = :lsType", ThingKind.class);
        q.setParameter("lsType", lsType);
        return q;
    }

	public static TypedQuery<ThingKind> findThingKindsByLsType(ThingType lsType, String sortFieldName, String sortOrder) {
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = ThingKind.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ThingKind AS o WHERE o.lsType = :lsType");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ThingKind> q = em.createQuery(queryBuilder.toString(), ThingKind.class);
        q.setParameter("lsType", lsType);
        return q;
    }

	public static TypedQuery<ThingKind> findThingKindsByLsTypeAndKindEquals(String lsTypeAndKind) {
        if (lsTypeAndKind == null || lsTypeAndKind.length() == 0) throw new IllegalArgumentException("The lsTypeAndKind argument is required");
        EntityManager em = ThingKind.entityManager();
        TypedQuery<ThingKind> q = em.createQuery("SELECT o FROM ThingKind AS o WHERE o.lsTypeAndKind = :lsTypeAndKind", ThingKind.class);
        q.setParameter("lsTypeAndKind", lsTypeAndKind);
        return q;
    }

	public static TypedQuery<ThingKind> findThingKindsByLsTypeAndKindEquals(String lsTypeAndKind, String sortFieldName, String sortOrder) {
        if (lsTypeAndKind == null || lsTypeAndKind.length() == 0) throw new IllegalArgumentException("The lsTypeAndKind argument is required");
        EntityManager em = ThingKind.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ThingKind AS o WHERE o.lsTypeAndKind = :lsTypeAndKind");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ThingKind> q = em.createQuery(queryBuilder.toString(), ThingKind.class);
        q.setParameter("lsTypeAndKind", lsTypeAndKind);
        return q;
    }
}
