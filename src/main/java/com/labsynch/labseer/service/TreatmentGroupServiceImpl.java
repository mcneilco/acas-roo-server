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
@Transactional
public class TreatmentGroupServiceImpl implements TreatmentGroupService {

	private static final Logger logger = LoggerFactory.getLogger(TreatmentGroupServiceImpl.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;
	
	@Autowired
	private SubjectService subjectService;

	@Override
	@Transactional
	public TreatmentGroup saveLsTreatmentGroup(TreatmentGroup treatmentGroup){

		logger.debug("incoming meta treatmentGroup: " + treatmentGroup.toJson());
		int batchSize = propertiesUtilService.getBatchSize();
		Date recordedDate = new Date();
		TreatmentGroup newTreatmentGroup = new TreatmentGroup(treatmentGroup);
		Set<AnalysisGroup> analysisGroups = treatmentGroup.getAnalysisGroups();
		Set<AnalysisGroup> currentAnalysisGroups = new HashSet<AnalysisGroup>();
		for (AnalysisGroup analysisGroup : analysisGroups){
			currentAnalysisGroups.add(AnalysisGroup.findAnalysisGroup(analysisGroup.getId()));
		}
		newTreatmentGroup.setAnalysisGroups(currentAnalysisGroups);
		newTreatmentGroup.persist();
		logger.debug("persisted the newTreatmentGroup: " + newTreatmentGroup.toJson());

		if (treatmentGroup.getLsLabels() != null) {
			for(TreatmentGroupLabel treatmentGroupLabel : treatmentGroup.getLsLabels()){
				TreatmentGroupLabel newTreatmentGroupLabel = new TreatmentGroupLabel(treatmentGroupLabel);
				newTreatmentGroupLabel.setTreatmentGroup(newTreatmentGroup);
				if (newTreatmentGroupLabel.getRecordedDate() == null) {newTreatmentGroupLabel.setRecordedDate(recordedDate);}
				newTreatmentGroupLabel.persist();	
			}		
		}
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

		if (treatmentGroup.getSubjects() != null){
			int j = 0;
			for(Subject subject : treatmentGroup.getSubjects()){
				Subject newSubject = subjectService.saveSubject(subject);
				if ( j % batchSize == 0 ) { 
					newSubject.flush();
					newSubject.clear();
				}
				j++;
			}
		}


		return TreatmentGroup.findTreatmentGroup(newTreatmentGroup.getId());
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
				}
				if (treatmentGroupState.getLsValues() != null){
					for(TreatmentGroupValue treatmentGroupValue : treatmentGroupState.getLsValues()){
						if (treatmentGroupValue.getId() == null){
							if (treatmentGroupValue.getRecordedDate() == null) {treatmentGroupValue.setRecordedDate(new Date());}
							treatmentGroupValue.setLsState(TreatmentGroupState.findTreatmentGroupState(treatmentGroupState.getId()));
							treatmentGroupValue.persist();
						} else {
							TreatmentGroupValue updatedTreatmentGroupValue = TreatmentGroupValue.update(treatmentGroupValue);
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
