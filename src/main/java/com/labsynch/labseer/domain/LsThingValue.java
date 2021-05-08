package com.labsynch.labseer.domain;

import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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

import com.labsynch.labseer.utils.ByteArrayTransformer;
import com.labsynch.labseer.utils.CustomBigDecimalFactory;
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = { "findLsThingValuesByLsState", "findLsThingValuesByLsTransactionEquals",
		 "findLsThingValuesByCodeValueEquals", "findLsThingValuesByIgnoredNotAndCodeValueEquals",
		 "findLsThingValuesByLsKindEqualsAndStringValueLike", "findLsThingValuesByLsKindEqualsAndDateValueLike",
		 "findLsThingValuesByLsKindEqualsAndCodeValueLike",
		 "findLsThingValuesByLsKindEqualsAndStringValueEquals",
		 "findLsThingValuesByLsKindEqualsAndNumericValueEquals",
		 "findLsThingValuesByLsKindEqualsAndNumericValueGreaterThanEquals",
		 "findLsThingValuesByLsKindEqualsAndNumericValueLessThanEquals",
		 "findLsThingValuesByLsKindEqualsAndDateValueGreaterThanEquals",
		 "findLsThingValuesByLsKindEqualsAndDateValueLessThanEquals"})
public class LsThingValue extends AbstractValue {

	private static final Logger logger = LoggerFactory.getLogger(LsThingValue.class);

	
    @NotNull
    @ManyToOne
    @JoinColumn(name = "lsthing_state_id")
    private LsThingState lsState;
    
    public LsThingValue(LsThingValue lsThingValue) {
        super.setBlobValue(lsThingValue.getBlobValue());
        super.setClobValue(lsThingValue.getClobValue());
        super.setCodeKind(lsThingValue.getCodeKind());
        super.setCodeOrigin(lsThingValue.getCodeOrigin());
        super.setCodeType(lsThingValue.getCodeType());
        super.setCodeTypeAndKind(lsThingValue.getCodeTypeAndKind());
        super.setCodeValue(lsThingValue.getCodeValue());
        super.setComments(lsThingValue.getComments());
        super.setConcentration(lsThingValue.getConcentration());
        super.setConcUnit(lsThingValue.getConcUnit());
        super.setDateValue(lsThingValue.getDateValue());
        super.setDeleted(lsThingValue.isDeleted());
        super.setFileValue(lsThingValue.getFileValue());
        super.setIgnored(lsThingValue.isIgnored());
        super.setLsKind(lsThingValue.getLsKind());
        super.setLsTransaction(lsThingValue.getLsTransaction());
        super.setLsType(lsThingValue.getLsType());
        super.setLsTypeAndKind(lsThingValue.getLsTypeAndKind());
        super.setModifiedBy(lsThingValue.getModifiedBy());
        super.setModifiedDate(lsThingValue.getModifiedDate());
        super.setNumberOfReplicates(lsThingValue.getNumberOfReplicates());
        super.setNumericValue(lsThingValue.getNumericValue());
        super.setOperatorKind(lsThingValue.getOperatorKind());
        super.setOperatorType(lsThingValue.getOperatorType());
        super.setOperatorTypeAndKind(lsThingValue.getOperatorTypeAndKind());
        super.setPublicData(lsThingValue.isPublicData());
        super.setRecordedBy(lsThingValue.getRecordedBy());
        super.setRecordedDate(lsThingValue.getRecordedDate());
        super.setSigFigs(lsThingValue.getSigFigs());
        super.setStringValue(lsThingValue.getStringValue());
        super.setUncertainty(lsThingValue.getUncertainty());
        super.setUncertaintyType(lsThingValue.getUncertaintyType());
        super.setUnitKind(lsThingValue.getUnitKind());
        super.setUnitType(lsThingValue.getUnitType());
        super.setUnitTypeAndKind(lsThingValue.getUnitTypeAndKind());
        super.setUrlValue(lsThingValue.getUrlValue());
        super.setVersion(lsThingValue.getVersion());
        
	}
    public LsThingValue() {
    }

