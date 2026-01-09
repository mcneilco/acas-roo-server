package com.labsynch.labseer.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.Version;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Entity
@Configurable
@Transactional

public class Salt {

    @Column(columnDefinition = "text")
    private String molStructure;

    @Size(max = 255)
    private String name;

    @Column(columnDefinition = "text")
    private String originalStructure;

    @Size(max = 100)
    private String abbrev;

    private Double molWeight;

    @Size(max = 255)
    private String formula;

    private int cdId;

    private Boolean ignore;

    private int charge;

    public static TypedQuery<Salt> findSaltsByAbbrevEquals(String abbrev) {
        if (abbrev == null || abbrev.length() == 0)
            throw new IllegalArgumentException("The abbrev argument is required");
        EntityManager em = Salt.entityManager();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Salt> criteria = criteriaBuilder.createQuery(Salt.class);
        Root<Salt> saltRoot = criteria.from(Salt.class);
        criteria.select(saltRoot);
        Predicate predicate = criteriaBuilder.equal(criteriaBuilder.upper(saltRoot.<String>get("abbrev")),
                abbrev.toUpperCase().trim());
        criteria.where(criteriaBuilder.and(predicate));
        TypedQuery<Salt> q = em.createQuery(criteria);
        return q;
    }

    public static TypedQuery<Salt> findSaltsByAbbrevEqualsAndNameEquals(String abbrev, String name) {
        if (abbrev == null || abbrev.length() == 0)
            throw new IllegalArgumentException("The abbrev argument is required");
        if (name == null || name.length() == 0)
            throw new IllegalArgumentException("The name argument is required");
        EntityManager em = Salt.entityManager();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Salt> criteria = criteriaBuilder.createQuery(Salt.class);
        Root<Salt> saltRoot = criteria.from(Salt.class);
        criteria.select(saltRoot);
        Predicate[] predicates = new Predicate[0];
        List<Predicate> predicateList = new ArrayList<Predicate>();
        Predicate predicate1 = criteriaBuilder.equal(criteriaBuilder.upper(saltRoot.<String>get("abbrev")),
                abbrev.toUpperCase().trim());
        Predicate predicate2 = criteriaBuilder.equal(criteriaBuilder.upper(saltRoot.<String>get("name")),
                name.toUpperCase().trim());
        predicateList.add(predicate1);
        predicateList.add(predicate2);
        predicates = predicateList.toArray(predicates);
        criteria.where(criteriaBuilder.and(predicates));
        TypedQuery<Salt> q = em.createQuery(criteria);
        return q;
    }

    public static TypedQuery<Salt> findSaltsByAbbrevLike(String abbrev) {
        if (abbrev == null || abbrev.length() == 0)
            throw new IllegalArgumentException("The abbrev argument is required");
        abbrev = abbrev.replace('*', '%');
        if (abbrev.charAt(0) != '%') {
            abbrev = "%" + abbrev;
        }
        if (abbrev.charAt(abbrev.length() - 1) != '%') {
            abbrev = abbrev + "%";
        }
        EntityManager em = Salt.entityManager();
        TypedQuery<Salt> q = em.createQuery("SELECT o FROM Salt AS o WHERE LOWER(o.abbrev) LIKE LOWER(:abbrev)",
                Salt.class);
        q.setParameter("abbrev", abbrev);
        return q;
    }

    public static TypedQuery<Salt> findSaltsByCdId(int cdId) {
        EntityManager em = Salt.entityManager();
        TypedQuery<Salt> q = em.createQuery("SELECT o FROM Salt AS o WHERE o.cdId = :cdId", Salt.class);
        q.setParameter("cdId", cdId);
        return q;
    }

    public static TypedQuery<Salt> findSaltsByNameEquals(String name) {
        if (name == null || name.length() == 0)
            throw new IllegalArgumentException("The name argument is required");
        EntityManager em = Salt.entityManager();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Salt> criteria = criteriaBuilder.createQuery(Salt.class);
        Root<Salt> saltRoot = criteria.from(Salt.class);
        criteria.select(saltRoot);
        Predicate predicate = criteriaBuilder.equal(criteriaBuilder.upper(saltRoot.<String>get("name")),
                name.toUpperCase().trim());
        criteria.where(criteriaBuilder.and(predicate));
        TypedQuery<Salt> q = em.createQuery(criteria);
        return q;
    }

