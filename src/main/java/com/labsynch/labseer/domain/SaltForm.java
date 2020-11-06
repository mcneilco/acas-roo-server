package com.labsynch.labseer.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.dto.SearchFormDTO;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Entity
@Configurable
@Transactional
@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = { "findSaltFormsByCorpNameEquals", "findSaltFormsByCorpNameLike", "findSaltFormsByParent", "findSaltFormsByCdId", "findSaltFormsByBulkLoadFileEquals" })
public class SaltForm implements Comparable {

	private static final Logger logger = LoggerFactory.getLogger(SaltForm.class);

	@Column(columnDefinition="text")
	private String molStructure;

	@Size(max = 255)
    @org.hibernate.annotations.Index(name="SaltForm_CorpName_IDX")
	private String corpName;

	@Size(max = 255)    
	@org.hibernate.annotations.Index(name="SaltForm_casNumber_IDX")
	private String casNumber;

	private String chemist;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "S-")
    @org.hibernate.annotations.Index(name="SaltForm_RegDate_IDX")
	private Date registrationDate;

//	@NotNull
    @org.hibernate.annotations.Index(name="SaltForm_CdId_IDX")
	private int CdId;

	private Boolean ignore;
	
	private Double saltWeight;

	@ManyToOne
    @org.hibernate.annotations.Index(name="SaltForm_Parent_IDX")
	private Parent parent;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "saltForm", fetch=FetchType.LAZY)
	private Set<Lot> lots = new HashSet<Lot>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "saltForm", fetch=FetchType.LAZY)
	private Set<IsoSalt> isoSalts = new HashSet<IsoSalt>();
	
    @ManyToOne
    private BulkLoadFile bulkLoadFile;
	
    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("corpName", "CdId", "parent", "bulkLoadFile");

    
    public static long countSaltFormsByCorpNameEquals(String corpName) {
        if (corpName == null || corpName.length() == 0) throw new IllegalArgumentException("The corpName argument is required");
        EntityManager em = SaltForm.entityManager();
        TypedQuery<Long> q = em.createQuery("SELECT count(o) FROM SaltForm AS o WHERE o.corpName = :corpName", Long.class);
        q.setParameter("corpName", corpName);
        return q.getSingleResult();
    }

    public static List<Integer> findSaltFormCdIdsByMeta(SearchFormDTO searchParams) {

		EntityManager em = SaltForm.entityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Integer> criteria = criteriaBuilder.createQuery(Integer.class);
		Root<SaltForm> saltFormRoot = criteria.from(SaltForm.class);
		Join<SaltForm, Lot> saltFormLot = saltFormRoot.join("lots");
		Join<SaltForm, Parent> saltFormParent = saltFormRoot.join("parent");
		Join<Parent, ParentAlias> parentAliases = saltFormParent.join("parentAliases", JoinType.LEFT);
		criteria.select(saltFormRoot.<Integer>get("CdId"));
		criteria.distinct(true);

		ParameterExpression<Date> firstDate = criteriaBuilder.parameter(Date.class);
		ParameterExpression<Date> lastDate = criteriaBuilder.parameter(Date.class);
		Predicate[] predicates = new Predicate[0];
		List<Predicate> predicateList = new ArrayList<Predicate>();

		Predicate predicateCdId = criteriaBuilder.notEqual(saltFormRoot.get("CdId"), 0);
		predicateList.add(predicateCdId);

		if (searchParams.getChemist() != null && !searchParams.getChemist().equals("anyone")) {
			logger.debug("incoming chemist :" + searchParams.getChemist().toString());
			Predicate predicate = criteriaBuilder.equal(saltFormLot.<Author>get("chemist"), searchParams.getChemist());
			predicateList.add(predicate);
		}
		if (searchParams.getBuidNumber() != null) {
			logger.debug("incoming buid number :" + searchParams.getBuidNumber());
			Predicate predicate = criteriaBuilder.equal(saltFormLot.get("buid"), searchParams.getBuidNumber());
			predicateList.add(predicate);
		}
		if (searchParams.getMinSynthDate() != null) {
			logger.debug("incoming minSynthesisDate :" + searchParams.getDateFrom());
			Predicate predicate = criteriaBuilder.greaterThanOrEqualTo(saltFormLot.<Date>get("synthesisDate"), firstDate);
			predicateList.add(predicate);
		}
		if (searchParams.getMaxSynthDate() != null) {
			logger.debug("incoming maxSynthesisDate :" + searchParams.getDateTo());
			Predicate predicate = criteriaBuilder.lessThanOrEqualTo(saltFormLot.<Date>get("synthesisDate"), lastDate);
			predicateList.add(predicate);
		}
		if (searchParams.getLotCorpName() != null) {
			logger.debug("incoming lotCorpName :" + searchParams.getLotCorpName());
			Predicate predicate = criteriaBuilder.equal(saltFormLot.get("corpName"), searchParams.getLotCorpName());
			predicateList.add(predicate);
		}
		if (searchParams.getSaltFormCorpName() != null) {
			logger.debug("incoming saltFormCorpName :" + searchParams.getSaltFormCorpName());
			Predicate predicate = criteriaBuilder.equal(saltFormRoot.get("corpName"), searchParams.getSaltFormCorpName());
			predicateList.add(predicate);
		}
		if (searchParams.getParentCorpName() != null) {
			logger.debug("incoming parentCorpName :" + searchParams.getParentCorpName());
			Predicate predicate = criteriaBuilder.equal(saltFormParent.get("corpName"), searchParams.getParentCorpName());
			predicateList.add(predicate);
		}
		if (searchParams.getAlias() != null && !searchParams.getAlias().equals("")) {
			Predicate predicate = null;
			String aliasName = searchParams.getAlias().trim().toUpperCase();
			logger.debug("incoming alias name : " + searchParams.getAlias());

			if (searchParams.getAliasContSelect().equals("exact")){
				predicate = criteriaBuilder.equal( criteriaBuilder.upper(parentAliases.<String>get("aliasName")), aliasName.toUpperCase());
			} else {
				aliasName = "%" + aliasName + "%";
				predicate = criteriaBuilder.like(criteriaBuilder.upper(parentAliases.<String>get("aliasName")), aliasName.toUpperCase());				
			}
			logger.debug("aliasName: " + aliasName);
			predicateList.add(predicate);
		}

		if (searchParams.getMinParentNumber() != null && searchParams.getMaxParentNumber() != null) {
			Predicate predicate1 = criteriaBuilder.greaterThanOrEqualTo(saltFormParent.<Long>get("parentNumber"), searchParams.getMinParentNumber());
			Predicate predicate2 = criteriaBuilder.lessThanOrEqualTo(saltFormParent.<Long>get("parentNumber"), searchParams.getMaxParentNumber());
			predicateList.add(predicate1);
			predicateList.add(predicate2);
		}

		predicates = predicateList.toArray(predicates);
		criteria.where(criteriaBuilder.and(predicates));

		TypedQuery<Integer> q = em.createQuery(criteria);
		if (searchParams.getMaxSynthDate() != null) {
			q.setParameter(lastDate, searchParams.getMaxSynthDate(), TemporalType.DATE);
		}
		if (searchParams.getMinSynthDate() != null) {
			q.setParameter(firstDate, searchParams.getMinSynthDate(), TemporalType.DATE);
		}

		logger.debug("SaltForm search: minSynthDate = " + searchParams.getMinSynthDate());
		List<Integer> saltFormCdIds = null;
		if (predicates.length == 0 && searchParams.getMinSynthDate() == null && searchParams.getMaxSynthDate() == null) {
			logger.debug("No search criteria. Returning null.");
		} else {
			saltFormCdIds = q.getResultList();
		}
		
		if (searchParams.getMaxResults() != null) q.setMaxResults(searchParams.getMaxResults());

		return saltFormCdIds;
	}


	public static TypedQuery<SaltForm> findSaltFormsByMeta(SearchFormDTO searchParams) {

		EntityManager em = SaltForm.entityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<SaltForm> criteria = criteriaBuilder.createQuery(SaltForm.class);
		Root<SaltForm> saltFormRoot = criteria.from(SaltForm.class);
		Join<SaltForm, Lot> saltFormLot = saltFormRoot.join("lots");
		Join<SaltForm, Parent> saltFormParent = saltFormRoot.join("parent");
		Join<Parent, ParentAlias> parentAliases = saltFormParent.join("parentAliases", JoinType.LEFT);
		Join<Lot, LotAlias> lotAliases = saltFormLot.join("lotAliases", JoinType.LEFT);

		criteria.select(saltFormRoot);
//		criteria.distinct(true);

		ParameterExpression<Date> firstDate = criteriaBuilder.parameter(Date.class);
		ParameterExpression<Date> lastDate = criteriaBuilder.parameter(Date.class);
		
		ParameterExpression<Long> minParent = criteriaBuilder.parameter(Long.class);
		ParameterExpression<Long> maxParent = criteriaBuilder.parameter(Long.class);		
		
		Predicate[] predicates = new Predicate[0];
		List<Predicate> predicateList = new ArrayList<Predicate>();
		
		if (searchParams.getChemist() != null && !searchParams.getChemist().equals("anyone")) {
			logger.debug("incoming chemist :" + searchParams.getChemist().toString());
			Predicate predicate = criteriaBuilder.equal(saltFormLot.<Author>get("chemist"), searchParams.getChemist());
			predicateList.add(predicate);
		}
		if (searchParams.getBuidNumber() != null) {
			logger.debug("incoming buid number :" + searchParams.getBuidNumber());
			Predicate predicate = criteriaBuilder.equal(saltFormLot.get("buid"), searchParams.getBuidNumber());
			predicateList.add(predicate);
		}
		if (searchParams.getMinSynthDate() != null) {
			logger.debug("incoming minSynthesisDate :" + searchParams.getDateFrom());
			Predicate predicate = criteriaBuilder.greaterThanOrEqualTo(saltFormLot.<Date>get("synthesisDate"), firstDate);
			predicateList.add(predicate);
		}
		if (searchParams.getMaxSynthDate() != null) {
			logger.debug("incoming maxSynthesisDate :" + searchParams.getDateTo());
			Predicate predicate = criteriaBuilder.lessThanOrEqualTo(saltFormLot.<Date>get("synthesisDate"), lastDate);
			predicateList.add(predicate);
		}
		if (searchParams.getLotCorpName() != null) {
			logger.debug("incoming lotCorpName :" + searchParams.getLotCorpName());
			Predicate predicate = criteriaBuilder.equal(saltFormLot.get("corpName"), searchParams.getLotCorpName());
			predicateList.add(predicate);
		}
		if (searchParams.getSaltFormCorpName() != null) {
			logger.debug("incoming saltFormCorpName :" + searchParams.getSaltFormCorpName());
			Predicate predicate = criteriaBuilder.equal(saltFormRoot.get("corpName"), searchParams.getSaltFormCorpName());
			predicateList.add(predicate);
		}
		if (searchParams.getParentCorpName() != null) {
			logger.debug("incoming parentCorpName :" + searchParams.getParentCorpName());
			Predicate predicate = criteriaBuilder.equal(saltFormParent.get("corpName"), searchParams.getParentCorpName());
			predicateList.add(predicate);
		}
		if (searchParams.getFormattedCorpNameList() != null) {
			logger.debug("incoming corpNameList :" + searchParams.getFormattedCorpNameList().toString());
			Predicate predicate = saltFormParent.get("corpName").in(searchParams.getFormattedCorpNameList());
			predicateList.add(predicate);
		}
		if (searchParams.getAlias() != null && !searchParams.getAlias().equals("")) {
			Predicate predicate = null;
			String aliasName = searchParams.getAlias().trim().toUpperCase();
			logger.debug("incoming alias name : " + searchParams.getAlias());

			if (searchParams.getAliasContSelect().equalsIgnoreCase("exact")) {
            	Predicate parentAliasNotIgnored = criteriaBuilder.not(parentAliases.<Boolean>get("ignored"));
                Predicate parentAliasNameEquals = criteriaBuilder.equal(criteriaBuilder.upper(parentAliases.<String>get("aliasName")), aliasName);
                Predicate parentAliasPredicate = criteriaBuilder.and(parentAliasNotIgnored, parentAliasNameEquals);
                Predicate lotAliasNotIgnored = criteriaBuilder.not(lotAliases.<Boolean>get("ignored"));
                Predicate lotAliasNameEquals = criteriaBuilder.equal(criteriaBuilder.upper(lotAliases.<String>get("aliasName")), aliasName);
                Predicate lotAliasPredicate = criteriaBuilder.and(lotAliasNotIgnored, lotAliasNameEquals);
                predicate = criteriaBuilder.or(parentAliasPredicate, lotAliasPredicate);
            } else {
                aliasName = "%" + aliasName + "%";
                logger.debug("looking for a like match on the alias   " + aliasName);
                Predicate parentAliasNotIgnored = criteriaBuilder.not(parentAliases.<Boolean>get("ignored"));
                Predicate parentAliasNameLike = criteriaBuilder.like(criteriaBuilder.upper(parentAliases.<String>get("aliasName")), aliasName);
                Predicate parentAliasPredicate = criteriaBuilder.and(parentAliasNotIgnored, parentAliasNameLike);
                Predicate lotAliasNotIgnored = criteriaBuilder.not(lotAliases.<Boolean>get("ignored"));
                Predicate lotAliasNameLike = criteriaBuilder.like(criteriaBuilder.upper(lotAliases.<String>get("aliasName")), aliasName);
                Predicate lotAliasPredicate = criteriaBuilder.and(lotAliasNotIgnored, lotAliasNameLike);
                predicate = criteriaBuilder.or(parentAliasPredicate, lotAliasPredicate);
            }
			logger.debug("aliasName: " + aliasName);
			predicateList.add(predicate);
		}

		if (searchParams.getMinParentNumber() != null && searchParams.getMaxParentNumber() != null) {

			Predicate predicate1 = criteriaBuilder.greaterThanOrEqualTo(saltFormParent.<Long>get("parentNumber"), minParent);
			Predicate predicate2 = criteriaBuilder.lessThanOrEqualTo(saltFormParent.<Long>get("parentNumber"), maxParent);

			
			predicateList.add(predicate1);
			predicateList.add(predicate2);
			logger.debug("searching with parent min and max numbers " + searchParams.getMaxParentNumber());
		}


		predicates = predicateList.toArray(predicates);
		criteria.where(criteriaBuilder.and(predicates));
		TypedQuery<SaltForm> q = em.createQuery(criteria);
		
		if (searchParams.getMaxSynthDate() != null) {
			q.setParameter(lastDate, searchParams.getMaxSynthDate(), TemporalType.DATE);
		}
		
		if (searchParams.getMinSynthDate() != null) {
			q.setParameter(firstDate, searchParams.getMinSynthDate(), TemporalType.DATE);
		}


		if (searchParams.getMinParentNumber() != null && searchParams.getMaxParentNumber() != null) {
			q.setParameter(minParent, searchParams.getMinParentNumber());
			q.setParameter(maxParent, searchParams.getMaxParentNumber());
			logger.debug("searching with parent min and max numbers " + searchParams.getMinParentNumber() + " " +searchParams.getMaxParentNumber());
		}
		
//		int maxResult = 20;
		if (searchParams.getMaxResults() != null) q.setMaxResults(searchParams.getMaxResults());
		
		return q;
	}

	public static TypedQuery<SaltForm> findSaltFormsByParentAndMeta(Parent parent, SearchFormDTO searchParams) {

		EntityManager em = SaltForm.entityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<SaltForm> criteria = criteriaBuilder.createQuery(SaltForm.class);
		Root<SaltForm> saltFormRoot = criteria.from(SaltForm.class);
		Join<SaltForm, Lot> saltFormLot = saltFormRoot.join("lots");
		Join<SaltForm, Parent> saltFormParent = saltFormRoot.join("parent");
		Join<Parent, ParentAlias> parentAliases = saltFormParent.join("parentAliases", JoinType.LEFT);
		criteria.select(saltFormRoot);
//		criteria.distinct(true);

		ParameterExpression<Date> firstDate = criteriaBuilder.parameter(Date.class);
		ParameterExpression<Date> lastDate = criteriaBuilder.parameter(Date.class);
		Predicate[] predicates = new Predicate[0];
		List<Predicate> predicateList = new ArrayList<Predicate>();

		Predicate parentPredicate = criteriaBuilder.equal(saltFormRoot.get("parent"), parent);
		predicateList.add(parentPredicate);

		if (searchParams.getChemist() != null && !searchParams.getChemist().equals("anyone")) {
			logger.debug("incoming chemist :" + searchParams.getChemist().toString());
			Predicate predicate = criteriaBuilder.equal(saltFormLot.<Author>get("chemist"), searchParams.getChemist());
			predicateList.add(predicate);
		}
		if (searchParams.getBuidNumber() != null) {
			logger.debug("incoming buid number :" + searchParams.getBuidNumber());
			Predicate predicate = criteriaBuilder.equal(saltFormLot.get("buid"), searchParams.getBuidNumber());
			predicateList.add(predicate);
		}
		if (searchParams.getMinSynthDate() != null) {
			logger.debug("incoming minSynthesisDate :" + searchParams.getDateFrom());
			Predicate predicate = criteriaBuilder.greaterThanOrEqualTo(saltFormLot.<Date>get("synthesisDate"), firstDate);
			predicateList.add(predicate);
		}
		if (searchParams.getMaxSynthDate() != null) {
			logger.debug("incoming maxSynthesisDate :" + searchParams.getDateTo());
			Predicate predicate = criteriaBuilder.lessThanOrEqualTo(saltFormLot.<Date>get("synthesisDate"), lastDate);
			predicateList.add(predicate);
		}
		if (searchParams.getLotCorpName() != null) {
			logger.debug("incoming lotCorpName :" + searchParams.getLotCorpName());
			Predicate predicate = criteriaBuilder.equal(saltFormLot.get("corpName"), searchParams.getLotCorpName());
			predicateList.add(predicate);
		}
		if (searchParams.getSaltFormCorpName() != null) {
			logger.debug("incoming saltFormCorpName :" + searchParams.getSaltFormCorpName());
			Predicate predicate = criteriaBuilder.equal(saltFormRoot.get("corpName"), searchParams.getSaltFormCorpName());
			predicateList.add(predicate);
		}
		if (searchParams.getParentCorpName() != null) {
			logger.debug("incoming parentCorpName :" + searchParams.getParentCorpName());
			Predicate predicate = criteriaBuilder.equal(saltFormParent.get("corpName"), searchParams.getParentCorpName());
			predicateList.add(predicate);
		}
		if (searchParams.getAlias() != null && !searchParams.getAlias().equals("")) {
			logger.debug("incoming alias name : " + searchParams.getAlias());
			Predicate predicate = null;
			
			String aliasName = searchParams.getAlias().trim().toUpperCase();
			logger.debug("aliasName: " + aliasName);

			if (searchParams.getAliasContSelect().equals("exact")){
				predicate = criteriaBuilder.equal(criteriaBuilder.upper(parentAliases.<String>get("aliasName")), aliasName.toUpperCase());
			} else {
				aliasName = "%" + aliasName + "%";
				predicate = criteriaBuilder.like(criteriaBuilder.upper(parentAliases.<String>get("aliasName")), aliasName);				
			}
			predicateList.add(predicate);
		}
		if (searchParams.getMinParentNumber() != null && searchParams.getMaxParentNumber() != null) {
			Predicate predicate1 = criteriaBuilder.greaterThanOrEqualTo(saltFormParent.<Long>get("parentNumber"), searchParams.getMinParentNumber());
			Predicate predicate2 = criteriaBuilder.lessThanOrEqualTo(saltFormParent.<Long>get("parentNumber"), searchParams.getMaxParentNumber());
			predicateList.add(predicate1);
			predicateList.add(predicate2);
		}

		predicates = predicateList.toArray(predicates);
		criteria.where(criteriaBuilder.and(predicates));
		TypedQuery<SaltForm> q = em.createQuery(criteria);
		if (searchParams.getMaxSynthDate() != null) {
			q.setParameter(lastDate, searchParams.getMaxSynthDate(), TemporalType.DATE);
		}
		if (searchParams.getMinSynthDate() != null) {
			q.setParameter(firstDate, searchParams.getMinSynthDate(), TemporalType.DATE);
		}
		
		if (searchParams.getMaxResults() != null) q.setMaxResults(searchParams.getMaxResults());

		return q;
	}

	public static List<Long> findSaltFormIDsByParentAndMeta(Parent parent, SearchFormDTO searchParams) {

		EntityManager em = SaltForm.entityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = criteriaBuilder.createQuery(Long.class);
		Root<SaltForm> saltFormRoot = criteria.from(SaltForm.class);
		Join<SaltForm, Lot> saltFormLot = saltFormRoot.join("lots");
		Join<SaltForm, Parent> saltFormParent = saltFormRoot.join("parent");
		Join<Parent, ParentAlias> parentAliases = saltFormParent.join("parentAliases", JoinType.LEFT);
		criteria.select(saltFormRoot.<Long>get("id"));
		criteria.distinct(true);
		
		ParameterExpression<Date> firstDate = criteriaBuilder.parameter(Date.class);
		ParameterExpression<Date> lastDate = criteriaBuilder.parameter(Date.class);
		Predicate[] predicates = new Predicate[0];
		List<Predicate> predicateList = new ArrayList<Predicate>();

		Predicate parentPredicate = criteriaBuilder.equal(saltFormRoot.get("parent"), parent);
		predicateList.add(parentPredicate);

		if (searchParams.getChemist() != null && !searchParams.getChemist().equals("anyone")) {
			logger.debug("incoming chemist :" + searchParams.getChemist().toString());
			Predicate predicate = criteriaBuilder.equal(saltFormLot.<Author>get("chemist"), searchParams.getChemist());
			predicateList.add(predicate);
		}
		if (searchParams.getBuidNumber() != null) {
			logger.debug("incoming buid number :" + searchParams.getBuidNumber());
			Predicate predicate = criteriaBuilder.equal(saltFormLot.get("buid"), searchParams.getBuidNumber());
			predicateList.add(predicate);
		}
		if (searchParams.getMinSynthDate() != null) {
			logger.debug("incoming minSynthesisDate :" + searchParams.getDateFrom());
			Predicate predicate = criteriaBuilder.greaterThanOrEqualTo(saltFormLot.<Date>get("synthesisDate"), firstDate);
			predicateList.add(predicate);
		}
		if (searchParams.getMaxSynthDate() != null) {
			logger.debug("incoming maxSynthesisDate :" + searchParams.getDateTo());
			Predicate predicate = criteriaBuilder.lessThanOrEqualTo(saltFormLot.<Date>get("synthesisDate"), lastDate);
			predicateList.add(predicate);
		}
		if (searchParams.getLotCorpName() != null) {
			logger.debug("incoming lotCorpName :" + searchParams.getLotCorpName());
			Predicate predicate = criteriaBuilder.equal(saltFormLot.get("corpName"), searchParams.getLotCorpName());
			predicateList.add(predicate);
		}
		if (searchParams.getSaltFormCorpName() != null) {
			logger.debug("incoming saltFormCorpName :" + searchParams.getSaltFormCorpName());
			Predicate predicate = criteriaBuilder.equal(saltFormRoot.get("corpName"), searchParams.getSaltFormCorpName());
			predicateList.add(predicate);
		}
		if (searchParams.getParentCorpName() != null) {
			logger.debug("incoming parentCorpName :" + searchParams.getParentCorpName());
			Predicate predicate = criteriaBuilder.equal(saltFormParent.get("corpName"), searchParams.getParentCorpName());
			predicateList.add(predicate);
		}
		if (searchParams.getAlias() != null && !searchParams.getAlias().equals("")) {
			Predicate predicate = null;
			String aliasName = searchParams.getAlias().trim().toUpperCase();
			logger.debug("incoming alias name :" + searchParams.getAlias());

			if (searchParams.getAliasContSelect().equals("exact")){
				predicate = criteriaBuilder.equal(parentAliases.<String>get("aliasName"), aliasName);				
			} else {
				aliasName = "%" + aliasName + "%";
				predicate = criteriaBuilder.like(criteriaBuilder.upper(parentAliases.<String>get("aliasName")), aliasName);				
			}
			predicateList.add(predicate);
		}

		if (searchParams.getMinParentNumber() != null && searchParams.getMaxParentNumber() != null) {
			Predicate predicate1 = criteriaBuilder.greaterThanOrEqualTo(saltFormParent.<Long>get("parentNumber"), searchParams.getMinParentNumber());
			Predicate predicate2 = criteriaBuilder.lessThanOrEqualTo(saltFormParent.<Long>get("parentNumber"), searchParams.getMaxParentNumber());
			predicateList.add(predicate1);
			predicateList.add(predicate2);
		}

		predicates = predicateList.toArray(predicates);
		criteria.where(criteriaBuilder.and(predicates));
		TypedQuery<Long> q = em.createQuery(criteria);
		if (searchParams.getMaxSynthDate() != null) {
			q.setParameter(lastDate, searchParams.getMaxSynthDate(), TemporalType.DATE);
		}
		if (searchParams.getMinSynthDate() != null) {
			q.setParameter(firstDate, searchParams.getMinSynthDate(), TemporalType.DATE);
		}
		
		if (searchParams.getMaxResults() != null) q.setMaxResults(searchParams.getMaxResults());

		return q.getResultList();
	}

	@Transactional
    public static long countSaltForms() {
        return SaltForm.entityManager().createQuery("SELECT COUNT(o) FROM SaltForm o", Long.class).getSingleResult();
    }
    
	@Transactional
    public static List<SaltForm> findAllSaltForms() {
        return SaltForm.entityManager().createQuery("SELECT o FROM SaltForm o", SaltForm.class).getResultList();
    }
    
	@Transactional
    public static SaltForm findSaltForm(Long id) {
        if (id == null) return null;
        return SaltForm.entityManager().find(SaltForm.class, id);
    }
    
	@Transactional
    public static List<SaltForm> findSaltFormEntries(int firstResult, int maxResults) {
        return SaltForm.entityManager().createQuery("SELECT o FROM SaltForm o", SaltForm.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
	@Override
	public int compareTo(Object otherSalt) {

        if(!(otherSalt instanceof SaltForm)){
            throw new ClassCastException("Invalid object");
        }
        
        long salt2 = ((SaltForm) otherSalt).getId();
       
        if(this.getId() > salt2)    
            return 1;
        else if ( this.getId() < salt2 )
            return -1;
        else
            return 0;
	}
	
	public static double calculateSaltWeight(SaltForm saltForm){
		double totalSaltWeight = 0;
		for (IsoSalt isoSalt : saltForm.getIsoSalts() ){
			double saltWeight = IsoSalt.calculateSaltWeight(isoSalt);
			totalSaltWeight = totalSaltWeight + saltWeight;
			logger.debug("current totalSaltWeigth: " + totalSaltWeight);	
		}
		return totalSaltWeight;
	}


	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final EntityManager entityManager() {
        EntityManager em = new SaltForm().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static List<SaltForm> findAllSaltForms(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM SaltForm o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, SaltForm.class).getResultList();
    }

	public static List<SaltForm> findSaltFormEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM SaltForm o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, SaltForm.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            SaltForm attached = SaltForm.findSaltForm(this.id);
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
    public SaltForm merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        SaltForm merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static SaltForm fromJsonToSaltForm(String json) {
        return new JSONDeserializer<SaltForm>()
        .use(null, SaltForm.class).deserialize(json);
    }

	public static String toJsonArray(Collection<SaltForm> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<SaltForm> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<SaltForm> fromJsonArrayToSaltForms(String json) {
        return new JSONDeserializer<List<SaltForm>>()
        .use("values", SaltForm.class).deserialize(json);
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

	public String getMolStructure() {
        return this.molStructure;
    }

	public void setMolStructure(String molStructure) {
        this.molStructure = molStructure;
    }

	public String getCorpName() {
        return this.corpName;
    }

	public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

	public String getCasNumber() {
        return this.casNumber;
    }

	public void setCasNumber(String casNumber) {
        this.casNumber = casNumber;
    }

	public String getChemist() {
        return this.chemist;
    }

	public void setChemist(String chemist) {
        this.chemist = chemist;
    }

	public Date getRegistrationDate() {
        return this.registrationDate;
    }

	public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

	public int getCdId() {
        return this.CdId;
    }

	public void setCdId(int CdId) {
        this.CdId = CdId;
    }

	public Boolean getIgnore() {
        return this.ignore;
    }

	public void setIgnore(Boolean ignore) {
        this.ignore = ignore;
    }

	public Double getSaltWeight() {
        return this.saltWeight;
    }

	public void setSaltWeight(Double saltWeight) {
        this.saltWeight = saltWeight;
    }

	public Parent getParent() {
        return this.parent;
    }

	public void setParent(Parent parent) {
        this.parent = parent;
    }

	public Set<Lot> getLots() {
        return this.lots;
    }

	public void setLots(Set<Lot> lots) {
        this.lots = lots;
    }

	public Set<IsoSalt> getIsoSalts() {
        return this.isoSalts;
    }

	public void setIsoSalts(Set<IsoSalt> isoSalts) {
        this.isoSalts = isoSalts;
    }

	public BulkLoadFile getBulkLoadFile() {
        return this.bulkLoadFile;
    }

	public void setBulkLoadFile(BulkLoadFile bulkLoadFile) {
        this.bulkLoadFile = bulkLoadFile;
    }

	public static Long countFindSaltFormsByBulkLoadFileEquals(BulkLoadFile bulkLoadFile) {
        if (bulkLoadFile == null) throw new IllegalArgumentException("The bulkLoadFile argument is required");
        EntityManager em = SaltForm.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM SaltForm AS o WHERE o.bulkLoadFile = :bulkLoadFile", Long.class);
        q.setParameter("bulkLoadFile", bulkLoadFile);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindSaltFormsByCdId(int CdId) {
        EntityManager em = SaltForm.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM SaltForm AS o WHERE o.CdId = :CdId", Long.class);
        q.setParameter("CdId", CdId);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindSaltFormsByCorpNameEquals(String corpName) {
        if (corpName == null || corpName.length() == 0) throw new IllegalArgumentException("The corpName argument is required");
        EntityManager em = SaltForm.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM SaltForm AS o WHERE o.corpName = :corpName", Long.class);
        q.setParameter("corpName", corpName);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindSaltFormsByCorpNameLike(String corpName) {
        if (corpName == null || corpName.length() == 0) throw new IllegalArgumentException("The corpName argument is required");
        corpName = corpName.replace('*', '%');
        if (corpName.charAt(0) != '%') {
            corpName = "%" + corpName;
        }
        if (corpName.charAt(corpName.length() - 1) != '%') {
            corpName = corpName + "%";
        }
        EntityManager em = SaltForm.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM SaltForm AS o WHERE LOWER(o.corpName) LIKE LOWER(:corpName)", Long.class);
        q.setParameter("corpName", corpName);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindSaltFormsByParent(Parent parent) {
        if (parent == null) throw new IllegalArgumentException("The parent argument is required");
        EntityManager em = SaltForm.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM SaltForm AS o WHERE o.parent = :parent", Long.class);
        q.setParameter("parent", parent);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<SaltForm> findSaltFormsByBulkLoadFileEquals(BulkLoadFile bulkLoadFile) {
        if (bulkLoadFile == null) throw new IllegalArgumentException("The bulkLoadFile argument is required");
        EntityManager em = SaltForm.entityManager();
        TypedQuery<SaltForm> q = em.createQuery("SELECT o FROM SaltForm AS o WHERE o.bulkLoadFile = :bulkLoadFile", SaltForm.class);
        q.setParameter("bulkLoadFile", bulkLoadFile);
        return q;
    }

	public static TypedQuery<SaltForm> findSaltFormsByBulkLoadFileEquals(BulkLoadFile bulkLoadFile, String sortFieldName, String sortOrder) {
        if (bulkLoadFile == null) throw new IllegalArgumentException("The bulkLoadFile argument is required");
        EntityManager em = SaltForm.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM SaltForm AS o WHERE o.bulkLoadFile = :bulkLoadFile");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<SaltForm> q = em.createQuery(queryBuilder.toString(), SaltForm.class);
        q.setParameter("bulkLoadFile", bulkLoadFile);
        return q;
    }

	public static TypedQuery<SaltForm> findSaltFormsByCdId(int CdId) {
        EntityManager em = SaltForm.entityManager();
        TypedQuery<SaltForm> q = em.createQuery("SELECT o FROM SaltForm AS o WHERE o.CdId = :CdId", SaltForm.class);
        q.setParameter("CdId", CdId);
        return q;
    }

	public static TypedQuery<SaltForm> findSaltFormsByCdId(int CdId, String sortFieldName, String sortOrder) {
        EntityManager em = SaltForm.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM SaltForm AS o WHERE o.CdId = :CdId");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<SaltForm> q = em.createQuery(queryBuilder.toString(), SaltForm.class);
        q.setParameter("CdId", CdId);
        return q;
    }

	public static TypedQuery<SaltForm> findSaltFormsByCorpNameEquals(String corpName) {
        if (corpName == null || corpName.length() == 0) throw new IllegalArgumentException("The corpName argument is required");
        EntityManager em = SaltForm.entityManager();
        TypedQuery<SaltForm> q = em.createQuery("SELECT o FROM SaltForm AS o WHERE o.corpName = :corpName", SaltForm.class);
        q.setParameter("corpName", corpName);
        return q;
    }

	public static TypedQuery<SaltForm> findSaltFormsByCorpNameEquals(String corpName, String sortFieldName, String sortOrder) {
        if (corpName == null || corpName.length() == 0) throw new IllegalArgumentException("The corpName argument is required");
        EntityManager em = SaltForm.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM SaltForm AS o WHERE o.corpName = :corpName");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<SaltForm> q = em.createQuery(queryBuilder.toString(), SaltForm.class);
        q.setParameter("corpName", corpName);
        return q;
    }

	public static TypedQuery<SaltForm> findSaltFormsByCorpNameLike(String corpName) {
        if (corpName == null || corpName.length() == 0) throw new IllegalArgumentException("The corpName argument is required");
        corpName = corpName.replace('*', '%');
        if (corpName.charAt(0) != '%') {
            corpName = "%" + corpName;
        }
        if (corpName.charAt(corpName.length() - 1) != '%') {
            corpName = corpName + "%";
        }
        EntityManager em = SaltForm.entityManager();
        TypedQuery<SaltForm> q = em.createQuery("SELECT o FROM SaltForm AS o WHERE LOWER(o.corpName) LIKE LOWER(:corpName)", SaltForm.class);
        q.setParameter("corpName", corpName);
        return q;
    }

	public static TypedQuery<SaltForm> findSaltFormsByCorpNameLike(String corpName, String sortFieldName, String sortOrder) {
        if (corpName == null || corpName.length() == 0) throw new IllegalArgumentException("The corpName argument is required");
        corpName = corpName.replace('*', '%');
        if (corpName.charAt(0) != '%') {
            corpName = "%" + corpName;
        }
        if (corpName.charAt(corpName.length() - 1) != '%') {
            corpName = corpName + "%";
        }
        EntityManager em = SaltForm.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM SaltForm AS o WHERE LOWER(o.corpName) LIKE LOWER(:corpName)");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<SaltForm> q = em.createQuery(queryBuilder.toString(), SaltForm.class);
        q.setParameter("corpName", corpName);
        return q;
    }

	public static TypedQuery<SaltForm> findSaltFormsByParent(Parent parent) {
        if (parent == null) throw new IllegalArgumentException("The parent argument is required");
        EntityManager em = SaltForm.entityManager();
        TypedQuery<SaltForm> q = em.createQuery("SELECT o FROM SaltForm AS o WHERE o.parent = :parent", SaltForm.class);
        q.setParameter("parent", parent);
        return q;
    }

	public static TypedQuery<SaltForm> findSaltFormsByParent(Parent parent, String sortFieldName, String sortOrder) {
        if (parent == null) throw new IllegalArgumentException("The parent argument is required");
        EntityManager em = SaltForm.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM SaltForm AS o WHERE o.parent = :parent");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<SaltForm> q = em.createQuery(queryBuilder.toString(), SaltForm.class);
        q.setParameter("parent", parent);
        return q;
    }
}
