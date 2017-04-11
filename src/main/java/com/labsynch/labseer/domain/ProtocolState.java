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
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
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
}
