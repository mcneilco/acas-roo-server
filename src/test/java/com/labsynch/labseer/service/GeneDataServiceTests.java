

package com.labsynch.labseer.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.labsynch.labseer.domain.AnalysisGroupValue;
import com.labsynch.labseer.dto.AnalysisGroupValueDTO;
import com.labsynch.labseer.dto.PreferredNameDTO;
import com.labsynch.labseer.dto.PreferredNameRequestDTO;
import com.labsynch.labseer.dto.PreferredNameResultsDTO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml")
@Configurable
public class GeneDataServiceTests {

	private static final Logger logger = LoggerFactory.getLogger(GeneDataServiceTests.class);
	
    @Autowired
    private LsThingService lsThingService;

	@Autowired
	private GeneThingService geneThingService;
	

	@Autowired
	private ExperimentService experimentService;


	// user enters an Entrez Gene ID to fetch all experiment data
	// service return an annotated DTO for display in Gene Page
	// return data as a long form
	
	// input: gene ID = 5
	// 
	
	@Test
	public void Test_1() throws IOException{
		String geneId = "30970";
		PreferredNameRequestDTO requestDTO = new PreferredNameRequestDTO();
		Collection<PreferredNameDTO> requests = new ArrayList<PreferredNameDTO>();
		PreferredNameDTO nameDTO = new PreferredNameDTO();
		nameDTO.setRequestName(geneId);
		requests.add(nameDTO);
		requestDTO.setRequests(requests);
		PreferredNameResultsDTO resultNames = lsThingService.getGeneCodeNameFromName(requestDTO.toJson());
		
		Set<String> geneCodeList = new HashSet<String>();

		Collection<PreferredNameDTO> preferredNames = resultNames.getResults();
		for (PreferredNameDTO preferredName : preferredNames){
			logger.info(preferredName.toJson());		
			geneCodeList.add(preferredName.getReferenceName());
		}
		
		Boolean publicData = true;
		Collection<AnalysisGroupValueDTO> agValues = null;
		try {
			if (publicData){
				agValues = AnalysisGroupValue.findAnalysisGroupValueDTO(geneCodeList, publicData).getResultList();
			} else {
				agValues = AnalysisGroupValue.findAnalysisGroupValueDTO(geneCodeList).getResultList();
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		
		logger.info(AnalysisGroupValueDTO.toJsonArray(agValues));
		
		
	}
	


}
