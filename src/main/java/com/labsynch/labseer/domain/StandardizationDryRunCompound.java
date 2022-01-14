package com.labsynch.labseer.domain;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Transient;
import javax.persistence.TypedQuery;
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

	public static TypedQuery<StandardizationDryRunCompound> searchStandardiationDryRun(StandardizationDryRunSearchDTO dryRunSearch) {
		return findStandardizationChanges();
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