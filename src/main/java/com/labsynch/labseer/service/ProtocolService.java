package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.Protocol;

@Service
public interface ProtocolService {

	Protocol saveLsProtocol(Protocol protocol);

	Protocol getFullProtocol(Protocol protocol);

	Protocol updateProtocol(Protocol protocol);

//	Collection<Protocol> findProtocolsByMetadataJson(String json);

	Collection<Protocol> findProtocolsByGenericMetaDataSearch(String query);
	
	Collection<Long> findProtocolIdsByMetadata(String queryString, String searchBy);

	Set<Protocol> findProtocolsByRequestMetadata(
			Map<String, String> requestParams);
	
}
