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
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
public class SolutionUnit {

    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String code; 
    

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("name", "code");

	public static final EntityManager entityManager() {
        EntityManager em = new SolutionUnit().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countSolutionUnits() {
        return entityManager().createQuery("SELECT COUNT(o) FROM SolutionUnit o", Long.class).getSingleResult();
    }

	public static List<SolutionUnit> findAllSolutionUnits() {
        return entityManager().createQuery("SELECT o FROM SolutionUnit o", SolutionUnit.class).getResultList();
    }

	public static List<SolutionUnit> findAllSolutionUnits(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM SolutionUnit o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, SolutionUnit.class).getResultList();
    }

	public static SolutionUnit findSolutionUnit(Long id) {
        if (id == null) return null;
        return entityManager().find(SolutionUnit.class, id);
    }

	public static List<SolutionUnit> findSolutionUnitEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM SolutionUnit o", SolutionUnit.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<SolutionUnit> findSolutionUnitEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM SolutionUnit o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, SolutionUnit.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            SolutionUnit attached = SolutionUnit.findSolutionUnit(this.id);
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
    public SolutionUnit merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        SolutionUnit merged = this.entityManager.merge(this);
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

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String getName() {
        return this.name;
    }

	public void setName(String name) {
        this.name = name;
    }

	public String getCode() {
        return this.code;
    }

	public void setCode(String code) {
        this.code = code;
    }

	public static Long countFindSolutionUnitsByCodeEquals(String code) {
        if (code == null || code.length() == 0) throw new IllegalArgumentException("The code argument is required");
        EntityManager em = SolutionUnit.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM SolutionUnit AS o WHERE o.code = :code", Long.class);
        q.setParameter("code", code);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindSolutionUnitsByCodeLike(String code) {
        if (code == null || code.length() == 0) throw new IllegalArgumentException("The code argument is required");
        code = code.replace('*', '%');
        if (code.charAt(0) != '%') {
            code = "%" + code;
        }
        if (code.charAt(code.length() - 1) != '%') {
            code = code + "%";
        }
        EntityManager em = SolutionUnit.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM SolutionUnit AS o WHERE LOWER(o.code) LIKE LOWER(:code)", Long.class);
        q.setParameter("code", code);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindSolutionUnitsByNameEquals(String name) {
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = SolutionUnit.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM SolutionUnit AS o WHERE o.name = :name", Long.class);
        q.setParameter("name", name);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<SolutionUnit> findSolutionUnitsByCodeEquals(String code) {
        if (code == null || code.length() == 0) throw new IllegalArgumentException("The code argument is required");
        EntityManager em = SolutionUnit.entityManager();
        TypedQuery<SolutionUnit> q = em.createQuery("SELECT o FROM SolutionUnit AS o WHERE o.code = :code", SolutionUnit.class);
        q.setParameter("code", code);
        return q;
    }

	public static TypedQuery<SolutionUnit> findSolutionUnitsByCodeEquals(String code, String sortFieldName, String sortOrder) {
        if (code == null || code.length() == 0) throw new IllegalArgumentException("The code argument is required");
        EntityManager em = SolutionUnit.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM SolutionUnit AS o WHERE o.code = :code");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<SolutionUnit> q = em.createQuery(queryBuilder.toString(), SolutionUnit.class);
        q.setParameter("code", code);
        return q;
    }

	public static TypedQuery<SolutionUnit> findSolutionUnitsByCodeLike(String code) {
        if (code == null || code.length() == 0) throw new IllegalArgumentException("The code argument is required");
        code = code.replace('*', '%');
        if (code.charAt(0) != '%') {
            code = "%" + code;
        }
        if (code.charAt(code.length() - 1) != '%') {
            code = code + "%";
        }
        EntityManager em = SolutionUnit.entityManager();
        TypedQuery<SolutionUnit> q = em.createQuery("SELECT o FROM SolutionUnit AS o WHERE LOWER(o.code) LIKE LOWER(:code)", SolutionUnit.class);
        q.setParameter("code", code);
        return q;
    }

	public static TypedQuery<SolutionUnit> findSolutionUnitsByCodeLike(String code, String sortFieldName, String sortOrder) {
        if (code == null || code.length() == 0) throw new IllegalArgumentException("The code argument is required");
        code = code.replace('*', '%');
        if (code.charAt(0) != '%') {
            code = "%" + code;
        }
        if (code.charAt(code.length() - 1) != '%') {
            code = code + "%";
        }
        EntityManager em = SolutionUnit.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM SolutionUnit AS o WHERE LOWER(o.code) LIKE LOWER(:code)");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<SolutionUnit> q = em.createQuery(queryBuilder.toString(), SolutionUnit.class);
        q.setParameter("code", code);
        return q;
    }

	public static TypedQuery<SolutionUnit> findSolutionUnitsByNameEquals(String name) {
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = SolutionUnit.entityManager();
        TypedQuery<SolutionUnit> q = em.createQuery("SELECT o FROM SolutionUnit AS o WHERE o.name = :name", SolutionUnit.class);
        q.setParameter("name", name);
        return q;
    }

	public static TypedQuery<SolutionUnit> findSolutionUnitsByNameEquals(String name, String sortFieldName, String sortOrder) {
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = SolutionUnit.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM SolutionUnit AS o WHERE o.name = :name");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<SolutionUnit> q = em.createQuery(queryBuilder.toString(), SolutionUnit.class);
        q.setParameter("name", name);
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

	public static SolutionUnit fromJsonToSolutionUnit(String json) {
        return new JSONDeserializer<SolutionUnit>()
        .use(null, SolutionUnit.class).deserialize(json);
    }

	public static String toJsonArray(Collection<SolutionUnit> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<SolutionUnit> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<SolutionUnit> fromJsonArrayToSolutionUnits(String json) {
        return new JSONDeserializer<List<SolutionUnit>>()
        .use("values", SolutionUnit.class).deserialize(json);
    }
}
