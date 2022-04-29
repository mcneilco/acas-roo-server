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
@RooJpaActiveRecord(finders = { "findItxContainerContainersByLsTransactionEquals", "findItxContainerContainersByLsTypeEqualsAndFirstContainerEquals", "findItxContainerContainersByLsTypeEqualsAndSecondContainerEquals" })
public class ItxContainerContainer extends AbstractThing {

	private static final Logger logger = LoggerFactory.getLogger(ItxContainerContainer.class);

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "first_container_id")
    private Container firstContainer;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "second_container_id")
    private Container secondContainer;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "itxContainerContainer", fetch = FetchType.LAZY)
    private Set<ItxContainerContainerState> lsStates = new HashSet<ItxContainerContainerState>();

    public ItxContainerContainer(com.labsynch.labseer.domain.ItxContainerContainer itxContainer) {
    	this.setRecordedBy(itxContainer.getRecordedBy());
    	this.setRecordedDate(itxContainer.getRecordedDate());
    	this.setLsTransaction(itxContainer.getLsTransaction());
    	this.setModifiedBy(itxContainer.getModifiedBy());
    	this.setModifiedDate(itxContainer.getModifiedDate());
    	this.setCodeName(itxContainer.getCodeName());
    	this.setLsType(itxContainer.getLsType());
    	this.setLsKind(itxContainer.getLsKind());
    	this.setLsTypeAndKind(itxContainer.getLsTypeAndKind());
        this.firstContainer = itxContainer.getFirstContainer();
        this.secondContainer = itxContainer.getSecondContainer();
    }
    
    public static ItxContainerContainer update(ItxContainerContainer object) {
    	ItxContainerContainer updatedObject = new JSONDeserializer<ItxContainerContainer>().use(null, ItxContainerContainer.class).
        		deserializeInto(object.toJson(), 
        				ItxContainerContainer.findItxContainerContainer(object.getId()));
    	updatedObject.setModifiedDate(new Date());
    	updatedObject.merge();
        return updatedObject;
    }
    
    public String toJson() {
        return new JSONSerializer().exclude("*.class")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(this);
    }
    
    public static ItxContainerContainer fromJsonToItxContainerContainer(String json) {
        return new JSONDeserializer<ItxContainerContainer>()
        		.use(null, ItxContainerContainer.class)
        		.use(BigDecimal.class, new CustomBigDecimalFactory())
        		.deserialize(json);
    }
    
    public static String toJsonArray(Collection<ItxContainerContainer> collection) {
        return new JSONSerializer().exclude("*.class")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(collection);
    }
    
    public static Collection<ItxContainerContainer> fromJsonArrayToItxContainerContainers(String json) {
        return new JSONDeserializer<List<ItxContainerContainer>>().use(null, ArrayList.class)
        		.use("values", ItxContainerContainer.class)
        		.use(BigDecimal.class, new CustomBigDecimalFactory())
        		.deserialize(json);
    }
    
    public static Collection<ItxContainerContainer> fromJsonArrayToItxContainerContainers(Reader json) {
        return new JSONDeserializer<List<ItxContainerContainer>>().use(null, ArrayList.class)
        		.use("values", ItxContainerContainer.class)
        		.use(BigDecimal.class, new CustomBigDecimalFactory())
        		.deserialize(json);
    }
    
    public static ItxContainerContainer updateNoStates(ItxContainerContainer itxContainerContainer) {
    	ItxContainerContainer updatedItxContainerContainer = ItxContainerContainer.findItxContainerContainer(itxContainerContainer.getId());
    	updatedItxContainerContainer.setRecordedBy(itxContainerContainer.getRecordedBy());
    	updatedItxContainerContainer.setRecordedDate(itxContainerContainer.getRecordedDate());
    	updatedItxContainerContainer.setIgnored(itxContainerContainer.isIgnored());
    	updatedItxContainerContainer.setDeleted(itxContainerContainer.isDeleted());
    	updatedItxContainerContainer.setLsTransaction(itxContainerContainer.getLsTransaction());
    	updatedItxContainerContainer.setModifiedBy(itxContainerContainer.getModifiedBy());
    	updatedItxContainerContainer.setCodeName(itxContainerContainer.getCodeName());
    	updatedItxContainerContainer.setLsType(itxContainerContainer.getLsType());
    	updatedItxContainerContainer.setLsKind(itxContainerContainer.getLsKind());
    	updatedItxContainerContainer.setLsTypeAndKind(itxContainerContainer.getLsTypeAndKind());
    	updatedItxContainerContainer.firstContainer = Container.findContainer(itxContainerContainer.getFirstContainer().getId());
    	updatedItxContainerContainer.secondContainer = Container.findContainer(itxContainerContainer.getSecondContainer().getId());    	
    	updatedItxContainerContainer.setModifiedDate(new Date());
    	updatedItxContainerContainer.merge();
    	
    	logger.debug("------------ Just updated the itxContainerContainer: ");
    	if(logger.isDebugEnabled()) logger.debug(updatedItxContainerContainer.toJson());
    	
        return updatedItxContainerContainer;
    }
    
    public static TypedQuery<ItxContainerContainer> findItxContainerContainersByLsTypeEqualsAndLsKindEqualsAndFirstContainerEquals(String lsType, String lsKind, Container firstContainer){
    	if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        if (firstContainer == null) throw new IllegalArgumentException("The firstContainer argument is required");
        
        boolean ignored = true;
        
        EntityManager em = ItxContainerContainer.entityManager();
		String query = "SELECT DISTINCT o FROM ItxContainerContainer o " +
				"WHERE o.ignored IS NOT :ignored " +
				"AND o.lsType = :lsType " +
				"AND o.lsKind = :lsKind " +
				"AND o.firstContainer = :firstContainer ";
        
        TypedQuery<ItxContainerContainer> q = em.createQuery(query, ItxContainerContainer.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        q.setParameter("firstContainer", firstContainer);        
        q.setParameter("ignored", ignored);
        
        return q;
    }
    
    public static TypedQuery<ItxContainerContainer> findItxContainerContainersByLsTypeEqualsAndLsKindEqualsAndSecondContainerEquals(String lsType, String lsKind, Container secondContainer){
    	if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        if (secondContainer == null) throw new IllegalArgumentException("The secondContainer argument is required");
        
        boolean ignored = true;
        
        EntityManager em = ItxContainerContainer.entityManager();
		String query = "SELECT DISTINCT o FROM ItxContainerContainer o " +
				"WHERE o.ignored IS NOT :ignored " +
				"AND o.lsType = :lsType " +
				"AND o.lsKind = :lsKind " +
				"AND o.secondContainer = :secondContainer ";
        
        TypedQuery<ItxContainerContainer> q = em.createQuery(query, ItxContainerContainer.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        q.setParameter("secondContainer", secondContainer);        
        q.setParameter("ignored", ignored);
        
        return q;
    }
    
    public static TypedQuery<ItxContainerContainer> findItxContainerContainersByLsTypeEqualsAndLsKindEqualsAndSecondContainerEqualsAndOrderEquals(String lsType, String lsKind, Container secondContainer, int order){
    	if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        if (secondContainer == null) throw new IllegalArgumentException("The secondContainer argument is required");
        
        boolean ignored = true;
        
        EntityManager em = ItxContainerContainer.entityManager();
		String query = "SELECT DISTINCT o FROM ItxContainerContainer o " +
				"JOIN o.lsStates itxstate JOIN itxstate.lsValues itxvalue WITH itxvalue.lsKind = 'order' " +
				"WHERE o.ignored IS NOT :ignored " +
				"AND o.lsType = :lsType " +
				"AND o.lsKind = :lsKind " +
				"AND itxvalue.numericValue = :order " +
				"AND o.secondContainer = :secondContainer ";
        
        TypedQuery<ItxContainerContainer> q = em.createQuery(query, ItxContainerContainer.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        q.setParameter("order", new BigDecimal(order));
        q.setParameter("secondContainer", secondContainer);        
        q.setParameter("ignored", ignored);
        
        return q;
    }
    
    public static TypedQuery<ItxContainerContainer> findItxContainerContainersByLsTypeEqualsAndLsKindEqualsAndFirstContainerEqualsAndSecondContainerEquals(String lsType, String lsKind, Container firstContainer, Container secondContainer){
    	if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        if (firstContainer == null) throw new IllegalArgumentException("The firstContainer argument is required");
        if (secondContainer == null) throw new IllegalArgumentException("The secondContainer argument is required");
        
        boolean ignored = true;
        
        EntityManager em = ItxContainerContainer.entityManager();
		String query = "SELECT DISTINCT o FROM ItxContainerContainer o " +
				"WHERE o.ignored IS NOT :ignored " +
				"AND o.lsType = :lsType " +
				"AND o.lsKind = :lsKind " +
				"AND o.firstContainer = :firstContainer " +
				"AND o.secondContainer = :secondContainer ";
        
        TypedQuery<ItxContainerContainer> q = em.createQuery(query, ItxContainerContainer.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        q.setParameter("firstContainer", firstContainer);        
        q.setParameter("secondContainer", secondContainer);        
        q.setParameter("ignored", ignored);
        
        return q;
    }

	public static Long countFindItxContainerContainersByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = ItxContainerContainer.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ItxContainerContainer AS o WHERE o.lsTransaction = :lsTransaction", Long.class);
        q.setParameter("lsTransaction", lsTransaction);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindItxContainerContainersByLsTypeEqualsAndFirstContainerEquals(String lsType, Container firstContainer) {
        if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (firstContainer == null) throw new IllegalArgumentException("The firstContainer argument is required");
        EntityManager em = ItxContainerContainer.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ItxContainerContainer AS o WHERE o.lsType = :lsType  AND o.firstContainer = :firstContainer", Long.class);
        q.setParameter("lsType", lsType);
        q.setParameter("firstContainer", firstContainer);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindItxContainerContainersByLsTypeEqualsAndSecondContainerEquals(String lsType, Container secondContainer) {
        if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (secondContainer == null) throw new IllegalArgumentException("The secondContainer argument is required");
        EntityManager em = ItxContainerContainer.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ItxContainerContainer AS o WHERE o.lsType = :lsType  AND o.secondContainer = :secondContainer", Long.class);
        q.setParameter("lsType", lsType);
        q.setParameter("secondContainer", secondContainer);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<ItxContainerContainer> findItxContainerContainersByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = ItxContainerContainer.entityManager();
        TypedQuery<ItxContainerContainer> q = em.createQuery("SELECT o FROM ItxContainerContainer AS o WHERE o.lsTransaction = :lsTransaction", ItxContainerContainer.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }

	public static TypedQuery<ItxContainerContainer> findItxContainerContainersByLsTransactionEquals(Long lsTransaction, String sortFieldName, String sortOrder) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = ItxContainerContainer.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ItxContainerContainer AS o WHERE o.lsTransaction = :lsTransaction");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ItxContainerContainer> q = em.createQuery(queryBuilder.toString(), ItxContainerContainer.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }

	public static TypedQuery<ItxContainerContainer> findItxContainerContainersByLsTypeEqualsAndFirstContainerEquals(String lsType, Container firstContainer) {
        if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (firstContainer == null) throw new IllegalArgumentException("The firstContainer argument is required");
        EntityManager em = ItxContainerContainer.entityManager();
        TypedQuery<ItxContainerContainer> q = em.createQuery("SELECT o FROM ItxContainerContainer AS o WHERE o.lsType = :lsType  AND o.firstContainer = :firstContainer", ItxContainerContainer.class);
        q.setParameter("lsType", lsType);
        q.setParameter("firstContainer", firstContainer);
        return q;
    }

	public static TypedQuery<ItxContainerContainer> findItxContainerContainersByLsTypeEqualsAndFirstContainerEquals(String lsType, Container firstContainer, String sortFieldName, String sortOrder) {
        if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (firstContainer == null) throw new IllegalArgumentException("The firstContainer argument is required");
        EntityManager em = ItxContainerContainer.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ItxContainerContainer AS o WHERE o.lsType = :lsType  AND o.firstContainer = :firstContainer");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ItxContainerContainer> q = em.createQuery(queryBuilder.toString(), ItxContainerContainer.class);
        q.setParameter("lsType", lsType);
        q.setParameter("firstContainer", firstContainer);
        return q;
    }

	public static TypedQuery<ItxContainerContainer> findItxContainerContainersByLsTypeEqualsAndSecondContainerEquals(String lsType, Container secondContainer) {
        if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (secondContainer == null) throw new IllegalArgumentException("The secondContainer argument is required");
        EntityManager em = ItxContainerContainer.entityManager();
        TypedQuery<ItxContainerContainer> q = em.createQuery("SELECT o FROM ItxContainerContainer AS o WHERE o.lsType = :lsType  AND o.secondContainer = :secondContainer", ItxContainerContainer.class);
        q.setParameter("lsType", lsType);
        q.setParameter("secondContainer", secondContainer);
        return q;
    }

	public static TypedQuery<ItxContainerContainer> findItxContainerContainersByLsTypeEqualsAndSecondContainerEquals(String lsType, Container secondContainer, String sortFieldName, String sortOrder) {
        if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (secondContainer == null) throw new IllegalArgumentException("The secondContainer argument is required");
        EntityManager em = ItxContainerContainer.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ItxContainerContainer AS o WHERE o.lsType = :lsType  AND o.secondContainer = :secondContainer");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ItxContainerContainer> q = em.createQuery(queryBuilder.toString(), ItxContainerContainer.class);
        q.setParameter("lsType", lsType);
        q.setParameter("secondContainer", secondContainer);
        return q;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public ItxContainerContainer() {
        super();
    }

	public Container getFirstContainer() {
        return this.firstContainer;
    }

	public void setFirstContainer(Container firstContainer) {
        this.firstContainer = firstContainer;
    }

	public Container getSecondContainer() {
        return this.secondContainer;
    }

	public void setSecondContainer(Container secondContainer) {
        this.secondContainer = secondContainer;
    }

	public Set<ItxContainerContainerState> getLsStates() {
        return this.lsStates;
    }

	public void setLsStates(Set<ItxContainerContainerState> lsStates) {
        this.lsStates = lsStates;
    }

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "firstContainer", "secondContainer", "lsStates");

	public static long countItxContainerContainers() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ItxContainerContainer o", Long.class).getSingleResult();
    }

	public static List<ItxContainerContainer> findAllItxContainerContainers() {
        return entityManager().createQuery("SELECT o FROM ItxContainerContainer o", ItxContainerContainer.class).getResultList();
    }

	public static List<ItxContainerContainer> findAllItxContainerContainers(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ItxContainerContainer o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ItxContainerContainer.class).getResultList();
    }

	public static ItxContainerContainer findItxContainerContainer(Long id) {
        if (id == null) return null;
        return entityManager().find(ItxContainerContainer.class, id);
    }

	public static List<ItxContainerContainer> findItxContainerContainerEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ItxContainerContainer o", ItxContainerContainer.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<ItxContainerContainer> findItxContainerContainerEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ItxContainerContainer o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ItxContainerContainer.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public ItxContainerContainer merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ItxContainerContainer merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
