package com.labsynch.labseer.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.dto.SearchFormDTO;

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

	@ManyToOne
    @org.hibernate.annotations.Index(name="SaltForm_Chemist_IDX")
	private Scientist chemist;

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

		if (searchParams.getChemist() != null && searchParams.getChemist().getId() != 0) {
			logger.debug("incoming chemist :" + searchParams.getChemist().toString());
			Predicate predicate = criteriaBuilder.equal(saltFormLot.<Scientist>get("chemist"), searchParams.getChemist());
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
		
		if (searchParams.getChemist() != null && searchParams.getChemist().getId() != 0) {
			logger.debug("incoming chemist :" + searchParams.getChemist().toString());
			Predicate predicate = criteriaBuilder.equal(saltFormLot.<Scientist>get("chemist"), searchParams.getChemist());
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

		if (searchParams.getChemist() != null && searchParams.getChemist().getId() != 0) {
			logger.debug("incoming chemist :" + searchParams.getChemist().toString());
			Predicate predicate = criteriaBuilder.equal(saltFormLot.<Scientist>get("chemist"), searchParams.getChemist());
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

		if (searchParams.getChemist() != null && searchParams.getChemist().getId() != 0) {
			logger.debug("incoming chemist :" + searchParams.getChemist().toString());
			Predicate predicate = criteriaBuilder.equal(saltFormLot.<Scientist>get("chemist"), searchParams.getChemist());
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

}