    public static TypedQuery<Salt> findSaltsByFormulaEquals(String formula) {
        if (formula == null || formula.length() == 0)
            throw new IllegalArgumentException("The formula provided is emptpy or null");
        EntityManager em = Salt.entityManager();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Salt> criteria = criteriaBuilder.createQuery(Salt.class);
        Root<Salt> saltRoot = criteria.from(Salt.class);
        criteria.select(saltRoot);
        Predicate predicate = criteriaBuilder.equal(criteriaBuilder.upper(saltRoot.<String>get("formula")),
                formula.toUpperCase().trim());
        criteria.where(criteriaBuilder.and(predicate));
        TypedQuery<Salt> q = em.createQuery(criteria);
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

    public static TypedQuery<Salt> findSaltsBySearchTerm(String searchTerm) {
        if (searchTerm == null || searchTerm.length() == 0)
            throw new IllegalArgumentException("The searchTerm argument is required");
        searchTerm = searchTerm.replace('*', '%');
        if (searchTerm.charAt(0) != '%') {
            searchTerm = "%" + searchTerm;
        }
        if (searchTerm.charAt(searchTerm.length() - 1) != '%') {
            searchTerm = searchTerm + "%";
        }
        EntityManager em = Salt.entityManager();
        TypedQuery<Salt> q = em.createQuery(
                "SELECT DISTINCT o FROM Salt AS o WHERE (LOWER(o.abbrev) LIKE LOWER(:searchTerm) OR LOWER(o.name) LIKE LOWER(:searchTerm))",
                Salt.class);
        q.setParameter("searchTerm", searchTerm);
        return q;
    }

    public static Long countFindSaltsByAbbrevEquals(String abbrev) {
        if (abbrev == null || abbrev.length() == 0)
            throw new IllegalArgumentException("The abbrev argument is required");
        EntityManager em = Salt.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Salt AS o WHERE o.abbrev = :abbrev", Long.class);
        q.setParameter("abbrev", abbrev);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindSaltsByAbbrevEqualsAndNameEquals(String abbrev, String name) {
        if (abbrev == null || abbrev.length() == 0)
            throw new IllegalArgumentException("The abbrev argument is required");
        if (name == null || name.length() == 0)
            throw new IllegalArgumentException("The name argument is required");
        EntityManager em = Salt.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Salt AS o WHERE o.abbrev = :abbrev  AND o.name = :name",
                Long.class);
        q.setParameter("abbrev", abbrev);
        q.setParameter("name", name);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindSaltsByAbbrevLike(String abbrev) {
        if (abbrev == null || abbrev.length() == 0)
            throw new IllegalArgumentException("The abbrev argument is required");
        abbrev = abbrev.replace('*', '%');
        if (abbrev.charAt(0) != '%') {
            abbrev = "%" + abbrev;
        }
        if (abbrev.charAt(abbrev.length() - 1) != '%') {
            abbrev = abbrev + "%";
        }
        EntityManager em = Salt.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Salt AS o WHERE LOWER(o.abbrev) LIKE LOWER(:abbrev)",
                Long.class);
        q.setParameter("abbrev", abbrev);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindSaltsByCdId(int cdId) {
        EntityManager em = Salt.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Salt AS o WHERE o.cdId = :cdId", Long.class);
        q.setParameter("cdId", cdId);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindSaltsByNameEquals(String name) {
        if (name == null || name.length() == 0)
            throw new IllegalArgumentException("The name argument is required");
        EntityManager em = Salt.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Salt AS o WHERE o.name = :name", Long.class);
        q.setParameter("name", name);
        return ((Long) q.getSingleResult());
    }

    public static TypedQuery<Salt> findSaltsByAbbrevEquals(String abbrev, String sortFieldName, String sortOrder) {
        if (abbrev == null || abbrev.length() == 0)
            throw new IllegalArgumentException("The abbrev argument is required");
        EntityManager em = Salt.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Salt AS o WHERE o.abbrev = :abbrev");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Salt> q = em.createQuery(queryBuilder.toString(), Salt.class);
        q.setParameter("abbrev", abbrev);
        return q;
    }

    public static TypedQuery<Salt> findSaltsByAbbrevEqualsAndNameEquals(String abbrev, String name,
            String sortFieldName, String sortOrder) {
        if (abbrev == null || abbrev.length() == 0)
            throw new IllegalArgumentException("The abbrev argument is required");
        if (name == null || name.length() == 0)
            throw new IllegalArgumentException("The name argument is required");
        EntityManager em = Salt.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM Salt AS o WHERE o.abbrev = :abbrev  AND o.name = :name");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Salt> q = em.createQuery(queryBuilder.toString(), Salt.class);
        q.setParameter("abbrev", abbrev);
        q.setParameter("name", name);
        return q;
    }

    public static TypedQuery<Salt> findSaltsByAbbrevLike(String abbrev, String sortFieldName, String sortOrder) {
        if (abbrev == null || abbrev.length() == 0)
            throw new IllegalArgumentException("The abbrev argument is required");
        abbrev = abbrev.replace('*', '%');
        if (abbrev.charAt(0) != '%') {
            abbrev = "%" + abbrev;
        }
        if (abbrev.charAt(abbrev.length() - 1) != '%') {
            abbrev = abbrev + "%";
        }
        EntityManager em = Salt.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM Salt AS o WHERE LOWER(o.abbrev) LIKE LOWER(:abbrev)");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Salt> q = em.createQuery(queryBuilder.toString(), Salt.class);
        q.setParameter("abbrev", abbrev);
        return q;
    }

    public static TypedQuery<Salt> findSaltsByCdId(int cdId, String sortFieldName, String sortOrder) {
        EntityManager em = Salt.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Salt AS o WHERE o.cdId = :cdId");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Salt> q = em.createQuery(queryBuilder.toString(), Salt.class);
        q.setParameter("cdId", cdId);
        return q;
    }

    public static TypedQuery<Salt> findSaltsByNameEquals(String name, String sortFieldName, String sortOrder) {
        if (name == null || name.length() == 0)
            throw new IllegalArgumentException("The name argument is required");
        EntityManager em = Salt.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Salt AS o WHERE o.name = :name");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Salt> q = em.createQuery(queryBuilder.toString(), Salt.class);
        q.setParameter("name", name);
        return q;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @PersistenceContext
    transient EntityManager entityManager;

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("molStructure", "name",
            "originalStructure", "abbrev", "molWeight", "formula", "cdId", "ignore", "charge");

    public static final EntityManager entityManager() {
        EntityManager em = new Salt().entityManager;
        if (em == null)
            throw new IllegalStateException(
                    "Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countSalts() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Salt o", Long.class).getSingleResult();
    }

    public static List<Salt> findAllSalts() {
        return entityManager().createQuery("SELECT o FROM Salt o", Salt.class).getResultList();
    }

    public static List<Salt> findAllSalts(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Salt o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Salt.class).getResultList();
    }

    public static Salt findSalt(Long id) {
        if (id == null)
            return null;
        return entityManager().find(Salt.class, id);
    }

    public static List<Salt> findSaltEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Salt o", Salt.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }

    public static List<Salt> findSaltEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Salt o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Salt.class).setFirstResult(firstResult).setMaxResults(maxResults)
                .getResultList();
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
            Salt attached = Salt.findSalt(this.id);
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
    public Salt merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        Salt merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    public String getMolStructure() {
        return this.molStructure;
    }

    public void setMolStructure(String molStructure) {
        this.molStructure = molStructure;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginalStructure() {
        return this.originalStructure;
    }

    public void setOriginalStructure(String originalStructure) {
        this.originalStructure = originalStructure;
    }

    public String getAbbrev() {
        return this.abbrev;
    }

    public void setAbbrev(String abbrev) {
        this.abbrev = abbrev;
    }

    public Double getMolWeight() {
        return this.molWeight;
    }

    public void setMolWeight(Double molWeight) {
        this.molWeight = molWeight;
    }

    public String getFormula() {
        return this.formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public int getCdId() {
        return this.cdId;
    }

    public void setCdId(int cdId) {
        this.cdId = cdId;
    }

    public Boolean getIgnore() {
        return this.ignore;
    }

    public void setIgnore(Boolean ignore) {
        this.ignore = ignore;
    }

    public int getCharge() {
        return this.charge;
    }

    public void setCharge(int charge) {
        this.charge = charge;
    }

    public String toJson() {
        return new JSONSerializer()
                .exclude("*.class").serialize(this);
    }

    public String toJson(String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(this);
    }

    public static Salt fromJsonToSalt(String json) {
        return new JSONDeserializer<Salt>()
                .use(null, Salt.class).deserialize(json);
    }

    public static String toJsonArray(Collection<Salt> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<Salt> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<Salt> fromJsonArrayToSalts(String json) {
        return new JSONDeserializer<List<Salt>>()
                .use("values", Salt.class).deserialize(json);
    }
}
