package com.labsynch.labseer.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.Lob;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = { "findSaltsByCdId", "findSaltsByAbbrevEquals", "findSaltsByAbbrevLike", "findSaltsByAbbrevEqualsAndNameEquals", "findSaltsByNameEquals" })
public class Salt {

	@Column(columnDefinition="text")
    private String molStructure;

    @Size(max = 255)
    private String name;

    @Column(columnDefinition="text")
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
        if (abbrev == null || abbrev.length() == 0) throw new IllegalArgumentException("The abbrev argument is required");
        EntityManager em = Salt.entityManager();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Salt> criteria = criteriaBuilder.createQuery(Salt.class);
        Root<Salt> saltRoot = criteria.from(Salt.class);
        criteria.select(saltRoot);
        Predicate predicate = criteriaBuilder.equal(criteriaBuilder.upper(saltRoot.<String>get("abbrev")), abbrev.toUpperCase().trim());
        criteria.where(criteriaBuilder.and(predicate));
        TypedQuery<Salt> q = em.createQuery(criteria);
        return q;
    }

    public static TypedQuery<Salt> findSaltsByAbbrevEqualsAndNameEquals(String abbrev, String name) {
        if (abbrev == null || abbrev.length() == 0) throw new IllegalArgumentException("The abbrev argument is required");
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = Salt.entityManager();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Salt> criteria = criteriaBuilder.createQuery(Salt.class);
        Root<Salt> saltRoot = criteria.from(Salt.class);
        criteria.select(saltRoot);
        Predicate[] predicates = new Predicate[0];
        List<Predicate> predicateList = new ArrayList<Predicate>();
        Predicate predicate1 = criteriaBuilder.equal(criteriaBuilder.upper(saltRoot.<String>get("abbrev")), abbrev.toUpperCase().trim());
        Predicate predicate2 = criteriaBuilder.equal(criteriaBuilder.upper(saltRoot.<String>get("name")), name.toUpperCase().trim());
        predicateList.add(predicate1);
        predicateList.add(predicate2);
        predicates = predicateList.toArray(predicates);
        criteria.where(criteriaBuilder.and(predicates));
        TypedQuery<Salt> q = em.createQuery(criteria);
        return q;
    }

    public static TypedQuery<Salt> findSaltsByAbbrevLike(String abbrev) {
        if (abbrev == null || abbrev.length() == 0) throw new IllegalArgumentException("The abbrev argument is required");
        abbrev = abbrev.replace('*', '%');
        if (abbrev.charAt(0) != '%') {
            abbrev = "%" + abbrev;
        }
        if (abbrev.charAt(abbrev.length() - 1) != '%') {
            abbrev = abbrev + "%";
        }
        EntityManager em = Salt.entityManager();
        TypedQuery<Salt> q = em.createQuery("SELECT o FROM Salt AS o WHERE LOWER(o.abbrev) LIKE LOWER(:abbrev)", Salt.class);
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
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = Salt.entityManager();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Salt> criteria = criteriaBuilder.createQuery(Salt.class);
        Root<Salt> saltRoot = criteria.from(Salt.class);
        criteria.select(saltRoot);
        Predicate predicate = criteriaBuilder.equal(criteriaBuilder.upper(saltRoot.<String>get("name")), name.toUpperCase().trim());
        criteria.where(criteriaBuilder.and(predicate));
        TypedQuery<Salt> q = em.createQuery(criteria);
        return q;
    }
}