	public static LsThingValue fromJsonToLsThingValue(String json) {
        return new JSONDeserializer<LsThingValue>().
        		use(null, LsThingValue.class).
        		
        		deserialize(json);
    }
        
    public static Collection<LsThingValue> fromJsonArrayToLsThingValues(String json) {
        return new JSONDeserializer<List<LsThingValue>>().
        		use(null, ArrayList.class).
        		use("values", LsThingValue.class).
        		
        		deserialize(json);
    }
    
    public static Collection<LsThingValue> fromJsonArrayToLsThingValues(Reader json) {
        return new JSONDeserializer<List<LsThingValue>>().
        		use(null, ArrayList.class).
        		use("values", LsThingValue.class).
        		
        		deserialize(json);
    }


	public static LsThingValue update(LsThingValue lsThingValue) {
		
		logger.debug("attempting to update lsThingValue: "  + lsThingValue.toJson());
		LsThingValue updatedLsThingValue = null;
		try {
			updatedLsThingValue = new JSONDeserializer<LsThingValue>().
					use(null, LsThingValue.class).
					
					deserializeInto(lsThingValue.toJson(), LsThingValue.findLsThingValue(lsThingValue.getId()));	
			LsThingState lsThingState = LsThingState.findLsThingState(lsThingValue.getLsState().getId());
			updatedLsThingValue.setLsState(lsThingState);
			updatedLsThingValue.merge();
			
		} catch (Exception e){
			logger.error("caught the error " + e);
		}
		return updatedLsThingValue;
	}

	@Transactional
	public static LsThingValue saveLsThingValue(LsTransaction lsTransaction,
			LsThingState lsThingState, String lsType, String lsKind,
			String lsValue) {

		LsThingValue newLsValue = new LsThingValue();
		newLsValue.setLsTransaction(lsTransaction.getId());
		newLsValue.setLsState(lsThingState);
		newLsValue.setLsType(lsType);
		newLsValue.setLsKind(lsKind);
		newLsValue.setStringValue(lsValue);

		return newLsValue;
		
	}

	public static LsThingValue saveLsThingValue(LsTransaction lsTransaction,
			LsThingState lsThingState, String lsType, String lsKind, Date lsValue) {

		LsThingValue newLsValue = new LsThingValue();
		newLsValue.setLsTransaction(lsTransaction.getId());
		newLsValue.setLsState(lsThingState);
		newLsValue.setLsType(lsType);
		newLsValue.setLsKind(lsKind);
		newLsValue.setDateValue(lsValue);
		
		return newLsValue;
		
	}

	public static TypedQuery<LsThingValue> findLsThingValuesByLsThingIDAndStateTypeKindAndValueTypeKind(
			Long lsThingId, String stateType, String stateKind,
			String valueType, String valueKind) {
		
		if (stateType == null || stateType.length() == 0) throw new IllegalArgumentException("The stateType argument is required");
		if (stateKind == null || stateKind.length() == 0) throw new IllegalArgumentException("The stateKind argument is required");
		if (valueType == null || valueType.length() == 0) throw new IllegalArgumentException("The valueType argument is required");
		if (valueKind == null || valueKind.length() == 0) throw new IllegalArgumentException("The valueKind argument is required");
		
		EntityManager em = entityManager();
		String hsqlQuery = "SELECT lstv FROM LsThingValue AS lstv " +
				"JOIN lstv.lsState lsts " +
				"JOIN lsts.lsThing lst " +
				"WHERE lsts.lsType = :stateType AND lsts.lsKind = :stateKind AND lsts.ignored IS NOT :ignored " +
				"AND lstv.lsType = :valueType AND lstv.lsKind = :valueKind AND lstv.ignored IS NOT :ignored " +
				"AND lst.ignored IS NOT :ignored " +
				"AND lst.id = :lsThingId ";
		TypedQuery<LsThingValue> q = em.createQuery(hsqlQuery, LsThingValue.class);
		q.setParameter("lsThingId", lsThingId);
		q.setParameter("stateType", stateType);
		q.setParameter("stateKind", stateKind);
		q.setParameter("valueType", valueType);
		q.setParameter("valueKind", valueKind);
		q.setParameter("ignored", true);
		return q;
	}
	
