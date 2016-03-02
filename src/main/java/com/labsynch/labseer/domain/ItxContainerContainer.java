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

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.utils.CustomBigDecimalFactory;
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = { "findItxContainerContainersByLsTransactionEquals", "findItxContainerContainersByLsTypeEqualsAndFirstContainerEquals", "findItxContainerContainersByLsTypeEqualsAndSecondContainerEquals" })
public class ItxContainerContainer extends AbstractThing {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @org.hibernate.annotations.Index(name = "ITX_CNTR_CNTR_FIRST_CONTAINER")
    @JoinColumn(name = "first_container_id")
    private Container firstContainer;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @org.hibernate.annotations.Index(name = "ITX_CNTR_CNTR_SECOND_CONTAINER")
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
        		use(BigDecimal.class, new CustomBigDecimalFactory()).deserializeInto(object.toJson(), 
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
}
