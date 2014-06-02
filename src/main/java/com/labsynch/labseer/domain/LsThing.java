package com.labsynch.labseer.domain;

import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
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
@RooJpaActiveRecord(finders = { "findLsThingsByCodeNameEquals", "findLsThingsByLsTransactionEquals", "findLsThingsByLsTypeAndKindEquals" })
public class LsThing extends AbstractThing {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lsThing")
    private Set<LsThingState> lsStates = new HashSet<LsThingState>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lsThing")
    private Set<LsThingLabel> lsLabels = new HashSet<LsThingLabel>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "LSTHING_TAG", 
    joinColumns = { @JoinColumn(name="lsthing_id") }, inverseJoinColumns = { @JoinColumn(name="tag_id") })
    private Set<LsTag> lsTags = new HashSet<LsTag>();
    

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
				"JOIN o.lsLabels ll with ll.ignored IS NOT :ignored AND ll.lsType = :labelType AND ll.lsKind = :labelKind AND ll.labelText = :labelText " +
				"WHERE o.ignored IS NOT :ignored " +
				"AND o.lsType = :thingType " +
				"AND o.lsKind = :thingKind ";
        
        TypedQuery<LsThing> q = em.createQuery(query, LsThing.class);
        q.setParameter("thingType", thingType);
        q.setParameter("thingKind", thingKind);
        q.setParameter("labelType", labelType);        
        q.setParameter("labelKind", labelKind);
        q.setParameter("labelText", labelText);
        q.setParameter("ignored", ignored);
        
        return q;
	}

	public static TypedQuery<LsThing> findLsThingByLabelText(String thingType,
			String thingKind, String labelType, String labelKind,
			Object batchCode, int firstResult, int resultSetSize) {
		// TODO Auto-generated method stub
		return null;
	}


}
