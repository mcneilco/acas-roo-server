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

@Configurable
@Entity

public class DDictType {

    private static final Logger logger = LoggerFactory.getLogger(DDictType.class);

    @NotNull
    @Column(unique = true)
    @Size(max = 255)
    private String name;

    @Size(max = 512)
    private String description;

    @Size(max = 512)
    private String comments;
    
	@NotNull
	private boolean ignored;

	private Integer displayOrder;

	


	public static Long countFindDDictTypesByNameEquals(String name) {
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = DDictType.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM DDictType AS o WHERE o.name = :name", Long.class);
        q.setParameter("name", name);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<DDictType> findDDictTypesByNameEquals(String name) {
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = DDictType.entityManager();
        TypedQuery<DDictType> q = em.createQuery("SELECT o FROM DDictType AS o WHERE o.name = :name", DDictType.class);
        q.setParameter("name", name);
        return q;
    }

	public static TypedQuery<DDictType> findDDictTypesByNameEquals(String name, String sortFieldName, String sortOrder) {
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = DDictType.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM DDictType AS o WHERE o.name = :name");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<DDictType> q = em.createQuery(queryBuilder.toString(), DDictType.class);
        q.setParameter("name", name);
        return q;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "name", "description", "comments", "ignored", "displayOrder");

	public static final EntityManager entityManager() {
        EntityManager em = new DDictType().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countDDictTypes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM DDictType o", Long.class).getSingleResult();
    }

	public static List<DDictType> findAllDDictTypes() {
        return entityManager().createQuery("SELECT o FROM DDictType o", DDictType.class).getResultList();
    }

	public static List<DDictType> findAllDDictTypes(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM DDictType o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, DDictType.class).getResultList();
    }

	public static DDictType findDDictType(Long id) {
        if (id == null) return null;
        return entityManager().find(DDictType.class, id);
    }

	public static List<DDictType> findDDictTypeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM DDictType o", DDictType.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<DDictType> findDDictTypeEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM DDictType o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, DDictType.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            DDictType attached = DDictType.findDDictType(this.id);
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
    public DDictType merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        DDictType merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String getName() {
        return this.name;
    }

	public void setName(String name) {
        this.name = name;
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

	@Id
    @SequenceGenerator(name = "dDictTypeGen", sequenceName = "DDICT_TYPE_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "dDictTypeGen")
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

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static DDictType fromJsonToDDictType(String json) {
        return new JSONDeserializer<DDictType>()
        .use(null, DDictType.class).deserialize(json);
    }

	public static String toJsonArray(Collection<DDictType> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<DDictType> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<DDictType> fromJsonArrayToDDictTypes(String json) {
        return new JSONDeserializer<List<DDictType>>()
        .use("values", DDictType.class).deserialize(json);
    }
}
