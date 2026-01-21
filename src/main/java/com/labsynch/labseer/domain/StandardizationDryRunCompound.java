package com.labsynch.labseer.domain;

import static java.lang.Math.toIntExact;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.Version;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.constraints.NotNull;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.chemclasses.CmpdRegMolecule.RegistrationStatus;
import com.labsynch.labseer.chemclasses.CmpdRegMolecule.StandardizationStatus;
import com.labsynch.labseer.dto.StandardizationDryRunSearchDTO;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Entity

@Configurable
public class StandardizationDryRunCompound {

	private int runNumber;

	private Date qcDate;

	@Column(columnDefinition = "text")
	private String newDuplicates;

	@Column(columnDefinition = "text")
	private String existingDuplicates;

	private Boolean changedStructure;

	private Double newMolWeight;

	private Double deltaMolWeight;

	private boolean displayChange;

	private boolean asDrawnDisplayChange;

	private Integer newDuplicateCount;

	private Integer existingDuplicateCount;

	@Column(length=1024)
	private String alias;

	private int CdId;

	@Column(columnDefinition = "text")
	private String molStructure;

	@Column(length=2000)
	private String comment;

	private boolean ignore;

	@Enumerated(EnumType.STRING)
	private CmpdRegMolecule.StandardizationStatus standardizationStatus;

	@Column(columnDefinition = "text")
	private String standardizationComment;

	@Enumerated(EnumType.STRING)
	private CmpdRegMolecule.RegistrationStatus registrationStatus;

	@Column(columnDefinition = "text")
	private String registrationComment;

	@Enumerated(EnumType.STRING)
	@NotNull
	private SyncStatus syncStatus;

	public enum SyncStatus {
		COMPLETE,
		NO_CHANGE_REQUIRED,
		READY
	}

	@ManyToOne
	@NotNull
	@JoinColumn(name = "parent_id")
	private Parent parent;
	
	public StandardizationDryRunCompound() {
		this.setSyncStatus( SyncStatus.NO_CHANGE_REQUIRED);
	}

	@Transactional
	public static TypedQuery<Long> findAllIds() {
		return StandardizationDryRunCompound.entityManager()
				.createQuery("SELECT o.id FROM StandardizationDryRunCompound o", Long.class);
	}

	@Transactional
	public static void truncateTable() {
		int output = StandardizationDryRunCompound.entityManager()
				.createNativeQuery("TRUNCATE standardization_dry_run_compound").executeUpdate();
	}

	public static TypedQuery<Integer> findMaxRunNumber() {
		return StandardizationDryRunCompound.entityManager()
				.createQuery("SELECT max(o.runNumber) FROM StandardizationDryRunCompound o", Integer.class);
	}

	@Transactional
	public static TypedQuery<StandardizationDryRunCompound> findReadyStandardizationChanges() {
		String querySQL = "SELECT o FROM StandardizationDryRunCompound o WHERE o.syncStatus = :syncStatus";
		return StandardizationDryRunCompound.entityManager().createQuery(querySQL, StandardizationDryRunCompound.class).setParameter("syncStatus", SyncStatus.READY);
	}

	@Transactional
	public static int getReadyStandardizationChangesCount() {
		String querySQL = "SELECT count(o.id) FROM StandardizationDryRunCompound o WHERE o.syncStatus = :syncStatus";
		return StandardizationDryRunCompound.entityManager().createQuery(querySQL, Long.class).setParameter("syncStatus", SyncStatus.READY).getSingleResult().intValue();
	}

	@Transactional
	public static int rowCount() {
		return StandardizationDryRunCompound.entityManager()
				.createQuery("SELECT COUNT(o) FROM StandardizationDryRunCompound o", Long.class).getSingleResult()
				.intValue();
	}

