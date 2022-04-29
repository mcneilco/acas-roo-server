package com.labsynch.labseer.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.labsynch.labseer.domain.Protocol;
import com.labsynch.labseer.domain.ProtocolState;
import com.labsynch.labseer.domain.ProtocolValue;
import com.labsynch.labseer.dto.GenericStatePathRequest;
import com.labsynch.labseer.dto.ProtocolStatePathDTO;
import com.labsynch.labseer.utils.PropertiesUtilService;
import com.labsynch.labseer.utils.SimpleUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProtocolStateServiceImpl implements ProtocolStateService {

	private static final Logger logger = LoggerFactory.getLogger(ProtocolStateServiceImpl.class);
	
	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Override
	public Collection<ProtocolState> ignoreAllProtocolStates(Collection<ProtocolState> protocolStates) {
		//mark protocolStates and values as ignore 
		Collection<ProtocolState> protocolStateSet = new HashSet<ProtocolState>();
		for (ProtocolState queryProtocolState : protocolStates){
			ProtocolState protocolState = ProtocolState.findProtocolState(queryProtocolState.getId());			
				for(ProtocolValue protocolValue : ProtocolValue.findProtocolValuesByLsState(protocolState).getResultList()){
					protocolValue.setIgnored(true);
					protocolValue.merge();
				}
				protocolState.setIgnored(true);
				protocolState.merge();
				protocolStateSet.add(ProtocolState.findProtocolState(protocolState.getId()));
		}

		return(protocolStateSet);

	}

	@Override
	public List<ProtocolState> getProtocolStatesByProtocolIdAndStateTypeKind(
			Long protocolId, String stateType, String stateKind) {
		
			List<ProtocolState> protocolStates = ProtocolState.findProtocolStatesByProtocolIDAndStateTypeKind(protocolId, stateType, stateKind).getResultList();

			return protocolStates;
	}

	@Override
	public ProtocolState createProtocolStateByProtocolIdAndStateTypeKind(
			Long protocolId, String stateType, String stateKind) {
		ProtocolState protocolState = new ProtocolState();
		Protocol protocol = Protocol.findProtocol(protocolId);
		protocolState.setProtocol(protocol);
		protocolState.setLsType(stateType);
		protocolState.setLsKind(stateKind);
		protocolState.setRecordedBy("default");
		protocolState.persist();
		return protocolState;
	}

	@Override
	public ProtocolState saveProtocolState(
			ProtocolState protocolState) {
		protocolState.setProtocol(Protocol.findProtocol(protocolState.getProtocol().getId()));		
		protocolState.persist();
		Set<ProtocolValue> savedValues = new HashSet<ProtocolValue>();
		for (ProtocolValue protocolValue : protocolState.getLsValues()){
			protocolValue.setLsState(protocolState);
			protocolValue.persist();
			savedValues.add(protocolValue);
		}
		protocolState.setLsValues(savedValues);
		protocolState.merge();
		return protocolState;
	}

	@Override
	public Collection<ProtocolState> saveProtocolStates(
			Collection<ProtocolState> protocolStates) {
		for (ProtocolState protocolState: protocolStates) {
			protocolState = saveProtocolState(protocolState);
		}
		return protocolStates;
	}
	
	@Override
	public ProtocolState updateProtocolState(
			ProtocolState protocolState) {
		protocolState.setVersion(ProtocolState.findProtocolState(protocolState.getId()).getVersion());
		protocolState.merge();
		return protocolState;
	}

	@Override
	public Collection<ProtocolState> updateProtocolStates(
			Collection<ProtocolState> protocolStates) {
		for (ProtocolState protocolState : protocolStates){
			protocolState = updateProtocolState(protocolState);
		}
		return null;
	}
	
	@Override
	public ProtocolState getProtocolState(String idOrCodeName,
			String stateType, String stateKind) {
		ProtocolState state = null;
		try{
			Collection<ProtocolState> states = getProtocolStates(idOrCodeName, stateType, stateKind);
			state = states.iterator().next();
		}catch (Exception e){
			logger.error("Caught error "+e.toString()+" trying to find a state.",e);
			state = null;
		}
		return state;
	}

	@Override
	public Collection<ProtocolStatePathDTO> getProtocolStates(
			Collection<GenericStatePathRequest> genericRequests) {
		Collection<ProtocolStatePathDTO> results = new ArrayList<ProtocolStatePathDTO>();
		for (GenericStatePathRequest request : genericRequests){
			ProtocolStatePathDTO result = new ProtocolStatePathDTO();
			result.setIdOrCodeName(request.getIdOrCodeName());
			result.setStateType(request.getStateType());
			result.setStateKind(request.getStateKind());
			result.setStates(getProtocolStates(request.getIdOrCodeName(), request.getStateType(), request.getStateKind()));
			results.add(result);
		}
		return results;
	}
	
	private Collection<ProtocolState> getProtocolStates(String idOrCodeName, String stateType, String stateKind){
		if (SimpleUtil.isNumeric(idOrCodeName)){
			Long id = Long.valueOf(idOrCodeName);
			return ProtocolState.findProtocolStatesByProtocolIDAndStateTypeKind(id, stateType, stateKind).getResultList();
		}else{
			return ProtocolState.findProtocolStatesByProtocolCodeNameAndStateTypeKind(idOrCodeName, stateType, stateKind).getResultList();
		}
	}

}
