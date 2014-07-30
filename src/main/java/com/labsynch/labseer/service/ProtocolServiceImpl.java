package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.Experiment;
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

	@Override
	public Collection<Protocol> findProtocolsByMetadataJson(String json) {
		Collection<Protocol> protocolList = new HashSet<Protocol>();
		Collection<StringCollectionDTO> metaDataList = StringCollectionDTO.fromJsonArrayToStringCollectioes(json);
		for (StringCollectionDTO metaData : metaDataList){
			Collection<Protocol> protocols = findProtocolByMetadata(metaData.getName());
			if (protocols.size() > 0){
				protocolList.addAll(protocols);
			}
		}
		
		return protocolList;
	}

	private Collection<Protocol> findProtocolByMetadata(String queryString) {
		Collection<Protocol> protocolList = new HashSet<Protocol>();
		
		//find by experiment codeName
		List<Protocol> protocols = Protocol.findProtocolsByCodeNameEquals(queryString).getResultList();
		if (!protocols.isEmpty()){
			protocolList.addAll(protocols);
		}
		
		
		return protocolList;
	}
}
