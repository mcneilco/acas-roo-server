package com.labsynch.labseer.domain;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = { "findIsotopesByAbbrevEquals", "findIsotopesByAbbrevEqualsAndNameEquals", "findIsotopesByNameEquals" })
public class Isotope {

	
    @Size(max = 255)
    private String name;

    @Size(max = 100)
    private String abbrev;

    private Double massChange;

    private Boolean ignore;

    public static TypedQuery<Isotope> findIsotopesByAbbrevEquals(String abbrev) {
        if (abbrev == null || abbrev.length() == 0) throw new IllegalArgumentException("The abbrev argument is required");
        EntityManager em = Isotope.entityManager();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Isotope> criteria = criteriaBuilder.createQuery(Isotope.class);
        Root<Isotope> isotopeRoot = criteria.from(Isotope.class);
        criteria.select(isotopeRoot);
        Predicate predicate = criteriaBuilder.equal(criteriaBuilder.upper(isotopeRoot.<String>get("abbrev")), abbrev.toUpperCase().trim());
        criteria.where(criteriaBuilder.and(predicate));
        TypedQuery<Isotope> q = em.createQuery(criteria);
        return q;
    }

    public static TypedQuery<Isotope> findIsotopesByAbbrevEqualsAndNameEquals(String abbrev, String name) {
        if (abbrev == null || abbrev.length() == 0) throw new IllegalArgumentException("The abbrev argument is required");
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = Isotope.entityManager();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Isotope> criteria = criteriaBuilder.createQuery(Isotope.class);
        Root<Isotope> isotopeRoot = criteria.from(Isotope.class);
        criteria.select(isotopeRoot);
        Predicate[] predicates = new Predicate[0];
        List<Predicate> predicateList = new ArrayList<Predicate>();
        Predicate predicate1 = criteriaBuilder.equal(criteriaBuilder.upper(isotopeRoot.<String>get("abbrev")), abbrev.toUpperCase().trim());
        Predicate predicate2 = criteriaBuilder.equal(criteriaBuilder.upper(isotopeRoot.<String>get("name")), name.toUpperCase().trim());
        predicateList.add(predicate1);
        predicateList.add(predicate2);
        predicates = predicateList.toArray(predicates);
        criteria.where(criteriaBuilder.and(predicates));
        TypedQuery<Isotope> q = em.createQuery(criteria);
        return q;
    }

	public static TypedQuery<Isotope> findIsotopesByNameEquals(String name) {
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = Isotope.entityManager();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Isotope> criteria = criteriaBuilder.createQuery(Isotope.class);
        Root<Isotope> isotopeRoot = criteria.from(Isotope.class);
        criteria.select(isotopeRoot);
        Predicate predicate = criteriaBuilder.equal(criteriaBuilder.upper(isotopeRoot.<String>get("name")), name.toUpperCase().trim());
        criteria.where(criteriaBuilder.and(predicate));
        TypedQuery<Isotope> q = em.createQuery(criteria);
        return q;
    }

	public String getName() {
        return this.name;
    }

	public void setName(String name) {
        this.name = name;
    }

	public String getAbbrev() {
        return this.abbrev;
    }

	public void setAbbrev(String abbrev) {
        this.abbrev = abbrev;
    }

	public Double getMassChange() {
        return this.massChange;
    }

	public void setMassChange(Double massChange) {
        this.massChange = massChange;
    }

	public Boolean getIgnore() {
        return this.ignore;
    }

	public void setIgnore(Boolean ignore) {
        this.ignore = ignore;
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static Isotope fromJsonToIsotope(String json) {
        return new JSONDeserializer<Isotope>()
        .use(null, Isotope.class).deserialize(json);
    }

	public static String toJsonArray(Collection<Isotope> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<Isotope> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<Isotope> fromJsonArrayToIsotopes(String json) {
        return new JSONDeserializer<List<Isotope>>()
        .use("values", Isotope.class).deserialize(json);
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("name", "abbrev", "massChange", "ignore");

	public static final EntityManager entityManager() {
        EntityManager em = new Isotope().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countIsotopes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Isotope o", Long.class).getSingleResult();
    }

	public static List<Isotope> findAllIsotopes() {
        return entityManager().createQuery("SELECT o FROM Isotope o", Isotope.class).getResultList();
    }

	public static List<Isotope> findAllIsotopes(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Isotope o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Isotope.class).getResultList();
    }

	public static Isotope findIsotope(Long id) {
        if (id == null) return null;
        return entityManager().find(Isotope.class, id);
    }

	public static List<Isotope> findIsotopeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Isotope o", Isotope.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<Isotope> findIsotopeEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Isotope o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Isotope.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Isotope attached = Isotope.findIsotope(this.id);
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
    public Isotope merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Isotope merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public static Long countFindIsotopesByAbbrevEquals(String abbrev) {
        if (abbrev == null || abbrev.length() == 0) throw new IllegalArgumentException("The abbrev argument is required");
        EntityManager em = Isotope.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Isotope AS o WHERE o.abbrev = :abbrev", Long.class);
        q.setParameter("abbrev", abbrev);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindIsotopesByAbbrevEqualsAndNameEquals(String abbrev, String name) {
        if (abbrev == null || abbrev.length() == 0) throw new IllegalArgumentException("The abbrev argument is required");
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = Isotope.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Isotope AS o WHERE o.abbrev = :abbrev  AND o.name = :name", Long.class);
        q.setParameter("abbrev", abbrev);
        q.setParameter("name", name);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindIsotopesByNameEquals(String name) {
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = Isotope.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Isotope AS o WHERE o.name = :name", Long.class);
        q.setParameter("name", name);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<Isotope> findIsotopesByAbbrevEquals(String abbrev, String sortFieldName, String sortOrder) {
        if (abbrev == null || abbrev.length() == 0) throw new IllegalArgumentException("The abbrev argument is required");
        EntityManager em = Isotope.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Isotope AS o WHERE o.abbrev = :abbrev");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Isotope> q = em.createQuery(queryBuilder.toString(), Isotope.class);
        q.setParameter("abbrev", abbrev);
        return q;
    }

	public static TypedQuery<Isotope> findIsotopesByAbbrevEqualsAndNameEquals(String abbrev, String name, String sortFieldName, String sortOrder) {
        if (abbrev == null || abbrev.length() == 0) throw new IllegalArgumentException("The abbrev argument is required");
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = Isotope.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Isotope AS o WHERE o.abbrev = :abbrev  AND o.name = :name");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Isotope> q = em.createQuery(queryBuilder.toString(), Isotope.class);
        q.setParameter("abbrev", abbrev);
        q.setParameter("name", name);
        return q;
    }

	public static TypedQuery<Isotope> findIsotopesByNameEquals(String name, String sortFieldName, String sortOrder) {
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = Isotope.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Isotope AS o WHERE o.name = :name");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Isotope> q = em.createQuery(queryBuilder.toString(), Isotope.class);
        q.setParameter("name", name);
        return q;
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
