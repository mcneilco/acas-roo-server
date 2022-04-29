

package com.labsynch.labseer.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.labsynch.labseer.dto.AnalysisGroupValueDTO;
import com.labsynch.labseer.dto.ExperimentSearchRequestDTO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class AnalysisGroupValueServiceTest2 {


	@Autowired
	private ExperimentService experimentService;
	
	private static final Logger logger = LoggerFactory.getLogger(AnalysisGroupValueServiceTest2.class);

	private static final int HashSet = 0;


	@Autowired
	private LsThingService lsThingService;
	
	@Autowired
	private AnalysisGroupValueService analysisGroupValueService;

	@Test
	public void SimpleTest_1(){
		
		
		
//		String searchRequestJSON = "";
//		ExperimentSearchRequestDTO searchRequest = ExperimentSearchRequestDTO.fromJsonToExperimentSearchRequestDTO(searchRequestJSON );
		
		ExperimentSearchRequestDTO searchRequest = new ExperimentSearchRequestDTO();
		Set<String> experimentCodeList = new HashSet<String>();
		experimentCodeList.add("EXPT-00000005");
		searchRequest.setExperimentCodeList(experimentCodeList );
		
        logger.debug("converted json: " + searchRequest.toJson());
        List<AnalysisGroupValueDTO> agValues = null;
        try {
            Boolean publicData = false;
			agValues = experimentService.getFilteredAGData(searchRequest, publicData );
            logger.debug("number of agvalues found: " + agValues.size());
            for(AnalysisGroupValueDTO ag : agValues){
            	logger.info(ag.toPrettyJson());
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }

	}



}
