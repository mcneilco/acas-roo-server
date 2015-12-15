package com.labsynch.labseer.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.Container;
import com.labsynch.labseer.domain.ContainerLabel;
import com.labsynch.labseer.domain.ContainerState;
import com.labsynch.labseer.domain.ContainerValue;
import com.labsynch.labseer.domain.ItxContainerContainer;
import com.labsynch.labseer.domain.ItxContainerContainerState;
import com.labsynch.labseer.domain.ItxContainerContainerValue;
import com.labsynch.labseer.domain.ItxLsThingLsThing;
import com.labsynch.labseer.domain.ItxLsThingLsThingState;
import com.labsynch.labseer.domain.ItxLsThingLsThingValue;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.LsThingLabel;
import com.labsynch.labseer.domain.LsThingValue;
import com.labsynch.labseer.utils.PropertiesUtilService;

import flexjson.JSONTokener;

@Service
@Transactional
public class ContainerServiceImpl implements ContainerService {

	private static final Logger logger = LoggerFactory.getLogger(ContainerServiceImpl.class);
	
	@Autowired
	private PropertiesUtilService propertiesUtilService;
	
	@Autowired
	private AutoLabelService autoLabelService;
	
	@Override
	public Collection<Container> saveLsContainersFile(String jsonFile){
		Collection<Container> savedContainers = new ArrayList<Container>();
		int batchSize = propertiesUtilService.getBatchSize();
		int i = 0;
		
		try {			
			BufferedReader br = new BufferedReader(new FileReader(jsonFile));
			JSONTokener jsonTokens = new JSONTokener(br);
			Object token;
			char delimiter;
			char END_OF_ARRAY = ']';
			while (jsonTokens.more()){
				delimiter = jsonTokens.nextClean();
				if (delimiter != END_OF_ARRAY){
					token = jsonTokens.nextValue();
					Container container = saveContainer(token.toString());
					savedContainers.add(container);
					if ( i % batchSize == 0 ) { 
						container.flush();
						container.clear();
					}
					i++;
				} else {
					break;
				}
			}		
		} catch (Exception e){
			logger.error("ERROR: " + e);
		}

		return savedContainers;

	}
	
	@Override
	public Collection<Container> saveLsContainersParse(String json){
		Collection<Container> savedContainers = new ArrayList<Container>();
		int batchSize = propertiesUtilService.getBatchSize();
		int i = 0;
		
		try {		
			StringReader sr = new StringReader(json);
			BufferedReader br = new BufferedReader(sr);
			
			for (Container container : Container.fromJsonArrayToContainers(br)){
				savedContainers.add(container);
				if ( i % batchSize == 0 ) { 
					container.flush();
					container.clear();
				}
				i++;				
			}
					
		} catch (Exception e){
			logger.error("ERROR: " + e);
		}

		return savedContainers;

	}
	
	
	
	private Container saveContainer(String json) {
		Container container = Container.fromJsonToContainer(json);
		return saveContainer(container);
	}

	private Container saveContainer(Container container) {
		Container newContainer = new Container(container);
		newContainer.persist();
		logger.debug("container: " + container.toJson());
		if (container.getLsLabels() != null){
			for(ContainerLabel containerLabel : container.getLsLabels()){
				ContainerLabel newContainerLabel = new ContainerLabel(containerLabel);
				newContainerLabel.setContainer(newContainer);
				logger.debug("here is the newcontainerLabel before save: " + newContainerLabel.toJson());
				newContainerLabel.persist();	
			}			
		} else {
			logger.info("No container labels to save");	
		}

		if (container.getLsStates() != null){
			for(ContainerState LsState : container.getLsStates()){
				ContainerState newLsState = new ContainerState(LsState);
				newLsState.setContainer(newContainer);
				logger.debug("here is the newLsState before save: " + newLsState.toJson());
				newLsState.persist();
				logger.debug("persisted the newLsState: " + newLsState.toJson());
				if (LsState.getLsValues() != null){
					for(ContainerValue containerValue : LsState.getLsValues()){
						logger.debug("containerValue: " + containerValue.toJson());
						containerValue.setLsState(newLsState);
						containerValue.persist();
						logger.debug("persisted the containerValue: " + containerValue.toJson());
					}				
				} else {
					logger.debug("No container values to save");
				}
			}
		}
		return newContainer;
	}


