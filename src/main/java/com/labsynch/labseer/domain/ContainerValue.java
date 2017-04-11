package com.labsynch.labseer.domain;

import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
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
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = { "findContainerValuesByLsState", "findContainerValuesByIgnoredNot" })
public class ContainerValue extends AbstractValue {

    private static final Logger logger = LoggerFactory.getLogger(ContainerValue.class);
    
    @SuppressWarnings("unused")
	public ContainerValue (){
		String lsType;
    	String lsKind;
    	String lsTypeAndKind;
    	String stringValue;
    	String codeValue;
    	String fileValue;
    	String urlValue;
    	Date dateValue;
        String clobValue;
        byte[] blobValue;
    	String operatorType;
    	String operatorKind;
    	String operatorTypeAndKind;
    	BigDecimal numericValue;
    	Integer sigFigs;
    	BigDecimal uncertainty;
    	Integer numberOfReplicates;
    	String uncertaintyType;
    	String unitType;
    	String unitKind;
    	String unitTypeAndKind;
    	String comments;
    	boolean ignored;
    	Long lsTransaction;
    	Date recordedDate;
    	String recordedBy;
    	Date modifiedDate;
    	String modifiedBy;
    	boolean publicData;
    }

    @SuppressWarnings("unused")
    public ContainerValue (String recordedByIn, Date recordedDateIn, Long lsTransactionIn, 
    		String lsTypeIn, String lsKindIn, String lsTypeAndKindIn, String stringValueIn, 
    		String codeValueIn, String batchSizeIn, String urlValueIn, Date dateValueIn, 
    		String clobValueIn, byte[] blobValueIn, String operatorTypeIn, String operatorKindIn
    		
    		){
		String lsType = lsTypeIn;
    	String lsKind = lsKindIn;
    	String lsTypeAndKind = lsTypeAndKindIn;
    	String stringValue = stringValueIn;
    	String codeValue = codeValueIn;
    	String batchSize = batchSizeIn;
    	String urlValue = urlValueIn;
    	Date dateValue = dateValueIn;
        String clobValue = clobValueIn;
        byte[] blobValue = blobValueIn;
    	String operatorType = operatorTypeIn;
    	String operatorKind = operatorKindIn;
    	String operatorTypeAndKind;
    	BigDecimal numericValue;
    	Integer sigFigs;
    	BigDecimal uncertainty;
    	Integer numberOfReplicates;
    	String uncertaintyType;
    	String unitType;
    	String unitKind;
    	String unitTypeAndKind;
    	String comments;
    	boolean ignored;
    	Long lsTransaction = lsTransactionIn;
    	Date recordedDate = recordedDateIn;
    	String recordedBy;
    	Date modifiedDate;
    	String modifiedBy;
    	boolean publicData;
    }

    public ContainerValue(ContainerValue containerValue) {
        super.setRecordedBy(containerValue.getRecordedBy());
        super.setRecordedDate(containerValue.getRecordedDate());
        super.setLsTransaction(containerValue.getLsTransaction());
        super.setModifiedBy(containerValue.getModifiedBy());
        super.setModifiedDate(containerValue.getModifiedDate());
        super.setLsType(containerValue.getLsType());
        super.setLsKind(containerValue.getLsKind());
        super.setIgnored(containerValue.isIgnored());
    }
    
