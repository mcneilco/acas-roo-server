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
import javax.persistence.TypedQuery;
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
@RooJpaActiveRecord(finders = { "findContainerStatesByContainer", "findContainerStatesByIgnoredNot", "findContainerStatesByContainerAndLsKindEqualsAndIgnoredNot" })
public class ContainerState extends AbstractState {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "container_id")
    private Container container;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lsState", fetch =  FetchType.LAZY)
    private Set<ContainerValue> lsValues = new HashSet<ContainerValue>();

    public ContainerState(com.labsynch.labseer.domain.ContainerState containerState) {
        this.setRecordedBy(containerState.getRecordedBy());
        this.setRecordedDate(containerState.getRecordedDate());
        this.setLsTransaction(containerState.getLsTransaction());
        this.setModifiedBy(containerState.getModifiedBy());
        this.setModifiedDate(containerState.getModifiedDate());
        this.setLsType(containerState.getLsType());
        this.setLsKind(containerState.getLsKind());
        this.setIgnored(containerState.isIgnored());
    }

//    public static com.labsynch.labseer.domain.ContainerState update(com.labsynch.labseer.domain.ContainerState containerState) {
//        ContainerState updatedContainerState = new JSONDeserializer<ContainerState>().use(null, ArrayList.class).use("values", ContainerState.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserializeInto(containerState.toJson(), ContainerState.findContainerState(containerState.getId()));
//        updatedContainerState.setModifiedDate(new Date());
//        updatedContainerState.merge();
//        return updatedContainerState;
//    }
    
    public static ContainerState update(ContainerState lsState) {
		ContainerState updatedContainerState = ContainerState.findContainerState(lsState.getId());
		if (updatedContainerState != null){
			updatedContainerState.setRecordedBy(lsState.getRecordedBy());
			updatedContainerState.setRecordedDate(lsState.getRecordedDate());
			updatedContainerState.setLsTransaction(lsState.getLsTransaction());
			updatedContainerState.setModifiedBy(lsState.getModifiedBy());
			updatedContainerState.setModifiedDate(new Date());
			updatedContainerState.setLsType(lsState.getLsType());
			updatedContainerState.setLsKind(lsState.getLsKind());
			updatedContainerState.setIgnored(lsState.isIgnored());
			updatedContainerState.merge();
			return updatedContainerState;
		} else {
			return null;
		}
		
	}

    @Transactional
    public String toJson() {
        return new JSONSerializer().exclude("*.class", "container").include("lsValues").transform(new ExcludeNulls(), void.class).serialize(this);
    }

    @Transactional
    public String toJsonStub() {
        return new JSONSerializer().exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }

    public static com.labsynch.labseer.domain.ContainerState fromJsonToContainerState(String json) {
        return new JSONDeserializer<ContainerState>().use(null, ContainerState.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
    }

    @Transactional
    public static String toJsonArray(Collection<com.labsynch.labseer.domain.ContainerState> collection) {
        return new JSONSerializer().exclude("*.class", "container", "lsValues.lsState").include("lsValues").transform(new ExcludeNulls(), void.class).serialize(collection);
    }

    @Transactional
    public static String toJsonArrayStub(Collection<com.labsynch.labseer.domain.ContainerState> collection) {
        return new JSONSerializer().exclude("*.class", "container").transform(new ExcludeNulls(), void.class).serialize(collection);
    }
    
    @Transactional
	public static String toJsonArrayWithNestedContainers(Collection<ContainerState> collection) {
        return new JSONSerializer().exclude("*.class", "lsValues.lsState").include("lsValues","container.lsLabels").transform(new ExcludeNulls(), void.class).serialize(collection);
	}

    public static Collection<com.labsynch.labseer.domain.ContainerState> fromJsonArrayToContainerStates(String json) {
        return new JSONDeserializer<List<ContainerState>>().use(null, ArrayList.class).use("values", ContainerState.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
    }
    
    public static Collection<com.labsynch.labseer.domain.ContainerState> fromJsonArrayToContainerStates(Reader json) {
        return new JSONDeserializer<List<ContainerState>>().use(null, ArrayList.class).use("values", ContainerState.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
    }


    public long countValidContainerValues() {
        boolean ignored = true;
        EntityManager em = ContainerValue.entityManager();
        TypedQuery<Long> q = em.createQuery("SELECT COUNT(o) FROM ContainerState AS o WHERE o.ignored IS NOT :ignored", Long.class);
        q.setParameter("ignored", ignored);
        return q.getSingleResult();
    }

    public static List<com.labsynch.labseer.domain.ContainerState> findValidContainerStates(List<Long> containerIds) {
        boolean ignored = true;
        EntityManager em = ContainerState.entityManager();
        TypedQuery<ContainerState> q = em.createQuery("SELECT o FROM ContainerState AS o WHERE o.container.id in (:containerIds) AND o.ignored IS NOT :ignored", ContainerState.class);
        q.setParameter("ignored", ignored);
        q.setParameter("containerIds", containerIds);
        return q.getResultList();
    }

    @Transactional
    public static int ignoreStates(Long transactionId) {
        boolean ignored = true;
        EntityManager em = ContainerState.entityManager();
        Query q = em.createQuery("UPDATE versioned ContainerState AS o set o.ignored = :ignored WHERE o.id " + "IN (  SELECT thing FROM UpdateLog " + "WHERE lsTransaction = :transactionId)");
        q.setParameter("ignored", ignored);
        q.setParameter("transactionId", transactionId);
        int results = q.executeUpdate();
        return results;
    }

	public static TypedQuery<ContainerState> findContainerStatesByContainerIDAndStateTypeKind(Long containerId, 
			String stateType, 
			String stateKind) {
			if (stateType == null || stateKind.length() == 0) throw new IllegalArgumentException("The stateType argument is required");
			if (stateKind == null || stateKind.length() == 0) throw new IllegalArgumentException("The stateKind argument is required");
			
			EntityManager em = entityManager();
			String hsqlQuery = "SELECT cs FROM ContainerState AS cs " +
			"JOIN cs.container c " +
			"WHERE cs.lsType = :stateType AND cs.lsKind = :stateKind AND cs.ignored IS NOT :ignored " +
			"AND c.id = :containerId ";
			TypedQuery<ContainerState> q = em.createQuery(hsqlQuery, ContainerState.class);
			q.setParameter("containerId", containerId);
			q.setParameter("stateType", stateType);
			q.setParameter("stateKind", stateKind);
			q.setParameter("ignored", true);
			return q;
		}

	public static TypedQuery<ContainerState> findContainerStatesByContainerCodeNameAndStateTypeKind(
			String containerCodeName, String stateType, String stateKind) {
		if (stateType == null || stateKind.length() == 0) throw new IllegalArgumentException("The stateType argument is required");
		if (stateKind == null || stateKind.length() == 0) throw new IllegalArgumentException("The stateKind argument is required");
		
		EntityManager em = entityManager();
		String hsqlQuery = "SELECT cs FROM ContainerState AS cs " +
		"JOIN cs.container c " +
		"WHERE cs.lsType = :stateType AND cs.lsKind = :stateKind AND cs.ignored IS NOT :ignored " +
		"AND c.codeName = :containerCodeName ";
		TypedQuery<ContainerState> q = em.createQuery(hsqlQuery, ContainerState.class);
		q.setParameter("containerCodeName", containerCodeName);
		q.setParameter("stateType", stateType);
		q.setParameter("stateKind", stateKind);
		q.setParameter("ignored", true);
		return q;
	}
	
}
