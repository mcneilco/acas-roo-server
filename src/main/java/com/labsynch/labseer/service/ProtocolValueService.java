package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.domain.ProtocolValue;

public interface ProtocolValueService {

	ProtocolValue updateProtocolValue(String idOrCodeName, String stateType,
			String stateKind, String valueType, String valueKind, String value);

	ProtocolValue saveProtocolValue(ProtocolValue protocolValue);

	ProtocolValue updateProtocolValue(ProtocolValue protocolValue);

	List<ProtocolValue> getProtocolValuesByProtocolId(Long id);

	List<ProtocolValue> getProtocolValuesByProtocolIdAndStateTypeKindAndValueTypeKind(
			Long protocolId, String stateType, String stateKind,
			String valueType, String valueKind);

	Collection<ProtocolValue> saveProtocolValues(
			Collection<ProtocolValue> protocolValues);


}
