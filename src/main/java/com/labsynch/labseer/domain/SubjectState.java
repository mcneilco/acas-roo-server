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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RooJavaBean
@RooToString(excludeFields={"subject", "lsValues"})
@RooJson
@RooJpaActiveRecord(finders = { "findSubjectStatesBySubject" })
public class SubjectState extends AbstractState {
	
	private static final Logger logger = LoggerFactory.getLogger(SubjectState.class);

	
    @NotNull
    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lsState", fetch = FetchType.LAZY)
    private Set<SubjectValue> lsValues = new HashSet<SubjectValue>();

	public SubjectState() {
		
	}

    public SubjectState(com.labsynch.labseer.domain.SubjectState subjectState) {
        this.setRecordedBy(subjectState.getRecordedBy());
        this.setRecordedDate(subjectState.getRecordedDate());
        this.setLsTransaction(subjectState.getLsTransaction());
        this.setModifiedBy(subjectState.getModifiedBy());
        this.setModifiedDate(subjectState.getModifiedDate());
        this.setLsType(subjectState.getLsType());
        this.setLsKind(subjectState.getLsKind());
        this.setIgnored(subjectState.isIgnored());
    }
    
	public static SubjectState update(SubjectState subjectState) {
		SubjectState updatedSubjectState = SubjectState.findSubjectState(subjectState.getId());
		updatedSubjectState.setRecordedBy(subjectState.getRecordedBy());
		updatedSubjectState.setRecordedDate(subjectState.getRecordedDate());
		updatedSubjectState.setLsTransaction(subjectState.getLsTransaction());
		updatedSubjectState.setModifiedBy(subjectState.getModifiedBy());
		updatedSubjectState.setModifiedDate(new Date());
		updatedSubjectState.setLsType(subjectState.getLsType());
		updatedSubjectState.setLsKind(subjectState.getLsKind());
		updatedSubjectState.setIgnored(subjectState.isIgnored());		
		return updatedSubjectState;
	}

    public static TypedQuery<com.labsynch.labseer.domain.SubjectState> findSubjectStatesBylsTypeAndKindEquals(String lsTypeAndKind) {
        if (lsTypeAndKind == null || lsTypeAndKind.length() == 0) throw new IllegalArgumentException("The lsTypeAndKind argument is required");
        EntityManager em = SubjectState.entityManager();
        TypedQuery<SubjectState> q = em.createQuery("SELECT o FROM SubjectState AS o WHERE o.lsTypeAndKind = :lsTypeAndKind  AND o.ignored IS NOT :ignored", SubjectState.class);
        q.setParameter("lsTypeAndKind", lsTypeAndKind);
        boolean ignored = true;
        q.setParameter("ignored", ignored);
        return q;
    }

    public static TypedQuery<com.labsynch.labseer.domain.SubjectState> findSubjectStatesBySubject(Subject subject) {
        if (subject == null) throw new IllegalArgumentException("The subject argument is required");
        EntityManager em = SubjectState.entityManager();
        TypedQuery<SubjectState> q = em.createQuery("SELECT o FROM SubjectState AS o WHERE o.subject = :subject", SubjectState.class);
        q.setParameter("subject", subject);
        return q;
    }

    public static TypedQuery<com.labsynch.labseer.domain.SubjectState> findSubjectStatesBySubjectAndLsTypeAndKindEqualsAndIgnoredNot(Subject subject, String lsTypeAndKind) {
        if (subject == null) throw new IllegalArgumentException("The subject argument is required");
        if (lsTypeAndKind == null || lsTypeAndKind.length() == 0) throw new IllegalArgumentException("The lsTypeAndKind argument is required");
        EntityManager em = SubjectState.entityManager();
        TypedQuery<SubjectState> q = em.createQuery("SELECT o FROM SubjectState AS o WHERE o.subject = :subject AND o.lsTypeAndKind = :lsTypeAndKind  AND o.ignored IS NOT :ignored", SubjectState.class);
        q.setParameter("subject", subject);
        q.setParameter("lsTypeAndKind", lsTypeAndKind);
        boolean ignored = true;
        q.setParameter("ignored", ignored);
        return q;
    }

	public static long countSubjectStates() {
        return entityManager().createQuery("SELECT COUNT(o) FROM SubjectState o", Long.class).getSingleResult();
    }

	public static List<SubjectState> findAllSubjectStates() {
        return entityManager().createQuery("SELECT o FROM SubjectState o", SubjectState.class).getResultList();
    }