	public static TypedQuery<LsThingValue> findLsThingValuesByLsThingCodeNameAndStateTypeKindAndValueTypeKind(
			String lsThingCodeName, String stateType, String stateKind,
			String valueType, String valueKind) {
		
		if (stateType == null || stateType.length() == 0) throw new IllegalArgumentException("The stateType argument is required");
		if (stateKind == null || stateKind.length() == 0) throw new IllegalArgumentException("The stateKind argument is required");
		if (valueType == null || valueType.length() == 0) throw new IllegalArgumentException("The valueType argument is required");
		if (valueKind == null || valueKind.length() == 0) throw new IllegalArgumentException("The valueKind argument is required");
		
		EntityManager em = entityManager();
		String hsqlQuery = "SELECT lstv FROM LsThingValue AS lstv " +
				"JOIN lstv.lsState lsts " +
				"JOIN lsts.lsThing lst " +
				"WHERE lsts.lsType = :stateType AND lsts.lsKind = :stateKind AND lsts.ignored IS NOT :ignored " +
				"AND lstv.lsType = :valueType AND lstv.lsKind = :valueKind AND lstv.ignored IS NOT :ignored " +
				"AND lst.ignored IS NOT :ignored " +
				"AND lst.codeName = :lsThingCodeName ";
		TypedQuery<LsThingValue> q = em.createQuery(hsqlQuery, LsThingValue.class);
		q.setParameter("lsThingCodeName", lsThingCodeName);
		q.setParameter("stateType", stateType);
		q.setParameter("stateKind", stateKind);
		q.setParameter("valueType", valueType);
		q.setParameter("valueKind", valueKind);
		q.setParameter("ignored", true);
		return q;
	}
	
	public static com.labsynch.labseer.domain.LsThingValue create(com.labsynch.labseer.domain.LsThingValue lsThingValue) {
        LsThingValue newLsThingValue = new JSONDeserializer<LsThingValue>().use(null, LsThingValue.class).deserializeInto(lsThingValue.toJson(), new LsThingValue());
        return newLsThingValue;
    }
	
	public static TypedQuery<LsThingValue> findLsThingValuesByLsKindEqualsAndDateValueLike(String lsKind, Date dateValue) {
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        if (dateValue == null) throw new IllegalArgumentException("The dateValue argument is required");
        EntityManager em = LsThingValue.entityManager();
        TypedQuery<LsThingValue> q = em.createQuery("SELECT o FROM LsThingValue AS o WHERE o.lsKind = :lsKind AND o.ignored IS false AND date(o.dateValue) = CAST(:dateValue AS date) ", LsThingValue.class);
        q.setParameter("lsKind", lsKind);
        q.setParameter("dateValue", dateValue);
        return q;
    }
	public static TypedQuery<LsThingValue> findLsThingValuesByStringValueLike(
			String stringValue) {
		if (stringValue == null) throw new IllegalArgumentException("The stringValue argument is required");
        EntityManager em = LsThingValue.entityManager();
        TypedQuery<LsThingValue> q = em.createQuery("SELECT o FROM LsThingValue AS o WHERE o.ignored IS false AND o.stringValue LIKE :stringValue ", LsThingValue.class);
        q.setParameter("stringValue", stringValue);
        return q;
	}
	
	public static TypedQuery<LsThingValue> findLsThingValuesByLsKindEqualsAndCodeValueLike(String lsKind, String codeValue) {
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        if (codeValue == null || codeValue.length() == 0) throw new IllegalArgumentException("The codeValue argument is required");
        codeValue = codeValue.replace('*', '%');
        if (codeValue.charAt(0) != '%') {
            codeValue = "%" + codeValue;
        }
        if (codeValue.charAt(codeValue.length() - 1) != '%') {
            codeValue = codeValue + "%";
        }
        EntityManager em = LsThingValue.entityManager();
        TypedQuery<LsThingValue> q = em.createQuery("SELECT o FROM LsThingValue AS o WHERE o.lsKind = :lsKind AND o.ignored IS false AND LOWER(o.codeValue) LIKE LOWER(:codeValue)", LsThingValue.class);
        q.setParameter("lsKind", lsKind);
        q.setParameter("codeValue", codeValue);
        return q;
    }
    
