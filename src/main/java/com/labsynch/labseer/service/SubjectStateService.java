package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.domain.SubjectState;
import com.labsynch.labseer.dto.GenericStatePathRequest;
import com.labsynch.labseer.dto.SubjectStatePathDTO;

import org.springframework.stereotype.Service;

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

	SubjectState updateSubjectState(SubjectState subjectState);

	Collection<SubjectState> updateSubjectStates(
			Collection<SubjectState> subjectStates);

	SubjectState getSubjectState(String idOrCodeName, String stateType,
			String stateKind);

	Collection<SubjectStatePathDTO> getSubjectStates(
			Collection<GenericStatePathRequest> genericRequests);

	
	
}
