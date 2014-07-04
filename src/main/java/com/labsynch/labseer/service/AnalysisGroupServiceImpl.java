package com.labsynch.labseer.service;


import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.AnalysisGroupLabel;
import com.labsynch.labseer.domain.AnalysisGroupState;
import com.labsynch.labseer.domain.AnalysisGroupValue;
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.TreatmentGroup;
import com.labsynch.labseer.utils.PropertiesUtilService;

@Service
public class AnalysisGroupServiceImpl implements AnalysisGroupService {

	private static final Logger logger = LoggerFactory.getLogger(AnalysisGroupServiceImpl.class);

	@Autowired
	private AutoLabelService autoLabelService;

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Autowired
	private TreatmentGroupService treatmentGroupService;

	@Transactional
	@Override
	public AnalysisGroup saveLsAnalysisGroup(AnalysisGroup analysisGroup){

		logger.debug("incoming meta analysisGroup: " + analysisGroup.toJson());
		Date recordedDate = new Date();

		AnalysisGroup newAnalysisGroup = null;
		if (analysisGroup.getId() == null){
			newAnalysisGroup = new AnalysisGroup(analysisGroup);
			//			logger.debug("incoming experiment array: " + Experiment.toJsonArray(newAnalysisGroup.getExperiments()));
			//			Set<Experiment> experimentSet = new HashSet<Experiment>();
			//			for (Experiment experiment : newAnalysisGroup.getExperiments()){
			//				experimentSet.add(Experiment.findExperiment(experiment.getId()));
			//			}
			//			newAnalysisGroup.setExperiments(experimentSet);		

			if (newAnalysisGroup.getCodeName() == null) { newAnalysisGroup.setCodeName(autoLabelService.getAnalysisGroupCodeName());}
			if (newAnalysisGroup.getRecordedDate() == null) { newAnalysisGroup.setRecordedDate(recordedDate);}
			if (newAnalysisGroup.getRecordedBy() == null) { 
				for (Experiment experiment : analysisGroup.getExperiments()){
					newAnalysisGroup.setRecordedBy(experiment.getRecordedBy()); 
				}
			}
			newAnalysisGroup.persist();
			logger.debug("persisted the newAnalysisGroup: " + newAnalysisGroup.toJson());
			
			this.saveLabels(analysisGroup, newAnalysisGroup, recordedDate);
			this.saveStates(analysisGroup, newAnalysisGroup, recordedDate);
			treatmentGroupService.saveLsTreatmentGroups( newAnalysisGroup,  analysisGroup.getTreatmentGroups(), recordedDate);

		} else {
			newAnalysisGroup = AnalysisGroup.update(analysisGroup);
			newAnalysisGroup.merge();
			
			treatmentGroupService.saveLsTreatmentGroups( newAnalysisGroup,  analysisGroup.getTreatmentGroups(), recordedDate);

		}

		return newAnalysisGroup;

		//		return AnalysisGroup.findAnalysisGroup(newAnalysisGroup.getId());
	}

//	private void saveTreatmentGroups(AnalysisGroup analysisGroup, AnalysisGroup newAnalysisGroup, Date recordedDate) {
//		if (analysisGroup.getTreatmentGroups() != null){
//			logger.debug("number of treatmentGroups " + analysisGroup.getTreatmentGroups().size());
//			int i = 0;
//			for(TreatmentGroup treatmentGroup : analysisGroup.getTreatmentGroups()){
//				treatmentGroupService.saveLsTreatmentGroup(treatmentGroup, recordedDate);
//				if (treatmentGroup.getId() == null){
//					TreatmentGroup newTreatmentGroup = new TreatmentGroup(treatmentGroup);
//					newTreatmentGroup.getAnalysisGroups().add(newAnalysisGroup);
//					newTreatmentGroup.persist();
//					saveTreatmentGroupLabels(newAnalysisGroup, newAnalysisGroup, recordedDate);
//					if (treatmentGroup.getLsLabels() != null) {
//						for(TreatmentGroupLabel treatmentGroupLabel : treatmentGroup.getLsLabels()){
//							TreatmentGroupLabel newTreatmentGroupLabel = new TreatmentGroupLabel(treatmentGroupLabel);
//							newTreatmentGroupLabel.setTreatmentGroup(newTreatmentGroup);
//							newTreatmentGroupLabel.persist();	
//						}		
//					}
//					if (treatmentGroup.getLsStates() != null){
//						for(TreatmentGroupState treatmentGroupState : treatmentGroup.getLsStates()){
//							TreatmentGroupState newTreatmentGroupState = new TreatmentGroupState(treatmentGroupState);
//							newTreatmentGroupState.setTreatmentGroup(newTreatmentGroup);
//							newTreatmentGroupState.persist();
//							if (treatmentGroupState.getLsValues() != null){
//								for(TreatmentGroupValue treatmentGroupValue : treatmentGroupState.getLsValues()){
//									treatmentGroupValue.setLsState(newTreatmentGroupState);
//									treatmentGroupValue.persist();
//									logger.debug("persisted the treatmentGroupValue: " + treatmentGroupValue.toJson());
//								}										
//							}
//						}					
//					}
//					if ( i % batchSize == 0 ) { //50, same as the JDBC batch size
//						newTreatmentGroup.flush();
//						newTreatmentGroup.clear();
//					}
//					i++;
//
//				} else {
//
//				}
//
//
//				if (treatmentGroup.getSubjects() != null){
//					int j = 0;
//					for(Subject subject : treatmentGroup.getSubjects()){
//						Subject newSubject = new Subject(subject);
//						TreatmentGroup newTreatmentGroup = null;
//						//						Set<TreatmentGroup> treatmentGroups = new HashSet<TreatmentGroup>();
//						//						treatmentGroups.add(newTreatmentGroup);
//						//						newSubject.setTreatmentGroups(treatmentGroups);
//						newSubject.getTreatmentGroups().add(TreatmentGroup.findTreatmentGroup(newTreatmentGroup.getId()));
//						newSubject.persist();
//						if (subject.getLsLabels() != null) {
//							for(SubjectLabel subjectLabel : subject.getLsLabels()){
//								SubjectLabel newSubjectLabel = new SubjectLabel(subjectLabel);
//								newSubjectLabel.setSubject(newSubject);
//								newSubjectLabel.persist();	
//							}		
//						}
//						if (subject.getLsStates() != null){
//							for(SubjectState subjectState : subject.getLsStates()){
//								SubjectState newSubjectState = new SubjectState(subjectState);
//								newSubjectState.setSubject(newSubject);
//								newSubjectState.persist();
//								if (subjectState.getLsValues() != null){
//									for (SubjectValue subjectValue : subjectState.getLsValues()){
//										subjectValue.setLsState(newSubjectState);
//										subjectValue.persist();
//									}								
//								}
//							}
//						}
//						if ( j % batchSize == 0 ) { 
//							newSubject.flush();
//							newSubject.clear();
//						}
//						j++;
//					}
//				}
//			}
//		}		
//	}

