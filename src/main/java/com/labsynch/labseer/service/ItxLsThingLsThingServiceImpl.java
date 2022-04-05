package com.labsynch.labseer.service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.map.MultiValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.persistence.NoResultException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.ItxLsThingLsThing;
import com.labsynch.labseer.domain.ItxLsThingLsThingState;
import com.labsynch.labseer.domain.ItxLsThingLsThingValue;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.LsThingLabel;
import com.labsynch.labseer.domain.LsThingState;
import com.labsynch.labseer.domain.LsThingValue;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.ErrorMessageDTO;
import com.labsynch.labseer.dto.LsThingValidationDTO;
import com.labsynch.labseer.dto.PreferredNameDTO;
import com.labsynch.labseer.dto.PreferredNameRequestDTO;
import com.labsynch.labseer.dto.PreferredNameResultsDTO;
import com.labsynch.labseer.dto.ValuePathDTO;
import com.labsynch.labseer.dto.ValueRuleDTO;
import com.labsynch.labseer.exceptions.ErrorMessage;
import com.labsynch.labseer.exceptions.UniqueInteractionsException;
import com.labsynch.labseer.exceptions.UniqueNameException;
import com.labsynch.labseer.utils.ItxLsThingLsThingComparator;
import com.labsynch.labseer.utils.LsThingComparatorByBatchNumber;
import com.labsynch.labseer.utils.LsThingComparatorByCodeName;
import com.labsynch.labseer.utils.PropertiesUtilService;
import com.labsynch.labseer.utils.SimpleUtil;

@Service
public class ItxLsThingLsThingServiceImpl implements ItxLsThingLsThingService {

	private static final Logger logger = LoggerFactory.getLogger(ItxLsThingLsThingServiceImpl.class);

	@Autowired
	private AutoLabelService autoLabelService;
	
