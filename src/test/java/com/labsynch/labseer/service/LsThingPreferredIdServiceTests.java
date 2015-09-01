

package com.labsynch.labseer.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.labsynch.labseer.dto.PreferredNameDTO;
import com.labsynch.labseer.dto.PreferredNameRequestDTO;
import com.labsynch.labseer.dto.PreferredNameResultsDTO;
import com.labsynch.labseer.utils.PropertiesUtilService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class LsThingPreferredIdServiceTests {

	private static final Logger logger = LoggerFactory.getLogger(LsThingPreferredIdServiceTests.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;
	
	@Autowired
	private LsThingService lsThingService;
	

	@Test
	public void Test_1(){
				
		PreferredNameRequestDTO inputData = new PreferredNameRequestDTO();
		List<PreferredNameDTO> requests = new ArrayList<PreferredNameDTO>();
		String[] inputNames = {"1", "2", "2","2", "27"};
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
   
		Assert.assertEquals(5, results.getResults().size());

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
		String[] inputNames = {"380653", "380654", "633417", "664857"};
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
   
		Assert.assertEquals(2, results.getResults().size());

		logger.info("#############################################");
		logger.info(results.toJson());

	}
	
	
}