	public StandardizationHistory fetchStats() {
		StandardizationHistory stats = new StandardizationHistory();
		stats.setStructuresStandardizedCount(toIntExact(StandardizationDryRunCompound.entityManager()
				.createQuery("SELECT count(s.id) FROM StandardizationDryRunCompound s", Long.class).getSingleResult()));
		stats.setChangedStructureCount(toIntExact(StandardizationDryRunCompound.entityManager()
				.createQuery("SELECT count(s.id) FROM StandardizationDryRunCompound s WHERE s.changedStructure = true",
						Long.class)
				.getSingleResult()));
		stats.setExistingDuplicateCount(toIntExact(StandardizationDryRunCompound.entityManager()
				.createQuery(
						"SELECT count(s.id) FROM StandardizationDryRunCompound s WHERE s.existingDuplicateCount > 0",
						Long.class)
				.getSingleResult()));
		stats.setNewDuplicateCount(toIntExact(StandardizationDryRunCompound.entityManager()
				.createQuery("SELECT count(s.id) FROM StandardizationDryRunCompound s WHERE s.newDuplicateCount > 0",
						Long.class)
				.getSingleResult()));
		stats.setDisplayChangeCount(toIntExact(StandardizationDryRunCompound.entityManager()
				.createQuery("SELECT count(s.id) FROM StandardizationDryRunCompound s WHERE s.displayChange = true",
						Long.class)
				.getSingleResult()));
		stats.setAsDrawnDisplayChangeCount(toIntExact(StandardizationDryRunCompound.entityManager()
				.createQuery(
						"SELECT count(s.id) FROM StandardizationDryRunCompound s WHERE s.asDrawnDisplayChange = true",
						Long.class)
				.getSingleResult()));

		TypedQuery<Long> standardizationStatusCountQuery = StandardizationDryRunCompound.entityManager().createQuery(
				"SELECT count(s.id) FROM StandardizationDryRunCompound s WHERE s.standardizationStatus = :standardizationStatus",
				Long.class);
		standardizationStatusCountQuery.setParameter("standardizationStatus", StandardizationStatus.ERROR);
		stats.setStandardizationErrorCount(toIntExact(standardizationStatusCountQuery.getSingleResult()));

		TypedQuery<Long> registrationStatusCountQuery = StandardizationDryRunCompound.entityManager().createQuery(
				"SELECT count(s.id) FROM StandardizationDryRunCompound s WHERE s.registrationStatus = :registrationStatus",
				Long.class);
		registrationStatusCountQuery.setParameter("registrationStatus", RegistrationStatus.ERROR);
		stats.setRegistrationErrorCount(toIntExact(registrationStatusCountQuery.getSingleResult()));

		return (stats);
	}

	public static List<Predicate> buildPredicateFromNumericValue(CriteriaBuilder cb,
			Root<StandardizationDryRunCompound> root, List<Predicate> predicates, String fieldName, Double value,
			String operator) {
		Predicate predicate = null;
		if (value == null)
			return predicates;
		if (operator == null || operator.equals("=")) {
			predicate = cb.equal(root.get(fieldName), value);
		} else if (operator.equals(">")) {
			predicate = cb.greaterThan(root.get(fieldName), value);
		} else if (operator.equals("<")) {
			predicate = cb.lessThan(root.get(fieldName), value);
		}
		if (predicate != null) {
			predicates.add(predicate);
		}
		return predicates;
	}

	public static TypedQuery<Long> searchStandardiationDryRunCount(StandardizationDryRunSearchDTO dryRunSearch) {
		EntityManager em = StandardizationDryRunCompound.entityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = criteriaBuilder.createQuery(Long.class);
		Root<StandardizationDryRunCompound> root = criteria.from(StandardizationDryRunCompound.class);
		criteria.select(criteriaBuilder.count(root));
		criteria.where(buildPredicatesForSearch(criteriaBuilder, root, dryRunSearch));

		return em.createQuery(criteria);
	}

