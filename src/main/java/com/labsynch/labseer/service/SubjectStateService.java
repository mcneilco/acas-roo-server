package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.SubjectState;

@Service
public interface SubjectStateService {

	Collection<SubjectState> ignoreAllSubjectStates(Collection<SubjectState> subjectStates);

	List<SubjectState> getSubjectStatesBySubjectIdAndStateTypeKind(
			Long subjectId, String stateType, String stateKind);

	SubjectState createSubjectStateBySubjectIdAndStateTypeKind(Long subjectId,
			String stateType, String stateKind);

	SubjectState saveSubjectState(SubjectState subjectState);

	Collection<SubjectState> saveSubjectStates(
			Collection<SubjectState> subjectStates);

	
	
}
