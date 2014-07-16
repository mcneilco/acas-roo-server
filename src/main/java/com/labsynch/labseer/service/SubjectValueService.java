package com.labsynch.labseer.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.SubjectValue;

@Service
public interface SubjectValueService {

	public SubjectValue saveSubjectValue(SubjectValue subjectValue);

	public void deleteSubjectValue(SubjectValue subjectValue);

	public SubjectValue updateSubjectValue(SubjectValue subjectValue);
	
	public List<SubjectValue> getSubjectValuesBySubjectId(Long id);
	
}