	private void saveStates(AnalysisGroup analysisGroup, AnalysisGroup newAnalysisGroup, Date recordedDate) {
		if (analysisGroup.getLsStates() != null){
			int i = 0;
			for(AnalysisGroupState analysisGroupState : analysisGroup.getLsStates()){
				AnalysisGroupState newAnalysisGroupState = new AnalysisGroupState(analysisGroupState);
				newAnalysisGroupState.setAnalysisGroup(newAnalysisGroup);
				if (newAnalysisGroupState.getRecordedDate() == null) {newAnalysisGroupState.setRecordedDate(recordedDate);}		
				if (newAnalysisGroupState.getRecordedBy() == null) { newAnalysisGroupState.setRecordedBy(newAnalysisGroupState.getAnalysisGroup().getRecordedBy()); }
				logger.debug("here is the ag state: " + newAnalysisGroupState.toJson());
				newAnalysisGroupState.persist();
				logger.debug("persisted the newAnalysisGroupState: " + newAnalysisGroupState.toJson());
				if (analysisGroupState.getLsValues() != null){
					for(AnalysisGroupValue analysisGroupValue : analysisGroupState.getLsValues()){
						if (analysisGroupValue.getRecordedDate() == null) {analysisGroupValue.setRecordedDate(recordedDate);}
						analysisGroupValue.setLsState(newAnalysisGroupState);
						analysisGroupValue.persist();
						logger.debug("persisted the analysisGroupValue: " + analysisGroupValue.toJson());
					}				
				}
				if ( i % propertiesUtilService.getBatchSize() == 0 ) { //50, same as the JDBC batch size
					newAnalysisGroupState.flush();
					newAnalysisGroupState.clear();
				}
				i++;
			}		
		}		
	}

