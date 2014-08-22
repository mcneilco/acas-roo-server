package com.labsynch.labseer.service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.labsynch.labseer.domain.AbstractValue;
import com.labsynch.labseer.domain.ExperimentValue;
import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.domain.SubjectState;
import com.labsynch.labseer.domain.SubjectValue;
import com.labsynch.labseer.domain.TreatmentGroupValue;


@Service
@Transactional
public class SubjectValueServiceImpl implements SubjectValueService {

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
				beanWriter.write(subjectValue, header, processors);
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
}
