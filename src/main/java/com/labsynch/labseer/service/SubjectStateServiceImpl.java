package com.labsynch.labseer.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.domain.SubjectState;
import com.labsynch.labseer.domain.SubjectValue;
import com.labsynch.labseer.dto.GenericStatePathRequest;
import com.labsynch.labseer.dto.SubjectCodeStateDTO;
import com.labsynch.labseer.dto.SubjectStatePathDTO;
import com.labsynch.labseer.utils.PropertiesUtilService;
import com.labsynch.labseer.utils.SimpleUtil;

@Service
@Transactional
public class SubjectStateServiceImpl implements SubjectStateService {

	private static final Logger logger = LoggerFactory.getLogger(SubjectStateServiceImpl.class);
	
	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Override
	public Collection<SubjectState> ignoreAllSubjectStates(Collection<SubjectState> subjectStates) {
		//mark subjectStates and values as ignore 
		Collection<SubjectState> subjectStateSet = new HashSet<SubjectState>();
		for (SubjectState querySubjectState : subjectStates){
			SubjectState subjectState = SubjectState.findSubjectState(querySubjectState.getId());			
				for(SubjectValue subjectValue : SubjectValue.findSubjectValuesByLsState(subjectState).getResultList()){
					subjectValue.setIgnored(true);
					subjectValue.merge();
				}
				subjectState.setIgnored(true);
				subjectState.merge();
				subjectStateSet.add(SubjectState.findSubjectState(subjectState.getId()));
		}

		return(subjectStateSet);

	}

	@Override
	public List<SubjectState> getSubjectStatesBySubjectIdAndStateTypeKind(
			Long subjectId, String stateType, String stateKind) {
		
			List<SubjectState> subjectStates = SubjectState.findSubjectStatesBySubjectIDAndStateTypeKind(subjectId, stateType, stateKind).getResultList();

			return subjectStates;
	}

	@Override
	public SubjectState createSubjectStateBySubjectIdAndStateTypeKind(
			Long subjectId, String stateType, String stateKind) {
		SubjectState subjectState = new SubjectState();
		Subject subject = Subject.findSubject(subjectId);
		subjectState.setSubject(subject);
		subjectState.setLsType(stateType);
		subjectState.setLsKind(stateKind);
		subjectState.setRecordedBy("default");
		subjectState.persist();
		return subjectState;
	}

	@Override
	public SubjectState saveSubjectState(
			SubjectState subjectState) {
		subjectState.setSubject(Subject.findSubject(subjectState.getSubject().getId()));		
		subjectState.persist();
		Set<SubjectValue> savedValues = new HashSet<SubjectValue>();
		for (SubjectValue subjectValue : subjectState.getLsValues()){
			subjectValue.setLsState(subjectState);
			subjectValue.persist();
			savedValues.add(subjectValue);
		}
		subjectState.setLsValues(savedValues);
		subjectState.merge();
		return subjectState;
	}

	@Override
	public Collection<SubjectState> saveSubjectStates(
			Collection<SubjectState> subjectStates) {
		for (SubjectState subjectState: subjectStates) {
			subjectState = saveSubjectState(subjectState);
		}
		return subjectStates;
	}
	
	@Override
	public SubjectState updateSubjectState(
			SubjectState subjectState) {
		subjectState.setVersion(SubjectState.findSubjectState(subjectState.getId()).getVersion());
		subjectState.merge();
		return subjectState;
	}

	@Override
	public Collection<SubjectState> updateSubjectStates(
			Collection<SubjectState> subjectStates) {
		for (SubjectState subjectState : subjectStates){
			subjectState = updateSubjectState(subjectState);
		}
		return null;
	}
	
	@Override
	public SubjectState getSubjectState(String idOrCodeName,
			String stateType, String stateKind) {
		SubjectState state = null;
		try{
			Collection<SubjectState> states = getSubjectStates(idOrCodeName, stateType, stateKind);
			state = states.iterator().next();
		}catch (Exception e){
			logger.error("Caught error "+e.toString()+" trying to find a state.",e);
			state = null;
		}
		return state;
	}