    public static TypedQuery<LsThingValue> findLsThingValuesByLsKindEqualsAndDateValueGreaterThanEquals(String lsKind, Date dateValue) {
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        if (dateValue == null) throw new IllegalArgumentException("The dateValue argument is required");
        EntityManager em = LsThingValue.entityManager();
        TypedQuery<LsThingValue> q = em.createQuery("SELECT o FROM LsThingValue AS o WHERE o.lsKind = :lsKind AND o.ignored IS false AND o.dateValue >= :dateValue", LsThingValue.class);
        q.setParameter("lsKind", lsKind);
        q.setParameter("dateValue", dateValue);
        return q;
    }
    
    public static TypedQuery<LsThingValue> findLsThingValuesByLsKindEqualsAndDateValueLessThanEquals(String lsKind, Date dateValue) {
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        if (dateValue == null) throw new IllegalArgumentException("The dateValue argument is required");
        EntityManager em = LsThingValue.entityManager();
        TypedQuery<LsThingValue> q = em.createQuery("SELECT o FROM LsThingValue AS o WHERE o.lsKind = :lsKind AND o.ignored IS false AND o.dateValue <= :dateValue", LsThingValue.class);
        q.setParameter("lsKind", lsKind);
        q.setParameter("dateValue", dateValue);
        return q;
    }
    
    public static TypedQuery<LsThingValue> findLsThingValuesByLsKindEqualsAndNumericValueEquals(String lsKind, BigDecimal numericValue) {
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        if (numericValue == null) throw new IllegalArgumentException("The numericValue argument is required");
        EntityManager em = LsThingValue.entityManager();
        TypedQuery<LsThingValue> q = em.createQuery("SELECT o FROM LsThingValue AS o WHERE o.lsKind = :lsKind AND o.ignored IS false AND o.numericValue = :numericValue", LsThingValue.class);
        q.setParameter("lsKind", lsKind);
        q.setParameter("numericValue", numericValue);
        return q;
    }
    
    public static TypedQuery<LsThingValue> findLsThingValuesByLsKindEqualsAndNumericValueGreaterThanEquals(String lsKind, BigDecimal numericValue) {
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        if (numericValue == null) throw new IllegalArgumentException("The numericValue argument is required");
        EntityManager em = LsThingValue.entityManager();
        TypedQuery<LsThingValue> q = em.createQuery("SELECT o FROM LsThingValue AS o WHERE o.lsKind = :lsKind AND o.ignored IS false AND o.numericValue >= :numericValue", LsThingValue.class);
        q.setParameter("lsKind", lsKind);
        q.setParameter("numericValue", numericValue);
        return q;
    }
    
    public static TypedQuery<LsThingValue> findLsThingValuesByLsKindEqualsAndNumericValueLessThanEquals(String lsKind, BigDecimal numericValue) {
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        if (numericValue == null) throw new IllegalArgumentException("The numericValue argument is required");
        EntityManager em = LsThingValue.entityManager();
        TypedQuery<LsThingValue> q = em.createQuery("SELECT o FROM LsThingValue AS o WHERE o.lsKind = :lsKind AND o.ignored IS false AND o.numericValue <= :numericValue", LsThingValue.class);
        q.setParameter("lsKind", lsKind);
        q.setParameter("numericValue", numericValue);
        return q;
    }
    