	@Autowired
	private PropertiesUtilService propertiesUtilService;
	
	
	@Override
	public ItxLsThingLsThing saveItxLsThingLsThing(ItxLsThingLsThing itxLsThingLsThing){
		ItxLsThingLsThing newItxLsThingLsThing = new ItxLsThingLsThing(itxLsThingLsThing);
		newItxLsThingLsThing.persist();

		if(itxLsThingLsThing.getLsStates() != null){
			Set<ItxLsThingLsThingState> lsStates = new HashSet<ItxLsThingLsThingState>();
			for(ItxLsThingLsThingState itxLsThingLsThingState : itxLsThingLsThing.getLsStates()){
				ItxLsThingLsThingState newItxLsThingLsThingState = new ItxLsThingLsThingState(itxLsThingLsThingState);
				newItxLsThingLsThingState.setItxLsThingLsThing(newItxLsThingLsThing);
				logger.debug("here is the newItxLsThingLsThingState before save: " + newItxLsThingLsThingState.toJson());
				newItxLsThingLsThingState.persist();
				logger.debug("persisted the newItxLsThingLsThingState: " + newItxLsThingLsThingState.toJson());
				
				if (itxLsThingLsThingState.getLsValues() != null){
					Set<ItxLsThingLsThingValue> lsValues = new HashSet<ItxLsThingLsThingValue>();
					for(ItxLsThingLsThingValue itxLsThingLsThingValue : itxLsThingLsThingState.getLsValues()){
						logger.debug("itxLsThingLsThingValue: " + itxLsThingLsThingValue.toJson());
						ItxLsThingLsThingValue newItxLsThingLsThingValue = new ItxLsThingLsThingValue(itxLsThingLsThingValue);
						newItxLsThingLsThingValue.setLsState(newItxLsThingLsThingState);
						newItxLsThingLsThingValue.persist();
						lsValues.add(newItxLsThingLsThingValue);
						logger.debug("persisted the itxLsThingLsThingValue: " + newItxLsThingLsThingValue.toJson());
					}	
					newItxLsThingLsThingState.setLsValues(lsValues);
				} else {
					logger.debug("No itxLsThingLsThing values to save");
				}
				lsStates.add(newItxLsThingLsThingState);
			}
			newItxLsThingLsThing.setLsStates(lsStates);
		}
		return newItxLsThingLsThing;
	}
	
//	public void updateItxLsStates(ItxLsThingLsThing jsonItxLsThingLsThing, ItxLsThingLsThing updatedItxLsThingLsThing){
//		if(jsonItxLsThingLsThing.getLsStates() != null){
//			for(ItxLsThingLsThingState itxLsThingLsThingState : jsonItxLsThingLsThing.getLsStates()){
//				ItxLsThingLsThingState updatedItxLsThingLsThingState;
//				if (itxLsThingLsThingState.getId() == null){
//					updatedItxLsThingLsThingState = new ItxLsThingLsThingState(itxLsThingLsThingState);
//					updatedItxLsThingLsThingState.setItxLsThingLsThing(updatedItxLsThingLsThing);
//					updatedItxLsThingLsThingState.persist();
//					updatedItxLsThingLsThing.getLsStates().add(updatedItxLsThingLsThingState);
//				} else {
//					updatedItxLsThingLsThingState = ItxLsThingLsThingState.updateNoMerge(itxLsThingLsThingState);
//					updatedItxLsThingLsThingState.setItxLsThingLsThing(updatedItxLsThingLsThing);
//					logger.debug("updated itxLsThingLsThing state " + updatedItxLsThingLsThingState.getId());
//
//				}
//				if (itxLsThingLsThingState.getLsValues() != null){
//					for(ItxLsThingLsThingValue itxLsThingLsThingValue : itxLsThingLsThingState.getLsValues()){
//						ItxLsThingLsThingValue updatedItxLsThingLsThingValue;
//						if (itxLsThingLsThingValue.getId() == null){
//							updatedItxLsThingLsThingValue = ItxLsThingLsThingValue.create(itxLsThingLsThingValue);
//							updatedItxLsThingLsThingValue.setLsState(ItxLsThingLsThingState.findItxLsThingLsThingState(updatedItxLsThingLsThingState.getId()));
//							updatedItxLsThingLsThingValue.persist();
//							updatedItxLsThingLsThingState.getLsValues().add(updatedItxLsThingLsThingValue);
//						} else {
//							itxLsThingLsThingValue.setLsState(updatedItxLsThingLsThingState);
//							updatedItxLsThingLsThingValue = ItxLsThingLsThingValue.update(itxLsThingLsThingValue);
//							updatedItxLsThingLsThingState.getLsValues().add(updatedItxLsThingLsThingValue);
//							logger.debug("updated itxLsThingLsThing value " + updatedItxLsThingLsThingValue.getId());
//						}
//					}	
//				} else {
//					logger.debug("No itxLsThingLsThing values to update");
//				}
//			}
//		}
//	}
	
