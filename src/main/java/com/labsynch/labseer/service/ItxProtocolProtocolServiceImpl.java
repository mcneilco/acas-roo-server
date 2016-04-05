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

import com.labsynch.labseer.domain.ItxProtocolProtocol;
import com.labsynch.labseer.domain.ItxProtocolProtocolState;
import com.labsynch.labseer.domain.ItxProtocolProtocolValue;
import com.labsynch.labseer.utils.PropertiesUtilService;

@Service
@Transactional
public class ItxProtocolProtocolServiceImpl implements ItxProtocolProtocolService {

	private static final Logger logger = LoggerFactory.getLogger(ItxProtocolProtocolServiceImpl.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;
	
	@Override
	@Transactional
	public ItxProtocolProtocol saveLsItxProtocol(ItxProtocolProtocol itxProtocol){
		logger.debug("incoming meta itxProtocolProtocol: " + itxProtocol.toJson() + "\n");
		int i = 0;
		int j = 0;
		int batchSize = propertiesUtilService.getBatchSize();
		ItxProtocolProtocol newItxProtocol = new ItxProtocolProtocol(itxProtocol);
		newItxProtocol.persist();

		if (itxProtocol.getLsStates() != null){
			for(ItxProtocolProtocolState itxState : itxProtocol.getLsStates()){
				ItxProtocolProtocolState newItxState = new ItxProtocolProtocolState(itxState);
				newItxState.setItxProtocolProtocol(newItxProtocol);
				newItxState.persist();
			    if ( j % batchSize == 0 ) { // same as the JDBC batch size
			    	newItxState.flush();
			    	newItxState.clear();
			    }
			    j++;
				if (itxState.getLsValues() != null){
					for(ItxProtocolProtocolValue itxValue : itxState.getLsValues()){
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
		
		return newItxProtocol;
	}

	@Override
	@Transactional
	public Collection<ItxProtocolProtocol> saveLsItxProtocols(String json){
		int i = 0;
		int j = 0;
		int batchSize = propertiesUtilService.getBatchSize();
        Collection<ItxProtocolProtocol> savedItxProtocolProtocols = new ArrayList<ItxProtocolProtocol>();

        StringReader sr = new StringReader(json);
		BufferedReader br = new BufferedReader(sr);
        for (ItxProtocolProtocol itxProtocol : ItxProtocolProtocol.fromJsonArrayToItxProtocolProtocols(br)) {
    		ItxProtocolProtocol newItxProtocol = new ItxProtocolProtocol(itxProtocol);
    		newItxProtocol.persist();
    		savedItxProtocolProtocols.add(newItxProtocol);
    		if (itxProtocol.getLsStates() != null){
    			for(ItxProtocolProtocolState itxState : itxProtocol.getLsStates()){
    				ItxProtocolProtocolState newItxState = new ItxProtocolProtocolState(itxState);
    				newItxState.setItxProtocolProtocol(newItxProtocol);
    				newItxState.persist();
    			    if ( j % batchSize == 0 ) { // same as the JDBC batch size
    			    	newItxState.flush();
    			    	newItxState.clear();
    			    }
    			    j++;
    				if (itxState.getLsValues() != null){
    					for(ItxProtocolProtocolValue itxValue : itxState.getLsValues()){
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
		
		return savedItxProtocolProtocols;
	}

	@Override
	public Collection<ItxProtocolProtocol> saveLsItxProtocols(
			Collection<ItxProtocolProtocol> itxProtocolProtocols) {
		Collection<ItxProtocolProtocol> savedItxProtocolProtocols = new ArrayList<ItxProtocolProtocol>();
		for (ItxProtocolProtocol itxProtocolProtocol : itxProtocolProtocols){
			savedItxProtocolProtocols.add(saveLsItxProtocol(itxProtocolProtocol));
		}
		return savedItxProtocolProtocols;
	}
}
