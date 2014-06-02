package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.dto.SubjectDTO;

@Service
public interface SubjectService {

	SubjectDTO getSubject(Subject subject);
	Set <SubjectDTO> getSubjects(Set<Subject> subjects);
	Set<SubjectDTO> getSubjectsWithStateTypeAndKind(Collection<Subject> subjects,
			String stateTypeKind);
	Set<Subject> ignoreAllSubjectStates(Set<Subject> subjects);
	Subject updateSubject(Subject subject);
	Subject saveSubject(Subject subject);

	
	
}
