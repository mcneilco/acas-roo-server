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
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.utils.CustomBigDecimalFactory;
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Entity
@Configurable

public class LsThingState extends AbstractState {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "lsthing_id")
    private LsThing lsThing;

    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "lsState", fetch = FetchType.LAZY)
    private Set<LsThingValue> lsValues = new HashSet<LsThingValue>();

    public LsThingState() {
    }
    
    public LsThingState(com.labsynch.labseer.domain.LsThingState lsState) {
        super.setRecordedBy(lsState.getRecordedBy());
        super.setRecordedDate(lsState.getRecordedDate());
        super.setLsTransaction(lsState.getLsTransaction());
        super.setModifiedBy(lsState.getModifiedBy());
        super.setModifiedDate(lsState.getModifiedDate());
        super.setLsType(lsState.getLsType());
        super.setLsKind(lsState.getLsKind());
    }

	public static LsThingState update(LsThingState lsState) {
		LsThingState updatedLsThingState = LsThingState.findLsThingState(lsState.getId());
		if (updatedLsThingState != null){
			updatedLsThingState.setRecordedBy(lsState.getRecordedBy());
			updatedLsThingState.setRecordedDate(lsState.getRecordedDate());
			updatedLsThingState.setLsTransaction(lsState.getLsTransaction());
			updatedLsThingState.setModifiedBy(lsState.getModifiedBy());
			updatedLsThingState.setModifiedDate(new Date());
			updatedLsThingState.setLsType(lsState.getLsType());
			updatedLsThingState.setLsKind(lsState.getLsKind());
			updatedLsThingState.setIgnored(lsState.isIgnored());
			updatedLsThingState.merge();
			return updatedLsThingState;
		} else {
			return null;
		}
		
	}

	@Transactional
    public String toJson() {
        return new JSONSerializer().include("lsValues").exclude("*.class", "lsThing").transform(new ExcludeNulls(), void.class).serialize(this);
    }
    
    public static LsThingState fromJsonToLsThingState(String json) {
        return new JSONDeserializer<LsThingState>().use(null, LsThingState.class).deserialize(json);
    }
    
    public static String toJsonArray(Collection<LsThingState> collection) {
        return new JSONSerializer().exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(collection);
    }
    
    public static Collection<LsThingState> fromJsonArrayToLsThingStates(String json) {
        return new JSONDeserializer<List<LsThingState>>().use(null, ArrayList.class).use("values", LsThingState.class).deserialize(json);
    }

    public static Collection<LsThingState> fromJsonArrayToLsThingStates(Reader json) {
        return new JSONDeserializer<List<LsThingState>>().use(null, ArrayList.class).use("values", LsThingState.class).deserialize(json);
    }

	public static TypedQuery<LsThingState> findLsThingStatesByLsThingIDAndStateTypeKind(
			Long lsThingId, String stateType, String stateKind) {
		
		if (stateType == null || stateKind.length() == 0) throw new IllegalArgumentException("The stateType argument is required");
		if (stateKind == null || stateKind.length() == 0) throw new IllegalArgumentException("The stateKind argument is required");
		
		EntityManager em = entityManager();
		String hsqlQuery = "SELECT lsts FROM LsThingState AS lsts " +
		"JOIN lsts.lsThing lst " +
		"WHERE lsts.lsType = :stateType AND lsts.lsKind = :stateKind AND lsts.ignored IS NOT :ignored " +
		"AND lst.id = :lsThingId ";
		TypedQuery<LsThingState> q = em.createQuery(hsqlQuery, LsThingState.class);
		q.setParameter("lsThingId", lsThingId);
		q.setParameter("stateType", stateType);
		q.setParameter("stateKind", stateKind);
		q.setParameter("ignored", true);
		return q;
	}

	public static TypedQuery<LsThingState> findLsThingStatesByLsThingCodeNameAndStateTypeKind(
			String lsThingCodeName, String stateType, String stateKind) {
		if (stateType == null || stateKind.length() == 0) throw new IllegalArgumentException("The stateType argument is required");
		if (stateKind == null || stateKind.length() == 0) throw new IllegalArgumentException("The stateKind argument is required");
		
		EntityManager em = entityManager();
		String hsqlQuery = "SELECT lsts FROM LsThingState AS lsts " +
		"JOIN lsts.lsThing lst " +
		"WHERE lsts.lsType = :stateType AND lsts.lsKind = :stateKind AND lsts.ignored IS NOT :ignored " +
		"AND lst.codeName = :lsThingCodeName ";
		TypedQuery<LsThingState> q = em.createQuery(hsqlQuery, LsThingState.class);
		q.setParameter("lsThingCodeName", lsThingCodeName);
		q.setParameter("stateType", stateType);
		q.setParameter("stateKind", stateKind);
		q.setParameter("ignored", true);
		return q;
	}

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("lsThing", "lsValues");

	public static long countLsThingStates() {
        return entityManager().createQuery("SELECT COUNT(o) FROM LsThingState o", Long.class).getSingleResult();
    }

	public static List<LsThingState> findAllLsThingStates() {
        return entityManager().createQuery("SELECT o FROM LsThingState o", LsThingState.class).getResultList();
    }

	public static List<LsThingState> findAllLsThingStates(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM LsThingState o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, LsThingState.class).getResultList();
    }

	public static LsThingState findLsThingState(Long id) {
        if (id == null) return null;
        return entityManager().find(LsThingState.class, id);
    }

	public static List<LsThingState> findLsThingStateEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM LsThingState o", LsThingState.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<LsThingState> findLsThingStateEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM LsThingState o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, LsThingState.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public LsThingState merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        LsThingState merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public static Long countFindLsThingStatesByLsThing(LsThing lsThing) {
        if (lsThing == null) throw new IllegalArgumentException("The lsThing argument is required");
        EntityManager em = LsThingState.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM LsThingState AS o WHERE o.lsThing = :lsThing", Long.class);
        q.setParameter("lsThing", lsThing);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindLsThingStatesByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = LsThingState.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM LsThingState AS o WHERE o.lsTransaction = :lsTransaction", Long.class);
        q.setParameter("lsTransaction", lsTransaction);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<LsThingState> findLsThingStatesByLsThing(LsThing lsThing) {
        if (lsThing == null) throw new IllegalArgumentException("The lsThing argument is required");
        EntityManager em = LsThingState.entityManager();
        TypedQuery<LsThingState> q = em.createQuery("SELECT o FROM LsThingState AS o WHERE o.lsThing = :lsThing", LsThingState.class);
        q.setParameter("lsThing", lsThing);
        return q;
    }

	public static TypedQuery<LsThingState> findLsThingStatesByLsThing(LsThing lsThing, String sortFieldName, String sortOrder) {
        if (lsThing == null) throw new IllegalArgumentException("The lsThing argument is required");
        EntityManager em = LsThingState.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM LsThingState AS o WHERE o.lsThing = :lsThing");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<LsThingState> q = em.createQuery(queryBuilder.toString(), LsThingState.class);
        q.setParameter("lsThing", lsThing);
        return q;
    }

	public static TypedQuery<LsThingState> findLsThingStatesByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = LsThingState.entityManager();
        TypedQuery<LsThingState> q = em.createQuery("SELECT o FROM LsThingState AS o WHERE o.lsTransaction = :lsTransaction", LsThingState.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }

	public static TypedQuery<LsThingState> findLsThingStatesByLsTransactionEquals(Long lsTransaction, String sortFieldName, String sortOrder) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = LsThingState.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM LsThingState AS o WHERE o.lsTransaction = :lsTransaction");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<LsThingState> q = em.createQuery(queryBuilder.toString(), LsThingState.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }

	public LsThing getLsThing() {
        return this.lsThing;
    }

	public void setLsThing(LsThing lsThing) {
        this.lsThing = lsThing;
    }

	public Set<LsThingValue> getLsValues() {
        return this.lsValues;
    }

	public void setLsValues(Set<LsThingValue> lsValues) {
        this.lsValues = lsValues;
    }
}
