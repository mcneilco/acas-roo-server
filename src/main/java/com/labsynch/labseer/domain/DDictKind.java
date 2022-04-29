package com.labsynch.labseer.domain;

import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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

@Configurable
@Entity

public class DDictKind {

    private static final Logger logger = LoggerFactory.getLogger(DDictKind.class);

    @Size(max = 255)
    @JoinColumn(name = "ls_type")
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
        if (em == null)
            throw new IllegalStateException(
                    "Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countDDictKinds() {
        return entityManager().createQuery("SELECT COUNT(o) FROM DDictKind o", Long.class).getSingleResult();
    }

    public static List<DDictKind> findAllDDictKinds() {
        return entityManager().createQuery("SELECT o FROM DDictKind o", DDictKind.class).getResultList();
    }

    public static DDictKind findDDictKind(Long id) {
        if (id == null)
            return null;
        return entityManager().find(DDictKind.class, id);
    }

    public static List<DDictKind> findDDictKindEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM DDictKind o", DDictKind.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public void persist() {
        this.setLsTypeAndKind(new StringBuilder().append(this.lsType).append("_").append(this.name).toString());
        if (this.entityManager == null)
            this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

    @Transactional
    public void remove() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            DDictKind attached = DDictKind.findDDictKind(this.id);
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
    public DDictKind merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        this.setLsTypeAndKind(new StringBuilder().append(this.lsType).append("_").append(this.name).toString());
        DDictKind merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    public String getLsType() {
        return this.lsType;
    }

    public void setLsType(String lsType) {
        this.lsType = lsType;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLsTypeAndKind() {
        return this.lsTypeAndKind;
    }

    public void setLsTypeAndKind(String lsTypeAndKind) {
        this.lsTypeAndKind = lsTypeAndKind;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean isIgnored() {
        return this.ignored;
    }

    public void setIgnored(boolean ignored) {
        this.ignored = ignored;
    }

    public Integer getDisplayOrder() {
        return this.displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public static Long countFindDDictKindsByLsTypeEqualsAndNameEquals(String lsType, String name) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        if (name == null || name.length() == 0)
            throw new IllegalArgumentException("The name argument is required");
        EntityManager em = DDictKind.entityManager();
        TypedQuery q = em.createQuery(
                "SELECT COUNT(o) FROM DDictKind AS o WHERE o.lsType = :lsType  AND o.name = :name", Long.class);
        q.setParameter("lsType", lsType);
        q.setParameter("name", name);
        return ((Long) q.getSingleResult());
    }

    public static TypedQuery<DDictKind> findDDictKindsByLsTypeEqualsAndNameEquals(String lsType, String name) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        if (name == null || name.length() == 0)
            throw new IllegalArgumentException("The name argument is required");
        EntityManager em = DDictKind.entityManager();
        TypedQuery<DDictKind> q = em.createQuery(
                "SELECT o FROM DDictKind AS o WHERE o.lsType = :lsType  AND o.name = :name", DDictKind.class);
        q.setParameter("lsType", lsType);
        q.setParameter("name", name);
        return q;
    }

    public static TypedQuery<DDictKind> findDDictKindsByLsTypeEqualsAndNameEquals(String lsType, String name,
            String sortFieldName, String sortOrder) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        if (name == null || name.length() == 0)
            throw new IllegalArgumentException("The name argument is required");
        EntityManager em = DDictKind.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM DDictKind AS o WHERE o.lsType = :lsType  AND o.name = :name");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<DDictKind> q = em.createQuery(queryBuilder.toString(), DDictKind.class);
        q.setParameter("lsType", lsType);
        q.setParameter("name", name);
        return q;
    }

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "lsType", "name",
            "lsTypeAndKind", "description", "comments", "ignored", "displayOrder", "id", "version", "entityManager");

    public static List<DDictKind> findAllDDictKinds(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM DDictKind o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, DDictKind.class).getResultList();
    }

    public static List<DDictKind> findDDictKindEntries(int firstResult, int maxResults, String sortFieldName,
            String sortOrder) {
        String jpaQuery = "SELECT o FROM DDictKind o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, DDictKind.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }

    public String toJson() {
        return new JSONSerializer()
                .exclude("*.class").serialize(this);
    }

    public String toJson(String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(this);
    }

    public static DDictKind fromJsonToDDictKind(String json) {
        return new JSONDeserializer<DDictKind>()
                .use(null, DDictKind.class).deserialize(json);
    }

    public static String toJsonArray(Collection<DDictKind> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<DDictKind> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<DDictKind> fromJsonArrayToDDictKinds(String json) {
        return new JSONDeserializer<List<DDictKind>>()
                .use("values", DDictKind.class).deserialize(json);
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
