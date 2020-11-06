package com.labsynch.labseer.domain;

import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;
import com.labsynch.labseer.utils.CustomBigDecimalFactory;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Entity
@Configurable
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
        return new JSONDeserializer<ProtocolValue>().use(null, ProtocolValue.class).deserialize(json);
    }

    public static Collection<com.labsynch.labseer.domain.ProtocolValue> fromJsonArrayToProtocolValues(String json) {
        return new JSONDeserializer<List<ProtocolValue>>().use(null, ArrayList.class).use("values", ProtocolValue.class).deserialize(json);
    }

    public static Collection<com.labsynch.labseer.domain.ProtocolValue> fromJsonArrayToProtocolValues(Reader json) {
        return new JSONDeserializer<List<ProtocolValue>>().use(null, ArrayList.class).use("values", ProtocolValue.class).deserialize(json);
    }

    public static com.labsynch.labseer.domain.ProtocolValue update(com.labsynch.labseer.domain.ProtocolValue protocolValue) {
        ProtocolValue updatedProtocolValue = new JSONDeserializer<ProtocolValue>().use(null, ProtocolValue.class).deserializeInto(protocolValue.toJson(), ProtocolValue.findProtocolValue(protocolValue.getId()));
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
	
	public static TypedQuery<ProtocolValue> findProtocolValuesByProtocolCodeNameAndStateTypeKindAndValueTypeKind(
			String protocolCodeName, String stateType, String stateKind,
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
				"AND p.codeName = :protocolCodeName ";
		TypedQuery<ProtocolValue> q = em.createQuery(hsqlQuery, ProtocolValue.class);
		q.setParameter("protocolCodeName", protocolCodeName);
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

	public ProtocolValue() {
        super();
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static String toJsonArray(Collection<ProtocolValue> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<ProtocolValue> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("lsState");

	public static long countProtocolValues() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ProtocolValue o", Long.class).getSingleResult();
    }

	public static List<ProtocolValue> findAllProtocolValues() {
        return entityManager().createQuery("SELECT o FROM ProtocolValue o", ProtocolValue.class).getResultList();
    }

	public static List<ProtocolValue> findAllProtocolValues(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ProtocolValue o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ProtocolValue.class).getResultList();
    }

	public static ProtocolValue findProtocolValue(Long id) {
        if (id == null) return null;
        return entityManager().find(ProtocolValue.class, id);
    }

	public static List<ProtocolValue> findProtocolValueEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ProtocolValue o", ProtocolValue.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<ProtocolValue> findProtocolValueEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ProtocolValue o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ProtocolValue.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public ProtocolValue merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ProtocolValue merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public ProtocolState getLsState() {
        return this.lsState;
    }

	public void setLsState(ProtocolState lsState) {
        this.lsState = lsState;
    }

	public static Long countFindProtocolValuesByLsKindEqualsAndCodeValueLike(String lsKind, String codeValue) {
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        if (codeValue == null || codeValue.length() == 0) throw new IllegalArgumentException("The codeValue argument is required");
        codeValue = codeValue.replace('*', '%');
        if (codeValue.charAt(0) != '%') {
            codeValue = "%" + codeValue;
        }
        if (codeValue.charAt(codeValue.length() - 1) != '%') {
            codeValue = codeValue + "%";
        }
        EntityManager em = ProtocolValue.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ProtocolValue AS o WHERE o.lsKind = :lsKind  AND LOWER(o.codeValue) LIKE LOWER(:codeValue)", Long.class);
        q.setParameter("lsKind", lsKind);
        q.setParameter("codeValue", codeValue);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindProtocolValuesByLsKindEqualsAndStringValueLike(String lsKind, String stringValue) {
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        if (stringValue == null || stringValue.length() == 0) throw new IllegalArgumentException("The stringValue argument is required");
        stringValue = stringValue.replace('*', '%');
        if (stringValue.charAt(0) != '%') {
            stringValue = "%" + stringValue;
        }
        if (stringValue.charAt(stringValue.length() - 1) != '%') {
            stringValue = stringValue + "%";
        }
        EntityManager em = ProtocolValue.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ProtocolValue AS o WHERE o.lsKind = :lsKind  AND LOWER(o.stringValue) LIKE LOWER(:stringValue)", Long.class);
        q.setParameter("lsKind", lsKind);
        q.setParameter("stringValue", stringValue);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindProtocolValuesByLsState(ProtocolState lsState) {
        if (lsState == null) throw new IllegalArgumentException("The lsState argument is required");
        EntityManager em = ProtocolValue.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ProtocolValue AS o WHERE o.lsState = :lsState", Long.class);
        q.setParameter("lsState", lsState);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindProtocolValuesByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = ProtocolValue.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ProtocolValue AS o WHERE o.lsTransaction = :lsTransaction", Long.class);
        q.setParameter("lsTransaction", lsTransaction);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<ProtocolValue> findProtocolValuesByLsKindEqualsAndCodeValueLike(String lsKind, String codeValue) {
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        if (codeValue == null || codeValue.length() == 0) throw new IllegalArgumentException("The codeValue argument is required");
        codeValue = codeValue.replace('*', '%');
        if (codeValue.charAt(0) != '%') {
            codeValue = "%" + codeValue;
        }
        if (codeValue.charAt(codeValue.length() - 1) != '%') {
            codeValue = codeValue + "%";
        }
        EntityManager em = ProtocolValue.entityManager();
        TypedQuery<ProtocolValue> q = em.createQuery("SELECT o FROM ProtocolValue AS o WHERE o.lsKind = :lsKind  AND LOWER(o.codeValue) LIKE LOWER(:codeValue)", ProtocolValue.class);
        q.setParameter("lsKind", lsKind);
        q.setParameter("codeValue", codeValue);
        return q;
    }

	public static TypedQuery<ProtocolValue> findProtocolValuesByLsKindEqualsAndCodeValueLike(String lsKind, String codeValue, String sortFieldName, String sortOrder) {
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        if (codeValue == null || codeValue.length() == 0) throw new IllegalArgumentException("The codeValue argument is required");
        codeValue = codeValue.replace('*', '%');
        if (codeValue.charAt(0) != '%') {
            codeValue = "%" + codeValue;
        }
        if (codeValue.charAt(codeValue.length() - 1) != '%') {
            codeValue = codeValue + "%";
        }
        EntityManager em = ProtocolValue.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ProtocolValue AS o WHERE o.lsKind = :lsKind  AND LOWER(o.codeValue) LIKE LOWER(:codeValue)");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ProtocolValue> q = em.createQuery(queryBuilder.toString(), ProtocolValue.class);
        q.setParameter("lsKind", lsKind);
        q.setParameter("codeValue", codeValue);
        return q;
    }

	public static TypedQuery<ProtocolValue> findProtocolValuesByLsKindEqualsAndStringValueLike(String lsKind, String stringValue) {
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        if (stringValue == null || stringValue.length() == 0) throw new IllegalArgumentException("The stringValue argument is required");
        stringValue = stringValue.replace('*', '%');
        if (stringValue.charAt(0) != '%') {
            stringValue = "%" + stringValue;
        }
        if (stringValue.charAt(stringValue.length() - 1) != '%') {
            stringValue = stringValue + "%";
        }
        EntityManager em = ProtocolValue.entityManager();
        TypedQuery<ProtocolValue> q = em.createQuery("SELECT o FROM ProtocolValue AS o WHERE o.lsKind = :lsKind  AND LOWER(o.stringValue) LIKE LOWER(:stringValue)", ProtocolValue.class);
        q.setParameter("lsKind", lsKind);
        q.setParameter("stringValue", stringValue);
        return q;
    }

	public static TypedQuery<ProtocolValue> findProtocolValuesByLsKindEqualsAndStringValueLike(String lsKind, String stringValue, String sortFieldName, String sortOrder) {
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        if (stringValue == null || stringValue.length() == 0) throw new IllegalArgumentException("The stringValue argument is required");
        stringValue = stringValue.replace('*', '%');
        if (stringValue.charAt(0) != '%') {
            stringValue = "%" + stringValue;
        }
        if (stringValue.charAt(stringValue.length() - 1) != '%') {
            stringValue = stringValue + "%";
        }
        EntityManager em = ProtocolValue.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ProtocolValue AS o WHERE o.lsKind = :lsKind  AND LOWER(o.stringValue) LIKE LOWER(:stringValue)");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ProtocolValue> q = em.createQuery(queryBuilder.toString(), ProtocolValue.class);
        q.setParameter("lsKind", lsKind);
        q.setParameter("stringValue", stringValue);
        return q;
    }

	public static TypedQuery<ProtocolValue> findProtocolValuesByLsState(ProtocolState lsState) {
        if (lsState == null) throw new IllegalArgumentException("The lsState argument is required");
        EntityManager em = ProtocolValue.entityManager();
        TypedQuery<ProtocolValue> q = em.createQuery("SELECT o FROM ProtocolValue AS o WHERE o.lsState = :lsState", ProtocolValue.class);
        q.setParameter("lsState", lsState);
        return q;
    }

	public static TypedQuery<ProtocolValue> findProtocolValuesByLsState(ProtocolState lsState, String sortFieldName, String sortOrder) {
        if (lsState == null) throw new IllegalArgumentException("The lsState argument is required");
        EntityManager em = ProtocolValue.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ProtocolValue AS o WHERE o.lsState = :lsState");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ProtocolValue> q = em.createQuery(queryBuilder.toString(), ProtocolValue.class);
        q.setParameter("lsState", lsState);
        return q;
    }

	public static TypedQuery<ProtocolValue> findProtocolValuesByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = ProtocolValue.entityManager();
        TypedQuery<ProtocolValue> q = em.createQuery("SELECT o FROM ProtocolValue AS o WHERE o.lsTransaction = :lsTransaction", ProtocolValue.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }

	public static TypedQuery<ProtocolValue> findProtocolValuesByLsTransactionEquals(Long lsTransaction, String sortFieldName, String sortOrder) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = ProtocolValue.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ProtocolValue AS o WHERE o.lsTransaction = :lsTransaction");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ProtocolValue> q = em.createQuery(queryBuilder.toString(), ProtocolValue.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }
}
