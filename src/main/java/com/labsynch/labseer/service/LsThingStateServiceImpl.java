package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.AnalysisGroupState;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.LsThingState;
import com.labsynch.labseer.domain.LsThingValue;
import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.domain.SubjectState;
import com.labsynch.labseer.domain.SubjectValue;
import com.labsynch.labseer.utils.PropertiesUtilService;

@Service
@Transactional
public class LsThingStateServiceImpl implements LsThingStateService {

	private static final Logger logger = LoggerFactory.getLogger(LsThingStateServiceImpl.class);
	
	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Override
	public Collection<LsThingState> ignoreAllLsThingStates(Collection<LsThingState> lsThingStates) {
		//mark lsThingStates and values as ignore 
		Collection<LsThingState> lsThingStateSet = new HashSet<LsThingState>();
		for (LsThingState queryLsThingState : lsThingStates){
			LsThingState lsThingState = LsThingState.findLsThingState(queryLsThingState.getId());			
				for(LsThingValue lsThingValue : LsThingValue.findLsThingValuesByLsState(lsThingState).getResultList()){
					lsThingValue.setIgnored(true);
					lsThingValue.merge();
				}
				lsThingState.setIgnored(true);
				lsThingState.merge();
				lsThingStateSet.add(LsThingState.findLsThingState(lsThingState.getId()));
		}

		return(lsThingStateSet);

	}

	@Override
	public List<LsThingState> getLsThingStatesByLsThingIdAndStateTypeKind(
			Long lsThingId, String stateType, String stateKind) {
		
			List<LsThingState> lsThingStates = LsThingState.findLsThingStatesByLsThingIDAndStateTypeKind(lsThingId, stateType, stateKind).getResultList();

			return lsThingStates;
	}

	@Override
	public LsThingState createLsThingStateByLsThingIdAndStateTypeKind(
			Long lsThingId, String stateType, String stateKind) {
		LsThingState lsThingState = new LsThingState();
		LsThing lsThing = LsThing.findLsThing(lsThingId);
		lsThingState.setLsThing(lsThing);
		lsThingState.setLsType(stateType);
		lsThingState.setLsKind(stateKind);
		lsThingState.setRecordedBy("default");
		lsThingState.persist();
		return lsThingState;
	}

	@Override
	public LsThingState saveLsThingState(
			LsThingState lsThingState) {
		lsThingState.setLsThing(LsThing.findLsThing(lsThingState.getLsThing().getId()));		
		lsThingState.persist();
		Set<LsThingValue> savedValues = new HashSet<LsThingValue>();
		for (LsThingValue lsThingValue : lsThingState.getLsValues()){
			lsThingValue.setLsState(lsThingState);
			lsThingValue.persist();
			savedValues.add(lsThingValue);
		}
		lsThingState.setLsValues(savedValues);
		lsThingState.merge();
		return lsThingState;
	}

	@Override
	public Collection<LsThingState> saveLsThingStates(
			Collection<LsThingState> lsThingStates) {
		for (LsThingState lsThingState: lsThingStates) {
			lsThingState = saveLsThingState(lsThingState);
		}
		return lsThingStates;
	}
	
	@Override
	public LsThingState updateLsThingState(
			LsThingState lsThingState) {
		lsThingState.setVersion(LsThingState.findLsThingState(lsThingState.getId()).getVersion());
		lsThingState.merge();
		return lsThingState;
	}

	@Override
	public Collection<LsThingState> updateLsThingStates(
			Collection<LsThingState> lsThingStates) {
		for (LsThingState lsThingState : lsThingStates){
			lsThingState = updateLsThingState(lsThingState);
		}
		return null;
	}
}
