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
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Entity
@Configurable

public class Vendor {

    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String code;

    
    public static TypedQuery<Vendor> findVendorsBySearchTerm(String searchTerm) {
        if (searchTerm == null || searchTerm.length() == 0) throw new IllegalArgumentException("The searchTerm argument is required");
        searchTerm = searchTerm.replace('*', '%');
        if (searchTerm.charAt(0) != '%') {
        	searchTerm = "%" + searchTerm;
        }
        if (searchTerm.charAt(searchTerm.length() - 1) != '%') {
        	searchTerm = searchTerm + "%";
        }
        EntityManager em = Vendor.entityManager();
        TypedQuery<Vendor> q = em.createQuery("SELECT DISTINCT o FROM Vendor AS o WHERE (LOWER(o.code) LIKE LOWER(:searchTerm) OR LOWER(o.name) LIKE LOWER(:searchTerm))", Vendor.class);
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

	public static Vendor fromJsonToVendor(String json) {
        return new JSONDeserializer<Vendor>()
        .use(null, Vendor.class).deserialize(json);
    }

	public static String toJsonArray(Collection<Vendor> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<Vendor> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<Vendor> fromJsonArrayToVendors(String json) {
        return new JSONDeserializer<List<Vendor>>()
        .use("values", Vendor.class).deserialize(json);
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

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("name", "code");

	public static final EntityManager entityManager() {
        EntityManager em = new Vendor().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countVendors() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Vendor o", Long.class).getSingleResult();
    }

	public static List<Vendor> findAllVendors() {
        return entityManager().createQuery("SELECT o FROM Vendor o", Vendor.class).getResultList();
    }

	public static List<Vendor> findAllVendors(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Vendor o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Vendor.class).getResultList();
    }

	public static Vendor findVendor(Long id) {
        if (id == null) return null;
        return entityManager().find(Vendor.class, id);
    }

	public static List<Vendor> findVendorEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Vendor o", Vendor.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<Vendor> findVendorEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Vendor o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Vendor.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Vendor attached = Vendor.findVendor(this.id);
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
    public Vendor merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Vendor merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
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

	public static Long countFindVendorsByCodeEquals(String code) {
        if (code == null || code.length() == 0) throw new IllegalArgumentException("The code argument is required");
        EntityManager em = Vendor.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Vendor AS o WHERE o.code = :code", Long.class);
        q.setParameter("code", code);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindVendorsByCodeLike(String code) {
        if (code == null || code.length() == 0) throw new IllegalArgumentException("The code argument is required");
        code = code.replace('*', '%');
        if (code.charAt(0) != '%') {
            code = "%" + code;
        }
        if (code.charAt(code.length() - 1) != '%') {
            code = code + "%";
        }
        EntityManager em = Vendor.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Vendor AS o WHERE LOWER(o.code) LIKE LOWER(:code)", Long.class);
        q.setParameter("code", code);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindVendorsByNameEquals(String name) {
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = Vendor.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Vendor AS o WHERE o.name = :name", Long.class);
        q.setParameter("name", name);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindVendorsByNameLike(String name) {
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        name = name.replace('*', '%');
        if (name.charAt(0) != '%') {
            name = "%" + name;
        }
        if (name.charAt(name.length() - 1) != '%') {
            name = name + "%";
        }
        EntityManager em = Vendor.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Vendor AS o WHERE LOWER(o.name) LIKE LOWER(:name)", Long.class);
        q.setParameter("name", name);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<Vendor> findVendorsByCodeEquals(String code) {
        if (code == null || code.length() == 0) throw new IllegalArgumentException("The code argument is required");
        EntityManager em = Vendor.entityManager();
        TypedQuery<Vendor> q = em.createQuery("SELECT o FROM Vendor AS o WHERE o.code = :code", Vendor.class);
        q.setParameter("code", code);
        return q;
    }

	public static TypedQuery<Vendor> findVendorsByCodeEquals(String code, String sortFieldName, String sortOrder) {
        if (code == null || code.length() == 0) throw new IllegalArgumentException("The code argument is required");
        EntityManager em = Vendor.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Vendor AS o WHERE o.code = :code");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Vendor> q = em.createQuery(queryBuilder.toString(), Vendor.class);
        q.setParameter("code", code);
        return q;
    }

	public static TypedQuery<Vendor> findVendorsByCodeLike(String code) {
        if (code == null || code.length() == 0) throw new IllegalArgumentException("The code argument is required");
        code = code.replace('*', '%');
        if (code.charAt(0) != '%') {
            code = "%" + code;
        }
        if (code.charAt(code.length() - 1) != '%') {
            code = code + "%";
        }
        EntityManager em = Vendor.entityManager();
        TypedQuery<Vendor> q = em.createQuery("SELECT o FROM Vendor AS o WHERE LOWER(o.code) LIKE LOWER(:code)", Vendor.class);
        q.setParameter("code", code);
        return q;
    }

	public static TypedQuery<Vendor> findVendorsByCodeLike(String code, String sortFieldName, String sortOrder) {
        if (code == null || code.length() == 0) throw new IllegalArgumentException("The code argument is required");
        code = code.replace('*', '%');
        if (code.charAt(0) != '%') {
            code = "%" + code;
        }
        if (code.charAt(code.length() - 1) != '%') {
            code = code + "%";
        }
        EntityManager em = Vendor.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Vendor AS o WHERE LOWER(o.code) LIKE LOWER(:code)");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Vendor> q = em.createQuery(queryBuilder.toString(), Vendor.class);
        q.setParameter("code", code);
        return q;
    }

	public static TypedQuery<Vendor> findVendorsByNameEquals(String name) {
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = Vendor.entityManager();
        TypedQuery<Vendor> q = em.createQuery("SELECT o FROM Vendor AS o WHERE o.name = :name", Vendor.class);
        q.setParameter("name", name);
        return q;
    }

	public static TypedQuery<Vendor> findVendorsByNameEquals(String name, String sortFieldName, String sortOrder) {
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = Vendor.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Vendor AS o WHERE o.name = :name");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Vendor> q = em.createQuery(queryBuilder.toString(), Vendor.class);
        q.setParameter("name", name);
        return q;
    }

	public static TypedQuery<Vendor> findVendorsByNameLike(String name) {
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        name = name.replace('*', '%');
        if (name.charAt(0) != '%') {
            name = "%" + name;
        }
        if (name.charAt(name.length() - 1) != '%') {
            name = name + "%";
        }
        EntityManager em = Vendor.entityManager();
        TypedQuery<Vendor> q = em.createQuery("SELECT o FROM Vendor AS o WHERE LOWER(o.name) LIKE LOWER(:name)", Vendor.class);
        q.setParameter("name", name);
        return q;
    }

	public static TypedQuery<Vendor> findVendorsByNameLike(String name, String sortFieldName, String sortOrder) {
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        name = name.replace('*', '%');
        if (name.charAt(0) != '%') {
            name = "%" + name;
        }
        if (name.charAt(name.length() - 1) != '%') {
            name = name + "%";
        }
        EntityManager em = Vendor.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Vendor AS o WHERE LOWER(o.name) LIKE LOWER(:name)");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Vendor> q = em.createQuery(queryBuilder.toString(), Vendor.class);
        q.setParameter("name", name);
        return q;
    }
}
