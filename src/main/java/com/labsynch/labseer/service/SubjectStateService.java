package com.labsynch.labseer.service;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.SubjectState;

@Service
public interface SubjectStateService {

	Collection<SubjectState> ignoreAllSubjectStates(Collection<SubjectState> subjectStates);

	
	
}
