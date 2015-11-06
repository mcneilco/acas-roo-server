

package com.labsynch.labseer.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.NoResultException;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.AnalysisGroupState;
import com.labsynch.labseer.domain.AnalysisGroupValue;
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ExperimentValue;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.LsThingLabel;
import com.labsynch.labseer.domain.SubjectValue;
import com.labsynch.labseer.dto.AnalysisGroupValueDTO;
import com.labsynch.labseer.dto.ExperimentSearchRequestDTO;
import com.labsynch.labseer.dto.PreferredNameDTO;
import com.labsynch.labseer.dto.PreferredNameResultsDTO;

import flexjson.JSONTokener;


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
		experimentCodeList.add("EXPT-00000003");
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
