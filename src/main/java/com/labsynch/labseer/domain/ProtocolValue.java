package com.labsynch.labseer.domain;

import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.utils.CustomBigDecimalFactory;

import flexjson.JSONDeserializer;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = { "findProtocolValuesByLsState", "findProtocolValuesByLsTransactionEquals", "findProtocolValuesByLsKindEqualsAndStringValueLike", "findProtocolValuesByLsKindEqualsAndCodeValueLike" })
public class ProtocolValue extends AbstractValue {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "protocol_state_id")
    private ProtocolState lsState;

    public ProtocolValue(ProtocolValue protocolValue) {
            super.setBlobValue(protocolValue.getBlobValue());
            super.setClobValue(protocolValue.getClobValue());
            super.setCodeKind(protocolValue.getCodeKind());
            super.setCodeOrigin(protocolValue.getCodeOrigin());
            super.setCodeType(protocolValue.getCodeType());
            super.setCodeTypeAndKind(protocolValue.getCodeTypeAndKind());
            super.setCodeValue(protocolValue.getCodeValue());
            super.setComments(protocolValue.getComments());
            super.setConcentration(protocolValue.getConcentration());
            super.setConcUnit(protocolValue.getConcUnit());
            super.setDateValue(protocolValue.getDateValue());
            super.setDeleted(protocolValue.isDeleted());
            super.setFileValue(protocolValue.getFileValue());
            super.setIgnored(protocolValue.isIgnored());
            super.setLsKind(protocolValue.getLsKind());
            super.setLsTransaction(protocolValue.getLsTransaction());
            super.setLsType(protocolValue.getLsType());
            super.setLsTypeAndKind(protocolValue.getLsTypeAndKind());
            super.setModifiedBy(protocolValue.getModifiedBy());
            super.setModifiedDate(protocolValue.getModifiedDate());
            super.setNumberOfReplicates(protocolValue.getNumberOfReplicates());
            super.setNumericValue(protocolValue.getNumericValue());
            super.setOperatorKind(protocolValue.getOperatorKind());
            super.setOperatorType(protocolValue.getOperatorType());
            super.setOperatorTypeAndKind(protocolValue.getOperatorTypeAndKind());
            super.setPublicData(protocolValue.isPublicData());
            super.setRecordedBy(protocolValue.getRecordedBy());
            super.setRecordedDate(protocolValue.getRecordedDate());
            super.setSigFigs(protocolValue.getSigFigs());
            super.setStringValue(protocolValue.getStringValue());
            super.setUncertainty(protocolValue.getUncertainty());
            super.setUncertaintyType(protocolValue.getUncertaintyType());
            super.setUnitKind(protocolValue.getUnitKind());
            super.setUnitType(protocolValue.getUnitType());
            super.setUnitTypeAndKind(protocolValue.getUnitTypeAndKind());
            super.setUrlValue(protocolValue.getUrlValue());
            super.setVersion(protocolValue.getVersion());
	}

	public static com.labsynch.labseer.domain.ProtocolValue fromJsonToProtocolValue(String json) {
        return new JSONDeserializer<ProtocolValue>().use(null, ProtocolValue.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
    }

    public static Collection<com.labsynch.labseer.domain.ProtocolValue> fromJsonArrayToProtocolValues(String json) {
        return new JSONDeserializer<List<ProtocolValue>>().use(null, ArrayList.class).use("values", ProtocolValue.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
    }

    public static Collection<com.labsynch.labseer.domain.ProtocolValue> fromJsonArrayToProtocolValues(Reader json) {
        return new JSONDeserializer<List<ProtocolValue>>().use(null, ArrayList.class).use("values", ProtocolValue.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
    }

    public static com.labsynch.labseer.domain.ProtocolValue update(com.labsynch.labseer.domain.ProtocolValue protocolValue) {
        ProtocolValue updatedProtocolValue = new JSONDeserializer<ProtocolValue>().use(null, ProtocolValue.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserializeInto(protocolValue.toJson(), ProtocolValue.findProtocolValue(protocolValue.getId()));
        updatedProtocolValue.merge();
        return updatedProtocolValue;
    }

	public static TypedQuery<ProtocolValue> findProtocolValuesByProtocolIDAndStateTypeKindAndValueTypeKind(
			Long protocolId, String stateType, String stateKind,
			String valueType, String valueKind) {

		if (stateType == null || stateType.length() == 0) throw new IllegalArgumentException("The stateType argument is required");
		if (stateKind == null || stateKind.length() == 0) throw new IllegalArgumentException("The stateKind argument is required");
		if (valueType == null || valueType.length() == 0) throw new IllegalArgumentException("The valueType argument is required");
		if (valueKind == null || valueKind.length() == 0) throw new IllegalArgumentException("The valueKind argument is required");
		
		EntityManager em = entityManager();
		String hsqlQuery = "SELECT pv FROM ProtocolValue AS pv " +
				"JOIN pv.lsState ps " +
				"JOIN ps.protocol p " +
				"WHERE ps.lsType = :stateType AND ps.lsKind = :stateKind AND ps.ignored IS NOT :ignored " +
				"AND pv.lsType = :valueType AND pv.lsKind = :valueKind AND pv.ignored IS NOT :ignored " +
//				"AND p.ignored IS NOT :ignored " +
				"AND p.id = :protocolId ORDER BY pv.id";
		TypedQuery<ProtocolValue> q = em.createQuery(hsqlQuery, ProtocolValue.class);
		q.setParameter("protocolId", protocolId);
		q.setParameter("stateType", stateType);
		q.setParameter("stateKind", stateKind);
		q.setParameter("valueType", valueType);
		q.setParameter("valueKind", valueKind);
		q.setParameter("ignored", true);
		return q;
	}
	
	 public static TypedQuery<ProtocolValue> findProtocolValuesByLsKindEqualsAndDateValueLike(String lsKind, Date dateValue) {
	        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
	        if (dateValue == null) throw new IllegalArgumentException("The dateValue argument is required");
	        EntityManager em = ProtocolValue.entityManager();
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(dateValue);
	        cal.add(Calendar.DATE, -1);
	        Date beforeDate = cal.getTime();
	        cal.setTime(dateValue);
	        cal.add(Calendar.DATE, 1);
	        Date afterDate = cal.getTime();
	        TypedQuery<ProtocolValue> q = em.createQuery("SELECT o FROM ProtocolValue AS o WHERE o.lsKind = :lsKind AND o.ignored = false AND o.dateValue > :beforeDate AND o.dateValue < :afterDate ", ProtocolValue.class);
	        q.setParameter("lsKind", lsKind);
	        q.setParameter("beforeDate", beforeDate);
	        q.setParameter("afterDate", afterDate);
	        
	        return q;
	    }
}
