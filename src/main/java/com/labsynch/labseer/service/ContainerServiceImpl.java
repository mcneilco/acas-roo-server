package com.labsynch.labseer.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.Container;
import com.labsynch.labseer.domain.ContainerLabel;
import com.labsynch.labseer.domain.ContainerState;
import com.labsynch.labseer.domain.ContainerValue;
import com.labsynch.labseer.domain.ItxContainerContainer;
import com.labsynch.labseer.domain.ItxContainerContainerState;
import com.labsynch.labseer.domain.ItxContainerContainerValue;
import com.labsynch.labseer.domain.LsTransaction;
import com.labsynch.labseer.dto.AutoLabelDTO;
import com.labsynch.labseer.dto.CodeLabelDTO;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.ContainerDependencyCheckDTO;
import com.labsynch.labseer.dto.ContainerErrorMessageDTO;
import com.labsynch.labseer.dto.ContainerLocationDTO;
import com.labsynch.labseer.dto.ContainerRequestDTO;
import com.labsynch.labseer.dto.ContainerSearchRequestDTO;
import com.labsynch.labseer.dto.ContainerValueRequestDTO;
import com.labsynch.labseer.dto.ContainerWellCodeDTO;
import com.labsynch.labseer.dto.CreatePlateRequestDTO;
import com.labsynch.labseer.dto.ErrorMessageDTO;
import com.labsynch.labseer.dto.PlateStubDTO;
import com.labsynch.labseer.dto.PlateWellDTO;
import com.labsynch.labseer.dto.PreferredNameDTO;
import com.labsynch.labseer.dto.PreferredNameRequestDTO;
import com.labsynch.labseer.dto.PreferredNameResultsDTO;
import com.labsynch.labseer.dto.WellContentDTO;
import com.labsynch.labseer.dto.WellStubDTO;
import com.labsynch.labseer.exceptions.ErrorMessage;
import com.labsynch.labseer.exceptions.UniqueNameException;
import com.labsynch.labseer.utils.PropertiesUtilService;
import com.labsynch.labseer.utils.SimpleUtil;

import flexjson.JSONTokener;

@Service
@Transactional
public class ContainerServiceImpl implements ContainerService {

	private static final Logger logger = LoggerFactory.getLogger(ContainerServiceImpl.class);
	
	@Autowired
	private PropertiesUtilService propertiesUtilService;
	
	@Autowired
	private AutoLabelService autoLabelService;
	
	private JdbcTemplate jdbcTemplate;
	
	private DataSource dataSource;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
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
		if (logger.isDebugEnabled()) logger.debug("incoming meta container: " + container.toJson() + "\n");
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
	
	@Override
	public ArrayList<ErrorMessage> validateContainer(Container container) {
		ArrayList<ErrorMessage> errors = new ArrayList<ErrorMessage>();
		try{
			checkContainerUniqueName(container);
		} catch (UniqueNameException e){
			logger.error("Caught UniqueNameException validating Container: " + e.getMessage().toString() + " whole message  " + e.toString());
            ErrorMessage error = new ErrorMessage();
            error.setErrorLevel("error");
            error.setMessage(e.getMessage());
            errors.add(error);
		}
		
		return errors;
	}
	
	private void checkContainerUniqueName(Container container) throws UniqueNameException{
		Set<ContainerLabel> containerLabels = container.getLsLabels();
		for (ContainerLabel containerLabel : containerLabels){
			if (!containerLabel.isIgnored() && containerLabel.getLsType().equals("name")){
				String labelText = containerLabel.getLabelText();
				Collection<Container> foundContainers = new HashSet<Container>();
				Collection<ContainerLabel> foundContainerLabels = new HashSet<ContainerLabel>();
				try{
					foundContainerLabels = ContainerLabel.findContainerLabelsByLsTypeEqualsAndLabelTextEqualsAndIgnoredNot(containerLabel.getLsType(), labelText, true).getResultList();
				} catch (EmptyResultDataAccessException e){
					//found nothing
				}
				if (!foundContainerLabels.isEmpty()){
					for (ContainerLabel foundLabel : foundContainerLabels){
						foundContainers.add(foundLabel.getContainer());
					}
				}
				if (!foundContainers.isEmpty()){
					for (Container foundContainer: foundContainers){
						if (container.getId() == null || container.getId().compareTo(foundContainer.getId()) != 0){
							//we found an container that is not the same as the one being validated that has the same label
							throw new UniqueNameException("Container with the name "+labelText+" already exists! "+foundContainer.getCodeName());
						}
					}
				}
			}	
		}
		
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
		Predicate itxKind = cb.equal(secondItx.<String>get("lsKind"), "container_well");
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
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<Container> container = cq.from(Container.class);
		Join<Container, ContainerLabel> label = container.join("lsLabels");
		
		Predicate[] predicates = new Predicate[0];
		List<Predicate> predicateList = new ArrayList<Predicate>();
		Expression<String> containerLabelText = label.<String>get("labelText");
		Predicate containerLabelTextEquals = SimpleUtil.buildInPredicate(cb, containerLabelText, labelTexts);
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
		cq.multiselect(container.<String>get("codeName").alias("foundCodeName"), label.<String>get("labelText").alias("requestLabel"));
		Query q = em.createQuery(cq);
//		if (logger.isDebugEnabled()) logger.debug(q.unwrap(org.hibernate.Query.class).getQueryString());
		List<Tuple> resultTuples = q.getResultList();
		Map<String, List<String>> resultMap = new HashMap<String, List<String>>();
		for (Tuple tuple : resultTuples){
			String requestLabel = (String) tuple.get("requestLabel");
			String foundCodeName = (String) tuple.get("foundCodeName");
			if (!resultMap.containsKey(requestLabel)){
				List<String> foundCodeNames = new ArrayList<String>();
				foundCodeNames.add(foundCodeName);
				resultMap.put(requestLabel, foundCodeNames);
			}else{
				resultMap.get(requestLabel).add(foundCodeName);
			}
		}
		Collection<CodeLabelDTO> results = new ArrayList<CodeLabelDTO>();
		for (String requestLabel: labelTexts){
			CodeLabelDTO result = new CodeLabelDTO();
			result.setRequestLabel(requestLabel);
			if (resultMap.containsKey(requestLabel)){
				result.setFoundCodeNames(resultMap.get(requestLabel));
			}else{
				result.setFoundCodeNames(new ArrayList<String>());
			}
			results.add(result);
		}
		return results;
	}

	@Override
	public Collection<WellContentDTO> getWellContent(List<String> wellCodes) {
		if (wellCodes.isEmpty()) return new ArrayList<WellContentDTO>();
		EntityManager em = Container.entityManager();
		String queryString = "SELECT new com.labsynch.labseer.dto.WellContentDTO( ";
		queryString += "well.codeName, ";
		queryString += "wellName.labelText, ";
		queryString += "well.rowIndex, well.columnIndex, ";
		queryString += "well.recordedBy, well.recordedDate, ";
		queryString += "amountValue.numericValue, amountValue.unitKind,  ";
		queryString += " batchCodeValue.codeValue, batchCodeValue.concentration, batchCodeValue.concUnit,  ";
		queryString += " solventCodeValue.codeValue,  ";
		queryString += " physicalStateValue.codeValue  ";
		queryString += " )  ";
		queryString += " from Container as well ";
		queryString += makeInnerJoinHql("well.lsStates", "statusContentState", "status", "content");
		queryString += makeLeftJoinHql("well.lsLabels", "wellName", "name", "well name");
		queryString += makeLeftJoinHql("statusContentState.lsValues","amountValue", "numericValue","amount");
		queryString += makeLeftJoinHql("statusContentState.lsValues","batchCodeValue", "codeValue","batch code");
		queryString += makeLeftJoinHql("statusContentState.lsValues","solventCodeValue", "codeValue","solvent code");
		queryString += makeLeftJoinHql("statusContentState.lsValues","physicalStateValue", "codeValue","physical state");
		queryString += "where ( well.ignored <> true ) and ";
		Collection<Query> queries = SimpleUtil.splitHqlInClause(em, queryString, "well.codeName", wellCodes);
		Collection<WellContentDTO> results = new HashSet<WellContentDTO>();
		logger.debug("Querying for "+wellCodes.size()+" well codeNames");
		for (Query q : queries){
			if (logger.isDebugEnabled()) logger.debug(q.unwrap(org.hibernate.Query.class).getQueryString());
			results.addAll(q.getResultList());
		}
		//diff request with results to find codeNames that could not be found
		Map<String, WellContentDTO> resultMap = new HashMap<String, WellContentDTO>();
		HashSet<String> requestWellCodeNames = new HashSet<String>();
		requestWellCodeNames.addAll(wellCodes);
		HashSet<String> foundWellCodeNames = new HashSet<String>();
		for (WellContentDTO result : results){
			foundWellCodeNames.add(result.getContainerCodeName());
			resultMap.put(result.getContainerCodeName(), result);
		}
		requestWellCodeNames.removeAll(foundWellCodeNames);
		logger.debug(requestWellCodeNames.size()+" not found with content");
		if (!requestWellCodeNames.isEmpty()){
			//smaller query to look for empty wells that still do exist
			List<String> notFoundWellCodes = new ArrayList<String>();
			notFoundWellCodes.addAll(requestWellCodeNames);
			String emptyWellsQuery = "SELECT new com.labsynch.labseer.dto.WellContentDTO( ";
			emptyWellsQuery += "well.codeName, ";
			emptyWellsQuery += "wellName.labelText, ";
			emptyWellsQuery += "well.rowIndex, well.columnIndex, ";
			emptyWellsQuery += "well.recordedBy, well.recordedDate ";
			emptyWellsQuery += " )  ";
			emptyWellsQuery += " from Container as well ";
			emptyWellsQuery += makeLeftJoinHql("well.lsLabels", "wellName", "name", "well name");
			emptyWellsQuery += "where ( well.ignored <> true ) and ";
			Collection<Query> emptyWellQueries = SimpleUtil.splitHqlInClause(em, emptyWellsQuery, "well.codeName", notFoundWellCodes);
			Collection<WellContentDTO> emptyWellResults = new HashSet<WellContentDTO>();
			for (Query q : emptyWellQueries){
				emptyWellResults.addAll(q.getResultList());
			}
			for (WellContentDTO result : emptyWellResults){
				foundWellCodeNames.add(result.getContainerCodeName());
				resultMap.put(result.getContainerCodeName(), result);
			}
			requestWellCodeNames.removeAll(foundWellCodeNames);
			logger.debug(emptyWellResults.size()+" found with no content, "+requestWellCodeNames.size()+" not found at all");
			for (String notFoundCodeName : requestWellCodeNames){
				WellContentDTO notFoundDTO = new WellContentDTO();
				notFoundDTO.setContainerCodeName(notFoundCodeName);
				notFoundDTO.setLevel("error");
				notFoundDTO.setMessage("containerCodeName not found");
				results.add(notFoundDTO);
				resultMap.put(notFoundDTO.getContainerCodeName(), notFoundDTO);
			}
		}
		
		//sort results to match input wellCode order
		Collection<WellContentDTO> sortedResults = new ArrayList<WellContentDTO>();
		for (String wellCode : wellCodes){
			sortedResults.add(resultMap.get(wellCode));
		}
		return sortedResults;
	}
	
