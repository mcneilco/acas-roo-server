package com.labsynch.labseer.domain;

import com.labsynch.labseer.utils.CustomBigDecimalFactory;
import com.labsynch.labseer.utils.ExcludeNulls;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = { "findItxLsThingLsThingValuesByLsTransactionEquals", "findItxLsThingLsThingValuesByLsKindEquals" })
public class ItxLsThingLsThingValue extends AbstractValue {

    @ManyToOne
    private ItxLsThingLsThingState lsState;

    public ItxLsThingLsThingValue(com.labsynch.labseer.domain.ItxLsThingLsThingValue itxLsThingLsThingValue) {
        super.setBlobValue(itxLsThingLsThingValue.getBlobValue());
        super.setClobValue(itxLsThingLsThingValue.getClobValue());
        super.setCodeKind(itxLsThingLsThingValue.getCodeKind());
        super.setCodeOrigin(itxLsThingLsThingValue.getCodeOrigin());
        super.setCodeType(itxLsThingLsThingValue.getCodeType());
        super.setCodeTypeAndKind(itxLsThingLsThingValue.getCodeTypeAndKind());
        super.setCodeValue(itxLsThingLsThingValue.getCodeValue());
        super.setComments(itxLsThingLsThingValue.getComments());
        super.setConcentration(itxLsThingLsThingValue.getConcentration());
        super.setConcUnit(itxLsThingLsThingValue.getConcUnit());
        super.setDateValue(itxLsThingLsThingValue.getDateValue());
        super.setDeleted(itxLsThingLsThingValue.isDeleted());
        super.setFileValue(itxLsThingLsThingValue.getFileValue());
        super.setIgnored(itxLsThingLsThingValue.isIgnored());
        super.setLsKind(itxLsThingLsThingValue.getLsKind());
        super.setLsTransaction(itxLsThingLsThingValue.getLsTransaction());
        super.setLsType(itxLsThingLsThingValue.getLsType());
        super.setLsTypeAndKind(itxLsThingLsThingValue.getLsTypeAndKind());
        super.setModifiedBy(itxLsThingLsThingValue.getModifiedBy());
        super.setModifiedDate(itxLsThingLsThingValue.getModifiedDate());
        super.setNumberOfReplicates(itxLsThingLsThingValue.getNumberOfReplicates());
        super.setNumericValue(itxLsThingLsThingValue.getNumericValue());
        super.setOperatorKind(itxLsThingLsThingValue.getOperatorKind());
        super.setOperatorType(itxLsThingLsThingValue.getOperatorType());
        super.setOperatorTypeAndKind(itxLsThingLsThingValue.getOperatorTypeAndKind());
        super.setPublicData(itxLsThingLsThingValue.isPublicData());
        super.setRecordedBy(itxLsThingLsThingValue.getRecordedBy());
        super.setRecordedDate(itxLsThingLsThingValue.getRecordedDate());
        super.setSigFigs(itxLsThingLsThingValue.getSigFigs());
        super.setStringValue(itxLsThingLsThingValue.getStringValue());
        super.setUncertainty(itxLsThingLsThingValue.getUncertainty());
        super.setUncertaintyType(itxLsThingLsThingValue.getUncertaintyType());
        super.setUnitKind(itxLsThingLsThingValue.getUnitKind());
        super.setUnitType(itxLsThingLsThingValue.getUnitType());
        super.setUnitTypeAndKind(itxLsThingLsThingValue.getUnitTypeAndKind());
        super.setUrlValue(itxLsThingLsThingValue.getUrlValue());
        super.setVersion(itxLsThingLsThingValue.getVersion());
    }

    public ItxLsThingLsThingValue() {
    }

    public static com.labsynch.labseer.domain.ItxLsThingLsThingValue update(com.labsynch.labseer.domain.ItxLsThingLsThingValue object) {
        ItxLsThingLsThingValue updatedObject = new JSONDeserializer<ItxLsThingLsThingValue>().use(null, ItxLsThingLsThingValue.class).deserializeInto(object.toJson(), ItxLsThingLsThingValue.findItxLsThingLsThingValue(object.getId()));
        updatedObject.setModifiedDate(new Date());
        updatedObject.merge();
        return updatedObject;
    }

    public static com.labsynch.labseer.domain.ItxLsThingLsThingValue updateNoMerge(com.labsynch.labseer.domain.ItxLsThingLsThingValue object) {
        ItxLsThingLsThingValue updatedObject = new JSONDeserializer<ItxLsThingLsThingValue>().use(null, ItxLsThingLsThingValue.class).deserializeInto(object.toJson(), ItxLsThingLsThingValue.findItxLsThingLsThingValue(object.getId()));
        updatedObject.setModifiedDate(new Date());
        return updatedObject;
    }