    public static TypedQuery<LsThingValue> findLsThingValuesByLsKindEqualsAndStringValueLike(String lsKind, String stringValue) {
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        if (stringValue == null || stringValue.length() == 0) throw new IllegalArgumentException("The stringValue argument is required");
        stringValue = stringValue.replace('*', '%');
        if (stringValue.charAt(0) != '%') {
            stringValue = "%" + stringValue;
        }
        if (stringValue.charAt(stringValue.length() - 1) != '%') {
            stringValue = stringValue + "%";
        }
        EntityManager em = LsThingValue.entityManager();
        TypedQuery<LsThingValue> q = em.createQuery("SELECT o FROM LsThingValue AS o WHERE o.lsKind = :lsKind AND o.ignored IS false AND LOWER(o.stringValue) LIKE LOWER(:stringValue)", LsThingValue.class);
        q.setParameter("lsKind", lsKind);
        q.setParameter("stringValue", stringValue);
        return q;
    }
    
    public static TypedQuery<LsThingValue> findLsThingValuesByLsKindEqualsAndStringValueEqualsIgnoreCase(String lsKind, String stringValue) {
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        if (stringValue == null || stringValue.length() == 0) throw new IllegalArgumentException("The stringValue argument is required");
        EntityManager em = LsThingValue.entityManager();
        TypedQuery<LsThingValue> q = em.createQuery("SELECT o FROM LsThingValue AS o WHERE o.lsKind = :lsKind AND o.ignored IS false AND LOWER(o.stringValue) = LOWER(:stringValue)", LsThingValue.class);
        q.setParameter("lsKind", lsKind);
        q.setParameter("stringValue", stringValue);
        return q;
    }
    
    public static TypedQuery<LsThingValue> findLsThingValuesByLsKindEqualsAndStringValueEquals(String lsKind, String stringValue) {
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        if (stringValue == null || stringValue.length() == 0) throw new IllegalArgumentException("The stringValue argument is required");
        EntityManager em = LsThingValue.entityManager();
        TypedQuery<LsThingValue> q = em.createQuery("SELECT o FROM LsThingValue AS o WHERE o.lsKind = :lsKind AND o.ignored IS false AND o.stringValue = :stringValue", LsThingValue.class);
        q.setParameter("lsKind", lsKind);
        q.setParameter("stringValue", stringValue);
        return q;
    }
    
    public static TypedQuery<LsThingValue> findLsThingValuesByTypeKindFullPath(
			String thingType, String thingKind,
			String stateType, String stateKind,
			String valueType, String valueKind) {
    	if (thingType == null || thingType.length() == 0) throw new IllegalArgumentException("The thingType argument is required");
		if (thingKind == null || thingKind.length() == 0) throw new IllegalArgumentException("The thingKind argument is required");
		if (stateType == null || stateType.length() == 0) throw new IllegalArgumentException("The stateType argument is required");
		if (stateKind == null || stateKind.length() == 0) throw new IllegalArgumentException("The stateKind argument is required");
		if (valueType == null || valueType.length() == 0) throw new IllegalArgumentException("The valueType argument is required");
		if (valueKind == null || valueKind.length() == 0) throw new IllegalArgumentException("The valueKind argument is required");
		
		EntityManager em = entityManager();
		String hsqlQuery = "SELECT lstv FROM LsThingValue AS lstv " +
				"JOIN lstv.lsState lsts " +
				"JOIN lsts.lsThing lst " +
				"WHERE lst.lsType = :thingType AND lst.lsKind = :thingKind AND lst.ignored IS NOT :ignored " +
				"AND lsts.lsType = :stateType AND lsts.lsKind = :stateKind AND lsts.ignored IS NOT :ignored " +
				"AND lstv.lsType = :valueType AND lstv.lsKind = :valueKind AND lstv.ignored IS NOT :ignored "
				;
		TypedQuery<LsThingValue> q = em.createQuery(hsqlQuery, LsThingValue.class);
		q.setParameter("thingType", thingType);
		q.setParameter("thingKind", thingKind);
		q.setParameter("stateType", stateType);
		q.setParameter("stateKind", stateKind);
		q.setParameter("valueType", valueType);
		q.setParameter("valueKind", valueKind);
		q.setParameter("ignored", true);
		return q;
	}

    @Transactional
    public String toJsonWithBlobValue() {
        return new JSONSerializer().
                exclude("*.class").
                include("blobValue").
                transform(new ByteArrayTransformer(), "blobValue").
                serialize(this);
     }
}
