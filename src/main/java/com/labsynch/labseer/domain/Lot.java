package com.labsynch.labseer.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Query;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.dto.SearchFormDTO;
import com.labsynch.labseer.dto.configuration.MainConfigDTO;
import com.labsynch.labseer.utils.Configuration;

import flexjson.JSONSerializer;

@Transactional
@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = { "findLotsByCorpNameEquals", "findLotsByCorpNameLike", "findLotsBySynthesisDateBetween", 
		                         "findLotsBySaltForm", "findLotsBySynthesisDateGreaterThan", "findLotsBySynthesisDateLessThan", 
		                         "findLotsByChemistAndSynthesisDateBetween", "findLotsByIsVirtualNot", "findLotsByBuid", 
		                         "findLotsByNotebookPageEquals", "findLotsByNotebookPageEqualsAndIgnoreNot", 
		                         "findLotsByBulkLoadFileEquals" })
public class Lot {
	
    private static final Logger logger = LoggerFactory.getLogger(Lot.class);
    
	private static final MainConfigDTO mainConfig = Configuration.getConfigInfo();
    
	private static final int formatBatchDigits = Configuration.getConfigInfo().getServerSettings().getFormatBatchDigits();

	private static final boolean appendSaltCodeToLotName = Configuration.getConfigInfo().getServerSettings().isAppendSaltCodeToLotName();

	private static final String noSaltCode = Configuration.getConfigInfo().getServerSettings().getNoSaltCode();

	private static final String corpBatchFormat = Configuration.getConfigInfo().getServerSettings().getCorpBatchFormat();

	private static final String corpPrefix = Configuration.getConfigInfo().getServerSettings().getCorpPrefix();

	private static final String corpSeparator = Configuration.getConfigInfo().getServerSettings().getCorpSeparator();

	@Column(columnDefinition="text")
    private String asDrawnStruct;

    private int lotAsDrawnCdId;

    private Long buid;

    @Column(unique = true)
    @NotNull
    @Size(max = 255)
    @org.hibernate.annotations.Index(name = "Lot_corpName_IDX")
    private String corpName;

    @org.hibernate.annotations.Index(name = "Lot_lotNumber_IDX")
    private Integer lotNumber;

    private Double lotMolWeight;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    @org.hibernate.annotations.Index(name = "Lot_SynthDate_IDX")
    private Date synthesisDate;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    @org.hibernate.annotations.Index(name = "Lot_RegDate_IDX")
    private Date registrationDate;
    
