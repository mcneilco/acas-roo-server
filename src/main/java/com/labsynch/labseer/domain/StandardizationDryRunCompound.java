package com.labsynch.labseer.domain;

import static java.lang.Math.toIntExact;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;

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

	private String newDuplicates;

	private String existingDuplicates;

	private boolean changedStructure;

	private Double newMolWeight;

	private Double deltaMolWeight;

	private boolean displayChange;

	private boolean asDrawnDisplayChange;

	private int newDuplicateCount;

	private int existingDuplicateCount;

	private String alias;

	private int CdId;

	@Column(columnDefinition = "text")
	private String molStructure;

	private String comment;

	private boolean ignore;

	@Enumerated(EnumType.STRING)
	private CmpdRegMolecule.StandardizationStatus standardizationStatus;

	private String standardizationComment;

	@Enumerated(EnumType.STRING)
	private CmpdRegMolecule.RegistrationStatus registrationStatus;

	private String registrationComment;

	@Enumerated(EnumType.STRING)
	@NotNull
	private SyncStatus parentSyncStatus;

	public enum SyncStatus {
		COMPLETE,
		READY
	}

	@ManyToOne
	@NotNull
	@JoinColumn(name = "parent_id")
	private Parent parent;
	
	public StandardizationDryRunCompound() {
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
	public static TypedQuery<StandardizationDryRunCompound> findStandardizationChanges() {
		String querySQL = "SELECT o FROM StandardizationDryRunCompound o WHERE o.changedStructure = true OR o.existingDuplicateCount > 0 OR o.newDuplicateCount > 0 OR o.displayChange = true";
		return StandardizationDryRunCompound.entityManager().createQuery(querySQL, StandardizationDryRunCompound.class);
	}

	public static TypedQuery<StandardizationDryRunCompound> findReadyStandardizationChanges() {
		String querySQL = "SELECT o FROM StandardizationDryRunCompound o WHERE o.parentSyncStatus = :parentSyncStatus and (o.changedStructure = true OR o.existingDuplicateCount > 0 OR o.newDuplicateCount > 0 OR o.displayChange = true)";
		return StandardizationDryRunCompound.entityManager().createQuery(querySQL, StandardizationDryRunCompound.class).setParameter("parentSyncStatus", SyncStatus.READY);
	}

	@Transactional
	public static int getReadyStandardizationChangesCount() {
		String querySQL = "SELECT count(o.id) FROM StandardizationDryRunCompound o WHERE o.parentSyncStatus = :parentSyncStatus and (o.changedStructure = true OR o.existingDuplicateCount > 0 OR o.newDuplicateCount > 0 OR o.displayChange = true)";
		return StandardizationDryRunCompound.entityManager().createQuery(querySQL, Long.class).setParameter("parentSyncStatus", SyncStatus.READY).getSingleResult().intValue();
	}

	@Transactional
	public static int getStandardizationChangesCount() {
		return toIntExact(StandardizationDryRunCompound.entityManager().createQuery(
				"SELECT count(s.id) FROM StandardizationDryRunCompound s WHERE changedStructure = true OR existingDuplicateCount > 0 OR newDuplicateCount > 0 OR displayChange = true",
				Long.class).getSingleResult());
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
					predicates.add(root.get("corpName").in(dryRunSearch.getCorpNames()));
				} else {
					predicates.add(criteriaBuilder.not(root.get("corpName").in(dryRunSearch.getCorpNames())));
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

		criteria.orderBy(criteriaBuilder.desc(root.get("corpName")));
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

	public int getNewDuplicateCount() {
		return this.newDuplicateCount;
	}

	public void setNewDuplicateCount(int newDuplicateCount) {
		this.newDuplicateCount = newDuplicateCount;
	}

	public int getExistingDuplicateCount() {
		return this.existingDuplicateCount;
	}

	public void setExistingDuplicateCount(int existingDuplicateCount) {
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

	public SyncStatus getParentSyncStatus() {
		return this.parentSyncStatus;
	}

	public void setParentSyncStatus(SyncStatus parentSyncStatus) {
		this.parentSyncStatus = parentSyncStatus;
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
				"SELECT COUNT(o) FROM StandardizationDryRunCompound AS o WHERE o.corpName = :corpName", Long.class);
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
				"SELECT o FROM StandardizationDryRunCompound AS o WHERE o.corpName = :corpName",
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
				"SELECT o FROM StandardizationDryRunCompound AS o WHERE o.corpName = :corpName");
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
				.exclude("*.class").serialize(collection);
	}

	public static String toJsonArray(Collection<StandardizationDryRunCompound> collection, String[] fields) {
		return new JSONSerializer()
				.include(fields).exclude("*.class").serialize(collection);
	}

	public static Collection<StandardizationDryRunCompound> fromJsonArrayToStandardizationDryRunCompounds(String json) {
		return new JSONDeserializer<List<StandardizationDryRunCompound>>()
				.use("values", StandardizationDryRunCompound.class).deserialize(json);
	}
}