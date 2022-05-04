package com.labsynch.labseer.service;

import java.util.Collection;

import com.labsynch.labseer.domain.ItxProtocolProtocolValue;

import org.springframework.stereotype.Service;

@Service
public interface ItxProtocolProtocolValueService {

	ItxProtocolProtocolValue updateItxProtocolProtocolValue(ItxProtocolProtocolValue itxProtocolProtocolValue);

	Collection<ItxProtocolProtocolValue> updateItxProtocolProtocolValues(
			Collection<ItxProtocolProtocolValue> itxProtocolProtocolValues);

	ItxProtocolProtocolValue saveItxProtocolProtocolValue(ItxProtocolProtocolValue itxProtocolProtocolValue);

	Collection<ItxProtocolProtocolValue> saveItxProtocolProtocolValues(
			Collection<ItxProtocolProtocolValue> itxProtocolProtocolValues);

}
