package com.labsynch.labseer.domain;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.Transient;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import static java.lang.Math.toIntExact;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.dto.StandardizationDryRunSearchDTO;
import com.labsynch.labseer.dto.configuration.StandardizerSettingsConfigDTO;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = {"findStandardizationDryRunCompoundsByCorpNameEquals","findStandardizationDryRunCompoundsByCdId" })
@Configurable
public class StandardizationDryRunCompound {
	
	private int runNumber;

	private Date qcDate;

	private Long parentId;

	private String corpName;

	private String newDuplicates;

	private String existingDuplicates;

	private boolean changedStructure;

	private Double oldMolWeight;

	private Double newMolWeight;

	private Double deltaMolWeight;

	private boolean displayChange;

	private boolean asDrawnDisplayChange;

	private int newDuplicateCount;

	private int existingDuplicateCount;

	private String alias;

	private String stereoCategory;

	private String stereoComment;

	private int CdId;

	@Column(columnDefinition = "text")
	private String molStructure;

	private String comment;

	private boolean ignore;

	public StandardizationDryRunCompound() {
	}

	@Transactional
	public static TypedQuery<Long> findAllIds() {
		return StandardizationDryRunCompound.entityManager().createQuery("SELECT o.id FROM StandardizationDryRunCompound o", Long.class);
	}

	@Transactional
	public static void truncateTable() {
		int output = StandardizationDryRunCompound.entityManager().createNativeQuery("TRUNCATE standardization_dry_run_compound").executeUpdate();
	}

	public static TypedQuery<Integer> findMaxRunNumber() {
		return StandardizationDryRunCompound.entityManager().createQuery("SELECT max(o.runNumber) FROM StandardizationDryRunCompound o", Integer.class);
	}

	public static TypedQuery<StandardizationDryRunCompound> findStandardizationChanges() {
		String querySQL = "SELECT o FROM StandardizationDryRunCompound o WHERE changedStructure = true OR existingDuplicateCount > 0 OR newDuplicateCount > 0 OR displayChange = true";
		return StandardizationDryRunCompound.entityManager().createQuery(querySQL, StandardizationDryRunCompound.class);
	}

	public static TypedQuery<Long> findParentIdsWithStandardizationChanges() {
		String querySQL = "SELECT o.parentId FROM StandardizationDryRunCompound o WHERE changedStructure = true OR existingDuplicateCount > 0 OR newDuplicateCount > 0 OR displayChange = true";
		return StandardizationDryRunCompound.entityManager().createQuery(querySQL, Long.class);
	}

	public static int getStandardizationChangesCount() {
		return toIntExact(StandardizationDryRunCompound.entityManager().createQuery("SELECT count(s.id) FROM StandardizationDryRunCompound s WHERE changedStructure = true OR existingDuplicateCount > 0 OR newDuplicateCount > 0 OR displayChange = true", Long.class).getSingleResult());
	}

	public static TypedQuery<Long> findParentsWithDisplayChanges() {
		String querySQL = "SELECT o.parentId FROM StandardizationDryRunCompound o WHERE displayChange = true";
		return StandardizationDryRunCompound.entityManager().createQuery(querySQL, Long.class);
	}

	public StandardizationHistory fetchStats() {
//		String querySQL = "SELECT o.parentId FROM StandardizationDryRunCompound o WHERE displayChange = true";
//		Query q = StandardizationDryRunCompound.entityManager().createNativeQuery(querySQL);
		StandardizationHistory stats = new StandardizationHistory();
		stats.setStructuresStandardizedCount(toIntExact(StandardizationDryRunCompound.entityManager().createQuery("SELECT count(s.id) FROM StandardizationDryRunCompound s", Long.class).getSingleResult()));
		stats.setChangedStructureCount(toIntExact(StandardizationDryRunCompound.entityManager().createQuery("SELECT count(s.id) FROM StandardizationDryRunCompound s WHERE s.changedStructure = true", Long.class).getSingleResult()));
		stats.setExistingDuplicateCount(toIntExact(StandardizationDryRunCompound.entityManager().createQuery("SELECT count(s.id) FROM StandardizationDryRunCompound s WHERE s.existingDuplicateCount > 0", Long.class).getSingleResult()));
		stats.setNewDuplicateCount(toIntExact(StandardizationDryRunCompound.entityManager().createQuery("SELECT count(s.id) FROM StandardizationDryRunCompound s WHERE s.newDuplicateCount > 0", Long.class).getSingleResult()));
		stats.setDisplayChangeCount(toIntExact(StandardizationDryRunCompound.entityManager().createQuery("SELECT count(s.id) FROM StandardizationDryRunCompound s WHERE s.displayChange = true", Long.class).getSingleResult()));
		stats.setAsDrawnDisplayChangeCount(toIntExact(StandardizationDryRunCompound.entityManager().createQuery("SELECT count(s.id) FROM StandardizationDryRunCompound s WHERE s.asDrawnDisplayChange = true", Long.class).getSingleResult()));

		return(stats);
	}

