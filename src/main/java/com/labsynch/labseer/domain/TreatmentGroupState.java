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
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.dto.FlatThingCsvDTO;
import com.labsynch.labseer.utils.CustomBigDecimalFactory;
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Entity
@Configurable
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

	public TreatmentGroupState(FlatThingCsvDTO treatmentGroupDTO) {
		this.setRecordedBy(treatmentGroupDTO.getRecordedBy());
		this.setRecordedDate(treatmentGroupDTO.getRecordedDate());
		this.setLsTransaction(treatmentGroupDTO.getLsTransaction());
		this.setModifiedBy(treatmentGroupDTO.getModifiedBy());
		this.setModifiedDate(treatmentGroupDTO.getModifiedDate());
		this.setLsType(treatmentGroupDTO.getStateType());
		this.setLsKind(treatmentGroupDTO.getStateKind());
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

	public static TypedQuery<TreatmentGroupState> findTreatmentGroupStatesByTreatmentGroupIDAndStateTypeKind(Long treatmentGroupId, 
			String stateType, 
			String stateKind) {
			if (stateType == null || stateKind.length() == 0) throw new IllegalArgumentException("The stateType argument is required");
			if (stateKind == null || stateKind.length() == 0) throw new IllegalArgumentException("The stateKind argument is required");
			
			EntityManager em = entityManager();
			String hsqlQuery = "SELECT ags FROM TreatmentGroupState AS ags " +
			"JOIN ags.treatmentGroup ag " +
			"WHERE ags.lsType = :stateType AND ags.lsKind = :stateKind AND ags.ignored IS NOT :ignored " +
			"AND ag.id = :treatmentGroupId ";
			TypedQuery<TreatmentGroupState> q = em.createQuery(hsqlQuery, TreatmentGroupState.class);
			q.setParameter("treatmentGroupId", treatmentGroupId);
			q.setParameter("stateType", stateType);
			q.setParameter("stateKind", stateKind);
			q.setParameter("ignored", true);
			return q;
		}

	public static TypedQuery<TreatmentGroupState> findTreatmentGroupStatesByTreatmentGroupCodeNameAndStateTypeKind(
			String treatmentGroupCodeName, String stateType, String stateKind) {
		if (stateType == null || stateKind.length() == 0) throw new IllegalArgumentException("The stateType argument is required");
		if (stateKind == null || stateKind.length() == 0) throw new IllegalArgumentException("The stateKind argument is required");
		
		EntityManager em = entityManager();
		String hsqlQuery = "SELECT ags FROM TreatmentGroupState AS ags " +
		"JOIN ags.treatmentGroup ag " +
		"WHERE ags.lsType = :stateType AND ags.lsKind = :stateKind AND ags.ignored IS NOT :ignored " +
		"AND ag.codeName = :treatmentGroupCodeName ";
		TypedQuery<TreatmentGroupState> q = em.createQuery(hsqlQuery, TreatmentGroupState.class);
		q.setParameter("treatmentGroupCodeName", treatmentGroupCodeName);
		q.setParameter("stateType", stateType);
		q.setParameter("stateKind", stateKind);
		q.setParameter("ignored", true);
		return q;
	}
	

	public static Long countFindTreatmentGroupStatesByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = TreatmentGroupState.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM TreatmentGroupState AS o WHERE o.lsTransaction = :lsTransaction", Long.class);
        q.setParameter("lsTransaction", lsTransaction);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindTreatmentGroupStatesByLsTypeAndKindEquals(String lsTypeAndKind) {
        if (lsTypeAndKind == null || lsTypeAndKind.length() == 0) throw new IllegalArgumentException("The lsTypeAndKind argument is required");
        EntityManager em = TreatmentGroupState.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM TreatmentGroupState AS o WHERE o.lsTypeAndKind = :lsTypeAndKind", Long.class);
        q.setParameter("lsTypeAndKind", lsTypeAndKind);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindTreatmentGroupStatesByTreatmentGroup(TreatmentGroup treatmentGroup) {
        if (treatmentGroup == null) throw new IllegalArgumentException("The treatmentGroup argument is required");
        EntityManager em = TreatmentGroupState.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM TreatmentGroupState AS o WHERE o.treatmentGroup = :treatmentGroup", Long.class);
        q.setParameter("treatmentGroup", treatmentGroup);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<TreatmentGroupState> findTreatmentGroupStatesByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = TreatmentGroupState.entityManager();
        TypedQuery<TreatmentGroupState> q = em.createQuery("SELECT o FROM TreatmentGroupState AS o WHERE o.lsTransaction = :lsTransaction", TreatmentGroupState.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }

	public static TypedQuery<TreatmentGroupState> findTreatmentGroupStatesByLsTransactionEquals(Long lsTransaction, String sortFieldName, String sortOrder) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = TreatmentGroupState.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM TreatmentGroupState AS o WHERE o.lsTransaction = :lsTransaction");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<TreatmentGroupState> q = em.createQuery(queryBuilder.toString(), TreatmentGroupState.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }

	public static TypedQuery<TreatmentGroupState> findTreatmentGroupStatesByLsTypeAndKindEquals(String lsTypeAndKind) {
        if (lsTypeAndKind == null || lsTypeAndKind.length() == 0) throw new IllegalArgumentException("The lsTypeAndKind argument is required");
        EntityManager em = TreatmentGroupState.entityManager();
        TypedQuery<TreatmentGroupState> q = em.createQuery("SELECT o FROM TreatmentGroupState AS o WHERE o.lsTypeAndKind = :lsTypeAndKind", TreatmentGroupState.class);
        q.setParameter("lsTypeAndKind", lsTypeAndKind);
        return q;
    }

	public static TypedQuery<TreatmentGroupState> findTreatmentGroupStatesByLsTypeAndKindEquals(String lsTypeAndKind, String sortFieldName, String sortOrder) {
        if (lsTypeAndKind == null || lsTypeAndKind.length() == 0) throw new IllegalArgumentException("The lsTypeAndKind argument is required");
        EntityManager em = TreatmentGroupState.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM TreatmentGroupState AS o WHERE o.lsTypeAndKind = :lsTypeAndKind");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<TreatmentGroupState> q = em.createQuery(queryBuilder.toString(), TreatmentGroupState.class);
        q.setParameter("lsTypeAndKind", lsTypeAndKind);
        return q;
    }

	public static TypedQuery<TreatmentGroupState> findTreatmentGroupStatesByTreatmentGroup(TreatmentGroup treatmentGroup) {
        if (treatmentGroup == null) throw new IllegalArgumentException("The treatmentGroup argument is required");
        EntityManager em = TreatmentGroupState.entityManager();
        TypedQuery<TreatmentGroupState> q = em.createQuery("SELECT o FROM TreatmentGroupState AS o WHERE o.treatmentGroup = :treatmentGroup", TreatmentGroupState.class);
        q.setParameter("treatmentGroup", treatmentGroup);
        return q;
    }

	public static TypedQuery<TreatmentGroupState> findTreatmentGroupStatesByTreatmentGroup(TreatmentGroup treatmentGroup, String sortFieldName, String sortOrder) {
        if (treatmentGroup == null) throw new IllegalArgumentException("The treatmentGroup argument is required");
        EntityManager em = TreatmentGroupState.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM TreatmentGroupState AS o WHERE o.treatmentGroup = :treatmentGroup");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<TreatmentGroupState> q = em.createQuery(queryBuilder.toString(), TreatmentGroupState.class);
        q.setParameter("treatmentGroup", treatmentGroup);
        return q;
    }

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("treatmentGroup", "lsValues");

	public static List<TreatmentGroupState> findAllTreatmentGroupStates(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM TreatmentGroupState o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, TreatmentGroupState.class).getResultList();
    }

	public static List<TreatmentGroupState> findTreatmentGroupStateEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM TreatmentGroupState o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, TreatmentGroupState.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public TreatmentGroup getTreatmentGroup() {
        return this.treatmentGroup;
    }

	public void setTreatmentGroup(TreatmentGroup treatmentGroup) {
        this.treatmentGroup = treatmentGroup;
    }

	public Set<TreatmentGroupValue> getLsValues() {
        return this.lsValues;
    }

	public void setLsValues(Set<TreatmentGroupValue> lsValues) {
        this.lsValues = lsValues;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
