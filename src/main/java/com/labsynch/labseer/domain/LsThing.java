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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.TypedQuery;

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
@RooToString(excludeFields = { "lsTags", "lsStates", "lsLabels" })
@RooJson
@RooJpaActiveRecord(finders = { "findLsThingsByCodeNameEquals", "findLsThingsByCodeNameLike", "findLsThingsByLsKindLike", "findLsThingsByLsTransactionEquals", "findLsThingsByLsTypeAndKindEquals", "findLsThingsByRecordedByLike", "findLsThingsByLsTypeEquals", "findLsThingsByLsKindEquals", "findLsThingsByRecordedDateGreaterThan", "findLsThingsByRecordedDateLessThan" })
public class LsThing extends AbstractThing {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lsThing")
    private Set<LsThingState> lsStates = new HashSet<LsThingState>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lsThing")
    private Set<LsThingLabel> lsLabels = new HashSet<LsThingLabel>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "LSTHING_TAG", 
    joinColumns = { @JoinColumn(name="lsthing_id") }, inverseJoinColumns = { @JoinColumn(name="tag_id") })
    private Set<LsTag> lsTags = new HashSet<LsTag>();
    
    @OneToMany(cascade={}, mappedBy = "secondLsThing", fetch = FetchType.LAZY)
    private Set<ItxLsThingLsThing> firstLsThings = new HashSet<ItxLsThingLsThing>();

    @OneToMany(cascade={}, mappedBy = "firstLsThing", fetch = FetchType.LAZY)
    private Set<ItxLsThingLsThing> secondLsThings = new HashSet<ItxLsThingLsThing>();

    public LsThing(com.labsynch.labseer.domain.LsThing lsThing) {
        this.setRecordedBy(lsThing.getRecordedBy());
        this.setRecordedDate(lsThing.getRecordedDate());
        this.setLsTransaction(lsThing.getLsTransaction());
        this.setModifiedBy(lsThing.getModifiedBy());
        this.setModifiedDate(lsThing.getModifiedDate());
        this.setIgnored(lsThing.isIgnored());
        this.setCodeName(lsThing.getCodeName());
        this.setLsType(lsThing.getLsType());
        this.setLsKind(lsThing.getLsKind());
        this.setLsTypeAndKind(lsThing.getLsTypeAndKind());
        if (lsThing.getLsTags() != null) {
            for (LsTag lsTag : lsThing.getLsTags()) {
                List<LsTag> queryTags = LsTag.findLsTagsByTagTextEquals(lsTag.getTagText()).getResultList();
                if (queryTags.size() < 1) {
                    LsTag newLsTag = new LsTag(lsTag);
                    newLsTag.persist();
                    this.getLsTags().add(newLsTag);
                } else {
                    this.getLsTags().add(queryTags.get(0));
                }
            }
        }
    }

    public LsThing() {
    	
    }
    
	
    @Transactional
    public String toJson() {
        return new JSONSerializer().exclude("*.class").include("lsTags", "lsLabels", "lsStates.lsValues").transform(new ExcludeNulls(), void.class).serialize(this);
    }

    public static com.labsynch.labseer.domain.LsThing fromJsonToProtocol(String json) {
        return new JSONDeserializer<LsThing>().use(null, LsThing.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
    }

    @Transactional
    public static String toJsonArray(Collection<com.labsynch.labseer.domain.LsThing> collection) {
        return new JSONSerializer().exclude("*.class").include("lsTags", "lsLabels", "lsStates.lsValues").transform(new ExcludeNulls(), void.class).serialize(collection);
    }

    public static Collection<com.labsynch.labseer.domain.LsThing> fromJsonArrayToProtocols(String json) {
        return new JSONDeserializer<List<LsThing>>().use(null, ArrayList.class).use("values", LsThing.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
    }

    public static Collection<com.labsynch.labseer.domain.LsThing> fromJsonArrayToProtocols(Reader json) {
        return new JSONDeserializer<List<LsThing>>().use(null, ArrayList.class).use("values", LsThing.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
    }
    
    @Transactional
    public String toJsonWithNestedStubs() {
        return new JSONSerializer().exclude("*.class").include("lsTags", "lsLabels", "lsStates.lsValues", "firstLsThings.firstLsThing.lsLabels","secondLsThings.secondLsThing.lsLabels").transform(new ExcludeNulls(), void.class).serialize(this);
    }
    
    @Transactional
    public String toJsonWithNestedFull() {
        return new JSONSerializer().exclude("*.class").include("lsTags", "lsLabels", "lsStates.lsValues", "firstLsThings.firstLsThing.lsStates.lsValues","firstLsThings.firstLsThing.lsLabels", "secondLsThings.secondLsThing.lsStates.lsValues","secondLsThings.secondLsThing.lsLabels","firstLsThings.lsStates.lsValues","firstLsThings.lsLabels","secondLsThings.lsStates.lsValues","secondLsThings.lsLabels").transform(new ExcludeNulls(), void.class).serialize(this);
    }

    @Transactional
    public String toPrettyJson() {
        return new JSONSerializer().exclude("*.class", "lsStates.lsValues.lsState", "lsStates.lsThing", "lsLabels.lsThing").include("lsTags", "lsLabels", "lsStates.lsValues").prettyPrint(true).transform(new ExcludeNulls(), void.class).serialize(this);
    }

    @Transactional
    public String toJsonStub() {
        return new JSONSerializer().exclude("*.class").include("lsTags", "lsLabels").prettyPrint(false).transform(new ExcludeNulls(), void.class).serialize(this);
    }
    
    @Transactional
    public static String toJsonArrayWithNestedStubs(Collection<com.labsynch.labseer.domain.LsThing> collection) {
        return new JSONSerializer().exclude("*.class").include("lsTags", "lsLabels", "lsStates.lsValues", "firstLsThings.firstLsThing.lsLabels","secondLsThings.secondLsThing.lsLabels").transform(new ExcludeNulls(), void.class).serialize(collection);
    }
    
    @Transactional
    public static String toJsonArrayWithNestedFull(Collection<com.labsynch.labseer.domain.LsThing> collection) {
        return new JSONSerializer().exclude("*.class").include("lsTags", "lsLabels", "lsStates.lsValues", "firstLsThings.firstLsThing.lsStates.lsValues","secondLsThings.secondLsThing.lsStates.lsValues","firstLsThings.firstLsThing.lsLabels","secondLsThings.secondLsThing.lsLabels","firstLsThings.lsStates.lsValues","secondLsThings.lsStates.lsValues","firstLsThings.lsLabels","secondLsThings.lsLabels").transform(new ExcludeNulls(), void.class).serialize(collection);
    }
    
    @Transactional
    public static String toJsonArrayPretty(Collection<com.labsynch.labseer.domain.LsThing> collection) {
        return new JSONSerializer().exclude("*.class", "lsStates.lsValues.lsState", "lsStates.lsThing", "lsLabels.lsThing").include("lsTags", "lsLabels", "lsStates.lsValues").prettyPrint(true).transform(new ExcludeNulls(), void.class).serialize(collection);
    }

    @Transactional
    public static String toJsonArrayStub(Collection<com.labsynch.labseer.domain.LsThing> collection) {
        return new JSONSerializer().exclude("*.class").include("lsTags", "lsLabels").prettyPrint(false).transform(new ExcludeNulls(), void.class).serialize(collection);
    }

    @Transactional
    public String toPrettyJsonStub() {
        return new JSONSerializer()
        .exclude("*.class", "lsStates.lsThing")
        .include("lsTags", "lsLabels", "lsStates.lsValues")
        .prettyPrint(true)
        .transform(new ExcludeNulls(), void.class).serialize(this);
    }

	public static TypedQuery<LsThing> findLsThing(String thingType, String thingKind) {
        if (thingType == null || thingType.length() == 0) throw new IllegalArgumentException("The thingType argument is required");
        if (thingKind == null || thingKind.length() == 0) throw new IllegalArgumentException("The thingKind argument is required");
        
        boolean ignored = true;
        
        EntityManager em = LsThing.entityManager();
		String query = "SELECT DISTINCT o FROM LsThing o " +
				"WHERE o.ignored IS NOT :ignored " +
				"AND o.lsType = :thingType " +
				"AND o.lsKind = :thingKind ";
        
        TypedQuery<LsThing> q = em.createQuery(query, LsThing.class);
        q.setParameter("thingType", thingType);
        q.setParameter("thingKind", thingKind);
        q.setParameter("ignored", ignored);
        
        return q;
	}

//	public static TypedQuery<LsThing> findLsThing(String thingType, String thingKind, String labelText) {
//        if (thingType == null || thingType.length() == 0) throw new IllegalArgumentException("The thingType argument is required");
//        if (thingKind == null || thingKind.length() == 0) throw new IllegalArgumentException("The thingKind argument is required");
//        if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");
//        
//        boolean ignored = true;
//        
//        EntityManager em = LsThing.entityManager();
//		String query = "SELECT o FROM LsThing o " +
//				"JOIN o.lsLabels ll with ll.ignored IS NOT :ignored AND ll.labelText = :labelText " +
//				"WHERE o.ignored IS NOT :ignored " +
//				"AND o.lsType = :thingType " +
//				"AND o.lsKind = :thingKind ";
//        
//        TypedQuery<LsThing> q = em.createQuery(query, LsThing.class);
//        q.setParameter("thingType", thingType);
//        q.setParameter("thingKind", thingKind);
//        q.setParameter("labelText", labelText);
//        q.setParameter("ignored", ignored);
//        
//        return q;
//	}

    
    public static TypedQuery<LsThing> findLsThingByLabelText(String labelText) {
        if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");
        
        boolean ignored = true;
        
        EntityManager em = LsThing.entityManager();
		String query = "SELECT DISTINCT o FROM LsThing o " +
				"JOIN o.lsLabels ll with ll.ignored IS NOT :ignored AND ll.labelText = :labelText " +
				"WHERE o.ignored IS NOT :ignored ";
        
        TypedQuery<LsThing> q = em.createQuery(query, LsThing.class);
        q.setParameter("labelText", labelText);
        q.setParameter("ignored", ignored);
        
        return q;
    }
    
    public static TypedQuery<LsThing> findLsThingByLabelTextAndLsKind(String labelText, String lsKind) {
        if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");
        
        boolean ignored = true;
        
        EntityManager em = LsThing.entityManager();
		String query = "SELECT DISTINCT o FROM LsThing o " +
				"JOIN o.lsLabels ll with ll.ignored IS NOT :ignored AND ll.labelText = :labelText " +
				"WHERE o.ignored IS NOT :ignored " +
				"AND o.lsKind = :lsKind ";
        
        TypedQuery<LsThing> q = em.createQuery(query, LsThing.class);
        q.setParameter("labelText", labelText);
        q.setParameter("lsKind", lsKind);
        q.setParameter("ignored", ignored);
        
        return q;
    }
       
    public static TypedQuery<LsThing> findLsThingByLabelTextList(List<String> labelTextList) {
        if (labelTextList == null || labelTextList.size() == 0) throw new IllegalArgumentException("The labelText argument is required");
        
        boolean ignored = true;
        
        EntityManager em = LsThing.entityManager();
		String query = "SELECT DISTINCT o FROM LsThing o " +
				"JOIN o.lsLabels ll with ll.ignored IS NOT :ignored AND ll.labelText in (:labelTextList) " +
				"WHERE o.ignored IS NOT :ignored ";
        
        TypedQuery<LsThing> q = em.createQuery(query, LsThing.class);
        q.setParameter("labelTextList", labelTextList);
        q.setParameter("ignored", ignored);
        
        return q;
    }

	public static TypedQuery<LsThing> findLsThingByLabelText(String thingType, String thingKind, String labelText) {
        if (thingType == null || thingType.length() == 0) throw new IllegalArgumentException("The thingType argument is required");
        if (thingKind == null || thingKind.length() == 0) throw new IllegalArgumentException("The thingKind argument is required");
		if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");
        
        boolean ignored = true;
        EntityManager em = LsThing.entityManager();
		String query = "SELECT DISTINCT o FROM LsThing o " +
				"JOIN o.lsLabels ll with ll.ignored IS NOT :ignored AND ll.labelText = :labelText " +
				"WHERE o.ignored IS NOT :ignored " +
				"AND o.lsType = :thingType " +
				"AND o.lsKind = :thingKind ";
        
        TypedQuery<LsThing> q = em.createQuery(query, LsThing.class);
        q.setParameter("thingType", thingType);
        q.setParameter("thingKind", thingKind);
        q.setParameter("labelText", labelText);
        q.setParameter("ignored", ignored);
        
        return q;
	}
	
	public static TypedQuery<LsThing> findLsThingByLabelText(String thingType, String thingKind, String labelType, String labelKind, String labelText) {
        if (thingType == null || thingType.length() == 0) throw new IllegalArgumentException("The thingType argument is required");
        if (thingKind == null || thingKind.length() == 0) throw new IllegalArgumentException("The thingKind argument is required");
        if (labelType == null || labelType.length() == 0) throw new IllegalArgumentException("The labelType argument is required");
        if (labelKind == null || labelKind.length() == 0) throw new IllegalArgumentException("The labelKind argument is required");
        if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");
        
        boolean ignored = true;
        
        EntityManager em = LsThing.entityManager();
		String query = "SELECT DISTINCT o FROM LsThing o " +
				"JOIN o.lsLabels ll " +
				"WHERE o.ignored IS NOT :ignored " +
				"AND o.lsType = :thingType " +
				"AND o.lsKind = :thingKind " +
				"AND ll.ignored IS NOT :ignored AND ll.lsType = :labelType AND ll.lsKind = :labelKind AND ll.labelText = :labelText";
        
        TypedQuery<LsThing> q = em.createQuery(query, LsThing.class);
        q.setParameter("thingType", thingType);
        q.setParameter("thingKind", thingKind);
        q.setParameter("labelType", labelType);        
        q.setParameter("labelKind", labelKind);
        q.setParameter("labelText", labelText);
        q.setParameter("ignored", ignored);
        
        return q;
	}

//	public static TypedQuery<LsThing> findLsThingByLabelText(String thingType,
//			String thingKind, String labelType, String labelKind,
//			Object batchCode, int firstResult, int resultSetSize) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	public static TypedQuery<LsThing> findLsThingByLabelTypeAndLabelText(
			String thingType, String thingKind, String labelType,
			String labelText) {
		if (thingType == null || thingType.length() == 0) throw new IllegalArgumentException("The thingType argument is required");
        if (thingKind == null || thingKind.length() == 0) throw new IllegalArgumentException("The thingKind argument is required");
        if (labelType == null || labelType.length() == 0) throw new IllegalArgumentException("The labelType argument is required");
        if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");
        
        boolean ignored = true;
        
        EntityManager em = LsThing.entityManager();
		String query = "SELECT DISTINCT o FROM LsThing o " +
				"JOIN o.lsLabels ll with ll.ignored IS NOT :ignored AND ll.lsType = :labelType AND ll.labelText = :labelText " +
				"WHERE o.ignored IS NOT :ignored " +
				"AND o.lsType = :thingType " +
				"AND o.lsKind = :thingKind ";
        
        TypedQuery<LsThing> q = em.createQuery(query, LsThing.class);
        q.setParameter("thingType", thingType);
        q.setParameter("thingKind", thingKind);
        q.setParameter("labelType", labelType);        
        q.setParameter("labelText", labelText);
        q.setParameter("ignored", ignored);
        
        return q;
	}

	public static TypedQuery<LsThing> findLsThingByLabelKindAndLabelText(
			String thingType, String thingKind, String labelKind,
			String labelText) {
		if (thingType == null || thingType.length() == 0) throw new IllegalArgumentException("The thingType argument is required");
        if (thingKind == null || thingKind.length() == 0) throw new IllegalArgumentException("The thingKind argument is required");
        if (labelKind == null || labelKind.length() == 0) throw new IllegalArgumentException("The labelKind argument is required");
        if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");
        
        boolean ignored = true;
        
        EntityManager em = LsThing.entityManager();
		String query = "SELECT DISTINCT o FROM LsThing o " +
				"JOIN o.lsLabels ll with ll.ignored IS NOT :ignored AND ll.lsKind = :labelKind AND ll.labelText = :labelText " +
				"WHERE o.ignored IS NOT :ignored " +
				"AND o.lsType = :thingType " +
				"AND o.lsKind = :thingKind ";
        
        TypedQuery<LsThing> q = em.createQuery(query, LsThing.class);
        q.setParameter("thingType", thingType);
        q.setParameter("thingKind", thingKind);
        q.setParameter("labelKind", labelKind);
        q.setParameter("labelText", labelText);
        q.setParameter("ignored", ignored);
        
        return q;
	}

	public static TypedQuery<LsThing> findLsThingByLabelTypeAndKind(
			String thingType, String thingKind, String labelType,
			String labelKind) {
		if (thingType == null || thingType.length() == 0) throw new IllegalArgumentException("The thingType argument is required");
        if (thingKind == null || thingKind.length() == 0) throw new IllegalArgumentException("The thingKind argument is required");
        if (labelType == null || labelType.length() == 0) throw new IllegalArgumentException("The labelType argument is required");
        if (labelKind == null || labelKind.length() == 0) throw new IllegalArgumentException("The labelKind argument is required");
        
        boolean ignored = true;
        
        EntityManager em = LsThing.entityManager();
		String query = "SELECT DISTINCT o FROM LsThing o " +
				"JOIN o.lsLabels ll with ll.ignored IS NOT :ignored AND ll.lsType = :labelType AND ll.lsKind = :labelKind " +
				"WHERE o.ignored IS NOT :ignored " +
				"AND o.lsType = :thingType " +
				"AND o.lsKind = :thingKind ";
        
        TypedQuery<LsThing> q = em.createQuery(query, LsThing.class);
        q.setParameter("thingType", thingType);
        q.setParameter("thingKind", thingKind);
        q.setParameter("labelType", labelType);        
        q.setParameter("labelKind", labelKind);
        q.setParameter("ignored", ignored);
        
        return q;
	}

	public static TypedQuery<LsThing> findLsThingByLabelKind(String thingType,
			String thingKind, String labelKind) {
		if (thingType == null || thingType.length() == 0) throw new IllegalArgumentException("The thingType argument is required");
        if (thingKind == null || thingKind.length() == 0) throw new IllegalArgumentException("The thingKind argument is required");
        if (labelKind == null || labelKind.length() == 0) throw new IllegalArgumentException("The labelKind argument is required");
        
        boolean ignored = true;
        
        EntityManager em = LsThing.entityManager();
		String query = "SELECT DISTINCT o FROM LsThing o " +
				"JOIN o.lsLabels ll with ll.ignored IS NOT :ignored AND ll.lsKind = :labelKind " +
				"WHERE o.ignored IS NOT :ignored " +
				"AND o.lsType = :thingType " +
				"AND o.lsKind = :thingKind ";
        
        TypedQuery<LsThing> q = em.createQuery(query, LsThing.class);
        q.setParameter("thingType", thingType);
        q.setParameter("thingKind", thingKind);
        q.setParameter("labelKind", labelKind);
        q.setParameter("ignored", ignored);
        
        return q;
	}

	public static TypedQuery<LsThing> findLsThingByLabelType(String thingType,
			String thingKind, String labelType) {
		if (thingType == null || thingType.length() == 0) throw new IllegalArgumentException("The thingType argument is required");
        if (thingKind == null || thingKind.length() == 0) throw new IllegalArgumentException("The thingKind argument is required");
        if (labelType == null || labelType.length() == 0) throw new IllegalArgumentException("The labelType argument is required");
        
        boolean ignored = true;
        
        EntityManager em = LsThing.entityManager();
		String query = "SELECT DISTINCT o FROM LsThing o " +
				"JOIN o.lsLabels ll with ll.ignored IS NOT :ignored AND ll.lsType = :labelType " +
				"WHERE o.ignored IS NOT :ignored " +
				"AND o.lsType = :thingType " +
				"AND o.lsKind = :thingKind ";
        
        TypedQuery<LsThing> q = em.createQuery(query, LsThing.class);
        q.setParameter("thingType", thingType);
        q.setParameter("thingKind", thingKind);
        q.setParameter("labelType", labelType);        
        q.setParameter("ignored", ignored);
        
        return q;
	}
	
	public static TypedQuery<LsThing> findFirstLsThingsByItxTypeKindEqualsAndSecondLsThingEquals(String lsType,
			String lsKind, LsThing secondLsThing) {
		if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        if (secondLsThing == null) throw new IllegalArgumentException("The secondLsThing argument is required");
        
        boolean ignored = true;
        
        EntityManager em = LsThing.entityManager();
		String query = "SELECT DISTINCT itx.firstLsThing FROM LsThing secondLsThing " +
				"JOIN secondLsThing.firstLsThings itx with itx.ignored IS NOT :ignored AND itx.lsType = :lsType AND itx.lsKind = :lsKind " +
				"WHERE secondLsThing.ignored IS NOT :ignored " +
				"AND secondLsThing = :secondLsThing ";
		
        TypedQuery<LsThing> q = em.createQuery(query, LsThing.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        q.setParameter("secondLsThing", secondLsThing);        
        q.setParameter("ignored", ignored);
        
        return q;
	}
	
	public static TypedQuery<LsThing> findSecondLsThingsByItxTypeKindEqualsAndFirstLsThingEquals(String lsType,
			String lsKind, LsThing firstLsThing) {
		if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        if (firstLsThing == null) throw new IllegalArgumentException("The firstLsThing argument is required");
        
        boolean ignored = true;
        
        EntityManager em = LsThing.entityManager();
		String query = "SELECT DISTINCT itx.secondLsThing FROM LsThing firstLsThing " +
				"JOIN firstLsThing.secondLsThings itx with itx.ignored IS NOT :ignored AND itx.lsType = :lsType AND itx.lsKind = :lsKind " +
				"WHERE firstLsThing.ignored IS NOT :ignored " +
				"AND firstLsThing = :firstLsThing ";
		
        TypedQuery<LsThing> q = em.createQuery(query, LsThing.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        q.setParameter("firstLsThing", firstLsThing);        
        q.setParameter("ignored", ignored);
        
        return q;
	}
	
	public static TypedQuery<LsThing> findFirstLsThingsByItxTypeKindEqualsAndSecondLsThingEqualsAndOrderEquals(String lsType,
			String lsKind, LsThing secondLsThing, int order) {
		if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        if (secondLsThing == null) throw new IllegalArgumentException("The secondLsThing argument is required");
        
        boolean ignored = true;
        
        EntityManager em = LsThing.entityManager();
		String query = "SELECT DISTINCT itx.firstLsThing FROM LsThing secondLsThing " +
				"JOIN secondLsThing.firstLsThings itx with itx.ignored IS NOT :ignored AND itx.lsType = :lsType AND itx.lsKind = :lsKind " +
				"JOIN itx.lsStates itxstate JOIN itxstate.lsValues itxvalue WITH itxvalue.lsKind = 'order' " +
				"WHERE secondLsThing.ignored IS NOT :ignored " +
				"AND itxvalue.numericValue = :order " +
				"AND secondLsThing = :secondLsThing ";
		
        TypedQuery<LsThing> q = em.createQuery(query, LsThing.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        q.setParameter("order", order);
        q.setParameter("secondLsThing", secondLsThing);        
        q.setParameter("ignored", ignored);
        
        return q;
	}
	
	public static TypedQuery<LsThing> findSecondLsThingsByItxTypeKindEqualsAndFirstLsThingEqualsAndOrderEquals(String lsType,
			String lsKind, LsThing firstLsThing, int order) {
		if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        if (firstLsThing == null) throw new IllegalArgumentException("The firstLsThing argument is required");
        
        boolean ignored = true;
        
        EntityManager em = LsThing.entityManager();
		String query = "SELECT DISTINCT itx.secondLsThing FROM LsThing firstLsThing " +
				"JOIN firstLsThing.secondLsThings itx with itx.ignored IS NOT :ignored AND itx.lsType = :lsType AND itx.lsKind = :lsKind " +
				"JOIN itx.lsStates itxstate JOIN itxstate.lsValues itxvalue WITH itxvalue.lsKind = 'order' " +
				"WHERE firstLsThing.ignored IS NOT :ignored " +
				"AND itxvalue.numericValue = :order " +
				"AND firstLsThing = :firstLsThing ";
		
        TypedQuery<LsThing> q = em.createQuery(query, LsThing.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        q.setParameter("order", order);
        q.setParameter("firstLsThing", firstLsThing);        
        q.setParameter("ignored", ignored);
        
        return q;
	}
	
	public static com.labsynch.labseer.domain.LsThing update(com.labsynch.labseer.domain.LsThing lsThing) {
        LsThing updatedLsThing = LsThing.findLsThing(lsThing.getId());
        updatedLsThing.setRecordedBy(lsThing.getRecordedBy());
        updatedLsThing.setRecordedDate(lsThing.getRecordedDate());
        updatedLsThing.setLsTransaction(lsThing.getLsTransaction());
        updatedLsThing.setModifiedBy(lsThing.getModifiedBy());
        updatedLsThing.setModifiedDate(new Date());
        updatedLsThing.setCodeName(lsThing.getCodeName());
        updatedLsThing.setLsType(lsThing.getLsType());
        updatedLsThing.setLsKind(lsThing.getLsKind());
        updatedLsThing.setLsTypeAndKind(lsThing.getLsTypeAndKind());
        updatedLsThing.setIgnored(lsThing.isIgnored());
        if (updatedLsThing.getLsTags() != null) {
            updatedLsThing.getLsTags().clear();
        }
        if (lsThing.getLsTags() != null) {
            for (LsTag lsTag : lsThing.getLsTags()) {
                List<LsTag> queryTags = LsTag.findLsTagsByTagTextEquals(lsTag.getTagText()).getResultList();
                if (queryTags.size() < 1) {
                    LsTag newLsTag = new LsTag(lsTag);
                    newLsTag.persist();
                    updatedLsThing.getLsTags().add(newLsTag);
                } else {
                    updatedLsThing.getLsTags().add(queryTags.get(0));
                }
            }
        }
        updatedLsThing.merge();
        return updatedLsThing;
    }
	
	public static com.labsynch.labseer.domain.LsThing updateNoMerge(com.labsynch.labseer.domain.LsThing lsThing) {
        LsThing updatedLsThing = LsThing.findLsThing(lsThing.getId());
        updatedLsThing.setRecordedBy(lsThing.getRecordedBy());
        updatedLsThing.setRecordedDate(lsThing.getRecordedDate());
        updatedLsThing.setLsTransaction(lsThing.getLsTransaction());
        updatedLsThing.setModifiedBy(lsThing.getModifiedBy());
        updatedLsThing.setModifiedDate(new Date());
        updatedLsThing.setCodeName(lsThing.getCodeName());
        updatedLsThing.setLsType(lsThing.getLsType());
        updatedLsThing.setLsKind(lsThing.getLsKind());
        updatedLsThing.setLsTypeAndKind(lsThing.getLsTypeAndKind());
        updatedLsThing.setIgnored(lsThing.isIgnored());
        if (updatedLsThing.getLsTags() != null) {
            updatedLsThing.getLsTags().clear();
        }
        if (lsThing.getLsTags() != null) {
            for (LsTag lsTag : lsThing.getLsTags()) {
                List<LsTag> queryTags = LsTag.findLsTagsByTagTextEquals(lsTag.getTagText()).getResultList();
                if (queryTags.size() < 1) {
                    LsTag newLsTag = new LsTag(lsTag);
                    newLsTag.persist();
                    updatedLsThing.getLsTags().add(newLsTag);
                } else {
                    updatedLsThing.getLsTags().add(queryTags.get(0));
                }
            }
        }
        return updatedLsThing;
    }

	public static TypedQuery<LsThing> findFirstLsThingsByItxTypeEqualsAndSecondLsThingEquals(
			String lsType, LsThing secondLsThing) {
		if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (secondLsThing == null) throw new IllegalArgumentException("The secondLsThing argument is required");
        
        boolean ignored = true;
        
        EntityManager em = LsThing.entityManager();
		String query = "SELECT DISTINCT itx.firstLsThing FROM LsThing secondLsThing " +
				"JOIN secondLsThing.firstLsThings itx with itx.ignored IS NOT :ignored AND itx.lsType = :lsType " +
				"WHERE secondLsThing.ignored IS NOT :ignored " +
				"AND secondLsThing = :secondLsThing ";
		
        TypedQuery<LsThing> q = em.createQuery(query, LsThing.class);
        q.setParameter("lsType", lsType);
        q.setParameter("secondLsThing", secondLsThing);        
        q.setParameter("ignored", ignored);
        
        return q;
	}

	public LsThingLabel pickBestCorpName() {
		Collection<LsThingLabel> labels = this.getLsLabels();
		if (labels.isEmpty()) return null;
		Collection<LsThingLabel> corpNames = new HashSet<LsThingLabel>();
		for (LsThingLabel label : labels){
			if (label.getLsType().equals("corpName")) corpNames.add(label);
		}
		Collection<LsThingLabel> preferredCorpNameLabels = new HashSet<LsThingLabel>();
		for (LsThingLabel label : corpNames){
			if (label.isPreferred()) preferredCorpNameLabels.add(label);
		}
		if (!preferredCorpNameLabels.isEmpty()){
			LsThingLabel bestLabel = preferredCorpNameLabels.iterator().next();
			for (LsThingLabel preferredLabel : preferredCorpNameLabels){
				if (preferredLabel.getRecordedDate().compareTo(bestLabel.getRecordedDate()) > 0) bestLabel = preferredLabel;
			}
			return bestLabel;
		}else if (!corpNames.isEmpty()){
			LsThingLabel bestLabel = corpNames.iterator().next();
			for (LsThingLabel preferredLabel : corpNames){
				if (preferredLabel.getRecordedDate().compareTo(bestLabel.getRecordedDate()) > 0) bestLabel = preferredLabel;
			}
			return bestLabel;
		}else{
			return null;
		}
	}
	
	public LsThingLabel pickBestName() {
		Collection<LsThingLabel> labels = this.getLsLabels();
		if (labels.isEmpty()) return null;
		Collection<LsThingLabel> names = new HashSet<LsThingLabel>();
		for (LsThingLabel label : labels){
			if (label.getLsType().equals("name")) names.add(label);
		}
		Collection<LsThingLabel> preferredNameLabels = new HashSet<LsThingLabel>();
		for (LsThingLabel label : names){
			if (label.isPreferred()) preferredNameLabels.add(label);
		}
		if (!preferredNameLabels.isEmpty()){
			LsThingLabel bestLabel = preferredNameLabels.iterator().next();
			for (LsThingLabel preferredLabel : preferredNameLabels){
				if (preferredLabel.getRecordedDate().compareTo(bestLabel.getRecordedDate()) > 0) bestLabel = preferredLabel;
			}
			return bestLabel;
		}else if (!names.isEmpty()){
			LsThingLabel bestLabel = names.iterator().next();
			for (LsThingLabel preferredLabel : names){
				if (preferredLabel.getRecordedDate().compareTo(bestLabel.getRecordedDate()) > 0) bestLabel = preferredLabel;
			}
			return bestLabel;
		}else{
			return null;
		}
	}


}
