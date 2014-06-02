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
import javax.persistence.Query;
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
@RooJpaActiveRecord(finders = { "findTreatmentGroupValuesByLsState", "findTreatmentGroupValuesByLsTransactionEquals",
		"findTreatmentGroupValuesByCodeValueEquals", "findTreatmentGroupValuesByIgnoredNotAndCodeValueEquals"})
public class TreatmentGroupValue extends AbstractValue {

	private static final Logger logger = LoggerFactory.getLogger(TreatmentGroupValue.class);

    @NotNull
    @ManyToOne
    @JoinColumn(name = "treatment_state_id")
    private TreatmentGroupState lsState;
    
    public static TreatmentGroupValue create(TreatmentGroupValue treatmentGroupValue) {
    	TreatmentGroupValue newTreatmentGroupValue = new JSONDeserializer<TreatmentGroupValue>().use(null, TreatmentGroupValue.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).deserializeInto(treatmentGroupValue.toJson(), 
        				new TreatmentGroupValue());	
    
        return newTreatmentGroupValue;
    }

	public static long countTreatmentGroupValues() {
        return entityManager().createQuery("SELECT COUNT(o) FROM TreatmentGroupValue o", Long.class).getSingleResult();
    }

	public static List<TreatmentGroupValue> findAllTreatmentGroupValues() {
        return entityManager().createQuery("SELECT o FROM TreatmentGroupValue o", TreatmentGroupValue.class).getResultList();
    }

	public static TreatmentGroupValue findTreatmentGroupValue(Long id) {
        if (id == null) return null;
        return entityManager().find(TreatmentGroupValue.class, id);
    }

	public static List<TreatmentGroupValue> findTreatmentGroupValueEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM TreatmentGroupValue o", TreatmentGroupValue.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

   
	public static TreatmentGroupValue update(TreatmentGroupValue treatmentGroupValue) {
		TreatmentGroupValue updatedTreatmentGroupValue = new JSONDeserializer<TreatmentGroupValue>().
		use(null, TreatmentGroupValue.class).
		use(BigDecimal.class, new CustomBigDecimalFactory()).
		deserializeInto(treatmentGroupValue.toJson(), TreatmentGroupValue.findTreatmentGroupValue(treatmentGroupValue.getId()));
		updatedTreatmentGroupValue.setModifiedDate(new Date());
		updatedTreatmentGroupValue.merge();
		return updatedTreatmentGroupValue;
	}
	
	@Transactional
    public TreatmentGroupValue merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        TreatmentGroupValue merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
	
	@Transactional
	public static List<Long> saveList(List<TreatmentGroupValue> entities) {
		logger.debug("saving the list of TreatmentGroupValues: " + entities.size());
		//return a list of ids
		List<Long> idList = new ArrayList<Long>();
		int batchSize = 50;

	    int imported = 0;
	    for (TreatmentGroupValue e : entities) {
	        e.persist();  // 1. Roo introduces this method via an ITD
	        idList.add(e.getId());
	         if (++imported % batchSize == 0) {
	        	 e.flush();  // 2. ... and this one
	        	 e.clear();  // 3. ... and this one
	        }
	    }
	    return idList;
	 }
		
	@Transactional
	public static String getTreatmentGroupValueCollectionJson(List<Long> idList) {
		Collection<TreatmentGroupValue> treatmentGroupValues = new HashSet<TreatmentGroupValue>();
		for (Long id : idList){
			TreatmentGroupValue query = TreatmentGroupValue.findTreatmentGroupValue(id);
			if (query != null) treatmentGroupValues.add(query);
		}
        return TreatmentGroupValue.toJsonArray(treatmentGroupValues);
    }
	
	@Transactional
	public static int deleteByExperimentID(Long experimentId) {
		if (experimentId == null) return 0;
		EntityManager em = TreatmentGroupValue.entityManager();
		String deleteSQL = "DELETE FROM TreatmentGroupValue oo WHERE id in (select o.id from TreatmentGroupValue o where o.lsState.treatmentGroup.analysisGroup.experiment.id = :experimentId)";
		Query q = em.createQuery(deleteSQL);
		q.setParameter("experimentId", experimentId);
		int numberOfDeletedEntities = q.executeUpdate();
		return numberOfDeletedEntities;
	}

    public static TreatmentGroupValue fromJsonToTreatmentGroupValue(String json) {
        return new JSONDeserializer<TreatmentGroupValue>().
        		use(null, TreatmentGroupValue.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).
        		deserialize(json);
    }
        
    public static Collection<TreatmentGroupValue> fromJsonArrayToTreatmentGroupValues(String json) {
        return new JSONDeserializer<List<TreatmentGroupValue>>().
        		use(null, ArrayList.class).
        		use("values", TreatmentGroupValue.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).
        		deserialize(json);
    }
    
    public static Collection<TreatmentGroupValue> fromJsonArrayToTreatmentGroupValues(Reader json) {
        return new JSONDeserializer<List<TreatmentGroupValue>>().
        		use(null, ArrayList.class).
        		use("values", TreatmentGroupValue.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).
        		deserialize(json);
    }
 
	@Transactional
    public String toJson() {
        return new JSONSerializer().exclude("*.class")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(this);
    }
    
	@Transactional
    public static String toJsonArray(Collection<TreatmentGroupValue> collection) {
        return new JSONSerializer().exclude("*.class")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(collection);
    }

	@Transactional
    public static String toJsonArrayStub(Collection<TreatmentGroupValue> collection) {
        return new JSONSerializer().exclude("*.class", "lsState")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(collection);
    }

}
