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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
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
@RooJpaActiveRecord(finders = { "findItxLsThingLsThingsByCodeNameEquals", "findItxLsThingLsThingsByFirstLsThing", "findItxLsThingLsThingsBySecondLsThing" })
public class ItxLsThingLsThing extends AbstractThing {

	private static final Logger logger = LoggerFactory.getLogger(ItxLsThingLsThing.class);

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "first_ls_thing_id")
    private LsThing firstLsThing;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "second_ls_thing_id")
    private LsThing secondLsThing;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "itxLsThingLsThing", fetch = FetchType.LAZY)
    private Set<ItxLsThingLsThingState> lsStates = new HashSet<ItxLsThingLsThingState>();

    public ItxLsThingLsThing(com.labsynch.labseer.domain.ItxLsThingLsThing itxLsThingLsThing) {
    	this.setRecordedBy(itxLsThingLsThing.getRecordedBy());
    	this.setRecordedDate(itxLsThingLsThing.getRecordedDate());
    	this.setIgnored(itxLsThingLsThing.isIgnored());
    	this.setDeleted(itxLsThingLsThing.isDeleted());
    	this.setLsTransaction(itxLsThingLsThing.getLsTransaction());
    	this.setModifiedBy(itxLsThingLsThing.getModifiedBy());
    	this.setModifiedDate(itxLsThingLsThing.getModifiedDate());
    	this.setCodeName(itxLsThingLsThing.getCodeName());
    	this.setLsType(itxLsThingLsThing.getLsType());
    	this.setLsKind(itxLsThingLsThing.getLsKind());
    	this.setLsTypeAndKind(itxLsThingLsThing.getLsTypeAndKind());
        this.firstLsThing = LsThing.findLsThing(itxLsThingLsThing.getFirstLsThing().getId());
        this.secondLsThing = LsThing.findLsThing(itxLsThingLsThing.getSecondLsThing().getId());
    }
        
    public static ItxLsThingLsThing update(ItxLsThingLsThing itxLsThingLsThing) {
    	ItxLsThingLsThing updatedItxLsThingLsThing = ItxLsThingLsThing.findItxLsThingLsThing(itxLsThingLsThing.getId());
    	updatedItxLsThingLsThing.setRecordedBy(itxLsThingLsThing.getRecordedBy());
    	updatedItxLsThingLsThing.setRecordedDate(itxLsThingLsThing.getRecordedDate());
    	updatedItxLsThingLsThing.setIgnored(itxLsThingLsThing.isIgnored());
    	updatedItxLsThingLsThing.setDeleted(itxLsThingLsThing.isDeleted());
    	updatedItxLsThingLsThing.setLsTransaction(itxLsThingLsThing.getLsTransaction());
    	updatedItxLsThingLsThing.setModifiedBy(itxLsThingLsThing.getModifiedBy());
    	updatedItxLsThingLsThing.setCodeName(itxLsThingLsThing.getCodeName());
    	updatedItxLsThingLsThing.setLsType(itxLsThingLsThing.getLsType());
    	updatedItxLsThingLsThing.setLsKind(itxLsThingLsThing.getLsKind());
    	updatedItxLsThingLsThing.setLsTypeAndKind(itxLsThingLsThing.getLsTypeAndKind());
    	updatedItxLsThingLsThing.firstLsThing = LsThing.findLsThing(itxLsThingLsThing.getFirstLsThing().getId());
    	updatedItxLsThingLsThing.secondLsThing = LsThing.findLsThing(itxLsThingLsThing.getSecondLsThing().getId());    	
    	updatedItxLsThingLsThing.setModifiedDate(new Date());
    	updatedItxLsThingLsThing.setLsStates(itxLsThingLsThing.getLsStates());
    	updatedItxLsThingLsThing.merge();
        return updatedItxLsThingLsThing;
    }
    
    public static ItxLsThingLsThing updateNoMerge(ItxLsThingLsThing itxLsThingLsThing) {
    	ItxLsThingLsThing updatedItxLsThingLsThing = ItxLsThingLsThing.findItxLsThingLsThing(itxLsThingLsThing.getId());
    	updatedItxLsThingLsThing.setRecordedBy(itxLsThingLsThing.getRecordedBy());
    	updatedItxLsThingLsThing.setRecordedDate(itxLsThingLsThing.getRecordedDate());
    	updatedItxLsThingLsThing.setIgnored(itxLsThingLsThing.isIgnored());
    	updatedItxLsThingLsThing.setDeleted(itxLsThingLsThing.isDeleted());
    	updatedItxLsThingLsThing.setLsTransaction(itxLsThingLsThing.getLsTransaction());
    	updatedItxLsThingLsThing.setModifiedBy(itxLsThingLsThing.getModifiedBy());
    	updatedItxLsThingLsThing.setCodeName(itxLsThingLsThing.getCodeName());
    	updatedItxLsThingLsThing.setLsType(itxLsThingLsThing.getLsType());
    	updatedItxLsThingLsThing.setLsKind(itxLsThingLsThing.getLsKind());
    	updatedItxLsThingLsThing.setLsTypeAndKind(itxLsThingLsThing.getLsTypeAndKind());
    	updatedItxLsThingLsThing.firstLsThing = LsThing.findLsThing(itxLsThingLsThing.getFirstLsThing().getId());
    	updatedItxLsThingLsThing.secondLsThing = LsThing.findLsThing(itxLsThingLsThing.getSecondLsThing().getId());    	
    	updatedItxLsThingLsThing.setModifiedDate(new Date());
    	updatedItxLsThingLsThing.setLsStates(itxLsThingLsThing.getLsStates());
        return updatedItxLsThingLsThing;
    }
    
    public static ItxLsThingLsThing updateNoStates(ItxLsThingLsThing itxLsThingLsThing) {
    	ItxLsThingLsThing updatedItxLsThingLsThing = ItxLsThingLsThing.findItxLsThingLsThing(itxLsThingLsThing.getId());
    	updatedItxLsThingLsThing.setRecordedBy(itxLsThingLsThing.getRecordedBy());
    	updatedItxLsThingLsThing.setRecordedDate(itxLsThingLsThing.getRecordedDate());
    	updatedItxLsThingLsThing.setIgnored(itxLsThingLsThing.isIgnored());
    	updatedItxLsThingLsThing.setDeleted(itxLsThingLsThing.isDeleted());
    	updatedItxLsThingLsThing.setLsTransaction(itxLsThingLsThing.getLsTransaction());
    	updatedItxLsThingLsThing.setModifiedBy(itxLsThingLsThing.getModifiedBy());
    	updatedItxLsThingLsThing.setCodeName(itxLsThingLsThing.getCodeName());
    	updatedItxLsThingLsThing.setLsType(itxLsThingLsThing.getLsType());
    	updatedItxLsThingLsThing.setLsKind(itxLsThingLsThing.getLsKind());
    	updatedItxLsThingLsThing.setLsTypeAndKind(itxLsThingLsThing.getLsTypeAndKind());
    	updatedItxLsThingLsThing.firstLsThing = LsThing.findLsThing(itxLsThingLsThing.getFirstLsThing().getId());
    	updatedItxLsThingLsThing.secondLsThing = LsThing.findLsThing(itxLsThingLsThing.getSecondLsThing().getId());    	
    	updatedItxLsThingLsThing.setModifiedDate(new Date());
    	updatedItxLsThingLsThing.merge();
    	
    	logger.debug("------------ Just updated the itxLsThingLsthing: ");
    	if(logger.isDebugEnabled()) logger.debug(updatedItxLsThingLsThing.toJson());
    	
        return updatedItxLsThingLsThing;
    }
        
    public static ItxLsThingLsThing fromJsonToItxLsThingLsThing(String json) {
        return new JSONDeserializer<ItxLsThingLsThing>()
        		.use(null, ItxLsThingLsThing.class)
        		.use(BigDecimal.class, new CustomBigDecimalFactory())
        		.deserialize(json);
    }
    
    public static String toJsonArray(Collection<ItxLsThingLsThing> collection) {
        return new JSONSerializer().include("lsStates.lsValues").exclude("*.class", "lsStates.itxLsThingLsThing", "lsStates.lsValues.lsState")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(collection);
    }
    
    public static Collection<ItxLsThingLsThing> fromJsonArrayToItxLsThingLsThings(String json) {
        return new JSONDeserializer<List<ItxLsThingLsThing>>().use(null, ArrayList.class)
        		.use("values", ItxLsThingLsThing.class)
        		.use(BigDecimal.class, new CustomBigDecimalFactory())
        		.deserialize(json);
    }
    
    public static Collection<ItxLsThingLsThing> fromJsonArrayToItxLsThingLsThings(Reader json) {
        return new JSONDeserializer<List<ItxLsThingLsThing>>().use(null, ArrayList.class)
        		.use("values", ItxLsThingLsThing.class)
        		.use(BigDecimal.class, new CustomBigDecimalFactory())
        		.deserialize(json);
    }
    
    public static TypedQuery<ItxLsThingLsThing> findItxLsThingLsThingsByLsTypeEqualsAndLsKindEquals(String lsType, String lsKind){
    	if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        boolean ignored = true;
        EntityManager em = ItxLsThingLsThing.entityManager();
		String query = "SELECT DISTINCT o FROM ItxLsThingLsThing o " +
				"WHERE o.ignored IS NOT :ignored " +
				"AND o.lsType = :lsType " +
				"AND o.lsKind = :lsKind ";
        TypedQuery<ItxLsThingLsThing> q = em.createQuery(query, ItxLsThingLsThing.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        q.setParameter("ignored", ignored);
        
        return q;
    }
    
    public static TypedQuery<ItxLsThingLsThing> findItxLsThingLsThingsByTypeAndKindAndFirstLsThingEqualsAndSecondLsThingEquals(String lsType, String lsKind, LsThing firstLsThing, LsThing secondLsThing){
    	if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        if (firstLsThing == null) throw new IllegalArgumentException("The firstLsThing argument is required");
        if (secondLsThing == null) throw new IllegalArgumentException("The secondLsThing argument is required");
        boolean ignored = true;
        EntityManager em = ItxLsThingLsThing.entityManager();
		String query = "SELECT DISTINCT o FROM ItxLsThingLsThing o " +
				"WHERE o.ignored IS NOT :ignored " +
				"AND o.lsType = :lsType " +
				"AND o.lsKind = :lsKind " +
				"AND o.firstLsThing = :firstLsThing " +
				"AND o.secondLsThing = :secondLsThing ";
        TypedQuery<ItxLsThingLsThing> q = em.createQuery(query, ItxLsThingLsThing.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        q.setParameter("firstLsThing", firstLsThing);
        q.setParameter("secondLsThing", secondLsThing);
        q.setParameter("ignored", ignored);
        
        return q;
    }
    
    public static TypedQuery<ItxLsThingLsThing> findItxLsThingLsThingsNotTypeAndKindAndFirstLsThingEquals(String lsType, String lsKind, LsThing firstLsThing){
    	if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        if (firstLsThing == null) throw new IllegalArgumentException("The firstLsThing argument is required");
        boolean ignored = true;
        EntityManager em = ItxLsThingLsThing.entityManager();
		String query = "SELECT DISTINCT o FROM ItxLsThingLsThing o " +
				"WHERE o.ignored IS NOT :ignored " +
				"AND o.lsType <> :lsType " +
				"AND o.lsKind <> :lsKind " +
				"AND o.firstLsThing = :firstLsThing ";
        TypedQuery<ItxLsThingLsThing> q = em.createQuery(query, ItxLsThingLsThing.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        q.setParameter("firstLsThing", firstLsThing);        
        q.setParameter("ignored", ignored);
        
        return q;
    }

    public static TypedQuery<ItxLsThingLsThing> findItxLsThingLsThingsNotTypeAndKindAndSecondLsThingEquals(String lsType, String lsKind, LsThing secondLsThing){
    	if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        if (secondLsThing == null) throw new IllegalArgumentException("The secondLsThing argument is required");
        boolean ignored = true;
        EntityManager em = ItxLsThingLsThing.entityManager();
		String query = "SELECT DISTINCT o FROM ItxLsThingLsThing o " +
				"WHERE o.ignored IS NOT :ignored " +
				"AND o.lsType <> :lsType " +
				"AND o.lsKind <> :lsKind " +
				"AND o.secondLsThing = :secondLsThing ";
        TypedQuery<ItxLsThingLsThing> q = em.createQuery(query, ItxLsThingLsThing.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        q.setParameter("secondLsThing", secondLsThing);        
        q.setParameter("ignored", ignored);
        
        return q;
    }

    
    public static TypedQuery<ItxLsThingLsThing> findItxLsThingLsThingsByLsTypeEqualsAndLsKindEqualsAndFirstLsThingEquals(String lsType, String lsKind, LsThing firstLsThing){
    	if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        if (firstLsThing == null) throw new IllegalArgumentException("The firstLsThing argument is required");
        boolean ignored = true;
        EntityManager em = ItxLsThingLsThing.entityManager();
		String query = "SELECT DISTINCT o FROM ItxLsThingLsThing o " +
				"WHERE o.ignored IS NOT :ignored " +
				"AND o.lsType = :lsType " +
				"AND o.lsKind = :lsKind " +
				"AND o.firstLsThing = :firstLsThing ";
        TypedQuery<ItxLsThingLsThing> q = em.createQuery(query, ItxLsThingLsThing.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        q.setParameter("firstLsThing", firstLsThing);        
        q.setParameter("ignored", ignored);
        
        return q;
    }
    
    public static TypedQuery<ItxLsThingLsThing> findItxLsThingLsThingsByLsTypeEqualsAndLsKindEqualsAndSecondLsThingEquals(String lsType, String lsKind, LsThing secondLsThing){
    	if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        if (secondLsThing == null) throw new IllegalArgumentException("The secondLsThing argument is required");
        boolean ignored = true;
        EntityManager em = ItxLsThingLsThing.entityManager();
		String query = "SELECT DISTINCT o FROM ItxLsThingLsThing o " +
				"WHERE o.ignored IS NOT :ignored " +
				"AND o.lsType = :lsType " +
				"AND o.lsKind = :lsKind " +
				"AND o.secondLsThing = :secondLsThing ";
        TypedQuery<ItxLsThingLsThing> q = em.createQuery(query, ItxLsThingLsThing.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        q.setParameter("secondLsThing", secondLsThing);        
        q.setParameter("ignored", ignored);
        
        return q;
    }
    
    public static TypedQuery<ItxLsThingLsThing> findItxLsThingLsThingsByLsTypeEqualsAndLsKindEqualsAndSecondLsThingEqualsAndOrderEquals(String lsType, String lsKind, LsThing secondLsThing, int order){
    	if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        if (secondLsThing == null) throw new IllegalArgumentException("The secondLsThing argument is required");
        
        boolean ignored = true;
        
        EntityManager em = ItxLsThingLsThing.entityManager();
		String query = "SELECT DISTINCT o FROM ItxLsThingLsThing o " +
				"JOIN o.lsStates itxstate JOIN itxstate.lsValues itxvalue WITH itxvalue.lsKind = 'order' " +
				"WHERE o.ignored IS NOT :ignored " +
				"AND o.lsType = :lsType " +
				"AND o.lsKind = :lsKind " +
				"AND itxvalue.numericValue = :order " +
				"AND o.secondLsThing = :secondLsThing ";
        
        TypedQuery<ItxLsThingLsThing> q = em.createQuery(query, ItxLsThingLsThing.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        q.setParameter("order", new BigDecimal(order));
        q.setParameter("secondLsThing", secondLsThing);        
        q.setParameter("ignored", ignored);
        
        return q;
    }
    
    public static TypedQuery<ItxLsThingLsThing> findItxLsThingLsThingsByLsTypeEqualsAndLsKindEqualsAndFirstLsThingEqualsAndSecondLsThingEquals(String lsType, String lsKind, LsThing firstLsThing, LsThing secondLsThing){
    	if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        if (firstLsThing == null) throw new IllegalArgumentException("The firstLsThing argument is required");
        if (secondLsThing == null) throw new IllegalArgumentException("The secondLsThing argument is required");
        
        boolean ignored = true;
        
        EntityManager em = ItxLsThingLsThing.entityManager();
		String query = "SELECT DISTINCT o FROM ItxLsThingLsThing o " +
				"WHERE o.ignored IS NOT :ignored " +
				"AND o.lsType = :lsType " +
				"AND o.lsKind = :lsKind " +
				"AND o.firstLsThing = :firstLsThing " +
				"AND o.secondLsThing = :secondLsThing ";
        
        TypedQuery<ItxLsThingLsThing> q = em.createQuery(query, ItxLsThingLsThing.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        q.setParameter("firstLsThing", firstLsThing);        
        q.setParameter("secondLsThing", secondLsThing);        
        q.setParameter("ignored", ignored);
        
        return q;
    }
    
    public int retrieveOrder() {
    	EntityManager em = ItxLsThingLsThing.entityManager();
		String query = "SELECT v.numericValue FROM ItxLsThingLsThingValue v " +
				"JOIN v.lsState s " + 
				"JOIN s.itxLsThingLsThing iltilt " +
				"WHERE s.lsType = 'metadata' " +
				"AND s.lsKind = 'composition' " +
				"AND v.lsType = 'numericValue' " +
				"AND v.lsKind = 'order' " +
				"AND iltilt = :itxLsThingLsThing ";
        
        TypedQuery<Integer> q = em.createQuery(query, Integer.class);
        q.setParameter("itxLsThingLsThing", this); 
        int order;
        try {
        	order = q.getSingleResult();
        } catch (EmptyResultDataAccessException e){
        	order = -1;
        }
    	return order;
    }

    public int grabItxOrder() {
        Set<ItxLsThingLsThingState> lsStates = this.getLsStates();
        for (ItxLsThingLsThingState lsState : lsStates) {
            Set<ItxLsThingLsThingValue> lsValues = lsState.getLsValues();
            for (ItxLsThingLsThingValue lsValue : lsValues) {
                if (lsValue.getLsKind().equals("order")) {
                    return lsValue.getNumericValue().intValue();
                }
            }
        }
        return 0;
    }
    
    public String toJson() {
        return new JSONSerializer()
        	.include("lsStates.lsValues")
        	.exclude("*.class", "lsStates.itxLsThingLsThing")
        	.transform(new ExcludeNulls(), void.class)
        	.serialize(this);
    }

	public String toJsonWithNestedFull() {
		return new JSONSerializer()
				.exclude("*.class", "lsStates.lsValues.lsState", "lsStates.itxLsThingLsThing", "lsLabels.itxLsThingLsThing")
				.include("lsStates.lsValues", 
						"firstLsThing.lsStates.lsValues", 
						"firstLsThing.lsLabels", 
						"secondLsThing.lsStates.lsValues",
						"secondLsThing.lsLabels")
				.transform(new ExcludeNulls(), void.class)
				.serialize(this);
	}

	public String toPrettyJson() {
        return new JSONSerializer()
        		.exclude("*.class", "lsStates.itxLsThingLsThing")
            	.include("lsStates.lsValues")
        		.prettyPrint(true)
        		.transform(new ExcludeNulls(), void.class)
        		.serialize(this);

	}

	public String toJsonWithNestedStubs() {
        return new JSONSerializer().
        		exclude("*.class").
        		include("lsStates.lsValues", 
						"firstLsThing.lsLabels", 
						"secondLsThing.lsLabels").
        		transform(new ExcludeNulls(), void.class).
        		serialize(this);

	}

	public String toJsonStub() {
        return new JSONSerializer()
    	.include()
    	.exclude("*.class")
    	.transform(new ExcludeNulls(), void.class)
    	.serialize(this);
	}

	public static String toJsonArrayWithNestedFull(
			Collection<ItxLsThingLsThing> updatedItxLsThingLsThings) {
        return new JSONSerializer().
        		exclude("*.class").
        		include("lsStates.lsValues", 
						"firstLsThing.lsStates.lsValues", 
						"firstLsThing.lsLabels", 
						"secondLsThing.lsStates.lsValues",
						"secondLsThing.lsLabels").
        		transform(new ExcludeNulls(), void.class).serialize(updatedItxLsThingLsThings);

	}

	public static String toPrettyJsonArray(
			Collection<ItxLsThingLsThing> updatedItxLsThingLsThings) {
        return new JSONSerializer().
        		exclude("*.class").
        		include("lsStates.lsValues", 
						"firstLsThing.lsStates.lsValues", 
						"firstLsThing.lsLabels", 
						"secondLsThing.lsStates.lsValues",
						"secondLsThing.lsLabels").
		        prettyPrint(true).
        		transform(new ExcludeNulls(), void.class).serialize(updatedItxLsThingLsThings);
	}

	public static String toJsonArrayWithNestedStub(
			Collection<ItxLsThingLsThing> updatedItxLsThingLsThings) {
        return new JSONSerializer().
        		exclude("*.class").
        		include("lsStates.lsValues", 
						"firstLsThing.lsLabels", 
						"secondLsThing.lsLabels").
        		transform(new ExcludeNulls(), void.class).serialize(updatedItxLsThingLsThings);
	}

	public static String toJsonArrayStub(
			Collection<ItxLsThingLsThing> updatedItxLsThingLsThings) {
        return new JSONSerializer().
        		exclude("*.class").
        		include("lsStates.lsValues").
        		transform(new ExcludeNulls(), void.class).serialize(updatedItxLsThingLsThings);
	}

	public static TypedQuery<ItxLsThingLsThing> findItxLsThingLsThingsByCodeNameEquals(String codeName) {
        if (codeName == null || codeName.length() == 0) throw new IllegalArgumentException("The codeName argument is required");
        EntityManager em = ItxLsThingLsThing.entityManager();
        TypedQuery<ItxLsThingLsThing> q = em.createQuery("SELECT o FROM ItxLsThingLsThing AS o WHERE o.codeName = :codeName", ItxLsThingLsThing.class);
        q.setParameter("codeName", codeName);
        return q;
    }

	public static TypedQuery<ItxLsThingLsThing> findItxLsThingLsThingsByFirstLsThing(LsThing firstLsThing) {
        if (firstLsThing == null) throw new IllegalArgumentException("The firstLsThing argument is required");
        EntityManager em = ItxLsThingLsThing.entityManager();
        TypedQuery<ItxLsThingLsThing> q = em.createQuery("SELECT o FROM ItxLsThingLsThing AS o WHERE o.firstLsThing = :firstLsThing", ItxLsThingLsThing.class);
        q.setParameter("firstLsThing", firstLsThing);
        return q;
    }

	public static TypedQuery<ItxLsThingLsThing> findItxLsThingLsThingsBySecondLsThing(LsThing secondLsThing) {
        if (secondLsThing == null) throw new IllegalArgumentException("The secondLsThing argument is required");
        EntityManager em = ItxLsThingLsThing.entityManager();
        TypedQuery<ItxLsThingLsThing> q = em.createQuery("SELECT o FROM ItxLsThingLsThing AS o WHERE o.secondLsThing = :secondLsThing", ItxLsThingLsThing.class);
        q.setParameter("secondLsThing", secondLsThing);
        return q;
    }
}
