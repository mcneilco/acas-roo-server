package com.labsynch.labseer.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.Container;
import com.labsynch.labseer.domain.ContainerLabel;
import com.labsynch.labseer.domain.ContainerState;
import com.labsynch.labseer.domain.ContainerValue;
import com.labsynch.labseer.domain.ItxContainerContainer;
import com.labsynch.labseer.domain.ItxContainerContainerState;
import com.labsynch.labseer.domain.ItxContainerContainerValue;
import com.labsynch.labseer.dto.CodeLabelDTO;
import com.labsynch.labseer.dto.ContainerRequestDTO;
import com.labsynch.labseer.dto.ContainerErrorMessageDTO;
import com.labsynch.labseer.dto.ContainerLocationDTO;
import com.labsynch.labseer.dto.PlateWellDTO;
import com.labsynch.labseer.dto.WellContentDTO;
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
		if (logger.isDebugEnabled()) logger.debug("container: " + container.toJson());
		if (container.getLsLabels() != null){
			for(ContainerLabel containerLabel : container.getLsLabels()){
				ContainerLabel newContainerLabel = new ContainerLabel(containerLabel);
				newContainerLabel.setContainer(newContainer);
				if (logger.isDebugEnabled()) logger.debug("here is the newcontainerLabel before save: " + newContainerLabel.toJson());
				newContainerLabel.persist();	
			}			
		} else {
			logger.info("No container labels to save");	
		}

		if (container.getLsStates() != null){
			for(ContainerState LsState : container.getLsStates()){
				ContainerState newLsState = new ContainerState(LsState);
				newLsState.setContainer(newContainer);
				if (logger.isDebugEnabled()) logger.debug("here is the newLsState before save: " + newLsState.toJson());
				newLsState.persist();
				if (logger.isDebugEnabled()) logger.debug("persisted the newLsState: " + newLsState.toJson());
				if (LsState.getLsValues() != null){
					for(ContainerValue containerValue : LsState.getLsValues()){
						if (logger.isDebugEnabled()) logger.debug("containerValue: " + containerValue.toJson());
						containerValue.setLsState(newLsState);
						containerValue.persist();
						if (logger.isDebugEnabled()) logger.debug("persisted the containerValue: " + containerValue.toJson());
					}				
				} else {
					if (logger.isDebugEnabled()) logger.debug("No container values to save");
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
		if (logger.isDebugEnabled()) logger.debug("incoming meta container: " + container.toJson() + "\n");
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
				if (logger.isDebugEnabled()) logger.debug("here is the newcontainerLabel before save: " + newContainerLabel.toJson());
				newContainerLabel.persist();
				lsLabels.add(newContainerLabel);
			}
			newContainer.setLsLabels(lsLabels);
		} else {
			if (logger.isDebugEnabled()) logger.debug("No container labels to save");	
		}

		if (container.getLsStates() != null){
			Set<ContainerState> lsStates = new HashSet<ContainerState>();
			for(ContainerState lsState : container.getLsStates()){
				ContainerState newLsState = new ContainerState(lsState);
				newLsState.setContainer(newContainer);
				if (logger.isDebugEnabled()) logger.debug("here is the newLsState before save: " + newLsState.toJson());
				newLsState.persist();
				if (logger.isDebugEnabled()) logger.debug("persisted the newLsState: " + newLsState.toJson());
				if (lsState.getLsValues() != null){
					Set<ContainerValue> lsValues = new HashSet<ContainerValue>();
					for(ContainerValue containerValue : lsState.getLsValues()){
						if (logger.isDebugEnabled()) logger.debug("containerValue: " + containerValue.toJson());
						ContainerValue newContainerValue = new ContainerValue(containerValue);
						newContainerValue.setLsState(newLsState);
						newContainerValue.persist();
						lsValues.add(newContainerValue);
						if (logger.isDebugEnabled()) logger.debug("persisted the containerValue: " + newContainerValue.toJson());
					}
					newLsState.setLsValues(lsValues);
				} else {
					if (logger.isDebugEnabled()) logger.debug("No container values to save");
				}
				lsStates.add(newLsState);
			}
			newContainer.setLsStates(lsStates);
		}
		
		if(container.getFirstContainers() != null){
			Set<ItxContainerContainer> firstContainers = new HashSet<ItxContainerContainer>();
			for (ItxContainerContainer itxContainerContainer : container.getFirstContainers()){
				if (itxContainerContainer.getId() == null){
					if (logger.isDebugEnabled()) logger.debug("saving new itxContainerContainer: " + itxContainerContainer.toJson());
					if (itxContainerContainer.getFirstContainer().getId() == null){
						if (logger.isDebugEnabled()) logger.debug("saving new nested Container" + itxContainerContainer.getFirstContainer().toJson());
						Container nestedContainer = saveLsContainer(itxContainerContainer.getFirstContainer());
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
					if (logger.isDebugEnabled()) logger.debug("saving new itxContainerContainer: " + itxContainerContainer.toJson());
					if (itxContainerContainer.getSecondContainer().getId() == null){
						if (logger.isDebugEnabled()) logger.debug("saving new nested Container: " + itxContainerContainer.getSecondContainer().toJson());
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
					updatedContainer.getLsLabels().add(newContainerLabel);
				} else {
					ContainerLabel updatedLabel = ContainerLabel.update(containerLabel);
					updatedContainer.getLsLabels().add(updatedLabel);
				}
			}	
		} else {
			logger.info("No container labels to update");	
		}
		updateLsStates(container, updatedContainer);

		Set<ItxContainerContainer> firstContainers = new HashSet<ItxContainerContainer>();
//		firstContainers.addAll(updatedContainer.getFirstContainers());
		if (logger.isDebugEnabled()) logger.debug("found number of first interactions: " + firstContainers.size());
		
		if(container.getFirstContainers() != null){
			//there are itx's
			for (ItxContainerContainer itxContainerContainer : container.getFirstContainers()){
				ItxContainerContainer updatedItxContainerContainer;
				if (itxContainerContainer.getId() == null){
					//need to save a new itx
					if (logger.isDebugEnabled()) logger.debug("saving new itxContainerContainer: " + itxContainerContainer.toJson());
					updateNestedFirstContainer(itxContainerContainer);
					itxContainerContainer.setSecondContainer(updatedContainer);
					updatedItxContainerContainer = saveItxContainerContainer(itxContainerContainer);
					firstContainers.add(updatedItxContainerContainer);
				}else {
					//old itx needs to be updated
					updateNestedFirstContainer(itxContainerContainer);
					itxContainerContainer.setSecondContainer(updatedContainer);
					updatedItxContainerContainer = ItxContainerContainer.update(itxContainerContainer);
					updateItxLsStates(itxContainerContainer, updatedItxContainerContainer);
					firstContainers.add(updatedItxContainerContainer);
				}
			}
			updatedContainer.setFirstContainers(firstContainers);
		}
		
		Set<ItxContainerContainer> secondContainers = new HashSet<ItxContainerContainer>();
//		secondContainers.addAll(updatedContainer.getSecondContainers());
//		secondContainers.addAll(ItxContainerContainer.findItxContainerContainersBySecondContainer(updatedContainer).getResultList());
		if (logger.isDebugEnabled()) logger.debug("found number of second interactions: " + secondContainers.size());

		
		if(container.getSecondContainers() != null){
			//there are itx's
			for (ItxContainerContainer itxContainerContainer : container.getSecondContainers()){
				if (logger.isDebugEnabled()) logger.debug("updating itxContainerContainer");
				ItxContainerContainer updatedItxContainerContainer;
				if (itxContainerContainer.getId() == null){
					//need to save a new itx
					if (logger.isDebugEnabled()) logger.debug("saving new itxContainerContainer: " + itxContainerContainer.toJson());
					updateNestedSecondContainer(itxContainerContainer);
					itxContainerContainer.setFirstContainer(updatedContainer);
					updatedItxContainerContainer = saveItxContainerContainer(itxContainerContainer);
					secondContainers.add(updatedItxContainerContainer);
				}else {
					//old itx needs to be updated
					updateNestedSecondContainer(itxContainerContainer);
					itxContainerContainer.setFirstContainer(updatedContainer);
					updatedItxContainerContainer = ItxContainerContainer.update(itxContainerContainer);
					updateItxLsStates(itxContainerContainer, updatedItxContainerContainer);
					secondContainers.add(updatedItxContainerContainer);
				}
			}
			updatedContainer.setSecondContainers(secondContainers);
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
	
	@Override
	public Collection<ContainerLocationDTO> getContainersInLocation(Collection<String> locationCodeNames){
		return getContainersInLocation(locationCodeNames, null, null);
	}
	
	@Override
	public Collection<ContainerLocationDTO> getContainersInLocation(Collection<String> locationCodeNames, String containerType, String containerKind){
		EntityManager em = Container.entityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ContainerLocationDTO> cq = cb.createQuery(ContainerLocationDTO.class);
		Root<Container> location = cq.from(Container.class);
		Join<Container, ItxContainerContainer> firstItx = location.join("firstContainers");
		Join<Container, ItxContainerContainer> container = firstItx.join("firstContainer");
		Join<Container, ContainerLabel> barcode = container.join("lsLabels", JoinType.LEFT);
		
		Predicate[] predicates = new Predicate[0];
		List<Predicate> predicateList = new ArrayList<Predicate>();
		Expression<String> locationCodeName = location.<String>get("codeName");
		Predicate locationCodeNameEquals = locationCodeName.in(locationCodeNames);
		predicateList.add(locationCodeNameEquals);
		Predicate itxType = cb.equal(firstItx.<String>get("lsType"), "moved to");
		predicateList.add(itxType);
		Predicate barcodeLsKind = cb.equal(barcode.<String>get("lsKind"), "barcode");
		predicateList.add(barcodeLsKind);
		
		//optional container type/kind
		if (containerType != null && containerType.length()>0){
			Predicate containerTypeEquals = cb.equal(container.<String>get("lsType"), containerType);
			predicateList.add(containerTypeEquals);
		}
		if (containerKind != null && containerKind.length()>0){
			Predicate containerKindEquals = cb.equal(container.<String>get("lsKind"), containerKind);
			predicateList.add(containerKindEquals);
		}
		//not ignored predicates
		Predicate locationNotIgnored = cb.not(location.<Boolean>get("ignored"));
		Predicate firstItxNotIgnored = cb.not(firstItx.<Boolean>get("ignored")); 
		Predicate containerNotIgnored =  cb.not(container.<Boolean>get("ignored"));
		Predicate barcodeNotIgnored =  cb.not(barcode.<Boolean>get("ignored"));
		predicateList.add(locationNotIgnored);
		predicateList.add(firstItxNotIgnored);
		predicateList.add(containerNotIgnored);
		predicateList.add(barcodeNotIgnored);
		
		predicates = predicateList.toArray(predicates);
		cq.where(cb.and(predicates));
		cq.multiselect(location.<String>get("codeName"), container.<String>get("codeName"), barcode.<String>get("labelText"));
		TypedQuery<ContainerLocationDTO> q = em.createQuery(cq);
//		if (logger.isDebugEnabled()) logger.debug(q.unwrap(org.hibernate.Query.class).getQueryString());
		Collection<ContainerLocationDTO> results = q.getResultList();
		return results;
	}
	
	private ItxContainerContainer saveItxContainerContainer(ItxContainerContainer itxContainerContainer){
		ItxContainerContainer newItxContainerContainer = new ItxContainerContainer(itxContainerContainer);
		newItxContainerContainer.persist();
		if(itxContainerContainer.getLsStates() != null){
			Set<ItxContainerContainerState> lsStates = new HashSet<ItxContainerContainerState>();
			for(ItxContainerContainerState itxContainerContainerState : itxContainerContainer.getLsStates()){
				ItxContainerContainerState newItxContainerContainerState = new ItxContainerContainerState(itxContainerContainerState);
				newItxContainerContainerState.setItxContainerContainer(newItxContainerContainer);
				if (logger.isDebugEnabled()) logger.debug("here is the newItxContainerContainerState before save: " + newItxContainerContainerState.toJson());
				newItxContainerContainerState.persist();
				if (logger.isDebugEnabled()) logger.debug("persisted the newItxContainerContainerState: " + newItxContainerContainerState.toJson());
				if (itxContainerContainerState.getLsValues() != null){
					Set<ItxContainerContainerValue> lsValues = new HashSet<ItxContainerContainerValue>();
					for(ItxContainerContainerValue itxContainerContainerValue : itxContainerContainerState.getLsValues()){
						if (logger.isDebugEnabled()) logger.debug("itxContainerContainerValue: " + itxContainerContainerValue.toJson());
						ItxContainerContainerValue newItxContainerContainerValue = new ItxContainerContainerValue(itxContainerContainerValue);
						newItxContainerContainerValue.setLsState(newItxContainerContainerState);
						newItxContainerContainerValue.persist();
						lsValues.add(newItxContainerContainerValue);
						if (logger.isDebugEnabled()) logger.debug("persisted the itxContainerContainerValue: " + newItxContainerContainerValue.toJson());
					}	
					newItxContainerContainerState.setLsValues(lsValues);
				} else {
					if (logger.isDebugEnabled()) logger.debug("No itxContainerContainer values to save");
				}
				lsStates.add(newItxContainerContainerState);
			}
			newItxContainerContainer.setLsStates(lsStates);
		}
		return newItxContainerContainer;
	}
	
	public void updateLsStates(Container jsonContainer, Container updatedContainer){
		if(jsonContainer.getLsStates() != null){
			for(ContainerState lsThingState : jsonContainer.getLsStates()){
				ContainerState updatedContainerState;
				if (lsThingState.getId() == null){
					updatedContainerState = new ContainerState(lsThingState);
					updatedContainerState.setContainer(updatedContainer);
					updatedContainerState.persist();
					updatedContainer.getLsStates().add(updatedContainerState);
					if (logger.isDebugEnabled()) logger.debug("persisted new lsThing state " + updatedContainerState.getId());

				} else {
					updatedContainerState = ContainerState.update(lsThingState);
					updatedContainer.getLsStates().add(updatedContainerState);

					if (logger.isDebugEnabled()) logger.debug("updated lsThing state " + updatedContainerState.getId());

				}
				if (lsThingState.getLsValues() != null){
					for(ContainerValue lsThingValue : lsThingState.getLsValues()){
						if (lsThingValue.getLsState() == null) lsThingValue.setLsState(updatedContainerState);
						ContainerValue updatedContainerValue;
						if (lsThingValue.getId() == null){
							updatedContainerValue = ContainerValue.create(lsThingValue);
							updatedContainerValue.setLsState(ContainerState.findContainerState(updatedContainerState.getId()));
							updatedContainerValue.persist();
							updatedContainerState.getLsValues().add(updatedContainerValue);
						} else {
							updatedContainerValue = ContainerValue.update(lsThingValue);
							if (logger.isDebugEnabled()) logger.debug("updated lsThing value " + updatedContainerValue.getId());
						}
						if (logger.isDebugEnabled()) logger.debug("checking lsThingValue " + updatedContainerValue.toJson());

					}	
				} else {
					if (logger.isDebugEnabled()) logger.debug("No lsThing values to update");
				}
			}
		}
	}
	
	private void updateNestedFirstContainer(ItxContainerContainer itxContainerContainer) {
		Container updatedNestedContainer;
		if (itxContainerContainer.getFirstContainer().getId() == null){
			//need to save a new nested lsthing
			if (logger.isDebugEnabled()) logger.debug("saving new nested Container" + itxContainerContainer.getFirstContainer().toJson());
			updatedNestedContainer = saveContainer(itxContainerContainer.getFirstContainer());
			itxContainerContainer.setFirstContainer(updatedNestedContainer);
		}
		else{
			//just need to update the old nested lsThing inside the new itx
			updatedNestedContainer = Container.update(itxContainerContainer.getFirstContainer());
			updateLsStates(itxContainerContainer.getFirstContainer(), updatedNestedContainer);
			itxContainerContainer.setFirstContainer(updatedNestedContainer);
		}
	}
	
	private void updateNestedSecondContainer(ItxContainerContainer itxContainerContainer) {
		Container updatedNestedContainer;
		if (itxContainerContainer.getSecondContainer().getId() == null){
			//need to save a new nested lsthing
			if (logger.isDebugEnabled()) logger.debug("saving new nested Container" + itxContainerContainer.getSecondContainer().toJson());
			updatedNestedContainer = saveContainer(itxContainerContainer.getSecondContainer());
			itxContainerContainer.setSecondContainer(updatedNestedContainer);
		}
		else{
			//just need to update the old nested lsThing inside the new itx
			updatedNestedContainer = Container.update(itxContainerContainer.getSecondContainer());
			updateLsStates(itxContainerContainer.getSecondContainer(), updatedNestedContainer);
			itxContainerContainer.setSecondContainer(updatedNestedContainer);
		}
	}
	
	private void updateItxLsStates(ItxContainerContainer jsonItxContainerContainer, ItxContainerContainer updatedItxContainerContainer){
		if(jsonItxContainerContainer.getLsStates() != null){
			for(ItxContainerContainerState itxContainerContainerState : jsonItxContainerContainer.getLsStates()){
				ItxContainerContainerState updatedItxContainerContainerState;
				if (itxContainerContainerState.getId() == null){
					updatedItxContainerContainerState = new ItxContainerContainerState(itxContainerContainerState);
					updatedItxContainerContainerState.setItxContainerContainer(updatedItxContainerContainer);
					updatedItxContainerContainerState.persist();
					updatedItxContainerContainer.getLsStates().add(updatedItxContainerContainerState);
				} else {
					updatedItxContainerContainerState = ItxContainerContainerState.update(itxContainerContainerState);
					updatedItxContainerContainerState.setItxContainerContainer(updatedItxContainerContainer);
					updatedItxContainerContainerState.merge();
					updatedItxContainerContainer.getLsStates().add(updatedItxContainerContainerState);
					if (logger.isDebugEnabled()) logger.debug("updated itxContainerContainer state " + updatedItxContainerContainerState.getId());

				}
				if (itxContainerContainerState.getLsValues() != null){
					for(ItxContainerContainerValue itxContainerContainerValue : itxContainerContainerState.getLsValues()){
						ItxContainerContainerValue updatedItxContainerContainerValue;
						if (itxContainerContainerValue.getId() == null){
							updatedItxContainerContainerValue = ItxContainerContainerValue.create(itxContainerContainerValue);
							updatedItxContainerContainerValue.setLsState(ItxContainerContainerState.findItxContainerContainerState(updatedItxContainerContainerState.getId()));
							updatedItxContainerContainerValue.persist();
							updatedItxContainerContainerState.getLsValues().add(updatedItxContainerContainerValue);
						} else {
							updatedItxContainerContainerValue = ItxContainerContainerValue.update(itxContainerContainerValue);
							updatedItxContainerContainerValue.setLsState(updatedItxContainerContainerState);
							updatedItxContainerContainerValue.merge();
							updatedItxContainerContainerState.getLsValues().add(updatedItxContainerContainerValue);
							if (logger.isDebugEnabled()) logger.debug("updated itxContainerContainer value " + updatedItxContainerContainerValue.getId());
						}
					}	
				} else {
					if (logger.isDebugEnabled()) logger.debug("No itxContainerContainer values to update");
				}
			}
		}
	}

	@Override
	public Collection<PlateWellDTO> getWellCodesByPlateBarcodes(
			List<String> plateBarcodes) {
		EntityManager em = Container.entityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<PlateWellDTO> cq = cb.createQuery(PlateWellDTO.class);
		Root<Container> plate = cq.from(Container.class);
		Join<Container, ItxContainerContainer> secondItx = plate.join("secondContainers");
		Join<Container, ItxContainerContainer> well = secondItx.join("secondContainer");
		Join<Container, ContainerLabel> plateBarcode = plate.join("lsLabels");
		Join<Container, ContainerLabel> wellLabel = well.join("lsLabels");
		
		Predicate[] predicates = new Predicate[0];
		List<Predicate> predicateList = new ArrayList<Predicate>();
		Predicate plateBarcodeLsType = cb.equal(plateBarcode.<String>get("lsType"), "barcode");
		Predicate plateBarcodeLsKind = cb.equal(plateBarcode.<String>get("lsKind"), "barcode");
		predicateList.add(plateBarcodeLsType);
		predicateList.add(plateBarcodeLsKind);
		Expression<String> plateBarcodeLabelText = plateBarcode.<String>get("labelText");
		Predicate plateBarcodeEquals = plateBarcodeLabelText.in(plateBarcodes);
		predicateList.add(plateBarcodeEquals);
		Predicate itxType = cb.equal(secondItx.<String>get("lsType"), "has member");
		Predicate itxKind = cb.equal(secondItx.<String>get("lsKind"), "plate well");
		predicateList.add(itxType);
		predicateList.add(itxKind);
		Predicate wellLabelLsType = cb.equal(wellLabel.<String>get("lsType"), "name");
		Predicate wellLabelLsKind = cb.equal(wellLabel.<String>get("lsKind"), "well name");
		predicateList.add(wellLabelLsType);
		predicateList.add(wellLabelLsKind);
		
		//not ignored predicates
		Predicate plateNotIgnored = cb.not(plate.<Boolean>get("ignored"));
		Predicate secondItxNotIgnored = cb.not(secondItx.<Boolean>get("ignored")); 
		Predicate wellNotIgnored =  cb.not(well.<Boolean>get("ignored"));
		Predicate plateBarcodeNotIgnored =  cb.not(plateBarcode.<Boolean>get("ignored"));
		Predicate wellBarcodeNotIgnored =  cb.not(plateBarcode.<Boolean>get("ignored"));
		predicateList.add(plateNotIgnored);
		predicateList.add(secondItxNotIgnored);
		predicateList.add(wellNotIgnored);
		predicateList.add(plateBarcodeNotIgnored);
		predicateList.add(wellBarcodeNotIgnored);
		
		predicates = predicateList.toArray(predicates);
		cq.where(cb.and(predicates));
		cq.multiselect(plateBarcode.<String>get("labelText"), plate.<String>get("codeName"), well.<String>get("codeName"), wellLabel.<String>get("labelText"));
		TypedQuery<PlateWellDTO> q = em.createQuery(cq);
//		if (logger.isDebugEnabled()) logger.debug(q.unwrap(org.hibernate.Query.class).getQueryString());
		Collection<PlateWellDTO> results = q.getResultList();
		return results;
	}

	@Override
	public Collection<CodeLabelDTO> getContainerCodesByLabels(
			List<String> labelTexts, String containerType, String containerKind, String labelType, String labelKind) {
		EntityManager em = Container.entityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<CodeLabelDTO> cq = cb.createQuery(CodeLabelDTO.class);
		Root<Container> container = cq.from(Container.class);
		Join<Container, ContainerLabel> label = container.join("lsLabels");
		
		Predicate[] predicates = new Predicate[0];
		List<Predicate> predicateList = new ArrayList<Predicate>();
		Expression<String> containerLabelText = label.<String>get("labelText");
		Predicate containerLabelTextEquals = containerLabelText.in(labelTexts);
		predicateList.add(containerLabelTextEquals);
		
		//optional container type/kind and label type/kind
		if (containerType != null && containerType.length()>0){
			Predicate containerTypeEquals = cb.equal(container.<String>get("lsType"), containerType);
			predicateList.add(containerTypeEquals);
		}
		if (containerKind != null && containerKind.length()>0){
			Predicate containerKindEquals = cb.equal(container.<String>get("lsKind"), containerKind);
			predicateList.add(containerKindEquals);
		}
		if (labelType != null && labelType.length()>0){
			Predicate containerLabelTypeEquals = cb.equal(label.<String>get("lsType"), labelType);
			predicateList.add(containerLabelTypeEquals);
		}
		if (labelKind != null && labelKind.length()>0){
			Predicate containerLabelKindEquals = cb.equal(label.<String>get("lsKind"), labelKind);
			predicateList.add(containerLabelKindEquals);
		}
		//not ignored predicates
		Predicate containerNotIgnored =  cb.not(container.<Boolean>get("ignored"));
		Predicate labelNotIgnored =  cb.not(label.<Boolean>get("ignored"));		
		predicateList.add(containerNotIgnored);
		predicateList.add(labelNotIgnored);
		
		predicates = predicateList.toArray(predicates);
		cq.where(cb.and(predicates));
		cq.multiselect(container.<String>get("codeName"), label.<String>get("labelText"));
		TypedQuery<CodeLabelDTO> q = em.createQuery(cq);
//		if (logger.isDebugEnabled()) logger.debug(q.unwrap(org.hibernate.Query.class).getQueryString());
		Collection<CodeLabelDTO> results = q.getResultList();
		return results;
	}

	@Override
	public Collection<WellContentDTO> getWellContent(Collection<ContainerRequestDTO> wellCodes) {
		List<String> wellCodeStrings = new ArrayList<String>();
		for (ContainerRequestDTO wellCode : wellCodes){
			wellCodeStrings.add(wellCode.getContainerCodeName());
		}
		EntityManager em = Container.entityManager();
		String queryString = "SELECT new com.labsynch.labseer.dto.WellContentDTO( ";
		queryString += "well.codeName, ";
		queryString += "amountValue.numericValue, amountValue.unitKind,  ";
		queryString += " batchCodeValue.codeValue, batchCodeValue.concentration, batchCodeValue.concUnit,  ";
		queryString += " solventCodeValue.codeValue,  ";
		queryString += " physicalStateValue.codeValue  ";
		queryString += " )  ";
		queryString += " from Container as well ";
		queryString += makeInnerJoinHql("well.lsStates", "statusContentState", "status", "content");
		queryString += makeLeftJoinHql("statusContentState.lsValues","amountValue", "numericValue","amount");
		queryString += makeLeftJoinHql("statusContentState.lsValues","batchCodeValue", "codeValue","batch code");
		queryString += makeLeftJoinHql("statusContentState.lsValues","solventCodeValue", "codeValue","solvent code");
		queryString += makeLeftJoinHql("statusContentState.lsValues","physicalStateValue", "codeValue","physical state");
		queryString += "where ( well.ignored <> true ) and ";
		Map<String, Collection<String>> sqlCurveIdMap = new HashMap<String, Collection<String>>();
    	List<String> allCodes = new ArrayList<String>();
    	allCodes.addAll(wellCodeStrings);
    	int startIndex = 0;
    	while (startIndex < wellCodeStrings.size()){
    		int endIndex;
    		if (startIndex+999 < wellCodeStrings.size()) endIndex = startIndex+999;
    		else endIndex = wellCodeStrings.size();
    		List<String> nextCurveIds = allCodes.subList(startIndex, endIndex);
    		String groupName = "curveIds"+startIndex;
    		String sqlClause = " well.codeName IN (:"+groupName+")";
    		sqlCurveIdMap.put(sqlClause, nextCurveIds);
    		startIndex=endIndex;
    	}
    	int numClause = 1;
    	for (String sqlClause : sqlCurveIdMap.keySet()){
    		if (numClause == 1){
    			queryString = queryString + sqlClause;
    		}else{
    			queryString = queryString + " OR " + sqlClause;
    		}
    		numClause++;
    	}
    	queryString = queryString + " )";
		TypedQuery<WellContentDTO> q = em.createQuery(queryString, WellContentDTO.class);
		for (String sqlClause : sqlCurveIdMap.keySet()){
        	String groupName = sqlClause.split(":")[1].replace(")","");
        	q.setParameter(groupName, sqlCurveIdMap.get(sqlClause));
        }
//		if (logger.isDebugEnabled()) logger.debug(q.unwrap(org.hibernate.Query.class).getQueryString());
		Collection<WellContentDTO> results = q.getResultList();
		return results;
	}
	
	private String makeLeftJoinHql(String table, String alias, String lsType, String lsKind){
		String queryString = "left join "+table+" as "+alias+" with "+alias+".lsType='"+lsType+"' and "+alias+".lsKind='"+lsKind+"' and "+alias+".ignored <> true ";
		return queryString;
	}
	private String makeInnerJoinHql(String table, String alias, String lsType, String lsKind){
		String queryString = "inner join "+table+" as "+alias+" with "+alias+".lsType='"+lsType+"' and "+alias+".lsKind='"+lsKind+"' and "+alias+".ignored <> true ";
		return queryString;
	}

	@Override
	public Collection<ContainerErrorMessageDTO> throwInTrash(
			Collection<ContainerRequestDTO> containersToTrash) throws Exception {
		Collection<ContainerErrorMessageDTO> results = new HashSet<ContainerErrorMessageDTO>();
		for (ContainerRequestDTO dto : containersToTrash){
			ContainerErrorMessageDTO result = new ContainerErrorMessageDTO();
			result.setContainerCodeName(dto.getContainerCodeName());
			results.add(result);
			Container container;
			try{
				container = Container.findContainerByCodeNameEquals(dto.getContainerCodeName());
			}catch (Exception e){
				result.setLevel("error");
				result.setMessage("containerCodeName not found");
				continue;
			}
			//ignore the old movedTo interaction to preserve history
			try{
				ItxContainerContainer movedTo = ItxContainerContainer.findItxContainerContainersByLsTypeEqualsAndLsKindEqualsAndFirstContainerEquals("moved to", "storage move", container).getSingleResult();
				movedTo.setIgnored(true);
				movedTo.setModifiedBy(dto.getModifiedBy());
				movedTo.setModifiedDate(dto.getModifiedDate());
				movedTo.merge();
			} catch(Exception e){
				result.setLevel("error");
				result.setMessage("Error finding 'moved to'/'storage move' interaction to ignore.");
				continue;
			}
			
			//create trash interaction to show container has been moved to the trash
			try{
				ItxContainerContainer trashItx = new ItxContainerContainer();
				trashItx.setLsType("moved to");
				trashItx.setLsKind("storage move");
				trashItx.setRecordedBy(dto.getModifiedBy());
				trashItx.setRecordedDate(dto.getModifiedDate());
				trashItx.setFirstContainer(container);
				trashItx.setSecondContainer(getOrCreateTrash());
				trashItx.persist();
			}catch(Exception e){
				result.setLevel("error");
				result.setMessage("Error creating new interaction to trash");
				continue;
			}
			//ignore container since it is now in the trash
			try{
				container.setIgnored(true);
				container.setModifiedBy(dto.getModifiedBy());
				container.setModifiedDate(dto.getModifiedDate());		
				container.merge();
			}catch (Exception e){
				result.setLevel("error");
				result.setMessage("Error ignoring container");
				continue;
			}
		}
		return results;
	}
	
	private Container getOrCreateTrash() throws Exception{
		try{
			List<Container> trashes = Container.findContainerByContainerLabel("trash");
			if (trashes.size() > 1 ) throw new Exception("Multiple containers called 'trash' exist.");
			Container trash = trashes.get(0);
			return trash;
		}catch(EmptyResultDataAccessException e){
			Container trash = new Container();
			trash.setCodeName(autoLabelService.getContainerCodeName());
			trash.setLsType("location");
			trash.setLsKind("default");
			trash.setRecordedBy("acas");
			trash.setRecordedDate(new Date());
			ContainerLabel trashLabel = new ContainerLabel();
			trashLabel.setLsType("name");
			trashLabel.setLsKind("common");
			trashLabel.setLabelText("trash");
			trashLabel.setRecordedBy("acas");
			trashLabel.setRecordedDate(new Date());
			trashLabel.setPreferred(true);
			trash.getLsLabels().add(trashLabel);
			
			ContainerState trashState = new ContainerState();
			trashState.setLsType("metadata");
			trashState.setLsKind("information");
			trashState.setRecordedBy("acas");
			trashState.setRecordedDate(new Date());
			
			ContainerValue trashUserValue = new ContainerValue();
			trashUserValue.setLsType("stringValue");
			trashUserValue.setLsKind("created user");
			trashUserValue.setRecordedBy("acas");
			trashUserValue.setRecordedDate(new Date());
			trashUserValue.setStringValue("acas");
			trashState.getLsValues().add(trashUserValue);
			
			ContainerValue trashDescriptionValue = new ContainerValue();
			trashDescriptionValue.setLsType("stringValue");
			trashDescriptionValue.setLsKind("description");
			trashDescriptionValue.setRecordedBy("acas");
			trashDescriptionValue.setRecordedDate(new Date());
			trashDescriptionValue.setStringValue("trash");
			trashState.getLsValues().add(trashDescriptionValue);
			
			ContainerValue trashDateValue = new ContainerValue();
			trashDateValue.setLsType("dateValue");
			trashDateValue.setLsKind("created date");
			trashDateValue.setRecordedBy("acas");
			trashDateValue.setRecordedDate(new Date());
			trashDateValue.setDateValue(new Date());
			trashState.getLsValues().add(trashDateValue);
			
			trash.getLsStates().add(trashState);
			
			Container newTrash = saveLsContainer(trash);
			
			return newTrash;
		}
	}

	@Override
	public Collection<ContainerErrorMessageDTO> updateAmountInWell(
			Collection<ContainerRequestDTO> wellsToUpdate) {
		Collection<ContainerErrorMessageDTO> results = new HashSet<ContainerErrorMessageDTO>();
		for (ContainerRequestDTO dto : wellsToUpdate){
			ContainerErrorMessageDTO result = new ContainerErrorMessageDTO();
			result.setContainerCodeName(dto.getContainerCodeName());
			results.add(result);
			Container container;
			try{
				container = Container.findContainerByCodeNameEquals(dto.getContainerCodeName());
			}catch (Exception e){
				result.setLevel("error");
				result.setMessage("containerCodeName not found");
				continue;
			}
			//find amount value and update
			ContainerValue amountValue;
			try{
				for (ContainerState state : container.getLsStates()){
					if (state.getLsType().equals("status") && state.getLsKind().equals("content")){
						for (ContainerValue value : state.getLsValues()){
							if (value.getLsKind().equals("amount")){
								amountValue = value;
								amountValue.setNumericValue(dto.getAmount());
								amountValue.setUnitKind(dto.getAmountUnits());
								amountValue.setModifiedBy(dto.getModifiedBy());
								amountValue.setModifiedDate(dto.getModifiedDate());
								amountValue.merge();
							}
						}
					}
				}
			}catch (Exception e){
				result.setLevel("error");
				result.setMessage("Amount value not found");
				continue;
			}
		}
		return results;
	}

}
