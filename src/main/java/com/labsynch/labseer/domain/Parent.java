package com.labsynch.labseer.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.labsynch.labseer.dto.LabelPrefixDTO;
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

public class Parent {

    private static final Logger logger = LoggerFactory.getLogger(Parent.class);

    // @Autowired
    // private ChemStructureService chemService;

    @Column(unique = true)
    @NotNull
    @Size(max = 255)
    @org.hibernate.annotations.Index(name = "Parent_corpName_IDX")
    private String corpName;

    @org.hibernate.annotations.Index(name = "Parent_parentNumber_IDX")
    private long parentNumber;

    private String chemist;

    @Size(max = 1000)
    @org.hibernate.annotations.Index(name = "Parent_commonName_IDX")
    private String commonName;

    @ManyToOne
    @org.hibernate.annotations.Index(name = "Parent_stereoCategory_IDX")
    @JoinColumn(name = "stereo_category")
    private StereoCategory stereoCategory;

    @Size(max = 1000)
    private String stereoComment;

    @Column(columnDefinition = "text")
    private String molStructure;

    private Double molWeight;

    private Double exactMass;

    @Size(max = 4000)
    private String molFormula;

    @NotNull
    @org.hibernate.annotations.Index(name = "Parent_CdId_IDX")
    private int CdId;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    @org.hibernate.annotations.Index(name = "Parent_RegDate_IDX")
    private Date registrationDate;

    private String registeredBy;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    private Date modifiedDate;

    private String modifiedBy;

