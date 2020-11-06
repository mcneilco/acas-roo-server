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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.dto.FlatThingCsvDTO;
import com.labsynch.labseer.utils.CustomBigDecimalFactory;
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Entity
@Configurable
public class Subject extends AbstractThing {
	
	private static final Logger logger = LoggerFactory.getLogger(Subject.class);
	
	//This direction: Subject is grandparent
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch =  FetchType.LAZY)
	@JoinTable(name="TREATMENTGROUP_SUBJECT", 
	joinColumns={@JoinColumn(name="subject_id")}, 
	inverseJoinColumns={@JoinColumn(name="treatment_group_id")})
    private Set<TreatmentGroup> treatmentGroups = new HashSet<TreatmentGroup>();
	
	//This direction: Experiment is grandparent
//	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "subjects")  
//    private Set<TreatmentGroup> treatmentGroups = new HashSet<TreatmentGroup>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "subject", fetch =  FetchType.LAZY)
    private Set<SubjectLabel> lsLabels = new HashSet<SubjectLabel>();
    
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "subject", fetch =  FetchType.LAZY)
	private Set<SubjectState> lsStates = new HashSet<SubjectState>();

	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "subject", fetch =  FetchType.LAZY, orphanRemoval = true)
	private Set<ItxSubjectContainer> containers = new HashSet<ItxSubjectContainer>();

	//constructor to instantiate a new sample from nested json objects
	public Subject(Subject subject) {

		super.setRecordedBy(subject.getRecordedBy());
		super.setRecordedDate(subject.getRecordedDate());
		super.setLsTransaction(subject.getLsTransaction());
		super.setModifiedBy(subject.getModifiedBy());
		super.setModifiedDate(subject.getModifiedDate());
        super.setIgnored(subject.isIgnored());
        super.setCodeName(subject.getCodeName());
        super.setLsType(subject.getLsType());
        super.setLsKind(subject.getLsKind());
        super.setLsTypeAndKind(subject.getLsTypeAndKind());
        
        this.setLsLabels(subject.getLsLabels());
        this.setLsStates(subject.getLsStates());
        this.setContainers(subject.getContainers());

	}

	public static Subject update(Subject subject) {
		Subject updatedSubject = Subject.findSubject(subject.getId());
		updatedSubject.setRecordedBy(subject.getRecordedBy());
		updatedSubject.setRecordedDate(subject.getRecordedDate());
		updatedSubject.setLsTransaction(subject.getLsTransaction());
		updatedSubject.setModifiedBy(subject.getModifiedBy());
		updatedSubject.setModifiedDate(new Date());
		updatedSubject.setCodeName(subject.getCodeName());
		updatedSubject.setLsKind(subject.getLsKind());
		updatedSubject.setLsType(subject.getLsType());
		updatedSubject.setLsTypeAndKind(subject.getLsTypeAndKind());
		updatedSubject.merge();
		return updatedSubject;
	}
	
	public String toJson() {
		return new JSONSerializer()
		.include("lsLabels", "lsStates.lsValues")
		.exclude("*.class", "lsStates.subject", "lsStates.lsValues.lsState", "treatmentGroup.analysisGroup")
		.transform(new ExcludeNulls(), void.class)
		.serialize(this);
	}
	
	public String toJsonStub() {
		return new JSONSerializer()
		.include("lsLabels")
		.exclude("*.class", "lsStates", "treatmentGroup.analysisGroup")
		.transform(new ExcludeNulls(), void.class)
		.prettyPrint(false).serialize(this);
	}
	
	public String toPrettyJson() {
		return new JSONSerializer()
		.include("lsLabels", "lsStates.lsValues")
		.exclude("*.class", "lsStates.subject", "lsStates.lsValues.lsState", "treatmentGroup.analysisGroup")
		.transform(new ExcludeNulls(), void.class)
		.prettyPrint(true).serialize(this);
	}
	
	public String toPrettyJsonStub() {
		return new JSONSerializer()
		.include("lsLabels")
		.exclude("*.class", "lsStates", "treatmentGroup.analysisGroup")
		.transform(new ExcludeNulls(), void.class)
		.prettyPrint(true).serialize(this);
	}
	
//	public String toJson() {
//        return new JSONSerializer()
//        .include("lsLabels", "lsStates.lsValues")
//        .exclude("*.class", "lsStates.subject", "lsStates.lsValues.lsState", "treatmentGroup.analysisGroup")
//        .serialize(this);
//    }	

