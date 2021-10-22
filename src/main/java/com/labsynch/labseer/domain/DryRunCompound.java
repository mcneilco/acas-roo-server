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
@RooJpaActiveRecord(finders = {"findDryRunCompoundsByCorpNameEquals", "findDryRunCompoundsByCdId" })
public class DryRunCompound {

	private String corpName;

	private String stereoCategory;

	private String stereoComment;

	private int CdId;

	private int RecordNumber;

	@Column(columnDefinition = "text")
	private String molStructure;

	public DryRunCompound() {
	}

	public static List<Long> getAllIds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public void truncateTable() {
		int output = DryRunCompound.entityManager().createNativeQuery("TRUNCATE dry_run_compound").executeUpdate();
	}


}
