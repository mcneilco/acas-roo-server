package com.labsynch.labseer.service;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.domain.TreatmentGroup;
import com.labsynch.labseer.dto.SubjectCodeNameDTO;
import com.labsynch.labseer.dto.SubjectDTO;
import com.labsynch.labseer.dto.TempThingDTO;

@Service
public interface SubjectService {

	SubjectDTO getSubject(Subject subject);
	Set <SubjectDTO> getSubjects(Set<Subject> subjects);
	Set<SubjectDTO> getSubjectsWithStateTypeAndKind(Collection<Subject> subjects,
			String stateTypeKind);
	Set<Subject> ignoreAllSubjectStates(Set<Subject> subjects);
	Subject updateSubject(Subject subject);
	void saveSubjects(TreatmentGroup treatmentGroup, Set<Subject> subjects, Date recordedDate);
	Subject saveSubject(Set<TreatmentGroup> treatmentGroups, Subject subject, Date recordedDate);
	Subject saveSubject(Subject subject);
	
	HashMap<String, TempThingDTO> createSubjectsFromCSV(String subjectFilePath, HashMap<String, TempThingDTO> treatmentGroupMap) throws IOException;
	Collection<SubjectCodeNameDTO> getSubjectsByCodeNames(List<String> codeNames);

	
	
}