//	.include("subjectLabels", "subjectStates.subjectValues", "containers")
//	.include("subjectLabels", "subjectStates.subjectValues")
//	.exclude("*.class",  "subjectLabels.subject", "subjectStates.subject", "subjectStates.subjectValues.subjectState", "treatmentGroup.analysisGroup")

	public static Subject fromJsonToSample(String json) {
		return new JSONDeserializer<Subject>().
				use(null, Subject.class).
        		
				deserialize(json);
	}

	public static String toJsonArray(Collection<Subject> collection) {
		return new JSONSerializer()
		.include("lsLabels", "lsStates.lsValues")
		.exclude("*.class",  "lsLabels.subject", "lsStates.subject", "lsStates.lsValues.lsState", "treatmentGroup.analysisGroup")
		.serialize(collection);
	}
	
	public static String toJsonArrayStub(Collection<Subject> collection) {
		return new JSONSerializer()
		.include("lsLabels")
		.exclude("*.class", "lsStates", "treatmentGroup.analysisGroup")
		.transform(new ExcludeNulls(), void.class)
		.serialize(collection);
	}



	public static Subject fromJsonToSubject(String json) {
        return new JSONDeserializer<Subject>().
        		use(null, Subject.class).
        		
        		deserialize(json);
    }

	public static Collection<Subject> fromJsonArrayToSubjects(String json) {
        return new JSONDeserializer<List<Subject>>().use(null, ArrayList.class).use("values", Subject.class).deserialize(json);
    }

	public static Collection<Subject> fromJsonArrayToSubjects(Reader json) {
        return new JSONDeserializer<List<Subject>>().use(null, ArrayList.class).use("values", Subject.class).deserialize(json);
    }
	
	@Transactional
	public static int deleteByExperimentID(Long experimentId) {
		if (experimentId == null) return 0;
		EntityManager em = ItxSubjectContainer.entityManager();
		String deleteSQL = "DELETE FROM Subject s WHERE Subject IN (SELECT s FROM Subject s JOIN s.treatmentGroups t JOIN t.analysisGroups a JOIN a.experiments e WHERE e.id = :experimentId)";
		Query q = em.createQuery(deleteSQL);
		q.setParameter("experimentId", experimentId);
		//int numberOfDeletedEntities = q.executeUpdate();
		//return numberOfDeletedEntities;
		return 0;
	}
	
	@Transactional
	public static void removeOrphans(Collection<Long> subjectIds) {
		for (Long id: subjectIds) {
			Subject subject = Subject.findSubject(id);
			if (subject.getTreatmentGroups().isEmpty()) {
				subject.remove();
//				Subject.removeCascadeAware(id);
			}
		}
	}

	@Transactional
	private static void removeCascadeAware(Long id) {
		Subject subject = findSubject(id);
        EntityManager em = Subject.entityManager();
        subject.remove();
		
	}

	public Subject() {
		
    }
	
	public Subject(FlatThingCsvDTO subjectDTO) {
		this.setRecordedBy(subjectDTO.getRecordedBy());
		this.setRecordedDate(subjectDTO.getRecordedDate());
		this.setLsTransaction(subjectDTO.getLsTransaction());
		this.setModifiedBy(subjectDTO.getModifiedBy());
		this.setModifiedDate(subjectDTO.getModifiedDate());
		this.setCodeName(subjectDTO.getCodeName());
		this.setLsKind(subjectDTO.getLsKind());
		this.setLsType(subjectDTO.getLsType());
	}
	
	public static Subject findSubjectByCodeNameEquals(String codeName) {
        if (codeName == null || codeName.length() == 0) throw new IllegalArgumentException("The codeName argument is required");
        EntityManager em = Subject.entityManager();
        TypedQuery<Subject> q = em.createQuery("SELECT o FROM Subject AS o WHERE o.codeName = :codeName", Subject.class);
        q.setParameter("codeName", codeName);
        return q.getSingleResult();
    }

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "treatmentGroups", "lsLabels", "lsStates", "containers");

	public static long countSubjects() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Subject o", Long.class).getSingleResult();
    }

	public static List<Subject> findAllSubjects() {
        return entityManager().createQuery("SELECT o FROM Subject o", Subject.class).getResultList();
    }

	public static List<Subject> findAllSubjects(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Subject o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Subject.class).getResultList();
    }

	public static Subject findSubject(Long id) {
        if (id == null) return null;
        return entityManager().find(Subject.class, id);
    }

	public static List<Subject> findSubjectEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Subject o", Subject.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<Subject> findSubjectEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Subject o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Subject.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public Subject merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Subject merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public Set<TreatmentGroup> getTreatmentGroups() {
        return this.treatmentGroups;
    }

	public void setTreatmentGroups(Set<TreatmentGroup> treatmentGroups) {
        this.treatmentGroups = treatmentGroups;
    }

	public Set<SubjectLabel> getLsLabels() {
        return this.lsLabels;
    }

	public void setLsLabels(Set<SubjectLabel> lsLabels) {
        this.lsLabels = lsLabels;
    }

	public Set<SubjectState> getLsStates() {
        return this.lsStates;
    }

	public void setLsStates(Set<SubjectState> lsStates) {
        this.lsStates = lsStates;
    }

	public Set<ItxSubjectContainer> getContainers() {
        return this.containers;
    }

	public void setContainers(Set<ItxSubjectContainer> containers) {
        this.containers = containers;
    }

	public static Long countFindSubjectsByCodeNameEquals(String codeName) {
        if (codeName == null || codeName.length() == 0) throw new IllegalArgumentException("The codeName argument is required");
        EntityManager em = Subject.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Subject AS o WHERE o.codeName = :codeName", Long.class);
        q.setParameter("codeName", codeName);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindSubjectsByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = Subject.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Subject AS o WHERE o.lsTransaction = :lsTransaction", Long.class);
        q.setParameter("lsTransaction", lsTransaction);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindSubjectsByTreatmentGroups(Set<TreatmentGroup> treatmentGroups) {
        if (treatmentGroups == null) throw new IllegalArgumentException("The treatmentGroups argument is required");
        EntityManager em = Subject.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT COUNT(o) FROM Subject AS o WHERE");
        for (int i = 0; i < treatmentGroups.size(); i++) {
            if (i > 0) queryBuilder.append(" AND");
            queryBuilder.append(" :treatmentGroups_item").append(i).append(" MEMBER OF o.treatmentGroups");
        }
        TypedQuery q = em.createQuery(queryBuilder.toString(), Long.class);
        int treatmentGroupsIndex = 0;
        for (TreatmentGroup _treatmentgroup: treatmentGroups) {
            q.setParameter("treatmentGroups_item" + treatmentGroupsIndex++, _treatmentgroup);
        }
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<Subject> findSubjectsByCodeNameEquals(String codeName) {
        if (codeName == null || codeName.length() == 0) throw new IllegalArgumentException("The codeName argument is required");
        EntityManager em = Subject.entityManager();
        TypedQuery<Subject> q = em.createQuery("SELECT o FROM Subject AS o WHERE o.codeName = :codeName", Subject.class);
        q.setParameter("codeName", codeName);
        return q;
    }

	public static TypedQuery<Subject> findSubjectsByCodeNameEquals(String codeName, String sortFieldName, String sortOrder) {
        if (codeName == null || codeName.length() == 0) throw new IllegalArgumentException("The codeName argument is required");
        EntityManager em = Subject.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Subject AS o WHERE o.codeName = :codeName");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Subject> q = em.createQuery(queryBuilder.toString(), Subject.class);
        q.setParameter("codeName", codeName);
        return q;
    }

	public static TypedQuery<Subject> findSubjectsByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = Subject.entityManager();
        TypedQuery<Subject> q = em.createQuery("SELECT o FROM Subject AS o WHERE o.lsTransaction = :lsTransaction", Subject.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }

	public static TypedQuery<Subject> findSubjectsByLsTransactionEquals(Long lsTransaction, String sortFieldName, String sortOrder) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = Subject.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Subject AS o WHERE o.lsTransaction = :lsTransaction");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Subject> q = em.createQuery(queryBuilder.toString(), Subject.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }

	public static TypedQuery<Subject> findSubjectsByTreatmentGroups(Set<TreatmentGroup> treatmentGroups) {
        if (treatmentGroups == null) throw new IllegalArgumentException("The treatmentGroups argument is required");
        EntityManager em = Subject.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Subject AS o WHERE");
        for (int i = 0; i < treatmentGroups.size(); i++) {
            if (i > 0) queryBuilder.append(" AND");
            queryBuilder.append(" :treatmentGroups_item").append(i).append(" MEMBER OF o.treatmentGroups");
        }
        TypedQuery<Subject> q = em.createQuery(queryBuilder.toString(), Subject.class);
        int treatmentGroupsIndex = 0;
        for (TreatmentGroup _treatmentgroup: treatmentGroups) {
            q.setParameter("treatmentGroups_item" + treatmentGroupsIndex++, _treatmentgroup);
        }
        return q;
    }

	public static TypedQuery<Subject> findSubjectsByTreatmentGroups(Set<TreatmentGroup> treatmentGroups, String sortFieldName, String sortOrder) {
        if (treatmentGroups == null) throw new IllegalArgumentException("The treatmentGroups argument is required");
        EntityManager em = Subject.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Subject AS o WHERE");
        for (int i = 0; i < treatmentGroups.size(); i++) {
            if (i > 0) queryBuilder.append(" AND");
            queryBuilder.append(" :treatmentGroups_item").append(i).append(" MEMBER OF o.treatmentGroups");
        }
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" " + sortOrder);
            }
        }
        TypedQuery<Subject> q = em.createQuery(queryBuilder.toString(), Subject.class);
        int treatmentGroupsIndex = 0;
        for (TreatmentGroup _treatmentgroup: treatmentGroups) {
            q.setParameter("treatmentGroups_item" + treatmentGroupsIndex++, _treatmentgroup);
        }
        return q;
    }
}
