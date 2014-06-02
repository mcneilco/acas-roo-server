package com.labsynch.labseer.domain;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.dto.PreferredNameDTO;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = { "findLsThingLabelsByLsThing", "findLsThingLabelsByLsTransactionEquals", 
		"findLsThingLabelsByLabelTextEquals",
		"findLsThingLabelsByLabelTextEqualsAndIgnoredNot",
		"findLsThingLabelsByLabelTextEqualsAndIgnoredNot", "findLsThingLabelsByLabelTextLike" })
public class LsThingLabel extends AbstractLabel {

	private static final Logger logger = LoggerFactory.getLogger(LsThingLabel.class);

	@NotNull
	@ManyToOne
	@JoinColumn(name = "lsthing_id")
	private LsThing lsThing;

	private String lsThingTypeAndKind;
	
    public LsThingLabel() {
    }
    
	public LsThingLabel(LsThingLabel lsLabel) {
		super.setLsType(lsLabel.getLsType());
		super.setLsKind(lsLabel.getLsKind());
		super.setLsTypeAndKind(lsLabel.getLsType() + "_" + lsLabel.getLsKind());
		super.setLabelText(lsLabel.getLabelText());
		super.setPreferred(lsLabel.isPreferred());
		super.setLsTransaction(lsLabel.getLsTransaction());
		super.setRecordedBy(lsLabel.getRecordedBy());
		super.setRecordedDate(lsLabel.getRecordedDate());
		super.setPhysicallyLabled(lsLabel.isPhysicallyLabled());
	}

	public static LsThingLabel update(LsThingLabel lsLabel) {
		LsThingLabel updatedLsThingLabel = LsThingLabel.findLsThingLabel(lsLabel.getId());
		updatedLsThingLabel.setLsType(lsLabel.getLsType());
		updatedLsThingLabel.setLsKind(lsLabel.getLsKind());
		updatedLsThingLabel.setLsTypeAndKind(lsLabel.getLsType() + "_" + lsLabel.getLsKind());
		updatedLsThingLabel.setLabelText(lsLabel.getLabelText());
		updatedLsThingLabel.setPreferred(lsLabel.isPreferred());
		updatedLsThingLabel.setLsTransaction(lsLabel.getLsTransaction());
		updatedLsThingLabel.setRecordedBy(lsLabel.getRecordedBy());
		updatedLsThingLabel.setRecordedDate(lsLabel.getRecordedDate());
		updatedLsThingLabel.setModifiedDate(new Date());
		updatedLsThingLabel.setPhysicallyLabled(lsLabel.isPhysicallyLabled());
		updatedLsThingLabel.setIgnored(lsLabel.isIgnored());
		updatedLsThingLabel.merge();
		return updatedLsThingLabel;
	}
	
	public static Long countOfLsThingByName(String thingType, String thingKind, String labelType, String labelKind, String labelText) {
		if (thingType == null || thingType.length() == 0) throw new IllegalArgumentException("The thingType argument is required");
		if (thingKind == null || thingKind.length() == 0) throw new IllegalArgumentException("The thingKind argument is required");		
		if (labelType == null || labelType.length() == 0) throw new IllegalArgumentException("The labelType argument is required");
		if (labelKind == null || labelKind.length() == 0) throw new IllegalArgumentException("The labelKind argument is required");	
		if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");

		boolean ignored = true;

		EntityManager em = LsThingLabel.entityManager();
		String query = "SELECT count(DISTINCT o) FROM LsThing AS o " +
				"JOIN o.lsLabels AS ll1 with ll1.ignored IS NOT :ignored AND ll1.labelText = :labelText AND ll1.lsType = :labelType AND ll1.lsKind = :labelKind " +
				"WHERE o.ignored IS NOT :ignored AND o.lsType = :thingType AND o.lsKind = :thingKind ";
		logger.debug("sql query " + query);
		TypedQuery<Long> q = em.createQuery(query, Long.class);

		q.setParameter("thingType", thingType);
		q.setParameter("thingKind", thingKind);
		q.setParameter("labelType", labelType);
		q.setParameter("labelKind", labelKind);
		q.setParameter("labelText", labelText);
		q.setParameter("ignored", ignored);
		return q.getSingleResult();
	}
	
	
	public static TypedQuery<LsThingLabel> findLsThingName(String thingType, String thingKind, String labelType, String labelKind, String labelText) {
		if (thingType == null || thingType.length() == 0) throw new IllegalArgumentException("The thingType argument is required");
		if (thingKind == null || thingKind.length() == 0) throw new IllegalArgumentException("The thingKind argument is required");		
		if (labelType == null || labelType.length() == 0) throw new IllegalArgumentException("The labelType argument is required");
		if (labelKind == null || labelKind.length() == 0) throw new IllegalArgumentException("The labelKind argument is required");	
		if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");

		boolean ignored = true;

		EntityManager em = LsThingLabel.entityManager();
		String query = "SELECT DISTINCT ll2 FROM LsThing AS o " +
				"JOIN o.lsLabels AS ll1 with ll1.ignored IS NOT :ignored AND ll1.labelText = :labelText AND ll1.lsType = :labelType AND ll1.lsKind = :labelKind " +
				"JOIN o.lsLabels AS ll2 with ll2.ignored IS NOT :ignored " +
				"WHERE o.ignored IS NOT :ignored AND o.lsType = :thingType AND o.lsKind = :thingKind ";
		logger.debug("sql query " + query);
		TypedQuery<LsThingLabel> q = em.createQuery(query, LsThingLabel.class);

		q.setParameter("thingType", thingType);
		q.setParameter("thingKind", thingKind);
		q.setParameter("labelType", labelType);
		q.setParameter("labelKind", labelKind);
		q.setParameter("labelText", labelText);
		q.setParameter("ignored", ignored);
		return q;
	}
	
