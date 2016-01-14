package com.labsynch.labseer.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.Container;
import com.labsynch.labseer.domain.ContainerLabel;
import com.labsynch.labseer.domain.ContainerState;
import com.labsynch.labseer.domain.ContainerValue;
import com.labsynch.labseer.domain.ItxContainerContainer;
import com.labsynch.labseer.dto.CodeLabelDTO;
import com.labsynch.labseer.dto.ContainerLocationDTO;
import com.labsynch.labseer.dto.PlateWellDTO;
import com.labsynch.labseer.dto.WellContentDTO;
import com.labsynch.labseer.domain.ItxContainerContainerState;
import com.labsynch.labseer.domain.ItxContainerContainerValue;
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
						ContainerValue newContainerValue = ContainerValue.create(containerValue);
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
	public Collection<WellContentDTO> getWellContent(List<String> wellCodes) {
		EntityManager em = Container.entityManager();
		String queryString = "SELECT new com.labsynch.labseer.dto.WellContentDTO( ";
		queryString += "well.codeName, ";
		queryString += "grossMassValue.numericValue, grossMassValue.unitKind,  ";
		queryString += "netMassValue.numericValue, netMassValue.unitKind, ";
		queryString += " batchCodeValue.codeValue, batchCodeValue.concentration, batchCodeValue.concUnit,  ";
		queryString += " solventCodeValue.codeValue,  ";
		queryString += " physicalStateValue.codeValue,  ";
		queryString += " volumeValue.numericValue, volumeValue.unitKind ";
		queryString += " )  ";
		queryString += " from Container as well ";
		queryString += makeInnerJoinHql("well.lsStates", "statusContentState", "status", "test compound content");
		queryString += makeLeftJoinHql("statusContentState.lsValues","grossMassValue", "numericValue","gross mass");
		queryString += makeLeftJoinHql("statusContentState.lsValues","netMassValue", "numericValue","net mass");
		queryString += makeLeftJoinHql("statusContentState.lsValues","batchCodeValue", "codeValue","batch code");
		queryString += makeLeftJoinHql("statusContentState.lsValues","solventCodeValue", "codeValue","solvent code");
		queryString += makeLeftJoinHql("statusContentState.lsValues","physicalStateValue", "codeValue","physical state");
		queryString += makeLeftJoinHql("statusContentState.lsValues","volumeValue", "numericValue","volume");
		queryString += "where ( well.codeName in (:wellCodes) ) and ( well.ignored <> true ) ";
		TypedQuery<WellContentDTO> q = em.createQuery(queryString, WellContentDTO.class);
		q.setParameter("wellCodes", wellCodes);
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

}
