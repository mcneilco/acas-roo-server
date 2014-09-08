package com.labsynch.labseer.domain;

import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
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

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.dto.FlatThingCsvDTO;
import com.labsynch.labseer.utils.CustomBigDecimalFactory;
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Entity
@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findSubjectsByTreatmentGroups", "findSubjectsByLsTransactionEquals", "findSubjectsByCodeNameEquals" })
@RooJson
public class Subject extends AbstractThing {

	@ManyToMany(cascade = CascadeType.ALL, fetch =  FetchType.LAZY)
	@JoinTable(name="TREATMENTGROUP_SUBJECT", 
	joinColumns={@JoinColumn(name="subject_id")}, 
	inverseJoinColumns={@JoinColumn(name="treatment_group_id")})
    private Set<TreatmentGroup> treatmentGroups = new HashSet<TreatmentGroup>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "subject", fetch =  FetchType.LAZY)
    private Set<SubjectLabel> lsLabels = new HashSet<SubjectLabel>();
    
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "subject", fetch =  FetchType.LAZY)
	private Set<SubjectState> lsStates = new HashSet<SubjectState>();

//	@OneToMany(cascade = CascadeType.ALL, mappedBy = "subject", fetch =  FetchType.EAGER)
//	private Set<ItxSubjectContainer> containers = new HashSet<ItxSubjectContainer>();

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
        
//        this.setSubjectLabels(subject.getSubjectLabels());
//        this.setSubjectStates(subject.getSubjectStates());
//        this.setContainers(subject.getContainers());

	}

	public static Subject update(Subject subject) {
		Subject updatedSubject = new JSONDeserializer<Subject>().
									use(null, Subject.class).
									use(BigDecimal.class, new CustomBigDecimalFactory()).
									deserializeInto(subject.toJson(), Subject.findSubject(subject.getId()));
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
        		use(BigDecimal.class, new CustomBigDecimalFactory()).
				deserialize(json);
	}

	public static String toJsonArray(Collection<Subject> collection) {
		return new JSONSerializer()
		.include("lsLabels", "lsStates.lsValues")
		.exclude("*.class",  "lsLabels.subject", "lsStates.subject", "lsStates.lsValues.lsState", "treatmentGroup.analysisGroup")
		.serialize(collection);
	}



	public static Subject fromJsonToSubject(String json) {
        return new JSONDeserializer<Subject>().
        		use(null, Subject.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).
        		deserialize(json);
    }

	public static Collection<Subject> fromJsonArrayToSubjects(String json) {
        return new JSONDeserializer<List<Subject>>().use(null, ArrayList.class).use("values", Subject.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
    }

	public static Collection<Subject> fromJsonArrayToSubjects(Reader json) {
        return new JSONDeserializer<List<Subject>>().use(null, ArrayList.class).use("values", Subject.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
    }
	
	@Transactional
	public static int deleteByExperimentID(Long experimentId) {
		if (experimentId == null) return 0;
		EntityManager em = ItxSubjectContainer.entityManager();
		String deleteSQL = "DELETE FROM Subject oo WHERE id in (select o.id from Subject o where o.treatmentGroup.analysisGroup.experiment.id = :experimentId)";
		Query q = em.createQuery(deleteSQL);
		q.setParameter("experimentId", experimentId);
		int numberOfDeletedEntities = q.executeUpdate();
		return numberOfDeletedEntities;
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
}