	private static Predicate buildPredicatesForSearch(CriteriaBuilder criteriaBuilder,
			Root<StandardizationDryRunCompound> root, StandardizationDryRunSearchDTO dryRunSearch) {

		// Predicate List
		List<Predicate> predicates = new ArrayList<Predicate>();

		// New mol weight
		if (dryRunSearch.getNewMolWeight() != null) {
			predicates = buildPredicateFromNumericValue(criteriaBuilder, root, predicates, "newMolWeight",
					dryRunSearch.getNewMolWeight().getValue(), dryRunSearch.getNewMolWeight().getOperator());
		}

		// Delta mol weight
		if (dryRunSearch.getDeltaMolWeight() != null) {
			predicates = buildPredicateFromNumericValue(criteriaBuilder, root, predicates, "deltaMolWeight",
					dryRunSearch.getDeltaMolWeight().getValue(), dryRunSearch.getDeltaMolWeight().getOperator());
		}

		// Old mol weight
		if (dryRunSearch.getOldMolWeight() != null) {
			predicates = buildPredicateFromNumericValue(criteriaBuilder, root, predicates, "oldMolWeight",
					dryRunSearch.getOldMolWeight().getValue(), dryRunSearch.getOldMolWeight().getOperator());
		}

		// Corp name in list
		if (dryRunSearch.getIncludeCorpNames() != null) {
			if (dryRunSearch.getCorpNames() != null && dryRunSearch.getCorpNames().length > 0) {
				if (dryRunSearch.getIncludeCorpNames()) {
					predicates.add(root.join("parent").get("corpName").in(dryRunSearch.getCorpNames()));
				} else {
					predicates.add(criteriaBuilder.not(root.join("parent", JoinType.LEFT).get("corpName").in(dryRunSearch.getCorpNames())));
				}
			} else {
				if (dryRunSearch.getIncludeCorpNames()) {
					// Return 0 rows on purpose because there are no corp names to search for
					predicates.add(criteriaBuilder.equal(root.get("id"), -1));
				}
				// else if not include corp names then it is excluding 0 corpnames and we don't
				// filter
			}
		}

		// Existing duplicate
		if (dryRunSearch.getHasExistingDuplicates() != null) {
			if (dryRunSearch.getHasExistingDuplicates()) {
				predicates.add(root.get("existingDuplicates").isNotNull());
			} else {
				predicates.add(root.get("existingDuplicates").isNull());
			}
		}

		// New duplicates
		if (dryRunSearch.getHasNewDuplicates() != null) {
			if (dryRunSearch.getHasNewDuplicates()) {
				predicates.add(root.get("newDuplicates").isNotNull());
			} else {
				predicates.add(root.get("newDuplicates").isNull());
			}

		}

		// Boolean searches
		// Changed structure
		if (dryRunSearch.getChangedStructure() != null) {
			predicates.add(criteriaBuilder.equal(root.get("changedStructure"), dryRunSearch.getChangedStructure()));
		}

		// Display change
		if (dryRunSearch.getDisplayChange() != null) {
			predicates.add(criteriaBuilder.equal(root.get("displayChange"), dryRunSearch.getDisplayChange()));
		}

		// As drawn display change
		if (dryRunSearch.getAsDrawnDisplayChange() != null) {
			predicates.add(
					criteriaBuilder.equal(root.get("asDrawnDisplayChange"), dryRunSearch.getAsDrawnDisplayChange()));
		}

		// Standardization statuses
		if (dryRunSearch.getStandardizationStatuses() != null) {
			if (dryRunSearch.getStandardizationStatuses().length > 0) {
				predicates.add(root.get("standardizationStatus").in(dryRunSearch.getStandardizationStatuses()));
			} else {
				predicates.add(criteriaBuilder.equal(root.get("id"), -1));
			}
		}

		// Registration statuses
		if (dryRunSearch.getRegistrationStatuses() != null) {
			if (dryRunSearch.getRegistrationStatuses().length > 0) {
				predicates.add(root.get("registrationStatus").in(dryRunSearch.getRegistrationStatuses()));
			} else {
				predicates.add(criteriaBuilder.equal(root.get("id"), -1));
			}
		}

		Predicate[] predicatesToAdd = new Predicate[0];
		predicatesToAdd = predicates.toArray(predicatesToAdd);
		Predicate wherePredicates = criteriaBuilder.and(predicatesToAdd);
		return wherePredicates;
	}

	public static TypedQuery<StandardizationDryRunCompound> searchStandardiationDryRun(
			StandardizationDryRunSearchDTO dryRunSearch) {
		EntityManager em = StandardizationDryRunCompound.entityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<StandardizationDryRunCompound> criteria = criteriaBuilder
				.createQuery(StandardizationDryRunCompound.class);
		Root<StandardizationDryRunCompound> root = criteria.from(StandardizationDryRunCompound.class);
		criteria.select(root);

		criteria.where(buildPredicatesForSearch(criteriaBuilder, root, dryRunSearch));

		criteria.orderBy(criteriaBuilder.desc(root.join("parent").get("corpName")));
		TypedQuery<StandardizationDryRunCompound> q = em.createQuery(criteria);

		if (dryRunSearch.getMaxResults() != null && dryRunSearch.getMaxResults() > -1) {
			q.setMaxResults(dryRunSearch.getMaxResults());
		}

		return q;
	}

