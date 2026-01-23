package com.labsynch.labseer.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.Version;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.labsynch.labseer.dto.LotsByProjectDTO;
import com.labsynch.labseer.dto.SearchFormDTO;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Entity
@Configurable
@Transactional
public class Lot {

    private static final Logger logger = LoggerFactory.getLogger(Lot.class);

    @Column(columnDefinition = "text")
    private String asDrawnStruct;

    private int lotAsDrawnCdId;

    private Long buid;

    @Column(unique = true)
    @NotNull
    @Size(max = 255)
    private String corpName;

    private Integer lotNumber;

    private Double lotMolWeight;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    private Date synthesisDate;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    private Date registrationDate;

    @Column(columnDefinition = "character varying")
    private String registeredBy;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    private Date modifiedDate;

    @Column(columnDefinition = "character varying")
    private String modifiedBy;

    @Size(max = 255)
    private String barcode;

    @Size(max = 255)
    private String color;

    @Size(max = 255)
    private String notebookPage;

    private Double amount;

    @ManyToOne
    @JoinColumn(name = "amount_units")
    private Unit amountUnits;

    private Double solutionAmount;

    @ManyToOne
    @JoinColumn(name = "solution_amount_units")
    private SolutionUnit solutionAmountUnits;

    @Size(max = 255)
    private String supplier;

    @Size(max = 255)
    private String supplierID;

    private Double purity;

    @ManyToOne
    @JoinColumn(name = "purity_operator")
    private Operator purityOperator;

    @ManyToOne
    @JoinColumn(name = "purity_measured_by")
    private PurityMeasuredBy purityMeasuredBy;

    @Column(columnDefinition = "character varying")
    private String chemist;

    private Double percentEE;

    @Column(columnDefinition = "text")
    private String comments;

    private Boolean isVirtual;

    private Boolean ignore;

    @ManyToOne
    @JoinColumn(name = "physical_state")
    private PhysicalState physicalState;

    @ManyToOne
    @JoinColumn(name = "vendor")
    private Vendor vendor;

    @Size(max = 255)
    @Column(name = "vendorid")
    private String vendorID;

