package com.labsynch.labseer.service;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.Protocol;

@Service
public interface ProtocolService {

	Protocol saveLsProtocol(Protocol protocol);

	Protocol getFullProtocol(Protocol protocol);

	Protocol updateProtocol(Protocol protocol);

	Collection<Protocol> findProtocolsByMetadataJson(String json);
	
	
	
}