	public static StandardizationHistory addStatsToHistory(StandardizationHistory standardizationHistory) {
		standardizationHistory.setStructuresStandardizedCount(toIntExact(StandardizationDryRunCompound.entityManager()
				.createQuery("SELECT count(s.id) FROM StandardizationDryRunCompound s", Long.class).getSingleResult()));
		standardizationHistory.setChangedStructureCount(toIntExact(StandardizationDryRunCompound.entityManager()
				.createQuery("SELECT count(s.id) FROM StandardizationDryRunCompound s WHERE s.changedStructure = true",
						Long.class)
				.getSingleResult()));
		standardizationHistory.setExistingDuplicateCount(toIntExact(StandardizationDryRunCompound.entityManager()
				.createQuery(
						"SELECT count(s.id) FROM StandardizationDryRunCompound s WHERE s.existingDuplicateCount > 0",
						Long.class)
				.getSingleResult()));
		standardizationHistory.setNewDuplicateCount(toIntExact(StandardizationDryRunCompound.entityManager()
				.createQuery("SELECT count(s.id) FROM StandardizationDryRunCompound s WHERE s.newDuplicateCount > 0",
						Long.class)
				.getSingleResult()));
		standardizationHistory.setDisplayChangeCount(toIntExact(StandardizationDryRunCompound.entityManager()
				.createQuery("SELECT count(s.id) FROM StandardizationDryRunCompound s WHERE s.displayChange = true",
						Long.class)
				.getSingleResult()));
		standardizationHistory.setAsDrawnDisplayChangeCount(toIntExact(StandardizationDryRunCompound.entityManager()
				.createQuery(
						"SELECT count(s.id) FROM StandardizationDryRunCompound s WHERE s.asDrawnDisplayChange = true",
						Long.class)
				.getSingleResult()));

		TypedQuery<Long> standardizationStatusCountQuery = StandardizationDryRunCompound.entityManager().createQuery(
				"SELECT count(s.id) FROM StandardizationDryRunCompound s WHERE s.standardizationStatus = :standardizationStatus",
				Long.class);
		standardizationStatusCountQuery.setParameter("standardizationStatus", StandardizationStatus.ERROR);
		standardizationHistory
				.setStandardizationErrorCount(toIntExact(standardizationStatusCountQuery.getSingleResult()));

		TypedQuery<Long> registrationStatusCountQuery = StandardizationDryRunCompound.entityManager().createQuery(
				"SELECT count(s.id) FROM StandardizationDryRunCompound s WHERE s.registrationStatus = :registrationStatus",
				Long.class);
		registrationStatusCountQuery.setParameter("registrationStatus", RegistrationStatus.ERROR);
		standardizationHistory.setRegistrationErrorCount(toIntExact(registrationStatusCountQuery.getSingleResult()));

		return (standardizationHistory);
	}

	public int getRunNumber() {
		return this.runNumber;
	}

	public void setRunNumber(int runNumber) {
		this.runNumber = runNumber;
	}

	public Date getQcDate() {
		return this.qcDate;
	}

	public void setQcDate(Date qcDate) {
		this.qcDate = qcDate;
	}

	public String getNewDuplicates() {
		return this.newDuplicates;
	}

	public void setNewDuplicates(String newDuplicates) {
		this.newDuplicates = newDuplicates;
	}

	public String getExistingDuplicates() {
		return this.existingDuplicates;
	}

	public void setExistingDuplicates(String existingDuplicates) {
		this.existingDuplicates = existingDuplicates;
	}

	public boolean isChangedStructure() {
		return this.changedStructure;
	}

	public void setChangedStructure(boolean changedStructure) {
		this.changedStructure = changedStructure;
	}

	public Double getNewMolWeight() {
		return this.newMolWeight;
	}

	public void setNewMolWeight(Double newMolWeight) {
		this.newMolWeight = newMolWeight;
	}

