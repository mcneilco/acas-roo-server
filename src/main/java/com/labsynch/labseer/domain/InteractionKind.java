package com.labsynch.labseer.domain;

import java.util.Collection;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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

public class InteractionKind {

    private static final Logger logger = LoggerFactory.getLogger(InteractionKind.class);

    @NotNull
    @ManyToOne
    @JoinColumn(name = "ls_type")
    private InteractionType lsType;

    @NotNull
    @Size(max = 255)
    private String kindName;

    @Column(unique = true)
    @Size(max = 255)
    private String lsTypeAndKind;

    @Id
    @SequenceGenerator(name = "interactionKindGen", sequenceName = "INTERACTION_KIND_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "interactionKindGen")
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
        EntityManager em = new InteractionKind().entityManager;
        if (em == null)
            throw new IllegalStateException(
                    "Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countInteractionKinds() {
        return entityManager().createQuery("SELECT COUNT(o) FROM InteractionKind o", Long.class).getSingleResult();
    }

    public static List<com.labsynch.labseer.domain.InteractionKind> findAllInteractionKinds() {
        return entityManager().createQuery("SELECT o FROM InteractionKind o", InteractionKind.class).getResultList();
    }

    public static com.labsynch.labseer.domain.InteractionKind findInteractionKind(Long id_) {
        if (id_ == null)
            return null;
        return entityManager().find(InteractionKind.class, id_);
    }

    public static List<com.labsynch.labseer.domain.InteractionKind> findInteractionKindEntries(int firstResult,
            int maxResults) {
        return entityManager().createQuery("SELECT o FROM InteractionKind o", InteractionKind.class)
                .setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public void persist() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        this.lsTypeAndKind = new StringBuilder().append(this.getLsType().getTypeName()).append('_')
                .append(this.getKindName()).toString();
        this.entityManager.persist(this);
    }

    @Transactional
    public void remove() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            InteractionKind attached = InteractionKind.findInteractionKind(this.id);
            this.entityManager.remove(attached);
        }
    }

    @Transactional
    public void flush() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        this.entityManager.flush();
    }

    @Transactional
    public void clear() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        this.entityManager.clear();
    }

    @Transactional
    public com.labsynch.labseer.domain.InteractionKind merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        this.lsTypeAndKind = new StringBuilder().append(this.getLsType().getTypeName()).append('_')
                .append(this.getKindName()).toString();
        InteractionKind merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    @Transactional
    public static InteractionKind getOrCreate(String typeName, String kindName) {
        InteractionType itxType = InteractionType.findInteractionTypesByTypeNameEquals(typeName).getSingleResult();
        InteractionKind itxKind = null;
        List<InteractionKind> itxKinds = InteractionKind
                .findInteractionKindsByKindNameEqualsAndLsType(kindName, itxType).getResultList();
        if (itxKinds.size() == 0) {
            itxKind = new InteractionKind();
            itxKind.setLsType(itxType);
            itxKind.setKindName(kindName);
            itxKind.persist();
        } else if (itxKinds.size() == 1) {
            itxKind = itxKinds.get(0);
        } else if (itxKinds.size() > 1) {
            // ERROR CASE
            logger.error("ERROR: Found multiple interaction kinds for " + kindName);
            throw new RuntimeException("ERROR: Found multiple interaction kinds for " + kindName);
        }

        return itxKind;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "lsType",
            "kindName", "lsTypeAndKind", "id", "version", "entityManager");

    public static List<InteractionKind> findAllInteractionKinds(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM InteractionKind o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, InteractionKind.class).getResultList();
    }

    public static List<InteractionKind> findInteractionKindEntries(int firstResult, int maxResults,
            String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM InteractionKind o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, InteractionKind.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }

    public static Long countFindInteractionKindsByKindNameEquals(String kindName) {
        if (kindName == null || kindName.length() == 0)
            throw new IllegalArgumentException("The kindName argument is required");
        EntityManager em = InteractionKind.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM InteractionKind AS o WHERE o.kindName = :kindName",
                Long.class);
        q.setParameter("kindName", kindName);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindInteractionKindsByKindNameEqualsAndLsType(String kindName, InteractionType lsType) {
        if (kindName == null || kindName.length() == 0)
            throw new IllegalArgumentException("The kindName argument is required");
        if (lsType == null)
            throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = InteractionKind.entityManager();
        TypedQuery q = em.createQuery(
                "SELECT COUNT(o) FROM InteractionKind AS o WHERE o.kindName = :kindName  AND o.lsType = :lsType",
                Long.class);
        q.setParameter("kindName", kindName);
        q.setParameter("lsType", lsType);
        return ((Long) q.getSingleResult());
    }

    public static TypedQuery<InteractionKind> findInteractionKindsByKindNameEquals(String kindName) {
        if (kindName == null || kindName.length() == 0)
            throw new IllegalArgumentException("The kindName argument is required");
        EntityManager em = InteractionKind.entityManager();
        TypedQuery<InteractionKind> q = em
                .createQuery("SELECT o FROM InteractionKind AS o WHERE o.kindName = :kindName", InteractionKind.class);
        q.setParameter("kindName", kindName);
        return q;
    }

    public static TypedQuery<InteractionKind> findInteractionKindsByKindNameEquals(String kindName,
            String sortFieldName, String sortOrder) {
        if (kindName == null || kindName.length() == 0)
            throw new IllegalArgumentException("The kindName argument is required");
        EntityManager em = InteractionKind.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM InteractionKind AS o WHERE o.kindName = :kindName");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<InteractionKind> q = em.createQuery(queryBuilder.toString(), InteractionKind.class);
        q.setParameter("kindName", kindName);
        return q;
    }

    public static TypedQuery<InteractionKind> findInteractionKindsByKindNameEqualsAndLsType(String kindName,
            InteractionType lsType) {
        if (kindName == null || kindName.length() == 0)
            throw new IllegalArgumentException("The kindName argument is required");
        if (lsType == null)
            throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = InteractionKind.entityManager();
        TypedQuery<InteractionKind> q = em.createQuery(
                "SELECT o FROM InteractionKind AS o WHERE o.kindName = :kindName  AND o.lsType = :lsType",
                InteractionKind.class);
        q.setParameter("kindName", kindName);
        q.setParameter("lsType", lsType);
        return q;
    }

    public static TypedQuery<InteractionKind> findInteractionKindsByKindNameEqualsAndLsType(String kindName,
            InteractionType lsType, String sortFieldName, String sortOrder) {
        if (kindName == null || kindName.length() == 0)
            throw new IllegalArgumentException("The kindName argument is required");
        if (lsType == null)
            throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = InteractionKind.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM InteractionKind AS o WHERE o.kindName = :kindName  AND o.lsType = :lsType");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<InteractionKind> q = em.createQuery(queryBuilder.toString(), InteractionKind.class);
        q.setParameter("kindName", kindName);
        q.setParameter("lsType", lsType);
        return q;
    }

    public String toJson() {
        return new JSONSerializer()
                .exclude("*.class").serialize(this);
    }

    public String toJson(String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(this);
    }

    public static InteractionKind fromJsonToInteractionKind(String json) {
        return new JSONDeserializer<InteractionKind>()
                .use(null, InteractionKind.class).deserialize(json);
    }

    public static String toJsonArray(Collection<InteractionKind> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<InteractionKind> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<InteractionKind> fromJsonArrayToInteractionKinds(String json) {
        return new JSONDeserializer<List<InteractionKind>>()
                .use("values", InteractionKind.class).deserialize(json);
    }

    public InteractionType getLsType() {
        return this.lsType;
    }

    public void setLsType(InteractionType lsType) {
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
