package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.SubjectValue;
import com.labsynch.labseer.dto.GenericValuePathRequest;
import com.labsynch.labseer.dto.SubjectValuePathDTO;

@Service
public interface SubjectValueService {

	public SubjectValue saveSubjectValue(SubjectValue subjectValue);

	public void deleteSubjectValue(SubjectValue subjectValue);

	public SubjectValue updateSubjectValue(SubjectValue subjectValue);
	
	public List<SubjectValue> getSubjectValuesBySubjectId(Long id);
	
	public List<SubjectValue> getSubjectValuesByExperimentIdAndStateTypeKind(Long experimentId, String stateType, String stateKind);
	
	public List<SubjectValue> getSubjectValuesByExperimentIdAndStateTypeKindAndValueTypeKind(Long experimentId, String stateType,
			String stateKind, String valueType, String valueKind);
	
	public String getCsvList(List<SubjectValue> subjectValues);
	
	public List<SubjectValue> getSubjectValuesByAnalysisGroupIdAndStateTypeKind(Long analysisGroupId, String stateType, String stateKind);

	public SubjectValue updateSubjectValue(String idOrCodeName,
			String stateType, String stateKind, String valueType,
			String valueKind, String value);

	List<SubjectValue> getSubjectValuesBySubjectIdAndStateTypeKindAndValueTypeKind(
			Long subjectId, String stateType, String stateKind,
			String valueType, String valueKind);

	Collection<SubjectValue> saveSubjectValues(
			Collection<SubjectValue> subjectValues);

	public Collection<SubjectValue> updateSubjectValues(
			Collection<SubjectValue> subjectValues);

	public SubjectValue getSubjectValue(String idOrCodeName, String stateType,
			String stateKind, String valueType, String valueKind);

	public Collection<SubjectValuePathDTO> getSubjectValues(
			Collection<GenericValuePathRequest> genericRequests);
	
}
