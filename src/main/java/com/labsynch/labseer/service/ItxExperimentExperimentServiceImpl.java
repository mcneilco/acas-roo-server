package com.labsynch.labseer.service;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.ItxExperimentExperiment;
import com.labsynch.labseer.domain.ItxExperimentExperimentState;
import com.labsynch.labseer.domain.ItxExperimentExperimentValue;
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.utils.PropertiesUtilService;

@Service
@Transactional
public class ItxExperimentExperimentServiceImpl implements ItxExperimentExperimentService {

	private static final Logger logger = LoggerFactory.getLogger(ItxExperimentExperimentServiceImpl.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;
	
	@Override
	@Transactional
	public ItxExperimentExperiment saveLsItxExperiment(ItxExperimentExperiment itxExperiment) throws Exception{
		logger.debug("incoming meta itxExperimentExperiment: " + itxExperiment.toJson() + "\n");
		try{
			itxExperiment.setFirstExperiment(Experiment.findExperiment(itxExperiment.getFirstExperiment().getId()));
			itxExperiment.setSecondExperiment(Experiment.findExperiment(itxExperiment.getSecondExperiment().getId()));
		}catch (Exception e){
			throw new Exception("One or both of the provided experiment do not exist");
		}
		int i = 0;
		int j = 0;
		int batchSize = propertiesUtilService.getBatchSize();
		ItxExperimentExperiment newItxExperiment = new ItxExperimentExperiment(itxExperiment);
		newItxExperiment.persist();

		if (itxExperiment.getLsStates() != null){
			for(ItxExperimentExperimentState itxState : itxExperiment.getLsStates()){
				ItxExperimentExperimentState newItxState = new ItxExperimentExperimentState(itxState);
				newItxState.setItxExperimentExperiment(newItxExperiment);
				newItxState.persist();
			    if ( j % batchSize == 0 ) { // same as the JDBC batch size
			    	newItxState.flush();
			    	newItxState.clear();
			    }
			    j++;
				if (itxState.getLsValues() != null){
					for(ItxExperimentExperimentValue itxValue : itxState.getLsValues()){
						itxValue.setLsState(newItxState);
						itxValue.persist();
					    if ( i % batchSize == 0 ) { // same as the JDBC batch size
					    	itxValue.flush();
					    	itxValue.clear();
					    }
					    i++;
					}				
				} 
			}
		}
		
		return newItxExperiment;
	}

	@Override
	@Transactional
	public Collection<ItxExperimentExperiment> saveLsItxExperiments(String json){
		int i = 0;
		int j = 0;
		int batchSize = propertiesUtilService.getBatchSize();
        Collection<ItxExperimentExperiment> savedItxExperimentExperiments = new ArrayList<ItxExperimentExperiment>();

        StringReader sr = new StringReader(json);
		BufferedReader br = new BufferedReader(sr);
        for (ItxExperimentExperiment itxExperiment : ItxExperimentExperiment.fromJsonArrayToItxExperimentExperiments(br)) {
    		ItxExperimentExperiment newItxExperiment = new ItxExperimentExperiment(itxExperiment);
    		newItxExperiment.persist();
    		savedItxExperimentExperiments.add(newItxExperiment);
    		if (itxExperiment.getLsStates() != null){
    			for(ItxExperimentExperimentState itxState : itxExperiment.getLsStates()){
    				ItxExperimentExperimentState newItxState = new ItxExperimentExperimentState(itxState);
    				newItxState.setItxExperimentExperiment(newItxExperiment);
    				newItxState.persist();
    			    if ( j % batchSize == 0 ) { // same as the JDBC batch size
    			    	newItxState.flush();
    			    	newItxState.clear();
    			    }
    			    j++;
    				if (itxState.getLsValues() != null){
    					for(ItxExperimentExperimentValue itxValue : itxState.getLsValues()){
    						itxValue.setLsState(newItxState);
    						itxValue.persist();
    					    if ( i % batchSize == 0 ) {
    					    	itxValue.flush();
    					    	itxValue.clear();
    					    }
    					    i++;
    					}				
    				} 
    			}
    		}
        }
		
		return savedItxExperimentExperiments;
	}

	@Override
	public Collection<ItxExperimentExperiment> saveLsItxExperiments(
			Collection<ItxExperimentExperiment> itxExperimentExperiments) throws Exception {
		Collection<ItxExperimentExperiment> savedItxExperimentExperiments = new ArrayList<ItxExperimentExperiment>();
		for (ItxExperimentExperiment itxExperimentExperiment : itxExperimentExperiments){
			savedItxExperimentExperiments.add(saveLsItxExperiment(itxExperimentExperiment));
		}
		return savedItxExperimentExperiments;
	}
}
