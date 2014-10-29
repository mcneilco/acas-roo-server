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

import com.labsynch.labseer.utils.CustomBigDecimalFactory;

import flexjson.JSONDeserializer;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = { "findLsThingValuesByLsState", "findLsThingValuesByLsTransactionEquals",
		 "findLsThingValuesByCodeValueEquals", "findLsThingValuesByIgnoredNotAndCodeValueEquals"})
public class LsThingValue extends AbstractValue {

	private static final Logger logger = LoggerFactory.getLogger(LsThingValue.class);

	
    @NotNull
    @ManyToOne
    @JoinColumn(name = "lsthing_state_id")
    private LsThingState lsState;
    
    public static LsThingValue fromJsonToLsThingValue(String json) {
        return new JSONDeserializer<LsThingValue>().
        		use(null, LsThingValue.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).
        		deserialize(json);
    }
        
    public static Collection<LsThingValue> fromJsonArrayToLsThingValues(String json) {
        return new JSONDeserializer<List<LsThingValue>>().
        		use(null, ArrayList.class).
        		use("values", LsThingValue.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).
        		deserialize(json);
    }
    
    public static Collection<LsThingValue> fromJsonArrayToLsThingValues(Reader json) {
        return new JSONDeserializer<List<LsThingValue>>().
        		use(null, ArrayList.class).
        		use("values", LsThingValue.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).
        		deserialize(json);
    }


	public static LsThingValue update(LsThingValue lsThingValue) {
		LsThingValue updatedLsThingValue = new JSONDeserializer<LsThingValue>().
		use(null, LsThingValue.class).
		use(BigDecimal.class, new CustomBigDecimalFactory()).
		deserializeInto(lsThingValue.toJson(), LsThingValue.findLsThingValue(lsThingValue.getId()));
		updatedLsThingValue.merge();
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
}