	@Override
	public Collection<Container> saveLsContainers(String json){
		Collection<Container> savedContainers = new ArrayList<Container>();
		int batchSize = propertiesUtilService.getBatchSize();
		int i = 0;
		StringReader sr = new StringReader(json);
		BufferedReader br = new BufferedReader(sr);
		
		for (Container container : Container.fromJsonArrayToContainers(br)){
			Container newContainer = saveContainer(container);
			savedContainers.add(newContainer);
			if ( i % batchSize == 0 ) { 
				newContainer.flush();
				newContainer.clear();
			}
			i++;
				
		}

		return savedContainers;
	}
	
	@Override
	public Container saveLsContainer(Container container){
		logger.debug("incoming meta container: " + container.toJson() + "\n");
		Container newContainer = new Container(container);
		if (newContainer.getCodeName() == null){
			if (newContainer.getLsTypeAndKind() == null) newContainer.setLsTypeAndKind(newContainer.getLsType()+"_"+newContainer.getLsKind());
			newContainer.setCodeName(autoLabelService.getContainerCodeName());
		}
		newContainer.persist();
		if (container.getLsLabels() != null){
			Set<ContainerLabel> lsLabels = new HashSet<ContainerLabel>();
			for(ContainerLabel containerLabel : container.getLsLabels()){
				ContainerLabel newContainerLabel = new ContainerLabel(containerLabel);
				newContainerLabel.setContainer(newContainer);
				logger.debug("here is the newcontainerLabel before save: " + newContainerLabel.toJson());
				newContainerLabel.persist();
				lsLabels.add(newContainerLabel);
			}
			newContainer.setLsLabels(lsLabels);
		} else {
			logger.debug("No container labels to save");	
		}

		if (container.getLsStates() != null){
			Set<ContainerState> lsStates = new HashSet<ContainerState>();
			for(ContainerState lsState : container.getLsStates()){
				ContainerState newLsState = new ContainerState(lsState);
				newLsState.setContainer(newContainer);
				logger.debug("here is the newLsState before save: " + newLsState.toJson());
				newLsState.persist();
				logger.debug("persisted the newLsState: " + newLsState.toJson());
				if (lsState.getLsValues() != null){
					Set<ContainerValue> lsValues = new HashSet<ContainerValue>();
					for(ContainerValue containerValue : lsState.getLsValues()){
						logger.debug("containerValue: " + containerValue.toJson());
						ContainerValue newContainerValue = new ContainerValue(containerValue);
						newContainerValue.setLsState(newLsState);
						newContainerValue.persist();
						lsValues.add(newContainerValue);
						logger.debug("persisted the containerValue: " + newContainerValue.toJson());
					}
					newLsState.setLsValues(lsValues);
				} else {
					logger.debug("No container values to save");
				}
				lsStates.add(newLsState);
			}
			newContainer.setLsStates(lsStates);
		}
		
		if(container.getFirstContainers() != null){
			Set<ItxContainerContainer> firstContainers = new HashSet<ItxContainerContainer>();
			for (ItxContainerContainer itxContainerContainer : container.getFirstContainers()){
				if (itxContainerContainer.getId() == null){
					logger.debug("saving new itxContainerContainer: " + itxContainerContainer.toJson());
					if (itxContainerContainer.getFirstContainer().getId() == null){
						logger.debug("saving new nested Container" + itxContainerContainer.getFirstContainer().toJson());
						Container nestedContainer = saveContainer(itxContainerContainer.getFirstContainer());
						itxContainerContainer.setFirstContainer(nestedContainer);
					}
					itxContainerContainer.setSecondContainer(newContainer);
					ItxContainerContainer newItxContainerContainer = saveItxContainerContainer(itxContainerContainer);
					firstContainers.add(newItxContainerContainer);
				}else {
					firstContainers.add(itxContainerContainer);
				}
			}
			newContainer.setFirstContainers(firstContainers);
		}
		
		if(container.getSecondContainers() != null){
			Set<ItxContainerContainer> secondContainers = new HashSet<ItxContainerContainer>();
			for (ItxContainerContainer itxContainerContainer : container.getSecondContainers()){
				if (itxContainerContainer.getId() == null){
					logger.debug("saving new itxContainerContainer: " + itxContainerContainer.toJson());
					if (itxContainerContainer.getSecondContainer().getId() == null){
						logger.debug("saving new nested Container: " + itxContainerContainer.getSecondContainer().toJson());
						Container nestedContainer = saveContainer(itxContainerContainer.getSecondContainer());
						itxContainerContainer.setSecondContainer(nestedContainer);
					}
					itxContainerContainer.setFirstContainer(newContainer);
					ItxContainerContainer newItxContainerContainer = saveItxContainerContainer(itxContainerContainer);
					secondContainers.add(newItxContainerContainer);
				}else {
					secondContainers.add(itxContainerContainer);
				}
			}
			newContainer.setSecondContainers(secondContainers);
		}
		
		return Container.findContainer(newContainer.getId());
	}

