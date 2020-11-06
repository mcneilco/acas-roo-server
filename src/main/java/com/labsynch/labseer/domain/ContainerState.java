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

import com.labsynch.labseer.utils.CustomBigDecimalFactory;
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Configurable
@Entity
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
//        ContainerState updatedContainerState = new JSONDeserializer<ContainerState>().use(null, ArrayList.class).use("values", ContainerState.class).deserializeInto(containerState.toJson(), ContainerState.findContainerState(containerState.getId()));
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
        return new JSONDeserializer<ContainerState>().use(null, ContainerState.class).deserialize(json);
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
        return new JSONDeserializer<List<ContainerState>>().use(null, ArrayList.class).use("values", ContainerState.class).deserialize(json);
    }
    
    public static Collection<com.labsynch.labseer.domain.ContainerState> fromJsonArrayToContainerStates(Reader json) {
        return new JSONDeserializer<List<ContainerState>>().use(null, ArrayList.class).use("values", ContainerState.class).deserialize(json);
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
	

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("container", "lsValues");

	public static long countContainerStates() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ContainerState o", Long.class).getSingleResult();
    }

	public static List<ContainerState> findAllContainerStates() {
        return entityManager().createQuery("SELECT o FROM ContainerState o", ContainerState.class).getResultList();
    }

	public static List<ContainerState> findAllContainerStates(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ContainerState o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ContainerState.class).getResultList();
    }

	public static ContainerState findContainerState(Long id) {
        if (id == null) return null;
        return entityManager().find(ContainerState.class, id);
    }

	public static List<ContainerState> findContainerStateEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ContainerState o", ContainerState.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<ContainerState> findContainerStateEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ContainerState o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ContainerState.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public ContainerState merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ContainerState merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public Container getContainer() {
        return this.container;
    }

	public void setContainer(Container container) {
        this.container = container;
    }

	public Set<ContainerValue> getLsValues() {
        return this.lsValues;
    }

	public void setLsValues(Set<ContainerValue> lsValues) {
        this.lsValues = lsValues;
    }

	public static Long countFindContainerStatesByContainer(Container container) {
        if (container == null) throw new IllegalArgumentException("The container argument is required");
        EntityManager em = ContainerState.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ContainerState AS o WHERE o.container = :container", Long.class);
        q.setParameter("container", container);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindContainerStatesByContainerAndLsKindEqualsAndIgnoredNot(Container container, String lsKind, boolean ignored) {
        if (container == null) throw new IllegalArgumentException("The container argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = ContainerState.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ContainerState AS o WHERE o.container = :container AND o.lsKind = :lsKind  AND o.ignored IS NOT :ignored", Long.class);
        q.setParameter("container", container);
        q.setParameter("lsKind", lsKind);
        q.setParameter("ignored", ignored);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindContainerStatesByIgnoredNot(boolean ignored) {
        EntityManager em = ContainerState.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ContainerState AS o WHERE o.ignored IS NOT :ignored", Long.class);
        q.setParameter("ignored", ignored);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<ContainerState> findContainerStatesByContainer(Container container) {
        if (container == null) throw new IllegalArgumentException("The container argument is required");
        EntityManager em = ContainerState.entityManager();
        TypedQuery<ContainerState> q = em.createQuery("SELECT o FROM ContainerState AS o WHERE o.container = :container", ContainerState.class);
        q.setParameter("container", container);
        return q;
    }

	public static TypedQuery<ContainerState> findContainerStatesByContainer(Container container, String sortFieldName, String sortOrder) {
        if (container == null) throw new IllegalArgumentException("The container argument is required");
        EntityManager em = ContainerState.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ContainerState AS o WHERE o.container = :container");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ContainerState> q = em.createQuery(queryBuilder.toString(), ContainerState.class);
        q.setParameter("container", container);
        return q;
    }

	public static TypedQuery<ContainerState> findContainerStatesByContainerAndLsKindEqualsAndIgnoredNot(Container container, String lsKind, boolean ignored) {
        if (container == null) throw new IllegalArgumentException("The container argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = ContainerState.entityManager();
        TypedQuery<ContainerState> q = em.createQuery("SELECT o FROM ContainerState AS o WHERE o.container = :container AND o.lsKind = :lsKind  AND o.ignored IS NOT :ignored", ContainerState.class);
        q.setParameter("container", container);
        q.setParameter("lsKind", lsKind);
        q.setParameter("ignored", ignored);
        return q;
    }

	public static TypedQuery<ContainerState> findContainerStatesByContainerAndLsKindEqualsAndIgnoredNot(Container container, String lsKind, boolean ignored, String sortFieldName, String sortOrder) {
        if (container == null) throw new IllegalArgumentException("The container argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = ContainerState.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ContainerState AS o WHERE o.container = :container AND o.lsKind = :lsKind  AND o.ignored IS NOT :ignored");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ContainerState> q = em.createQuery(queryBuilder.toString(), ContainerState.class);
        q.setParameter("container", container);
        q.setParameter("lsKind", lsKind);
        q.setParameter("ignored", ignored);
        return q;
    }

	public static TypedQuery<ContainerState> findContainerStatesByIgnoredNot(boolean ignored) {
        EntityManager em = ContainerState.entityManager();
        TypedQuery<ContainerState> q = em.createQuery("SELECT o FROM ContainerState AS o WHERE o.ignored IS NOT :ignored", ContainerState.class);
        q.setParameter("ignored", ignored);
        return q;
    }

	public static TypedQuery<ContainerState> findContainerStatesByIgnoredNot(boolean ignored, String sortFieldName, String sortOrder) {
        EntityManager em = ContainerState.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ContainerState AS o WHERE o.ignored IS NOT :ignored");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ContainerState> q = em.createQuery(queryBuilder.toString(), ContainerState.class);
        q.setParameter("ignored", ignored);
        return q;
    }

	public ContainerState() {
        super();
    }
}