	public static List<Predicate> buildPredicateFromNumericValue(CriteriaBuilder cb, Root<StandardizationDryRunCompound> root, List<Predicate> predicates, String fieldName, Double value, String operator) {
		Predicate predicate = null;
		if (operator == null || operator.equals("=")) {
			predicate = cb.equal(root.get(fieldName), value);
		} else if (operator.equals(">")) {
			predicate = cb.greaterThan(root.get(fieldName), value);
		} else if (operator.equals("<")) {
			predicate = cb.lessThan(root.get(fieldName), value);
		}
		if(predicate != null) {
			predicates.add(predicate);
		}
		return predicates;
	}

	public static TypedQuery<StandardizationDryRunCompound> searchStandardiationDryRun(StandardizationDryRunSearchDTO dryRunSearch) {
        EntityManager em = StandardizationDryRunCompound.entityManager();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<StandardizationDryRunCompound> criteria = criteriaBuilder.createQuery(StandardizationDryRunCompound.class);
        Root<StandardizationDryRunCompound> root = criteria.from(StandardizationDryRunCompound.class);
        criteria.select(root);

		// Predicate List
		List<Predicate> predicates = new ArrayList<Predicate>();

		// New mol weight
		if (dryRunSearch.getNewMolWeight() != null) {
			predicates = buildPredicateFromNumericValue(criteriaBuilder, root, predicates, "newMolWeight", dryRunSearch.getNewMolWeight().getValue(), dryRunSearch.getNewMolWeight().getOperator());
		}

		// Delta mol weight
		if (dryRunSearch.getDeltaMolWeight() != null) {
			predicates = buildPredicateFromNumericValue(criteriaBuilder, root, predicates, "deltaMolWeight", dryRunSearch.getDeltaMolWeight().getValue(), dryRunSearch.getDeltaMolWeight().getOperator());
		}

		// Old mol weight
		if (dryRunSearch.getOldMolWeight() != null) {
			predicates = buildPredicateFromNumericValue(criteriaBuilder, root, predicates, "oldMolWeight", dryRunSearch.getOldMolWeight().getValue(), dryRunSearch.getOldMolWeight().getOperator());
		}

		// Corp name in list
		if (dryRunSearch.getCorpNames() != null && dryRunSearch.getCorpNames().length > 0) {
			predicates.add(root.get("corpName").in(dryRunSearch.getCorpNames()));
		}

		// Existing duplicate
		if (dryRunSearch.getHasExistingDuplicates() != null) {
			if(dryRunSearch.getHasExistingDuplicates()) {
				predicates.add(root.get("existingDuplicates").isNotNull());
			} else {
				predicates.add(root.get("existingDuplicates").isNull());
			}
		}

		// New duplicates
		if (dryRunSearch.getHasNewDuplicates() != null) {
			if(dryRunSearch.getHasNewDuplicates()) {
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
			predicates.add(criteriaBuilder.equal(root.get("asDrawnDisplayChange"), dryRunSearch.getAsDrawnDisplayChange()));
		}

		if(predicates.size() > 0) {
			Predicate[] predicatesToAdd = new Predicate[0];
			predicatesToAdd = predicates.toArray(predicatesToAdd);
			Predicate firstItxAndPredicates = criteriaBuilder.and(predicatesToAdd);
			criteria.where(predicates.toArray(new Predicate[predicates.size()]));
		}

        criteria.orderBy(criteriaBuilder.desc(root.get("corpName")));
        TypedQuery<StandardizationDryRunCompound> q = em.createQuery(criteria);		

		return q;
	} 

	public static StandardizationHistory addStatsToHistory(StandardizationHistory standardizationHistory) {
		standardizationHistory.setStructuresStandardizedCount(toIntExact(StandardizationDryRunCompound.entityManager().createQuery("SELECT count(s.id) FROM StandardizationDryRunCompound s", Long.class).getSingleResult()));
		standardizationHistory.setChangedStructureCount(toIntExact(StandardizationDryRunCompound.entityManager().createQuery("SELECT count(s.id) FROM StandardizationDryRunCompound s WHERE s.changedStructure = true", Long.class).getSingleResult()));
		standardizationHistory.setExistingDuplicateCount(toIntExact(StandardizationDryRunCompound.entityManager().createQuery("SELECT count(s.id) FROM StandardizationDryRunCompound s WHERE s.existingDuplicateCount > 0", Long.class).getSingleResult()));
		standardizationHistory.setNewDuplicateCount(toIntExact(StandardizationDryRunCompound.entityManager().createQuery("SELECT count(s.id) FROM StandardizationDryRunCompound s WHERE s.newDuplicateCount > 0", Long.class).getSingleResult()));
		standardizationHistory.setDisplayChangeCount(toIntExact(StandardizationDryRunCompound.entityManager().createQuery("SELECT count(s.id) FROM StandardizationDryRunCompound s WHERE s.displayChange = true", Long.class).getSingleResult()));
		standardizationHistory.setAsDrawnDisplayChangeCount(toIntExact(StandardizationDryRunCompound.entityManager().createQuery("SELECT count(s.id) FROM StandardizationDryRunCompound s WHERE s.asDrawnDisplayChange = true", Long.class).getSingleResult()));
		return(standardizationHistory);
	}
}