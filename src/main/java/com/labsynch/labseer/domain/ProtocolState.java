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
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.utils.CustomBigDecimalFactory;
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Entity
@Configurable
@RooJavaBean
@RooToString
@RooJson
@Transactional
@RooJpaActiveRecord(finders = { "findProtocolStatesByProtocol", "findProtocolStatesByLsTransactionEquals" })
public class ProtocolState extends AbstractState {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "protocol_id")
    private Protocol protocol;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lsState", fetch = FetchType.LAZY)
    private Set<ProtocolValue> lsValues = new HashSet<ProtocolValue>();

    public ProtocolState(com.labsynch.labseer.domain.ProtocolState protocolState) {
        super.setRecordedBy(protocolState.getRecordedBy());
        super.setRecordedDate(protocolState.getRecordedDate());
        super.setLsTransaction(protocolState.getLsTransaction());
        super.setModifiedBy(protocolState.getModifiedBy());
        super.setModifiedDate(protocolState.getModifiedDate());
        super.setLsType(protocolState.getLsType());
        super.setLsKind(protocolState.getLsKind());
    }

	public static ProtocolState update(ProtocolState protocolState) {
		ProtocolState updatedProtocolState = ProtocolState.findProtocolState(protocolState.getId());
		if (updatedProtocolState != null){
			updatedProtocolState.setRecordedBy(protocolState.getRecordedBy());
			updatedProtocolState.setRecordedDate(protocolState.getRecordedDate());
			updatedProtocolState.setLsTransaction(protocolState.getLsTransaction());
			updatedProtocolState.setModifiedBy(protocolState.getModifiedBy());
			updatedProtocolState.setModifiedDate(new Date());
			updatedProtocolState.setLsType(protocolState.getLsType());
			updatedProtocolState.setLsKind(protocolState.getLsKind());
			updatedProtocolState.setIgnored(protocolState.isIgnored());
			updatedProtocolState.merge();
			return updatedProtocolState;
		} else {
			return null;
		}
		
	}
    
    @Transactional
    public String toJson() {
        return new JSONSerializer().include("lsValues").exclude("*.class", "protocol").transform(new ExcludeNulls(), void.class).serialize(this);
    }
    
    public static ProtocolState fromJsonToProtocolState(String json) {
        return new JSONDeserializer<ProtocolState>().use(null, ProtocolState.class).deserialize(json);
    }
    
    public static String toJsonArray(Collection<ProtocolState> collection) {
        return new JSONSerializer().exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(collection);
    }
    
    public static Collection<ProtocolState> fromJsonArrayToProtocolStates(String json) {
        return new JSONDeserializer<List<ProtocolState>>().use(null, ArrayList.class).use("values", ProtocolState.class).deserialize(json);
    }

    public static Collection<ProtocolState> fromJsonArrayToProtocolStates(Reader json) {
        return new JSONDeserializer<List<ProtocolState>>().use(null, ArrayList.class).use("values", ProtocolState.class).deserialize(json);
    }

	public static TypedQuery<ProtocolState> findProtocolStatesByProtocolIDAndStateTypeKind(
			Long protocolId, String stateType, String stateKind) {

		if (stateType == null || stateKind.length() == 0) throw new IllegalArgumentException("The stateType argument is required");
		if (stateKind == null || stateKind.length() == 0) throw new IllegalArgumentException("The stateKind argument is required");
		
		EntityManager em = entityManager();
		String hsqlQuery = "SELECT ps FROM ProtocolState AS ps " +
		"JOIN ps.protocol p " +
		"WHERE ps.lsType = :stateType AND ps.lsKind = :stateKind AND ps.ignored IS NOT :ignored " +
		"AND p.id = :protocolId ";
		TypedQuery<ProtocolState> q = em.createQuery(hsqlQuery, ProtocolState.class);
		q.setParameter("protocolId", protocolId);
		q.setParameter("stateType", stateType);
		q.setParameter("stateKind", stateKind);
		q.setParameter("ignored", true);
		return q;
	}

	@Transactional
	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public static TypedQuery<ProtocolState> findProtocolStatesByProtocolCodeNameAndStateTypeKind(
			String protocolCodeName, String stateType, String stateKind) {
		if (stateType == null || stateKind.length() == 0) throw new IllegalArgumentException("The stateType argument is required");
		if (stateKind == null || stateKind.length() == 0) throw new IllegalArgumentException("The stateKind argument is required");
		
		EntityManager em = entityManager();
		String hsqlQuery = "SELECT ps FROM ProtocolState AS ps " +
		"JOIN ps.protocol p " +
		"WHERE ps.lsType = :stateType AND ps.lsKind = :stateKind AND ps.ignored IS NOT :ignored " +
		"AND p.codeName = :protocolCodeName ";
		TypedQuery<ProtocolState> q = em.createQuery(hsqlQuery, ProtocolState.class);
		q.setParameter("protocolCodeName", protocolCodeName);
		q.setParameter("stateType", stateType);
		q.setParameter("stateKind", stateKind);
		q.setParameter("ignored", true);
		return q;
	}

	public Protocol getProtocol() {
        return this.protocol;
    }

	public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

	public Set<ProtocolValue> getLsValues() {
        return this.lsValues;
    }

	public void setLsValues(Set<ProtocolValue> lsValues) {
        this.lsValues = lsValues;
    }

	public static Long countFindProtocolStatesByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = ProtocolState.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ProtocolState AS o WHERE o.lsTransaction = :lsTransaction", Long.class);
        q.setParameter("lsTransaction", lsTransaction);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindProtocolStatesByProtocol(Protocol protocol) {
        if (protocol == null) throw new IllegalArgumentException("The protocol argument is required");
        EntityManager em = ProtocolState.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ProtocolState AS o WHERE o.protocol = :protocol", Long.class);
        q.setParameter("protocol", protocol);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<ProtocolState> findProtocolStatesByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = ProtocolState.entityManager();
        TypedQuery<ProtocolState> q = em.createQuery("SELECT o FROM ProtocolState AS o WHERE o.lsTransaction = :lsTransaction", ProtocolState.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }

	public static TypedQuery<ProtocolState> findProtocolStatesByLsTransactionEquals(Long lsTransaction, String sortFieldName, String sortOrder) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = ProtocolState.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ProtocolState AS o WHERE o.lsTransaction = :lsTransaction");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ProtocolState> q = em.createQuery(queryBuilder.toString(), ProtocolState.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }

	public static TypedQuery<ProtocolState> findProtocolStatesByProtocol(Protocol protocol) {
        if (protocol == null) throw new IllegalArgumentException("The protocol argument is required");
        EntityManager em = ProtocolState.entityManager();
        TypedQuery<ProtocolState> q = em.createQuery("SELECT o FROM ProtocolState AS o WHERE o.protocol = :protocol", ProtocolState.class);
        q.setParameter("protocol", protocol);
        return q;
    }

	public static TypedQuery<ProtocolState> findProtocolStatesByProtocol(Protocol protocol, String sortFieldName, String sortOrder) {
        if (protocol == null) throw new IllegalArgumentException("The protocol argument is required");
        EntityManager em = ProtocolState.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ProtocolState AS o WHERE o.protocol = :protocol");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ProtocolState> q = em.createQuery(queryBuilder.toString(), ProtocolState.class);
        q.setParameter("protocol", protocol);
        return q;
    }

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("protocol", "lsValues");

	public static long countProtocolStates() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ProtocolState o", Long.class).getSingleResult();
    }

	public static List<ProtocolState> findAllProtocolStates() {
        return entityManager().createQuery("SELECT o FROM ProtocolState o", ProtocolState.class).getResultList();
    }

	public static List<ProtocolState> findAllProtocolStates(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ProtocolState o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ProtocolState.class).getResultList();
    }

	public static ProtocolState findProtocolState(Long id) {
        if (id == null) return null;
        return entityManager().find(ProtocolState.class, id);
    }

	public static List<ProtocolState> findProtocolStateEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ProtocolState o", ProtocolState.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<ProtocolState> findProtocolStateEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ProtocolState o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ProtocolState.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public ProtocolState merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ProtocolState merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public ProtocolState() {
        super();
    }
}
