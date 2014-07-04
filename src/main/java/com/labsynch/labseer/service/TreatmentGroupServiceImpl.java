package com.labsynch.labseer.service;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.domain.TreatmentGroup;
import com.labsynch.labseer.domain.TreatmentGroupLabel;
import com.labsynch.labseer.domain.TreatmentGroupState;
import com.labsynch.labseer.domain.TreatmentGroupValue;
import com.labsynch.labseer.utils.PropertiesUtilService;

@Service
public class TreatmentGroupServiceImpl implements TreatmentGroupService {

	private static final Logger logger = LoggerFactory.getLogger(TreatmentGroupServiceImpl.class);

	@Autowired
	private AutoLabelService autoLabelService;

	@Autowired
	private PropertiesUtilService propertiesUtilService;

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

		logger.debug("incoming meta treatmentGroup: " + treatmentGroup.toJson());
		Date recordedDate = new Date();		
		return saveLsTreatmentGroup(treatmentGroup.getAnalysisGroups(), treatmentGroup, recordedDate);

	}

	@Override
	@Transactional
	public TreatmentGroup saveLsTreatmentGroup(Set<AnalysisGroup> analysisGroups, TreatmentGroup treatmentGroup, Date recordedDate){

		//logger.debug("incoming meta treatmentGroup: " + treatmentGroup.toJson());
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
			logger.debug("persisted the new treatmentGroup: " + newTreatmentGroup.toJson());

			saveLabels(treatmentGroup, newTreatmentGroup, recordedDate);
			saveStates(treatmentGroup, newTreatmentGroup, recordedDate);
			
			logger.debug("look at subjects: ------------------ " + Subject.toJsonArray(treatmentGroup.getSubjects()));
			saveSubjects(newTreatmentGroup, treatmentGroup.getSubjects(), recordedDate);


		} else {
			newTreatmentGroup = TreatmentGroup.findTreatmentGroup(treatmentGroup.getId());
			for (AnalysisGroup analysisGroup : analysisGroups){
				newTreatmentGroup.getAnalysisGroups().add(AnalysisGroup.findAnalysisGroup(analysisGroup.getId()));				
			}
			newTreatmentGroup.merge();
			logger.debug("updated the treatmentGroup: -------------- " + newTreatmentGroup.toJson());
			if (treatmentGroup.getSubjects() != null){
				logger.debug("look at subjects: ------------------ " + Subject.toJsonArray(newTreatmentGroup.getSubjects()));
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

		logger.info("incoming meta treatmentGroup to update: " + treatmentGroup.toJson());

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
					logger.debug(updatedTreatmentGroupState.toJson());
				}
				if (treatmentGroupState.getLsValues() != null){
					for(TreatmentGroupValue treatmentGroupValue : treatmentGroupState.getLsValues()){
						if (treatmentGroupValue.getId() == null){
							if (treatmentGroupValue.getRecordedDate() == null) {treatmentGroupValue.setRecordedDate(new Date());}
							treatmentGroupValue.setLsState(TreatmentGroupState.findTreatmentGroupState(treatmentGroupState.getId()));
							treatmentGroupValue.persist();
						} else {
							TreatmentGroupValue updatedTreatmentGroupValue = TreatmentGroupValue.update(treatmentGroupValue);
							logger.debug(updatedTreatmentGroupValue.toJson());
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



}
