package com.labsynch.labseer.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
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
}
