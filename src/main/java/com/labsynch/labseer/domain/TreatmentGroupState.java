package com.labsynch.labseer.domain;

import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;

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
@RooJpaActiveRecord(finders = { "findTreatmentGroupStatesByLsTypeAndKindEquals", "findTreatmentGroupStatesByTreatmentGroup", "findTreatmentGroupStatesByLsTransactionEquals" })
public class TreatmentGroupState extends AbstractState {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "treatment_group_id")
    private TreatmentGroup treatmentGroup;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lsState", fetch = FetchType.LAZY)
    private Set<TreatmentGroupValue> lsValues = new HashSet<TreatmentGroupValue>();

    public TreatmentGroupState() {
    }
    
    public TreatmentGroupState(com.labsynch.labseer.domain.TreatmentGroupState treatmentGroupState) {
        super.setRecordedBy(treatmentGroupState.getRecordedBy());
        super.setRecordedDate(treatmentGroupState.getRecordedDate());
        super.setLsTransaction(treatmentGroupState.getLsTransaction());
        super.setModifiedBy(treatmentGroupState.getModifiedBy());
        super.setModifiedDate(treatmentGroupState.getModifiedDate());
        super.setLsType(treatmentGroupState.getLsType());
        super.setLsKind(treatmentGroupState.getLsKind());
    }

	public static TreatmentGroupState update(TreatmentGroupState treatmentGroupState) {
		TreatmentGroupState updatedTreatmentGroupState = TreatmentGroupState.findTreatmentGroupState(treatmentGroupState.getId());
		updatedTreatmentGroupState.setRecordedBy(treatmentGroupState.getRecordedBy());
		updatedTreatmentGroupState.setRecordedDate(treatmentGroupState.getRecordedDate());
		updatedTreatmentGroupState.setLsTransaction(treatmentGroupState.getLsTransaction());
		updatedTreatmentGroupState.setModifiedBy(treatmentGroupState.getModifiedBy());
		updatedTreatmentGroupState.setModifiedDate(new Date());
		updatedTreatmentGroupState.setLsType(treatmentGroupState.getLsType());
		updatedTreatmentGroupState.setLsKind(treatmentGroupState.getLsKind());		
		updatedTreatmentGroupState.merge();
		return updatedTreatmentGroupState;
	}
	
	public static long countTreatmentGroupStates() {
        return entityManager().createQuery("SELECT COUNT(o) FROM TreatmentGroupState o", Long.class).getSingleResult();
    }

	public static List<TreatmentGroupState> findAllTreatmentGroupStates() {
        return entityManager().createQuery("SELECT o FROM TreatmentGroupState o", TreatmentGroupState.class).getResultList();
    }

	public static TreatmentGroupState findTreatmentGroupState(Long id) {
        if (id == null) return null;
        return entityManager().find(TreatmentGroupState.class, id);
    }

	public static List<TreatmentGroupState> findTreatmentGroupStateEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM TreatmentGroupState o", TreatmentGroupState.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public TreatmentGroupState merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        TreatmentGroupState merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
	
	@Transactional
	public static List<Long> saveList(List<TreatmentGroupState> entities) {
		//return a list of ids
		List<Long> idList = new ArrayList<Long>();
	    int imported = 0;
		int batchSize = 50;

	    for (TreatmentGroupState e : entities) {
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
	public static String getTreatmentGroupStateCollectionJson(List<Long> idList) {
		Collection<TreatmentGroupState> treatmentGroupStates = new ArrayList<TreatmentGroupState>();
		for (Long id : idList){
			TreatmentGroupState query = TreatmentGroupState.findTreatmentGroupState(id);
			if (query != null) treatmentGroupStates.add(query);
		}
        return TreatmentGroupState.toJsonArray(treatmentGroupStates);
    }

	@Transactional
    public String toJson() {
        return new JSONSerializer()
        		.exclude("*.class")
        		.transform(new ExcludeNulls(), void.class)
        		.serialize(this);
    }
    
    public static TreatmentGroupState fromJsonToTreatmentGroupState(String json) {
        return new JSONDeserializer<TreatmentGroupState>()
        		.use(null, TreatmentGroupState.class)
        		.use(BigDecimal.class, new CustomBigDecimalFactory())
        		.deserialize(json);
    }
    
	@Transactional
    public static String toJsonArray(Collection<TreatmentGroupState> collection) {
        return new JSONSerializer()
        		.exclude("*.class")
        		.transform(new ExcludeNulls(), void.class)
        		.serialize(collection);
    }

	@Transactional
    public static String toJsonArrayStub(Collection<TreatmentGroupState> collection) {
        return new JSONSerializer()
        		.exclude("*.class", "treatmentGroup")
        		.transform(new ExcludeNulls(), void.class)
        		.serialize(collection);
    }

    public static Collection<TreatmentGroupState> fromJsonArrayToTreatmentGroupStates(String json) {
        return new JSONDeserializer<List<TreatmentGroupState>>()
        		.use(null, ArrayList.class)
        		.use("values", TreatmentGroupState.class)
        		.use(BigDecimal.class, new CustomBigDecimalFactory())
        		.deserialize(json);
    }
    
    public static Collection<TreatmentGroupState> fromJsonArrayToTreatmentGroupStates(Reader json) {
        return new JSONDeserializer<List<TreatmentGroupState>>()
        		.use(null, ArrayList.class)
        		.use("values", TreatmentGroupState.class)
        		.use(BigDecimal.class, new CustomBigDecimalFactory())
        		.deserialize(json);
    }

	public static int deleteByExperimentID(Long experimentId) {
		if (experimentId == null) return 0;
		EntityManager em = SubjectValue.entityManager();
		String deleteSQL = "DELETE FROM TreatmentGroupState oo WHERE id in (select o.id from TreatmentGroupState o where o.treatmentGroup.analysisGroup.experiment.id = :experimentId)";

		Query q = em.createQuery(deleteSQL);
		q.setParameter("experimentId", experimentId);
		int numberOfDeletedEntities = q.executeUpdate();
		return numberOfDeletedEntities;
	}
	
}
