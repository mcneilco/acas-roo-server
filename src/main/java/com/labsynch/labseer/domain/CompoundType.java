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
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Configurable
@Entity

public class CompoundType {

	@NotNull
    private String code;
    
    @NotNull
    private String name;
    
    private Integer displayOrder;
    
    private String comment;
    

	public String getCode() {
        return this.code;
    }

	public void setCode(String code) {
        this.code = code;
    }

	public String getName() {
        return this.name;
    }

	public void setName(String name) {
        this.name = name;
    }

	public Integer getDisplayOrder() {
        return this.displayOrder;
    }

	public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

	public String getComment() {
        return this.comment;
    }

	public void setComment(String comment) {
        this.comment = comment;
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

	public static CompoundType fromJsonToCompoundType(String json) {
        return new JSONDeserializer<CompoundType>()
        .use(null, CompoundType.class).deserialize(json);
    }

	public static String toJsonArray(Collection<CompoundType> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<CompoundType> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<CompoundType> fromJsonArrayToCompoundTypes(String json) {
        return new JSONDeserializer<List<CompoundType>>()
        .use("values", CompoundType.class).deserialize(json);
    }

	public static Long countFindCompoundTypesByCodeEquals(String code) {
        if (code == null || code.length() == 0) throw new IllegalArgumentException("The code argument is required");
        EntityManager em = CompoundType.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM CompoundType AS o WHERE o.code = :code", Long.class);
        q.setParameter("code", code);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<CompoundType> findCompoundTypesByCodeEquals(String code) {
        if (code == null || code.length() == 0) throw new IllegalArgumentException("The code argument is required");
        EntityManager em = CompoundType.entityManager();
        TypedQuery<CompoundType> q = em.createQuery("SELECT o FROM CompoundType AS o WHERE o.code = :code", CompoundType.class);
        q.setParameter("code", code);
        return q;
    }

	public static TypedQuery<CompoundType> findCompoundTypesByCodeEquals(String code, String sortFieldName, String sortOrder) {
        if (code == null || code.length() == 0) throw new IllegalArgumentException("The code argument is required");
        EntityManager em = CompoundType.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM CompoundType AS o WHERE o.code = :code");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<CompoundType> q = em.createQuery(queryBuilder.toString(), CompoundType.class);
        q.setParameter("code", code);
        return q;
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("code", "name", "displayOrder", "comment");

	public static final EntityManager entityManager() {
        EntityManager em = new CompoundType().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countCompoundTypes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM CompoundType o", Long.class).getSingleResult();
    }

	public static List<CompoundType> findAllCompoundTypes() {
        return entityManager().createQuery("SELECT o FROM CompoundType o", CompoundType.class).getResultList();
    }

	public static List<CompoundType> findAllCompoundTypes(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM CompoundType o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, CompoundType.class).getResultList();
    }

	public static CompoundType findCompoundType(Long id) {
        if (id == null) return null;
        return entityManager().find(CompoundType.class, id);
    }

	public static List<CompoundType> findCompoundTypeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM CompoundType o", CompoundType.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<CompoundType> findCompoundTypeEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM CompoundType o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, CompoundType.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            CompoundType attached = CompoundType.findCompoundType(this.id);
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
    public CompoundType merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        CompoundType merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
}