	private String registeredBy;
    
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "S-")
	private Date modifiedDate;
	
	private String modifiedBy;

    @Size(max = 255)
    @org.hibernate.annotations.Index(name = "Lot_Barcode_IDX")
    private String barcode;
    
    @Size(max = 255)
    private String color;

    @Size(max = 255)
    @org.hibernate.annotations.Index(name = "Lot_Notebook_IDX")
    private String notebookPage;

    private Double amount;

    @ManyToOne
    private Unit amountUnits;

    private Double solutionAmount;

    @ManyToOne    
    private SolutionUnit solutionAmountUnits;
    
    @Size(max = 255)
    private String supplier;

    @Size(max = 255)
    private String supplierID;

    private Double purity;

    @ManyToOne
    private Operator purityOperator;

    @ManyToOne
    private PurityMeasuredBy purityMeasuredBy;

    private String chemist;

    private Double percentEE;

    @Column(columnDefinition="text")
    private String comments;

    @org.hibernate.annotations.Index(name = "Lot_Virtual_IDX")
    private Boolean isVirtual;

    private Boolean ignore;

    
    @ManyToOne
    @org.hibernate.annotations.Index(name = "Lot_PhysicalState_IDX")
    private PhysicalState physicalState;

    @ManyToOne
    @org.hibernate.annotations.Index(name = "Lot_Vendor_IDX")
    private Vendor vendor;

    @Size(max = 255)
    private String vendorID;

    @ManyToOne
    @org.hibernate.annotations.Index(name = "Lot_SaltForm_IDX")
    private SaltForm saltForm;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "lot", fetch = FetchType.LAZY)
    private Set<FileList> fileLists = new HashSet<FileList>();

    private Double retain;

    @ManyToOne
    private Unit retainUnits;
    
    private String retainLocation;

    private Double meltingPoint;

    private Double boilingPoint;

    @Size(max = 255)
    private String supplierLot;

    @ManyToOne
    @org.hibernate.annotations.Index(name = "Lot_Project_IDX")
    private Project project;

    @Transient
    private Parent parent;
    
    @ManyToOne
    private BulkLoadFile bulkLoadFile;
    
    private Double lambda;
    
    private Double absorbance;
    
    private String stockSolvent;
    
    private String stockLocation;
    
    private Double observedMassOne;
    
    private Double observedMassTwo;
    
    private Double tareWeight;
    
    @ManyToOne
    private Unit tareWeightUnits;
    
    private Double totalAmountStored;
    
    @ManyToOne
    private Unit totalAmountStoredUnits;
    
    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "lot", fetch = FetchType.LAZY)
	private Set<LotAlias> lotAliases = new HashSet<LotAlias>();
   
    @Transient
    private transient String storageLocation;
    
    public String getStorageLocation() {
    		return this.storageLocation;
    }
    
    public void setStorageLocation(String storageLocation) {
    		this.storageLocation = storageLocation;
    }
    
    public Long getBuid() {
        return this.buid;
    }

    public Parent getParent() {
        if (this.saltForm != null) {
            return this.saltForm.getParent();
        } else {
            return this.parent;
        }
    }

    public void setParent(Parent parent) {
        if (this.saltForm != null) {
            this.saltForm.setParent(parent);
        }
        this.parent = parent;
    }

    @Transactional
    public SaltForm getSaltForm() {
        return this.saltForm;
    }

    public void setSaltForm(SaltForm saltForm) {
        this.saltForm = saltForm;
        if (this.saltForm != null && this.saltForm.getParent() == null) {
            this.saltForm.setParent(this.parent);
        }
    }

	public static int generateCasCheckDigit(String inputCasLabel){
		int checkDigit = 0;
		logger.info("" + inputCasLabel.length());
		int sum = 0;
		for (int index=0; index < inputCasLabel.length(); index++){
			logger.info("current index: " + index);
			sum += (inputCasLabel.length() - index ) * Integer.parseInt(inputCasLabel.substring(index, index+1));
			logger.info("sum: " + sum);
		}
		checkDigit = sum % 10;
		return checkDigit;
	}
    
    public String generateCorpName() {
        MainConfigDTO mainConfig = Configuration.getConfigInfo();
        logger.info("corp batch format is: " + mainConfig.getServerSettings().getCorpBatchFormat());
        logger.info("other corp batch format is: " + corpBatchFormat);

        String corpName = null;
        if (mainConfig.getServerSettings().getCorpBatchFormat().equalsIgnoreCase("cas_style_format")){
        	corpName = generateCasStyleLotName();
        } else if (mainConfig.getMetaLot().isSaltBeforeLot()) {
            corpName = generateSaltFormLotName();
        } else {
            corpName = generateParentLotName();
        }
        return corpName;
    }
    
    public int generateLotNumber() {
        MainConfigDTO mainConfig = Configuration.getConfigInfo();

        int lotNumber = 0;
        if (mainConfig.getMetaLot().isSaltBeforeLot()) {
        	lotNumber = generateSaltFormLotNumber();
        } else {
        	lotNumber = generateParentLotNumber();
        }
        return lotNumber;
    }
    
    private int generateParentLotNumber(){
    	//set lot number
		Integer lotNumber = this.getLotNumber();
        if (this.getIsVirtual()) {
            lotNumber = 0;
        } else {
        	if (this.lotNumber != null && this.lotNumber > 0){
        		lotNumber = this.lotNumber;
        	}else{
        		int lotCount = 0;
        		if (this.getParent().getId() == null){
        			logger.debug("Setting lotCount of new parent = 0");
        		}else if (Lot.getMaxParentLotNumber(this.getParent()) == null) {
                    logger.debug("this is a null pointer exception. Set lotCount = 0");
                } else {
                    lotCount = Lot.getMaxParentLotNumber(this.getParent());
                }
                logger.debug("Lot Count = " + lotCount);
                lotNumber = lotCount + 1;
        	}
        }
        logger.debug("Lot Number = " + lotNumber);
        return lotNumber;
    }
    
    private int generateSaltFormLotNumber(){
    	Integer lotNumber = 0;
        if (this.getIsVirtual()) {
            lotNumber = 0;
        } else {
        	if (this.lotNumber != null && this.lotNumber > 0){
        		lotNumber = this.lotNumber;
        	}else{
        		 int lotCount = 0;
                 if (Lot.getMaxSaltFormLotNumber(this.getSaltForm()) == null) {
                     logger.error("this is a null pointer exception. Set lotCount = 0");
                 } else {
                     lotCount = Lot.getMaxSaltFormLotNumber(this.getSaltForm());
                 }
                 logger.debug("Lot Count = " + lotCount);
                 lotNumber = lotCount + 1;
        	}
        }
        logger.debug("Lot Number = " + lotNumber);
        return lotNumber;
    }

    public String generateCasStyleLotName() {
    	List seqList = generateCustomLotSequence();
		String inputSequence = seqList.get(0).toString();
		int casCheckDigit = Lot.generateCasCheckDigit(inputSequence);
		String fullName =String.format("%09d", Long.parseLong(inputSequence));
		logger.info(fullName);
		StringBuilder sb = new StringBuilder();
		sb = sb.append(corpPrefix).append(corpSeparator);
		
		String regexPattern = "(\\d{3})(\\d{3})(\\d{3})";
		Pattern p = Pattern.compile(regexPattern);
		Matcher m = p.matcher(fullName);
		if (m.find()){
			sb.append(m.group(1)).append(corpSeparator);
			sb.append(m.group(2)).append(corpSeparator);
			sb.append(m.group(3)).append(corpSeparator);
			sb.append(Integer.toString(casCheckDigit));
		}
		String lotName = sb.toString();
		logger.info(lotName);

		//set lot number
		int lotNumber = this.generateParentLotNumber();
        this.setLotNumber(lotNumber);
		
    	return lotName;
    }
    
    private String generateParentLotName() {
        logger.debug("generating the new lot corp name");
        corpName = this.getSaltForm().getParent().getCorpName();
        logger.debug("Parent corpName = " + corpName);
        int lotNumber = this.generateParentLotNumber();
        this.setLotNumber(lotNumber);
        
        if (formatBatchDigits == 0){
        	logger.error("formatBatchDigits is set to " + formatBatchDigits);
        }
        String batchFormat = "%0"+ formatBatchDigits + "d";
        logger.debug("batch format is " + batchFormat);
        if (corpBatchFormat.equalsIgnoreCase("corp_saltcode_batch")){
        	corpName = corpName.concat(CorpName.saltSeparator);
        	corpName = appendSaltCode(corpName);
            corpName = corpName.concat(CorpName.batchSeparator).concat(String.format(batchFormat, lotNumber));
        } else if (corpBatchFormat.equalsIgnoreCase("corp_batch_saltcode")) {
            corpName = corpName.concat(CorpName.batchSeparator).concat(String.format(batchFormat, lotNumber));
            if (appendSaltCodeToLotName){
            	corpName = corpName.concat(CorpName.saltSeparator);
            	corpName = appendSaltCode(corpName);
            }        	
        } else {
            corpName = corpName.concat(CorpName.batchSeparator).concat(String.format(batchFormat, lotNumber));
        }
        
        logger.debug("corpName: " + corpName);
        return corpName;
    }

    private String appendSaltCode(String corpName) {
    	List<IsoSalt> isoSalts = IsoSalt.findIsoSaltsBySaltForm(this.getSaltForm()).getResultList();
    	logger.debug("number of isoSalts: " + isoSalts.size());
    	if (isoSalts.size() == 0){
        	corpName = corpName.concat(noSaltCode);        		
    	} else {
    		for (IsoSalt isoSalt:isoSalts){
    			if (isoSalt.getType().equalsIgnoreCase("salt")){
                   	corpName = corpName.concat(isoSalt.getSalt().getAbbrev());        		        				
    			} else if (isoSalt.getType().equalsIgnoreCase("isotope")){
                   	corpName = corpName.concat(isoSalt.getIsotope().getAbbrev());        		
    			}                           			
    		}
    	}
		return corpName;
	}

	private String generateSaltFormLotName() {
        corpName = this.getSaltForm().getCorpName();
        logger.debug("Salt corpName = " + corpName);
        int lotNumber = this.generateSaltFormLotNumber();
        this.setLotNumber(lotNumber);
        
        String batchFormat = "%0"+ formatBatchDigits + "d";
        corpName = corpName.concat(CorpName.batchSeparator).concat(String.format(batchFormat, lotNumber));
        logger.debug("corpName: " + corpName);
        return corpName;
    }
	
    @Transactional
    public static List<Lot> findLotsByLessMolWeight(double molWeight) {
		EntityManager em = Lot.entityManager();
		String sqlQuery = "SELECT o FROM Lot o WHERE o.lotMolWeight < :molWeight ";
		TypedQuery<Lot> query = em.createQuery(sqlQuery, Lot.class);
		query.setParameter("molWeight", molWeight);
		List<Lot> lots = query.getResultList();

		return lots;       
    }
  
    
	public static double calculateLotMolWeight(Lot lot) {
		double totalLotWeight = lot.getSaltForm().getSaltWeight() + lot.getParent().getMolWeight();
		logger.debug("the new lot weight: " + totalLotWeight);
		return totalLotWeight;
	}


    @Transactional
    public static List<Lot> findAllLots() {
        return Lot.entityManager().createQuery("SELECT o FROM Lot o", Lot.class).getResultList();
    }
    
    @Transactional  
    public static List<Lot> findLotEntries(int firstResult, int maxResults) {
        return Lot.entityManager().createQuery("SELECT o FROM Lot o", Lot.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional  
    public static Lot findLot(Long id) {
        if (id == null) return null;
        return Lot.entityManager().find(Lot.class, id);
    }
    

    public static TypedQuery<Lot> findLotsByBuidNumber(Long buidNumber) {
        if (buidNumber == null) throw new IllegalArgumentException("The buidNumber argument is required");
        EntityManager em = Lot.entityManager();
        TypedQuery<Lot> q = em.createQuery("SELECT o FROM Lot AS o WHERE o.buid = :buidNumber AND o.ignore IS null", Lot.class);
        q.setParameter("buidNumber", buidNumber);
        return q;
    }
    
    public static TypedQuery<Lot> findLotsByNotebookPageEquals(String notebookPage) {
        if (notebookPage == null || notebookPage.length() == 0) throw new IllegalArgumentException("The notebookPage argument is required");
        EntityManager em = Lot.entityManager();
//        boolean ignore = true;
        TypedQuery<Lot> q = em.createQuery("SELECT o FROM Lot AS o WHERE o.notebookPage = :notebookPage AND o.ignore IS null", Lot.class);
        q.setParameter("notebookPage", notebookPage);
//        q.setParameter("ignore", ignore);
        return q;
    }

    public static TypedQuery<Lot> findLotsByDate(Date synthesisDate) {
        if (synthesisDate == null) throw new IllegalArgumentException("The synthesisDate argument is required");
        EntityManager em = Lot.entityManager();
        TypedQuery<Lot> q = em.createQuery("SELECT o FROM Lot AS o WHERE o.synthesisDate > :synthesisDate", Lot.class);
        q.setParameter("synthesisDate", synthesisDate);
        return q;
    }

    public static TypedQuery<Lot> findLotsByMetaQuery(Author chemist, Date minSynthesisDate, Date maxSynthesisDate) {
        EntityManager em = Lot.entityManager();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Lot> criteria = criteriaBuilder.createQuery(Lot.class);
        Root<Lot> lotRoot = criteria.from(Lot.class);
        criteria.select(lotRoot);
        ParameterExpression<Date> firstDate = criteriaBuilder.parameter(Date.class);
        ParameterExpression<Date> lastDate = criteriaBuilder.parameter(Date.class);
        Predicate[] predicates = new Predicate[0];
        List<Predicate> predicateList = new ArrayList<Predicate>();
        if (chemist != null && chemist.getId() != 0) {
            logger.debug("incoming chemist :" + chemist.toString());
            Predicate predicate1 = criteriaBuilder.equal(lotRoot.<Author>get("chemist"), chemist);
            predicateList.add(predicate1);
        }
        if (minSynthesisDate != null) {
            logger.debug("incoming minSynthesisDate :" + minSynthesisDate);
            Predicate predicate2 = criteriaBuilder.greaterThanOrEqualTo(lotRoot.<Date>get("synthesisDate"), firstDate);
            predicateList.add(predicate2);
        }
        if (maxSynthesisDate != null) {
            logger.debug("incoming maxSynthesisDate :" + maxSynthesisDate);
            Predicate predicate3 = criteriaBuilder.lessThanOrEqualTo(lotRoot.<Date>get("synthesisDate"), lastDate);
            predicateList.add(predicate3);
        }
        criteria.where(criteriaBuilder.and(predicateList.toArray(predicates)));
        TypedQuery<Lot> q = em.createQuery(criteria);
        if (maxSynthesisDate != null) {
            q.setParameter(lastDate, maxSynthesisDate, TemporalType.DATE);
        }
        if (minSynthesisDate != null) {
            q.setParameter(firstDate, minSynthesisDate, TemporalType.DATE);
        }
        return q;
    }

    public static Long countNonVirtualSaltFormLots(SaltForm saltForm, Boolean isVirtual) {
        if (saltForm == null) throw new IllegalArgumentException("The saltForm argument is required");
        if (isVirtual == null) throw new IllegalArgumentException("The isVirtual argument is required");
        EntityManager em = Lot.entityManager();
        TypedQuery<Long> q = em.createQuery("SELECT COUNT(o) FROM Lot o WHERE o.saltForm = :saltForm AND o.isVirtual IS NOT :isVirtual", Long.class);
        q.setParameter("saltForm", saltForm);
        q.setParameter("isVirtual", isVirtual);
        return q.getSingleResult();
    }

    public static Integer getMaxSaltFormLotNumber(SaltForm saltForm) {
        if (saltForm == null) throw new IllegalArgumentException("The saltForm argument is required");
        EntityManager em = Lot.entityManager();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Integer> criteria = criteriaBuilder.createQuery(Integer.class);
        Root<Lot> lotRoot = criteria.from(Lot.class);
        Join<Lot, SaltForm> lotSaltForm = lotRoot.join("saltForm");
        Predicate predicate = criteriaBuilder.equal(lotSaltForm, saltForm);
        criteria.select(criteriaBuilder.max(lotRoot.<Integer>get("lotNumber")));
        criteria.where(criteriaBuilder.and(predicate));
        TypedQuery<Integer> q = em.createQuery(criteria);
        return q.getSingleResult();
    }

    public static Integer getMaxParentLotNumber(Parent parent) {
        if (parent == null) throw new IllegalArgumentException("The parent argument is required");
        logger.debug("get max parent lot number for parent " + parent.getCorpName());
        EntityManager em = Lot.entityManager();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Integer> criteria = criteriaBuilder.createQuery(Integer.class);
        Root<Lot> lotRoot = criteria.from(Lot.class);
        Join<Lot, SaltForm> lotSaltForm = lotRoot.join("saltForm");
        Join<SaltForm, Parent> saltFormParent = lotSaltForm.join("parent");
        Predicate predicate = criteriaBuilder.equal(saltFormParent, parent);
        criteria.select(criteriaBuilder.max(lotRoot.<Integer>get("lotNumber")));
        criteria.where(criteriaBuilder.and(predicate));
        TypedQuery<Integer> q = em.createQuery(criteria);
        return q.getSingleResult();
    }

	@SuppressWarnings("unchecked")
	public static List<Long> generateCustomLotSequence() {
        String sqlQuery = "SELECT nextval('custom_lot_pkseq')";
        logger.info(sqlQuery);
        EntityManager em = Lot.entityManager();
        Query q = em.createNativeQuery(sqlQuery);
        return q.getResultList();
    }
    
    public static Long countLotsByParent(Parent parent) {
        if (parent == null) throw new IllegalArgumentException("The parent argument is required");
        EntityManager em = Lot.entityManager();
        TypedQuery<Long> q = em.createQuery("SELECT COUNT(l) FROM Lot l, SaltForm sf WHERE l.saltForm = sf AND sf.parent = :parent AND l.isVirtual IS NOT :isVirtual", Long.class);
        q.setParameter("parent", parent);
        q.setParameter("isVirtual", true);
        return q.getSingleResult();
    }

    public static Long countLotsByVendor(Vendor vendor) {
        if (vendor == null) throw new IllegalArgumentException("The vendor argument is required");
        EntityManager em = Lot.entityManager();
        TypedQuery<Long> q = em.createQuery("SELECT COUNT(l) FROM Lot l WHERE l.vendor = :vendor ", Long.class);
        q.setParameter("vendor", vendor);
        return q.getSingleResult();
    }

    public static Long countLotsByRegisteredBy(Author registeredBy) {
        if (registeredBy == null) throw new IllegalArgumentException("The registeredBy argument is required");
        EntityManager em = Lot.entityManager();
        TypedQuery<Long> q = em.createQuery("SELECT COUNT(l) FROM Lot l WHERE l.registeredBy = :registeredBy ", Long.class);
        q.setParameter("registeredBy", registeredBy);
        return q.getSingleResult();
    }
    
    
    public static TypedQuery<Lot> findLotsByParent(Parent parent) {
        if (parent == null) throw new IllegalArgumentException("The parent argument is required");
        EntityManager em = Lot.entityManager();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Lot> criteria = criteriaBuilder.createQuery(Lot.class);
        Root<Lot> lotRoot = criteria.from(Lot.class);
        Join<Lot, SaltForm> lotSaltForm = lotRoot.join("saltForm");
        Join<SaltForm, Parent> saltFormParent = lotSaltForm.join("parent");
        criteria.select(lotRoot);
        Predicate predicate = criteriaBuilder.equal(saltFormParent, parent);
        criteria.where(criteriaBuilder.and(predicate));
        criteria.orderBy(criteriaBuilder.desc(lotRoot.get("lotNumber")));
        TypedQuery<Lot> q = em.createQuery(criteria);
        return q;
    }
    

    public static TypedQuery<Lot> findFirstLotByParent(Parent parent) {
        if (parent == null) throw new IllegalArgumentException("The parent argument is required");
        EntityManager em = Lot.entityManager();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Lot> criteria = criteriaBuilder.createQuery(Lot.class);
        Root<Lot> lotRoot = criteria.from(Lot.class);
        Join<Lot, SaltForm> lotSaltForm = lotRoot.join("saltForm");
        Join<SaltForm, Parent> saltFormParent = lotSaltForm.join("parent");
        criteria.select(lotRoot);
        Predicate predicate = criteriaBuilder.equal(saltFormParent, parent);
        criteria.where(criteriaBuilder.and(predicate));
        TypedQuery<Lot> q = em.createQuery(criteria);
        return q;
    }
    
    public static TypedQuery<Lot> findLotByParentAndLotNumber(Parent parent, int lotNumber) {
        if (parent == null) throw new IllegalArgumentException("The parent argument is required");
        EntityManager em = Lot.entityManager();
        TypedQuery<Lot> q = em.createQuery("SELECT o FROM Lot AS o WHERE o.saltForm.parent = :parent AND o.lotNumber = :lotNumber ORDER BY o.lotNumber desc", Lot.class);
        q.setParameter("parent", parent);
        q.setParameter("lotNumber", lotNumber);
        return q;
    }

    public static TypedQuery<Lot> findLotByParentAndLowestLotNumber(Parent parent) {
        if (parent == null) throw new IllegalArgumentException("The parent argument is required");
        EntityManager em = Lot.entityManager();
        TypedQuery<Lot> q = em.createQuery("SELECT o FROM Lot AS o WHERE o.saltForm.parent = :parent AND (o.ignore IS NULL OR o.ignore IS :ignore) AND o.lotNumber = (select min(l.lotNumber) FROM Lot AS l WHERE l.saltForm.parent = :parent AND (l.ignore IS NULL OR l.ignore IS :ignore ))", Lot.class);
        q.setParameter("parent", parent);
        q.setParameter("ignore", false);
        return q;
    }
    public static TypedQuery<Lot> findLotsBySaltForm(SaltForm saltForm) {
        if (saltForm == null) throw new IllegalArgumentException("The saltForm argument is required");
        EntityManager em = Lot.entityManager();
        TypedQuery<Lot> q = em.createQuery("SELECT o FROM Lot AS o WHERE o.saltForm = :saltForm ORDER BY ID desc", Lot.class);
        q.setParameter("saltForm", saltForm);
        return q;
    }

    public static TypedQuery<Lot> findLotsBySaltFormAndMeta(SaltForm saltForm, SearchFormDTO searchParams) {
        if (saltForm == null) throw new IllegalArgumentException("The saltForm argument is required");
        if (searchParams == null) throw new IllegalArgumentException("The searchParams argument is required");
        EntityManager em = Lot.entityManager();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Lot> criteria = criteriaBuilder.createQuery(Lot.class);
        Root<Lot> lotRoot = criteria.from(Lot.class);
        Join<Lot, SaltForm> lotSaltForm = lotRoot.join("saltForm");
        Join<SaltForm, Parent> saltFormParent = lotSaltForm.join("parent");
		Join<Parent, ParentAlias> parentAliases = saltFormParent.join("parentAliases", JoinType.LEFT);

        criteria.select(lotRoot);
//        criteria.distinct(true);
        ParameterExpression<Date> firstDate = criteriaBuilder.parameter(Date.class);
        ParameterExpression<Date> lastDate = criteriaBuilder.parameter(Date.class);
        Predicate[] predicates = new Predicate[0];
        List<Predicate> predicateList = new ArrayList<Predicate>();
        Predicate saltFormPredicate = criteriaBuilder.equal(lotRoot.get("saltForm"), saltForm);
        predicateList.add(saltFormPredicate);
        if (searchParams.getChemist() != null) {
            logger.debug("incoming chemist :" + searchParams.getChemist().toString());
            Predicate predicate = criteriaBuilder.equal(lotRoot.<Author>get("chemist"), searchParams.getChemist());
            predicateList.add(predicate);
        }
        if (searchParams.getBuidNumber() != null) {
            logger.debug("incoming buid number :" + searchParams.getBuidNumber());
            Predicate predicate = criteriaBuilder.equal(lotRoot.get("buid"), searchParams.getBuidNumber());
            predicateList.add(predicate);
        }
        if (searchParams.getMinSynthDate() != null) {
            logger.debug("incoming minSynthesisDate :" + searchParams.getDateFrom());
            Predicate predicate = criteriaBuilder.greaterThanOrEqualTo(lotRoot.<Date>get("synthesisDate"), firstDate);
            predicateList.add(predicate);
        }
        if (searchParams.getMaxSynthDate() != null) {
            logger.debug("incoming maxSynthesisDate :" + searchParams.getDateTo());
            Predicate predicate = criteriaBuilder.lessThanOrEqualTo(lotRoot.<Date>get("synthesisDate"), lastDate);
            predicateList.add(predicate);
        }
        if (searchParams.getLotCorpName() != null) {
            logger.debug("incoming lotCorpName :" + searchParams.getLotCorpName());
            Predicate predicate = criteriaBuilder.equal(lotRoot.get("corpName"), searchParams.getLotCorpName());
            predicateList.add(predicate);
        }
        if (searchParams.getSaltFormCorpName() != null) {
            logger.debug("incoming saltFormCorpName :" + searchParams.getSaltFormCorpName());
            Predicate predicate = criteriaBuilder.equal(lotSaltForm.get("corpName"), searchParams.getSaltFormCorpName());
            predicateList.add(predicate);
        }
        if (searchParams.getParentCorpName() != null) {
            logger.debug("incoming parentCorpName :" + searchParams.getParentCorpName());
            Predicate predicate = criteriaBuilder.equal(saltFormParent.get("corpName"), searchParams.getParentCorpName());
            predicateList.add(predicate);
        }
        if (searchParams.getAlias() != null && !searchParams.getAlias().equals("")) {
            logger.debug("incoming AliasContSelect :" + searchParams.getAliasContSelect());
            Predicate predicate = null;
            String aliasName = searchParams.getAlias().trim().toUpperCase();
            logger.debug("incoming alias name :" + searchParams.getAlias());
            if (searchParams.getAliasContSelect().equalsIgnoreCase("exact")) {
            	Predicate notIgnored = criteriaBuilder.not(parentAliases.<Boolean>get("ignored"));
                Predicate nameEquals = criteriaBuilder.equal(criteriaBuilder.upper(parentAliases.<String>get("aliasName")), aliasName);
                predicate = criteriaBuilder.and(notIgnored, nameEquals);
            } else {
                aliasName = "%" + aliasName + "%";
                logger.debug("looking for a like match on the alias   " + aliasName);
                Predicate notIgnored = criteriaBuilder.not(parentAliases.<Boolean>get("ignored"));
                Predicate nameLike = criteriaBuilder.like(criteriaBuilder.upper(parentAliases.<String>get("aliasName")), aliasName);
                predicate = criteriaBuilder.and(notIgnored, nameLike);
            }
            predicateList.add(predicate);
        }
        predicates = predicateList.toArray(predicates);
        criteria.where(criteriaBuilder.and(predicates));
        criteria.orderBy(criteriaBuilder.desc(lotRoot.get("lotNumber")));
        TypedQuery<Lot> q = em.createQuery(criteria);
        if (searchParams.getMaxSynthDate() != null) {
            q.setParameter(lastDate, searchParams.getMaxSynthDate(), TemporalType.DATE);
        }
        if (searchParams.getMinSynthDate() != null) {
            q.setParameter(firstDate, searchParams.getMinSynthDate(), TemporalType.DATE);
        }
        
        if (searchParams.getMaxResults() != null) q.setMaxResults(searchParams.getMaxResults());
        
        return q;
    }
    
    public String toJsonIncludeAliases() {
        return new JSONSerializer()
        		.include("lotAliases")
        .exclude("*.class").serialize(this);
    }

    
}
