package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.AnalysisGroupValue;
import com.labsynch.labseer.domain.Container;
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ItxSubjectContainer;
import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.domain.SubjectValue;
import com.labsynch.labseer.domain.TreatmentGroupValue;
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
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
	
}