	@Override
	public Collection<SubjectStatePathDTO> getSubjectStates(
			Collection<GenericStatePathRequest> genericRequests) {
		Collection<SubjectStatePathDTO> results = new ArrayList<SubjectStatePathDTO>();
		Map<String,Map<String, Set<String>>> stateTypeKindSubjectCodeMap = new HashMap<String, Map<String, Set<String>>>();
		for (GenericStatePathRequest request : genericRequests){
			//sort incoming requests by state type and kind to group them
			if (stateTypeKindSubjectCodeMap.containsKey(request.getStateType())){
				//entry for state type exists. Check if state kind already seen
				Map<String, Set<String>> stateKindSubjectCodeMap = stateTypeKindSubjectCodeMap.get(request.getStateType());
				if (stateKindSubjectCodeMap.containsKey(request.getStateKind())){
					//state type and kind seen before. add codeName to the list
					Set<String> subjectCodes = stateKindSubjectCodeMap.get(request.getStateKind());
					subjectCodes.add(request.getIdOrCodeName());
					stateKindSubjectCodeMap.put(request.getStateKind(), subjectCodes);
				}else{
					//state kind not seen yet
					Set<String> subjectCodes = new HashSet<String>();
					subjectCodes.add(request.getIdOrCodeName());
					stateKindSubjectCodeMap.put(request.getStateKind(), subjectCodes);
					stateTypeKindSubjectCodeMap.put(request.getStateType(), stateKindSubjectCodeMap);
				}
				stateTypeKindSubjectCodeMap.put(request.getStateType(), stateKindSubjectCodeMap);
			}else{
				//no entry for state type or kind. create a new one
				Map<String, Set<String>> stateKindSubjectCodeMap = new HashMap<String, Set<String>>();
				Set<String> subjectCodes = new HashSet<String>();
				subjectCodes.add(request.getIdOrCodeName());
				stateKindSubjectCodeMap.put(request.getStateKind(), subjectCodes);
				stateTypeKindSubjectCodeMap.put(request.getStateType(), stateKindSubjectCodeMap);
			}
		}
		for (String stateType : stateTypeKindSubjectCodeMap.keySet()){
			for (String stateKind : stateTypeKindSubjectCodeMap.get(stateType).keySet()){
				Collection<String> subjectCodes = stateTypeKindSubjectCodeMap.get(stateType).get(stateKind);
				Collection<SubjectCodeStateDTO> codeStateDTOs = getSubjectCodesAndStatesByStateTypeKindAndSubjectCodes(subjectCodes, stateType, stateKind);
				Map<String, Collection<SubjectState>> codeStatesMap = new HashMap<String, Collection<SubjectState>>();
				for (SubjectCodeStateDTO codeStateDTO : codeStateDTOs){
					if (codeStatesMap.containsKey(codeStateDTO.getSubjectCode())){
						Collection<SubjectState> states = codeStatesMap.get(codeStateDTO.getSubjectCode());
						states.add(codeStateDTO.getSubjectState());
						codeStatesMap.put(codeStateDTO.getSubjectCode(), states);
					}else{
						Collection<SubjectState> states = new ArrayList<SubjectState>();
						states.add(codeStateDTO.getSubjectState());
						codeStatesMap.put(codeStateDTO.getSubjectCode(), states);
					}
				}
				for (String subjectCode : codeStatesMap.keySet()){
					SubjectStatePathDTO result = new SubjectStatePathDTO();
					result.setIdOrCodeName(subjectCode);
					result.setStateType(stateType);
					result.setStateKind(stateKind);
					result.setStates(codeStatesMap.get(subjectCode));
					results.add(result);
				}
			}
		}
		return results;
	}
	
	private Collection<SubjectState> getSubjectStates(String idOrCodeName, String stateType, String stateKind){
		if (SimpleUtil.isNumeric(idOrCodeName)){
			Long id = Long.valueOf(idOrCodeName);
			return SubjectState.findSubjectStatesBySubjectIDAndStateTypeKind(id, stateType, stateKind).getResultList();
		}else{
			return SubjectState.findSubjectStatesBySubjectCodeNameAndStateTypeKind(idOrCodeName, stateType, stateKind).getResultList();
		}
	}
	
	private Collection<SubjectCodeStateDTO> getSubjectCodesAndStatesByStateTypeKindAndSubjectCodes(Collection<String> subjectCodes, String stateType, String stateKind){
		if (stateType == null || stateKind.length() == 0) throw new IllegalArgumentException("The stateType argument is required");
		if (stateKind == null || stateKind.length() == 0) throw new IllegalArgumentException("The stateKind argument is required");
		List<String> codeNameList = new ArrayList<String>();
		codeNameList.addAll(subjectCodes);
		EntityManager em = SubjectState.entityManager();
		String hsqlQuery = "SELECT new com.labsynch.labseer.dto.SubjectCodeStateDTO( s.codeName, ss ) FROM SubjectState AS ss " +
		"JOIN ss.subject AS s " +
		"WHERE ss.lsType = :stateType AND ss.lsKind = :stateKind AND ss.ignored IS NOT :ignored AND ";
		Query q = SimpleUtil.addHqlInClause(em, hsqlQuery, "s.codeName", codeNameList);
		q.setParameter("stateType", stateType);
		q.setParameter("stateKind", stateKind);
		q.setParameter("ignored", true);
		logger.debug("Querying for state type: "+stateType+" and state kind: "+stateKind);
		logger.debug(q.unwrap(org.hibernate.Query.class).getQueryString());
		Collection<SubjectCodeStateDTO> results = q.getResultList();
		logger.debug("Fetched "+results.size()+" states");
		return results;
	}

}