	public static TypedQuery<LsThingLabel> findLsThingPreferredName(String thingType, String thingKind, String labelType, String labelKind, String labelText) {
		if (thingType == null || thingType.length() == 0) throw new IllegalArgumentException("The thingType argument is required");
		if (thingKind == null || thingKind.length() == 0) throw new IllegalArgumentException("The thingKind argument is required");		
		if (labelType == null || labelType.length() == 0) throw new IllegalArgumentException("The labelType argument is required");
		if (labelKind == null || labelKind.length() == 0) throw new IllegalArgumentException("The labelKind argument is required");	
		if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");

		boolean preferred = true;
		boolean ignored = true;

		EntityManager em = LsThingLabel.entityManager();
		String query = "SELECT DISTINCT ll2 FROM LsThing AS o " +
				"JOIN o.lsLabels AS ll1 with ll1.ignored IS NOT :ignored AND ll1.labelText = :labelText AND ll1.lsType = :labelType AND ll1.lsKind = :labelKind " +
				"JOIN o.lsLabels AS ll2 with ll2.ignored IS NOT :ignored AND ll2.preferred IS :preferred " +
				"WHERE o.ignored IS NOT :ignored AND o.lsType = :thingType AND o.lsKind = :thingKind ";
		logger.debug("sql query " + query);
		TypedQuery<LsThingLabel> q = em.createQuery(query, LsThingLabel.class);

		q.setParameter("thingType", thingType);
		q.setParameter("thingKind", thingKind);
		q.setParameter("labelType", labelType);
		q.setParameter("labelKind", labelKind);
		q.setParameter("labelText", labelText);
		q.setParameter("preferred", preferred);
		q.setParameter("ignored", ignored);
		return q;
	}

	public static TypedQuery<LsThingLabel> findLsThingPreferredName(String thingType, String thingKind, String labelText) {
		if (thingType == null || thingType.length() == 0) throw new IllegalArgumentException("The thingType argument is required");
		if (thingKind == null || thingKind.length() == 0) throw new IllegalArgumentException("The thingKind argument is required");		
		if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");

		boolean preferred = true;
		boolean ignored = true;

		EntityManager em = LsThingLabel.entityManager();
		String query = "SELECT DISTINCT ll2 FROM LsThing AS o " +
				"JOIN o.lsLabels AS ll1 with ll1.ignored IS NOT :ignored AND ll1.labelText = :labelText " +
				"JOIN o.lsLabels AS ll2 with ll2.ignored IS NOT :ignored AND ll2.preferred IS :preferred " +
				"WHERE o.ignored IS NOT :ignored AND o.lsType = :thingType AND o.lsKind = :thingKind ";
		logger.debug("sql query " + query);
		TypedQuery<LsThingLabel> q = em.createQuery(query, LsThingLabel.class);

		q.setParameter("thingType", thingType);
		q.setParameter("thingKind", thingKind);
		q.setParameter("labelText", labelText);
		q.setParameter("preferred", preferred);
		q.setParameter("ignored", ignored);
		return q;
	}
	
	public static TypedQuery<LsThingLabel> findLsThingPreferredName(String labelText) {
		if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");
		
		boolean preferred = true;
		boolean ignored = true;

		EntityManager em = LsThingLabel.entityManager();
		String query = "SELECT DISTINCT ll2 FROM LsThing AS o " +
				"JOIN o.lsLabels AS ll1 with ll1.ignored IS NOT :ignored AND ll1.labelText = :labelText " +
				"JOIN o.lsLabels AS ll2 with ll2.ignored IS NOT :ignored AND ll2.preferred IS :preferred " +
				"WHERE o.ignored IS NOT :ignored ";
		logger.debug("sql query " + query);
		TypedQuery<LsThingLabel> q = em.createQuery(query, LsThingLabel.class);

		q.setParameter("labelText", labelText);
		q.setParameter("preferred", preferred);
		q.setParameter("ignored", ignored);
		return q;
	}

	public static TypedQuery<LsThingLabel> findLsThingPreferredName(Long lsThingId, String labelType, String labelKind) {
		if (lsThingId == null || lsThingId == 0) throw new IllegalArgumentException("The lsThingId argument is required");
		if (labelType == null || labelType.length() == 0) throw new IllegalArgumentException("The labelType argument is required");
		if (labelKind == null || labelKind.length() == 0) throw new IllegalArgumentException("The labelKind argument is required");
		
		boolean preferred = true;
		boolean ignored = true;

		TypedQuery<LsThingLabel> q = findLsThingPreferredName(lsThingId, labelType, labelKind,  preferred,  ignored);
		return q;
	}
	