	public Double getDeltaMolWeight() {
		return this.deltaMolWeight;
	}

	public void setDeltaMolWeight(Double deltaMolWeight) {
		this.deltaMolWeight = deltaMolWeight;
	}

	public boolean isDisplayChange() {
		return this.displayChange;
	}

	public void setDisplayChange(boolean displayChange) {
		this.displayChange = displayChange;
	}

	public boolean isAsDrawnDisplayChange() {
		return this.asDrawnDisplayChange;
	}

	public void setAsDrawnDisplayChange(boolean asDrawnDisplayChange) {
		this.asDrawnDisplayChange = asDrawnDisplayChange;
	}

	public Integer getNewDuplicateCount() {
		return this.newDuplicateCount;
	}

	public void setNewDuplicateCount(Integer newDuplicateCount) {
		this.newDuplicateCount = newDuplicateCount;
	}

	public Integer getExistingDuplicateCount() {
		return this.existingDuplicateCount;
	}

	public void setExistingDuplicateCount(Integer existingDuplicateCount) {
		this.existingDuplicateCount = existingDuplicateCount;
	}

	public String getAlias() {
		return this.alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public int getCdId() {
		return this.CdId;
	}

	public void setCdId(int CdId) {
		this.CdId = CdId;
	}

	public String getMolStructure() {
		return this.molStructure;
	}

	public void setMolStructure(String molStructure) {
		this.molStructure = molStructure;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public boolean isIgnore() {
		return this.ignore;
	}

	public void setIgnore(boolean ignore) {
		this.ignore = ignore;
	}

	public StandardizationStatus getStandardizationStatus() {
		return this.standardizationStatus;
	}

	public void setStandardizationStatus(StandardizationStatus standardizationStatus) {
		this.standardizationStatus = standardizationStatus;
	}

	public String getStandardizationComment() {
		return this.standardizationComment;
	}

	public void setStandardizationComment(String standardizationComment) {
		this.standardizationComment = standardizationComment;
	}

	public RegistrationStatus getRegistrationStatus() {
		return this.registrationStatus;
	}

	public void setRegistrationStatus(RegistrationStatus registrationStatus) {
		this.registrationStatus = registrationStatus;
	}

	public String getRegistrationComment() {
		return this.registrationComment;
	}

	public void setRegistrationComment(String registrationComment) {
		this.registrationComment = registrationComment;
	}

	public SyncStatus getSyncStatus() {
		return this.syncStatus;
	}

	public void setSyncStatus(SyncStatus syncStatus) {
		this.syncStatus = syncStatus;
	}

	public Parent getParent() {
		return this.parent;
	}

	public void setParent(Parent parent) {
		this.parent = parent;
	}

	public static Long countFindStandardizationDryRunCompoundsByCdId(int CdId) {
		EntityManager em = StandardizationDryRunCompound.entityManager();
		TypedQuery q = em.createQuery("SELECT COUNT(o) FROM StandardizationDryRunCompound AS o WHERE o.CdId = :CdId",
				Long.class);
		q.setParameter("CdId", CdId);
		return ((Long) q.getSingleResult());
	}

	public static Long countFindStandardizationDryRunCompoundsByCorpNameEquals(String corpName) {
		if (corpName == null || corpName.length() == 0)
			throw new IllegalArgumentException("The corpName argument is required");
		EntityManager em = StandardizationDryRunCompound.entityManager();
		TypedQuery q = em.createQuery(
				"SELECT COUNT(o) FROM StandardizationDryRunCompound AS o JOIN o.parent p WHERE p.corpName = :corpName", Long.class);
		q.setParameter("corpName", corpName);
		return ((Long) q.getSingleResult());
	}

	public static TypedQuery<StandardizationDryRunCompound> findStandardizationDryRunCompoundsByCdId(int CdId) {
		EntityManager em = StandardizationDryRunCompound.entityManager();
		TypedQuery<StandardizationDryRunCompound> q = em.createQuery(
				"SELECT o FROM StandardizationDryRunCompound AS o WHERE o.CdId = :CdId",
				StandardizationDryRunCompound.class);
		q.setParameter("CdId", CdId);
		return q;
	}

	public static TypedQuery<StandardizationDryRunCompound> findStandardizationDryRunCompoundsByCdId(int CdId,
			String sortFieldName, String sortOrder) {
		EntityManager em = StandardizationDryRunCompound.entityManager();
		StringBuilder queryBuilder = new StringBuilder(
				"SELECT o FROM StandardizationDryRunCompound AS o WHERE o.CdId = :CdId");
		if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
			queryBuilder.append(" ORDER BY ").append(sortFieldName);
			if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
				queryBuilder.append(" ").append(sortOrder);
			}
		}
		TypedQuery<StandardizationDryRunCompound> q = em.createQuery(queryBuilder.toString(),
				StandardizationDryRunCompound.class);
		q.setParameter("CdId", CdId);
		return q;
	}

