

package com.labsynch.labseer.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.InteractionType;
import com.labsynch.labseer.domain.ItxLsThingLsThing;
import com.labsynch.labseer.domain.ItxLsThingLsThingState;
import com.labsynch.labseer.domain.ItxLsThingLsThingValue;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.LsThingLabel;
import com.labsynch.labseer.domain.Protocol;
import com.labsynch.labseer.domain.ThingKind;
import com.labsynch.labseer.domain.ThingType;
import com.labsynch.labseer.dto.LsThingValidationDTO;
import com.labsynch.labseer.dto.PreferredNameDTO;
import com.labsynch.labseer.dto.PreferredNameRequestDTO;
import com.labsynch.labseer.dto.PreferredNameResultsDTO;
import com.labsynch.labseer.dto.ValuePathDTO;
import com.labsynch.labseer.dto.ValueRuleDTO;
import com.labsynch.labseer.exceptions.ErrorMessage;
import com.labsynch.labseer.exceptions.UniqueNameException;
import com.labsynch.labseer.utils.ExcludeNulls;
import com.labsynch.labseer.utils.PropertiesUtilService;

import flexjson.JSONSerializer;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class LsThingPreferredIdServiceTests {

	private static final Logger logger = LoggerFactory.getLogger(LsThingPreferredIdServiceTests.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;
	
	@Autowired
	private LsThingService lsThingService;
	

	//@Test
	public void Test_1(){
				
		PreferredNameRequestDTO inputData = new PreferredNameRequestDTO();
		List<PreferredNameDTO> requests = new ArrayList<PreferredNameDTO>();
		String[] inputNames = {"1", "2", "2","2", "27", "380653"};
		for (String name : inputNames){
			PreferredNameDTO requestName = new PreferredNameDTO();
			requestName.setRequestName(name);
			requests.add(requestName);
		}
		inputData.setRequests(requests);
		
		logger.info("here is the input requests: " + inputData.toJson());
		
    	PreferredNameRequestDTO requestDTO = PreferredNameRequestDTO.fromJsonToPreferredNameRequestDTO(inputData.toJson());
        String thingType = "gene";
        String thingKind = "entrez gene";
        String labelType = "name";
        String labelKind = "Entrez Gene ID";
        logger.info("getGeneCodeNameFromNameRequest incoming json: " + requestDTO.toJson());
        PreferredNameResultsDTO results = lsThingService.getPreferredNameFromName(thingType, thingKind, labelType, labelKind, inputData);
   
		Assert.assertEquals(6, results.getResults().size());

		logger.info("#############################################");
		logger.info(results.toJson());

		
	}
	
//	@Test
	public void Test_2(){
				
		PreferredNameRequestDTO inputData = new PreferredNameRequestDTO();
		List<PreferredNameDTO> requests = new ArrayList<PreferredNameDTO>();
		for (int i = 0; i < 2500; i++){
			PreferredNameDTO requestName = new PreferredNameDTO();
			requestName.setRequestName(Integer.toString(i));
			requests.add(requestName);
		}
		inputData.setRequests(requests);
		
		logger.info("here is the input requests: " + inputData.toJson());
		
    	PreferredNameRequestDTO requestDTO = PreferredNameRequestDTO.fromJsonToPreferredNameRequestDTO(inputData.toJson());
        String thingType = "gene";
        String thingKind = "entrez gene";
        String labelType = "name";
        String labelKind = "Entrez Gene ID";
        logger.info("getGeneCodeNameFromNameRequest incoming json: " + requestDTO.toJson());
        PreferredNameResultsDTO results = lsThingService.getPreferredNameFromName(thingType, thingKind, labelType, labelKind, inputData);
   
		Assert.assertEquals(2500, results.getResults().size());


		logger.info("#############################################");
		logger.info(results.toJson());

		
	}

	@Test
	public void Test_3(){
				
		PreferredNameRequestDTO inputData = new PreferredNameRequestDTO();
		List<PreferredNameDTO> requests = new ArrayList<PreferredNameDTO>();
		String[] inputNames = {"11797", "77616", "633417", "78190", "102633673", "72971", "101056086", "100132941", "100288144"};
		for (String name : inputNames){
			PreferredNameDTO requestName = new PreferredNameDTO();
			requestName.setRequestName(name);
			requests.add(requestName);
		}
		inputData.setRequests(requests);
		
		logger.info("here is the input requests: " + inputData.toJson());
		
    	PreferredNameRequestDTO requestDTO = PreferredNameRequestDTO.fromJsonToPreferredNameRequestDTO(inputData.toJson());
        String thingType = "gene";
        String thingKind = "entrez gene";
        String labelType = "name";
        String labelKind = "Entrez Gene ID";
        logger.info("getGeneCodeNameFromNameRequest incoming json: " + requestDTO.toJson());
        PreferredNameResultsDTO results = lsThingService.getPreferredNameFromName(thingType, thingKind, labelType, labelKind, inputData);
   
		Assert.assertEquals(9, results.getResults().size());

		logger.info("#############################################");
		logger.info(results.toJson());

		
	}
	
	
}
