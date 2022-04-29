package com.labsynch.labseer.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.DDictValue;
import com.labsynch.labseer.domain.LsTag;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.Protocol;
import com.labsynch.labseer.domain.ProtocolLabel;
import com.labsynch.labseer.domain.ProtocolState;
import com.labsynch.labseer.domain.ProtocolValue;
import com.labsynch.labseer.dto.AutoLabelDTO;
import com.labsynch.labseer.dto.DateValueComparisonRequest;
import com.labsynch.labseer.dto.ProtocolErrorMessageDTO;
import com.labsynch.labseer.exceptions.UniqueNameException;
import com.labsynch.labseer.utils.PropertiesUtilService;
import com.labsynch.labseer.utils.SimpleUtil;

@Service
public class ProtocolServiceImpl implements ProtocolService {

	private static final Logger logger = LoggerFactory.getLogger(ProtocolServiceImpl.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Autowired
	private AutoLabelService autoLabelService;

	@Autowired
	private AuthorService authorService;

	// @PostConstruct
	// public void init(){
	// logger.info("calling the appSetting 201 init method upon startup");
	// ApplicationSetting appSet = new ApplicationSetting();
	// appSet.setPropName("testing201");
	// appSet.setPropValue("testingValue201");
	// appSet.persist();
	// }

	@Override
	public Protocol saveLsProtocol(Protocol protocol) throws UniqueNameException {
		if (logger.isDebugEnabled())
			logger.debug("incoming meta protocol: " + protocol.toJson() + "\n");

		// check if protocol with the same name exists
		boolean checkProtocolName = propertiesUtilService.getUniqueProtocolName();
		if (checkProtocolName) {
			boolean protocolExists = false;
			Set<ProtocolLabel> protLabels = protocol.getLsLabels();
			for (ProtocolLabel label : protLabels) {
				String labelText = label.getLabelText();
				if (logger.isDebugEnabled())
					logger.debug("Searching for labelText: " + labelText);
				List<ProtocolLabel> protocolLabels = ProtocolLabel.findProtocolLabelsByName(labelText).getResultList();
				if (logger.isDebugEnabled())
					logger.debug("Found " + protocolLabels.size() + " labels");
				for (ProtocolLabel pl : protocolLabels) {
					Protocol pro = pl.getProtocol();
					// if the protocol is not hard deleted or soft deleted, there is a name conflict
					if (!pro.isIgnored() && !pl.isIgnored()) {
						protocolExists = true;
					}
				}
			}

			if (protocolExists) {
				throw new UniqueNameException("Protocol with the same name exists");
			}
		}

		Protocol newProtocol = new Protocol(protocol);
		if (newProtocol.getCodeName() == null) {

			String thingTypeAndKind = "document_protocol";
			String labelTypeAndKind = "id_codeName";
			Long numberOfLabels = 1L;
			List<AutoLabelDTO> labels = autoLabelService.getAutoLabels(thingTypeAndKind, labelTypeAndKind,
					numberOfLabels);
			newProtocol.setCodeName(labels.get(0).getAutoLabel());
		}

		newProtocol.persist();
		if (logger.isDebugEnabled())
			logger.debug("persisted the newProtocol: " + newProtocol.toJson() + "\n");
		if (protocol.getLsLabels() != null) {
			Set<ProtocolLabel> lsLabels = new HashSet<ProtocolLabel>();
			for (ProtocolLabel protocolLabel : protocol.getLsLabels()) {
				ProtocolLabel newProtocolLabel = new ProtocolLabel(protocolLabel);
				newProtocolLabel.setProtocol(newProtocol);
				logger.info("here is the newProtocolLabel before save: " + newProtocolLabel.toJson());
				newProtocolLabel.persist();
				lsLabels.add(newProtocolLabel);
			}
			newProtocol.setLsLabels(lsLabels);
		} else {
			if (logger.isDebugEnabled())
				logger.debug("No protocol labels to save");
		}

		if (protocol.getLsStates() != null) {
			Set<ProtocolState> lsStates = new HashSet<ProtocolState>();
			for (ProtocolState protocolState : protocol.getLsStates()) {
				ProtocolState newProtocolState = new ProtocolState(protocolState);
				newProtocolState.setProtocol(newProtocol);
				newProtocolState.persist();
				if (protocolState.getLsValues() != null) {
					Set<ProtocolValue> lsValues = new HashSet<ProtocolValue>();
					for (ProtocolValue protocolValue : protocolState.getLsValues()) {
						protocolValue.setLsState(newProtocolState);
						protocolValue.persist();
						lsValues.add(protocolValue);
						if (logger.isDebugEnabled())
							logger.debug("persisted the protocolValue: " + protocolValue.toJson());
					}
					newProtocolState.setLsValues(lsValues);
				} else {
					if (logger.isDebugEnabled())
						logger.debug("No protocol values to save");
				}
				lsStates.add(newProtocolState);
			}
			newProtocol.setLsStates(lsStates);
		}

		return newProtocol;
	}

	@Override
	public Protocol updateProtocol(Protocol protocol) throws UniqueNameException {
		if (logger.isDebugEnabled())
			logger.debug("UPDATE PROTOCOL --- incoming meta protocol: " + protocol.toJson() + "\n");

		boolean checkProtocolName = propertiesUtilService.getUniqueProtocolName();
		if (logger.isDebugEnabled())
			logger.debug("checkProtocolName = " + checkProtocolName);
		if (checkProtocolName) {
			boolean protocolExists = false;
			Set<ProtocolLabel> protLabels = protocol.getLsLabels();
			for (ProtocolLabel label : protLabels) {
				String labelText = label.getLabelText();
				if (logger.isDebugEnabled())
					logger.debug("Searching for labelText: " + labelText);
				List<ProtocolLabel> protocolLabels = ProtocolLabel.findProtocolLabelsByName(labelText).getResultList();
				if (logger.isDebugEnabled())
					logger.debug("Found " + protocolLabels.size() + " labels");
				for (ProtocolLabel pl : protocolLabels) {
					Protocol pro = pl.getProtocol();
					// if the protocol is not hard deleted or soft deleted, there is a name conflict
					if (!pro.isIgnored() && !pl.isIgnored() && !label.isIgnored()
							&& pro.getId().compareTo(protocol.getId()) != 0) {
						protocolExists = true;
					}
				}
			}

			if (protocolExists) {
				throw new UniqueNameException("Protocol with the same name exists");
			}
		}

		Protocol updatedProtocol = Protocol.update(protocol);
		if (protocol.getLsLabels() != null) {
			Set<ProtocolLabel> updatedProtocolLabels = new HashSet<ProtocolLabel>();
			for (ProtocolLabel protocolLabel : protocol.getLsLabels()) {
				if (logger.isDebugEnabled())
					logger.debug(protocolLabel.toJson());
				if (protocolLabel.getId() == null) {
					ProtocolLabel newProtocolLabel = new ProtocolLabel(protocolLabel);
					newProtocolLabel.setProtocol(updatedProtocol);
					newProtocolLabel.persist();
					// updatedProtocol.getLsLabels().add(newProtocolLabel);
					updatedProtocolLabels.add(newProtocolLabel);
				} else {
					ProtocolLabel updatedProtocolLabel = ProtocolLabel.update(protocolLabel);
					updatedProtocolLabels.add(updatedProtocolLabel);
				}
			}
			updatedProtocol.setLsLabels(updatedProtocolLabels);
		} else {
			if (logger.isDebugEnabled())
				logger.debug("No protocol labels to save");
		}

		if (protocol.getLsStates() != null) {
			Set<ProtocolState> updatedProtocolStates = new HashSet<ProtocolState>();
			for (ProtocolState protocolState : protocol.getLsStates()) {
				ProtocolState updatedProtocolState;
				if (protocolState.getId() == null) {
					updatedProtocolState = new ProtocolState(protocolState);
					updatedProtocolState.setProtocol(updatedProtocol);
					updatedProtocolState.persist();
					// protocolState.setId(newProtocolState.getId());
					// updatedProtocol.getLsStates().add(newProtocolState);
				} else {
					updatedProtocolState = ProtocolState.update(protocolState);
					if (logger.isDebugEnabled())
						logger.debug("updatedProtocolState: " + updatedProtocolState.toJson());
				}

				if (protocolState.getLsValues() != null) {
					Set<ProtocolValue> updatedProtocolValues = new HashSet<ProtocolValue>();
					for (ProtocolValue protocolValue : protocolState.getLsValues()) {
						ProtocolValue updatedProtocolValue;
						if (protocolValue.getId() == null) {
							updatedProtocolValue = new ProtocolValue(protocolValue);
							updatedProtocolValue.setLsState(updatedProtocolState);
							updatedProtocolValue.persist();
							updatedProtocolValues.add(updatedProtocolValue);
						} else {
							protocolValue.setLsState(updatedProtocolState);
							updatedProtocolValue = ProtocolValue.update(protocolValue);
							updatedProtocolValues.add(updatedProtocolValue);
							if (logger.isDebugEnabled())
								logger.debug("updatedProtocolValue: " + updatedProtocolValue.toJson());
						}
					}
					updatedProtocolState.setLsValues(updatedProtocolValues);
				} else {
					if (logger.isDebugEnabled())
						logger.debug("No protocol values to save");
				}
				updatedProtocolStates.add(updatedProtocolState);
			}
			updatedProtocol.setLsStates(updatedProtocolStates);
		}

		return updatedProtocol;
	}

	@Override
	public Protocol getFullProtocol(Protocol queryProtocol) {
		Protocol protocol = Protocol.findProtocol(queryProtocol.getId());
		Set<ProtocolLabel> lsLabels = new HashSet<ProtocolLabel>();
		for (ProtocolLabel protocolLabel : ProtocolLabel.findProtocolLabelsByProtocol(protocol).getResultList()) {
			lsLabels.add(protocolLabel);
		}
		protocol.setLsLabels(lsLabels);
		Set<ProtocolState> lsStates = new HashSet<ProtocolState>();
		for (ProtocolState protocolState : ProtocolState.findProtocolStatesByProtocol(protocol).getResultList()) {
			lsStates.add(protocolState);
		}
		protocol.setLsStates(lsStates);

		return protocol;
	}

	// @Override
	// public Collection<Protocol> findProtocolsByMetadataJson(String json) {
	// Collection<Protocol> protocolList = new HashSet<Protocol>();
	// Collection<StringCollectionDTO> metaDataList =
	// StringCollectionDTO.fromJsonArrayToStringCollectioes(json);
	// for (StringCollectionDTO metaData : metaDataList){
	//
	// //finding by name
	// Collection<Protocol> protocols = findProtocolByMetadata(metaData.getName(),
	// "NAME");
	// if (protocols.size() > 0){
	// protocolList.addAll(protocols);
	// }
	//
	// //finding by code
	// protocols = findProtocolByMetadata(metaData.getCode(), "CODENAME");
	// if (protocols.size() > 0){
	// protocolList.addAll(protocols);
	// }
	// }
	//
	// return protocolList;
	// }

	@Override
	public Collection<Protocol> findProtocolsByGenericMetaDataSearch(String queryString, String userName) {
		Collection<Protocol> rawResults = findProtocolsByGenericMetaDataSearch(queryString);
		if (propertiesUtilService.getRestrictExperiments()) {
			Collection<LsThing> projects = authorService.getUserProjects(userName);
			List<String> allowedProjectCodeNames = new ArrayList<String>();
			allowedProjectCodeNames.add("unassigned");
			for (LsThing project : projects) {
				allowedProjectCodeNames.add(project.getCodeName());
			}
			Collection<Protocol> results = new HashSet<Protocol>();
			for (Protocol rawResult : rawResults) {
				String protocolProject = null;
				for (ProtocolState state : rawResult.getLsStates()) {
					for (ProtocolValue value : state.getLsValues()) {
						if (value.getLsKind().equals("project")) {
							protocolProject = value.getCodeValue();
							break;
						}
					}
				}
				if (protocolProject == null) {
					// no project associated with protocol, pass it through
					results.add(rawResult);
				} else if (allowedProjectCodeNames.contains(protocolProject)) {
					results.add(rawResult);
				}
			}
			return results;
		} else {
			return rawResults;
		}
	}

	@Override
	public Collection<Protocol> findProtocolsByGenericMetaDataSearch(String queryString, List<String> projects) {
		Collection<Protocol> rawResults = findProtocolsByGenericMetaDataSearch(queryString);
		if (propertiesUtilService.getRestrictExperiments()) {
			projects.add("unassigned");
			Collection<Protocol> results = new HashSet<Protocol>();
			for (Protocol rawResult : rawResults) {
				String protocolProject = null;
				for (ProtocolState state : rawResult.getLsStates()) {
					if (!state.isIgnored() && !state.isDeleted()) {
						for (ProtocolValue value : state.getLsValues()) {
							if (value.getLsKind().equals("project") && !value.getDeleted() && !value.getIgnored()) {
								protocolProject = value.getCodeValue();
								break;
							}
						}
					}
				}
				if (protocolProject == null) {
					// no project associated with protocol, pass it through
					results.add(rawResult);
				} else if (projects.contains(protocolProject)) {
					results.add(rawResult);
				}
			}
			return results;
		} else {
			return rawResults;
		}
	}

	public Collection<Protocol> findProtocolsByGenericMetaDataSearch(String queryString) {
		// make our HashSets: protocolIdList will be filled/cleared/refilled for each
		// term
		// protocolList is the final search result
		HashSet<Long> protocolIdList = new HashSet<Long>();
		HashSet<Long> protocolAllIdList = new HashSet<Long>();
		Collection<Protocol> protocolList = new HashSet<Protocol>();
		// Split the query up on spaces
		List<String> splitQuery = SimpleUtil.splitSearchString(queryString);
		if (logger.isDebugEnabled())
			logger.debug("Number of search terms: " + splitQuery.size());
		// Make the Map of terms and HashSets of protocol id's then fill. We will run
		// intersect logic later.
		Map<String, HashSet<Long>> resultsByTerm = new HashMap<String, HashSet<Long>>();
		for (String term : splitQuery) {
			protocolIdList.addAll(findProtocolIdsByMetadata(term, "CODENAME"));
			protocolIdList.addAll(findProtocolIdsByMetadata(term, "NAME"));
			protocolIdList.addAll(findProtocolIdsByMetadata(term, "SCIENTIST"));
			// protocolIdList.addAll(findProtocolIdsByMetadata(term, "RECORDEDBY"));
			protocolIdList.addAll(findProtocolIdsByMetadata(term, "TYPE"));
			protocolIdList.addAll(findProtocolIdsByMetadata(term, "KIND"));
			protocolIdList.addAll(findProtocolIdsByMetadata(term, "STATUS"));
			protocolIdList.addAll(findProtocolIdsByMetadata(term, "DATE"));
			protocolIdList.addAll(findProtocolIdsByMetadata(term, "NOTEBOOK"));
			protocolIdList.addAll(findProtocolIdsByMetadata(term, "KEYWORD"));
			protocolIdList.addAll(findProtocolIdsByMetadata(term, "ASSAY ACTIVITY"));
			protocolIdList.addAll(findProtocolIdsByMetadata(term, "MOLECULAR TARGET"));
			protocolIdList.addAll(findProtocolIdsByMetadata(term, "ASSAY TYPE"));
			protocolIdList.addAll(findProtocolIdsByMetadata(term, "ASSAY TECHNOLOGY"));
			protocolIdList.addAll(findProtocolIdsByMetadata(term, "CELL LINE"));
			protocolIdList.addAll(findProtocolIdsByMetadata(term, "TARGET ORIGIN"));
			protocolIdList.addAll(findProtocolIdsByMetadata(term, "ASSAY STAGE"));

			resultsByTerm.put(term, new HashSet<Long>(protocolIdList));
			protocolAllIdList.addAll(protocolIdList);
			protocolIdList.clear();
		}
		// Here is the intersect logic
		for (String term : splitQuery) {
			protocolAllIdList.retainAll(resultsByTerm.get(term));
		}
		for (Long id : protocolAllIdList)
			protocolList.add(Protocol.findProtocol(id));
		// This method uses finders that will find everything, whether or not it is
		// ignored or deleted
		Collection<Protocol> result = new HashSet<Protocol>();
		for (Protocol protocol : protocolList) {
			// For Protocol Browser, we want to see soft deleted (ignored=true,
			// deleted=false), but not hard deleted (ignored=deleted=true)
			if (protocol.isDeleted()) {
				if (logger.isDebugEnabled())
					logger.debug("removing a deleted protocol from the results");
			} else {
				result.add(protocol);
			}
		}
		return result;
	}

	public Collection<Long> findProtocolIdsByMetadata(String queryString, String searchBy) {
		Collection<Long> protocolIdList = new HashSet<Long>();
		if (searchBy == "CODENAME") {
			List<Protocol> protocols = Protocol.findProtocolsByCodeNameLike(queryString).getResultList();
			if (!protocols.isEmpty()) {
				for (Protocol protocol : protocols) {
					protocolIdList.add(protocol.getId());
				}
			}
			protocols.clear();
		}
		if (searchBy == "NAME") {
			List<ProtocolLabel> protocolLabels = ProtocolLabel.findProtocolLabelsByLabelTextLike(queryString)
					.getResultList();
			if (!protocolLabels.isEmpty()) {
				for (ProtocolLabel protocolLabel : protocolLabels) {
					protocolIdList.add(protocolLabel.getProtocol().getId());
				}
			}
			protocolLabels.clear();
		}
		if (searchBy == "SCIENTIST") {
			Collection<ProtocolValue> protocolValues = ProtocolValue
					.findProtocolValuesByLsKindEqualsAndCodeValueLike("scientist", queryString).getResultList();
			if (!protocolValues.isEmpty()) {
				for (ProtocolValue protocolValue : protocolValues) {
					protocolIdList.add(protocolValue.getLsState().getProtocol().getId());
				}
			}
			protocolValues.clear();
		}
		if (searchBy == "RECORDEDBY") {
			List<Protocol> protocols = Protocol.findProtocolsByRecordedByLike(queryString).getResultList();
			if (!protocols.isEmpty()) {
				for (Protocol protocol : protocols) {
					protocolIdList.add(protocol.getId());
				}
			}
			protocols.clear();
		}
		if (searchBy == "TYPE") {
			List<Protocol> protocols = Protocol.findProtocolsByLsTypeLike(queryString).getResultList();
			if (!protocols.isEmpty()) {
				for (Protocol protocol : protocols) {
					protocolIdList.add(protocol.getId());
				}
			}
			protocols.clear();
		}
		if (searchBy == "KIND") {
			List<Protocol> protocols = Protocol.findProtocolsByLsKindLike(queryString).getResultList();
			if (!protocols.isEmpty()) {
				for (Protocol protocol : protocols) {
					protocolIdList.add(protocol.getId());
				}
			}
			protocols.clear();
		}
		if (searchBy == "STATUS") {
			Collection<ProtocolValue> protocolValues = ProtocolValue
					.findProtocolValuesByLsKindEqualsAndCodeValueLike("protocol status", queryString).getResultList();
			if (!protocolValues.isEmpty()) {
				for (ProtocolValue protocolValue : protocolValues) {
					if (!protocolValue.isIgnored())
						protocolIdList.add(protocolValue.getLsState().getProtocol().getId());
				}
			}
			protocolValues.clear();
		}
		if (searchBy == "DATE") {
			Collection<ProtocolValue> protocolValues = new HashSet<ProtocolValue>();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
			DateFormat df2 = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
			try {
				Date date = df.parse(queryString);
				if (logger.isDebugEnabled())
					logger.debug("Successfully parsed date: " + queryString);
				protocolValues = ProtocolValue.findProtocolValuesByLsKindEqualsAndDateValueLike("creation date", date)
						.getResultList();
			} catch (Exception e) {
				try {
					Date date = df2.parse(queryString);
					if (logger.isDebugEnabled())
						logger.debug("Successfully parsed date: " + queryString);
					protocolValues = ProtocolValue
							.findProtocolValuesByLsKindEqualsAndDateValueLike("creation date", date).getResultList();
				} catch (Exception e2) {
					// do nothing
				}
			}
			if (!protocolValues.isEmpty()) {
				for (ProtocolValue protocolValue : protocolValues) {
					protocolIdList.add(protocolValue.getLsState().getProtocol().getId());
				}
			}
			protocolValues.clear();
		}
		if (searchBy == "NOTEBOOK") {
			Collection<ProtocolValue> protocolValues = ProtocolValue
					.findProtocolValuesByLsKindEqualsAndStringValueLike("notebook", queryString).getResultList();
			if (!protocolValues.isEmpty()) {
				for (ProtocolValue protocolValue : protocolValues) {
					protocolIdList.add(protocolValue.getLsState().getProtocol().getId());
				}
			}
			protocolValues.clear();
		}
		if (searchBy == "KEYWORD") {
			Collection<LsTag> tags = LsTag.findLsTagsByTagTextLike(queryString).getResultList();
			if (!tags.isEmpty()) {
				for (LsTag tag : tags) {
					Collection<Protocol> protocols = tag.getProtocols();
					if (!protocols.isEmpty()) {
						for (Protocol protocol : protocols) {
							protocolIdList.add(protocol.getId());
						}
					}
					protocols.clear();
				}
			}
			tags.clear();
		}
		if (searchBy == "MOLECULAR TARGET") {
			Collection<ProtocolValue> protocolValues = ProtocolValue
					.findProtocolValuesByLsKindEqualsAndCodeValueLike(searchBy.toLowerCase(), queryString)
					.getResultList();
			if (!protocolValues.isEmpty()) {
				for (ProtocolValue protocolValue : protocolValues) {
					protocolIdList.add(protocolValue.getLsState().getProtocol().getId());
				}
			}
			protocolValues.clear();
		}
		if (searchBy == "ASSAY ACTIVITY" || searchBy == "ASSAY TYPE" || searchBy == "ASSAY TECHNOLOGY"
				|| searchBy == "CELL LINE" || searchBy == "TARGET ORIGIN" || searchBy == "ASSAY STAGE") {
			Collection<DDictValue> ddictValues = DDictValue.findDDictValuesByLabelTextLike(queryString).getResultList();
			if (!ddictValues.isEmpty()) {
				for (DDictValue ddictvalue : ddictValues) {
					if (ddictvalue.getShortName() != null) {
						Collection<ProtocolValue> protocolValues = ProtocolValue
								.findProtocolValuesByLsKindEqualsAndCodeValueLike(searchBy.toLowerCase(),
										ddictvalue.getShortName())
								.getResultList();
						if (!protocolValues.isEmpty()) {
							for (ProtocolValue protocolValue : protocolValues) {
								protocolIdList.add(protocolValue.getLsState().getProtocol().getId());
							}
						}
						protocolValues.clear();
					}
				}
			}
		}

		return protocolIdList;
	}

	public Collection<Protocol> findProtocolsByMetadata(String queryString, String searchBy) {
		Collection<Protocol> protocolList = new HashSet<Protocol>();
		Collection<Long> protocolIdList = findProtocolIdsByMetadata(queryString, searchBy);
		if (!protocolIdList.isEmpty()) {
			for (Long id : protocolIdList) {
				protocolList.add(Protocol.findProtocol(id));
			}
		}
		return protocolList;
	}

	@Override
	public Set<Protocol> findProtocolsByRequestMetadata(
			Map<String, String> requestParams) {

		Set<Protocol> result = new HashSet<Protocol>();

		if (requestParams.isEmpty()) {
			result.addAll(Protocol.findAllProtocols());
			return result;
		}

		Set<Protocol> resultByKind = new HashSet<Protocol>();
		Set<Protocol> resultByType = new HashSet<Protocol>();
		Set<Protocol> resultByName = new HashSet<Protocol>();
		Set<Protocol> resultByCodeName = new HashSet<Protocol>();

		if (requestParams.containsKey("kind"))
			resultByKind.addAll(findProtocolsByMetadata(requestParams.get("kind"), "KIND"));
		if (requestParams.containsKey("type"))
			resultByType.addAll(findProtocolsByMetadata(requestParams.get("type"), "TYPE"));
		if (requestParams.containsKey("name"))
			resultByName.addAll(findProtocolsByMetadata(requestParams.get("name"), "NAME"));
		if (requestParams.containsKey("codeName"))
			resultByCodeName.addAll(findProtocolsByMetadata(requestParams.get("codeName"), "CODENAME"));

		result.addAll(resultByKind);
		result.addAll(resultByType);
		result.addAll(resultByName);
		result.addAll(resultByCodeName);

		if (requestParams.containsKey("kind"))
			result.retainAll(resultByKind);
		if (requestParams.containsKey("type"))
			result.retainAll(resultByType);
		if (requestParams.containsKey("name"))
			result.retainAll(resultByName);
		if (requestParams.containsKey("codeName"))
			result.retainAll(resultByCodeName);

		return result;
	}

	@Override
	public Collection<ProtocolErrorMessageDTO> findProtocolsByCodeNames(List<String> codeNames) {
		if (codeNames.isEmpty())
			return new ArrayList<ProtocolErrorMessageDTO>();
		EntityManager em = Protocol.entityManager();
		String queryString = "SELECT new com.labsynch.labseer.dto.ProtocolErrorMessageDTO( "
				+ "protocol.codeName, "
				+ "protocol )"
				+ " FROM Protocol protocol ";
		queryString += "where ( protocol.ignored <> true ) and ( ";
		Collection<Query> queries = SimpleUtil.splitHqlInClause(em, queryString, "protocol.codeName", codeNames);
		Collection<ProtocolErrorMessageDTO> results = new HashSet<ProtocolErrorMessageDTO>();
		for (Query q : queries) {
			results.addAll(q.getResultList());
		}
		// diff request with results to find codeNames that could not be found
		HashSet<String> requestCodeNames = new HashSet<String>();
		requestCodeNames.addAll(codeNames);
		HashSet<String> foundCodeNames = new HashSet<String>();
		for (ProtocolErrorMessageDTO result : results) {
			foundCodeNames.add(result.getProtocolCodeName());
		}
		requestCodeNames.removeAll(foundCodeNames);
		if (!requestCodeNames.isEmpty()) {
			for (String notFoundCodeName : requestCodeNames) {
				ProtocolErrorMessageDTO notFoundDTO = new ProtocolErrorMessageDTO();
				notFoundDTO.setProtocolCodeName(notFoundCodeName);
				notFoundDTO.setLevel("error");
				notFoundDTO.setMessage("protocolCodeName not found");
				results.add(notFoundDTO);
			}
		}

		return results;
	}

	@Override
	public Collection<String> getProtocolCodesByDateValueComparison(
			DateValueComparisonRequest requestDTO) throws Exception {
		if (requestDTO.getStateType() == null || requestDTO.getStateType().length() == 0)
			throw new Exception("Must provide stateType");
		if (requestDTO.getStateKind() == null || requestDTO.getStateKind().length() == 0)
			throw new Exception("Must provide stateKind");
		if (requestDTO.getValueKind() == null || requestDTO.getValueKind().length() == 0)
			throw new Exception("Must provide valueKind");
		if (requestDTO.getSecondsDelta() == null)
			throw new Exception("Must provide secondsDelta");
		if (requestDTO.getNewerThanModified() == null)
			requestDTO.setNewerThanModified(false);
		EntityManager em = Protocol.entityManager();
		String query = "SELECT new Map( protocol.codeName AS codeName, protocol.modifiedDate AS modifiedDate, protocol.recordedDate as recordedDate, value.dateValue AS dateValue )"
				+ "FROM Protocol AS protocol "
				+ "JOIN protocol.lsStates  state "
				+ "WITH state.ignored = false "
				+ "AND state.lsType = :stateType "
				+ "AND state.lsKind = :stateKind "
				+ "LEFT OUTER JOIN state.lsValues AS value "
				+ "WITH value.ignored = false "
				+ "AND value.lsType = :valueType "
				+ "AND value.lsKind = :valueKind "
				+ "WHERE protocol.ignored = false ";
		if (requestDTO.getLsType() != null && requestDTO.getLsType().length() > 0)
			query += "AND protocol.lsType = :protocolType ";
		if (requestDTO.getLsKind() != null && requestDTO.getLsKind().length() > 0)
			query += "AND protocol.lsKind = :protocolKind ";

		TypedQuery<Map> q = em.createQuery(query, Map.class);
		q.setParameter("stateType", requestDTO.getStateType());
		q.setParameter("stateKind", requestDTO.getStateKind());
		q.setParameter("valueType", "dateValue");
		q.setParameter("valueKind", requestDTO.getValueKind());
		if (requestDTO.getLsType() != null && requestDTO.getLsType().length() > 0)
			q.setParameter("protocolType", requestDTO.getLsType());
		if (requestDTO.getLsKind() != null && requestDTO.getLsKind().length() > 0)
			q.setParameter("protocolKind", requestDTO.getLsKind());

		Collection<Map> resultMaps = q.getResultList();
		Collection<String> protocolCodes = new HashSet<String>();
		for (Map resultMap : resultMaps) {
			if (resultMap.get("dateValue") == null)
				protocolCodes.add((String) resultMap.get("codeName"));
			else {
				Date dateValue = (Date) resultMap.get("dateValue");
				Date modifiedDate;
				if (resultMap.get("modifiedDate") == null) {
					modifiedDate = (Date) resultMap.get("recordedDate");
				} else {
					modifiedDate = (Date) resultMap.get("modifiedDate");
				}
				Integer secondsDelta = requestDTO.getSecondsDelta();
				logger.debug("dateValue: " + dateValue.getTime());
				logger.debug("modifiedDate: " + modifiedDate.getTime());
				if (requestDTO.getNewerThanModified()) {
					if (dateValue.getTime() - secondsDelta * 1000 > modifiedDate.getTime())
						protocolCodes.add((String) resultMap.get("codeName"));
				} else {
					if (dateValue.getTime() + secondsDelta * 1000 < modifiedDate.getTime())
						protocolCodes.add((String) resultMap.get("codeName"));
				}
			}
		}
		return protocolCodes;
	}

}
