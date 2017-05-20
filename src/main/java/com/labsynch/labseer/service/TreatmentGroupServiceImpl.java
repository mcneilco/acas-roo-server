package com.labsynch.labseer.service;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.domain.TreatmentGroup;
import com.labsynch.labseer.domain.TreatmentGroupLabel;
import com.labsynch.labseer.domain.TreatmentGroupState;
import com.labsynch.labseer.domain.TreatmentGroupValue;
import com.labsynch.labseer.dto.FlatThingCsvDTO;
import com.labsynch.labseer.dto.TempThingDTO;
import com.labsynch.labseer.utils.PropertiesUtilService;

@Service
@Transactional
public class TreatmentGroupServiceImpl implements TreatmentGroupService {

	private static final Logger logger = LoggerFactory.getLogger(TreatmentGroupServiceImpl.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Autowired
	private AutoLabelService autoLabelService;

	@Autowired
	private SubjectService subjectService;

	@Override
	public void saveLsTreatmentGroups(AnalysisGroup newAnalysisGroup, Set<TreatmentGroup> treatmentGroups,
			Date recordedDate) {
		Set<AnalysisGroup> analysisGroups = new HashSet<AnalysisGroup>();
		analysisGroups.add(newAnalysisGroup);
		if (treatmentGroups != null){
			int j = 0;
			for (TreatmentGroup treatmentGroup : treatmentGroups){
				TreatmentGroup newTreatmentGroup = saveLsTreatmentGroup(analysisGroups, treatmentGroup, recordedDate);
				if ( j % propertiesUtilService.getBatchSize() == 0 ) { 
					newTreatmentGroup.flush();
					newTreatmentGroup.clear();
				}
				j++;
			}
		}		
	}

	@Override
	public TreatmentGroup saveLsTreatmentGroup(TreatmentGroup treatmentGroup){

		// logger.debug("incoming meta treatmentGroup: " + treatmentGroup.toJson());
		Date recordedDate = new Date();		
		return saveLsTreatmentGroup(treatmentGroup.getAnalysisGroups(), treatmentGroup, recordedDate);

	}

	@Override
	@Transactional
	public TreatmentGroup saveLsTreatmentGroup(Set<AnalysisGroup> analysisGroups, TreatmentGroup treatmentGroup, Date recordedDate){

		//// logger.debug("incoming meta treatmentGroup: " + treatmentGroup.toJson());
		TreatmentGroup newTreatmentGroup = null;

		if (treatmentGroup.getId() == null){
			newTreatmentGroup = new TreatmentGroup(treatmentGroup);
			if (newTreatmentGroup.getCodeName() == null){
				newTreatmentGroup.setCodeName(autoLabelService.getTreatmentGroupCodeName());
			}
			if (newTreatmentGroup.getRecordedDate() == null){
				newTreatmentGroup.setRecordedDate(recordedDate);
			}

			for (AnalysisGroup analysisGroup : analysisGroups){
				newTreatmentGroup.getAnalysisGroups().add(AnalysisGroup.findAnalysisGroup(analysisGroup.getId()));				
			}
			newTreatmentGroup.persist();
			// logger.debug("persisted the new treatmentGroup: " + newTreatmentGroup.toJson());

			saveLabels(treatmentGroup, newTreatmentGroup, recordedDate);
			saveStates(treatmentGroup, newTreatmentGroup, recordedDate);

			// logger.debug("look at subjects: ------------------ " + Subject.toJsonArray(treatmentGroup.getSubjects()));
			saveSubjects(newTreatmentGroup, treatmentGroup.getSubjects(), recordedDate);


		} else {
			newTreatmentGroup = TreatmentGroup.findTreatmentGroup(treatmentGroup.getId());
			for (AnalysisGroup analysisGroup : analysisGroups){
				newTreatmentGroup.getAnalysisGroups().add(AnalysisGroup.findAnalysisGroup(analysisGroup.getId()));				
			}
			newTreatmentGroup.merge();
			// logger.debug("updated the treatmentGroup: -------------- " + newTreatmentGroup.toJson());
			if (treatmentGroup.getSubjects() != null){
				// logger.debug("look at subjects: ------------------ " + Subject.toJsonArray(newTreatmentGroup.getSubjects()));
				saveSubjects(newTreatmentGroup, newTreatmentGroup.getSubjects(), recordedDate);				
			}
		}

		return newTreatmentGroup;
	}

	private void saveSubjects(TreatmentGroup treatmentGroup,
			Set<Subject> subjects, Date recordedDate) {
		if (treatmentGroup.getSubjects().size() > 0){
			subjectService.saveSubjects(treatmentGroup, subjects, recordedDate);
		}		
	}

	private void saveStates(TreatmentGroup treatmentGroup,
			TreatmentGroup newTreatmentGroup, Date recordedDate) {
		if (treatmentGroup.getLsStates() != null){
			for(TreatmentGroupState treatmentGroupState : treatmentGroup.getLsStates()){
				TreatmentGroupState newTreatmentGroupState = new TreatmentGroupState(treatmentGroupState);
				newTreatmentGroupState.setTreatmentGroup(newTreatmentGroup);
				newTreatmentGroupState.persist();
				if (treatmentGroupState.getLsValues() != null){
					for(TreatmentGroupValue treatmentGroupValue : treatmentGroupState.getLsValues()){
						treatmentGroupValue.setLsState(newTreatmentGroupState);
						if (treatmentGroupValue.getRecordedDate() == null) {treatmentGroupValue.setRecordedDate(recordedDate);}
						treatmentGroupValue.persist();
					}										
				}
			}					
		}		
	}

	private void saveLabels(TreatmentGroup treatmentGroup,
			TreatmentGroup newTreatmentGroup, Date recordedDate) {
		if (treatmentGroup.getLsLabels() != null) {
			for(TreatmentGroupLabel treatmentGroupLabel : treatmentGroup.getLsLabels()){
				TreatmentGroupLabel newTreatmentGroupLabel = new TreatmentGroupLabel(treatmentGroupLabel);
				newTreatmentGroupLabel.setTreatmentGroup(newTreatmentGroup);
				if (newTreatmentGroupLabel.getRecordedDate() == null) {newTreatmentGroupLabel.setRecordedDate(recordedDate);}
				newTreatmentGroupLabel.persist();	
			}		
		}		
	}

	@Override
	@Transactional
	public TreatmentGroup updateTreatmentGroup(TreatmentGroup treatmentGroup){

		// logger.info("incoming meta treatmentGroup to update: " + treatmentGroup.toJson());

		TreatmentGroup updatedTreatmentGroup = TreatmentGroup.update(treatmentGroup);

		if (treatmentGroup.getLsLabels() != null) {
			for(TreatmentGroupLabel treatmentGroupLabel : treatmentGroup.getLsLabels()){
				if (treatmentGroupLabel.getId() == null){
					TreatmentGroupLabel newTreatmentGroupLabel = new TreatmentGroupLabel(treatmentGroupLabel);
					newTreatmentGroupLabel.setTreatmentGroup(updatedTreatmentGroup);
					newTreatmentGroupLabel.persist();
					treatmentGroupLabel.setId(newTreatmentGroupLabel.getId());
				} else {
					treatmentGroupLabel = TreatmentGroupLabel.update(treatmentGroupLabel);
				}
			}			
		} 

		if (treatmentGroup.getLsStates() != null){
			for(TreatmentGroupState treatmentGroupState : treatmentGroup.getLsStates()){
				if (treatmentGroupState.getId() == null){
					TreatmentGroupState newTreatmentGroupState = new TreatmentGroupState(treatmentGroupState);
					newTreatmentGroupState.setTreatmentGroup(updatedTreatmentGroup);
					if (newTreatmentGroupState.getRecordedDate() == null) {newTreatmentGroupState.setRecordedDate(new Date());}		
					if (newTreatmentGroupState.getRecordedBy() == null) { newTreatmentGroupState.setRecordedBy(updatedTreatmentGroup.getRecordedBy()); }
					newTreatmentGroupState.persist();
					treatmentGroupState.setId(newTreatmentGroupState.getId());
				} else {
					TreatmentGroupState updatedTreatmentGroupState = TreatmentGroupState.update(treatmentGroupState);
					// logger.debug(updatedTreatmentGroupState.toJson());
				}
				if (treatmentGroupState.getLsValues() != null){
					for(TreatmentGroupValue treatmentGroupValue : treatmentGroupState.getLsValues()){
						if (treatmentGroupValue.getId() == null){
							if (treatmentGroupValue.getRecordedDate() == null) {treatmentGroupValue.setRecordedDate(new Date());}
							treatmentGroupValue.setLsState(TreatmentGroupState.findTreatmentGroupState(treatmentGroupState.getId()));
							treatmentGroupValue.persist();
						} else {
							treatmentGroupValue.setLsState(TreatmentGroupState.findTreatmentGroupState(treatmentGroupState.getId()));
							TreatmentGroupValue updatedTreatmentGroupValue = TreatmentGroupValue.update(treatmentGroupValue);
							// logger.debug(updatedTreatmentGroupValue.toJson());
						}

					}				
				}
			}
		}

		if (treatmentGroup.getSubjects() != null){
			for(Subject subject : treatmentGroup.getSubjects()){
				if (subject.getId() == null){
					Subject newSubject = new Subject(subject);
					newSubject.getTreatmentGroups().add(treatmentGroup);
					newSubject.persist();							
				} else {
					subject = Subject.update(subject);
				}
			}
		}

		return TreatmentGroup.findTreatmentGroup(treatmentGroup.getId());

	}

	@Override
	public HashMap<String, TempThingDTO> createLsTreatmentGroupsFromCSV(
			HashMap<String, TempThingDTO> analysisGroupMap,
			String treatmentGroupCSV, String subjectCSV) throws IOException {

		HashMap<String, TempThingDTO> treatmentGroupMap = createTreatmentGroupsFromCSV(treatmentGroupCSV, analysisGroupMap);

		if (subjectCSV != null){

			HashMap<String, TempThingDTO> subjectGroupMap = subjectService.createSubjectsFromCSV(subjectCSV, treatmentGroupMap);

		}

		return analysisGroupMap;

	}

//	private HashMap<String, TempThingDTO> createTreatmentGroupsFromCSV(
//			HashMap<String, TempThingDTO> analysisGroupMap,
//			String treatmentGroupCSV) {
//
//
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	@Transactional

	public HashMap<String, TempThingDTO> createTreatmentGroupsFromCSV(
			String treatmentGroupFilePath, HashMap<String, TempThingDTO> analysisGroupMap) throws IOException {

		int batchSize = propertiesUtilService.getBatchSize();
		ICsvBeanReader beanReader = null;
		HashMap<String, TempThingDTO> treatmentGroupMap = new HashMap<String, TempThingDTO>();
		HashMap<String, TempThingDTO> treatmentStateMap = new HashMap<String, TempThingDTO>();
		HashMap<String, TempThingDTO> treatmentValueMap = new HashMap<String, TempThingDTO>();

		try {
			// logger.info("read csv delimited file");
			InputStream is = new FileInputStream(treatmentGroupFilePath);  
			InputStreamReader isr = new InputStreamReader(is);  
			BufferedReader br = new BufferedReader(isr);

			beanReader = new CsvBeanReader(br, CsvPreference.TAB_PREFERENCE);
			String[] headerText = beanReader.getHeader(true);

			List<String> headerList = new ArrayList<String>();
			int position = 0;
			for (String head : headerText){
				// logger.info("header column: " + position + "  " + head);
				headerList.add(head);
				position++;
			}

			// logger.info("size of header list  " + headerList.size());
			String[] header = new String[headerList.size()];
			headerList.toArray(header);

			for (String head : header){
				// logger.debug("header column array : " + position + "  " + head);
				position++;
			}

			final CellProcessor[] processors = FlatThingCsvDTO.getProcessors();

			FlatThingCsvDTO treatmentGroupDTO;
			TreatmentGroup treatmentGroup;
			TreatmentGroupState treatmentGroupState;
			TreatmentGroupValue treatmentGroupValue;

			long rowIndex = 1;
			Set<AnalysisGroup> analysisGroups = new HashSet<AnalysisGroup>();
			while( (treatmentGroupDTO = beanReader.read(FlatThingCsvDTO.class, header, processors)) != null ) {
				// logger.debug(String.format("lineNo=%s, rowNo=%s, bulkData=%s", beanReader.getLineNumber(), beanReader.getRowNumber(), treatmentGroupDTO));

				if (treatmentGroupDTO.getLsType() == null) treatmentGroupDTO.setLsType("default");
				if (treatmentGroupDTO.getLsKind() == null) treatmentGroupDTO.setLsKind("default");
				if (treatmentGroupDTO.getTempId() == null) treatmentGroupDTO.setTempId(treatmentGroupDTO.getId().toString());

				treatmentGroup = getOrCreateTreatmentGroup(treatmentGroupDTO, treatmentGroupMap, analysisGroupMap);
				if (treatmentGroup != null){
					if (treatmentGroup.getId() == null){
						treatmentGroup.persist();
					} else {
						treatmentGroup.merge();
					}
//					AnalysisGroup analysisGroup = AnalysisGroup.findAnalysisGroup(analysisGroupMap.get(treatmentGroupDTO.getTempParentId()).getId());
//					analysisGroup.getTreatmentGroups().add(treatmentGroup);
//					analysisGroups.add(analysisGroup);
					// logger.debug("saved the new treatment Group: ID: " + treatmentGroup.getId() + " codeName" + treatmentGroup.getCodeName());
					// logger.debug("saved the new treatment group: " + treatmentGroup.toJson());
					treatmentGroupMap = saveTempTreatmentGroup(treatmentGroup, treatmentGroupDTO, treatmentGroupMap);
				}

				if (treatmentGroupDTO.getStateType() != null && treatmentGroupDTO.getStateKind() != null){
					if (treatmentGroupDTO.getTempStateId() == null) treatmentGroupDTO.setTempStateId(treatmentGroupDTO.getStateId().toString());
					// logger.debug("treatmentGroupDTO.getTempStateId() is " + treatmentGroupDTO.getTempStateId());
					treatmentGroupState = getOrCreateTreatmentState(treatmentGroupDTO, treatmentStateMap, treatmentGroupMap);
					if (treatmentGroupState != null){
						treatmentGroupState.persist();
						// logger.debug("saved the new treatment group state: " + treatmentGroupState.getId());
						// logger.debug("saved the new treatment group state: " + treatmentGroupState.toJson());
						treatmentStateMap = saveTempTreatmentState(treatmentGroupState, treatmentGroupDTO, treatmentStateMap);
					}

					if (treatmentGroupDTO.getValueType() != null && treatmentGroupDTO.getValueKind() != null){
						if (treatmentGroupDTO.getTempValueId() == null) treatmentGroupDTO.setTempValueId(Long.toString(rowIndex));
						treatmentGroupValue = getOrCreateTreatmentValue(treatmentGroupDTO, treatmentValueMap, treatmentStateMap);
						if (treatmentGroupValue != null){
							treatmentGroupValue.persist();
							// logger.debug("saved the treatment Group Value: " + treatmentGroupValue.toJson());
							if ( rowIndex % batchSize == 0 ) {
								treatmentGroupValue.flush();
								treatmentGroupValue.clear();
							}
							treatmentValueMap = saveTempTreatmentValue(treatmentGroupValue, treatmentGroupDTO, treatmentValueMap);
						}
					}
				} else {
					// logger.debug("---------- not saving a new treatment group state: " + treatmentGroupDTO.getStateType());
				}

				rowIndex++;
			}
//			Long beforeMerge = new Date().getTime();
//			// logger.info("Number of AnalysisGroups to merge: "+ analysisGroups.size());
//			for (AnalysisGroup analysisGroup: analysisGroups) {
//				analysisGroup.merge();	
//			}
//			Long afterMerge = new Date().getTime();
//			Long mergeDuration = afterMerge - beforeMerge;
//			// logger.info("Merging AnalysisGroups took: "+ mergeDuration + " ms");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			beanReader.close();
		}

		return treatmentGroupMap;
	}

	private HashMap<String, TempThingDTO> saveTempTreatmentValue(
			TreatmentGroupValue treatmentGroupValue,
			FlatThingCsvDTO treatmentGroupDTO,
			HashMap<String, TempThingDTO> treatmentValueMap) {

		TempThingDTO tempThingDTO = new TempThingDTO();
		tempThingDTO.setId(treatmentGroupValue.getId());
		tempThingDTO.setTempId(treatmentGroupDTO.getTempValueId());
		treatmentValueMap.put(treatmentGroupDTO.getTempValueId(), tempThingDTO);

		return treatmentValueMap;	
	}

	private TreatmentGroupValue getOrCreateTreatmentValue(
			FlatThingCsvDTO treatmentGroupDTO,
			HashMap<String, TempThingDTO> treatmentValueMap,
			HashMap<String, TempThingDTO> treatmentStateMap) {

		TreatmentGroupValue treatmentGroupValue = null;
		if (!treatmentValueMap.containsKey(treatmentGroupDTO.getTempValueId())){
			if (treatmentGroupDTO.getValueId() == null){
				treatmentGroupValue = new TreatmentGroupValue(treatmentGroupDTO);
				// logger.debug("query state: " + treatmentGroupDTO.getTempStateId());
				Set<String> treatmentStateKeys = treatmentStateMap.keySet();
				for (String key : treatmentStateKeys){
					// logger.debug("treatmentStateKey: " + key);
				}
				treatmentGroupValue.setLsState(TreatmentGroupState.findTreatmentGroupState(treatmentStateMap.get(treatmentGroupDTO.getTempStateId()).getId()));				
			} else {
				treatmentGroupValue = TreatmentGroupValue.findTreatmentGroupValue(treatmentGroupDTO.getValueId());
			}
		} else {
			// logger.debug("skipping the saved treatmentGroupValue --------- " + treatmentGroupDTO.getValueId());
		}

		return treatmentGroupValue;
	}

	private HashMap<String, TempThingDTO> saveTempTreatmentState(
			TreatmentGroupState treatmentGroupState,
			FlatThingCsvDTO treatmentGroupDTO,
			HashMap<String, TempThingDTO> treatmentStateMap) {

		TempThingDTO tempThingDTO = new TempThingDTO();
		tempThingDTO.setId(treatmentGroupState.getId());
		tempThingDTO.setTempId(treatmentGroupDTO.getTempStateId());
		// logger.debug("saving the temp state: " + tempThingDTO.getTempId());
		treatmentStateMap.put(treatmentGroupDTO.getTempStateId(), tempThingDTO);

		return treatmentStateMap;		
	}

	private TreatmentGroupState getOrCreateTreatmentState(
			FlatThingCsvDTO treatmentGroupDTO,
			HashMap<String, TempThingDTO> treatmentStateMap,
			HashMap<String, TempThingDTO> treatmentGroupMap) {

		TreatmentGroupState treatmentGroupState = null;
		if (!treatmentStateMap.containsKey(treatmentGroupDTO.getTempStateId())){
			if (treatmentGroupDTO.getStateId() == null){
				treatmentGroupState = new TreatmentGroupState(treatmentGroupDTO);
				treatmentGroupState.setTreatmentGroup(TreatmentGroup.findTreatmentGroup(treatmentGroupMap.get(treatmentGroupDTO.getTempId()).getId()));				
			} else {
				treatmentGroupState = TreatmentGroupState.findTreatmentGroupState(treatmentGroupDTO.getStateId());
			}
		} else {
			// logger.debug("skipping the saved treatmentGroupState --------- " + treatmentGroupDTO.getStateId());
		}

		return treatmentGroupState;

	}

	private HashMap<String, TempThingDTO> saveTempTreatmentGroup(
			TreatmentGroup treatmentGroup, FlatThingCsvDTO treatmentGroupDTO,
			HashMap<String, TempThingDTO> treatmentGroupMap) {

		TempThingDTO tempThingDTO = new TempThingDTO();
		tempThingDTO.setId(treatmentGroup.getId());
		tempThingDTO.setCodeName(treatmentGroup.getCodeName());
		tempThingDTO.setTempId(treatmentGroupDTO.getTempId().toString());
		treatmentGroupMap.put(treatmentGroupDTO.getTempId(), tempThingDTO);
		return treatmentGroupMap;

	}

	private TreatmentGroup getOrCreateTreatmentGroup(
			FlatThingCsvDTO treatmentGroupDTO,
			HashMap<String, TempThingDTO> treatmentGroupMap,
			HashMap<String, TempThingDTO> analysisGroupMap) {

		TreatmentGroup treatmentGroup = null;
		if (!treatmentGroupMap.containsKey(treatmentGroupDTO.getTempId())){
			if (treatmentGroupDTO.getId() == null){
				if (treatmentGroupDTO.getTempParentId() != null && !treatmentGroupDTO.getTempParentId().equalsIgnoreCase("null")){
					treatmentGroup = new TreatmentGroup(treatmentGroupDTO);
					if (treatmentGroup.getCodeName() == null){
						String newCodeName = autoLabelService.getTreatmentGroupCodeName();
						// logger.debug("------------------ new codeName: " + newCodeName);
						treatmentGroup.setCodeName(newCodeName);
					}
				} else {
					// logger.debug("the temp parent ID is null" + treatmentGroupDTO.getTempParentId());
				}
			} else {
				treatmentGroup = TreatmentGroup.findTreatmentGroup(treatmentGroupDTO.getId());
			}
			Set<AnalysisGroup> analysisGroups = new HashSet<AnalysisGroup>();
			AnalysisGroup foundAnalysisGroup = AnalysisGroup.findAnalysisGroup(analysisGroupMap.get(treatmentGroupDTO.getTempParentId()).getId());
			analysisGroups.add(foundAnalysisGroup);
			if (treatmentGroup.getAnalysisGroups() == null){
				treatmentGroup.setAnalysisGroups(analysisGroups);
			} else {
				treatmentGroup.getAnalysisGroups().addAll(analysisGroups);
			}

		} else {
			// logger.debug("skipping the previously saved treatmentGroup --------- " + treatmentGroupDTO.getCodeName());
		}
		return treatmentGroup;
	}

}
