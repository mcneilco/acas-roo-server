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

@Configurable
@Entity
public class StereoCategory {

    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String code;
    
    public static TypedQuery<StereoCategory> findStereoCategoriesBySearchTerm(String searchTerm) {
        if (searchTerm == null || searchTerm.length() == 0) throw new IllegalArgumentException("The searchTerm argument is required");
        searchTerm = searchTerm.replace('*', '%');
        if (searchTerm.charAt(0) != '%') {
        	searchTerm = "%" + searchTerm;
        }
        if (searchTerm.charAt(searchTerm.length() - 1) != '%') {
        	searchTerm = searchTerm + "%";
        }
        EntityManager em = StereoCategory.entityManager();
        TypedQuery<StereoCategory> q = em.createQuery("SELECT DISTINCT o FROM StereoCategory AS o WHERE (LOWER(o.code) LIKE LOWER(:searchTerm) OR LOWER(o.name) LIKE LOWER(:searchTerm))", StereoCategory.class);
        q.setParameter("searchTerm", searchTerm);
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

	public static StereoCategory fromJsonToStereoCategory(String json) {
        return new JSONDeserializer<StereoCategory>()
        .use(null, StereoCategory.class).deserialize(json);
    }

	public static String toJsonArray(Collection<StereoCategory> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<StereoCategory> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<StereoCategory> fromJsonArrayToStereoCategorys(String json) {
        return new JSONDeserializer<List<StereoCategory>>()
        .use("values", StereoCategory.class).deserialize(json);
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("name", "code");

	public static final EntityManager entityManager() {
        EntityManager em = new StereoCategory().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countStereoCategorys() {
        return entityManager().createQuery("SELECT COUNT(o) FROM StereoCategory o", Long.class).getSingleResult();
    }

	public static List<StereoCategory> findAllStereoCategorys() {
        return entityManager().createQuery("SELECT o FROM StereoCategory o", StereoCategory.class).getResultList();
    }

	public static List<StereoCategory> findAllStereoCategorys(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM StereoCategory o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, StereoCategory.class).getResultList();
    }

	public static StereoCategory findStereoCategory(Long id) {
        if (id == null) return null;
        return entityManager().find(StereoCategory.class, id);
    }

	public static List<StereoCategory> findStereoCategoryEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM StereoCategory o", StereoCategory.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<StereoCategory> findStereoCategoryEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM StereoCategory o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, StereoCategory.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            StereoCategory attached = StereoCategory.findStereoCategory(this.id);
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
    public StereoCategory merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        StereoCategory merged = this.entityManager.merge(this);
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

	public static Long countFindStereoCategorysByCodeEquals(String code) {
        if (code == null || code.length() == 0) throw new IllegalArgumentException("The code argument is required");
        EntityManager em = StereoCategory.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM StereoCategory AS o WHERE o.code = :code", Long.class);
        q.setParameter("code", code);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindStereoCategorysByCodeLike(String code) {
        if (code == null || code.length() == 0) throw new IllegalArgumentException("The code argument is required");
        code = code.replace('*', '%');
        if (code.charAt(0) != '%') {
            code = "%" + code;
        }
        if (code.charAt(code.length() - 1) != '%') {
            code = code + "%";
        }
        EntityManager em = StereoCategory.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM StereoCategory AS o WHERE LOWER(o.code) LIKE LOWER(:code)", Long.class);
        q.setParameter("code", code);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<StereoCategory> findStereoCategorysByCodeEquals(String code) {
        if (code == null || code.length() == 0) throw new IllegalArgumentException("The code argument is required");
        EntityManager em = StereoCategory.entityManager();
        TypedQuery<StereoCategory> q = em.createQuery("SELECT o FROM StereoCategory AS o WHERE o.code = :code", StereoCategory.class);
        q.setParameter("code", code);
        return q;
    }

	public static TypedQuery<StereoCategory> findStereoCategorysByCodeEquals(String code, String sortFieldName, String sortOrder) {
        if (code == null || code.length() == 0) throw new IllegalArgumentException("The code argument is required");
        EntityManager em = StereoCategory.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM StereoCategory AS o WHERE o.code = :code");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<StereoCategory> q = em.createQuery(queryBuilder.toString(), StereoCategory.class);
        q.setParameter("code", code);
        return q;
    }

	public static TypedQuery<StereoCategory> findStereoCategorysByCodeLike(String code) {
        if (code == null || code.length() == 0) throw new IllegalArgumentException("The code argument is required");
        code = code.replace('*', '%');
        if (code.charAt(0) != '%') {
            code = "%" + code;
        }
        if (code.charAt(code.length() - 1) != '%') {
            code = code + "%";
        }
        EntityManager em = StereoCategory.entityManager();
        TypedQuery<StereoCategory> q = em.createQuery("SELECT o FROM StereoCategory AS o WHERE LOWER(o.code) LIKE LOWER(:code)", StereoCategory.class);
        q.setParameter("code", code);
        return q;
    }

	public static TypedQuery<StereoCategory> findStereoCategorysByCodeLike(String code, String sortFieldName, String sortOrder) {
        if (code == null || code.length() == 0) throw new IllegalArgumentException("The code argument is required");
        code = code.replace('*', '%');
        if (code.charAt(0) != '%') {
            code = "%" + code;
        }
        if (code.charAt(code.length() - 1) != '%') {
            code = code + "%";
        }
        EntityManager em = StereoCategory.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM StereoCategory AS o WHERE LOWER(o.code) LIKE LOWER(:code)");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<StereoCategory> q = em.createQuery(queryBuilder.toString(), StereoCategory.class);
        q.setParameter("code", code);
        return q;
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
}
