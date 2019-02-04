package com.labsynch.labseer.domain;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.TypedQuery;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = {"findQcCompoundsByCdId" })
public class QcCompound {

	// id, runNumber, qcDate, parentId, corpName, dupeCount, dupeCorpName, asDrawnStruct, preMolStruct, postMolStruct, comment
	private int runNumber;

	private Date qcDate;

	private Long parentId;

	private String corpName;

	private boolean displayChange;

	private int dupeCount;

	private String dupeCorpName;

	private String alias;

	private String stereoCategory;

	private String stereoComment;

	private int CdId;

	@Column(columnDefinition = "text")
	private String molStructure;

	private String comment;

	private boolean ignore;

	public QcCompound() {
	}

	public static List<Long> getAllIds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public static TypedQuery<Long> findAllIds() {
		return QcCompound.entityManager().createQuery("SELECT o.id FROM QcCompound o", Long.class);
	}

	@Transactional
	public static void truncateTable() {
		int output = QcCompound.entityManager().createNativeQuery("TRUNCATE qc_compound").executeUpdate();
	}

	public static TypedQuery<Integer> findMaxRunNumber() {
		return QcCompound.entityManager().createQuery("SELECT max(o.runNumber) FROM QcCompound o", Integer.class);
	}

	public static TypedQuery<Long> findPotentialQcCmpds() {
		String querySQL = "SELECT o.id FROM QcCompound o WHERE displayChange = true OR dupeCount > 0";
		return QcCompound.entityManager().createQuery(querySQL, Long.class);
	}
	
	public static TypedQuery<Long> findParentsWithDisplayChanges() {
		String querySQL = "SELECT o.parentId FROM QcCompound o WHERE displayChange = true";
		return QcCompound.entityManager().createQuery(querySQL, Long.class);
	}
}
