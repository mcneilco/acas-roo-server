package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.Protocol;
import com.labsynch.labseer.exceptions.UniqueNameException;

@Service
public interface ProtocolService {

	Protocol saveLsProtocol(Protocol protocol) throws UniqueNameException;

	Protocol getFullProtocol(Protocol protocol);

	Protocol updateProtocol(Protocol protocol) throws UniqueNameException;

//	Collection<Protocol> findProtocolsByMetadataJson(String json);

	Collection<Protocol> findProtocolsByGenericMetaDataSearch(String query);
	
	Collection<Long> findProtocolIdsByMetadata(String queryString, String searchBy);

	Set<Protocol> findProtocolsByRequestMetadata(
			Map<String, String> requestParams);
	
}