	@Override
	@Transactional
	public ItxLsThingLsThing updateItxLsThingLsThing(ItxLsThingLsThing jsonItxLsThingLsThing){
		
		ItxLsThingLsThing updatedItxLsThingLsThing = ItxLsThingLsThing.updateNoStates(jsonItxLsThingLsThing);
		updatedItxLsThingLsThing.merge();
		logger.debug("here is the updated itx: " + updatedItxLsThingLsThing.toJson());
		logger.debug("----------------- here is the itx id " + updatedItxLsThingLsThing.getId() + "   -----------");
		
		if(jsonItxLsThingLsThing.getLsStates() != null){
			for(ItxLsThingLsThingState itxLsThingLsThingState : jsonItxLsThingLsThing.getLsStates()){
				logger.debug("-------- current itxLsThingLsThingState ID: " + itxLsThingLsThingState.getId());
				ItxLsThingLsThingState updatedItxLsThingLsThingState;
				if (itxLsThingLsThingState.getId() == null){
					updatedItxLsThingLsThingState = new ItxLsThingLsThingState(itxLsThingLsThingState);
					updatedItxLsThingLsThingState.setItxLsThingLsThing(ItxLsThingLsThing.findItxLsThingLsThing(updatedItxLsThingLsThing.getId()));
					updatedItxLsThingLsThingState.persist();
					updatedItxLsThingLsThing.getLsStates().add(updatedItxLsThingLsThingState);
				} else {

					if (itxLsThingLsThingState.getItxLsThingLsThing() == null) itxLsThingLsThingState.setItxLsThingLsThing(updatedItxLsThingLsThing);
					updatedItxLsThingLsThingState = ItxLsThingLsThingState.update(itxLsThingLsThingState);			
					updatedItxLsThingLsThing.getLsStates().add(updatedItxLsThingLsThingState);
					logger.debug("updated itxLsThingLsThing state " + updatedItxLsThingLsThingState.getId());

				}
				if (itxLsThingLsThingState.getLsValues() != null){
					for(ItxLsThingLsThingValue itxLsThingLsThingValue : itxLsThingLsThingState.getLsValues()){
						ItxLsThingLsThingValue updatedItxLsThingLsThingValue;
						if (itxLsThingLsThingValue.getId() == null){
							updatedItxLsThingLsThingValue = ItxLsThingLsThingValue.create(itxLsThingLsThingValue);
							updatedItxLsThingLsThingValue.setLsState(updatedItxLsThingLsThingState);
							updatedItxLsThingLsThingValue.persist();
							updatedItxLsThingLsThingState.getLsValues().add(updatedItxLsThingLsThingValue);

						} else {
							//itxLsThingLsThingValue.setLsState(updatedItxLsThingLsThingState);
							itxLsThingLsThingValue.setLsState(updatedItxLsThingLsThingState);
							updatedItxLsThingLsThingValue = ItxLsThingLsThingValue.update(itxLsThingLsThingValue);
							updatedItxLsThingLsThingState.getLsValues().add(updatedItxLsThingLsThingValue);
						}
					}	
				} else {
					logger.debug("No itxLsThingLsThing values to update");
				}
			}
		}
		
		return updatedItxLsThingLsThing;
	}

	@Override
	public Collection<CodeTableDTO> convertToCodeTables(Collection<ItxLsThingLsThing> itxLsThingLsThings) {
		Collection<CodeTableDTO> codeTables = new ArrayList<CodeTableDTO>();
		for (ItxLsThingLsThing itx : itxLsThingLsThings){
			CodeTableDTO codeTable = new CodeTableDTO();
			codeTable.setId(itx.getId());
			codeTable.setCode(itx.getCodeName());
			codeTable.setName(itx.getCodeName());
			codeTable.setIgnored(itx.isIgnored());
			codeTables.add(codeTable);
		}
		
		return codeTables;
	}

//	@Override
//	@Transactional
//	public ItxLsThingLsThing updateItxLsThingLsThing(ItxLsThingLsThing jsonItxLsThingLsThing) {
//		ItxLsThingLsThing updatedItxLsThingLsThing;
//		jsonItxLsThingLsThing.setFirstLsThing(LsThing.findLsThing(jsonItxLsThingLsThing.getFirstLsThing().getId()));
//		jsonItxLsThingLsThing.setSecondLsThing(LsThing.findLsThing(jsonItxLsThingLsThing.getSecondLsThing().getId()));
//		if (jsonItxLsThingLsThing.getId() == null){
//			//need to save a new itx
//			logger.debug("saving new itxLsThingLsThing: " + jsonItxLsThingLsThing.toJson());
//			updatedItxLsThingLsThing = saveItxLsThingLsThing(jsonItxLsThingLsThing);
//		}else {
//			//old itx needs to be updated
//			updatedItxLsThingLsThing = ItxLsThingLsThing.update(jsonItxLsThingLsThing);
//		}
////		ItxLsThingLsThingState
//		ItxLsThingLsThing returnObject = ItxLsThingLsThing.findItxLsThingLsThing(updatedItxLsThingLsThing.getId());
////		Set<ItxLsThingLsThingState> lsStates = ItxLsThingLsThingState.f
////		returnObject.setLsStates(lsStates);
//		return returnObject;
//	}
}
