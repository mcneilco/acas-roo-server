package com.labsynch.labseer.service;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.domain.SubjectState;
import com.labsynch.labseer.domain.SubjectValue;
import com.labsynch.labseer.dto.GenericValuePathRequest;
import com.labsynch.labseer.dto.SubjectValueDTO;
import com.labsynch.labseer.dto.SubjectValuePathDTO;
import com.labsynch.labseer.utils.SimpleUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;


@Service
@Transactional
public class SubjectValueServiceImpl implements SubjectValueService {

	@Autowired
	private SubjectStateService subjectStateService;
	
	private static final Logger logger = LoggerFactory.getLogger(SubjectValueServiceImpl.class);

	@Override
	public void deleteSubjectValue(SubjectValue subjectValue){
		logger.debug("incoming meta subject: " + subjectValue.toJson());

	}

	//Query hibernate object and grab existing table references - add them to json hydrated object
	@Override
	@Transactional
	public SubjectValue updateSubjectValue(SubjectValue subjectValue){
		if (subjectValue.getLsState().getId() == null) {
			SubjectState subjectState = new SubjectState(subjectValue.getLsState());
			subjectState.setSubject(Subject.findSubject(subjectValue.getLsState().getSubject().getId()));
			subjectState.persist();
			subjectValue.setLsState(subjectState); 
		} else {
			subjectValue.setLsState(SubjectState.findSubjectState(subjectValue.getLsState().getId()));
		}
		subjectValue.setVersion(SubjectValue.findSubjectValue(subjectValue.getId()).getVersion());
		subjectValue.merge();
		return subjectValue;
	}

	@Override
	@Transactional
	public SubjectValue saveSubjectValue(SubjectValue subjectValue) {
		if (subjectValue.getLsState().getId() == null) {
			SubjectState subjectState = new SubjectState(subjectValue.getLsState());
			subjectState.setSubject(Subject.findSubject(subjectValue.getLsState().getSubject().getId()));
			subjectState.persist();
			subjectValue.setLsState(subjectState); 
		} else {
			subjectValue.setLsState(SubjectState.findSubjectState(subjectValue.getLsState().getId()));
		}		
		subjectValue.persist();
		return subjectValue;
	}
	
	@Override
	@Transactional
	public Collection<SubjectValue> saveSubjectValues(Collection<SubjectValue> subjectValues) {
		for (SubjectValue subjectValue: subjectValues) {
			subjectValue = saveSubjectValue(subjectValue);
		}
		return subjectValues;
	}

	@Override
	public List<SubjectValue> getSubjectValuesBySubjectId(Long id){	
		List<SubjectValue> subjectValues = new ArrayList<SubjectValue>();
		Subject subject = Subject.findSubject(id);
		if(subject.getLsStates() != null) {
			for (SubjectState subjectState : subject.getLsStates()) {
				if(subjectState.getLsValues() != null) {
					for(SubjectValue subjectValue : subjectState.getLsValues()) {
						subjectValues.add(subjectValue);
					}
				}
			}
		}
		return subjectValues;
	}

	@Override
	public List<SubjectValue> getSubjectValuesByExperimentIdAndStateTypeKind(
			Long experimentId, String stateType, String stateKind) {
		
		List<SubjectValue> subjectValues = SubjectValue.findSubjectValuesByExptIDAndStateTypeKind(experimentId, stateType, stateKind).getResultList();

		return subjectValues;
	}

	@Override
	public List<SubjectValue> getSubjectValuesByExperimentIdAndStateTypeKindAndValueTypeKind(
			Long experimentId, String stateType, String stateKind,
			String valueType, String valueKind) {
		
		List<SubjectValue> subjectValues = SubjectValue.findSubjectValuesByExptIDAndStateTypeKindAndValueTypeKind(experimentId, stateType,
				stateKind, valueType, valueKind).getResultList();
		
		return subjectValues;
	}