	private static TypedQuery<LsThingLabel> findLsThingPreferredName(
			Long lsThingId, String labelType, String labelKind,
			boolean preferred, boolean ignored) {
		
		if (lsThingId == null || lsThingId == 0) throw new IllegalArgumentException("The lsThingId argument is required");
		if (labelType == null || labelType.length() == 0) throw new IllegalArgumentException("The labelType argument is required");
		if (labelKind == null || labelKind.length() == 0) throw new IllegalArgumentException("The labelKind argument is required");
		
		EntityManager em = LsThingLabel.entityManager();
		String query = "SELECT DISTINCT o FROM LsThingLabel AS o WHERE o.lsType = :labelType  "
				+ "AND o.lsKind = :labelKind "
				+ "AND o.lsThing.id = :lsThingId "
				+ "AND o.preferred = :preferred AND o.ignored IS NOT :ignored";
		logger.debug("sql query " + query);
		TypedQuery<LsThingLabel> q = em.createQuery(query, LsThingLabel.class);
		q.setParameter("lsThingId", lsThingId);
		q.setParameter("labelType", labelType);
		q.setParameter("labelKind", labelKind);
		q.setParameter("preferred", preferred);
		q.setParameter("ignored", ignored);
		return q;
	}

	
	public String toJson() {
		return new JSONSerializer().exclude("*.class").serialize(this);
	}

	public static LsThingLabel fromJsonToLsThingLabel(String json) {
		return new JSONDeserializer<LsThingLabel>().use(null, LsThingLabel.class).deserialize(json);
	}

	public static String toJsonArray(Collection<LsThingLabel> collection) {
		return new JSONSerializer().exclude("*.class").serialize(collection);
	}

	public static Collection<LsThingLabel> fromJsonArrayToLsThingLabels(String json) {
		return new JSONDeserializer<List<LsThingLabel>>().use(null, ArrayList.class).use("values", LsThingLabel.class).deserialize(json);
	}

	public static Collection<LsThingLabel> fromJsonArrayToLsThingLabels(Reader json) {
		return new JSONDeserializer<List<LsThingLabel>>().use(null, ArrayList.class).use("values", LsThingLabel.class).deserialize(json);
	}

	@Transactional
	public static LsThingLabel saveLsThingLabel(LsTransaction lsTransaction, String recordedBy, LsThing lsThing, String labelType, String labelKind, boolean preferred, String label) {
		LsThingLabel lsLabel = new LsThingLabel();
		lsLabel.setLsThing(lsThing);
		lsLabel.setLsThingTypeAndKind(lsThing.getLsTypeAndKind());
		lsLabel.setLsType(labelType);
		lsLabel.setLsKind(labelKind);
		lsLabel.setPreferred(preferred);
		lsLabel.setLabelText(label);
		lsLabel.setLsTransaction(lsTransaction.getId());
		lsLabel.setRecordedBy(recordedBy);
		lsLabel.persist();
		logger.info("saving thing label: " + label);		

		return lsLabel;

	}

	@Transactional
	public static List<LsThingLabel> saveLsThingLabels(LsTransaction lsTransaction, String recordedBy, LsThing lsThing, String labelType, String labelKind, boolean preferred, String labels,  String labelSeparator) {
		List<LsThingLabel> lsLabels = new ArrayList<LsThingLabel>();
		String[] labelList = labels.split(labelSeparator);
		for (String label : labelList){
			lsLabels.add(saveLsThingLabel(lsTransaction, recordedBy, lsThing, labelType, labelKind, preferred, label));
		}
		
		return lsLabels;

	}

	public static TypedQuery<PreferredNameDTO> findLsThingPreferredName(String thingType, String thingKind, String labelType, String labelKind, Set<String> requestNameList) {
		
		EntityManager em = LsThingLabel.entityManager();
		String query = "SELECT new com.labsynch.labseer.dto.PreferredNameDTO( o.labelText as requestName, o.labelText as preferredName, lst.codeName as referenceName) " 
				+ "FROM LsThingLabel AS o "
				+ "JOIN o.lsThing lst "
				+ "WHERE o.lsType = :labelType  "
				+ "AND o.lsKind = :labelKind "
				+ "AND lst.lsType = :thingType "
				+ "AND lst.lsKind = :thingKind "
				+ "AND o.preferred = :preferred AND o.ignored IS NOT :ignored "
				+ "AND o.labelText in (:requestNameList)";
		
		logger.debug("sql query " + query);
		TypedQuery<PreferredNameDTO> q = em.createQuery(query, PreferredNameDTO.class);
		q.setParameter("labelType", labelType);
		q.setParameter("labelKind", labelKind);
		q.setParameter("thingType", thingType);
		q.setParameter("thingKind", thingKind);
		q.setParameter("requestNameList", requestNameList);
		q.setParameter("preferred", true);
		q.setParameter("ignored", true);
		return q;
	}

	
}