	@Override
	@Transactional
	public Container updateContainer(Container container){
		logger.info("incoming meta container: " + container.toJson() + "\n");
		Container updatedContainer = Container.update(container);
		if (container.getLsLabels() != null){
			for(ContainerLabel containerLabel : container.getLsLabels()){
				if (containerLabel.getId() == null){
					ContainerLabel newContainerLabel = new ContainerLabel(containerLabel);
					newContainerLabel.setContainer(updatedContainer);
					newContainerLabel.persist();						
				} else {
					ContainerLabel.update(containerLabel).merge();
				}
			}	
		} else {
			logger.info("No container labels to save");	
		}

		if (container.getLsStates() != null){
			for(ContainerState containerState : container.getLsStates()){
				if (containerState.getId() == null){
					ContainerState newContainerState = new ContainerState(containerState);
					newContainerState.setContainer(updatedContainer);
					newContainerState.persist();		
					containerState.setId(newContainerState.getId());
				} else {
					ContainerState updatedContainerState = ContainerState.update(containerState);
					logger.debug("updatedContainerState: " + updatedContainerState.toJson());
				}

				if (containerState.getLsValues() != null){
					for(ContainerValue containerValue : containerState.getLsValues()){
						if (containerValue.getId() == null){
							containerValue.setLsState(ContainerState.findContainerState(containerState.getId()));
							containerValue.persist();							
						} else {
							ContainerValue updatedContainerValue = ContainerValue.update(containerValue);
							logger.debug("updatedContainerValue: " + updatedContainerValue.toJson());
						}
					}				
				} else {
					logger.info("No container values to save");
				}
			}
		}

		return Container.findContainer(updatedContainer.getId());
	}

	@Override
	public Container saveLsContainer(String json) {
		return saveLsContainer(Container.fromJsonToContainer(json));
	}

	@Override
	public Collection<Container> saveLsContainers(
			Collection<Container> containers) {
		Collection<Container> savedContainers = new HashSet<Container>();
		for (Container container : containers){
			Container savedContainer = saveLsContainer(container);
			savedContainers.add(savedContainer);
		}
		return savedContainers;
	}
	
	private ItxContainerContainer saveItxContainerContainer(ItxContainerContainer itxContainerContainer){
		ItxContainerContainer newItxContainerContainer = new ItxContainerContainer(itxContainerContainer);
		newItxContainerContainer.persist();
		if(itxContainerContainer.getLsStates() != null){
			Set<ItxContainerContainerState> lsStates = new HashSet<ItxContainerContainerState>();
			for(ItxContainerContainerState itxContainerContainerState : itxContainerContainer.getLsStates()){
				ItxContainerContainerState newItxContainerContainerState = new ItxContainerContainerState(itxContainerContainerState);
				newItxContainerContainerState.setItxContainerContainer(newItxContainerContainer);
				logger.debug("here is the newItxContainerContainerState before save: " + newItxContainerContainerState.toJson());
				newItxContainerContainerState.persist();
				logger.debug("persisted the newItxContainerContainerState: " + newItxContainerContainerState.toJson());
				if (itxContainerContainerState.getLsValues() != null){
					Set<ItxContainerContainerValue> lsValues = new HashSet<ItxContainerContainerValue>();
					for(ItxContainerContainerValue itxContainerContainerValue : itxContainerContainerState.getLsValues()){
						logger.debug("itxContainerContainerValue: " + itxContainerContainerValue.toJson());
						ItxContainerContainerValue newItxContainerContainerValue = new ItxContainerContainerValue(itxContainerContainerValue);
						newItxContainerContainerValue.setLsState(newItxContainerContainerState);
						newItxContainerContainerValue.persist();
						lsValues.add(newItxContainerContainerValue);
						logger.debug("persisted the itxContainerContainerValue: " + newItxContainerContainerValue.toJson());
					}	
					newItxContainerContainerState.setLsValues(lsValues);
				} else {
					logger.debug("No itxContainerContainer values to save");
				}
				lsStates.add(newItxContainerContainerState);
			}
			newItxContainerContainer.setLsStates(lsStates);
		}
		return newItxContainerContainer;
	}

}