	private void saveLabels(AnalysisGroup analysisGroup, AnalysisGroup newAnalysisGroup, Date recordedDate) {
		if (analysisGroup.getLsLabels() != null) {
			int i = 0;
			for(AnalysisGroupLabel analysisGroupLabel : analysisGroup.getLsLabels()){
				AnalysisGroupLabel newAnalysisGroupLabel = new AnalysisGroupLabel(analysisGroupLabel);
				newAnalysisGroupLabel.setAnalysisGroup(newAnalysisGroup);
				newAnalysisGroupLabel.persist();
				if ( i % propertiesUtilService.getBatchSize() == 0 ) { // same as the JDBC batch size
					newAnalysisGroupLabel.flush();
					newAnalysisGroupLabel.clear();
				}
				i++;
			}			
		} 		
	}

	@Override
	@Transactional
	public AnalysisGroup updateLsAnalysisGroup(AnalysisGroup analysisGroup){

		logger.debug("incoming meta analysisGroup to update: " + analysisGroup.toJson());
		AnalysisGroup updatedAnalysisGroup = AnalysisGroup.update(analysisGroup);

		if (analysisGroup.getLsLabels() != null) {
			for(AnalysisGroupLabel analysisGroupLabel : analysisGroup.getLsLabels()){
				if (analysisGroupLabel.getId() == null){
					AnalysisGroupLabel newAnalysisGroupLabel = new AnalysisGroupLabel(analysisGroupLabel);
					newAnalysisGroupLabel.setAnalysisGroup(updatedAnalysisGroup);
					newAnalysisGroupLabel.persist();
				} else {
					AnalysisGroupLabel updatedAnalysisGroupLabel = AnalysisGroupLabel.update(analysisGroupLabel);
				}
			}			
		} 


		if (analysisGroup.getLsStates() != null){
			for(AnalysisGroupState analysisGroupState : analysisGroup.getLsStates()){
				if (analysisGroupState.getId() == null){
					AnalysisGroupState newAnalysisGroupState = new AnalysisGroupState(analysisGroupState);
					newAnalysisGroupState.setAnalysisGroup(updatedAnalysisGroup);
					if (newAnalysisGroupState.getRecordedDate() == null) {newAnalysisGroupState.setRecordedDate(new Date());}		
					if (newAnalysisGroupState.getRecordedBy() == null) { newAnalysisGroupState.setRecordedBy(updatedAnalysisGroup.getRecordedBy()); }
					newAnalysisGroupState.persist();
					logger.debug("persisted the newAnalysisGroupState: " + newAnalysisGroupState.toJson());					
				} else {
					AnalysisGroupState updatedAnalysisGroupState = AnalysisGroupState.update(analysisGroupState);
				}
				if (analysisGroupState.getLsValues() != null){
					for(AnalysisGroupValue analysisGroupValue : analysisGroupState.getLsValues()){
						if (analysisGroupValue.getId() == null){
							if (analysisGroupValue.getRecordedDate() == null) {analysisGroupValue.setRecordedDate(new Date());}
							analysisGroupValue.setLsState(AnalysisGroupState.findAnalysisGroupState(analysisGroupState.getId()));
							analysisGroupValue.persist();
							logger.debug("persisted the analysisGroupValue: " + analysisGroupValue.toJson());							
						} else {
							AnalysisGroupValue updatedAnalysisGroupValue = AnalysisGroupValue.update(analysisGroupValue);
						}

					}				
				}
			}		
		}

		if (analysisGroup.getTreatmentGroups() != null){
			logger.debug("number of treatmentGroups " + analysisGroup.getTreatmentGroups().size());
			for(TreatmentGroup treatmentGroup : analysisGroup.getTreatmentGroups()){
				treatmentGroupService.updateTreatmentGroup(treatmentGroup);
			}

		}

		return AnalysisGroup.findAnalysisGroup(updatedAnalysisGroup.getId());
	}

}