    @ManyToOne
    @JoinColumn(name = "salt_form")
    private SaltForm saltForm;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "lot", fetch = FetchType.LAZY)
    private Set<FileList> fileLists = new HashSet<FileList>();

    private Double retain;

    @ManyToOne
    @JoinColumn(name = "retain_units")
    private Unit retainUnits;

    private String retainLocation;

    private Double meltingPoint;

    private Double boilingPoint;

    @Size(max = 255)
    private String supplierLot;

    @Column(columnDefinition = "character varying")
    private String project;

    @Transient
    private Parent parent;

    @ManyToOne
    @JoinColumn(name = "bulk_load_file")
    private BulkLoadFile bulkLoadFile;

    private Double lambda;

    private Double absorbance;

    private String stockSolvent;

    private String stockLocation;

    private Double observedMassOne;

    private Double observedMassTwo;

    private Double tareWeight;

    @ManyToOne
    @JoinColumn(name = "tare_weight_units")
    private Unit tareWeightUnits;

    private Double totalAmountStored;

    @ManyToOne
    @JoinColumn(name = "total_amount_stored_units")
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
        return Lot.entityManager().createQuery("SELECT o FROM Lot o", Lot.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public static Lot findLot(Long id) {
        if (id == null)
            return null;
        return Lot.entityManager().find(Lot.class, id);
    }

    public static TypedQuery<Lot> findLotsByBuidNumber(Long buidNumber) {
        if (buidNumber == null)
            throw new IllegalArgumentException("The buidNumber argument is required");
        EntityManager em = Lot.entityManager();
        TypedQuery<Lot> q = em.createQuery("SELECT o FROM Lot AS o WHERE o.buid = :buidNumber AND o.ignore IS null",
                Lot.class);
        q.setParameter("buidNumber", buidNumber);
        return q;
    }

    public static TypedQuery<Lot> findLotsByNotebookPageEquals(String notebookPage) {
        if (notebookPage == null || notebookPage.length() == 0)
            throw new IllegalArgumentException("The notebookPage argument is required");
        EntityManager em = Lot.entityManager();
        // boolean ignore = true;
        TypedQuery<Lot> q = em.createQuery(
                "SELECT o FROM Lot AS o WHERE o.notebookPage = :notebookPage AND o.ignore IS null", Lot.class);
        q.setParameter("notebookPage", notebookPage);
        // q.setParameter("ignore", ignore);
        return q;
    }

    public static TypedQuery<Lot> findLotsByDate(Date synthesisDate) {
        if (synthesisDate == null)
            throw new IllegalArgumentException("The synthesisDate argument is required");
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
        if (saltForm == null)
            throw new IllegalArgumentException("The saltForm argument is required");
        if (isVirtual == null)
            throw new IllegalArgumentException("The isVirtual argument is required");
        EntityManager em = Lot.entityManager();
        TypedQuery<Long> q = em.createQuery(
                "SELECT COUNT(o) FROM Lot o WHERE o.saltForm = :saltForm AND o.isVirtual IS NOT :isVirtual",
                Long.class);
        q.setParameter("saltForm", saltForm);
        q.setParameter("isVirtual", isVirtual);
        return q.getSingleResult();
    }

    public static Integer getMaxSaltFormLotNumber(SaltForm saltForm) {
        if (saltForm == null)
            throw new IllegalArgumentException("The saltForm argument is required");
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

    public static Integer getMaxParentLotNumber(Parent parent, Integer maxAutoLotNumber) {
        if (parent == null)
            throw new IllegalArgumentException("The parent argument is required");
        logger.debug("get max parent lot number for parent " + parent.getCorpName());
        EntityManager em = Lot.entityManager();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Integer> criteria = criteriaBuilder.createQuery(Integer.class);
        Root<Lot> lotRoot = criteria.from(Lot.class);
        Join<Lot, SaltForm> lotSaltForm = lotRoot.join("saltForm");
        Join<SaltForm, Parent> saltFormParent = lotSaltForm.join("parent");
        Predicate predicate = criteriaBuilder.equal(saltFormParent, parent);
        criteria.select(criteriaBuilder.max(lotRoot.<Integer>get("lotNumber")));
        List<Predicate> predicateList = new ArrayList<Predicate>();
        predicateList.add(predicate);

        // If max auto lot number is passed in
        // We don't want to use any lot number that is greater than the max auto lot number
        // so add it to the where clause list
        if(maxAutoLotNumber != null && maxAutoLotNumber > 0) {
            Predicate maxAutoLotPredicate = criteriaBuilder.lessThanOrEqualTo(lotRoot.<Integer>get("lotNumber"), maxAutoLotNumber);
            predicateList.add(maxAutoLotPredicate);
        }
		criteria.where(criteriaBuilder.and(predicateList.toArray( new Predicate[0])));
        TypedQuery<Integer> q = em.createQuery(criteria);
        Integer result = q.getSingleResult();
        if(result == null) {
            result = 0;
        }
        return result;
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
        if (parent == null)
            throw new IllegalArgumentException("The parent argument is required");
        EntityManager em = Lot.entityManager();
        TypedQuery<Long> q = em.createQuery(
                "SELECT COUNT(l) FROM Lot l, SaltForm sf WHERE l.saltForm = sf AND sf.parent = :parent AND l.isVirtual IS NOT :isVirtual",
                Long.class);
        q.setParameter("parent", parent);
        q.setParameter("isVirtual", true);
        return q.getSingleResult();
    }

    public static Long countLotsByVendor(Vendor vendor) {
        if (vendor == null)
            throw new IllegalArgumentException("The vendor argument is required");
        EntityManager em = Lot.entityManager();
        TypedQuery<Long> q = em.createQuery("SELECT COUNT(l) FROM Lot l WHERE l.vendor = :vendor ", Long.class);
        q.setParameter("vendor", vendor);
        return q.getSingleResult();
    }

    public static Long countLotsByRegisteredBy(Author registeredBy) {
        if (registeredBy == null)
            throw new IllegalArgumentException("The registeredBy argument is required");
        EntityManager em = Lot.entityManager();
        TypedQuery<Long> q = em.createQuery("SELECT COUNT(l) FROM Lot l WHERE l.registeredBy = :registeredBy ",
                Long.class);
        q.setParameter("registeredBy", registeredBy);
        return q.getSingleResult();
    }

    public static TypedQuery<Lot> findLotsByParent(Parent parent) {
        if (parent == null)
            throw new IllegalArgumentException("The parent argument is required");
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
        if (parent == null)
            throw new IllegalArgumentException("The parent argument is required");
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
        if (parent == null)
            throw new IllegalArgumentException("The parent argument is required");
        EntityManager em = Lot.entityManager();
        TypedQuery<Lot> q = em.createQuery(
                "SELECT o FROM Lot AS o WHERE o.saltForm.parent = :parent AND o.lotNumber = :lotNumber ORDER BY o.lotNumber desc",
                Lot.class);
        q.setParameter("parent", parent);
        q.setParameter("lotNumber", lotNumber);
        return q;
    }

    public static TypedQuery<Lot> findLotByParentAndLowestLotNumber(Parent parent) {
        if (parent == null)
            throw new IllegalArgumentException("The parent argument is required");
        EntityManager em = Lot.entityManager();
        TypedQuery<Lot> q = em.createQuery(
                "SELECT o FROM Lot AS o WHERE o.saltForm.parent = :parent AND (o.ignore IS NULL OR o.ignore = :ignore) AND o.lotNumber = (select min(l.lotNumber) FROM Lot AS l WHERE l.saltForm.parent = :parent AND (l.ignore IS NULL OR l.ignore = :ignore ))",
                Lot.class);
        q.setParameter("parent", parent);
        q.setParameter("ignore", false);
        return q;
    }

    public static TypedQuery<Lot> findLotsBySaltForm(SaltForm saltForm) {
        if (saltForm == null)
            throw new IllegalArgumentException("The saltForm argument is required");
        EntityManager em = Lot.entityManager();
        TypedQuery<Lot> q = em.createQuery("SELECT o FROM Lot AS o WHERE o.saltForm = :saltForm ORDER BY ID desc",
                Lot.class);
        q.setParameter("saltForm", saltForm);
        return q;
    }

    public static TypedQuery<Lot> findLotsBySaltFormAndMeta(SaltForm saltForm, SearchFormDTO searchParams) {
        if (saltForm == null)
            throw new IllegalArgumentException("The saltForm argument is required");
        if (searchParams == null)
            throw new IllegalArgumentException("The searchParams argument is required");
        EntityManager em = Lot.entityManager();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Lot> criteria = criteriaBuilder.createQuery(Lot.class);
        Root<Lot> lotRoot = criteria.from(Lot.class);
        Join<Lot, SaltForm> lotSaltForm = lotRoot.join("saltForm");
        Join<SaltForm, Parent> saltFormParent = lotSaltForm.join("parent");
        Join<Parent, ParentAlias> parentAliases = saltFormParent.join("parentAliases", JoinType.LEFT);

        criteria.select(lotRoot);
        // criteria.distinct(true);
        ParameterExpression<Date> firstDate = criteriaBuilder.parameter(Date.class);
        ParameterExpression<Date> lastDate = criteriaBuilder.parameter(Date.class);
        Predicate[] predicates = new Predicate[0];
        List<Predicate> predicateList = new ArrayList<Predicate>();
        Predicate saltFormPredicate = criteriaBuilder.equal(lotRoot.get("saltForm"), saltForm);
        predicateList.add(saltFormPredicate);
        if (searchParams.getChemist() != null && !searchParams.getChemist().equals("anyone")) {
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
            Predicate predicate = criteriaBuilder.equal(lotSaltForm.get("corpName"),
                    searchParams.getSaltFormCorpName());
            predicateList.add(predicate);
        }
        if (searchParams.getParentCorpName() != null) {
            logger.debug("incoming parentCorpName :" + searchParams.getParentCorpName());
            Predicate predicate = criteriaBuilder.equal(saltFormParent.get("corpName"),
                    searchParams.getParentCorpName());
            predicateList.add(predicate);
        }
        if (searchParams.getAlias() != null && !searchParams.getAlias().equals("")) {
            logger.debug("incoming AliasContSelect :" + searchParams.getAliasContSelect());
            Predicate predicate = null;
            String aliasName = searchParams.getAlias().trim().toUpperCase();
            logger.debug("incoming alias name :" + searchParams.getAlias());
            if (searchParams.getAliasContSelect().equalsIgnoreCase("exact")) {
                Predicate notIgnored = criteriaBuilder.not(parentAliases.<Boolean>get("ignored"));
                Predicate nameEquals = criteriaBuilder
                        .equal(criteriaBuilder.upper(parentAliases.<String>get("aliasName")), aliasName);
                predicate = criteriaBuilder.and(notIgnored, nameEquals);
            } else {
                aliasName = "%" + aliasName + "%";
                logger.debug("looking for a like match on the alias   " + aliasName);
                Predicate notIgnored = criteriaBuilder.not(parentAliases.<Boolean>get("ignored"));
                Predicate nameLike = criteriaBuilder.like(criteriaBuilder.upper(parentAliases.<String>get("aliasName")),
                        aliasName);
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

        if (searchParams.getMaxResults() != null)
            q.setMaxResults(searchParams.getMaxResults());

        return q;
    }

    public static TypedQuery<LotsByProjectDTO> findLotsByProjectsList(List<String> projects) {

        EntityManager em = Lot.entityManager();
        String query = "SELECT new com.labsynch.labseer.dto.LotsByProjectDTO( "
                + "lt.id as id, lt.corpName as lotCorpName, lt.lotNumber as lotNumber, lt.registrationDate as registrationDate, prnt.corpName as parentCorpName, lt.project as project, lt.chemist as chemist, vnd.name as vendorName, vnd.code as vendorCode, lt.supplier as supplier) "
                + "FROM Lot AS lt "
                + "JOIN lt.saltForm sltfrm "
                + "JOIN sltfrm.parent prnt "
                + "LEFT JOIN lt.vendor vnd "
                + "WHERE lt.project in (:projects) ";

        logger.debug("sql query " + query);
        TypedQuery<LotsByProjectDTO> q = em.createQuery(query, LotsByProjectDTO.class);
        q.setParameter("projects", projects);

        return q;
    }

    public String toJsonIncludeAliases() {
        return new JSONSerializer()
                .include("lotAliases")
                .exclude("*.class").serialize(this);
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

    public String getAsDrawnStruct() {
        return this.asDrawnStruct;
    }

    public void setAsDrawnStruct(String asDrawnStruct) {
        this.asDrawnStruct = asDrawnStruct;
    }

    public int getLotAsDrawnCdId() {
        return this.lotAsDrawnCdId;
    }

    public void setLotAsDrawnCdId(int lotAsDrawnCdId) {
        this.lotAsDrawnCdId = lotAsDrawnCdId;
    }

    public void setBuid(Long buid) {
        this.buid = buid;
    }

    public String getCorpName() {
        return this.corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public Integer getLotNumber() {
        return this.lotNumber;
    }

    public void setLotNumber(Integer lotNumber) {
        this.lotNumber = lotNumber;
    }

    public Double getLotMolWeight() {
        return this.lotMolWeight;
    }

    public void setLotMolWeight(Double lotMolWeight) {
        this.lotMolWeight = lotMolWeight;
    }

    public Date getSynthesisDate() {
        return this.synthesisDate;
    }

    public void setSynthesisDate(Date synthesisDate) {
        this.synthesisDate = synthesisDate;
    }

    public Date getRegistrationDate() {
        return this.registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getRegisteredBy() {
        return this.registeredBy;
    }

    public void setRegisteredBy(String registeredBy) {
        this.registeredBy = registeredBy;
    }

    public Date getModifiedDate() {
        return this.modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getModifiedBy() {
        return this.modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getBarcode() {
        return this.barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getNotebookPage() {
        return this.notebookPage;
    }

    public void setNotebookPage(String notebookPage) {
        this.notebookPage = notebookPage;
    }

    public Double getAmount() {
        return this.amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Unit getAmountUnits() {
        return this.amountUnits;
    }

    public void setAmountUnits(Unit amountUnits) {
        this.amountUnits = amountUnits;
    }

    public Double getSolutionAmount() {
        return this.solutionAmount;
    }

    public void setSolutionAmount(Double solutionAmount) {
        this.solutionAmount = solutionAmount;
    }

    public SolutionUnit getSolutionAmountUnits() {
        return this.solutionAmountUnits;
    }

    public void setSolutionAmountUnits(SolutionUnit solutionAmountUnits) {
        this.solutionAmountUnits = solutionAmountUnits;
    }

    public String getSupplier() {
        return this.supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getSupplierID() {
        return this.supplierID;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    public Double getPurity() {
        return this.purity;
    }

    public void setPurity(Double purity) {
        this.purity = purity;
    }

    public Operator getPurityOperator() {
        return this.purityOperator;
    }

    public void setPurityOperator(Operator purityOperator) {
        this.purityOperator = purityOperator;
    }

    public PurityMeasuredBy getPurityMeasuredBy() {
        return this.purityMeasuredBy;
    }

    public void setPurityMeasuredBy(PurityMeasuredBy purityMeasuredBy) {
        this.purityMeasuredBy = purityMeasuredBy;
    }

    public String getChemist() {
        return this.chemist;
    }

    public void setChemist(String chemist) {
        this.chemist = chemist;
    }

    public Double getPercentEE() {
        return this.percentEE;
    }

    public void setPercentEE(Double percentEE) {
        this.percentEE = percentEE;
    }

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Boolean getIsVirtual() {
        return this.isVirtual;
    }

    public void setIsVirtual(Boolean isVirtual) {
        this.isVirtual = isVirtual;
    }

    public Boolean getIgnore() {
        return this.ignore;
    }

    public void setIgnore(Boolean ignore) {
        this.ignore = ignore;
    }

    public PhysicalState getPhysicalState() {
        return this.physicalState;
    }

    public void setPhysicalState(PhysicalState physicalState) {
        this.physicalState = physicalState;
    }

    public Vendor getVendor() {
        return this.vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public String getVendorID() {
        return this.vendorID;
    }

    public void setVendorID(String vendorID) {
        this.vendorID = vendorID;
    }

    public Set<FileList> getFileLists() {
        return this.fileLists;
    }

    public void setFileLists(Set<FileList> fileLists) {
        this.fileLists = fileLists;
    }

    public Double getRetain() {
        return this.retain;
    }

    public void setRetain(Double retain) {
        this.retain = retain;
    }

    public Unit getRetainUnits() {
        return this.retainUnits;
    }

    public void setRetainUnits(Unit retainUnits) {
        this.retainUnits = retainUnits;
    }

    public String getRetainLocation() {
        return this.retainLocation;
    }

    public void setRetainLocation(String retainLocation) {
        this.retainLocation = retainLocation;
    }

    public Double getMeltingPoint() {
        return this.meltingPoint;
    }

    public void setMeltingPoint(Double meltingPoint) {
        this.meltingPoint = meltingPoint;
    }

    public Double getBoilingPoint() {
        return this.boilingPoint;
    }

    public void setBoilingPoint(Double boilingPoint) {
        this.boilingPoint = boilingPoint;
    }

    public String getSupplierLot() {
        return this.supplierLot;
    }

    public void setSupplierLot(String supplierLot) {
        this.supplierLot = supplierLot;
    }

    public String getProject() {
        return this.project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public BulkLoadFile getBulkLoadFile() {
        return this.bulkLoadFile;
    }

    public void setBulkLoadFile(BulkLoadFile bulkLoadFile) {
        this.bulkLoadFile = bulkLoadFile;
    }

    public Double getLambda() {
        return this.lambda;
    }

    public void setLambda(Double lambda) {
        this.lambda = lambda;
    }

    public Double getAbsorbance() {
        return this.absorbance;
    }

    public void setAbsorbance(Double absorbance) {
        this.absorbance = absorbance;
    }

    public String getStockSolvent() {
        return this.stockSolvent;
    }

    public void setStockSolvent(String stockSolvent) {
        this.stockSolvent = stockSolvent;
    }

    public String getStockLocation() {
        return this.stockLocation;
    }

    public void setStockLocation(String stockLocation) {
        this.stockLocation = stockLocation;
    }

    public Double getObservedMassOne() {
        return this.observedMassOne;
    }

    public void setObservedMassOne(Double observedMassOne) {
        this.observedMassOne = observedMassOne;
    }

    public Double getObservedMassTwo() {
        return this.observedMassTwo;
    }

    public void setObservedMassTwo(Double observedMassTwo) {
        this.observedMassTwo = observedMassTwo;
    }

    public Double getTareWeight() {
        return this.tareWeight;
    }

    public void setTareWeight(Double tareWeight) {
        this.tareWeight = tareWeight;
    }

    public Unit getTareWeightUnits() {
        return this.tareWeightUnits;
    }

    public void setTareWeightUnits(Unit tareWeightUnits) {
        this.tareWeightUnits = tareWeightUnits;
    }

    public Double getTotalAmountStored() {
        return this.totalAmountStored;
    }

    public void setTotalAmountStored(Double totalAmountStored) {
        this.totalAmountStored = totalAmountStored;
    }

    public Unit getTotalAmountStoredUnits() {
        return this.totalAmountStoredUnits;
    }

    public void setTotalAmountStoredUnits(Unit totalAmountStoredUnits) {
        this.totalAmountStoredUnits = totalAmountStoredUnits;
    }

    public Set<LotAlias> getLotAliases() {
        return this.lotAliases;
    }

    public void setLotAliases(Set<LotAlias> lotAliases) {
        this.lotAliases = lotAliases;
    }

    @PersistenceContext
    transient EntityManager entityManager;

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "asDrawnStruct",
            "lotAsDrawnCdId", "buid", "corpName", "lotNumber", "lotMolWeight", "synthesisDate", "registrationDate",
            "registeredBy", "modifiedDate", "modifiedBy", "barcode", "color", "notebookPage", "amount", "amountUnits",
            "solutionAmount", "solutionAmountUnits", "supplier", "supplierID", "purity", "purityOperator",
            "purityMeasuredBy", "chemist", "percentEE", "comments", "isVirtual", "ignore", "physicalState", "vendor",
            "vendorID", "saltForm", "fileLists", "retain", "retainUnits", "retainLocation", "meltingPoint",
            "boilingPoint", "supplierLot", "project", "parent", "bulkLoadFile", "lambda", "absorbance", "stockSolvent",
            "stockLocation", "observedMassOne", "observedMassTwo", "tareWeight", "tareWeightUnits", "totalAmountStored",
            "totalAmountStoredUnits", "lotAliases", "storageLocation");

    public static final EntityManager entityManager() {
        EntityManager em = new Lot().entityManager;
        if (em == null)
            throw new IllegalStateException(
                    "Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countLots() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Lot o", Long.class).getSingleResult();
    }

    public static List<Lot> findAllLots(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Lot o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Lot.class).getResultList();
    }

    public static List<Lot> findLotEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Lot o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Lot.class).setFirstResult(firstResult).setMaxResults(maxResults)
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
            Lot attached = Lot.findLot(this.id);
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
    public Lot merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        Lot merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    public static Long countFindLotsByBuid(Long buid) {
        if (buid == null)
            throw new IllegalArgumentException("The buid argument is required");
        EntityManager em = Lot.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Lot AS o WHERE o.buid = :buid", Long.class);
        q.setParameter("buid", buid);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindLotsByBulkLoadFileEquals(BulkLoadFile bulkLoadFile) {
        if (bulkLoadFile == null)
            throw new IllegalArgumentException("The bulkLoadFile argument is required");
        EntityManager em = Lot.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Lot AS o WHERE o.bulkLoadFile = :bulkLoadFile", Long.class);
        q.setParameter("bulkLoadFile", bulkLoadFile);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindLotsByChemistAndSynthesisDateBetween(String chemist, Date minSynthesisDate,
            Date maxSynthesisDate) {
        if (chemist == null || chemist.length() == 0)
            throw new IllegalArgumentException("The chemist argument is required");
        if (minSynthesisDate == null)
            throw new IllegalArgumentException("The minSynthesisDate argument is required");
        if (maxSynthesisDate == null)
            throw new IllegalArgumentException("The maxSynthesisDate argument is required");
        EntityManager em = Lot.entityManager();
        TypedQuery q = em.createQuery(
                "SELECT COUNT(o) FROM Lot AS o WHERE o.chemist = :chemist AND o.synthesisDate BETWEEN :minSynthesisDate AND :maxSynthesisDate",
                Long.class);
        q.setParameter("chemist", chemist);
        q.setParameter("minSynthesisDate", minSynthesisDate);
        q.setParameter("maxSynthesisDate", maxSynthesisDate);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindLotsByCorpNameEquals(String corpName) {
        if (corpName == null || corpName.length() == 0)
            throw new IllegalArgumentException("The corpName argument is required");
        EntityManager em = Lot.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Lot AS o WHERE o.corpName = :corpName", Long.class);
        q.setParameter("corpName", corpName);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindLotsByCorpNameLike(String corpName) {
        if (corpName == null || corpName.length() == 0)
            throw new IllegalArgumentException("The corpName argument is required");
        corpName = corpName.replace('*', '%');
        if (corpName.charAt(0) != '%') {
            corpName = "%" + corpName;
        }
        if (corpName.charAt(corpName.length() - 1) != '%') {
            corpName = corpName + "%";
        }
        EntityManager em = Lot.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Lot AS o WHERE LOWER(o.corpName) LIKE LOWER(:corpName)",
                Long.class);
        q.setParameter("corpName", corpName);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindLotsByIsVirtualNot(Boolean isVirtual) {
        if (isVirtual == null)
            throw new IllegalArgumentException("The isVirtual argument is required");
        EntityManager em = Lot.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Lot AS o WHERE o.isVirtual IS NOT :isVirtual", Long.class);
        q.setParameter("isVirtual", isVirtual);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindLotsByNotebookPageEquals(String notebookPage) {
        if (notebookPage == null || notebookPage.length() == 0)
            throw new IllegalArgumentException("The notebookPage argument is required");
        EntityManager em = Lot.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Lot AS o WHERE o.notebookPage = :notebookPage", Long.class);
        q.setParameter("notebookPage", notebookPage);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindLotsByNotebookPageEqualsAndIgnoreNot(String notebookPage, Boolean ignore) {
        if (notebookPage == null || notebookPage.length() == 0)
            throw new IllegalArgumentException("The notebookPage argument is required");
        if (ignore == null)
            throw new IllegalArgumentException("The ignore argument is required");
        EntityManager em = Lot.entityManager();
        TypedQuery q = em.createQuery(
                "SELECT COUNT(o) FROM Lot AS o WHERE o.notebookPage = :notebookPage  AND (o.ignore IS NULL OR o.ignore != :ignore)",
                Long.class);
        q.setParameter("notebookPage", notebookPage);
        q.setParameter("ignore", ignore);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindLotsBySaltForm(SaltForm saltForm) {
        if (saltForm == null)
            throw new IllegalArgumentException("The saltForm argument is required");
        EntityManager em = Lot.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Lot AS o WHERE o.saltForm = :saltForm", Long.class);
        q.setParameter("saltForm", saltForm);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindLotsBySynthesisDateBetween(Date minSynthesisDate, Date maxSynthesisDate) {
        if (minSynthesisDate == null)
            throw new IllegalArgumentException("The minSynthesisDate argument is required");
        if (maxSynthesisDate == null)
            throw new IllegalArgumentException("The maxSynthesisDate argument is required");
        EntityManager em = Lot.entityManager();
        TypedQuery q = em.createQuery(
                "SELECT COUNT(o) FROM Lot AS o WHERE o.synthesisDate BETWEEN :minSynthesisDate AND :maxSynthesisDate",
                Long.class);
        q.setParameter("minSynthesisDate", minSynthesisDate);
        q.setParameter("maxSynthesisDate", maxSynthesisDate);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindLotsBySynthesisDateGreaterThan(Date synthesisDate) {
        if (synthesisDate == null)
            throw new IllegalArgumentException("The synthesisDate argument is required");
        EntityManager em = Lot.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Lot AS o WHERE o.synthesisDate > :synthesisDate",
                Long.class);
        q.setParameter("synthesisDate", synthesisDate);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindLotsBySynthesisDateLessThan(Date synthesisDate) {
        if (synthesisDate == null)
            throw new IllegalArgumentException("The synthesisDate argument is required");
        EntityManager em = Lot.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Lot AS o WHERE o.synthesisDate < :synthesisDate",
                Long.class);
        q.setParameter("synthesisDate", synthesisDate);
        return ((Long) q.getSingleResult());
    }

    public static TypedQuery<Lot> findLotsByBuid(Long buid) {
        if (buid == null)
            throw new IllegalArgumentException("The buid argument is required");
        EntityManager em = Lot.entityManager();
        TypedQuery<Lot> q = em.createQuery("SELECT o FROM Lot AS o WHERE o.buid = :buid", Lot.class);
        q.setParameter("buid", buid);
        return q;
    }

    public static TypedQuery<Lot> findLotsByBuid(Long buid, String sortFieldName, String sortOrder) {
        if (buid == null)
            throw new IllegalArgumentException("The buid argument is required");
        EntityManager em = Lot.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Lot AS o WHERE o.buid = :buid");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Lot> q = em.createQuery(queryBuilder.toString(), Lot.class);
        q.setParameter("buid", buid);
        return q;
    }

    public static TypedQuery<Lot> findLotsByBulkLoadFileEquals(BulkLoadFile bulkLoadFile) {
        if (bulkLoadFile == null)
            throw new IllegalArgumentException("The bulkLoadFile argument is required");
        EntityManager em = Lot.entityManager();
        TypedQuery<Lot> q = em.createQuery("SELECT o FROM Lot AS o WHERE o.bulkLoadFile = :bulkLoadFile", Lot.class);
        q.setParameter("bulkLoadFile", bulkLoadFile);
        return q;
    }

    public static TypedQuery<Lot> findLotsByCorpNameCollection(Collection<String> lotCorpNames) {
		EntityManager em = Lot.entityManager();
		List<String> batchCodes = new ArrayList<String>();
		batchCodes.addAll(lotCorpNames);

		String queryString = "Select lot "
        + "FROM Lot lot "
        + "JOIN FETCH lot.saltForm saltForm "
        + "JOIN FETCH saltForm.parent parent "
        + "JOIN FETCH parent.stereoCategory stereoCategory "
        + "LEFT JOIN FETCH lot.amountUnits amountUnits "
        + "LEFT JOIN FETCH lot.bulkLoadFile bulkLoadFile "
        + "LEFT JOIN FETCH lot.physicalState physicalState "
        + "LEFT JOIN FETCH lot.purityMeasuredBy purityMeasuredBy "
        + "LEFT JOIN FETCH lot.purityOperator purityOperator "
        + "LEFT JOIN FETCH lot.lotAliases lotAliases "
        + "LEFT JOIN FETCH saltForm.isoSalts isoSalts "
        + "LEFT JOIN FETCH lot.vendor vendor "
        + "LEFT JOIN FETCH parent.parentAliases parentAliases "
        + "LEFT JOIN FETCH parent.parentAnnotation parentAnnotation "
        + "LEFT JOIN FETCH parent.compoundType compoundType "
        + "WHERE lot.corpName IN (:batchCodes)";
		TypedQuery<Lot> q = em.createQuery(queryString, Lot.class)
				.setParameter("batchCodes", batchCodes);
		return q;
	}

    public static TypedQuery<Lot> findLotsByBulkLoadFileEquals(BulkLoadFile bulkLoadFile, String sortFieldName,
            String sortOrder) {
        if (bulkLoadFile == null)
            throw new IllegalArgumentException("The bulkLoadFile argument is required");
        EntityManager em = Lot.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Lot AS o WHERE o.bulkLoadFile = :bulkLoadFile");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Lot> q = em.createQuery(queryBuilder.toString(), Lot.class);
        q.setParameter("bulkLoadFile", bulkLoadFile);
        return q;
    }

    public static TypedQuery<Lot> findLotsByChemistAndSynthesisDateBetween(String chemist, Date minSynthesisDate,
            Date maxSynthesisDate) {
        if (chemist == null || chemist.length() == 0)
            throw new IllegalArgumentException("The chemist argument is required");
        if (minSynthesisDate == null)
            throw new IllegalArgumentException("The minSynthesisDate argument is required");
        if (maxSynthesisDate == null)
            throw new IllegalArgumentException("The maxSynthesisDate argument is required");
        EntityManager em = Lot.entityManager();
        TypedQuery<Lot> q = em.createQuery(
                "SELECT o FROM Lot AS o WHERE o.chemist = :chemist AND o.synthesisDate BETWEEN :minSynthesisDate AND :maxSynthesisDate",
                Lot.class);
        q.setParameter("chemist", chemist);
        q.setParameter("minSynthesisDate", minSynthesisDate);
        q.setParameter("maxSynthesisDate", maxSynthesisDate);
        return q;
    }

    public static TypedQuery<Lot> findLotsByChemistAndSynthesisDateBetween(String chemist, Date minSynthesisDate,
            Date maxSynthesisDate, String sortFieldName, String sortOrder) {
        if (chemist == null || chemist.length() == 0)
            throw new IllegalArgumentException("The chemist argument is required");
        if (minSynthesisDate == null)
            throw new IllegalArgumentException("The minSynthesisDate argument is required");
        if (maxSynthesisDate == null)
            throw new IllegalArgumentException("The maxSynthesisDate argument is required");
        EntityManager em = Lot.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM Lot AS o WHERE o.chemist = :chemist AND o.synthesisDate BETWEEN :minSynthesisDate AND :maxSynthesisDate");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Lot> q = em.createQuery(queryBuilder.toString(), Lot.class);
        q.setParameter("chemist", chemist);
        q.setParameter("minSynthesisDate", minSynthesisDate);
        q.setParameter("maxSynthesisDate", maxSynthesisDate);
        return q;
    }

    public static TypedQuery<Lot> findLotsByCorpNameEquals(String corpName) {
        if (corpName == null || corpName.length() == 0)
            throw new IllegalArgumentException("The corpName argument is required");
        EntityManager em = Lot.entityManager();
        TypedQuery<Lot> q = em.createQuery("SELECT o FROM Lot AS o WHERE o.corpName = :corpName", Lot.class);
        q.setParameter("corpName", corpName);
        return q;
    }

    public static TypedQuery<Lot> findLotsByCorpNameEquals(String corpName, String sortFieldName, String sortOrder) {
        if (corpName == null || corpName.length() == 0)
            throw new IllegalArgumentException("The corpName argument is required");
        EntityManager em = Lot.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Lot AS o WHERE o.corpName = :corpName");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Lot> q = em.createQuery(queryBuilder.toString(), Lot.class);
        q.setParameter("corpName", corpName);
        return q;
    }

    public static TypedQuery<Lot> findLotsByCorpNameLike(String corpName) {
        if (corpName == null || corpName.length() == 0)
            throw new IllegalArgumentException("The corpName argument is required");
        corpName = corpName.replace('*', '%');
        if (corpName.charAt(0) != '%') {
            corpName = "%" + corpName;
        }
        if (corpName.charAt(corpName.length() - 1) != '%') {
            corpName = corpName + "%";
        }
        EntityManager em = Lot.entityManager();
        TypedQuery<Lot> q = em.createQuery("SELECT o FROM Lot AS o WHERE LOWER(o.corpName) LIKE LOWER(:corpName)",
                Lot.class);
        q.setParameter("corpName", corpName);
        return q;
    }

    public static TypedQuery<Lot> findLotsByCorpNameLike(String corpName, String sortFieldName, String sortOrder) {
        if (corpName == null || corpName.length() == 0)
            throw new IllegalArgumentException("The corpName argument is required");
        corpName = corpName.replace('*', '%');
        if (corpName.charAt(0) != '%') {
            corpName = "%" + corpName;
        }
        if (corpName.charAt(corpName.length() - 1) != '%') {
            corpName = corpName + "%";
        }
        EntityManager em = Lot.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM Lot AS o WHERE LOWER(o.corpName) LIKE LOWER(:corpName)");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Lot> q = em.createQuery(queryBuilder.toString(), Lot.class);
        q.setParameter("corpName", corpName);
        return q;
    }

    public static TypedQuery<Lot> findLotsByIsVirtualNot(Boolean isVirtual) {
        if (isVirtual == null)
            throw new IllegalArgumentException("The isVirtual argument is required");
        EntityManager em = Lot.entityManager();
        TypedQuery<Lot> q = em.createQuery("SELECT o FROM Lot AS o WHERE o.isVirtual IS NOT :isVirtual", Lot.class);
        q.setParameter("isVirtual", isVirtual);
        return q;
    }

    public static TypedQuery<Lot> findLotsByIsVirtualNot(Boolean isVirtual, String sortFieldName, String sortOrder) {
        if (isVirtual == null)
            throw new IllegalArgumentException("The isVirtual argument is required");
        EntityManager em = Lot.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Lot AS o WHERE o.isVirtual IS NOT :isVirtual");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Lot> q = em.createQuery(queryBuilder.toString(), Lot.class);
        q.setParameter("isVirtual", isVirtual);
        return q;
    }

    public static TypedQuery<Lot> findLotsByNotebookPageEquals(String notebookPage, String sortFieldName,
            String sortOrder) {
        if (notebookPage == null || notebookPage.length() == 0)
            throw new IllegalArgumentException("The notebookPage argument is required");
        EntityManager em = Lot.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Lot AS o WHERE o.notebookPage = :notebookPage");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Lot> q = em.createQuery(queryBuilder.toString(), Lot.class);
        q.setParameter("notebookPage", notebookPage);
        return q;
    }

    public static TypedQuery<Lot> findLotsByNotebookPageEqualsAndIgnoreNot(String notebookPage, Boolean ignore) {
        if (notebookPage == null || notebookPage.length() == 0)
            throw new IllegalArgumentException("The notebookPage argument is required");
        if (ignore == null)
            throw new IllegalArgumentException("The ignore argument is required");
        EntityManager em = Lot.entityManager();
        TypedQuery<Lot> q = em.createQuery(
                "SELECT o FROM Lot AS o WHERE o.notebookPage = :notebookPage  AND (o.ignore IS NULL OR o.ignore != :ignore)", Lot.class);
        q.setParameter("notebookPage", notebookPage);
        q.setParameter("ignore", ignore);
        return q;
    }

    public static TypedQuery<Lot> findLotsByNotebookPageEqualsAndIgnoreNot(String notebookPage, Boolean ignore,
            String sortFieldName, String sortOrder) {
        if (notebookPage == null || notebookPage.length() == 0)
            throw new IllegalArgumentException("The notebookPage argument is required");
        if (ignore == null)
            throw new IllegalArgumentException("The ignore argument is required");
        EntityManager em = Lot.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM Lot AS o WHERE o.notebookPage = :notebookPage  AND (o.ignore IS NULL OR o.ignore != :ignore)");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Lot> q = em.createQuery(queryBuilder.toString(), Lot.class);
        q.setParameter("notebookPage", notebookPage);
        q.setParameter("ignore", ignore);
        return q;
    }

    public static TypedQuery<Lot> findLotsBySaltForm(SaltForm saltForm, String sortFieldName, String sortOrder) {
        if (saltForm == null)
            throw new IllegalArgumentException("The saltForm argument is required");
        EntityManager em = Lot.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Lot AS o WHERE o.saltForm = :saltForm");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Lot> q = em.createQuery(queryBuilder.toString(), Lot.class);
        q.setParameter("saltForm", saltForm);
        return q;
    }

    public static TypedQuery<Lot> findLotsBySynthesisDateBetween(Date minSynthesisDate, Date maxSynthesisDate) {
        if (minSynthesisDate == null)
            throw new IllegalArgumentException("The minSynthesisDate argument is required");
        if (maxSynthesisDate == null)
            throw new IllegalArgumentException("The maxSynthesisDate argument is required");
        EntityManager em = Lot.entityManager();
        TypedQuery<Lot> q = em.createQuery(
                "SELECT o FROM Lot AS o WHERE o.synthesisDate BETWEEN :minSynthesisDate AND :maxSynthesisDate",
                Lot.class);
        q.setParameter("minSynthesisDate", minSynthesisDate);
        q.setParameter("maxSynthesisDate", maxSynthesisDate);
        return q;
    }

    public static TypedQuery<Lot> findLotsBySynthesisDateBetween(Date minSynthesisDate, Date maxSynthesisDate,
            String sortFieldName, String sortOrder) {
        if (minSynthesisDate == null)
            throw new IllegalArgumentException("The minSynthesisDate argument is required");
        if (maxSynthesisDate == null)
            throw new IllegalArgumentException("The maxSynthesisDate argument is required");
        EntityManager em = Lot.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM Lot AS o WHERE o.synthesisDate BETWEEN :minSynthesisDate AND :maxSynthesisDate");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Lot> q = em.createQuery(queryBuilder.toString(), Lot.class);
        q.setParameter("minSynthesisDate", minSynthesisDate);
        q.setParameter("maxSynthesisDate", maxSynthesisDate);
        return q;
    }

    public static TypedQuery<Lot> findLotsBySynthesisDateGreaterThan(Date synthesisDate) {
        if (synthesisDate == null)
            throw new IllegalArgumentException("The synthesisDate argument is required");
        EntityManager em = Lot.entityManager();
        TypedQuery<Lot> q = em.createQuery("SELECT o FROM Lot AS o WHERE o.synthesisDate > :synthesisDate", Lot.class);
        q.setParameter("synthesisDate", synthesisDate);
        return q;
    }

    public static TypedQuery<Lot> findLotsBySynthesisDateGreaterThan(Date synthesisDate, String sortFieldName,
            String sortOrder) {
        if (synthesisDate == null)
            throw new IllegalArgumentException("The synthesisDate argument is required");
        EntityManager em = Lot.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Lot AS o WHERE o.synthesisDate > :synthesisDate");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Lot> q = em.createQuery(queryBuilder.toString(), Lot.class);
        q.setParameter("synthesisDate", synthesisDate);
        return q;
    }

    public static TypedQuery<Lot> findLotsBySynthesisDateLessThan(Date synthesisDate) {
        if (synthesisDate == null)
            throw new IllegalArgumentException("The synthesisDate argument is required");
        EntityManager em = Lot.entityManager();
        TypedQuery<Lot> q = em.createQuery("SELECT o FROM Lot AS o WHERE o.synthesisDate < :synthesisDate", Lot.class);
        q.setParameter("synthesisDate", synthesisDate);
        return q;
    }

    public static TypedQuery<Lot> findLotsBySynthesisDateLessThan(Date synthesisDate, String sortFieldName,
            String sortOrder) {
        if (synthesisDate == null)
            throw new IllegalArgumentException("The synthesisDate argument is required");
        EntityManager em = Lot.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Lot AS o WHERE o.synthesisDate < :synthesisDate");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Lot> q = em.createQuery(queryBuilder.toString(), Lot.class);
        q.setParameter("synthesisDate", synthesisDate);
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

    public static Lot fromJsonToLot(String json) {
        return new JSONDeserializer<Lot>()
                .use(null, Lot.class).deserialize(json);
    }

    public static String toJsonArray(Collection<Lot> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<Lot> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<Lot> fromJsonArrayToLots(String json) {
        return new JSONDeserializer<List<Lot>>()
                .use("values", Lot.class).deserialize(json);
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public static HashMap<Long, String> getOriginallyDrawnAsStructuresByParentIds(List<Long> parentIds) {

        Query q = entityManager().createNativeQuery("WITH AS_DRAWN_STRUCTS as ("
            + "select p.id as parent_id, coalesce(l.as_drawn_struct, p.mol_structure) as_drawn_struct, ROW_NUMBER () OVER (PARTITION BY p.id order by l.lot_number asc) as rn "
            + "from parent p "
            + "left join salt_form s "
            + "on p.id = s.parent "
            + "left join lot l on s.id = l.salt_form and (l.ignore is null or l.ignore = :ignore) and l.as_drawn_struct is not null "
            + "where p.id in (:parent_ids)) "
            + "select parent_id, as_drawn_struct from AS_DRAWN_STRUCTS where rn = 1")
        .setParameter("parent_ids", parentIds)
        .setParameter("ignore", false);

        // Convert the result list into a hashmap of parent id to mol structure
        HashMap<Long, String> parentIdsToMolStructures = new HashMap<Long, String>();
        List<Object[]> results = q.getResultList();
        for (Object[] result : results) {
            Long parentId = (Long) result[0];
            parentIdsToMolStructures.put(parentId, (String) result[1]);
        }

        // Verify that each of the original parentIds exist in the result set
        for (Long parentId : parentIds) {
            if (!parentIdsToMolStructures.containsKey(parentId)) {
                parentIdsToMolStructures.put(parentId, null);
            }
        }

        return parentIdsToMolStructures;
    }
}
