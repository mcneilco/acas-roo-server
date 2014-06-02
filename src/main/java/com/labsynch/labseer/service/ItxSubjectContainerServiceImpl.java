package com.labsynch.labseer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.ItxSubjectContainer;
import com.labsynch.labseer.domain.ItxSubjectContainerState;
import com.labsynch.labseer.domain.ItxSubjectContainerValue;
import com.labsynch.labseer.utils.PropertiesUtilService;

@Service
@Transactional
public class ItxSubjectContainerServiceImpl implements ItxSubjectContainerService {

	private static final Logger logger = LoggerFactory.getLogger(ItxSubjectContainerServiceImpl.class);
	
	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Override
	public ItxSubjectContainer saveLsItxSubjectContainer(ItxSubjectContainer itxSubjectContainer){
		int i = 0;
		int j = 0;
		int batchSize = propertiesUtilService.getBatchSize();
		logger.debug("incoming meta ItxSubjectContainer: " + itxSubjectContainer.toJson() + "\n");
		ItxSubjectContainer newItxSubjContainer = new ItxSubjectContainer(itxSubjectContainer);
		newItxSubjContainer.persist();

		if (itxSubjectContainer.getLsStates() != null){
			for(ItxSubjectContainerState itxState : itxSubjectContainer.getLsStates()){
				ItxSubjectContainerState newItxState = new ItxSubjectContainerState(itxState);
				newItxState.setItxSubjectContainer(newItxSubjContainer);
				newItxState.persist();
			    if ( j % batchSize == 0 ) { // same as the JDBC batch size
			    	newItxState.flush();
			    	newItxState.clear();
			    }
			    j++;
				if (itxState.getLsValues() != null){
					for(ItxSubjectContainerValue itxValue : itxState.getLsValues()){
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
		
		return ItxSubjectContainer.findItxSubjectContainer(newItxSubjContainer.getId());
	}

}
