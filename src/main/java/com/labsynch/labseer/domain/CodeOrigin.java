package com.labsynch.labseer.domain;

import java.util.Collection;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

public class CodeOrigin {

    private static final Logger logger = LoggerFactory.getLogger(CodeOrigin.class);

    @NotNull
    @Column(unique = true)
    @Size(max = 256)
    private String name;

    public static CodeOrigin getOrCreate(String name) {

        CodeOrigin codeOrigin = null;
        List<CodeOrigin> codeOrigins = CodeOrigin.findCodeOriginsByNameEquals(name).getResultList();
        if (codeOrigins.size() == 0) {
            codeOrigin = new CodeOrigin();
            codeOrigin.setName(name);
            codeOrigin.persist();
        } else if (codeOrigins.size() == 1) {
            codeOrigin = codeOrigins.get(0);
        } else if (codeOrigins.size() > 1) {
            logger.error("ERROR: multiple code types with the same name");
        }

        return codeOrigin;
    }

    public String toJson() {
        return new JSONSerializer()
                .exclude("*.class").serialize(this);
    }

    public String toJson(String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(this);
    }

    public static CodeOrigin fromJsonToCodeOrigin(String json) {
        return new JSONDeserializer<CodeOrigin>()
                .use(null, CodeOrigin.class).deserialize(json);
    }

    public static String toJsonArray(Collection<CodeOrigin> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<CodeOrigin> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<CodeOrigin> fromJsonArrayToCodeOrigins(String json) {
        return new JSONDeserializer<List<CodeOrigin>>()
                .use("values", CodeOrigin.class).deserialize(json);
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @Id
    @SequenceGenerator(name = "codeOriginGen", sequenceName = "CODE_ORIGIN_PKSEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "codeOriginGen")
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

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "name");

    public static final EntityManager entityManager() {
        EntityManager em = new CodeOrigin().entityManager;
        if (em == null)
            throw new IllegalStateException(
                    "Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countCodeOrigins() {
        return entityManager().createQuery("SELECT COUNT(o) FROM CodeOrigin o", Long.class).getSingleResult();
    }

    public static List<CodeOrigin> findAllCodeOrigins() {
        return entityManager().createQuery("SELECT o FROM CodeOrigin o", CodeOrigin.class).getResultList();
    }

    public static List<CodeOrigin> findAllCodeOrigins(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM CodeOrigin o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, CodeOrigin.class).getResultList();
    }

    public static CodeOrigin findCodeOrigin(Long id) {
        if (id == null)
            return null;
        return entityManager().find(CodeOrigin.class, id);
    }

    public static List<CodeOrigin> findCodeOriginEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM CodeOrigin o", CodeOrigin.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }

    public static List<CodeOrigin> findCodeOriginEntries(int firstResult, int maxResults, String sortFieldName,
            String sortOrder) {
        String jpaQuery = "SELECT o FROM CodeOrigin o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, CodeOrigin.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public void persist() {
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
            CodeOrigin attached = CodeOrigin.findCodeOrigin(this.id);
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
    public CodeOrigin merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        CodeOrigin merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Long countFindCodeOriginsByNameEquals(String name) {
        if (name == null || name.length() == 0)
            throw new IllegalArgumentException("The name argument is required");
        EntityManager em = CodeOrigin.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM CodeOrigin AS o WHERE o.name = :name", Long.class);
        q.setParameter("name", name);
        return ((Long) q.getSingleResult());
    }

    public static TypedQuery<CodeOrigin> findCodeOriginsByNameEquals(String name) {
        if (name == null || name.length() == 0)
            throw new IllegalArgumentException("The name argument is required");
        EntityManager em = CodeOrigin.entityManager();
        TypedQuery<CodeOrigin> q = em.createQuery("SELECT o FROM CodeOrigin AS o WHERE o.name = :name",
                CodeOrigin.class);
        q.setParameter("name", name);
        return q;
    }

    public static TypedQuery<CodeOrigin> findCodeOriginsByNameEquals(String name, String sortFieldName,
            String sortOrder) {
        if (name == null || name.length() == 0)
            throw new IllegalArgumentException("The name argument is required");
        EntityManager em = CodeOrigin.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM CodeOrigin AS o WHERE o.name = :name");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<CodeOrigin> q = em.createQuery(queryBuilder.toString(), CodeOrigin.class);
        q.setParameter("name", name);
        return q;
    }
}
