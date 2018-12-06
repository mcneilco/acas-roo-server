package com.labsynch.labseer.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.dto.LabelPrefixDTO;
import com.labsynch.labseer.dto.SearchFormDTO;
import com.labsynch.labseer.service.ChemStructureService;
import com.labsynch.labseer.service.ParentService;

@Transactional
@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = { "findParentsByCorpNameEquals", "findParentsByCorpNameLike", "findParentsByCdId", "findParentsBySaltForms", "findParentsByCommonNameLike", "findParentsByBulkLoadFileEquals" })
public class Parent {

	private static final Logger logger = LoggerFactory.getLogger(Parent.class);
	
//	@Autowired
//	private ChemStructureService chemService;
	
	@Column(unique = true)
	@NotNull
	@Size(max = 255)
	@org.hibernate.annotations.Index(name="Parent_corpName_IDX")
	private String corpName;

	@org.hibernate.annotations.Index(name="Parent_parentNumber_IDX")
	private long parentNumber;

	@ManyToOne
	@org.hibernate.annotations.Index(name="Parent_chemist_IDX")
	private Scientist chemist;

	@Size(max = 1000)
	@org.hibernate.annotations.Index(name="Parent_commonName_IDX")
	private String commonName;

	@ManyToOne
	@org.hibernate.annotations.Index(name="Parent_stereoCategory_IDX")
	private StereoCategory stereoCategory;

	@Size(max = 1000)
	private String stereoComment;

	@Column(columnDefinition="text")
	private String molStructure;

	private Double molWeight;
	
	private Double exactMass;

	@Size(max = 4000)
	private String molFormula;

