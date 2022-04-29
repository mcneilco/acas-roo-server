package com.labsynch.labseer.domain;

import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Entity
@Configurable

public class InteractionType {

    private static final Logger logger = LoggerFactory.getLogger(InteractionType.class);

    @NotNull
    @Column(unique = true)
    @Size(max = 128)
    private String typeName;

    @NotNull
    @Size(max = 128)
    private String typeVerb;

    @Transactional
    public static com.labsynch.labseer.domain.InteractionType getOrCreate(String typeVerb, String typeName) {
        InteractionType itxType = null;
        List<InteractionType> itxTypes = InteractionType.findInteractionTypesByTypeNameEqualsAndTypeVerbEquals(typeName, typeVerb).getResultList();
        if (itxTypes.size() == 0) {
            itxType = new InteractionType();
            itxType.setTypeVerb(typeVerb);
            itxType.setTypeName(typeName);
            itxType.persist();
        } else if (itxTypes.size() == 1) {
            itxType = itxTypes.get(0);
        } else if (itxTypes.size() > 1) {
            logger.error("ERROR: Found multiple interaction types for " + typeName);
            throw new RuntimeException("ERROR: Found multiple interaction types for " + typeName);
        }
        return itxType;
    }

	@Id
    @SequenceGenerator(name = "interactionTypeGen", sequenceName = "INTERACTION_TYPE_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "interactionTypeGen")
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

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "typeName", "typeVerb");

	public static final EntityManager entityManager() {
        EntityManager em = new InteractionType().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countInteractionTypes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM InteractionType o", Long.class).getSingleResult();
    }

	public static List<InteractionType> findAllInteractionTypes() {
        return entityManager().createQuery("SELECT o FROM InteractionType o", InteractionType.class).getResultList();
    }

	public static List<InteractionType> findAllInteractionTypes(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM InteractionType o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, InteractionType.class).getResultList();
    }

	public static InteractionType findInteractionType(Long id) {
        if (id == null) return null;
        return entityManager().find(InteractionType.class, id);
    }

	public static List<InteractionType> findInteractionTypeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM InteractionType o", InteractionType.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<InteractionType> findInteractionTypeEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM InteractionType o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, InteractionType.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

	@Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            InteractionType attached = InteractionType.findInteractionType(this.id);
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
    public InteractionType merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        InteractionType merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String getTypeName() {
        return this.typeName;
    }

	public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

	public String getTypeVerb() {
        return this.typeVerb;
    }

	public void setTypeVerb(String typeVerb) {
        this.typeVerb = typeVerb;
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static InteractionType fromJsonToInteractionType(String json) {
        return new JSONDeserializer<InteractionType>()
        .use(null, InteractionType.class).deserialize(json);
    }

	public static String toJsonArray(Collection<InteractionType> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<InteractionType> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<InteractionType> fromJsonArrayToInteractionTypes(String json) {
        return new JSONDeserializer<List<InteractionType>>()
        .use("values", InteractionType.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public static Long countFindInteractionTypesByTypeNameEquals(String typeName) {
        if (typeName == null || typeName.length() == 0) throw new IllegalArgumentException("The typeName argument is required");
        EntityManager em = InteractionType.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM InteractionType AS o WHERE o.typeName = :typeName", Long.class);
        q.setParameter("typeName", typeName);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindInteractionTypesByTypeNameEqualsAndTypeVerbEquals(String typeName, String typeVerb) {
        if (typeName == null || typeName.length() == 0) throw new IllegalArgumentException("The typeName argument is required");
        if (typeVerb == null || typeVerb.length() == 0) throw new IllegalArgumentException("The typeVerb argument is required");
        EntityManager em = InteractionType.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM InteractionType AS o WHERE o.typeName = :typeName  AND o.typeVerb = :typeVerb", Long.class);
        q.setParameter("typeName", typeName);
        q.setParameter("typeVerb", typeVerb);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<InteractionType> findInteractionTypesByTypeNameEquals(String typeName) {
        if (typeName == null || typeName.length() == 0) throw new IllegalArgumentException("The typeName argument is required");
        EntityManager em = InteractionType.entityManager();
        TypedQuery<InteractionType> q = em.createQuery("SELECT o FROM InteractionType AS o WHERE o.typeName = :typeName", InteractionType.class);
        q.setParameter("typeName", typeName);
        return q;
    }

	public static TypedQuery<InteractionType> findInteractionTypesByTypeNameEquals(String typeName, String sortFieldName, String sortOrder) {
        if (typeName == null || typeName.length() == 0) throw new IllegalArgumentException("The typeName argument is required");
        EntityManager em = InteractionType.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM InteractionType AS o WHERE o.typeName = :typeName");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<InteractionType> q = em.createQuery(queryBuilder.toString(), InteractionType.class);
        q.setParameter("typeName", typeName);
        return q;
    }

	public static TypedQuery<InteractionType> findInteractionTypesByTypeNameEqualsAndTypeVerbEquals(String typeName, String typeVerb) {
        if (typeName == null || typeName.length() == 0) throw new IllegalArgumentException("The typeName argument is required");
        if (typeVerb == null || typeVerb.length() == 0) throw new IllegalArgumentException("The typeVerb argument is required");
        EntityManager em = InteractionType.entityManager();
        TypedQuery<InteractionType> q = em.createQuery("SELECT o FROM InteractionType AS o WHERE o.typeName = :typeName  AND o.typeVerb = :typeVerb", InteractionType.class);
        q.setParameter("typeName", typeName);
        q.setParameter("typeVerb", typeVerb);
        return q;
    }

	public static TypedQuery<InteractionType> findInteractionTypesByTypeNameEqualsAndTypeVerbEquals(String typeName, String typeVerb, String sortFieldName, String sortOrder) {
        if (typeName == null || typeName.length() == 0) throw new IllegalArgumentException("The typeName argument is required");
        if (typeVerb == null || typeVerb.length() == 0) throw new IllegalArgumentException("The typeVerb argument is required");
        EntityManager em = InteractionType.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM InteractionType AS o WHERE o.typeName = :typeName  AND o.typeVerb = :typeVerb");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<InteractionType> q = em.createQuery(queryBuilder.toString(), InteractionType.class);
        q.setParameter("typeName", typeName);
        q.setParameter("typeVerb", typeVerb);
        return q;
    }
}
