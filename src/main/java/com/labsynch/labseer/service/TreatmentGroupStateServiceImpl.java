package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.AnalysisGroupState;
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ExperimentState;
import com.labsynch.labseer.domain.TreatmentGroup;
import com.labsynch.labseer.domain.TreatmentGroupState;
import com.labsynch.labseer.domain.TreatmentGroupValue;
import com.labsynch.labseer.utils.PropertiesUtilService;

@Service
@Transactional
public class TreatmentGroupStateServiceImpl implements TreatmentGroupStateService {

	private static final Logger logger = LoggerFactory.getLogger(TreatmentGroupStateServiceImpl.class);
	
	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Override
	public List<TreatmentGroupState> getTreatmentGroupStatesByTreatmentGroupIdAndStateTypeKind(
			Long treatmentGroupId, String stateType, String stateKind) {
		
			List<TreatmentGroupState> treatmentGroupStates = TreatmentGroupState.findTreatmentGroupStatesByTreatmentGroupIDAndStateTypeKind(treatmentGroupId, stateType, stateKind).getResultList();

			return treatmentGroupStates;
		
	}
	
	@Override
	public Collection<TreatmentGroupState> ignoreAllTreatmentGroupStates(Collection<TreatmentGroupState> treatmentGroupStates) {
		//mark treatmentGroupStates and values as ignore 
		Collection<TreatmentGroupState> treatmentGroupStateSet = new HashSet<TreatmentGroupState>();
		for (TreatmentGroupState queryTreatmentGroupState : treatmentGroupStates){
			TreatmentGroupState treatmentGroupState = TreatmentGroupState.findTreatmentGroupState(queryTreatmentGroupState.getId());			
				for(TreatmentGroupValue treatmentGroupValue : TreatmentGroupValue.findTreatmentGroupValuesByLsState(treatmentGroupState).getResultList()){
					treatmentGroupValue.setIgnored(true);
					treatmentGroupValue.merge();
				}
				treatmentGroupState.setIgnored(true);
				treatmentGroupState.merge();
				treatmentGroupStateSet.add(TreatmentGroupState.findTreatmentGroupState(treatmentGroupState.getId()));
		}

		return(treatmentGroupStateSet);

	}

	@Override
	public TreatmentGroupState createTreatmentGroupStateByTreatmentGroupIdAndStateTypeKind(Long treatmentGroupId, String stateType, String stateKind) {
		TreatmentGroupState treatmentGroupState = new TreatmentGroupState();
		TreatmentGroup treatmentGroup = TreatmentGroup.findTreatmentGroup(treatmentGroupId);
		treatmentGroupState.setTreatmentGroup(treatmentGroup);
		treatmentGroupState.setLsType(stateType);
		treatmentGroupState.setLsKind(stateKind);
		treatmentGroupState.setRecordedBy("default");
		treatmentGroupState.persist();
		return treatmentGroupState;
	}

	@Override
	public TreatmentGroupState saveTreatmentGroupState(
			TreatmentGroupState treatmentGroupState) {
		treatmentGroupState.setTreatmentGroup(TreatmentGroup.findTreatmentGroup(treatmentGroupState.getTreatmentGroup().getId()));		
		treatmentGroupState.persist();
		return treatmentGroupState;
	}

	@Override
	public Collection<TreatmentGroupState> saveTreatmentGroupStates(
			Collection<TreatmentGroupState> treatmentGroupStates) {
		for (TreatmentGroupState treatmentGroupState: treatmentGroupStates) {
			treatmentGroupState = saveTreatmentGroupState(treatmentGroupState);
		}
		return treatmentGroupStates;
	}

}
