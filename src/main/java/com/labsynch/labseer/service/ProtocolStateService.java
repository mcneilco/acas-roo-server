package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.domain.ProtocolState;
import com.labsynch.labseer.dto.GenericStatePathRequest;
import com.labsynch.labseer.dto.ProtocolStatePathDTO;

public interface ProtocolStateService {

	List<ProtocolState> getProtocolStatesByProtocolIdAndStateTypeKind(
			Long protocolId, String stateType, String stateKind);

	Collection<ProtocolState> ignoreAllProtocolStates(
			Collection<ProtocolState> protocolStates);

	ProtocolState createProtocolStateByProtocolIdAndStateTypeKind(Long protocolId,
			String stateType, String stateKind);

	ProtocolState saveProtocolState(ProtocolState protocolState);

	Collection<ProtocolState> saveProtocolStates(
			Collection<ProtocolState> protocolStates);

	ProtocolState updateProtocolState(ProtocolState protocolState);

	Collection<ProtocolState> updateProtocolStates(
			Collection<ProtocolState> protocolStates);

	ProtocolState getProtocolState(String idOrCodeName, String stateType,
			String stateKind);

	Collection<ProtocolStatePathDTO> getProtocolStates(
			Collection<GenericStatePathRequest> genericRequests);

}
