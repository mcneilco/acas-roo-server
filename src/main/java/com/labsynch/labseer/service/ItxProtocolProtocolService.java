package com.labsynch.labseer.service;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.ItxProtocolProtocol;

@Service
public interface ItxProtocolProtocolService {

	ItxProtocolProtocol saveLsItxProtocol(ItxProtocolProtocol itxProtocol) throws Exception;

	Collection<ItxProtocolProtocol> saveLsItxProtocols(String json);
	
	Collection<ItxProtocolProtocol> saveLsItxProtocols(Collection<ItxProtocolProtocol> itxProtocolProtocols) throws Exception;

	ItxProtocolProtocol updateItxProtocolProtocol(
			ItxProtocolProtocol jsonItxProtocolProtocol);
	
	
	
}
