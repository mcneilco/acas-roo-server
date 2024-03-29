package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.labsynch.labseer.domain.Protocol;
import com.labsynch.labseer.dto.DateValueComparisonRequest;
import com.labsynch.labseer.dto.ProtocolErrorMessageDTO;
import com.labsynch.labseer.exceptions.UniqueNameException;

import org.springframework.stereotype.Service;

@Service
public interface ProtocolService {

	Protocol saveLsProtocol(Protocol protocol) throws UniqueNameException;

	Protocol getFullProtocol(Protocol protocol);

	Protocol updateProtocol(Protocol protocol) throws UniqueNameException;

	// Collection<Protocol> findProtocolsByMetadataJson(String json);

	Collection<Protocol> findProtocolsByGenericMetaDataSearch(String query);

	Collection<Long> findProtocolIdsByMetadata(String queryString, String searchBy);

	Set<Protocol> findProtocolsByRequestMetadata(
			Map<String, String> requestParams);

	Collection<Protocol> findProtocolsByGenericMetaDataSearch(
			String queryString, String userName);

	Collection<Protocol> findProtocolsByGenericMetaDataSearch(
			String queryString, List<String> projects);

	Collection<ProtocolErrorMessageDTO> findProtocolsByCodeNames(
			List<String> codeNames);

	Collection<String> getProtocolCodesByDateValueComparison(
			DateValueComparisonRequest requestDTO) throws Exception;

	int renameBatchCode(String oldCode, String newCode, String modifiedByUser, Long transactionId);

}
