package com.labsynch.labseer.domain;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.dto.PreferredNameDTO;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Configurable
@Entity

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
	
	public static Long countOfLsThingLabelsByLabel(LsThing lsThing, String labelType, String labelKind, String labelText) {
		if (lsThing == null ) throw new IllegalArgumentException("The lsThing argument is required");
		if (labelType == null || labelType.length() == 0) throw new IllegalArgumentException("The labelType argument is required");
		if (labelKind == null || labelKind.length() == 0) throw new IllegalArgumentException("The labelKind argument is required");	
		if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");

		boolean ignored = true;

		EntityManager em = LsThingLabel.entityManager();
		String query = "SELECT count(DISTINCT o) " 
				+ "FROM LsThingLabel AS o "
				+ "WHERE o.lsThing = :lsThing "
				+ "AND o.lsType = :labelType  "
				+ "AND o.lsKind = :labelKind "
				+ "AND o.ignored IS NOT :ignored "
				+ "AND o.labelText = :labelText ";
		logger.debug("sql query " + query);
		TypedQuery<Long> q = em.createQuery(query, Long.class);

		q.setParameter("lsThing", lsThing);
		q.setParameter("labelType", labelType);
		q.setParameter("labelKind", labelKind);
		q.setParameter("labelText", labelText);
		q.setParameter("ignored", ignored);
		return q.getSingleResult();
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
	
	public static Long countOfLsThingByPreferredName(String thingType, String thingKind, String labelType, String labelKind, String labelText) {
		if (thingType == null || thingType.length() == 0) throw new IllegalArgumentException("The thingType argument is required");
		if (thingKind == null || thingKind.length() == 0) throw new IllegalArgumentException("The thingKind argument is required");		
		if (labelType == null || labelType.length() == 0) throw new IllegalArgumentException("The labelType argument is required");
		if (labelKind == null || labelKind.length() == 0) throw new IllegalArgumentException("The labelKind argument is required");	
		if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");

		boolean ignored = true;
		boolean preferred = true;

		EntityManager em = LsThingLabel.entityManager();
		String query = "SELECT count(o) FROM LsThing AS o " +
				"JOIN o.lsLabels AS ll1 with ll1.ignored IS NOT :ignored AND ll1.labelText = :labelText "
				+ "AND ll1.lsType = :labelType AND ll1.lsKind = :labelKind AND ll1.preferred is :preferred " +
				"WHERE o.ignored IS NOT :ignored AND o.lsType = :thingType AND o.lsKind = :thingKind ";
		logger.debug("sql query " + query);
		TypedQuery<Long> q = em.createQuery(query, Long.class);

		q.setParameter("thingType", thingType);
		q.setParameter("thingKind", thingKind);
		q.setParameter("labelType", labelType);
		q.setParameter("labelKind", labelKind);
		q.setParameter("labelText", labelText);
		q.setParameter("ignored", ignored);
		q.setParameter("preferred", preferred);

		return q.getSingleResult();
	}
	
	
	public static Long countOfAllLsThingByPreferredName(String thingType, String thingKind, String labelType, String labelKind, String labelText) {
		if (thingType == null || thingType.length() == 0) throw new IllegalArgumentException("The thingType argument is required");
		if (thingKind == null || thingKind.length() == 0) throw new IllegalArgumentException("The thingKind argument is required");		
		if (labelType == null || labelType.length() == 0) throw new IllegalArgumentException("The labelType argument is required");
		if (labelKind == null || labelKind.length() == 0) throw new IllegalArgumentException("The labelKind argument is required");	
		if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");

		boolean ignored = true;
		boolean preferred = true;

		EntityManager em = LsThingLabel.entityManager();
		String query = "SELECT count(o) FROM LsThing AS o " +
				"JOIN o.lsLabels AS ll1 " +
				"WHERE o.lsType = :thingType AND o.lsKind = :thingKind " +
				"AND ll1.lsType = :labelType AND ll1.lsKind = :labelKind AND ll1.preferred is :preferred " +
				"AND ll1.ignored IS NOT :ignored AND ll1.labelText = :labelText ";
		logger.debug("sql query " + query);
		TypedQuery<Long> q = em.createQuery(query, Long.class);

		q.setParameter("thingType", thingType);
		q.setParameter("thingKind", thingKind);
		q.setParameter("labelType", labelType);
		q.setParameter("labelKind", labelKind);
		q.setParameter("labelText", labelText);
		q.setParameter("ignored", ignored);
		q.setParameter("preferred", preferred);

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
	
	public static TypedQuery<LsThingLabel> findLsThingAllPreferredName(Long lsThingId, String labelType, String labelKind) {
		if (lsThingId == null || lsThingId == 0) throw new IllegalArgumentException("The lsThingId argument is required");
		if (labelType == null || labelType.length() == 0) throw new IllegalArgumentException("The labelType argument is required");
		if (labelKind == null || labelKind.length() == 0) throw new IllegalArgumentException("The labelKind argument is required");
		
		boolean preferred = true;

		EntityManager em = LsThingLabel.entityManager();
		String query = "SELECT DISTINCT o FROM LsThingLabel AS o WHERE o.lsType = :labelType  "
				+ "AND o.lsKind = :labelKind "
				+ "AND o.lsThing.id = :lsThingId "
				+ "AND o.preferred = :preferred";
		logger.debug("sql query " + query);
		TypedQuery<LsThingLabel> q = em.createQuery(query, LsThingLabel.class);
		q.setParameter("lsThingId", lsThingId);
		q.setParameter("labelType", labelType);
		q.setParameter("labelKind", labelKind);
		q.setParameter("preferred", preferred);
		return q;
	}
	
	public static TypedQuery<LsThingLabel> findLsThingPreferredName(
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

	public static TypedQuery<PreferredNameDTO> findLsThingPreferredName(String thingType, String thingKind, String labelType, String labelKind, Collection<String> requestNameList) {
		
		EntityManager em = LsThingLabel.entityManager();
		String query = "SELECT new com.labsynch.labseer.dto.PreferredNameDTO( "
				+ "tl1.labelText as requestName, tl2.labelText as preferredName, lst.codeName as referenceName) " 
				+ "FROM LsThing AS lst "
				+ "JOIN lst.lsLabels tl1 "
				+ "JOIN lst.lsLabels tl2 "
				+ "WHERE tl1.lsType = :labelType "
				+ "AND tl1.lsKind = :labelKind "
				+ "AND tl1.ignored IS NOT :ignored "
				+ "AND tl2.lsType = :labelType "
				+ "AND tl2.lsKind = :labelKind "
				+ "AND tl2.preferred = :preferred AND tl2.ignored IS NOT :ignored "
				+ "AND lst.lsType = :thingType "
				+ "AND lst.lsKind = :thingKind "
				+ "AND lst.ignored IS NOT :ignored  "
				+ "AND tl1.labelText in (:requestNameList) ";
		
//		+ "FROM LsThingLabel AS o "
//		+ ", LsThingLabel AS o2 "
//		+ "JOIN o.lsThing lst "
//		+ "WHERE o.lsType = :labelType  "
//		+ "AND o.lsKind = :labelKind "
//		+ "AND lst.lsType = :thingType "
//		+ "AND lst.lsKind = :thingKind "
//		+ "AND lst.ignored IS NOT :ignored  "
//		+ "AND o.preferred = :preferred AND o.ignored IS NOT :ignored "
//		+ "AND o.labelText in (:requestNameList) "
//		+ "AND o.lsThing = o2.lsThing "
//		+ "AND o2.preferred != :preferred "
//		+ "AND o2.lsType = :labelType  "
//		+ "AND o2.lsKind = :labelKind ";
		
//		select tl2.label_text as reference, tl1.label_text as other, tl1.preferred
//		from ls_thing lst
//		join  ls_thing_label tl1 on lst.id = tl1.lsthing_id
//		join  ls_thing_label tl2 on lst.id = tl2.lsthing_id AND tl2.preferred = 1
//		where tl1.label_text in ('380653','380654') ;		
		
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
	
public static TypedQuery<PreferredNameDTO> findLsThingPreferredName(Set<String> requestNameList) {
	
		logger.info("in the correct sql generation");
		
		EntityManager em = LsThingLabel.entityManager();
		String query = "SELECT new com.labsynch.labseer.dto.PreferredNameDTO( o.labelText as requestName, o2.labelText as preferredName, lst.codeName as referenceName) " 
				+ "FROM LsThingLabel AS o, LsThingLabel AS o2 "
				+ "JOIN o.lsThing lst "
				+ "JOIN o2.lsThing lst2 "
//				+ "JOIN o o2"
				+ "WHERE o.ignored IS NOT :ignored AND o.labelText in (:requestNameList) AND lst.codeName = lst2.codeName "
				+ "AND o2.preferred = :preferred AND o2.ignored IS NOT :ignored";
		
		logger.debug("sql query " + query);
		TypedQuery<PreferredNameDTO> q = em.createQuery(query, PreferredNameDTO.class);
		q.setParameter("requestNameList", requestNameList);
		q.setParameter("preferred", true);
		q.setParameter("ignored", true);
		return q;
	}
	
	public static LsThingLabel pickBestLabel(Collection<LsThingLabel> labels) {
		if (labels.isEmpty()) return null;
		Collection<LsThingLabel> preferredLabels = new HashSet<LsThingLabel>();
		for (LsThingLabel label : labels){
			if (label.isPreferred()) preferredLabels.add(label);
		}
		if (!preferredLabels.isEmpty()){
			LsThingLabel bestLabel = preferredLabels.iterator().next();
			for (LsThingLabel preferredLabel : preferredLabels){
				if (preferredLabel.getRecordedDate().compareTo(bestLabel.getRecordedDate()) > 0) bestLabel = preferredLabel;
			}
			return bestLabel;
		} else {
			Collection<LsThingLabel> nameLabels = new HashSet<LsThingLabel>();
			for (LsThingLabel label : labels){
				if (label.getLsType().equals("name")) nameLabels.add(label);
			}
			if (!nameLabels.isEmpty()){
				LsThingLabel bestLabel = nameLabels.iterator().next();
				for (LsThingLabel nameLabel : nameLabels){
					if (nameLabel.getRecordedDate().compareTo(bestLabel.getRecordedDate()) > 0) bestLabel = nameLabel;
				}
				return bestLabel;
			} else {
				Collection<LsThingLabel> notIgnoredLabels = new HashSet<LsThingLabel>();
				for (LsThingLabel label : labels){
					if (!label.isIgnored()) notIgnoredLabels.add(label);
				}
				if (!notIgnoredLabels.isEmpty()){
					LsThingLabel bestLabel = notIgnoredLabels.iterator().next();
					for (LsThingLabel notIgnoredLabel : notIgnoredLabels){
						if (notIgnoredLabel.getRecordedDate().compareTo(bestLabel.getRecordedDate()) > 0) bestLabel = notIgnoredLabel;
					}
					return bestLabel;
				} else {
					return labels.iterator().next();
				}
			}
		}
	}

	public static TypedQuery<LsThingLabel> findLsThingLabelsByLsThingAndLsTypeAndLsKindAndLabelText(
			LsThing lsThing, String labelType, String labelKind, String labelText) {
		
		if (lsThing == null ) throw new IllegalArgumentException("The lsThing argument is required");
		if (labelType == null || labelType.length() == 0) throw new IllegalArgumentException("The labelType argument is required");
		if (labelKind == null || labelKind.length() == 0) throw new IllegalArgumentException("The labelKind argument is required");
		if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");
		
		EntityManager em = LsThingLabel.entityManager();
		String query = "SELECT DISTINCT o FROM LsThingLabel AS o WHERE o.lsType = :labelType  "
				+ "AND o.lsKind = :labelKind "
				+ "AND o.lsThing = :lsThing "
				+ "AND o.labelText = :labelText "
				+ "AND o.ignored IS NOT :ignored "
				+ "ORDER by o.id ";
		logger.debug("sql query " + query);
		TypedQuery<LsThingLabel> q = em.createQuery(query, LsThingLabel.class);
		q.setParameter("lsThing", lsThing);
		q.setParameter("labelType", labelType);
		q.setParameter("labelKind", labelKind);
		q.setParameter("labelText", labelText);
		q.setParameter("ignored", true);

		return q;
	}
	
	public static TypedQuery<LsThingLabel> findAllLsThingLabelsByLsThingAndLsTypeAndLsKindAndLabelText(
			LsThing lsThing, String labelType, String labelKind, String labelText) {
		
		if (lsThing == null ) throw new IllegalArgumentException("The lsThing argument is required");
		if (labelType == null || labelType.length() == 0) throw new IllegalArgumentException("The labelType argument is required");
		if (labelKind == null || labelKind.length() == 0) throw new IllegalArgumentException("The labelKind argument is required");
		if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");
		
		EntityManager em = LsThingLabel.entityManager();
		String query = "SELECT DISTINCT o FROM LsThingLabel AS o WHERE o.lsType = :labelType  "
				+ "AND o.lsKind = :labelKind "
				+ "AND o.lsThing = :lsThing "
				+ "AND o.labelText = :labelText "
				+ "ORDER by o.id ";
		logger.debug("sql query " + query);
		TypedQuery<LsThingLabel> q = em.createQuery(query, LsThingLabel.class);
		q.setParameter("lsThing", lsThing);
		q.setParameter("labelType", labelType);
		q.setParameter("labelKind", labelKind);
		q.setParameter("labelText", labelText);

		return q;
	}

	public static TypedQuery<LsThingLabel> findLsThingLabelsByLsThingAndLsTypeAndLsKind(
			LsThing lsThing, String labelType, String labelKind) {
		if (lsThing == null ) throw new IllegalArgumentException("The lsThing argument is required");
		if (labelType == null || labelType.length() == 0) throw new IllegalArgumentException("The labelType argument is required");
		if (labelKind == null || labelKind.length() == 0) throw new IllegalArgumentException("The labelKind argument is required");
		
		EntityManager em = LsThingLabel.entityManager();
		String query = "SELECT DISTINCT o FROM LsThingLabel AS o WHERE o.lsType = :labelType  "
				+ "AND o.lsKind = :labelKind "
				+ "AND o.lsThing = :lsThing "
				+ "ORDER by o.id ";
		logger.debug("sql query " + query);
		TypedQuery<LsThingLabel> q = em.createQuery(query, LsThingLabel.class);
		q.setParameter("lsThing", lsThing);
		q.setParameter("labelType", labelType);
		q.setParameter("labelKind", labelKind);

		return q;
	}

	public static TypedQuery<LsThingLabel> findLsThingLabelsByLsThingAndLsTypeAndLsKindNotIgnored(
			LsThing lsThing, String labelType, String labelKind, boolean ignored) {
		if (lsThing == null ) throw new IllegalArgumentException("The lsThing argument is required");
		if (labelType == null || labelType.length() == 0) throw new IllegalArgumentException("The labelType argument is required");
		if (labelKind == null || labelKind.length() == 0) throw new IllegalArgumentException("The labelKind argument is required");
		
		EntityManager em = LsThingLabel.entityManager();
		String query = "SELECT DISTINCT o FROM LsThingLabel AS o WHERE o.lsType = :labelType  "
				+ "AND o.lsKind = :labelKind "
				+ "AND o.lsThing = :lsThing "
				+ "AND o.ignored IS NOT :ignored "
				+ "ORDER by o.id ";
		logger.debug("sql query " + query);
		TypedQuery<LsThingLabel> q = em.createQuery(query, LsThingLabel.class);
		q.setParameter("lsThing", lsThing);
		q.setParameter("labelType", labelType);
		q.setParameter("labelKind", labelKind);
		q.setParameter("ignored", ignored);

		return q;
	}


	

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public LsThing getLsThing() {
        return this.lsThing;
    }

	public void setLsThing(LsThing lsThing) {
        this.lsThing = lsThing;
    }

	public String getLsThingTypeAndKind() {
        return this.lsThingTypeAndKind;
    }

	public void setLsThingTypeAndKind(String lsThingTypeAndKind) {
        this.lsThingTypeAndKind = lsThingTypeAndKind;
    }

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "lsThing", "lsThingTypeAndKind");

	public static long countLsThingLabels() {
        return entityManager().createQuery("SELECT COUNT(o) FROM LsThingLabel o", Long.class).getSingleResult();
    }

	public static List<LsThingLabel> findAllLsThingLabels() {
        return entityManager().createQuery("SELECT o FROM LsThingLabel o", LsThingLabel.class).getResultList();
    }

	public static List<LsThingLabel> findAllLsThingLabels(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM LsThingLabel o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, LsThingLabel.class).getResultList();
    }

	public static LsThingLabel findLsThingLabel(Long id) {
        if (id == null) return null;
        return entityManager().find(LsThingLabel.class, id);
    }

	public static List<LsThingLabel> findLsThingLabelEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM LsThingLabel o", LsThingLabel.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<LsThingLabel> findLsThingLabelEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM LsThingLabel o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, LsThingLabel.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public LsThingLabel merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        LsThingLabel merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static Long countFindLsThingLabelsByLabelTextEquals(String labelText) {
        if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");
        EntityManager em = LsThingLabel.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM LsThingLabel AS o WHERE o.labelText = :labelText", Long.class);
        q.setParameter("labelText", labelText);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindLsThingLabelsByLabelTextEqualsAndIgnoredNot(String labelText, boolean ignored) {
        if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");
        EntityManager em = LsThingLabel.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM LsThingLabel AS o WHERE o.labelText = :labelText  AND o.ignored IS NOT :ignored", Long.class);
        q.setParameter("labelText", labelText);
        q.setParameter("ignored", ignored);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindLsThingLabelsByLabelTextLike(String labelText) {
        if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");
        labelText = labelText.replace('*', '%');
        if (labelText.charAt(0) != '%') {
            labelText = "%" + labelText;
        }
        if (labelText.charAt(labelText.length() - 1) != '%') {
            labelText = labelText + "%";
        }
        EntityManager em = LsThingLabel.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM LsThingLabel AS o WHERE LOWER(o.labelText) LIKE LOWER(:labelText)", Long.class);
        q.setParameter("labelText", labelText);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindLsThingLabelsByLsThing(LsThing lsThing) {
        if (lsThing == null) throw new IllegalArgumentException("The lsThing argument is required");
        EntityManager em = LsThingLabel.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM LsThingLabel AS o WHERE o.lsThing = :lsThing", Long.class);
        q.setParameter("lsThing", lsThing);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindLsThingLabelsByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = LsThingLabel.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM LsThingLabel AS o WHERE o.lsTransaction = :lsTransaction", Long.class);
        q.setParameter("lsTransaction", lsTransaction);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<LsThingLabel> findLsThingLabelsByLabelTextEquals(String labelText) {
        if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");
        EntityManager em = LsThingLabel.entityManager();
        TypedQuery<LsThingLabel> q = em.createQuery("SELECT o FROM LsThingLabel AS o WHERE o.labelText = :labelText", LsThingLabel.class);
        q.setParameter("labelText", labelText);
        return q;
    }

	public static TypedQuery<LsThingLabel> findLsThingLabelsByLabelTextEquals(String labelText, String sortFieldName, String sortOrder) {
        if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");
        EntityManager em = LsThingLabel.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM LsThingLabel AS o WHERE o.labelText = :labelText");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<LsThingLabel> q = em.createQuery(queryBuilder.toString(), LsThingLabel.class);
        q.setParameter("labelText", labelText);
        return q;
    }

	public static TypedQuery<LsThingLabel> findLsThingLabelsByLabelTextEqualsAndIgnoredNot(String labelText, boolean ignored) {
        if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");
        EntityManager em = LsThingLabel.entityManager();
        TypedQuery<LsThingLabel> q = em.createQuery("SELECT o FROM LsThingLabel AS o WHERE o.labelText = :labelText  AND o.ignored IS NOT :ignored", LsThingLabel.class);
        q.setParameter("labelText", labelText);
        q.setParameter("ignored", ignored);
        return q;
    }

	public static TypedQuery<LsThingLabel> findLsThingLabelsByLabelTextEqualsAndIgnoredNot(String labelText, boolean ignored, String sortFieldName, String sortOrder) {
        if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");
        EntityManager em = LsThingLabel.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM LsThingLabel AS o WHERE o.labelText = :labelText  AND o.ignored IS NOT :ignored");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<LsThingLabel> q = em.createQuery(queryBuilder.toString(), LsThingLabel.class);
        q.setParameter("labelText", labelText);
        q.setParameter("ignored", ignored);
        return q;
    }

	public static TypedQuery<LsThingLabel> findLsThingLabelsByLabelTextLike(String labelText) {
        if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");
        labelText = labelText.replace('*', '%');
        if (labelText.charAt(0) != '%') {
            labelText = "%" + labelText;
        }
        if (labelText.charAt(labelText.length() - 1) != '%') {
            labelText = labelText + "%";
        }
        EntityManager em = LsThingLabel.entityManager();
        TypedQuery<LsThingLabel> q = em.createQuery("SELECT o FROM LsThingLabel AS o WHERE LOWER(o.labelText) LIKE LOWER(:labelText)", LsThingLabel.class);
        q.setParameter("labelText", labelText);
        return q;
    }

	public static TypedQuery<LsThingLabel> findLsThingLabelsByLabelTextLike(String labelText, String sortFieldName, String sortOrder) {
        if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");
        labelText = labelText.replace('*', '%');
        if (labelText.charAt(0) != '%') {
            labelText = "%" + labelText;
        }
        if (labelText.charAt(labelText.length() - 1) != '%') {
            labelText = labelText + "%";
        }
        EntityManager em = LsThingLabel.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM LsThingLabel AS o WHERE LOWER(o.labelText) LIKE LOWER(:labelText)");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<LsThingLabel> q = em.createQuery(queryBuilder.toString(), LsThingLabel.class);
        q.setParameter("labelText", labelText);
        return q;
    }

	public static TypedQuery<LsThingLabel> findLsThingLabelsByLsThing(LsThing lsThing) {
        if (lsThing == null) throw new IllegalArgumentException("The lsThing argument is required");
        EntityManager em = LsThingLabel.entityManager();
        TypedQuery<LsThingLabel> q = em.createQuery("SELECT o FROM LsThingLabel AS o WHERE o.lsThing = :lsThing", LsThingLabel.class);
        q.setParameter("lsThing", lsThing);
        return q;
    }

	public static TypedQuery<LsThingLabel> findLsThingLabelsByLsThing(LsThing lsThing, String sortFieldName, String sortOrder) {
        if (lsThing == null) throw new IllegalArgumentException("The lsThing argument is required");
        EntityManager em = LsThingLabel.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM LsThingLabel AS o WHERE o.lsThing = :lsThing");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<LsThingLabel> q = em.createQuery(queryBuilder.toString(), LsThingLabel.class);
        q.setParameter("lsThing", lsThing);
        return q;
    }

	public static TypedQuery<LsThingLabel> findLsThingLabelsByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = LsThingLabel.entityManager();
        TypedQuery<LsThingLabel> q = em.createQuery("SELECT o FROM LsThingLabel AS o WHERE o.lsTransaction = :lsTransaction", LsThingLabel.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }

	public static TypedQuery<LsThingLabel> findLsThingLabelsByLsTransactionEquals(Long lsTransaction, String sortFieldName, String sortOrder) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = LsThingLabel.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM LsThingLabel AS o WHERE o.lsTransaction = :lsTransaction");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<LsThingLabel> q = em.createQuery(queryBuilder.toString(), LsThingLabel.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }
}