	public static TypedQuery<StandardizationDryRunCompound> findStandardizationDryRunCompoundsByCorpNameEquals(
			String corpName) {
		if (corpName == null || corpName.length() == 0)
			throw new IllegalArgumentException("The corpName argument is required");
		EntityManager em = StandardizationDryRunCompound.entityManager();
		TypedQuery<StandardizationDryRunCompound> q = em.createQuery(
				"SELECT o FROM StandardizationDryRunCompound AS o JOIN o.parent p WHERE p.corpName = :corpName",
				StandardizationDryRunCompound.class);
		q.setParameter("corpName", corpName);
		return q;
	}

	public static TypedQuery<StandardizationDryRunCompound> findStandardizationDryRunCompoundsByCorpNameEquals(
			String corpName, String sortFieldName, String sortOrder) {
		if (corpName == null || corpName.length() == 0)
			throw new IllegalArgumentException("The corpName argument is required");
		EntityManager em = StandardizationDryRunCompound.entityManager();
		StringBuilder queryBuilder = new StringBuilder(
				"SELECT o FROM StandardizationDryRunCompound AS o JOIN o.parent p WHERE p.corpName = :corpName");
		if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
			queryBuilder.append(" ORDER BY ").append(sortFieldName);
			if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
				queryBuilder.append(" ").append(sortOrder);
			}
		}
		TypedQuery<StandardizationDryRunCompound> q = em.createQuery(queryBuilder.toString(),
				StandardizationDryRunCompound.class);
		q.setParameter("corpName", corpName);
		return q;
	}

    public static TypedQuery<StandardizationDryRunCompound> checkForDuplicateStandardizationDryRunCompoundByCdId(Long standardizationDryRunCompoundId, int[] cdIds) {
		// Given a a standardization dry run compound id ID and list of structure ids
		// Return a list of StandardizationDryRunCompounds which have theame stereo category and stereo comment as the parent id
		// exclude the StandardizationDryRunCompound id from the list
		if(cdIds == null)
			throw new IllegalArgumentException("The cdIds argument is required");
		if(standardizationDryRunCompoundId == null)
			throw new IllegalArgumentException("The standardizationDryRunRowId argument is required");

		StandardizationDryRunCompound standardizationDryRunCompound = findStandardizationDryRunCompound(standardizationDryRunCompoundId);
		if(standardizationDryRunCompound == null)
			throw new IllegalArgumentException("The standardizationDryRunRowId argument is invalid - no standardizationDryRunRow with id " + standardizationDryRunCompoundId + " found");
		
		EntityManager em = StandardizationDryRunCompound.entityManager();
		String queryBuilder = "SELECT o FROM StandardizationDryRunCompound AS o JOIN FETCH o.parent p WHERE o.CdId IN (:cdIds) AND o.id != :id and p.stereoCategory = :stereoCategory";

		Boolean stereoCommentEmpty = standardizationDryRunCompound.getParent().getStereoComment() == null || standardizationDryRunCompound.getParent().getStereoComment().length() == 0;
		if(stereoCommentEmpty) {
			queryBuilder += " AND (p.stereoComment IS NULL or p.stereoComment = '')";
		} else {
			queryBuilder += " AND lower(p.stereoComment) = lower(:stereoComment)";
		}
		TypedQuery<StandardizationDryRunCompound> q = em.createQuery(queryBuilder, StandardizationDryRunCompound.class);
		q.setParameter("cdIds", Arrays.stream(cdIds).boxed().collect( Collectors.toList() ));
		q.setParameter("id", standardizationDryRunCompound.getId());
		q.setParameter("stereoCategory", standardizationDryRunCompound.getParent().getStereoCategory());
		if(!stereoCommentEmpty) {
			q.setParameter("stereoComment", standardizationDryRunCompound.getParent().getStereoComment());
		}
		return q;
    }

	@PersistenceContext
	transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("runNumber", "qcDate",
			"parentId", "corpName", "newDuplicates", "existingDuplicates", "changedStructure", "oldMolWeight",
			"newMolWeight", "deltaMolWeight", "displayChange", "asDrawnDisplayChange", "newDuplicateCount",
			"existingDuplicateCount", "alias", "stereoCategory", "stereoComment", "CdId", "molStructure", "comment",
			"ignore", "standardizationStatus", "standardizationComment", "registrationStatus", "registrationComment");

	public static final EntityManager entityManager() {
		EntityManager em = new StandardizationDryRunCompound().entityManager;
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	public static long countStandardizationDryRunCompounds() {
		return entityManager().createQuery("SELECT COUNT(o) FROM StandardizationDryRunCompound o", Long.class)
				.getSingleResult();
	}

	public static List<StandardizationDryRunCompound> findStandardizationDryRunCompoundsByParent(Parent parent) {
		if (parent == null)
			return null;
		return entityManager().createQuery(
				"SELECT o FROM StandardizationDryRunCompound o WHERE o.parent = :parent", StandardizationDryRunCompound.class)
				.setParameter("parent", parent).getResultList();
	}

	public static StandardizationDryRunCompound findStandardizationDryRunCompoundByParentId(Long parentId) {
		if (parentId == null)
			return null;
		return entityManager().createQuery(
				"SELECT o FROM StandardizationDryRunCompound o WHERE o.parent.id = :parentId", StandardizationDryRunCompound.class)
				.setParameter("parentId", parentId).getSingleResult();
	}

	@Transactional
	public static List<Long> fetchUnprocessedParentIds(int limit) {
		// Fetch a list of parent IDs that are associated with unprocessed dry run standardization rows
		// and lock the parent rows for update
		String sql = "SELECT p.id " +
					 "FROM parent p " +
					 "JOIN standardization_dry_run_compound s ON p.id = s.parent_id " +
					 "WHERE s.registration_status != '" + RegistrationStatus.ERROR + "' " +
					 "AND s.existing_duplicate_count IS NULL " +
					 "ORDER BY p.id ASC " +
					 "FOR UPDATE SKIP LOCKED " +
					 "LIMIT :limit";
	
		@SuppressWarnings("unchecked")
		List<BigInteger> result = StandardizationDryRunCompound.entityManager()
				.createNativeQuery(sql)
				.setParameter("limit", limit)
				.getResultList();
	
		// Convert BigInteger to Long
		return result.stream()
					 .map(BigInteger::longValue)
					 .collect(Collectors.toList());
	}

	@Transactional
	public static int countUnprocessedDryRunStandardizationIds() {
		// Verifies that there are no more unprocessed dry run standardization rows
		String query = "SELECT COUNT(s.id) " +
					   "FROM StandardizationDryRunCompound s " +
					   "WHERE s.registrationStatus != '" + RegistrationStatus.ERROR + "' " +
					   "AND s.existingDuplicateCount IS NULL";
		return StandardizationDryRunCompound.entityManager()
				.createQuery(query, Long.class)
				.getSingleResult().intValue();
	}

	public static List<StandardizationDryRunCompound> findAllStandardizationDryRunCompounds() {
		return entityManager()
				.createQuery("SELECT o FROM StandardizationDryRunCompound o", StandardizationDryRunCompound.class)
				.getResultList();
	}

	public static List<StandardizationDryRunCompound> findAllStandardizationDryRunCompounds(String sortFieldName,
			String sortOrder) {
		String jpaQuery = "SELECT o FROM StandardizationDryRunCompound o";
		if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
			jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
			if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
				jpaQuery = jpaQuery + " " + sortOrder;
			}
		}
		return entityManager().createQuery(jpaQuery, StandardizationDryRunCompound.class).getResultList();
	}

	public static StandardizationDryRunCompound findStandardizationDryRunCompound(Long id) {
		if (id == null)
			return null;
		return entityManager().find(StandardizationDryRunCompound.class, id);
	}

	public static List<StandardizationDryRunCompound> findStandardizationDryRunCompoundEntries(int firstResult,
			int maxResults) {
		return entityManager()
				.createQuery("SELECT o FROM StandardizationDryRunCompound o", StandardizationDryRunCompound.class)
				.setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
	}

	public static List<StandardizationDryRunCompound> findStandardizationDryRunCompoundEntries(int firstResult,
			int maxResults, String sortFieldName, String sortOrder) {
		String jpaQuery = "SELECT o FROM StandardizationDryRunCompound o";
		if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
			jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
			if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
				jpaQuery = jpaQuery + " " + sortOrder;
			}
		}
		return entityManager().createQuery(jpaQuery, StandardizationDryRunCompound.class).setFirstResult(firstResult)
				.setMaxResults(maxResults).getResultList();
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
			StandardizationDryRunCompound attached = StandardizationDryRunCompound
					.findStandardizationDryRunCompound(this.id);
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
	public StandardizationDryRunCompound merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		StandardizationDryRunCompound merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
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

	public String toJson() {
		return new JSONSerializer()
				.exclude("*.class").serialize(this);
	}

	public String toJson(String[] fields) {
		return new JSONSerializer()
				.include(fields).exclude("*.class").serialize(this);
	}

	public static StandardizationDryRunCompound fromJsonToStandardizationDryRunCompound(String json) {
		return new JSONDeserializer<StandardizationDryRunCompound>()
				.use(null, StandardizationDryRunCompound.class).deserialize(json);
	}

	public static String toJsonArray(Collection<StandardizationDryRunCompound> collection) {
		return new JSONSerializer()
				.include("parent.corpName", 
					"parent.molWeight",
					"runNumber", 
					"qcDate",
					"newDuplicates",
					"existingDuplicates",
					"changedStructure",
					"newMolWeight",
					"deltaMolWeight",
					"displayChange",
					"asDrawnDisplayChange",
					"newDuplicateCount",
					"existingDuplicateCount",
					"alias",
					"CdId",
					"comment",
					"standardizationComment",
					"registrationComment",
					"registrationStatus",
					"standardizationStatus",
					"syncStatus"
				)
				.exclude("*.class", "*").serialize(collection);
	}

	public static String toJsonArray(Collection<StandardizationDryRunCompound> collection, String[] fields) {
		return new JSONSerializer()
				.include(fields).exclude("*.class").serialize(collection);
	}

	public static Collection<StandardizationDryRunCompound> fromJsonArrayToStandardizationDryRunCompounds(String json) {
		return new JSONDeserializer<List<StandardizationDryRunCompound>>()
				.use("values", StandardizationDryRunCompound.class).deserialize(json);
	}

	@Transactional
	public static List<Long> fetchUnlockedParentIds(int limit) {
		String sql = "SELECT p.id " +
					"FROM parent p " +
					"WHERE NOT EXISTS (" +
					"    SELECT 1 " +
					"    FROM standardization_dry_run_compound s " +
					"    WHERE s.parent_id = p.id" +
					") " +
					"ORDER BY p.id ASC " +
					"FOR UPDATE SKIP LOCKED " +
					"LIMIT :limit";

		@SuppressWarnings("unchecked")
		List<BigInteger> result = StandardizationDryRunCompound.entityManager()
				.createNativeQuery(sql)
				.setParameter("limit", limit)
				.getResultList();

		// Convert BigInteger to Long
		return result.stream()
					.map(BigInteger::longValue)
					.collect(Collectors.toList());
	}

	@Transactional
	public static int countRemainingParentsNotInStandardizationDryRunCompound() {
		// Count how many parents have not been added to the standardization dry run compound table
		String query = "SELECT COUNT(p.id) " +
					   "FROM Parent p " +
					   "WHERE NOT EXISTS (" +
					   "    SELECT 1 " +
					   "    FROM StandardizationDryRunCompound s " +
					   "    WHERE s.parent.id = p.id" +
					   ")";
		return StandardizationDryRunCompound.entityManager()
				.createQuery(query, Long.class)
				.getSingleResult().intValue();
	}

}