    public static ContainerValue create(ContainerValue containerValue) {
    	ContainerValue newContainerValue = new JSONDeserializer<ContainerValue>().use(null, ContainerValue.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).deserializeInto(containerValue.toJson(), 
        				new ContainerValue());	
    
        return newContainerValue;
    }

    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "container_state_id")
    private ContainerState lsState;

    public static long countContainerValues() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ContainerValue o", Long.class).getSingleResult();
    }

    public static List<com.labsynch.labseer.domain.ContainerValue> findAllContainerValues() {
        return entityManager().createQuery("SELECT o FROM ContainerValue o", ContainerValue.class).getResultList();
    }

    public static com.labsynch.labseer.domain.ContainerValue findContainerValue(Long id) {
        if (id == null) return null;
        return entityManager().find(ContainerValue.class, id);
    }

    public static long countValidContainerValues() {
        boolean ignored = true;
        EntityManager em = ContainerValue.entityManager();
        TypedQuery<Long> q = em.createQuery("SELECT COUNT(o) FROM ContainerValue AS o WHERE o.ignored IS NOT :ignored", Long.class);
        q.setParameter("ignored", ignored);
        return q.getSingleResult();
    }

    public static List<com.labsynch.labseer.domain.ContainerValue> findValidContainerValues(int maxResults) {
        boolean ignored = true;
        EntityManager em = ContainerValue.entityManager();
        TypedQuery<ContainerValue> q = em.createQuery("SELECT o FROM ContainerValue AS o WHERE o.ignored IS NOT :ignored", ContainerValue.class).setMaxResults(maxResults);
        q.setParameter("ignored", ignored);
        return q.getResultList();
    }
    
    public static long countValidContainerValuesByLsState(ContainerState lsState) {
        boolean ignored = true;
        EntityManager em = ContainerValue.entityManager();
        TypedQuery<Long> q = em.createQuery("SELECT COUNT(o) FROM ContainerValue AS o WHERE o.ignored IS NOT :ignored AND o.lsState = :lsState", Long.class);
        q.setParameter("ignored", ignored);
        q.setParameter("lsState", lsState);
        return q.getSingleResult();
    }

    public static long countContainerValuesByLsState(ContainerState lsState) {
        EntityManager em = ContainerValue.entityManager();
        TypedQuery<Long> q = em.createQuery("SELECT COUNT(o) FROM ContainerValue AS o WHERE o.lsState = :lsState", Long.class);
        q.setParameter("lsState", lsState);
        return q.getSingleResult();
    }
    
    public static List<ContainerValue> findValidContainerValuesByLsState(ContainerState lsState, int maxResults) {
        if (lsState == null) throw new IllegalArgumentException("The lsState argument is required");
        EntityManager em = ContainerValue.entityManager();
        TypedQuery<ContainerValue> q = em.createQuery("SELECT o FROM ContainerValue AS o WHERE o.ignored IS NOT :ignored AND o.lsState = :lsState", ContainerValue.class).setFirstResult(0).setMaxResults(maxResults);
        boolean ignored = true;
        q.setParameter("ignored", ignored);
        q.setParameter("lsState", lsState);
        return q.getResultList();
    }

    public static List<ContainerValue> findAllContainerValuesByLsState(ContainerState lsState, int maxResults) {
        if (lsState == null) throw new IllegalArgumentException("The lsState argument is required");
        EntityManager em = ContainerValue.entityManager();
        TypedQuery<ContainerValue> q = em.createQuery("SELECT o FROM ContainerValue AS o WHERE o.lsState = :lsState", ContainerValue.class).setFirstResult(0).setMaxResults(maxResults);
        q.setParameter("lsState", lsState);
        return q.getResultList();
    }
    
    public static List<ContainerValue> findAllContainerValuesByLsState(ContainerState lsState) {
        if (lsState == null) throw new IllegalArgumentException("The lsState argument is required");
        EntityManager em = ContainerValue.entityManager();
//        Session session = em.unwrap(Session.class);
//        Query q = session.createQuery("SELECT o FROM ContainerValue AS o WHERE o.lsState = :lsState");

        TypedQuery<ContainerValue> q = em.createQuery("SELECT o FROM ContainerValue AS o WHERE o.lsState = :lsState", ContainerValue.class);
        q.setParameter("lsState", lsState);
        return q.getResultList();
    }
    
    public static List<com.labsynch.labseer.domain.ContainerValue> findContainerValueEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ContainerValue o", ContainerValue.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    public static com.labsynch.labseer.domain.ContainerValue fromJsonToContainerValue(String json) {
        return new JSONDeserializer<ContainerValue>().use(null, ContainerValue.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
    }

    public static Collection<com.labsynch.labseer.domain.ContainerValue> fromJsonArrayToContainerValues(String json) {
        return new JSONDeserializer<List<ContainerValue>>().use(null, ArrayList.class).use("values", ContainerValue.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
    }

    public static Collection<com.labsynch.labseer.domain.ContainerValue> fromJsonArrayToContainerValues(Reader json) {
        return new JSONDeserializer<List<ContainerValue>>().use(null, ArrayList.class).use("values", ContainerValue.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
    }
    
    public static com.labsynch.labseer.domain.ContainerValue update(com.labsynch.labseer.domain.ContainerValue containerValue) {
        ContainerValue updatedContainerValue = new JSONDeserializer<ContainerValue>().use(null, ArrayList.class).use("values", ContainerValue.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserializeInto(containerValue.toJson(), ContainerValue.findContainerValue(containerValue.getId()));
        updatedContainerValue.setModifiedDate(new Date());
        updatedContainerValue.merge();
        return updatedContainerValue;
    }

    @Transactional
    public com.labsynch.labseer.domain.ContainerValue merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ContainerValue merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    @Transactional
    public static List<java.lang.Long> saveList(List<com.labsynch.labseer.domain.ContainerValue> entities) {
        logger.debug("saving the list of ContainerValues: " + entities.size());
        List<Long> idList = new ArrayList<Long>();
        int batchSize = 50;
        int imported = 0;
        for (ContainerValue e : entities) {
            e.persist();
            idList.add(e.getId());
            if (++imported % batchSize == 0) {
                e.flush();
                e.clear();
            }
        }
        return idList;
    }

    @Transactional
    public static String getContainerValueCollectionJson(List<java.lang.Long> idList) {
        Collection<ContainerValue> containerValues = new HashSet<ContainerValue>();
        for (Long id : idList) {
            ContainerValue query = ContainerValue.findContainerValue(id);
            if (query != null) containerValues.add(query);
        }
        return ContainerValue.toJsonArray(containerValues);
    }

    @Transactional
    public String toJson() {
        return new JSONSerializer().exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }

    @Transactional
    public static String toJsonArray(Collection<com.labsynch.labseer.domain.ContainerValue> collection) {
        return new JSONSerializer().exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(collection);
    }

    @Transactional
    public static String toJsonArrayStub(Collection<com.labsynch.labseer.domain.ContainerValue> collection) {
        return new JSONSerializer().exclude("*.class", "lsState").transform(new ExcludeNulls(), void.class).serialize(collection);
    }
}
