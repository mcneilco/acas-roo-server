package com.labsynch.labseer.service;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import oracle.sql.DATE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.DDictValue;
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ExperimentValue;
import com.labsynch.labseer.domain.LsTag;
import com.labsynch.labseer.domain.Protocol;
import com.labsynch.labseer.domain.ProtocolLabel;
import com.labsynch.labseer.domain.ProtocolState;
import com.labsynch.labseer.domain.ProtocolValue;
import com.labsynch.labseer.dto.AutoLabelDTO;
import com.labsynch.labseer.dto.StringCollectionDTO;
import com.labsynch.labseer.utils.PropertiesUtilService;

@Service
public class ProtocolServiceImpl implements ProtocolService {

	private static final Logger logger = LoggerFactory.getLogger(ProtocolServiceImpl.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;
	
	@Autowired
	private AutoLabelService autoLabelService;


	//    @PostConstruct
	//    public void init(){
	//    	logger.info("calling the appSetting 201 init method upon startup");
	//    	ApplicationSetting appSet = new ApplicationSetting();
	//    	appSet.setPropName("testing201");
	//    	appSet.setPropValue("testingValue201");
	//    	appSet.persist();
	//    }

	@Override
	public Protocol saveLsProtocol(Protocol protocol){
		logger.debug("incoming meta protocol: " + protocol.toJson() + "\n");
		Protocol newProtocol = new Protocol(protocol);
		if (newProtocol.getCodeName() == null){
			
			String thingTypeAndKind = "document_protocol";
			String labelTypeAndKind = "id_codeName";
			Long numberOfLabels = 1L;
			List<AutoLabelDTO> labels = autoLabelService.getAutoLabels(thingTypeAndKind, labelTypeAndKind, numberOfLabels );
			newProtocol.setCodeName(labels.get(0).getAutoLabel());
		}
		
		
		newProtocol.persist();
		logger.debug("persisted the newProtocol: " + newProtocol.toJson() + "\n");
		if (protocol.getLsLabels() != null){
			Set<ProtocolLabel> lsLabels = new HashSet<ProtocolLabel>();
			for(ProtocolLabel protocolLabel : protocol.getLsLabels()){
				ProtocolLabel newProtocolLabel = new ProtocolLabel(protocolLabel);
				newProtocolLabel.setProtocol(newProtocol);
				logger.info("here is the newProtocolLabel before save: " + newProtocolLabel.toJson());
				newProtocolLabel.persist();	
				lsLabels.add(newProtocolLabel);
			}	
			newProtocol.setLsLabels(lsLabels);
		} else {
			logger.debug("No protocol labels to save");	
		}

		if (protocol.getLsStates() != null){
			Set<ProtocolState> lsStates = new HashSet<ProtocolState>();
			for(ProtocolState protocolState : protocol.getLsStates()){
				ProtocolState newProtocolState = new ProtocolState(protocolState);
				newProtocolState.setProtocol(newProtocol);
				newProtocolState.persist();
				if (protocolState.getLsValues() != null){
					Set<ProtocolValue> lsValues = new HashSet<ProtocolValue>();
					for(ProtocolValue protocolValue : protocolState.getLsValues()){
						protocolValue.setLsState(newProtocolState);
						protocolValue.persist();
						lsValues.add(protocolValue);
						logger.debug("persisted the protocolValue: " + protocolValue.toJson());
					}				
					newProtocolState.setLsValues(lsValues);
				} else {
					logger.debug("No protocol values to save");
				}
				lsStates.add(newProtocolState);
			}
			newProtocol.setLsStates(lsStates);
		}

		return newProtocol;
	}

	@Override
	public Protocol updateProtocol(Protocol protocol){
		logger.debug("incoming meta protocol: " + protocol.toJson() + "\n");
		Protocol updatedProtocol = Protocol.update(protocol);
		if (protocol.getLsLabels() != null){
			for(ProtocolLabel protocolLabel : protocol.getLsLabels()){
				if (protocolLabel.getId() == null){
					ProtocolLabel newProtocolLabel = new ProtocolLabel(protocolLabel);
					newProtocolLabel.setProtocol(updatedProtocol);
					newProtocolLabel.persist();						
				} else {
					ProtocolLabel.update(protocolLabel);
				}
			}	
		} else {
			logger.debug("No protocol labels to save");	
		}

		if (protocol.getLsStates() != null){
			for(ProtocolState protocolState : protocol.getLsStates()){
				if (protocolState.getId() == null){
					ProtocolState newProtocolState = new ProtocolState(protocolState);
					newProtocolState.setProtocol(updatedProtocol);
					newProtocolState.persist();		
					protocolState.setId(newProtocolState.getId());
				} else {
					ProtocolState updatedProtocolState = ProtocolState.update(protocolState);
					logger.debug("updatedProtocolState: " + updatedProtocolState.toJson());
				}

				if (protocolState.getLsValues() != null){
					for(ProtocolValue protocolValue : protocolState.getLsValues()){
						if (protocolValue.getId() == null){
							protocolValue.setLsState(ProtocolState.findProtocolState(protocolState.getId()));
							protocolValue.persist();							
						} else {
							ProtocolValue updatedProtocolValue = ProtocolValue.update(protocolValue);
							logger.debug("updatedProtocolValue: " + updatedProtocolValue.toJson());
						}
					}				
				} else {
					logger.debug("No protocol values to save");
				}
			}
		}

		return updatedProtocol;
	}

	@Override
	public Protocol getFullProtocol(Protocol queryProtocol){
		Protocol protocol = Protocol.findProtocol(queryProtocol.getId());
		Set<ProtocolLabel> lsLabels = new HashSet<ProtocolLabel>();
		for (ProtocolLabel protocolLabel : ProtocolLabel.findProtocolLabelsByProtocol(protocol).getResultList()){
			lsLabels.add(protocolLabel);
		}
		protocol.setLsLabels(lsLabels);
		Set<ProtocolState> lsStates = new HashSet<ProtocolState>();
		for (ProtocolState protocolState : ProtocolState.findProtocolStatesByProtocol(protocol).getResultList()){
			lsStates.add(protocolState);
		}	
		protocol.setLsStates(lsStates);

		return protocol;
	}

//	@Override
//	public Collection<Protocol> findProtocolsByMetadataJson(String json) {
//		Collection<Protocol> protocolList = new HashSet<Protocol>();
//		Collection<StringCollectionDTO> metaDataList = StringCollectionDTO.fromJsonArrayToStringCollectioes(json);
//		for (StringCollectionDTO metaData : metaDataList){
//			
//			//finding by name
//			Collection<Protocol> protocols = findProtocolByMetadata(metaData.getName(), "NAME");
//			if (protocols.size() > 0){
//				protocolList.addAll(protocols);
//			}
//			
//			//finding by code
//			protocols = findProtocolByMetadata(metaData.getCode(), "CODE");
//			if (protocols.size() > 0){
//				protocolList.addAll(protocols);
//			}
//		}
//		
//		return protocolList;
//	}
	
	public Collection<Protocol> findProtocolsByGenericMetaDataSearch(String queryString) {
		//make our HashSets: protocolIdList will be filled/cleared/refilled for each term
		//protocolList is the final search result
		HashSet<Long> protocolIdList = new HashSet<Long>();
		HashSet<Long> protocolAllIdList = new HashSet<Long>();
		Collection<Protocol> protocolList = new HashSet<Protocol>();
		//Split the query up on spaces
		String[] splitQuery = queryString.split("\\s+");
		logger.debug("Number of search terms: " + splitQuery.length);
		//Make the Map of terms and HashSets of protocol id's then fill. We will run intersect logic later.
		Map<String, HashSet<Long>> resultsByTerm = new HashMap<String, HashSet<Long>>();
		for (String term : splitQuery) {
			protocolIdList.addAll(findProtocolIdByMetadata(term, "CODE"));
			protocolIdList.addAll(findProtocolIdByMetadata(term, "NAME"));
			protocolIdList.addAll(findProtocolIdByMetadata(term, "SCIENTIST"));
			protocolIdList.addAll(findProtocolIdByMetadata(term, "TYPE"));
			protocolIdList.addAll(findProtocolIdByMetadata(term, "KIND"));
			protocolIdList.addAll(findProtocolIdByMetadata(term, "DATE"));
			protocolIdList.addAll(findProtocolIdByMetadata(term, "NOTEBOOK"));
			protocolIdList.addAll(findProtocolIdByMetadata(term, "KEYWORD"));
			protocolIdList.addAll(findProtocolIdByMetadata(term, "ASSAY ACTIVITY"));
			protocolIdList.addAll(findProtocolIdByMetadata(term, "MOLECULAR TARGET"));
			protocolIdList.addAll(findProtocolIdByMetadata(term, "ASSAY TYPE"));
			protocolIdList.addAll(findProtocolIdByMetadata(term, "ASSAY TECHNOLOGY"));
			protocolIdList.addAll(findProtocolIdByMetadata(term, "CELL LINE"));
			protocolIdList.addAll(findProtocolIdByMetadata(term, "TARGET ORIGIN"));
			protocolIdList.addAll(findProtocolIdByMetadata(term, "ASSAY STAGE"));
			
			resultsByTerm.put(term, new HashSet<Long>(protocolIdList));
			protocolAllIdList.addAll(protocolIdList);
			protocolIdList.clear();
		}
		//Here is the intersect logic
		for (String term: splitQuery) {
			protocolAllIdList.retainAll(resultsByTerm.get(term));
		}
		for (Long id: protocolAllIdList) protocolList.add(Protocol.findProtocol(id));
		return protocolList;
	}

	public Collection<Long> findProtocolIdByMetadata(String queryString, String searchBy) {
		Collection<Long> protocolIdList = new HashSet<Long>();
		if (searchBy == "CODE") {
			List<Protocol> protocols = Protocol.findProtocolsByCodeNameEquals(queryString).getResultList();
			if (!protocols.isEmpty()){
				for (Protocol protocol:protocols) {
					protocolIdList.add(protocol.getId());
				}
			}
			protocols.clear();
		}
		if (searchBy == "NAME") {
			List<ProtocolLabel> protocolLabels = ProtocolLabel.findProtocolLabelsByLabelTextLike(queryString).getResultList();
			if (!protocolLabels.isEmpty()) {
				for (ProtocolLabel protocolLabel: protocolLabels) {
					protocolIdList.add(protocolLabel.getProtocol().getId());
				}
			}
			protocolLabels.clear();
		}
		if (searchBy == "SCIENTIST") {
			Collection<ProtocolValue> protocolValues = ProtocolValue.findProtocolValuesByLsKindEqualsAndStringValueLike("scientist", queryString).getResultList();
			if (!protocolValues.isEmpty()){
				for (ProtocolValue protocolValue : protocolValues) {
					protocolIdList.add(protocolValue.getLsState().getProtocol().getId());
				}
			}
			protocolValues.clear();
		}
		if (searchBy == "TYPE") {
			List<Protocol> protocols = Protocol.findProtocolsByLsTypeLike(queryString).getResultList();
			if (!protocols.isEmpty()){
				for (Protocol protocol: protocols) {
					protocolIdList.add(protocol.getId());
				}
			}
			protocols.clear();
		}
		if (searchBy == "KIND") {
			List<Protocol> protocols = Protocol.findProtocolsByLsKindLike(queryString).getResultList();
			if (!protocols.isEmpty()){
				for (Protocol protocol: protocols) {
					protocolIdList.add(protocol.getId());
				}
			}
			protocols.clear();
		}
		if (searchBy == "DATE") {
			Collection<ProtocolValue> protocolValues = new HashSet<ProtocolValue>();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
			DateFormat df2 = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
			try {
				Date date = df.parse(queryString);
				protocolValues = ProtocolValue.findProtocolValuesByLsKindEqualsAndDateValueLike("creation date", date).getResultList();
			} catch (Exception e) {
				try {
					Date date = df2.parse(queryString);
					protocolValues = ProtocolValue.findProtocolValuesByLsKindEqualsAndDateValueLike("creation date", date).getResultList();
				} catch (Exception e2) {
					//do nothing
				}
			}
			if (!protocolValues.isEmpty()) {
				for (ProtocolValue protocolValue : protocolValues) {
					protocolIdList.add(protocolValue.getLsState().getProtocol().getId());
				}
			}
			protocolValues.clear();
		}
		if (searchBy == "NOTEBOOK") {
			Collection<ProtocolValue> protocolValues = ProtocolValue.findProtocolValuesByLsKindEqualsAndStringValueLike("notebook", queryString).getResultList();
			if (!protocolValues.isEmpty()) {
				for (ProtocolValue protocolValue : protocolValues) {
					protocolIdList.add(protocolValue.getLsState().getProtocol().getId());
				}
			}
			protocolValues.clear();
		}
		if (searchBy == "KEYWORD") {
			Collection<LsTag> tags = LsTag.findLsTagsByTagTextLike(queryString).getResultList();
			if (!tags.isEmpty()) {
				for (LsTag tag: tags) {
					Collection<Protocol> protocols = tag.getProtocols();
					if (!protocols.isEmpty()) {
						for (Protocol protocol:protocols) {
							protocolIdList.add(protocol.getId());
						}
					}
					protocols.clear();
				}
			}
			tags.clear();
		}
		if (searchBy == "ASSAY ACTIVITY" || searchBy == "MOLECULAR TARGET" || searchBy == "ASSAY TYPE" || searchBy == "ASSAY TECHNOLOGY" || searchBy == "CELL LINE" || searchBy == "TARGET ORIGIN" || searchBy == "ASSAY STAGE") {
			Collection<DDictValue> ddictValues = DDictValue.findDDictValuesByLabelTextLike(queryString).getResultList();
			if (!ddictValues.isEmpty()) {
				for (DDictValue ddictvalue : ddictValues) {
					if (ddictvalue.getShortName() != null) {
						Collection<ProtocolValue> protocolValues = ProtocolValue.findProtocolValuesByLsKindEqualsAndCodeValueLike(searchBy.toLowerCase(), ddictvalue.getShortName()).getResultList();
						if (!protocolValues.isEmpty()) {
							for (ProtocolValue protocolValue : protocolValues) {
								protocolIdList.add(protocolValue.getLsState().getProtocol().getId());
							}
						}
						protocolValues.clear();
					}
				}
			}
		}
		
		return protocolIdList;
	}
	
	public Collection<Protocol> findProtocolsByMetadata(String queryString, String searchBy) {
		Collection<Protocol> protocolList = new HashSet<Protocol>();
		Collection<Long> protocolIdList = findProtocolIdsByMetadata(queryString, searchBy);
		if (!protocolIdList.isEmpty()) {
			for (Long id: protocolIdList) {
				protocolList.add(Protocol.findProtocol(id));
			}
		}
		return protocolList;
	}
}
