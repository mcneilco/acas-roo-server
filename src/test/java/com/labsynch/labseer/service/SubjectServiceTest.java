

package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.domain.SubjectLabel;
import com.labsynch.labseer.domain.SubjectState;
import com.labsynch.labseer.domain.SubjectValue;
import com.labsynch.labseer.dto.SubjectDTO;
import com.labsynch.labseer.dto.SubjectLabelDTO;
import com.labsynch.labseer.dto.SubjectStateDTO;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
public class SubjectServiceTest {
	
	private static final Logger logger = LoggerFactory.getLogger(SubjectServiceTest.class);
	
	@Autowired
	private SubjectService subjectService;
	
	
//	@Test
	public void FindSubjectTest_1(){
		Long subjectId = 3860L; //3860L; //3484
		Subject subj = Subject.findSubject(subjectId);
		logger.info(subj.toJson());
		
		SubjectDTO subjectDTO = new SubjectDTO(subj);

		List<SubjectLabel> subjectLabels = SubjectLabel.findSubjectLabelsBySubject(subj).getResultList();
		Set<SubjectLabelDTO> subjectLabelDTOs = new HashSet<SubjectLabelDTO>();
		for (SubjectLabel subjectLabel : subjectLabels){
			SubjectLabelDTO subjectLabelDTO = new SubjectLabelDTO(subjectLabel);
			subjectLabelDTOs.add(subjectLabelDTO);
		}
		subjectDTO.setLsLabels(subjectLabelDTOs);
		
		List<SubjectState> subjectStates = SubjectState.findSubjectStatesBySubject(subj).getResultList();
		Set<SubjectStateDTO> subjectStateDTOs = new HashSet<SubjectStateDTO>();
		for (SubjectState ss:subjectStates ){
			SubjectStateDTO ssDTO = new SubjectStateDTO(ss);
			Set<SubjectValue> subjectValues = new HashSet<SubjectValue>(SubjectValue.findSubjectValuesByLsState(ss).getResultList());
			ssDTO.setSubjectValues(subjectValues);
			subjectStateDTOs.add(ssDTO);
		}
		subjectDTO.setLsStates(subjectStateDTOs);
		
		logger.info(subjectDTO.toJson());
		
	}
	
//	@Test
	public void FindSubjectTest_2(){
		Subject subject1 = new Subject();
		subject1.setId(75186L);
		Subject subject2 = new Subject();
		subject2.setId(75187L);
		Subject subject3 = new Subject();
		subject3.setId(75190L);

		Set<Subject> subjectList = new HashSet<Subject>();
		subjectList.add(subject1);
		subjectList.add(subject2);
		subjectList.add(subject3);
		
		logger.info(Subject.toJsonArray(subjectList));

		String stateTypeKind = "data_results";
		Set<SubjectDTO> output = subjectService.getSubjectsWithStateTypeAndKind(subjectList, stateTypeKind);
		logger.info(SubjectDTO.toJsonArray(output));
		
	}

	//@Test
	public void FindSubjectTest_3(){
		String json = "[{\"id\":75186}, {\"id\":75187}, {\"id\":75190}]";
		String stateTypeAndKind = "data_results";
    	logger.info("incoming json: " + json);
    	logger.info("incoming stateTypeAndKind: " + stateTypeAndKind);
    	Collection<Subject> querySubjects = Subject.fromJsonArrayToSubjects(json);
		Set<Subject> subjectList = new HashSet<Subject>();
		for (Subject query : querySubjects){
			logger.info("current subject: " + query.toJson());
			Subject newSubject = new Subject();
			newSubject.setId(query.getId());
			subjectList.add(newSubject);
		}
    	Set<SubjectDTO> subjects = subjectService.getSubjectsWithStateTypeAndKind(subjectList, stateTypeAndKind);
    	logger.info(SubjectDTO.toJsonArray(subjects));
	}
	
//	@Test
	public void IgnoreSubjectTest_4(){
		String json = "[{\"id\":75186}, {\"id\":75187}, {\"id\":75190}]";
    	logger.info("incoming json: " + json);
    	Collection<Subject> querySubjects = Subject.fromJsonArrayToSubjects(json);
		Set<Subject> subjectList = new HashSet<Subject>();
		for (Subject query : querySubjects){
			logger.info("current subject: " + query.toJson());
			Subject newSubject = new Subject();
			newSubject.setId(query.getId());
			subjectList.add(newSubject);
		}
    	Set<Subject> subjects = subjectService.ignoreAllSubjectStates(subjectList);
    	logger.info(Subject.toJsonArray(subjects));
	}
	
	@Test
	public void SimpleTest_5(){
		Subject subject = Subject.findSubject(75228L);
		logger.info(SubjectLabel.toJsonArray(SubjectLabel.findSubjectLabelsBySubject(subject).getResultList()));
		logger.info(subject.toJson());
	}
}
