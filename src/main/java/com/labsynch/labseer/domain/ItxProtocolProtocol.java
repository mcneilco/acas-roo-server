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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.utils.CustomBigDecimalFactory;
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Entity
@Configurable
public class ItxProtocolProtocol extends AbstractThing {
	
	private static final Logger logger = LoggerFactory.getLogger(ItxProtocolProtocol.class);

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "first_protocol_id")
    private Protocol firstProtocol;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "second_protocol_id")
    private Protocol secondProtocol;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "itxProtocolProtocol", fetch = FetchType.LAZY)
    private Set<ItxProtocolProtocolState> lsStates = new HashSet<ItxProtocolProtocolState>();

    public ItxProtocolProtocol(com.labsynch.labseer.domain.ItxProtocolProtocol itxProtocol) {
    	this.setRecordedBy(itxProtocol.getRecordedBy());
    	this.setRecordedDate(itxProtocol.getRecordedDate());
    	this.setLsTransaction(itxProtocol.getLsTransaction());
    	this.setModifiedBy(itxProtocol.getModifiedBy());
    	this.setModifiedDate(itxProtocol.getModifiedDate());
    	this.setCodeName(itxProtocol.getCodeName());
    	this.setLsType(itxProtocol.getLsType());
    	this.setLsKind(itxProtocol.getLsKind());
    	this.setLsTypeAndKind(itxProtocol.getLsTypeAndKind());
        this.firstProtocol = itxProtocol.getFirstProtocol();
        this.secondProtocol = itxProtocol.getSecondProtocol();
    }
    
    public static ItxProtocolProtocol update(ItxProtocolProtocol object) {
    	ItxProtocolProtocol updatedObject = new JSONDeserializer<ItxProtocolProtocol>().use(null, ItxProtocolProtocol.class).
        		deserializeInto(object.toJson(), 
        				ItxProtocolProtocol.findItxProtocolProtocol(object.getId()));
    	updatedObject.setModifiedDate(new Date());
    	updatedObject.merge();
        return updatedObject;
    }
    
    @Transactional
    public String toJson() {
        return new JSONSerializer().exclude("*.class")
        		.include("firstProtocol","secondProtocol","lsStates.lsValues")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(this);
    }
    
    public static ItxProtocolProtocol fromJsonToItxProtocolProtocol(String json) {
        return new JSONDeserializer<ItxProtocolProtocol>()
        		.use(null, ItxProtocolProtocol.class)
        		.use(BigDecimal.class, new CustomBigDecimalFactory())
        		.deserialize(json);
    }
    
    public static String toJsonArray(Collection<ItxProtocolProtocol> collection) {
        return new JSONSerializer().exclude("*.class")
        		.include("firstProtocol","secondProtocol","lsStates.lsValues")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(collection);
    }
    
    public static Collection<ItxProtocolProtocol> fromJsonArrayToItxProtocolProtocols(String json) {
        return new JSONDeserializer<List<ItxProtocolProtocol>>().use(null, ArrayList.class)
        		.use("values", ItxProtocolProtocol.class)
        		.use(BigDecimal.class, new CustomBigDecimalFactory())
        		.deserialize(json);
    }
    
    public static Collection<ItxProtocolProtocol> fromJsonArrayToItxProtocolProtocols(Reader json) {
        return new JSONDeserializer<List<ItxProtocolProtocol>>().use(null, ArrayList.class)
        		.use("values", ItxProtocolProtocol.class)
        		.use(BigDecimal.class, new CustomBigDecimalFactory())
        		.deserialize(json);
    }
    
    public static ItxProtocolProtocol updateNoStates(ItxProtocolProtocol itxProtocolProtocol) {
    	ItxProtocolProtocol updatedItxProtocolProtocol = ItxProtocolProtocol.findItxProtocolProtocol(itxProtocolProtocol.getId());
    	updatedItxProtocolProtocol.setRecordedBy(itxProtocolProtocol.getRecordedBy());
    	updatedItxProtocolProtocol.setRecordedDate(itxProtocolProtocol.getRecordedDate());
    	updatedItxProtocolProtocol.setIgnored(itxProtocolProtocol.isIgnored());
    	updatedItxProtocolProtocol.setDeleted(itxProtocolProtocol.isDeleted());
    	updatedItxProtocolProtocol.setLsTransaction(itxProtocolProtocol.getLsTransaction());
    	updatedItxProtocolProtocol.setModifiedBy(itxProtocolProtocol.getModifiedBy());
    	updatedItxProtocolProtocol.setCodeName(itxProtocolProtocol.getCodeName());
    	updatedItxProtocolProtocol.setLsType(itxProtocolProtocol.getLsType());
    	updatedItxProtocolProtocol.setLsKind(itxProtocolProtocol.getLsKind());
    	updatedItxProtocolProtocol.setLsTypeAndKind(itxProtocolProtocol.getLsTypeAndKind());
    	updatedItxProtocolProtocol.firstProtocol = Protocol.findProtocol(itxProtocolProtocol.getFirstProtocol().getId());
    	updatedItxProtocolProtocol.secondProtocol = Protocol.findProtocol(itxProtocolProtocol.getSecondProtocol().getId());    	
    	updatedItxProtocolProtocol.setModifiedDate(new Date());
    	updatedItxProtocolProtocol.merge();
    	
    	logger.debug("------------ Just updated the itxProtocolProtocol: ");
    	if(logger.isDebugEnabled()) logger.debug(updatedItxProtocolProtocol.toJson());
    	
        return updatedItxProtocolProtocol;
    }
    
    
    @Transactional
    public static TypedQuery<ItxProtocolProtocol> findItxProtocolProtocolsByFirstProtocol(Protocol firstProtocol) {
        if (firstProtocol == null) throw new IllegalArgumentException("The firstProtocol argument is required");
        EntityManager em = ItxProtocolProtocol.entityManager();
        TypedQuery<ItxProtocolProtocol> q = em.createQuery("SELECT o FROM ItxProtocolProtocol AS o WHERE o.firstProtocol = :firstProtocol", ItxProtocolProtocol.class);
        q.setParameter("firstProtocol", firstProtocol);
        return q;
    }
    
    @Transactional
    public static TypedQuery<ItxProtocolProtocol> findItxProtocolProtocolsByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = ItxProtocolProtocol.entityManager();
        TypedQuery<ItxProtocolProtocol> q = em.createQuery("SELECT o FROM ItxProtocolProtocol AS o WHERE o.lsTransaction = :lsTransaction", ItxProtocolProtocol.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }
    
    @Transactional
    public static TypedQuery<ItxProtocolProtocol> findItxProtocolProtocolsBySecondProtocol(Protocol secondProtocol) {
        if (secondProtocol == null) throw new IllegalArgumentException("The secondProtocol argument is required");
        EntityManager em = ItxProtocolProtocol.entityManager();
        TypedQuery<ItxProtocolProtocol> q = em.createQuery("SELECT o FROM ItxProtocolProtocol AS o WHERE o.secondProtocol = :secondProtocol", ItxProtocolProtocol.class);
        q.setParameter("secondProtocol", secondProtocol);
        return q;
    }

	public ItxProtocolProtocol() {
        super();
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public static Long countFindItxProtocolProtocolsByFirstProtocol(Protocol firstProtocol) {
        if (firstProtocol == null) throw new IllegalArgumentException("The firstProtocol argument is required");
        EntityManager em = ItxProtocolProtocol.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ItxProtocolProtocol AS o WHERE o.firstProtocol = :firstProtocol", Long.class);
        q.setParameter("firstProtocol", firstProtocol);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindItxProtocolProtocolsByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = ItxProtocolProtocol.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ItxProtocolProtocol AS o WHERE o.lsTransaction = :lsTransaction", Long.class);
        q.setParameter("lsTransaction", lsTransaction);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindItxProtocolProtocolsBySecondProtocol(Protocol secondProtocol) {
        if (secondProtocol == null) throw new IllegalArgumentException("The secondProtocol argument is required");
        EntityManager em = ItxProtocolProtocol.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ItxProtocolProtocol AS o WHERE o.secondProtocol = :secondProtocol", Long.class);
        q.setParameter("secondProtocol", secondProtocol);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<ItxProtocolProtocol> findItxProtocolProtocolsByFirstProtocol(Protocol firstProtocol, String sortFieldName, String sortOrder) {
        if (firstProtocol == null) throw new IllegalArgumentException("The firstProtocol argument is required");
        EntityManager em = ItxProtocolProtocol.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ItxProtocolProtocol AS o WHERE o.firstProtocol = :firstProtocol");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ItxProtocolProtocol> q = em.createQuery(queryBuilder.toString(), ItxProtocolProtocol.class);
        q.setParameter("firstProtocol", firstProtocol);
        return q;
    }

	public static TypedQuery<ItxProtocolProtocol> findItxProtocolProtocolsByLsTransactionEquals(Long lsTransaction, String sortFieldName, String sortOrder) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = ItxProtocolProtocol.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ItxProtocolProtocol AS o WHERE o.lsTransaction = :lsTransaction");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ItxProtocolProtocol> q = em.createQuery(queryBuilder.toString(), ItxProtocolProtocol.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }

	public static TypedQuery<ItxProtocolProtocol> findItxProtocolProtocolsBySecondProtocol(Protocol secondProtocol, String sortFieldName, String sortOrder) {
        if (secondProtocol == null) throw new IllegalArgumentException("The secondProtocol argument is required");
        EntityManager em = ItxProtocolProtocol.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ItxProtocolProtocol AS o WHERE o.secondProtocol = :secondProtocol");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ItxProtocolProtocol> q = em.createQuery(queryBuilder.toString(), ItxProtocolProtocol.class);
        q.setParameter("secondProtocol", secondProtocol);
        return q;
    }

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "firstProtocol", "secondProtocol", "lsStates");

	public static long countItxProtocolProtocols() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ItxProtocolProtocol o", Long.class).getSingleResult();
    }

	public static List<ItxProtocolProtocol> findAllItxProtocolProtocols() {
        return entityManager().createQuery("SELECT o FROM ItxProtocolProtocol o", ItxProtocolProtocol.class).getResultList();
    }

	public static List<ItxProtocolProtocol> findAllItxProtocolProtocols(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ItxProtocolProtocol o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ItxProtocolProtocol.class).getResultList();
    }

	public static ItxProtocolProtocol findItxProtocolProtocol(Long id) {
        if (id == null) return null;
        return entityManager().find(ItxProtocolProtocol.class, id);
    }

	public static List<ItxProtocolProtocol> findItxProtocolProtocolEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ItxProtocolProtocol o", ItxProtocolProtocol.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<ItxProtocolProtocol> findItxProtocolProtocolEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ItxProtocolProtocol o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ItxProtocolProtocol.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public ItxProtocolProtocol merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ItxProtocolProtocol merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public Protocol getFirstProtocol() {
        return this.firstProtocol;
    }

	public void setFirstProtocol(Protocol firstProtocol) {
        this.firstProtocol = firstProtocol;
    }

	public Protocol getSecondProtocol() {
        return this.secondProtocol;
    }

	public void setSecondProtocol(Protocol secondProtocol) {
        this.secondProtocol = secondProtocol;
    }

	public Set<ItxProtocolProtocolState> getLsStates() {
        return this.lsStates;
    }

	public void setLsStates(Set<ItxProtocolProtocolState> lsStates) {
        this.lsStates = lsStates;
    }
}