    public static com.labsynch.labseer.domain.ItxLsThingLsThingValue fromJsonToItxLsThingLsThingValue(String json) {
        return new JSONDeserializer<ItxLsThingLsThingValue>().use(null, ItxLsThingLsThingValue.class).deserialize(json);
    }

    public static Collection<com.labsynch.labseer.domain.ItxLsThingLsThingValue> fromJsonArrayToItxLsThingLsThingValues(String json) {
        return new JSONDeserializer<List<ItxLsThingLsThingValue>>().use(null, ArrayList.class).use("values", ItxLsThingLsThingValue.class).deserialize(json);
    }

    public static Collection<com.labsynch.labseer.domain.ItxLsThingLsThingValue> fromJsonArrayToItxLsThingLsThingValues(Reader json) {
        return new JSONDeserializer<List<ItxLsThingLsThingValue>>().use(null, ArrayList.class).use("values", ItxLsThingLsThingValue.class).deserialize(json);
    }

    @Transactional
    public String toJson() {
        return new JSONSerializer().exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }

    @Transactional
    public static String toJsonArray(Collection<com.labsynch.labseer.domain.ItxLsThingLsThingValue> collection) {
        return new JSONSerializer().exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(collection);
    }

    @Transactional
    public static String toJsonArrayStub(Collection<com.labsynch.labseer.domain.ItxLsThingLsThingValue> collection) {
        return new JSONSerializer().exclude("*.class", "lsState").transform(new ExcludeNulls(), void.class).serialize(collection);
    }

    public static com.labsynch.labseer.domain.ItxLsThingLsThingValue create(com.labsynch.labseer.domain.ItxLsThingLsThingValue lsThingValue) {
        ItxLsThingLsThingValue newItxLsThingLsThingValue = new JSONDeserializer<ItxLsThingLsThingValue>().use(null, ItxLsThingLsThingValue.class).deserializeInto(lsThingValue.toJson(), new ItxLsThingLsThingValue());
        return newItxLsThingLsThingValue;
    }

    public static TypedQuery<ItxLsThingLsThingValue> findItxLsValueByItxThingAndStateTypeStateKindAndValueTypeKind(Long itxOrthologId, String stateType,
    		String stateKind, String valueType, String valueKind) {
        if (valueType == null || valueType.length() == 0) throw new IllegalArgumentException("The valueType argument is required");
        if (valueKind == null || valueKind.length() == 0) throw new IllegalArgumentException("The valueKind argument is required");

        EntityManager em = ItxLsThingLsThingValue.entityManager();
        String hqlQuery = "SELECT o FROM ItxLsThingLsThingValue AS o "
        		+ "JOIN o.lsState its with its.ignored = false AND its.lsType = :stateType and its.lsKind = :stateKind "
        		+ "JOIN its.itxLsThingLsThing itxThing with itxThing.ignored = false "
        		+ "WHERE o.lsType = :valueType AND o.lsKind = :valueKind "
        		+ "AND itxThing.id = :itxOrthologId";
                
        TypedQuery<ItxLsThingLsThingValue> q = em.createQuery(hqlQuery, ItxLsThingLsThingValue.class);
        q.setParameter("stateType", stateType);
        q.setParameter("stateKind", stateKind);
        q.setParameter("valueType", valueType);
        q.setParameter("valueKind", valueKind);
        q.setParameter("itxOrthologId", itxOrthologId);
        return q;
    }

    public static TypedQuery<ItxLsThingLsThingValue> findItxLsThingLsThingValuesByLsKindEquals(String lsKind) {
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = ItxLsThingLsThingValue.entityManager();
        TypedQuery<ItxLsThingLsThingValue> q = em.createQuery("SELECT o FROM ItxLsThingLsThingValue AS o WHERE o.lsKind = :lsKind", ItxLsThingLsThingValue.class);
        q.setParameter("lsKind", lsKind);
        return q;
    }
    
    public static TypedQuery<ItxLsThingLsThingValue> findItxLsThingLsThingValuesByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = ItxLsThingLsThingValue.entityManager();
        TypedQuery<ItxLsThingLsThingValue> q = em.createQuery("SELECT o FROM ItxLsThingLsThingValue AS o WHERE o.lsTransaction = :lsTransaction", ItxLsThingLsThingValue.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }
    
}
