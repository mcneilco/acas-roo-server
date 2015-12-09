package com.labsynch.labseer.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

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
import com.labsynch.labseer.dto.ContainerCodeDTO;
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
		newContainer.persist();
		if (container.getLsLabels() != null){
			for(ContainerLabel containerLabel : container.getLsLabels()){
				ContainerLabel newContainerLabel = new ContainerLabel(containerLabel);
				newContainerLabel.setContainer(newContainer);
				logger.debug("here is the newcontainerLabel before save: " + newContainerLabel.toJson());
				newContainerLabel.persist();	
			}			
		} else {
			logger.debug("No container labels to save");	
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
//		logger.debug(q.unwrap(org.hibernate.Query.class).getQueryString());
		Collection<ContainerLocationDTO> results = q.getResultList();
		return results;
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
//		logger.debug(q.unwrap(org.hibernate.Query.class).getQueryString());
		Collection<PlateWellDTO> results = q.getResultList();
		return results;
	}

	@Override
	public Collection<ContainerCodeDTO> getContainerCodesByLabels(
			List<String> labelTexts, String containerType, String containerKind, String labelType, String labelKind) {
		EntityManager em = Container.entityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ContainerCodeDTO> cq = cb.createQuery(ContainerCodeDTO.class);
		Root<Container> container = cq.from(Container.class);
		Join<Container, ContainerLabel> barcode = container.join("lsLabels");
		Join<Container, ContainerLabel> containerLabel = container.join("lsLabels");
		
		Predicate[] predicates = new Predicate[0];
		List<Predicate> predicateList = new ArrayList<Predicate>();
		Expression<String> containerLabelText = containerLabel.<String>get("labelText");
		Predicate containerLabelTextEquals = containerLabelText.in(labelTexts);
		predicateList.add(containerLabelTextEquals);
		Predicate barcodeLsType = cb.equal(barcode.<String>get("lsType"), "barcode");
		Predicate barcodeLsKind = cb.equal(barcode.<String>get("lsKind"), "barcode");
		predicateList.add(barcodeLsType);
		predicateList.add(barcodeLsKind);
		
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
			Predicate containerLabelTypeEquals = cb.equal(containerLabel.<String>get("lsType"), labelType);
			predicateList.add(containerLabelTypeEquals);
		}
		if (labelKind != null && labelKind.length()>0){
			Predicate containerLabelKindEquals = cb.equal(containerLabel.<String>get("lsKind"), labelKind);
			predicateList.add(containerLabelKindEquals);
		}
		//not ignored predicates
		Predicate containerNotIgnored =  cb.not(container.<Boolean>get("ignored"));
		Predicate barcodeNotIgnored =  cb.not(barcode.<Boolean>get("ignored"));
		Predicate containerLabelNotIgnored =  cb.not(barcode.<Boolean>get("ignored"));		
		predicateList.add(containerNotIgnored);
		predicateList.add(barcodeNotIgnored);
		predicateList.add(containerLabelNotIgnored);
		
		predicates = predicateList.toArray(predicates);
		cq.where(cb.and(predicates));
		cq.multiselect(container.<String>get("codeName"), barcode.<String>get("labelText"));
		TypedQuery<ContainerCodeDTO> q = em.createQuery(cq);
//		logger.debug(q.unwrap(org.hibernate.Query.class).getQueryString());
		Collection<ContainerCodeDTO> results = q.getResultList();
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
//		logger.debug(q.unwrap(org.hibernate.Query.class).getQueryString());
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
