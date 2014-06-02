package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.AnalysisGroupState;
import com.labsynch.labseer.domain.AnalysisGroupValue;
import com.labsynch.labseer.utils.PropertiesUtilService;

@Service
@Transactional
public class AnalysisGroupStateServiceImpl implements AnalysisGroupStateService {

	private static final Logger logger = LoggerFactory.getLogger(AnalysisGroupStateServiceImpl.class);
	
	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Override
	public Collection<AnalysisGroupState> ignoreAllAnalysisGroupStates(Collection<AnalysisGroupState> analysisGroupStates) {
		//mark AnalysisGroupStates and values as ignore 
		Collection<AnalysisGroupState> analysisGroupStateSet = new HashSet<AnalysisGroupState>();
		for (AnalysisGroupState queryAnalysisGroupState : analysisGroupStates){
			AnalysisGroupState analysisGroupState = AnalysisGroupState.findAnalysisGroupState(queryAnalysisGroupState.getId());			
				for(AnalysisGroupValue analysisGroupValue : AnalysisGroupValue.findAnalysisGroupValuesByLsState(analysisGroupState).getResultList()){
					analysisGroupValue.setIgnored(true);
					analysisGroupValue.merge();
				}
				analysisGroupState.setIgnored(true);
				analysisGroupState.merge();
				analysisGroupStateSet.add(AnalysisGroupState.findAnalysisGroupState(analysisGroupState.getId()));
		}

		return(analysisGroupStateSet);

	}


}
