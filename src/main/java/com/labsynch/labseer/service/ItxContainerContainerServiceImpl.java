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

import com.labsynch.labseer.domain.ItxContainerContainer;
import com.labsynch.labseer.domain.ItxContainerContainerState;
import com.labsynch.labseer.domain.ItxContainerContainerValue;
import com.labsynch.labseer.utils.PropertiesUtilService;

@Service
@Transactional
public class ItxContainerContainerServiceImpl implements ItxContainerContainerService {

	private static final Logger logger = LoggerFactory.getLogger(ItxContainerContainerServiceImpl.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;
	
	@Override
	@Transactional
	public ItxContainerContainer saveLsItxContainer(ItxContainerContainer itxContainer){
		logger.debug("incoming meta itxContainerContainer: " + itxContainer.toJson() + "\n");
		int i = 0;
		int j = 0;
		int batchSize = propertiesUtilService.getBatchSize();
		ItxContainerContainer newItxContainer = new ItxContainerContainer(itxContainer);
		newItxContainer.persist();

		if (itxContainer.getLsStates() != null){
			for(ItxContainerContainerState itxState : itxContainer.getLsStates()){
				ItxContainerContainerState newItxState = new ItxContainerContainerState(itxState);
				newItxState.setItxContainerContainer(newItxContainer);
				newItxState.persist();
			    if ( j % batchSize == 0 ) { // same as the JDBC batch size
			    	newItxState.flush();
			    	newItxState.clear();
			    }
			    j++;
				if (itxState.getLsValues() != null){
					for(ItxContainerContainerValue itxValue : itxState.getLsValues()){
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
		
		return newItxContainer;
	}

	@Override
	@Transactional
	public Collection<ItxContainerContainer> saveLsItxContainers(String json){
		int i = 0;
		int j = 0;
		int batchSize = propertiesUtilService.getBatchSize();
        Collection<ItxContainerContainer> savedItxContainerContainers = new ArrayList<ItxContainerContainer>();

        StringReader sr = new StringReader(json);
		BufferedReader br = new BufferedReader(sr);
        for (ItxContainerContainer itxContainer : ItxContainerContainer.fromJsonArrayToItxContainerContainers(br)) {
    		ItxContainerContainer newItxContainer = new ItxContainerContainer(itxContainer);
    		newItxContainer.persist();
    		savedItxContainerContainers.add(newItxContainer);
    		if (itxContainer.getLsStates() != null){
    			for(ItxContainerContainerState itxState : itxContainer.getLsStates()){
    				ItxContainerContainerState newItxState = new ItxContainerContainerState(itxState);
    				newItxState.setItxContainerContainer(newItxContainer);
    				newItxState.persist();
    			    if ( j % batchSize == 0 ) { // same as the JDBC batch size
    			    	newItxState.flush();
    			    	newItxState.clear();
    			    }
    			    j++;
    				if (itxState.getLsValues() != null){
    					for(ItxContainerContainerValue itxValue : itxState.getLsValues()){
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
		
		return savedItxContainerContainers;
	}
}