	public static SubjectState findSubjectState(Long id) {
        if (id == null) return null;
//        EntityManager em = SubjectState.entityManager();
//        TypedQuery<SubjectState> q = em.createQuery("SELECT o FROM SubjectState o WHERE o.id = :id", SubjectState.class);
//        q.setParameter("id", id);
//        return q.getSingleResult();

        return entityManager().find(SubjectState.class, id);
    }

	public static List<SubjectState> findSubjectStateEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM SubjectState o", SubjectState.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public SubjectState merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        SubjectState merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
	
	@Transactional
	public static List<Long> saveList(List<SubjectState> entities) {
		//return a list of ids
		List<Long> idList = new ArrayList<Long>();
		int batchSize = 50;
	    int imported = 0;
	    for (SubjectState e : entities) {
	        e.persist();  // 1. Roo introduces this method via an ITD
	        idList.add(e.getId());
	         if (++imported % batchSize == 0) {
	        	 e.flush();  // 2. ... and this one
	        	 e.clear();  // 3. ... and this one
	        }
	    }
	    return idList;
	 }
		
	@Transactional
	public static String getSubjectStateCollectionJson(List<Long> idList) {
		Collection<SubjectState> subjectStates = new ArrayList<SubjectState>();
		for (Long id : idList){
			SubjectState query = SubjectState.findSubjectState(id);
			if (query != null) subjectStates.add(query);
		}
        return SubjectState.toJsonArray(subjectStates);
    }
	
	public static int deleteByExperimentID(Long experimentId) {
		if (experimentId == null) return 0;
		EntityManager em = SubjectState.entityManager();
		String deleteSQL = "DELETE FROM SubjectState oo WHERE id in (select o.id from SubjectState o where o.subject.treatmentGroup.analysisGroup.experiment.id = :experimentId)";
		logger.debug("attempting to delete subject states: " + deleteSQL);
		Query q = em.createQuery(deleteSQL);
		q.setParameter("experimentId", experimentId);
		int numberOfDeletedEntities = q.executeUpdate();
		return numberOfDeletedEntities;
	}
	
	public static int deleteByTransactionID(Long transactionId) {
		if (transactionId == null) return 0;
		EntityManager em = SubjectState.entityManager();
		String deleteSQL = "DELETE FROM SubjectState oo WHERE lsTransaction = :transactionId";
		logger.debug("attempting to delete subject states: " + deleteSQL);
		Query q = em.createQuery(deleteSQL);
		q.setParameter("transactionId", transactionId);
		int numberOfDeletedEntities = q.executeUpdate();
		return numberOfDeletedEntities;
	}

    public static SubjectState fromJsonToSubjectState(String json) {
        return new JSONDeserializer<SubjectState>()
        		.use(null, SubjectState.class)
        		.use(BigDecimal.class, new CustomBigDecimalFactory())
        		.deserialize(json);
    }
    
    public static Collection<SubjectState> fromJsonArrayToSubjectStates(Reader json) {
        return new JSONDeserializer<List<SubjectState>>().use(null, ArrayList.class)
        		.use("values", SubjectState.class)
        		.use(BigDecimal.class, new CustomBigDecimalFactory())
        		.deserialize(json);
    }
    
    public static Collection<SubjectState> fromJsonArrayToSubjectStates(String json) {
        return new JSONDeserializer<List<SubjectState>>().use(null, ArrayList.class)
        		.use("values", SubjectState.class)
        		.use(BigDecimal.class, new CustomBigDecimalFactory())
        		.deserialize(json);
    }
	
	@Transactional
    public static String toJsonArray(Collection<SubjectState> collection) {
        return new JSONSerializer()
        	.exclude("*.class")
        	.transform(new ExcludeNulls(), void.class)
        	.serialize(collection);
    }
    
	@Transactional
    public static String toJsonArrayStub(Collection<SubjectState> collection) {
        return new JSONSerializer()
        	.exclude("*.class", "subject")
        	.include("subject.id")
        	.transform(new ExcludeNulls(), void.class)
        	.serialize(collection);
    }

	@Transactional
    public String toJson() {
        return new JSONSerializer()
        .exclude("*.class")
        .transform(new ExcludeNulls(), void.class)
        .serialize(this);
    }

	@Transactional
    public String toJsonStub() {
        return new JSONSerializer()
        .exclude("*.class", "subject")
        .transform(new ExcludeNulls(), void.class)
        .serialize(this);
    }

}