    private Boolean ignore;

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "parent", fetch = FetchType.LAZY)
    private Set<SaltForm> saltForms = new HashSet<SaltForm>();

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "parent", fetch = FetchType.LAZY)
    private Set<ParentAlias> parentAliases = new HashSet<ParentAlias>();

    @ManyToOne
    @JoinColumn(name = "bulk_load_file")
    private BulkLoadFile bulkLoadFile;

    @ManyToOne
    @JoinColumn(name = "parent_annotation")
    private ParentAnnotation parentAnnotation;

    @ManyToOne
    @JoinColumn(name = "compound_type")
    private CompoundType compoundType;

    @Column(columnDefinition = "text")
    private String comment;

    private Boolean isMixture;

    @Transient
    private transient LabelPrefixDTO labelPrefix;

    @Transactional
    public static void deleteAllParents() {
        List<Parent> parents = Parent.findAllParents();
        for (Parent parent : parents) {
            parent.remove();
        }
    }

    @Transactional
    public static List<Parent> findAllParents() {
        return Parent.entityManager().createQuery("SELECT o FROM Parent o", Parent.class).getResultList();
    }

    @Transactional
    public static Parent findParent(Long id) {
        if (id == null)
            return null;
        return Parent.entityManager().find(Parent.class, id);
    }

    @Transactional
    public static List<Parent> findParentEntries(int firstResult, int maxResults) {
        return Parent.entityManager().createQuery("SELECT o FROM Parent o", Parent.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }

    public static TypedQuery<Parent> findParentsByCdId(int CdId) {
        EntityManager em = Parent.entityManager();
        TypedQuery<Parent> q = em.createQuery(
                "SELECT o FROM Parent AS o WHERE o.CdId = :CdId AND (o.ignore is null OR o.ignore = :ignore)",
                Parent.class);
        q.setParameter("CdId", CdId);
        q.setParameter("ignore", false);
        return q;
    }

    public static TypedQuery<Integer> findParentIdsByCdIdInAndProjectIn(List<Integer> cdIds,
            List<String> projectNames) {
        EntityManager em = Parent.entityManager();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Integer> criteria = criteriaBuilder.createQuery(Integer.class);
        Root<Parent> parentRoot = criteria.from(Parent.class);

        // Join to lots so we can filter out parents that are not in the project list
        Join<Parent, SaltForm> parentSaltForm = parentRoot.join("saltForms");
        Join<SaltForm, Lot> saltFormLot = parentRoot.join("saltForms").join("lots");

        Predicate[] predicates = new Predicate[0];
        List<Predicate> predicateList = new ArrayList<Predicate>();

        // Boolean return empty to break queries if cdIds or project names are empty
        Boolean return_empty_query = false;

        // Filter the results by cdIds list or return no results if no cdIds
        if (cdIds != null && !cdIds.isEmpty()) {
            predicateList.add(parentRoot.get("CdId").in(cdIds));
        } else {
            // Return 0 rows on purpose because there are no corp names to search for
            return_empty_query = true;
        }

        // Filter the results by projects list or return no results if no projects
        if (projectNames != null && !projectNames.isEmpty()) {
            predicateList.add(saltFormLot.get("project").in(projectNames));
        } else {
            // Return 0 rows on purpose because there are no projects to search for
            return_empty_query = true;
        }

        // Return no results if return empty query is true
        if (return_empty_query) {
            predicateList.add(criteriaBuilder.equal(parentRoot.get("id"), -1));
        }

        criteria.select(parentRoot.<Integer>get("id"));

        // Select distinct because we joined one to many lots
        criteria.distinct(true);

        // Add the predicates and create the query
        predicates = predicateList.toArray(predicates);
        criteria.where(criteriaBuilder.and(predicates));
        TypedQuery<Integer> q = em.createQuery(criteria);
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
        if (searchParams.getChemist() != null) {
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
            Predicate predicate = criteriaBuilder.greaterThanOrEqualTo(saltFormLot.<Date>get("synthesisDate"),
                    firstDate);
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
            Predicate predicate = criteriaBuilder.equal(parentSaltForm.get("corpName"),
                    searchParams.getSaltFormCorpName());
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
                predicate = criteriaBuilder.equal(criteriaBuilder.upper(parentAliases.<String>get("aliasName")),
                        aliasName.toUpperCase());
            } else {
                logger.debug("setting up a like query: " + searchParams.getAliasContSelect());
                aliasName = "%" + aliasName + "%";
                predicate = criteriaBuilder.like(criteriaBuilder.upper(parentAliases.<String>get("aliasName")),
                        aliasName);
            }
            predicateList.add(predicate);
        }
        if (searchParams.getMinParentNumber() != null && searchParams.getMaxParentNumber() != null) {
            Predicate predicate1 = criteriaBuilder.greaterThanOrEqualTo(parentRoot.<Long>get("parentNumber"),
                    searchParams.getMinParentNumber());
            Predicate predicate2 = criteriaBuilder.lessThanOrEqualTo(parentRoot.<Long>get("parentNumber"),
                    searchParams.getMaxParentNumber());
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
        if (predicates.length == 0 && searchParams.getMaxSynthDate() == null
                && searchParams.getMaxSynthDate() == null) {
            logger.debug("No search criteria. Returning null.");
        } else {
            logger.debug("parent search query string: " + q.toString());
            parentCdIds = q.getResultList();
        }
        if (searchParams.getMaxResults() != null)
            q.setMaxResults(searchParams.getMaxResults());

        return parentCdIds;
    }

    @Transactional
    public static List<Integer> findAllCdIds() {
        EntityManager em = Parent.entityManager();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Integer> criteria = criteriaBuilder.createQuery(Integer.class);
        Root<Parent> parentRoot = criteria.from(Parent.class);
        criteria.select(parentRoot.<Integer>get("CdId"));
        criteria.distinct(true);
        TypedQuery<Integer> q = em.createQuery(criteria);
        return q.getResultList();
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
        if (stereoCategory == null)
            throw new IllegalArgumentException("The stereoCategory argument is required");
        EntityManager em = Lot.entityManager();
        TypedQuery<Long> q = em.createQuery("SELECT COUNT(p) FROM Parent p WHERE p.stereoCategory = :stereoCategory ",
                Long.class);
        q.setParameter("stereoCategory", stereoCategory);
        return q.getSingleResult();
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

    public static Long countFindParentsByBulkLoadFileEquals(BulkLoadFile bulkLoadFile) {
        if (bulkLoadFile == null)
            throw new IllegalArgumentException("The bulkLoadFile argument is required");
        EntityManager em = Parent.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Parent AS o WHERE o.bulkLoadFile = :bulkLoadFile",
                Long.class);
        q.setParameter("bulkLoadFile", bulkLoadFile);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindParentsByCdId(int CdId) {
        EntityManager em = Parent.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Parent AS o WHERE o.CdId = :CdId", Long.class);
        q.setParameter("CdId", CdId);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindParentsByCommonNameLike(String commonName) {
        if (commonName == null || commonName.length() == 0)
            throw new IllegalArgumentException("The commonName argument is required");
        commonName = commonName.replace('*', '%');
        if (commonName.charAt(0) != '%') {
            commonName = "%" + commonName;
        }
        if (commonName.charAt(commonName.length() - 1) != '%') {
            commonName = commonName + "%";
        }
        EntityManager em = Parent.entityManager();
        TypedQuery q = em.createQuery(
                "SELECT COUNT(o) FROM Parent AS o WHERE LOWER(o.commonName) LIKE LOWER(:commonName)", Long.class);
        q.setParameter("commonName", commonName);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindParentsByCorpNameEquals(String corpName) {
        if (corpName == null || corpName.length() == 0)
            throw new IllegalArgumentException("The corpName argument is required");
        EntityManager em = Parent.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Parent AS o WHERE o.corpName = :corpName", Long.class);
        q.setParameter("corpName", corpName);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindParentsByCorpNameLike(String corpName) {
        if (corpName == null || corpName.length() == 0)
            throw new IllegalArgumentException("The corpName argument is required");
        corpName = corpName.replace('*', '%');
        if (corpName.charAt(0) != '%') {
            corpName = "%" + corpName;
        }
        if (corpName.charAt(corpName.length() - 1) != '%') {
            corpName = corpName + "%";
        }
        EntityManager em = Parent.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Parent AS o WHERE LOWER(o.corpName) LIKE LOWER(:corpName)",
                Long.class);
        q.setParameter("corpName", corpName);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindParentsBySaltForms(Set<SaltForm> saltForms) {
        if (saltForms == null)
            throw new IllegalArgumentException("The saltForms argument is required");
        EntityManager em = Parent.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT COUNT(o) FROM Parent AS o WHERE");
        for (int i = 0; i < saltForms.size(); i++) {
            if (i > 0)
                queryBuilder.append(" AND");
            queryBuilder.append(" :saltForms_item").append(i).append(" MEMBER OF o.saltForms");
        }
        TypedQuery q = em.createQuery(queryBuilder.toString(), Long.class);
        int saltFormsIndex = 0;
        for (SaltForm _saltform : saltForms) {
            q.setParameter("saltForms_item" + saltFormsIndex++, _saltform);
        }
        return ((Long) q.getSingleResult());
    }

    public static TypedQuery<Parent> findParentsByBulkLoadFileEquals(BulkLoadFile bulkLoadFile) {
        if (bulkLoadFile == null)
            throw new IllegalArgumentException("The bulkLoadFile argument is required");
        EntityManager em = Parent.entityManager();
        TypedQuery<Parent> q = em.createQuery("SELECT o FROM Parent AS o WHERE o.bulkLoadFile = :bulkLoadFile",
                Parent.class);
        q.setParameter("bulkLoadFile", bulkLoadFile);
        return q;
    }

    public static TypedQuery<Parent> findParentsByBulkLoadFileEquals(BulkLoadFile bulkLoadFile, String sortFieldName,
            String sortOrder) {
        if (bulkLoadFile == null)
            throw new IllegalArgumentException("The bulkLoadFile argument is required");
        EntityManager em = Parent.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM Parent AS o WHERE o.bulkLoadFile = :bulkLoadFile");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Parent> q = em.createQuery(queryBuilder.toString(), Parent.class);
        q.setParameter("bulkLoadFile", bulkLoadFile);
        return q;
    }

    public static TypedQuery<Parent> findParentsByCdId(int CdId, String sortFieldName, String sortOrder) {
        EntityManager em = Parent.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Parent AS o WHERE o.CdId = :CdId");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Parent> q = em.createQuery(queryBuilder.toString(), Parent.class);
        q.setParameter("CdId", CdId);
        return q;
    }

    public static TypedQuery<Parent> findParentsByCommonNameLike(String commonName) {
        if (commonName == null || commonName.length() == 0)
            throw new IllegalArgumentException("The commonName argument is required");
        commonName = commonName.replace('*', '%');
        if (commonName.charAt(0) != '%') {
            commonName = "%" + commonName;
        }
        if (commonName.charAt(commonName.length() - 1) != '%') {
            commonName = commonName + "%";
        }
        EntityManager em = Parent.entityManager();
        TypedQuery<Parent> q = em.createQuery(
                "SELECT o FROM Parent AS o WHERE LOWER(o.commonName) LIKE LOWER(:commonName)", Parent.class);
        q.setParameter("commonName", commonName);
        return q;
    }

    public static TypedQuery<Parent> findParentsByCommonNameLike(String commonName, String sortFieldName,
            String sortOrder) {
        if (commonName == null || commonName.length() == 0)
            throw new IllegalArgumentException("The commonName argument is required");
        commonName = commonName.replace('*', '%');
        if (commonName.charAt(0) != '%') {
            commonName = "%" + commonName;
        }
        if (commonName.charAt(commonName.length() - 1) != '%') {
            commonName = commonName + "%";
        }
        EntityManager em = Parent.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM Parent AS o WHERE LOWER(o.commonName) LIKE LOWER(:commonName)");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Parent> q = em.createQuery(queryBuilder.toString(), Parent.class);
        q.setParameter("commonName", commonName);
        return q;
    }

    public static TypedQuery<Parent> findParentsByCorpNameEquals(String corpName) {
        if (corpName == null || corpName.length() == 0)
            throw new IllegalArgumentException("The corpName argument is required");
        EntityManager em = Parent.entityManager();
        TypedQuery<Parent> q = em.createQuery("SELECT o FROM Parent AS o WHERE o.corpName = :corpName", Parent.class);
        q.setParameter("corpName", corpName);
        return q;
    }

    public static TypedQuery<Parent> findParentsByCorpNameEquals(String corpName, String sortFieldName,
            String sortOrder) {
        if (corpName == null || corpName.length() == 0)
            throw new IllegalArgumentException("The corpName argument is required");
        EntityManager em = Parent.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Parent AS o WHERE o.corpName = :corpName");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Parent> q = em.createQuery(queryBuilder.toString(), Parent.class);
        q.setParameter("corpName", corpName);
        return q;
    }

    public static TypedQuery<Parent> findParentsByCorpNameLike(String corpName) {
        if (corpName == null || corpName.length() == 0)
            throw new IllegalArgumentException("The corpName argument is required");
        corpName = corpName.replace('*', '%');
        if (corpName.charAt(0) != '%') {
            corpName = "%" + corpName;
        }
        if (corpName.charAt(corpName.length() - 1) != '%') {
            corpName = corpName + "%";
        }
        EntityManager em = Parent.entityManager();
        TypedQuery<Parent> q = em.createQuery("SELECT o FROM Parent AS o WHERE LOWER(o.corpName) LIKE LOWER(:corpName)",
                Parent.class);
        q.setParameter("corpName", corpName);
        return q;
    }

    public static TypedQuery<Parent> findParentsByCorpNameLike(String corpName, String sortFieldName,
            String sortOrder) {
        if (corpName == null || corpName.length() == 0)
            throw new IllegalArgumentException("The corpName argument is required");
        corpName = corpName.replace('*', '%');
        if (corpName.charAt(0) != '%') {
            corpName = "%" + corpName;
        }
        if (corpName.charAt(corpName.length() - 1) != '%') {
            corpName = corpName + "%";
        }
        EntityManager em = Parent.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM Parent AS o WHERE LOWER(o.corpName) LIKE LOWER(:corpName)");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Parent> q = em.createQuery(queryBuilder.toString(), Parent.class);
        q.setParameter("corpName", corpName);
        return q;
    }

    public static TypedQuery<Parent> findParentsBySaltForms(Set<SaltForm> saltForms) {
        if (saltForms == null)
            throw new IllegalArgumentException("The saltForms argument is required");
        EntityManager em = Parent.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Parent AS o WHERE");
        for (int i = 0; i < saltForms.size(); i++) {
            if (i > 0)
                queryBuilder.append(" AND");
            queryBuilder.append(" :saltForms_item").append(i).append(" MEMBER OF o.saltForms");
        }
        TypedQuery<Parent> q = em.createQuery(queryBuilder.toString(), Parent.class);
        int saltFormsIndex = 0;
        for (SaltForm _saltform : saltForms) {
            q.setParameter("saltForms_item" + saltFormsIndex++, _saltform);
        }
        return q;
    }

    public static TypedQuery<Parent> findParentsByAnySaltForms(Set<SaltForm> saltForms) {
        if (saltForms == null)
            throw new IllegalArgumentException("The saltForms argument is required");
        EntityManager em = Parent.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Parent AS o WHERE");
        for (int i = 0; i < saltForms.size(); i++) {
            if (i > 0)
                queryBuilder.append(" OR");
            queryBuilder.append(" :saltForms_item").append(i).append(" MEMBER OF o.saltForms");
        }
        TypedQuery<Parent> q = em.createQuery(queryBuilder.toString(), Parent.class);
        int saltFormsIndex = 0;
        for (SaltForm _saltform : saltForms) {
            q.setParameter("saltForms_item" + saltFormsIndex++, _saltform);
        }
        return q;
    }

    public static TypedQuery<Parent> findParentsBySaltForms(Set<SaltForm> saltForms, String sortFieldName,
            String sortOrder) {
        if (saltForms == null)
            throw new IllegalArgumentException("The saltForms argument is required");
        EntityManager em = Parent.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Parent AS o WHERE");
        for (int i = 0; i < saltForms.size(); i++) {
            if (i > 0)
                queryBuilder.append(" AND");
            queryBuilder.append(" :saltForms_item").append(i).append(" MEMBER OF o.saltForms");
        }
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" " + sortOrder);
            }
        }
        TypedQuery<Parent> q = em.createQuery(queryBuilder.toString(), Parent.class);
        int saltFormsIndex = 0;
        for (SaltForm _saltform : saltForms) {
            q.setParameter("saltForms_item" + saltFormsIndex++, _saltform);
        }
        return q;
    }

    public static TypedQuery<Parent> checkForDuplicateParentByCdId(Long parentId, int[] cdIds) {
        if(cdIds == null)
            throw new IllegalArgumentException("The cdIds argument is required");
        if(parentId == null)
            throw new IllegalArgumentException("The parentId argument is required");

        Parent parent = findParent(parentId);
        if(parent == null)
            throw new IllegalArgumentException("The parentId argument is invalid - no parent with id " + parentId + " found");
        
        EntityManager em = Parent.entityManager();
        String queryBuilder = "SELECT o FROM Parent AS o WHERE o.CdId IN (:cdIds) AND o.id != :id and o.stereoCategory = :stereoCategory";

        if(parent.getStereoComment() == null) {
            queryBuilder += " AND o.stereoComment IS NULL";
        } else {
            queryBuilder += " AND o.stereoComment = :stereoComment";
        }
        TypedQuery<Parent> q = em.createQuery(queryBuilder, Parent.class);
        q.setParameter("cdIds", Arrays.stream(cdIds).boxed().collect( Collectors.toList() ));
        q.setParameter("id", parent.getId());
        q.setParameter("stereoCategory", parent.getStereoCategory());
		if(parent.getStereoComment() != null) {
			q.setParameter("stereoComment", parent.getStereoComment());
		}
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

    public static Parent fromJsonToParent(String json) {
        return new JSONDeserializer<Parent>()
                .use(null, Parent.class).deserialize(json);
    }

    public static String toJsonArray(Collection<Parent> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<Parent> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<Parent> fromJsonArrayToParents(String json) {
        return new JSONDeserializer<List<Parent>>()
                .use("values", Parent.class).deserialize(json);
    }

    public String getCorpName() {
        return this.corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public long getParentNumber() {
        return this.parentNumber;
    }

    public void setParentNumber(long parentNumber) {
        this.parentNumber = parentNumber;
    }

    public String getChemist() {
        return this.chemist;
    }

    public void setChemist(String chemist) {
        this.chemist = chemist;
    }

    public String getCommonName() {
        return this.commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public StereoCategory getStereoCategory() {
        return this.stereoCategory;
    }

    public void setStereoCategory(StereoCategory stereoCategory) {
        this.stereoCategory = stereoCategory;
    }

    public String getStereoComment() {
        return this.stereoComment;
    }

    public void setStereoComment(String stereoComment) {
        this.stereoComment = stereoComment;
    }

    public String getMolStructure() {
        return this.molStructure;
    }

    public void setMolStructure(String molStructure) {
        this.molStructure = molStructure;
    }

    public Double getMolWeight() {
        return this.molWeight;
    }

    public void setMolWeight(Double molWeight) {
        this.molWeight = molWeight;
    }

    public Double getExactMass() {
        return this.exactMass;
    }

    public void setExactMass(Double exactMass) {
        this.exactMass = exactMass;
    }

    public String getMolFormula() {
        return this.molFormula;
    }

    public void setMolFormula(String molFormula) {
        this.molFormula = molFormula;
    }

    public int getCdId() {
        return this.CdId;
    }

    public void setCdId(int CdId) {
        this.CdId = CdId;
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

    public Boolean getIgnore() {
        return this.ignore;
    }

    public void setIgnore(Boolean ignore) {
        this.ignore = ignore;
    }

    public Set<SaltForm> getSaltForms() {
        return this.saltForms;
    }

    public void setSaltForms(Set<SaltForm> saltForms) {
        this.saltForms = saltForms;
    }

    public Set<ParentAlias> getParentAliases() {
        return this.parentAliases;
    }

    public void setParentAliases(Set<ParentAlias> parentAliases) {
        this.parentAliases = parentAliases;
    }

    public BulkLoadFile getBulkLoadFile() {
        return this.bulkLoadFile;
    }

    public void setBulkLoadFile(BulkLoadFile bulkLoadFile) {
        this.bulkLoadFile = bulkLoadFile;
    }

    public ParentAnnotation getParentAnnotation() {
        return this.parentAnnotation;
    }

    public void setParentAnnotation(ParentAnnotation parentAnnotation) {
        this.parentAnnotation = parentAnnotation;
    }

    public CompoundType getCompoundType() {
        return this.compoundType;
    }

    public void setCompoundType(CompoundType compoundType) {
        this.compoundType = compoundType;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getIsMixture() {
        return this.isMixture;
    }

    public void setIsMixture(Boolean isMixture) {
        this.isMixture = isMixture;
    }

    @PersistenceContext
    transient EntityManager entityManager;

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "corpName",
            "parentNumber", "chemist", "commonName", "stereoCategory", "stereoComment", "molStructure", "molWeight",
            "exactMass", "molFormula", "CdId", "registrationDate", "registeredBy", "modifiedDate", "modifiedBy",
            "ignore", "saltForms", "parentAliases", "bulkLoadFile", "parentAnnotation", "compoundType", "comment",
            "isMixture", "labelPrefix");

    public static final EntityManager entityManager() {
        EntityManager em = new Parent().entityManager;
        if (em == null)
            throw new IllegalStateException(
                    "Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countParents() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Parent o", Long.class).getSingleResult();
    }

    public static List<Parent> findAllParents(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Parent o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Parent.class).getResultList();
    }

    public static List<Parent> findParentEntries(int firstResult, int maxResults, String sortFieldName,
            String sortOrder) {
        String jpaQuery = "SELECT o FROM Parent o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Parent.class).setFirstResult(firstResult).setMaxResults(maxResults)
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
            Parent attached = Parent.findParent(this.id);
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
    public Parent merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        Parent merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
