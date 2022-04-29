package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.AnalysisGroupValue;
import com.labsynch.labseer.domain.Container;
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ItxSubjectContainer;
import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.domain.SubjectValue;
import com.labsynch.labseer.domain.TreatmentGroupValue;
import com.labsynch.labseer.utils.ExcludeNulls;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


public class ContainerDependencyCheckDTO {
	
	public ContainerDependencyCheckDTO() {
		this.linkedDataExists = false;
		this.dependentCorpNames = new HashSet<String>();
		this.linkedExperiments = new HashSet<CodeTableDTO>();
	}
	
	public ContainerDependencyCheckDTO(Container queryContainer) {
		this.setQueryContainer(queryContainer);
	}
	
	private static final Logger logger = LoggerFactory.getLogger(ContainerDependencyCheckDTO.class);
	
	private Container queryContainer;
	
	private Collection<String> dependentCorpNames;
	
	private boolean linkedDataExists;
	
	private Collection<CodeTableDTO> linkedExperiments;
	
	private Collection<ErrorMessageDTO> errors;
	
	@Transactional
	public String toJson() {
	        return new JSONSerializer().exclude("*.class").include("linkedExperiments.*", "queryCodeNames.*", "dependentCorpNames.*").transform(new ExcludeNulls(), void.class).serialize(this);
	    }
	
	@Transactional
	public void checkForDependentData(Container container, Collection<Container> members){
		errors = new HashSet<ErrorMessageDTO>();
		checkForSubjectInteractions(container);
		if (!members.isEmpty()) checkMemberSubjectInteractions(members);
	}

	private void checkMemberSubjectInteractions(Collection<Container> members) {
		Collection<Subject> referencedSubjects = new HashSet<Subject>();
		for (Container member : members){
			Collection<ItxSubjectContainer> subjectItxs = member.getSubjects();
			for (ItxSubjectContainer subjectItx : subjectItxs){
				if (!subjectItx.isIgnored() && !subjectItx.getSubject().isIgnored()) referencedSubjects.add(subjectItx.getSubject());
			}
		}
		if (!referencedSubjects.isEmpty()){
			Collection<Experiment> referencedExperiments = getReferencedExperiments(referencedSubjects);
			for (Experiment experiment : referencedExperiments){
				this.linkedExperiments.add(new CodeTableDTO(experiment));
			}
			if (!this.linkedExperiments.isEmpty()) this.linkedDataExists = true;
		}
	}

	private void checkForSubjectInteractions(Container container) {
		Collection<Subject> referencedSubjects = new HashSet<Subject>();
		Collection<ItxSubjectContainer> subjectItxs = container.getSubjects();
		for (ItxSubjectContainer subjectItx : subjectItxs){
			if (!subjectItx.isIgnored() && !subjectItx.getSubject().isIgnored()) referencedSubjects.add(subjectItx.getSubject());
		}
		if (!referencedSubjects.isEmpty()){
			Collection<Experiment> referencedExperiments = getReferencedExperiments(referencedSubjects);
			for (Experiment experiment : referencedExperiments){
				this.linkedExperiments.add(new CodeTableDTO(experiment));
			}
			if (!this.linkedExperiments.isEmpty()) this.linkedDataExists = true;
		}
	}


	private void dedupeLinkedExperiments() {
		HashMap<String,String> experimentMap = new HashMap<String,String>();
		logger.debug("Incoming size: "+linkedExperiments.size());
		for (CodeTableDTO codeTable : linkedExperiments){
			experimentMap.put(codeTable.getCode(), codeTable.getName());
		}
		HashSet<CodeTableDTO> dedupedExperimentSet = new HashSet<CodeTableDTO>();
		for (String key : experimentMap.keySet()){
			CodeTableDTO experimentCodeTable = new CodeTableDTO();
			experimentCodeTable.setCode(key);
			experimentCodeTable.setName(experimentMap.get(key));
			dedupedExperimentSet.add(experimentCodeTable);
		}
		linkedExperiments = dedupedExperimentSet;
		logger.debug("Deduped size: "+linkedExperiments.size());
	}
	
	private Collection<Experiment> getReferencedExperiments(Collection<Subject> subjects){
		String hql = "SELECT experiment.id FROM Subject subject JOIN subject.treatmentGroups as treatmentGroup JOIN treatmentGroup.analysisGroups as analysisGroup JOIN analysisGroup.experiments as experiment WHERE subject IN :subjects";
		EntityManager em = Subject.entityManager();
		TypedQuery<Long> query = em.createQuery(hql, Long.class);
		query.setParameter("subjects", subjects);
		Collection<Long> experimentIds = query.getResultList();
		Collection<Experiment> results = new HashSet<Experiment>();
		for (Long experimentId : experimentIds){
			results.add(Experiment.findExperiment(experimentId));
		}
		return results;
	}
	

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public Container getQueryContainer() {
        return this.queryContainer;
    }

	public void setQueryContainer(Container queryContainer) {
        this.queryContainer = queryContainer;
    }

	public Collection<String> getDependentCorpNames() {
        return this.dependentCorpNames;
    }

	public void setDependentCorpNames(Collection<String> dependentCorpNames) {
        this.dependentCorpNames = dependentCorpNames;
    }

	public boolean isLinkedDataExists() {
        return this.linkedDataExists;
    }

	public void setLinkedDataExists(boolean linkedDataExists) {
        this.linkedDataExists = linkedDataExists;
    }

	public Collection<CodeTableDTO> getLinkedExperiments() {
        return this.linkedExperiments;
    }

	public void setLinkedExperiments(Collection<CodeTableDTO> linkedExperiments) {
        this.linkedExperiments = linkedExperiments;
    }

	public Collection<ErrorMessageDTO> getErrors() {
        return this.errors;
    }

	public void setErrors(Collection<ErrorMessageDTO> errors) {
        this.errors = errors;
    }

	public static ContainerDependencyCheckDTO fromJsonToContainerDependencyCheckDTO(String json) {
        return new JSONDeserializer<ContainerDependencyCheckDTO>()
        .use(null, ContainerDependencyCheckDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<ContainerDependencyCheckDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<ContainerDependencyCheckDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<ContainerDependencyCheckDTO> fromJsonArrayToCoes(String json) {
        return new JSONDeserializer<List<ContainerDependencyCheckDTO>>()
        .use("values", ContainerDependencyCheckDTO.class).deserialize(json);
    }
}