	private String makeLeftJoinHql(String table, String alias, String lsType, String lsKind){
		String queryString = "left join "+table+" as "+alias+" with "+alias+".lsType='"+lsType+"' and "+alias+".lsKind='"+lsKind+"' and "+alias+".ignored <> true ";
		return queryString;
	}
	private String makeInnerJoinHql(String table, String alias, String lsType, String lsKind){
		String queryString = "inner join "+table+" as "+alias+" with "+alias+".lsType='"+lsType+"' and "+alias+".lsKind='"+lsKind+"' and "+alias+".ignored <> true ";
		return queryString;
	}
	
	private String makeInnerJoinHql(String table, String alias, String lsType){
		String queryString = "inner join "+table+" as "+alias+" with "+alias+".lsType='"+lsType+"' and "+alias+".ignored <> true ";
		return queryString;
	}

	@Override
	public ContainerDependencyCheckDTO checkDependencies(Container container) {
		ContainerDependencyCheckDTO result = new ContainerDependencyCheckDTO();
		result.setQueryContainer(container);
		Collection<ItxContainerContainer> movedToItxs = ItxContainerContainer.findItxContainerContainersByLsTypeEqualsAndSecondContainerEquals("moved to", container).getResultList();
		if (movedToItxs != null && !movedToItxs.isEmpty()){
			for (ItxContainerContainer movedToItx : movedToItxs){
				Container contents = movedToItx.getFirstContainer();
				if (!contents.isIgnored()){
					result.getDependentCorpNames().add(contents.getCodeName());
				}
			}
		}
		Collection<ItxContainerContainer> hasMemberItxs = ItxContainerContainer.findItxContainerContainersByLsTypeEqualsAndFirstContainerEquals("has member", container).getResultList();
		Collection<Container> members = new HashSet<Container>();
		if (hasMemberItxs != null && !hasMemberItxs.isEmpty()){
			for (ItxContainerContainer hasMemberItx : hasMemberItxs){
				Container contents = hasMemberItx.getSecondContainer();
				if (!contents.isIgnored()){
					members.add(contents);
				}
			}
		}
		if (!result.getDependentCorpNames().isEmpty()) result.setLinkedDataExists(true);
		logger.debug("finished checking for linked containers. Now checking for dependent experimental data");
		result.checkForDependentData(result.getQueryContainer(), members);
		return result;
	}

	@Override
	public PreferredNameResultsDTO getCodeNameFromName(String containerType,
			String containerKind, String labelType, String labelKind,
			PreferredNameRequestDTO requestDTO) {
		logger.info("number of requests: " + requestDTO.getRequests().size());
		Collection<PreferredNameDTO> requests = requestDTO.getRequests();

		PreferredNameResultsDTO responseOutput = new PreferredNameResultsDTO();
		Collection<ErrorMessageDTO> errors = new HashSet<ErrorMessageDTO>();

		for (PreferredNameDTO request : requests){
			request.setPreferredName("");
			request.setReferenceName("");
			List<Container> lsThings = new ArrayList<Container>();
			if (labelType==null || labelKind==null || labelType.length()==0 || labelKind.length()==0){
				lsThings = Container.findContainerByLabelText(containerType, containerKind, request.getRequestName()).getResultList();

			}else{
				lsThings = Container.findContainerByLabelText(containerType, containerKind, labelType, labelKind, request.getRequestName()).getResultList();
			}
			if (lsThings.size() == 1){
				request.setPreferredName(pickBestLabel(lsThings.get(0)));
				request.setReferenceName(lsThings.get(0).getCodeName());
			} else if (lsThings.size() > 1){
				responseOutput.setError(true);
				ErrorMessageDTO error = new ErrorMessageDTO();
				error.setLevel("MULTIPLE RESULTS");
				error.setMessage("FOUND MULTIPLE LSTHINGS WITH THE SAME NAME: " + request.getRequestName() );	
				logger.error("FOUND MULTIPLE LSTHINGS WITH THE SAME NAME: " + request.getRequestName());
				errors.add(error);
			} else {
				try{
					Container codeNameMatch = Container.findContainerByCodeNameEquals(request.getRequestName());
					if (codeNameMatch.getLsKind().equals(containerKind) && codeNameMatch.getLsType().equals(containerType)){
						logger.info("Made it to the codeMatch");
						request.setPreferredName(pickBestLabel(codeNameMatch));
	 					request.setReferenceName(codeNameMatch.getCodeName());
					}else{
						logger.info("Did not find a Container with the requested name: " + request.getRequestName());
					}
				}catch (EmptyResultDataAccessException e){
					logger.info("Did not find a Container with the requested name: " + request.getRequestName());
				}
			}
		}
		responseOutput.setResults(requests);
		responseOutput.setErrorMessages(errors);

		return responseOutput;
	}
	
	@Override
	public String pickBestLabel(Container container) {
		Collection<ContainerLabel> labels = container.getLsLabels();
		if (labels.isEmpty()) return null;
		return ContainerLabel.pickBestLabel(labels).getLabelText();
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
				ItxContainerContainer movedTo = ItxContainerContainer.findItxContainerContainersByLsTypeEqualsAndLsKindEqualsAndFirstContainerEquals("moved to", "container_location", container).getSingleResult();
				movedTo.setIgnored(true);
				movedTo.setModifiedBy(dto.getModifiedBy());
				movedTo.setModifiedDate(dto.getModifiedDate());
				movedTo.merge();
			} catch(Exception e){
				result.setLevel("error");
				result.setMessage("Error finding 'moved to'/'container_location' interaction to ignore.");
				continue;
			}
			