	@Override
	public String getCsvList(List<SubjectValue> subjectValues) {
		StringWriter outFile = new StringWriter();
		ICsvBeanWriter beanWriter = null;
		try {
			beanWriter = new CsvBeanWriter(outFile, CsvPreference.TAB_PREFERENCE);
			final String[] header = SubjectValue.getColumns();
			final CellProcessor[] processors = SubjectValue.getProcessors();
			beanWriter.writeHeader(header);
			for (final SubjectValue subjectValue : subjectValues) {
				Collection<SubjectValueDTO> subjectValueDTOs = subjectValue.makeDTOsByTreatmentGroupIds();
				for (SubjectValueDTO subjectValueDTO : subjectValueDTOs) {
					beanWriter.write(subjectValueDTO, header, processors);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (beanWriter != null) {
				try {
					beanWriter.close();
					outFile.flush();
					outFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return outFile.toString();
	}

	@Override
	public List<SubjectValue> getSubjectValuesByAnalysisGroupIdAndStateTypeKind(
			Long analysisGroupId, String stateType, String stateKind) {
		
		List<SubjectValue> subjectValues = SubjectValue.findSubjectValuesByAnalysisGroupIDAndStateTypeKind(analysisGroupId, stateType, stateKind).getResultList();

		return subjectValues;
	}

	@Override
	public List<SubjectValue> getSubjectValuesBySubjectIdAndStateTypeKindAndValueTypeKind(
			Long subjectId, String stateType, String stateKind,
			String valueType, String valueKind) {
		
		List<SubjectValue> subjectValues = SubjectValue.findSubjectValuesBySubjectIDAndStateTypeKindAndValueTypeKind(subjectId, stateType,
				stateKind, valueType, valueKind).getResultList();
		
		return subjectValues;
	}
	
	@Override
	public SubjectValue updateSubjectValue(String idOrCodeName, String stateType, String stateKind, String valueType, String valueKind, String value) {
		//fetch the entity
		Subject subject;
		if(SimpleUtil.isNumeric(idOrCodeName)) {
			subject = Subject.findSubject(Long.valueOf(idOrCodeName));
		} else {		
			try {
				subject = Subject.findSubjectsByCodeNameEquals(idOrCodeName).getSingleResult();
			} catch(Exception ex) {
				subject = null;
			}
		}
		//fetch the state, and if it doesn't exist, create it
		List<SubjectState> subjectStates;
		if(subject != null) {
			Long subjectId = subject.getId();
			subjectStates = subjectStateService.getSubjectStatesBySubjectIdAndStateTypeKind(subjectId, stateType, stateKind);
			if (subjectStates.isEmpty()) {
				//create the state
				subjectStates.add(subjectStateService.createSubjectStateBySubjectIdAndStateTypeKind(subjectId, stateType, stateKind));
				logger.debug("Created the subject state: " + subjectStates.get(0).toJson());
			}
		}
		//fetch the value, update it if it exists, and if it doesn't exist, create it
		List<SubjectValue> subjectValues;
		SubjectValue subjectValue = null;
		if(subject != null) {
			Long subjectId = subject.getId();
			subjectValues = getSubjectValuesBySubjectIdAndStateTypeKindAndValueTypeKind(subjectId, stateType, stateKind, valueType, valueKind);
			if (subjectValues.size() > 1){
				logger.error("Error: multiple subject statuses found");
			}
			else if (subjectValues.size() == 1){
				subjectValue = subjectValues.get(0);
				subjectValue.setIgnored(true);
				subjectValue.merge();
				logger.debug("Ignored the subject value: " + subjectValue.toJson());
			}
			subjectValue = createSubjectValueBySubjectIdAndStateTypeKindAndValueTypeKind(subjectId, stateType, stateKind, valueType, valueKind, value);
			logger.debug("Created the subject value: " + subjectValue.toJson());
		}
		return subjectValue;

	}

	private SubjectValue createSubjectValueBySubjectIdAndStateTypeKindAndValueTypeKind(
			Long subjectId, String stateType, String stateKind,
			String valueType, String valueKind, String value) {
		SubjectValue subjectValue = new SubjectValue();
		SubjectState subjectState = SubjectState.findSubjectStatesBySubjectIDAndStateTypeKind(subjectId, stateType, stateKind).getSingleResult();
		subjectValue.setLsState(subjectState);
		subjectValue.setLsType(valueType);
		subjectValue.setLsKind(valueKind);
		if (valueType.equals("stringValue")) subjectValue.setStringValue(value);
		if (valueType.equals("fileValue")) subjectValue.setFileValue(value);
		if (valueType.equals("clobValue")) subjectValue.setClobValue(value);
		if (valueType.equals("blobValue")) subjectValue.setBlobValue(value.getBytes(Charset.forName("UTF-8")));
		if (valueType.equals("numericValue")) subjectValue.setNumericValue(new BigDecimal(value));
		if (valueType.equals("dateValue")) subjectValue.setDateValue(new Date(Long.parseLong(value)));
		if (valueType.equals("codeValue")) subjectValue.setCodeValue(value);
		if (valueType.equals("urlValue")) subjectValue.setUrlValue(value);
		subjectValue.setRecordedBy("default");
		subjectValue.persist();
		return subjectValue;
	}
	
	@Override
	public Collection<SubjectValue> updateSubjectValues(
			Collection<SubjectValue> subjectValues) {
		for (SubjectValue subjectValue: subjectValues) {
			subjectValue = updateSubjectValue(subjectValue);
		}
		return subjectValues;
	}
	
	@Override
	public SubjectValue getSubjectValue(String idOrCodeName,
			String stateType, String stateKind, String valueType, String valueKind) {
		SubjectValue value = null;
		try{
			Collection<SubjectValue> values = getSubjectValues(idOrCodeName, stateType, stateKind, valueType, valueKind);
			value = values.iterator().next();
		}catch (Exception e){
			logger.error("Caught error "+e.toString()+" trying to find a state.",e);
			value = null;
		}
		return value;
	}

	@Override
	public Collection<SubjectValuePathDTO> getSubjectValues(
			Collection<GenericValuePathRequest> genericRequests) {
		Collection<SubjectValuePathDTO> results = new ArrayList<SubjectValuePathDTO>();
		for (GenericValuePathRequest request : genericRequests){
			SubjectValuePathDTO result = new SubjectValuePathDTO();
			result.setIdOrCodeName(request.getIdOrCodeName());
			result.setValueType(request.getValueType());
			result.setValueKind(request.getValueKind());
			result.setValues(getSubjectValues(request.getIdOrCodeName(), request.getStateType(), request.getStateKind(), request.getValueType(), request.getValueKind()));
			results.add(result);
		}
		return results;
	}
	
	private Collection<SubjectValue> getSubjectValues(String idOrCodeName, String stateType, String stateKind, String valueType, String valueKind){
		if (SimpleUtil.isNumeric(idOrCodeName)){
			Long id = Long.valueOf(idOrCodeName);
			return SubjectValue.findSubjectValuesBySubjectIDAndStateTypeKindAndValueTypeKind(id, stateType, stateKind, valueType, valueKind).getResultList();
		}else{
			return SubjectValue.findSubjectValuesBySubjectCodeNameAndStateTypeKindAndValueTypeKind(idOrCodeName, stateType, stateKind, valueType, valueKind).getResultList();
		}
	}
}