	@NotNull
	@org.hibernate.annotations.Index(name="Parent_CdId_IDX")
	private int CdId;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "S-")
	@org.hibernate.annotations.Index(name="Parent_RegDate_IDX")
	private Date registrationDate;
	
	@ManyToOne
	private Scientist registeredBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "S-")
	private Date modifiedDate;
	
	@ManyToOne
	private Scientist modifiedBy;

	private Boolean ignore;

	@OneToMany(cascade = CascadeType.MERGE, mappedBy = "parent", fetch = FetchType.LAZY)
	private Set<SaltForm> saltForms = new HashSet<SaltForm>();
	
	@OneToMany(cascade = CascadeType.MERGE, mappedBy = "parent", fetch = FetchType.LAZY)
	private Set<ParentAlias> parentAliases = new HashSet<ParentAlias>();

    @ManyToOne
    private BulkLoadFile bulkLoadFile;
    
    @ManyToOne
    private ParentAnnotation parentAnnotation;
    
    @ManyToOne
    private CompoundType compoundType;
    
    @Column(columnDefinition="text")
    private String comment;
    
    private Boolean isMixture;
    
    @Transient
    private transient LabelPrefixDTO labelPrefix;
    

	@Transactional
	public static void deleteAllParents(){        
		List<Parent> parents = Parent.findAllParents();
		for (Parent parent : parents){
			parent.remove();
		}   	
	}

    @Transactional
    public static List<Parent> findAllParents() {
        return Parent.entityManager().createQuery("SELECT o FROM Parent o", Parent.class).getResultList();
    }
    
    @Transactional
    public static Parent findParent(Long id) {
        if (id == null) return null;
        return Parent.entityManager().find(Parent.class, id);
    }

    @Transactional
    public static List<Parent> findParentEntries(int firstResult, int maxResults) {
        return Parent.entityManager().createQuery("SELECT o FROM Parent o", Parent.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static TypedQuery<Parent> findParentsByCdId(int CdId) {
        EntityManager em = Parent.entityManager();
        TypedQuery<Parent> q = em.createQuery("SELECT o FROM Parent AS o WHERE o.CdId = :CdId AND (o.ignore is null OR o.ignore = :ignore)", Parent.class);
        q.setParameter("CdId", CdId);
        q.setParameter("ignore", false);
        return q;
    }
    
    @Transactional
    public static List<Parent> findParentsByLessMolWeight(double molWeight) {
		EntityManager em = Parent.entityManager();
		String sqlQuery = "SELECT o FROM Parent o WHERE o.molWeight < :molWeight ";
		TypedQuery<Parent> query = em.createQuery(sqlQuery, Parent.class);
		query.setParameter("molWeight", molWeight);
		List<Parent> parents = query.getResultList();

		return parents;       
    }
    
    
    @Transactional	
	public static List<Integer> findParentCdIdsByMeta(SearchFormDTO searchParams) {
		EntityManager em = Parent.entityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Integer> criteria = criteriaBuilder.createQuery(Integer.class);
		Root<Parent> parentRoot = criteria.from(Parent.class);
		Join<Parent, SaltForm> parentSaltForm = parentRoot.join("saltForms");
		Join<SaltForm, Lot> saltFormLot = parentRoot.join("saltForms").join("lots");
		Join<Parent, ParentAlias> parentAliases = parentRoot.join("parentAliases", JoinType.LEFT);

		criteria.select(parentRoot.<Integer>get("CdId"));
		criteria.distinct(true);
		ParameterExpression<Date> firstDate = criteriaBuilder.parameter(Date.class);
		ParameterExpression<Date> lastDate = criteriaBuilder.parameter(Date.class);
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
			Predicate predicate = criteriaBuilder.equal(parentSaltForm.get("corpName"), searchParams.getSaltFormCorpName());
			predicateList.add(predicate);
		}
		if (searchParams.getParentCorpName() != null) {
			logger.debug("incoming parentCorpName :" + searchParams.getParentCorpName());
			Predicate predicate = criteriaBuilder.equal(parentRoot.get("corpName"), searchParams.getParentCorpName());
			predicateList.add(predicate);
		}
		if (searchParams.getAlias() != null && !searchParams.getAlias().equals("")) {
			Predicate predicate = null;
			String aliasName = searchParams.getAlias().trim().toUpperCase();
			logger.debug("incoming alias name :" + searchParams.getAlias());
			if (searchParams.getAliasContSelect().equalsIgnoreCase("exact")) {
				predicate = criteriaBuilder.equal(criteriaBuilder.upper(parentAliases.<String>get("aliasName")), aliasName.toUpperCase());
			} else {
				logger.debug("setting up a like query: " + searchParams.getAliasContSelect());
				aliasName = "%" + aliasName + "%";
				predicate = criteriaBuilder.like(criteriaBuilder.upper(parentAliases.<String>get("aliasName")), aliasName);				
			}
			predicateList.add(predicate);
		}
		if (searchParams.getMinParentNumber() != null && searchParams.getMaxParentNumber() != null) {
			Predicate predicate1 = criteriaBuilder.greaterThanOrEqualTo(parentRoot.<Long>get("parentNumber"), searchParams.getMinParentNumber());
			Predicate predicate2 = criteriaBuilder.lessThanOrEqualTo(parentRoot.<Long>get("parentNumber"), searchParams.getMaxParentNumber());
			predicateList.add(predicate1);
			predicateList.add(predicate2);
		}
		predicates = predicateList.toArray(predicates);
		criteria.where(criteriaBuilder.and(predicates));
		TypedQuery<Integer> q = em.createQuery(criteria);
		if (searchParams.getMinSynthDate() != null) {
			q.setParameter(firstDate, searchParams.getMinSynthDate(), TemporalType.DATE);
		}
		if (searchParams.getMaxSynthDate() != null) {
			q.setParameter(lastDate, searchParams.getMaxSynthDate(), TemporalType.DATE);
		}
		List<Integer> parentCdIds = null;
		if (predicates.length == 0 && searchParams.getMaxSynthDate() == null && searchParams.getMaxSynthDate() == null) {
			logger.debug("No search criteria. Returning null.");
		} else {
			logger.debug("parent search query string: " + q.toString());
			parentCdIds = q.getResultList();
		}
		if (searchParams.getMaxResults() != null) q.setMaxResults(searchParams.getMaxResults());
		
		return parentCdIds;
	}


    
	public static Parent update(Parent inputParent) {
		Parent parent = Parent.findParent(inputParent.getId());
		parent.setChemist(inputParent.getChemist());
		parent.setCommonName(inputParent.getCommonName());
		parent.setCorpName(inputParent.getCorpName());
		parent.setIgnore(inputParent.getIgnore());
		parent.setParentNumber(inputParent.getParentNumber());
		parent.setRegistrationDate(inputParent.getRegistrationDate());
		parent.setStereoCategory(inputParent.getStereoCategory());
		parent.setStereoComment(inputParent.getStereoComment());
		parent.setCompoundType(inputParent.getCompoundType());
		parent.setParentAnnotation(inputParent.getParentAnnotation());
		parent.setComment(inputParent.getComment());
		parent.setIsMixture(inputParent.getIsMixture());
		return parent;
	}

	public static List<Long> getParentIds() {
		EntityManager em = Parent.entityManager();
		String sqlQuery = "SELECT o.id FROM Parent o WHERE o.ignore IS NOT :ignore OR o.ignore is null";
		boolean ignore = true;
		TypedQuery<Long> query = em.createQuery(sqlQuery, Long.class);
		query.setParameter("ignore", ignore);
		List<Long> parentIds = query.getResultList();		
		return parentIds;
	}
	
	public LabelPrefixDTO getLabelPrefix() {
		return this.labelPrefix;
	}
	
	public void setLabelPrefix(LabelPrefixDTO labelPrefix) {
		this.labelPrefix = labelPrefix;
	}
	

    public static Long countParentsByStereoCategory(StereoCategory stereoCategory) {
        if (stereoCategory == null) throw new IllegalArgumentException("The stereoCategory argument is required");
        EntityManager em = Lot.entityManager();
        TypedQuery<Long> q = em.createQuery("SELECT COUNT(p) FROM Parent p WHERE p.stereoCategory = :stereoCategory ", Long.class);
        q.setParameter("stereoCategory", stereoCategory);
        return q.getSingleResult();
    }
    

}