			//create trash interaction to show container has been moved to the trash
			try{
				ItxContainerContainer trashItx = new ItxContainerContainer();
				trashItx.setLsType("moved to");
				trashItx.setLsKind("container_location");
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
			//ignore container since it is now in the trash -- decided NOT to ignore the container as of 04-05-2016. Still a valid container, just in the trash
//			try{
//				container.setIgnored(true);
//				container.setModifiedBy(dto.getModifiedBy());
//				container.setModifiedDate(dto.getModifiedDate());		
//				container.merge();
//			}catch (Exception e){
//				result.setLevel("error");
//				result.setMessage("Error ignoring container");
//				continue;
//			}
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
			trashState.setLsValues(new HashSet<ContainerValue>());
			
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
			newTrash.flush();
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
	
	@Override
	public PlateStubDTO createPlate(CreatePlateRequestDTO plateRequest) throws Exception {
		return createPlate(plateRequest, "plate");
	}
	
	@Override
	public PlateStubDTO createTube(CreatePlateRequestDTO plateRequest) throws Exception {
		return createPlate(plateRequest, "tube");
	}

	@Override
	public PlateStubDTO createPlate(CreatePlateRequestDTO plateRequest, String containerKind) throws Exception {
		Container definition;
		try{
			definition = Container.findContainerByCodeNameEquals(plateRequest.getDefinition());
		}catch (Exception e){
			throw new Exception("Error finding definition: "+plateRequest.getDefinition());
		}
		LsTransaction lsTransaction = new LsTransaction();
		lsTransaction.setRecordedDate(new Date());
		lsTransaction.persist();
		Container plate = new Container();
		plate.setCodeName(autoLabelService.getContainerCodeName());
		plate.setRecordedBy(plateRequest.getRecordedBy());
		plate.setRecordedDate(new Date());
		plate.setLsType("container");
		plate.setLsKind(containerKind);
		plate.setLsTransaction(lsTransaction.getId());
		List<Container> plateList = new ArrayList<Container>();
		plateList.add(plate);
		plate.setId(insertContainers(plateList).get(0));

		
		ContainerLabel plateBarcode = new ContainerLabel();
		plateBarcode.setRecordedBy(plate.getRecordedBy());
		plateBarcode.setRecordedDate(plate.getRecordedDate());
		plateBarcode.setLsType("barcode");
		plateBarcode.setLsKind("barcode");
		plateBarcode.setLabelText(plateRequest.getBarcode());
		plateBarcode.setLsTransaction(plate.getLsTransaction());
		plateBarcode.setContainer(plate);
		plate.getLsLabels().add(plateBarcode);
		List<ContainerLabel> plateBarcodeList = new ArrayList<ContainerLabel>();
		plateBarcodeList.add(plateBarcode);
		plateBarcode.setId(insertContainerLabels(plateBarcodeList).get(0));
		
		ContainerState metadataState = new ContainerState();
		metadataState.setRecordedBy(plate.getRecordedBy());
		metadataState.setRecordedDate(plate.getRecordedDate());
		metadataState.setLsType("metadata");
		metadataState.setLsKind("information");
		metadataState.setLsTransaction(plate.getLsTransaction());
		metadataState.setContainer(plate);
		List<ContainerState> plateStateList = new ArrayList<ContainerState>();
		plateStateList.add(metadataState);
		metadataState.setId(insertContainerStates(plateStateList).get(0));
		
		Set<ContainerValue> values = new HashSet<ContainerValue>();
		List<ContainerValue> plateValueList = new ArrayList<ContainerValue>();

		ContainerValue createdUser = new ContainerValue();
		createdUser.setRecordedBy(plate.getRecordedBy());
		createdUser.setRecordedDate(plate.getRecordedDate());
		createdUser.setLsType("codeValue");
		createdUser.setLsKind("created user");
		if (plateRequest.getCreatedUser() != null) createdUser.setCodeValue(plateRequest.getCreatedUser());
		else createdUser.setCodeValue(plate.getRecordedBy());
		createdUser.setLsTransaction(plate.getLsTransaction());
		createdUser.setLsState(metadataState);
		values.add(createdUser);
		plateValueList.add(createdUser);
		
		ContainerValue createdDate = new ContainerValue();
		createdDate.setRecordedBy(plate.getRecordedBy());
		createdDate.setRecordedDate(plate.getRecordedDate());
		createdDate.setLsType("dateValue");
		createdDate.setLsKind("created date");
		if (plateRequest.getCreatedDate() != null) createdDate.setDateValue(plateRequest.getCreatedDate());
		else createdDate.setDateValue(new Date());
		createdDate.setLsTransaction(plate.getLsTransaction());
		createdDate.setLsState(metadataState);
		values.add(createdDate);
		plateValueList.add(createdDate);
		
		ContainerValue description = new ContainerValue();
		description.setRecordedBy(plate.getRecordedBy());
		description.setRecordedDate(plate.getRecordedDate());
		description.setLsType("stringValue");
		description.setLsKind("description");
		description.setStringValue(plateRequest.getDescription());
		description.setLsTransaction(plate.getLsTransaction());
		description.setLsState(metadataState);
		
		if(plateRequest.getDescription() != null) {
			values.add(description);
			plateValueList.add(description);
		}
		metadataState.setLsValues(values);

		List<Long> valueIds = insertContainerValues(plateValueList);
		createdUser.setId(valueIds.get(0));
		createdDate.setId(valueIds.get(1));
		if(plateRequest.getDescription() != null) description.setId(valueIds.get(2));
		
		plate.getLsStates().add(metadataState);
		plate.getLsLabels().add(plateBarcode);
		ItxContainerContainer defines = makeItxContainerContainer("defines", definition, plate, plateRequest.getRecordedBy(), lsTransaction.getId());
		List<ItxContainerContainer> definesList = new ArrayList<ItxContainerContainer>();
		definesList.add(defines);
		insertItxContainerContainers(definesList);
		//getOrCreate location
		Container location = getOrCreateBench(plateRequest.getRecordedBy(), lsTransaction);
		//move plate to location
		ContainerLocationDTO moveRequest = new ContainerLocationDTO(location.getCodeName(), plate.getCodeName(), plateRequest.getBarcode());
		moveRequest.setModifiedBy(plateRequest.getRecordedBy());
		moveRequest.setModifiedDate(new Date());
		Collection<ContainerLocationDTO> moveRequests = new ArrayList<ContainerLocationDTO>();
		moveRequests.add(moveRequest);
		moveToLocation(moveRequests);
		//create and populate wells
		try{
			Map<String, List<?>> wellsAndNames = createWellsFromDefinition(plate, definition);
			List<Container> wells = (List<Container>)wellsAndNames.get("wells");
			List<ContainerLabel> wellNames = (List<ContainerLabel>) wellsAndNames.get("wellNames");
			//fill in recorded by for all wells to update
			Collection<WellContentDTO> wellDTOs = new ArrayList<WellContentDTO>();
			if (plateRequest.getWells() != null && !plateRequest.getWells().isEmpty()){
				for (WellContentDTO wellDTO: plateRequest.getWells()){
					if (wellDTO.getRecordedBy() == null) wellDTO.setRecordedBy(plate.getRecordedBy());
				}
				wellDTOs = lookUpWellCodesByWellNames(wells, wellNames, plateRequest.getWells());
			}
			if (plateRequest.getPhysicalState() != null || plateRequest.getBatchConcentrationUnits() != null){
				//fill in empty wellContentDTOs for new wells not passed in via plateRequest.getWells()
				Set<String> suppliedWellCodes = new HashSet<String>();
				if (!wellDTOs.isEmpty()){
					for (WellContentDTO suppliedWellDTO : wellDTOs){
						suppliedWellCodes.add(suppliedWellDTO.getContainerCodeName());
					}
				}
				Set<String> allWellCodes = new HashSet<String>();
				for (Container well : wells){
					allWellCodes.add(well.getCodeName());
				}
				allWellCodes.removeAll(suppliedWellCodes);
				for (String wellCode : allWellCodes){
					//populate wellContentDTO for unsupplied codes to pass in default physical state and/or batch concentration units
					WellContentDTO notSuppliedWell = new WellContentDTO();
					notSuppliedWell.setContainerCodeName(wellCode);
					notSuppliedWell.setPhysicalState(plateRequest.getPhysicalState());
					notSuppliedWell.setBatchConcUnits(plateRequest.getBatchConcentrationUnits());
					notSuppliedWell.setRecordedBy(plate.getRecordedBy());
					wellDTOs.add(notSuppliedWell);
				}
			}
			logger.debug("Size of wellDTOs is: "+wellDTOs.size());
			if (!wellDTOs.isEmpty()){
				try{
					logger.debug("Updating well content"+wellDTOs.size());
					updateWellContent(wellDTOs, true);
				}catch (Exception e){
					logger.error("Caught exception updating well content",e);
				}
			}
			//TODO: do something with templates
			PlateStubDTO result = new PlateStubDTO();
			result.setBarcode(plateRequest.getBarcode());
			result.setCodeName(plate.getCodeName());
			Collection<WellStubDTO> wellStubs = WellStubDTO.convertToWellStubDTOs(wells, wellNames);
			result.setWells(wellStubs);
			return result;
		}catch (Exception e){
			logger.error("Error creating wells from definition",e);
			throw new Exception("Error creating wells from definition",e);
		}
	}

	private Container getOrCreateBench(String recordedBy, LsTransaction lsTransaction) throws SQLException {
		PreferredNameDTO request = new PreferredNameDTO(recordedBy, null, null);
		Collection<PreferredNameDTO> requests = new ArrayList<PreferredNameDTO>();
		requests.add(request);
		PreferredNameRequestDTO requestDTO = new PreferredNameRequestDTO();
		requestDTO.setRequests(requests);
		PreferredNameResultsDTO resultsDTO = getCodeNameFromName("location", "default", "name", "common", requestDTO);
		String locationCodeName = resultsDTO.getResults().iterator().next().getReferenceName();
		if (locationCodeName != null && locationCodeName.length() > 0){
			Container location = Container.findContainerByCodeNameEquals(locationCodeName);
			return location;
		}else{
			logger.warn("bench location not found for "+recordedBy+". Creating new location.");
			Container bench = new Container();
			bench.setCodeName(autoLabelService.getContainerCodeName());
			bench.setRecordedBy(recordedBy);
			bench.setRecordedDate(new Date());
			bench.setLsType("location");
			bench.setLsKind("default");
			bench.setLsTransaction(lsTransaction.getId());
			List<Container> benchList = new ArrayList<Container>();
			benchList.add(bench);
			bench.setId(insertContainers(benchList).get(0));

			
			ContainerLabel benchName = new ContainerLabel();
			benchName.setRecordedBy(bench.getRecordedBy());
			benchName.setRecordedDate(bench.getRecordedDate());
			benchName.setLsType("name");
			benchName.setLsKind("common");
			benchName.setLabelText(recordedBy);
			benchName.setLsTransaction(bench.getLsTransaction());
			benchName.setContainer(bench);
			bench.getLsLabels().add(benchName);
			List<ContainerLabel> benchNameList = new ArrayList<ContainerLabel>();
			benchNameList.add(benchName);
			benchName.setId(insertContainerLabels(benchNameList).get(0));
			
			ContainerState metadataState = new ContainerState();
			metadataState.setRecordedBy(bench.getRecordedBy());
			metadataState.setRecordedDate(bench.getRecordedDate());
			metadataState.setLsType("metadata");
			metadataState.setLsKind("information");
			metadataState.setLsTransaction(bench.getLsTransaction());
			metadataState.setContainer(bench);
			List<ContainerState> benchStateList = new ArrayList<ContainerState>();
			benchStateList.add(metadataState);
			metadataState.setId(insertContainerStates(benchStateList).get(0));
			
			Set<ContainerValue> values = new HashSet<ContainerValue>();
			List<ContainerValue> benchValueList = new ArrayList<ContainerValue>();
			
			ContainerValue description = new ContainerValue();
			description.setRecordedBy(bench.getRecordedBy());
			description.setRecordedDate(bench.getRecordedDate());
			description.setLsType("stringValue");
			description.setLsKind("description");
			description.setStringValue(recordedBy+"'s bench");
			description.setLsTransaction(bench.getLsTransaction());
			description.setLsState(metadataState);
			values.add(description);
			benchValueList.add(description);
			
			ContainerValue createdUser = new ContainerValue();
			createdUser.setRecordedBy(bench.getRecordedBy());
			createdUser.setRecordedDate(bench.getRecordedDate());
			createdUser.setLsType("codeValue");
			createdUser.setLsKind("created user");
			createdUser.setCodeValue(recordedBy);
			createdUser.setLsTransaction(bench.getLsTransaction());
			createdUser.setLsState(metadataState);
			values.add(createdUser);
			benchValueList.add(createdUser);
			
			ContainerValue createdDate = new ContainerValue();
			createdDate.setRecordedBy(bench.getRecordedBy());
			createdDate.setRecordedDate(bench.getRecordedDate());
			createdDate.setLsType("dateValue");
			createdDate.setLsKind("created date");
			createdDate.setDateValue(new Date());
			createdDate.setLsTransaction(bench.getLsTransaction());
			createdDate.setLsState(metadataState);
			values.add(createdDate);
			benchValueList.add(createdDate);
			
			metadataState.setLsValues(values);
			List<Long> valueIds = insertContainerValues(benchValueList);
			description.setId(valueIds.get(0));
			createdUser.setId(valueIds.get(1));
			createdDate.setId(valueIds.get(2));
			
			bench.getLsStates().add(metadataState);
			bench.getLsLabels().add(benchName);
			return bench;
		}
		
	}

	private Collection<WellContentDTO> lookUpWellCodesByWellNames(List<Container> wells, List<ContainerLabel> wellNames, Collection<WellContentDTO> wellDTOs) {
		List<String> wellNameList = new ArrayList<String>();
		for (ContainerLabel wellName : wellNames){
			wellNameList.add(wellName.getLabelText());
		}
		for (WellContentDTO wellDTO : wellDTOs){
			String wellName = wellDTO.getWellName();
			List<String> possibleWellNames = new ArrayList<String>();
			if (SimpleUtil.isNumeric(wellName)){
				possibleWellNames.add(wellName);
			}else{
				//well name must be A1, A01, or A001 format
				String letterPart = wellName.replaceAll("[0-9]+", "");
				Integer numberPart = Integer.valueOf(wellName.replaceAll("[^0-9]+", ""));
				try{
					possibleWellNames.add(letterPart+Integer.toString(numberPart));
				}catch (Exception e){}
				try{
				possibleWellNames.add(letterPart+String.format("%02d", numberPart));
				}catch (Exception e){}
				try{
				possibleWellNames.add(letterPart+String.format("%03d", numberPart));
				}catch (Exception e){}
			}
			for (String possibleWellName : possibleWellNames){
				if (wellNameList.indexOf(possibleWellName) != -1){
					wellDTO.setContainerCodeName(wells.get(wellNameList.indexOf(possibleWellName)).getCodeName());
					break;
				}
			}
//			for (Container well : wells){
//				for (ContainerLabel wellLabel : well.getLsLabels()){
//					if (possibleWellNames.contains(wellLabel.getLabelText())){
//						wellDTO.setContainerCodeName(well.getCodeName());
//						break;
//					}
//				}
//			}
		}
		return wellDTOs;
	}

	public Map<String,List<?>> createWellsFromDefinition(Container plate,
			Container definition) throws Exception {
		List<Container> wells = new ArrayList<Container>();
		List<ContainerLabel> wellNames = new ArrayList<ContainerLabel>();
		List<ItxContainerContainer> itxContainerContainers = new ArrayList<ItxContainerContainer>();
		String wellFormat = getWellFormat(definition);
		Integer numberOfWells = getDefinitionIntegerValue(definition, "wells");
		Integer numberOfColumns = getDefinitionIntegerValue(definition, "columns");
		if (wellFormat != null){
			int n = 0;
			List<AutoLabelDTO> wellCodeNames = autoLabelService.getAutoLabels("material_container", "id_codeName", numberOfWells.longValue()); 
			logger.debug("wellFormat: "+wellFormat);
			logger.debug("numberOfWells: "+numberOfWells.toString());
			logger.debug("numberOfColumns: "+numberOfColumns.toString());
			while (n < numberOfWells){
				String letter = "";
				String number = "";
				int rowNum = n / numberOfColumns;
				int colNum = n % numberOfColumns;
				if (wellFormat.equals("1")) number = Integer.toString(n+1);
				else if (wellFormat.equals("A1")){
					letter = SimpleUtil.toAlphabetic(rowNum);
					number = Integer.toString(colNum+1);
				}
				else if (wellFormat.equals("A01")){
					letter = SimpleUtil.toAlphabetic(rowNum);
					number = String.format("%02d", colNum+1);
				}else if (wellFormat.equals("A001")){
					letter = SimpleUtil.toAlphabetic(rowNum);
					number = String.format("%03d", colNum+1);
				}
				//create well Container object
				Container well = new Container();
				well.setCodeName(wellCodeNames.get(n).getAutoLabel());
				well.setRecordedBy(plate.getRecordedBy());
				well.setRecordedDate(new Date());
				well.setLsType("well");
				well.setLsKind("default");
				well.setRowIndex(rowNum+1);
				well.setColumnIndex(colNum+1);
				well.setLsTransaction(plate.getLsTransaction());
				wells.add(well);
				ContainerLabel wellName = new ContainerLabel();
				wellName.setRecordedBy(plate.getRecordedBy());
				wellName.setRecordedDate(new Date());
				wellName.setLsType("name");
				wellName.setLsKind("well name");
				wellName.setLabelText(letter+number);
				wellName.setLsTransaction(plate.getLsTransaction());
				wellNames.add(wellName);
				
				//create ItxContainerContainer "has member" to link plate and well
				ItxContainerContainer hasMemberItx = makeItxContainerContainer("has member", plate, well, plate.getRecordedBy(), plate.getLsTransaction());
				itxContainerContainers.add(hasMemberItx);
				n++;
			}
			List<Long> wellIds = insertContainers(wells);
			int i=0;
			for (Long wellId : wellIds){
				Container fakeWell = new Container();
				fakeWell.setId(wellId);
				wellNames.get(i).setContainer(fakeWell);
				itxContainerContainers.get(i).setSecondContainer(fakeWell);
				i++;
			}
			insertContainerLabels(wellNames);
			insertItxContainerContainers(itxContainerContainers);
		} else{
			throw new Exception("Well format could not be found for definition: "+definition.getCodeName());
		}
		Map<String, List<?>> result = new HashMap<String, List<?>>();
		result.put("wells", wells);
		result.put("wellNames", wellNames);
		return result;
	}
	
	private String getWellFormat(Container definition){
		for (ContainerState state : definition.getLsStates()){
			if( state.getLsType().equals("constants") && state.getLsKind().equals("format")){
				for (ContainerValue value : state.getLsValues()){
					if (value.getLsKind().equals("subcontainer naming convention")){
						return value.getCodeValue();
					}
				}
			}
		}
		return null;
	}
	
	private Integer getDefinitionIntegerValue(Container definition, String lsKind){
		for (ContainerState state : definition.getLsStates()){
			if( state.getLsType().equals("constants") && state.getLsKind().equals("format")){
				logger.debug(state.getLsTypeAndKind());
				for (ContainerValue value : state.getLsValues()){
					logger.debug(value.getLsTypeAndKind());
					if (value.getLsKind().equals(lsKind)){
						return Integer.valueOf(value.getNumericValue().intValue());
					}
				}
			}
		}
		return null;
	}
	
	private BigDecimal getDefinitionNumericValue(Container definition, String lsKind){
		for (ContainerState state : definition.getLsStates()){
			if( state.getLsType().equals("constants") && state.getLsKind().equals("format")){
				logger.debug(state.getLsTypeAndKind());
				for (ContainerValue value : state.getLsValues()){
					logger.debug(value.getLsTypeAndKind());
					if (value.getLsKind().equals(lsKind)){
						return value.getNumericValue();
					}
				}
			}
		}
		return null;
	}
	
	private ItxContainerContainer makeItxContainerContainer(String lsType, Container firstContainer, Container secondContainer, String recordedBy, Long transactionId){
		ItxContainerContainer itxContainerContainer = new ItxContainerContainer();
		itxContainerContainer.setLsType(lsType);
		itxContainerContainer.setLsKind(firstContainer.getLsType()+"_"+secondContainer.getLsType());
		itxContainerContainer.setLsTransaction(transactionId);
		itxContainerContainer.setFirstContainer(firstContainer);
		itxContainerContainer.setSecondContainer(secondContainer);
		itxContainerContainer.setRecordedBy(recordedBy);
		itxContainerContainer.setRecordedDate(new Date());
		return itxContainerContainer;
	}

	@Override
	public Collection<WellContentDTO> getWellContentByPlateBarcode(
			String plateBarcode) {
		List<String> plateBarcodes = new ArrayList<String>();
		plateBarcodes.add(plateBarcode);
		Collection<PlateWellDTO> wellCodes = getWellCodesByPlateBarcodes(plateBarcodes);
		List<String> requestWellCodes = new ArrayList<String>();
		for (PlateWellDTO wellCode : wellCodes){
			requestWellCodes.add(wellCode.getWellCodeName());
		}
		Collection<WellContentDTO> results = getWellContent(requestWellCodes);
		return results;
	}

	@Override
	@Transactional
	public Collection<ContainerErrorMessageDTO> updateWellContent(
			Collection<WellContentDTO> wellsToUpdate, boolean copyPreviousValues) throws Exception {
		Long start = (new Date()).getTime();
		Collection<ContainerErrorMessageDTO> results = new ArrayList<ContainerErrorMessageDTO>();
		LsTransaction lsTransaction = new LsTransaction();
		lsTransaction.setRecordedDate(new Date());
		lsTransaction.persist();
		//get current well status in bulk, make into map
		List<String> wellCodes = new ArrayList<String>();
		for (WellContentDTO well : wellsToUpdate){
			wellCodes.add(well.getContainerCodeName());
		}
		Collection<WellContentDTO> currentWellContents = getWellContent(wellCodes);
		Map<String, WellContentDTO> currentWellContentsMap = new HashMap<String, WellContentDTO>();
		for (WellContentDTO currentWellContent : currentWellContents){
			currentWellContentsMap.put(currentWellContent.getContainerCodeName(), currentWellContent);
		}
		Long afterBuildMap = (new Date()).getTime();
		logger.debug("time to get content and build map: "+String.valueOf(afterBuildMap - start));
		logger.debug("time per well to get content and build map: "+String.valueOf((afterBuildMap - start)/currentWellContents.size()));
		Map<String, Long[]> wellCodeToWellIdStateId = getWellAndStatusContentStateIdsByCodeNames(wellCodes);
		Long afterGetIds = (new Date()).getTime();
		logger.debug("time to get ids and build map: "+String.valueOf(afterGetIds - afterBuildMap));
		logger.debug("time per well to get ids and build map: "+String.valueOf((afterGetIds - afterBuildMap)/wellCodeToWellIdStateId.size()));
		List<ContainerState> oldContentStates = new ArrayList<ContainerState>();
		List<ContainerState> newContentStates = new ArrayList<ContainerState>();
		List<List<ContainerValue>> newContentValues = new ArrayList<List<ContainerValue>>();
		Long beforeWells = (new Date()).getTime();
		for (WellContentDTO wellToUpdate : wellsToUpdate){
			ContainerErrorMessageDTO result = new ContainerErrorMessageDTO();
			result.setContainerCodeName(wellToUpdate.getContainerCodeName());
			if (wellCodeToWellIdStateId.get(wellToUpdate.getContainerCodeName()) == null){
				result.setLevel("error");
				result.setMessage("containerCodeName not found");
				results.add(result);
				continue;
			}else if (wellCodeToWellIdStateId.get(wellToUpdate.getContainerCodeName()).length == 2){
				ContainerState oldStatusContentState = new ContainerState();
				oldStatusContentState.setId(wellCodeToWellIdStateId.get(wellToUpdate.getContainerCodeName())[1]);
				oldStatusContentState.setIgnored(true);
				oldStatusContentState.setModifiedBy(wellToUpdate.getRecordedBy());
				oldStatusContentState.setModifiedDate(new Date());
				oldContentStates.add(oldStatusContentState);
			}
			Container fakeWell = new Container();
			fakeWell.setCodeName(wellToUpdate.getContainerCodeName());
			fakeWell.setId(wellCodeToWellIdStateId.get(wellToUpdate.getContainerCodeName())[0]);
			
			WellContentDTO oldContent = currentWellContentsMap.get(wellToUpdate.getContainerCodeName());
			BigDecimal oldAmount = oldContent.getAmount();
			String oldAmountUnits = oldContent.getAmountUnits();
			String oldBatchCode = oldContent.getBatchCode();
			Double oldBatchConcentration = oldContent.getBatchConcentration();
			String oldBatchConcUnits = oldContent.getBatchConcUnits();
			String oldPhysicalState = oldContent.getPhysicalState();
			String oldSolventCode = oldContent.getSolventCode();
			
//			Long extractedOldValues = (new Date()).getTime();
//			logger.debug("time to extract old values: "+String.valueOf(extractedOldValues - afterGetByCodeName));
			//create a new state and new values
			ContainerState newStatusContentState = new ContainerState();
			newStatusContentState.setRecordedBy(wellToUpdate.getRecordedBy());
			newStatusContentState.setRecordedDate(new Date());
			newStatusContentState.setLsType("status");
			newStatusContentState.setLsKind("content");
			newStatusContentState.setLsTransaction(lsTransaction.getId());
			newStatusContentState.setContainer(fakeWell);
			newContentStates.add(newStatusContentState);
			//fill in new values from DTO. if they are null, use the old value
			BigDecimal newAmount = wellToUpdate.getAmount();
			String newAmountUnits = wellToUpdate.getAmountUnits();
			String newBatchCode = wellToUpdate.getBatchCode();
			Double newBatchConcentration = wellToUpdate.getBatchConcentration();
			String newBatchConcUnits = wellToUpdate.getBatchConcUnits();
			String newPhysicalState = wellToUpdate.getPhysicalState();
			String newSolventCode = wellToUpdate.getSolventCode();
			if (copyPreviousValues){
				if (newAmount == null) newAmount = oldAmount;
				if (newAmountUnits == null) newAmountUnits = oldAmountUnits;
				if (newBatchCode == null) newBatchCode = oldBatchCode;
				if (newBatchConcentration == null) newBatchConcentration = oldBatchConcentration;
				if (newBatchConcUnits == null) newBatchConcUnits = oldBatchConcUnits;
				if (newPhysicalState == null) newPhysicalState = oldPhysicalState;
				if (newSolventCode == null) newSolventCode = oldSolventCode;
			}
			//create new values for attributes that exist
			List<ContainerValue> newValues = new ArrayList<ContainerValue>();
			if (newAmount != null || newAmountUnits != null){
				ContainerValue amount = new ContainerValue();
				amount.setRecordedBy(wellToUpdate.getRecordedBy());
				amount.setRecordedDate(new Date());
				amount.setLsType("numericValue");
				amount.setLsKind("amount");
				amount.setNumericValue(newAmount);
				amount.setUnitKind(newAmountUnits);
				amount.setLsTransaction(lsTransaction.getId());
				newValues.add(amount);
			}
			if (newBatchCode != null || newBatchConcentration != null || newBatchConcUnits != null){
				ContainerValue batchCode = new ContainerValue();
				batchCode.setRecordedBy(wellToUpdate.getRecordedBy());
				batchCode.setRecordedDate(new Date());
				batchCode.setLsType("codeValue");
				batchCode.setLsKind("batch code");
				batchCode.setCodeValue(newBatchCode);
				batchCode.setConcentration(newBatchConcentration);
				batchCode.setConcUnit(newBatchConcUnits);
				batchCode.setLsTransaction(lsTransaction.getId());
				newValues.add(batchCode);
			}
			if (newPhysicalState != null){
				ContainerValue physicalState = new ContainerValue();
				physicalState.setRecordedBy(wellToUpdate.getRecordedBy());
				physicalState.setRecordedDate(new Date());
				physicalState.setLsType("codeValue");
				physicalState.setLsKind("physical state");
				physicalState.setCodeValue(newPhysicalState);
				physicalState.setLsTransaction(lsTransaction.getId());
				newValues.add(physicalState);
			}
			if (newSolventCode != null){
				ContainerValue solventCode = new ContainerValue();
				solventCode.setRecordedBy(wellToUpdate.getRecordedBy());
				solventCode.setRecordedDate(new Date());
				solventCode.setLsType("codeValue");
				solventCode.setLsKind("solvent code");
				solventCode.setCodeValue(newSolventCode);
				solventCode.setLsTransaction(lsTransaction.getId());
				newValues.add(solventCode);
			}
			newContentValues.add(newValues);
			results.add(result);
		}
		Long finishedWells = (new Date()).getTime();
		logger.debug("total time for all wells: "+String.valueOf(finishedWells - beforeWells));
		logger.debug("time per well to prepare for inserts: "+String.valueOf((finishedWells - beforeWells)/wellsToUpdate.size()));
		//save everything in batches, being careful with ids
		try{
			Long beforeDbActions = (new Date()).getTime();
			logger.debug("states to ignore:" +oldContentStates.size());
			ignoreContainerStates(oldContentStates);
			Long afterIgnore = (new Date()).getTime();
			logger.debug("new states to create:" +newContentStates.size());
			List<Long> newStateIds = insertContainerStates(newContentStates);
			logger.debug("new state ids created:" +newStateIds.size());
			Long afterInsertState = (new Date()).getTime();
			int i = 0;
			logger.debug("groups of values to create:" +newContentValues.size());
			List<ContainerValue> flattenedNewValues = new ArrayList<ContainerValue>();
			for (Long newStateId : newStateIds){
				List<ContainerValue> values = newContentValues.get(i);
				ContainerState fakeState = new ContainerState();
				fakeState.setId(newStateId);
				for (ContainerValue value: values){
					value.setLsState(fakeState);
				}
				flattenedNewValues.addAll(values);
				i++;
			}
			logger.debug("total number of values to create:" +newContentValues.size());
			insertContainerValues(flattenedNewValues);
			Long afterInsertValue = (new Date()).getTime();
			logger.debug("time to ignore states: "+String.valueOf(afterIgnore - beforeDbActions));
			logger.debug("time to insert states: "+String.valueOf(afterInsertState - afterIgnore));
			logger.debug("time to insert values: "+String.valueOf(afterInsertValue - afterInsertState));

		}catch (SQLException e){
			logger.error("Caught error updating well content",e);
			if (e.getNextException() != null){
				e = e.getNextException();
				logger.error("Caught SQL Exception", e);
			}
		}

		Long finishedAll = (new Date()).getTime();
		logger.debug("total time: "+String.valueOf(finishedAll - start));
		return results;
	}
	
	private Map<String, Long[]> getWellAndStatusContentStateIdsByCodeNames(List<String> wellCodes) throws Exception{
		EntityManager em = ContainerValue.entityManager();
		String queryString = "SELECT new map( well.codeName as wellCode, well.id as wellId, state.id as stateId) "
				+ "from Container as well ";
		queryString += makeInnerJoinHql("well.lsStates", "state", "status", "content");
		queryString += "where ( well.ignored <> true ) and ";
		Collection<Query> queries = SimpleUtil.splitHqlInClause(em, queryString, "well.codeName", wellCodes);
		List<Map<String,Object>> results = new ArrayList<Map<String,Object>>();
		for (Query q : queries){
			results.addAll(q.getResultList());
		}
		//aggregate results
		Map<String, Long[]> resultMap = new HashMap<String, Long[]>();
		for (Map<String, Object> result : results){
			String containerCodeName = (String) result.get("wellCode");
			Long wellId = (Long) result.get("wellId");
			Long stateId = (Long) result.get("stateId");
			if (!resultMap.containsKey(containerCodeName)){
				Long[] wellIdStateId = new Long[2];
				wellIdStateId[0] = wellId;
				wellIdStateId[1] = stateId;
				resultMap.put(containerCodeName, wellIdStateId);
			}else{
				throw new Exception("Found more than one status content state for well.");
			}
		}
		//diff request with results to find codeNames that could not be found
		HashSet<String> requestCodeNames = new HashSet<String>();
		requestCodeNames.addAll(wellCodes);
		HashSet<String> foundCodeNames = new HashSet<String>();
		foundCodeNames.addAll(resultMap.keySet());
		requestCodeNames.removeAll(foundCodeNames);
		logger.debug(requestCodeNames.size()+" not found with content");
		if (!requestCodeNames.isEmpty()){
			List<String> notFoundWellCodes = new ArrayList<String>();
			notFoundWellCodes.addAll(requestCodeNames);
			String emptyWellQueryString = "SELECT new map( well.codeName as wellCode, well.id as wellId) "
					+ "from Container as well ";
			emptyWellQueryString += "where ( well.ignored <> true ) and ";
			Collection<Query> emptyWellQueries = SimpleUtil.splitHqlInClause(em, emptyWellQueryString, "well.codeName", notFoundWellCodes);
			List<Map<String,Object>> emptyWellResults = new ArrayList<Map<String,Object>>();
			for (Query q : emptyWellQueries){
				emptyWellResults.addAll(q.getResultList());
			}
			for (Map<String, Object> result : emptyWellResults){
				String containerCodeName = (String) result.get("wellCode");
				Long wellId = (Long) result.get("wellId");
				Long[] wellIdArray = new Long[1];
				wellIdArray[0] = wellId;
				foundCodeNames.add( containerCodeName);
				resultMap.put(containerCodeName, wellIdArray);
			}
			requestCodeNames.removeAll(foundCodeNames);
			logger.debug(emptyWellResults.size()+" found with no content, "+requestCodeNames.size()+" not found at all");
		}
		if (!requestCodeNames.isEmpty()){
			for (String notFoundCodeName : requestCodeNames){
				resultMap.put(notFoundCodeName, null);
			}
		}
		return resultMap;
	}

	@Override
	public PlateStubDTO getPlateTypeByPlateBarcode(String plateBarcode) {
		EntityManager em = Container.entityManager();
		String queryString = "SELECT new com.labsynch.labseer.dto.PlateStubDTO( ";
		queryString += "barcode.labelText, ";
		queryString += "plate.codeName, ";
		queryString += "plateType.codeValue  ";
		queryString += " )  ";
		queryString += " from Container as plate ";
		queryString += makeInnerJoinHql("plate.lsLabels", "barcode", "barcode", "barcode");
		queryString += makeLeftJoinHql("plate.lsStates", "metadataInformationState", "metadata", "information");
		queryString += makeLeftJoinHql("metadataInformationState.lsValues","plateType", "codeValue","plate type");
		queryString += "where barcode.labelText = :plateBarcode";
		TypedQuery<PlateStubDTO> q = em.createQuery(queryString, PlateStubDTO.class);
		q.setParameter("plateBarcode", plateBarcode);
		try{
			PlateStubDTO result = q.getSingleResult();
			return result;
		}catch(EmptyResultDataAccessException e){
			return null;
		}
	}
	
	@Override
	public Collection<ContainerErrorMessageDTO> getContainersByCodeNames(List<String> codeNames){
		if (codeNames.isEmpty()) return new ArrayList<ContainerErrorMessageDTO>();
		EntityManager em = Container.entityManager();
		String queryString = "SELECT new com.labsynch.labseer.dto.ContainerErrorMessageDTO( "
				+ "container.codeName, "
				+ "container )"
				+ " FROM Container container ";
		queryString += "where ( container.ignored <> true ) and ( ";
		Collection<Query> queries = SimpleUtil.splitHqlInClause(em, queryString, "container.codeName", codeNames);
		Collection<ContainerErrorMessageDTO> results = new HashSet<ContainerErrorMessageDTO>();
		for (Query q : queries){
			results.addAll(q.getResultList());
		}
		//diff request with results to find codeNames that could not be found
		HashSet<String> requestCodeNames = new HashSet<String>();
		requestCodeNames.addAll(codeNames);
		HashSet<String> foundCodeNames = new HashSet<String>();
		for (ContainerErrorMessageDTO result : results){
			foundCodeNames.add(result.getContainerCodeName());
		}
		requestCodeNames.removeAll(foundCodeNames);
		if (!requestCodeNames.isEmpty()){
			for (String notFoundCodeName : requestCodeNames){
				ContainerErrorMessageDTO notFoundDTO = new ContainerErrorMessageDTO();
				notFoundDTO.setContainerCodeName(notFoundCodeName);
				notFoundDTO.setLevel("error");
				notFoundDTO.setMessage("containerCodeName not found");
				results.add(notFoundDTO);
			}
		}
		
		return results;
	}

	@Override
	public Collection<ContainerErrorMessageDTO> getDefinitionContainersByContainerCodeNames(
			List<String> codeNames) {
		if (codeNames.isEmpty()) return new ArrayList<ContainerErrorMessageDTO>();
		EntityManager em = Container.entityManager();
		String queryString = "SELECT new com.labsynch.labseer.dto.ContainerErrorMessageDTO( "
				+ "container.codeName, "
				+ "definition )"
				+ " FROM Container container ";
		queryString += makeInnerJoinHql("container.firstContainers", "itx", "defines", "definition container_container");
		queryString += makeInnerJoinHql("itx.firstContainer", "definition", "definition container");
		queryString += "where ( container.ignored <> true ) and ( ";
		Collection<Query> queries = SimpleUtil.splitHqlInClause(em, queryString, "container.codeName", codeNames);
		Collection<ContainerErrorMessageDTO> results = new HashSet<ContainerErrorMessageDTO>();
		for (Query q : queries){
			results.addAll(q.getResultList());
		}
		//diff request with results to find codeNames that could not be found
		HashSet<String> requestCodeNames = new HashSet<String>();
		requestCodeNames.addAll(codeNames);
		HashSet<String> foundCodeNames = new HashSet<String>();
		for (ContainerErrorMessageDTO result : results){
			result.setDefinition(result.getContainer());
			result.setContainer(null);
			foundCodeNames.add(result.getContainerCodeName());
		}
		requestCodeNames.removeAll(foundCodeNames);
		if (!requestCodeNames.isEmpty()){
			for (String notFoundCodeName : requestCodeNames){
				ContainerErrorMessageDTO notFoundDTO = new ContainerErrorMessageDTO();
				notFoundDTO.setContainerCodeName(notFoundCodeName);
				notFoundDTO.setLevel("error");
				notFoundDTO.setMessage("containerCodeName not found");
				results.add(notFoundDTO);
			}
		}
		
		return results;
	}

	@Override
	public Collection<ContainerWellCodeDTO> getWellCodesByContainerCodes(
			List<String> codeNames) {
		if (codeNames.isEmpty()) return new ArrayList<ContainerWellCodeDTO>();
		EntityManager em = Container.entityManager();
		String queryString = "SELECT new map( "
				+ "container.codeName as containerCodeName, "
				+ "well.codeName as wellCodeName )"
				+ " FROM Container container ";
		queryString += makeInnerJoinHql("container.secondContainers", "itx", "has member");
		queryString += makeInnerJoinHql("itx.secondContainer", "well", "well");
		queryString += "where ( container.ignored <> true ) and ( well.ignored <> true) and ( ";
		Collection<Query> queries = SimpleUtil.splitHqlInClause(em, queryString, "container.codeName", codeNames);
		Collection<Map<String,String>> results = new HashSet<Map<String,String>>();
		for (Query q : queries){
			results.addAll(q.getResultList());
		}
		//aggregate results
		Map<String, List<String>> resultMap = new HashMap<String, List<String>>();
		for (Map<String, String> result : results){
			String containerCodeName = result.get("containerCodeName");
			String wellCodeName = result.get("wellCodeName");
			if (!resultMap.containsKey(containerCodeName)){
				List<String> foundWellCodes = new ArrayList<String>();
				foundWellCodes.add(wellCodeName);
				resultMap.put(containerCodeName, foundWellCodes);
			}else{
				resultMap.get(containerCodeName).add(wellCodeName);
			}
		}
		//diff request with results to find codeNames that could not be found
		HashSet<String> requestCodeNames = new HashSet<String>();
		requestCodeNames.addAll(codeNames);
		HashSet<String> foundCodeNames = new HashSet<String>();
		foundCodeNames.addAll(resultMap.keySet());
		requestCodeNames.removeAll(foundCodeNames);
		if (!requestCodeNames.isEmpty()){
			for (String notFoundCodeName : requestCodeNames){
				resultMap.put(notFoundCodeName, new ArrayList<String>());
			}
		}
		Collection<ContainerWellCodeDTO> sortedResults = new ArrayList<ContainerWellCodeDTO>();
		for (String requestCodeName : codeNames){
			ContainerWellCodeDTO result = new ContainerWellCodeDTO(requestCodeName, resultMap.get(requestCodeName));
			sortedResults.add(result);
		}
		return sortedResults;
	}

	@Override
	public Collection<ContainerLocationDTO> moveToLocation(
			Collection<ContainerLocationDTO> requests) {
		for (ContainerLocationDTO request : requests){
			//verify modifiedBy and modifiedDate are provided
			if (request.getModifiedBy() == null){
				request.setLevel("error");
				request.setMessage("modifiedBy must be provided");
				continue;
			}
			if (request.getModifiedDate() == null){
				request.setLevel("error");
				request.setMessage("modifiedDate must be provided");
				continue;
			}
			Container container;
			try{
				container = Container.findContainerByCodeNameEquals(request.getContainerCodeName());
			}catch (EmptyResultDataAccessException e){
				request.setLevel("error");
				request.setMessage("containerCodeName not found");
				continue;
			}
			Container location;
			try{
				location = Container.findContainerByCodeNameEquals(request.getLocationCodeName());
			}catch (EmptyResultDataAccessException e){
				request.setLevel("error");
				request.setMessage("locationCodeName not found");
				continue;
			}
			if (!location.getLsType().equals("location")){
				request.setLevel("error");
				request.setMessage("locationCodeName not a location");
				continue;
			}
			//container and location found, and location verified to be "location" type. Changing interactions.
			Collection<ItxContainerContainer> oldMovedToItxs = ItxContainerContainer.findItxContainerContainersByLsTypeEqualsAndFirstContainerEquals("moved to", container).getResultList();
			for (ItxContainerContainer oldMovedToItx : oldMovedToItxs){
				oldMovedToItx.setIgnored(true);
				oldMovedToItx.setModifiedBy(request.getModifiedBy());
				oldMovedToItx.setModifiedDate(request.getModifiedDate());
				oldMovedToItx.merge();
			}
			//create new "moved to" interaction
			ItxContainerContainer newMovedToItx = new ItxContainerContainer();
			newMovedToItx.setLsType("moved to");
			newMovedToItx.setLsKind(container.getLsType()+"_"+location.getLsType());
			newMovedToItx.setRecordedBy(request.getModifiedBy());
			newMovedToItx.setRecordedDate(request.getModifiedDate());
			newMovedToItx.setFirstContainer(container);
			newMovedToItx.setSecondContainer(location);
			newMovedToItx.persist();
		}
		return requests;
	}
	
	@Override
	@Transactional
	public List<Long> insertContainerStates(final List<ContainerState> states) throws SQLException{
		jdbcTemplate = new JdbcTemplate(dataSource);		
		final List<Long> idList = SimpleUtil.getIdsFromSequence(jdbcTemplate, "state_pkseq", states.size());
		String sql = "INSERT INTO CONTAINER_STATE"
				+ "(id, comments, deleted, ignored, ls_kind, ls_transaction, ls_type, ls_type_and_kind, modified_by, modified_date, recorded_by, recorded_date, version, container_id) VALUES"
				+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ContainerState state = states.get(i);
				ps.setLong(1, idList.get(i));
				ps.setString(2, state.getComments());
				ps.setBoolean(3, state.isDeleted());
				ps.setBoolean(4, state.isIgnored());
				ps.setString(5, state.getLsKind());
				ps.setLong(6, state.getLsTransaction());
				ps.setString(7, state.getLsType());
				ps.setString(8, state.getLsType()+"_"+state.getLsKind());
				ps.setString(9, state.getModifiedBy());
				if (state.getModifiedDate() != null) ps.setTimestamp(10, new java.sql.Timestamp(state.getModifiedDate().getTime()));
				else ps.setTimestamp(10, null);
				ps.setString(11, state.getRecordedBy());
				ps.setTimestamp(12, new java.sql.Timestamp(state.getRecordedDate().getTime()));
				ps.setInt(13, 0);
				ps.setLong(14, state.getContainer().getId());
			}
					
			@Override
			public int getBatchSize() {
				return states.size();
			}
		  });
		return idList;
	}
	
	@Override
	@Transactional
	public List<Long> insertContainers(final List<Container> containers) throws SQLException{
		jdbcTemplate = new JdbcTemplate(dataSource);		
		final List<Long> idList = SimpleUtil.getIdsFromSequence(jdbcTemplate, "thing_pkseq", containers.size());
		String sql = "INSERT INTO CONTAINER"
				+ "(id, code_name, deleted, ignored, ls_kind, ls_transaction, ls_type, ls_type_and_kind, modified_by, modified_date, recorded_by, recorded_date, version, column_index, location_id, row_index) VALUES"
				+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Container container = containers.get(i);
				ps.setLong(1, idList.get(i));
				ps.setString(2, container.getCodeName());
				ps.setBoolean(3, container.isDeleted());
				ps.setBoolean(4, container.isIgnored());
				ps.setString(5, container.getLsKind());
				ps.setLong(6, container.getLsTransaction());
				ps.setString(7, container.getLsType());
				ps.setString(8, container.getLsType()+"_"+container.getLsKind());
				ps.setString(9, container.getModifiedBy());
				if (container.getModifiedDate() != null) ps.setTimestamp(10, new java.sql.Timestamp(container.getModifiedDate().getTime()));
				else ps.setTimestamp(10, null);
				ps.setString(11, container.getRecordedBy());
				ps.setTimestamp(12, new java.sql.Timestamp(container.getRecordedDate().getTime()));
				ps.setInt(13, 0);
				if (container.getColumnIndex() != null) ps.setInt(14,container.getColumnIndex());
				else ps.setNull(14, java.sql.Types.INTEGER);
				if (container.getLocationId() != null) ps.setLong(15,container.getLocationId());
				else ps.setNull(15, java.sql.Types.BIGINT);
				if (container.getRowIndex() != null) ps.setInt(16,container.getRowIndex());
				else ps.setNull(16, java.sql.Types.INTEGER);
			}
					
			@Override
			public int getBatchSize() {
				return containers.size();
			}
		  });
		return idList;
	}
	
	@Override
	@Transactional
	public List<Long> insertContainerLabels(final List<ContainerLabel> labels) throws SQLException{
		jdbcTemplate = new JdbcTemplate(dataSource);		
		final List<Long> idList = SimpleUtil.getIdsFromSequence(jdbcTemplate, "label_pkseq", labels.size());
		String sql = "INSERT INTO CONTAINER_LABEL"
				+ "(id, deleted, ignored, image_file, label_text, ls_kind, ls_transaction, ls_type, ls_type_and_kind, modified_date, physically_labled, preferred, recorded_by, recorded_date, version, container_id) VALUES"
				+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ContainerLabel label = labels.get(i);
				ps.setLong(1, idList.get(i));
				ps.setBoolean(2, label.isDeleted());
				ps.setBoolean(3, label.isIgnored());
				ps.setString(4, label.getImageFile());
				ps.setString(5, label.getLabelText());
				ps.setString(6, label.getLsKind());
				ps.setLong(7, label.getLsTransaction());
				ps.setString(8, label.getLsType());
				ps.setString(9, label.getLsType()+"_"+label.getLsKind());
				if (label.getModifiedDate() != null) ps.setTimestamp(10, new java.sql.Timestamp(label.getModifiedDate().getTime()));
				else ps.setTimestamp(10, null);
				ps.setBoolean(11, label.isPhysicallyLabled());
				ps.setBoolean(12, label.isPreferred());
				ps.setString(13, label.getRecordedBy());
				ps.setTimestamp(14, new java.sql.Timestamp(label.getRecordedDate().getTime()));
				ps.setInt(15, 0);
				ps.setLong(16, label.getContainer().getId());
			}
					
			@Override
			public int getBatchSize() {
				return labels.size();
			}
		  });
		return idList;
	}
	
	@Override
	@Transactional
	public List<Long> insertItxContainerContainers(final List<ItxContainerContainer> itxContainerContainers) throws SQLException{
		jdbcTemplate = new JdbcTemplate(dataSource);		
		final List<Long> idList = SimpleUtil.getIdsFromSequence(jdbcTemplate, "thing_pkseq", itxContainerContainers.size());
		String sql = "INSERT INTO ITX_CONTAINER_CONTAINER"
				+ "(id, code_name, deleted, ignored, ls_kind, ls_transaction, ls_type, ls_type_and_kind, modified_by, modified_date, recorded_by, recorded_date, version, first_container_id, second_container_id) VALUES"
				+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ItxContainerContainer itxContainerContainer = itxContainerContainers.get(i);
				ps.setLong(1, idList.get(i));
				ps.setString(2, itxContainerContainer.getCodeName());
				ps.setBoolean(3, itxContainerContainer.isDeleted());
				ps.setBoolean(4, itxContainerContainer.isIgnored());
				ps.setString(5, itxContainerContainer.getLsKind());
				ps.setLong(6, itxContainerContainer.getLsTransaction());
				ps.setString(7, itxContainerContainer.getLsType());
				ps.setString(8, itxContainerContainer.getLsType()+"_"+itxContainerContainer.getLsKind());
				ps.setString(9, itxContainerContainer.getModifiedBy());
				if (itxContainerContainer.getModifiedDate() != null) ps.setTimestamp(10, new java.sql.Timestamp(itxContainerContainer.getModifiedDate().getTime()));
				else ps.setTimestamp(10, null);
				ps.setString(11, itxContainerContainer.getRecordedBy());
				ps.setTimestamp(12, new java.sql.Timestamp(itxContainerContainer.getRecordedDate().getTime()));
				ps.setInt(13, 0);
				ps.setLong(14, itxContainerContainer.getFirstContainer().getId());
				ps.setLong(15, itxContainerContainer.getSecondContainer().getId());
			}
					
			@Override
			public int getBatchSize() {
				return itxContainerContainers.size();
			}
		  });
		return idList;
	}
	
	@Override
	@Transactional
	public void ignoreContainerStates(final List<ContainerState> states) throws SQLException{
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "UPDATE CONTAINER_STATE "
				+ "set ignored = ?, modified_by = ?, modified_date = ?, version = version + 1 WHERE id = ?";
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ContainerState state = states.get(i);
				ps.setBoolean(1, true);
				ps.setString(2, state.getModifiedBy());
				if (state.getModifiedDate() != null) ps.setTimestamp(3, new java.sql.Timestamp(state.getModifiedDate().getTime()));
				else ps.setTimestamp(3, null);
				ps.setLong(4, state.getId());
			}
					
			@Override
			public int getBatchSize() {
				return states.size();
			}
		  });
	}
	
	@Override
	@Transactional
	public List<Long> insertContainerValues(final List<ContainerValue> values) throws SQLException{
		jdbcTemplate = new JdbcTemplate(dataSource);
		final List<Long> idList = SimpleUtil.getIdsFromSequence(jdbcTemplate, "value_pkseq", values.size());
		String sql = "INSERT INTO CONTAINER_VALUE "
				+ "(id, blob_value, clob_value, code_kind, code_origin, code_type, code_type_and_kind, code_value, comments, conc_unit, concentration, "
				+ " date_value, deleted, file_value, ignored, ls_kind, ls_transaction, ls_type, ls_type_and_kind, modified_by, modified_date, "
				+ "number_of_replicates, numeric_value, operator_kind, operator_type, operator_type_and_kind, public_data, recorded_by, recorded_date, "
				+ "sig_figs, string_value, uncertainty, uncertainty_type, unit_kind, unit_type, url_value, version, container_state_id) VALUES "
				+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ "?, ?, ?, ?, ?, ?, ?, ?, "
				+ "?, ?, ?, ?, ?, ?, ?, ?, ?)";
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ContainerValue value = values.get(i);
				ps.setLong(1,idList.get(i));
				ps.setBytes(2,value.getBlobValue());
				ps.setString(3,value.getClobValue());
//				ps.setNull(2,  java.sql.Types.BLOB);
//				ps.setNull(3,  java.sql.Types.CLOB);
				//TODO: fix blobs and clobs
				ps.setString(4,value.getCodeKind());
				ps.setString(5,value.getCodeOrigin());
				ps.setString(6,value.getCodeType());
				ps.setString(7,value.getCodeTypeAndKind());
				ps.setString(8,value.getCodeValue());
				ps.setString(9,value.getComments());
				ps.setString(10,value.getConcUnit());
				if (value.getConcentration() != null) ps.setDouble(11,value.getConcentration());
				else ps.setNull(11, java.sql.Types.DOUBLE);
				if (value.getDateValue() != null) ps.setTimestamp(12,new java.sql.Timestamp(value.getDateValue().getTime()));
				else ps.setTimestamp(12, null);
				ps.setBoolean(13,value.getDeleted());
				ps.setString(14,value.getFileValue());
				ps.setBoolean(15,value.getIgnored());
				ps.setString(16,value.getLsKind());
				ps.setLong(17,value.getLsTransaction());
				ps.setString(18,value.getLsType());
				ps.setString(19,value.getLsTypeAndKind());
				ps.setString(20,value.getModifiedBy());
				if (value.getModifiedDate() != null) ps.setTimestamp(21,new java.sql.Timestamp(value.getModifiedDate().getTime()));
				else ps.setTimestamp(21, null);
				if (value.getNumberOfReplicates() != null) ps.setInt(22,value.getNumberOfReplicates());
				else ps.setNull(22, java.sql.Types.INTEGER);
				ps.setBigDecimal(23,value.getNumericValue());
				ps.setString(24,value.getOperatorKind());
				ps.setString(25,value.getOperatorType());
				ps.setString(26,value.getOperatorTypeAndKind());
				ps.setBoolean(27,value.getPublicData());
				ps.setString(28,value.getRecordedBy());
				if (value.getRecordedDate() != null) ps.setTimestamp(29,new java.sql.Timestamp(value.getRecordedDate().getTime()));
				else ps.setTimestamp(29, null);
				if (value.getSigFigs() != null) ps.setInt(30,value.getSigFigs());
				else ps.setNull(30, java.sql.Types.INTEGER);
				ps.setString(31,value.getStringValue());
				ps.setBigDecimal(32,value.getUncertainty());
				ps.setString(33,value.getUncertaintyType());
				ps.setString(34,value.getUnitKind());
				ps.setString(35,value.getUnitType());
				ps.setString(36,value.getUrlValue());
				ps.setInt(37, 0);
				ps.setLong(38,value.getLsState().getId());
			}
					
			@Override
			public int getBatchSize() {
				return values.size();
			}
		  });
		return idList;
	}
	
	@Override
	@Transactional
	public List<CodeTableDTO> convertToCodeTables(List<Container> containers){
		List<CodeTableDTO> codeTables = new ArrayList<CodeTableDTO>();
		for (Container container: containers){
			CodeTableDTO codeTable = new CodeTableDTO();
			codeTable.setCode(container.getCodeName());
			codeTable.setName(pickBestLabel(container));
			codeTable.setIgnored(container.isIgnored());
			codeTables.add(codeTable);
		}
		return codeTables;
	}

	@Override
	@Transactional
	public Collection<Container> searchContainers(
			ContainerSearchRequestDTO searchRequest) {
		List<Container> containerList = new ArrayList<Container>();
		EntityManager em = Container.entityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Container> criteria = cb.createQuery(Container.class);
		Root<Container> containerRoot = criteria.from(Container.class);
		
		criteria.select(containerRoot);
		criteria.distinct(true);
		Predicate[] predicates = new Predicate[0];
		List<Predicate> predicateList = new ArrayList<Predicate>();
		
		//barcode
		if (searchRequest.getBarcode()!= null){
			String barcode = searchRequest.getBarcode().replaceAll("\\*", "%");
			Join<Container, ContainerLabel> containerLabel = containerRoot.join("lsLabels");
			Predicate barcodeType = cb.equal(containerLabel.<String>get("lsType"), "barcode");
			Predicate barcodeKind = cb.equal(containerLabel.<String>get("lsKind"), "barcode");
			Predicate barcodeLabelPredicate = cb.like(containerLabel.<String>get("labelText"), '%'+barcode+'%');
			Predicate containerLabelNotIgnored = cb.not(containerLabel.<Boolean>get("ignored"));
			Predicate barcodePredicate = cb.and(barcodeType,
					barcodeKind,
					barcodeLabelPredicate, 
					containerLabelNotIgnored 
					);
			predicateList.add(barcodePredicate);
		}
		//description
		if (searchRequest.getDescription() != null || searchRequest.getCreatedUser() != null || searchRequest.getStatus()!= null || searchRequest.getType() != null){
			Join<Container, ContainerState> containerState = containerRoot.join("lsStates");
			Predicate stateType = cb.equal(containerState.<String>get("lsType"), "metadata");
			Predicate stateKind = cb.equal(containerState.<String>get("lsKind"), "information");
			Predicate stateNotIgnored = cb.not(containerState.<Boolean>get("ignored"));
			Predicate statePredicate = cb.and(stateType, stateKind, stateNotIgnored);
			predicateList.add(statePredicate);
			if (searchRequest.getDescription() != null){
				String description  = searchRequest.getDescription().replaceAll("\\*", "%");;
				Join<ContainerState, ContainerValue> descriptionValue = containerState.join("lsValues");
				Predicate descriptionStringValue = cb.like(descriptionValue.<String>get("stringValue"), '%'+description+'%');
				Predicate descriptionType = cb.equal(descriptionValue.<String>get("lsType"), "stringValue");
				Predicate descriptionKind = cb.equal(descriptionValue.<String>get("lsKind"), "description");
				Predicate descriptionNotIgnored = cb.not(descriptionValue.<Boolean>get("ignored"));
				Predicate descriptionPredicate = cb.and(descriptionStringValue,descriptionType, descriptionKind, descriptionNotIgnored);
				predicateList.add(descriptionPredicate);
			}
			if (searchRequest.getCreatedUser() != null){
				String createdUser  = searchRequest.getCreatedUser().replaceAll("\\*", "%");;
				Join<ContainerState, ContainerValue> createdUserValue = containerState.join("lsValues");
				Predicate createdUserStringValue = cb.like(createdUserValue.<String>get("codeValue"), '%'+createdUser+'%');
				Predicate createdUserType = cb.equal(createdUserValue.<String>get("lsType"), "codeValue");
				Predicate createdUserKind = cb.equal(createdUserValue.<String>get("lsKind"), "created user");
				Predicate createdUserNotIgnored = cb.not(createdUserValue.<Boolean>get("ignored"));
				Predicate createdUserPredicate = cb.and(createdUserStringValue,createdUserType, createdUserKind, createdUserNotIgnored);
				predicateList.add(createdUserPredicate);
			}
			if (searchRequest.getStatus() != null){
				String status  = searchRequest.getStatus().replaceAll("\\*", "%");;
				Join<ContainerState, ContainerValue> statusValue = containerState.join("lsValues");
				Predicate statusStringValue = cb.like(statusValue.<String>get("codeValue"), '%'+status+'%');
				Predicate statusType = cb.equal(statusValue.<String>get("lsType"), "codeValue");
				Predicate statusKind = cb.equal(statusValue.<String>get("lsKind"), "status");
				Predicate statusNotIgnored = cb.not(statusValue.<Boolean>get("ignored"));
				Predicate statusPredicate = cb.and(statusStringValue,statusType, statusKind, statusNotIgnored);
				predicateList.add(statusPredicate);
			}
			if (searchRequest.getType() != null){
				String type  = searchRequest.getType().replaceAll("\\*", "%");;
				Join<ContainerState, ContainerValue> typeValue = containerState.join("lsValues");
				Predicate typeStringValue = cb.like(typeValue.<String>get("codeValue"), '%'+type+'%');
				Predicate typeType = cb.equal(typeValue.<String>get("lsType"), "codeValue");
				Predicate typeKind = cb.equal(typeValue.<String>get("lsKind"), "type");
				Predicate typeNotIgnored = cb.not(typeValue.<Boolean>get("ignored"));
				Predicate typePredicate = cb.and(typeStringValue,typeType, typeKind, typeNotIgnored);
				predicateList.add(typePredicate);
			}
		}
		//definition
		if (searchRequest.getDefinition() != null){
			Join<Container, ItxContainerContainer> definesItx = containerRoot.join("firstContainers");
			Join<ItxContainerContainer, Container> definitionContainer = definesItx.join("firstContainer");
			Predicate definesType = cb.equal(definesItx.<String>get("lsType"), "defines");
			Predicate definesKind = cb.equal(definesItx.<String>get("lsKind"), "definition container_container");
			Predicate definitionCode = cb.equal(definitionContainer.<String>get("codeName"), searchRequest.getDefinition());
			Predicate definitionPredicate = cb.and(definesType, definesKind, definitionCode);
			predicateList.add(definitionPredicate);
		}
		//lsType
		if (searchRequest.getLsType() != null){
			Predicate containerType = cb.equal(containerRoot.<String>get("lsType"),searchRequest.getLsType());
			predicateList.add(containerType);
		}
		//lsKind
		if (searchRequest.getLsKind() != null){
			Predicate containerKind = cb.equal(containerRoot.<String>get("lsKind"),searchRequest.getLsKind());
			predicateList.add(containerKind);
		}
		////requestId
		//container not ignored
		Predicate containerNotIgnored = cb.not(containerRoot.<Boolean>get("ignored"));
		predicateList.add(containerNotIgnored);
		predicates = predicateList.toArray(predicates);
		criteria.where(cb.and(predicates));
		TypedQuery<Container> q = em.createQuery(criteria);
		q.setMaxResults(propertiesUtilService.getContainerInventorySearchMaxResult());
		containerList = q.getResultList();
		return containerList;
	}

	@Override
	public Collection<String> getContainersByContainerValue(
			ContainerValueRequestDTO requestDTO) throws Exception {
		//validate request
		if (requestDTO.getContainerType() == null) throw new Exception("Container type must be specified");
		if (requestDTO.getContainerKind() == null) throw new Exception("Container kind must be specified");
		if (requestDTO.getStateType() == null) throw new Exception("State type must be specified");
		if (requestDTO.getStateKind() == null) throw new Exception("State kind must be specified");
		if (requestDTO.getValueType() == null) throw new Exception("Value type must be specified");
		if (requestDTO.getValueKind() == null) throw new Exception("Value kind must be specified");
		else if (!requestDTO.getValueType().equals("stringValue") && !requestDTO.getValueType().equals("codeValue")) throw new Exception("Only value types of stringValue or codeValue are accepted"); 
		if (requestDTO.getValue() == null) throw new Exception("Value must be specified");
		
		EntityManager em = Container.entityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> criteria = cb.createQuery(String.class);
		Root<Container> containerRoot = criteria.from(Container.class);
		criteria.select(containerRoot.<String>get("codeName"));
		criteria.distinct(true);
		Predicate[] predicates = new Predicate[0];
		List<Predicate> predicateList = new ArrayList<Predicate>();
		
		Predicate containerType = cb.equal(containerRoot.<String>get("lsType"), requestDTO.getContainerType());
		Predicate containerKind = cb.equal(containerRoot.<String>get("lsKind"), requestDTO.getContainerKind());
		Predicate containerNotIgnored = cb.not(containerRoot.<Boolean>get("ignored"));
		Predicate containerPredicate = cb.and(containerType, containerKind, containerNotIgnored);
		predicateList.add(containerPredicate);
		
		Join<Container, ContainerState> containerState = containerRoot.join("lsStates");
		Predicate stateType = cb.equal(containerState.<String>get("lsType"), requestDTO.getStateType());
		Predicate stateKind = cb.equal(containerState.<String>get("lsKind"), requestDTO.getStateKind());
		Predicate stateNotIgnored = cb.not(containerState.<Boolean>get("ignored"));
		Predicate statePredicate = cb.and(stateType, stateKind, stateNotIgnored);
		predicateList.add(statePredicate);
		
		Join<ContainerState, ContainerValue> containerValue = containerState.join("lsValues");
		Predicate valueType = cb.equal(containerValue.<String>get("lsType"), requestDTO.getValueType());
		Predicate valueKind = cb.equal(containerValue.<String>get("lsKind"), requestDTO.getValueKind());
		Predicate valueNotIgnored = cb.not(containerValue.<Boolean>get("ignored"));
		if (requestDTO.getValueType().equals("stringValue")){
			Predicate stringValue = cb.equal(containerValue.<String>get("stringValue"), requestDTO.getValue());
			Predicate valuePredicate = cb.and(valueType,valueKind, valueNotIgnored, stringValue);
			predicateList.add(valuePredicate);
		}else if(requestDTO.getValueType().equals("codeValue")){
			Predicate codeValue = cb.equal(containerValue.<String>get("codeValue"), requestDTO.getValue());
			Predicate valuePredicate = cb.and(valueType,valueKind, valueNotIgnored, codeValue);
			predicateList.add(valuePredicate);
		}
		predicates = predicateList.toArray(predicates);
		criteria.where(cb.and(predicates));
		TypedQuery<String> q = em.createQuery(criteria);
		
		return q.getResultList();
	}


}
