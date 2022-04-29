package com.labsynch.labseer.service;

import com.labsynch.labseer.domain.Container;
import com.labsynch.labseer.domain.ItxSubjectContainer;
import com.labsynch.labseer.domain.ItxSubjectContainerState;
import com.labsynch.labseer.domain.ItxSubjectContainerValue;
import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.utils.PropertiesUtilService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ItxSubjectContainerServiceImpl implements ItxSubjectContainerService {

	private static final Logger logger = LoggerFactory.getLogger(ItxSubjectContainerServiceImpl.class);
	
	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Transactional
	@Override
	public ItxSubjectContainer saveLsItxSubjectContainer(ItxSubjectContainer itxSubjectContainer){
		int i = 0;
		int j = 0;
		int batchSize = propertiesUtilService.getBatchSize();
		logger.debug("incoming meta ItxSubjectContainer: " + itxSubjectContainer.toJson() + "\n");
		ItxSubjectContainer newItxSubjContainer = new ItxSubjectContainer(itxSubjectContainer);
		newItxSubjContainer.setContainer(Container.findContainer(newItxSubjContainer.getContainer().getId()));
		newItxSubjContainer.setSubject(Subject.findSubject(newItxSubjContainer.getSubject().getId()));
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
	
	@Override
	@Transactional
	public ItxSubjectContainer updateItxSubjectContainer(ItxSubjectContainer jsonItxSubjectContainer){
		
		ItxSubjectContainer updatedItxSubjectContainer = ItxSubjectContainer.updateNoStates(jsonItxSubjectContainer);
		updatedItxSubjectContainer.merge();
		logger.debug("here is the updated itx: " + updatedItxSubjectContainer.toJson());
		logger.debug("----------------- here is the itx id " + updatedItxSubjectContainer.getId() + "   -----------");
		
		if(jsonItxSubjectContainer.getLsStates() != null){
			for(ItxSubjectContainerState itxSubjectContainerState : jsonItxSubjectContainer.getLsStates()){
				logger.debug("-------- current itxSubjectContainerState ID: " + itxSubjectContainerState.getId());
				ItxSubjectContainerState updatedItxSubjectContainerState;
				if (itxSubjectContainerState.getId() == null){
					updatedItxSubjectContainerState = new ItxSubjectContainerState(itxSubjectContainerState);
					updatedItxSubjectContainerState.setItxSubjectContainer(ItxSubjectContainer.findItxSubjectContainer(updatedItxSubjectContainer.getId()));
					updatedItxSubjectContainerState.persist();
					updatedItxSubjectContainer.getLsStates().add(updatedItxSubjectContainerState);
				} else {

					if (itxSubjectContainerState.getItxSubjectContainer() == null) itxSubjectContainerState.setItxSubjectContainer(updatedItxSubjectContainer);
					updatedItxSubjectContainerState = ItxSubjectContainerState.update(itxSubjectContainerState);			
					updatedItxSubjectContainer.getLsStates().add(updatedItxSubjectContainerState);
					logger.debug("updated itxSubjectContainer state " + updatedItxSubjectContainerState.getId());

				}
				if (itxSubjectContainerState.getLsValues() != null){
					for(ItxSubjectContainerValue itxSubjectContainerValue : itxSubjectContainerState.getLsValues()){
						ItxSubjectContainerValue updatedItxSubjectContainerValue;
						if (itxSubjectContainerValue.getId() == null){
							updatedItxSubjectContainerValue = ItxSubjectContainerValue.create(itxSubjectContainerValue);
							updatedItxSubjectContainerValue.setLsState(updatedItxSubjectContainerState);
							updatedItxSubjectContainerValue.persist();
							updatedItxSubjectContainerState.getLsValues().add(updatedItxSubjectContainerValue);

						} else {
							//itxSubjectContainerValue.setLsState(updatedItxSubjectContainerState);
							itxSubjectContainerValue.setLsState(updatedItxSubjectContainerState);
							updatedItxSubjectContainerValue = ItxSubjectContainerValue.update(itxSubjectContainerValue);
							updatedItxSubjectContainerState.getLsValues().add(updatedItxSubjectContainerValue);
						}
					}	
				} else {
					logger.debug("No itxSubjectContainer values to update");
				}
			}
		}
		
		return updatedItxSubjectContainer;
	}

}
