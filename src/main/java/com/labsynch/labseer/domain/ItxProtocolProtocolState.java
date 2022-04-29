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
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

public class ItxProtocolProtocolState extends AbstractState {

    public ItxProtocolProtocolState(ItxProtocolProtocolState itxState) {
		this.setRecordedBy(itxState.getRecordedBy());
		this.setRecordedDate(itxState.getRecordedDate());
		this.setLsTransaction(itxState.getLsTransaction());
		this.setModifiedBy(itxState.getModifiedBy());
		this.setModifiedDate(itxState.getModifiedDate());
		
    }
    
    public static ItxProtocolProtocolState update(ItxProtocolProtocolState object) {
    	ItxProtocolProtocolState updatedObject = new JSONDeserializer<ItxProtocolProtocolState>().use(null, ItxProtocolProtocolState.class).
        		deserializeInto(object.toJson(), 
        				ItxProtocolProtocolState.findItxProtocolProtocolState(object.getId()));
    	updatedObject.setModifiedDate(new Date());
    	updatedObject.merge();
        return updatedObject;
    }

	@ManyToOne
	@JoinColumn(name = "itx_protocol_protocol")
    private ItxProtocolProtocol itxProtocolProtocol;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lsState",  fetch =  FetchType.LAZY)
    private Set<ItxProtocolProtocolValue> lsValues = new HashSet<ItxProtocolProtocolValue>();
    
    
	@Transactional
    public String toJson() {
        return new JSONSerializer().exclude("*.class")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(this);
    }

	@Transactional
    public String toJsonStub() {
        return new JSONSerializer().exclude("*.class", "itxProtocolProtocol")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(this);
    }

    public static ItxProtocolProtocolState fromJsonToItxContainerContainerState(String json) {
        return new JSONDeserializer<ItxProtocolProtocolState>().use(null, ItxProtocolProtocolState.class)
        		.use(BigDecimal.class, new CustomBigDecimalFactory())
        		.deserialize(json);
    }
    
	@Transactional
    public static String toJsonArray(Collection<ItxProtocolProtocolState> collection) {
        return new JSONSerializer().exclude("*.class")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(collection);
    }

	@Transactional
    public static String toJsonArrayStub(Collection<ItxProtocolProtocolState> collection) {
        return new JSONSerializer().exclude("*.class", "itxProtocolProtocol")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(collection);
    }
    
    public static Collection<ItxProtocolProtocolState> fromJsonArrayToItxProtocolProtocolStates(String json) {
        return new JSONDeserializer<List<ItxProtocolProtocolState>>().use(null, ArrayList.class)
        		.use("values", ItxProtocolProtocolState.class)
        		.use(BigDecimal.class, new CustomBigDecimalFactory())
        		.deserialize(json);
    }

    public static Collection<ItxProtocolProtocolState> fromJsonArrayToItxProtocolProtocolStates(Reader json) {
        return new JSONDeserializer<List<ItxProtocolProtocolState>>().use(null, ArrayList.class)
        		.use("values", ItxProtocolProtocolState.class)
        		.use(BigDecimal.class, new CustomBigDecimalFactory())
        		.deserialize(json);
    }



	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("itxProtocolProtocol", "lsValues");

	public static long countItxProtocolProtocolStates() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ItxProtocolProtocolState o", Long.class).getSingleResult();
    }

	public static List<ItxProtocolProtocolState> findAllItxProtocolProtocolStates() {
        return entityManager().createQuery("SELECT o FROM ItxProtocolProtocolState o", ItxProtocolProtocolState.class).getResultList();
    }

	public static List<ItxProtocolProtocolState> findAllItxProtocolProtocolStates(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ItxProtocolProtocolState o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ItxProtocolProtocolState.class).getResultList();
    }

	public static ItxProtocolProtocolState findItxProtocolProtocolState(Long id) {
        if (id == null) return null;
        return entityManager().find(ItxProtocolProtocolState.class, id);
    }

	public static List<ItxProtocolProtocolState> findItxProtocolProtocolStateEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ItxProtocolProtocolState o", ItxProtocolProtocolState.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<ItxProtocolProtocolState> findItxProtocolProtocolStateEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ItxProtocolProtocolState o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ItxProtocolProtocolState.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public ItxProtocolProtocolState merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ItxProtocolProtocolState merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public ItxProtocolProtocolState() {
        super();
    }

	public ItxProtocolProtocol getItxProtocolProtocol() {
        return this.itxProtocolProtocol;
    }

	public void setItxProtocolProtocol(ItxProtocolProtocol itxProtocolProtocol) {
        this.itxProtocolProtocol = itxProtocolProtocol;
    }

	public Set<ItxProtocolProtocolValue> getLsValues() {
        return this.lsValues;
    }

	public void setLsValues(Set<ItxProtocolProtocolValue> lsValues) {
        this.lsValues = lsValues;
    }

	public static ItxProtocolProtocolState fromJsonToItxProtocolProtocolState(String json) {
        return new JSONDeserializer<ItxProtocolProtocolState>()
        .use(null, ItxProtocolProtocolState.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
