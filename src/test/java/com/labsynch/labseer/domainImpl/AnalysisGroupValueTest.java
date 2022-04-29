package com.labsynch.labseer.domainImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.NoResultException;

import com.labsynch.labseer.domain.AnalysisGroupValue;
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.dto.AnalysisGroupValueDTO;
import com.labsynch.labseer.dto.ExperimentSearchRequestDTO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class AnalysisGroupValueTest {
	
	private static final Logger logger = LoggerFactory.getLogger(AnalysisGroupValueTest.class);
	
	//@Test
	@Transactional
    public void getLsthingsByName() {
    	String lsType = "gene";
		String lsKind = "entrez gene";
		String labelType = "name";
		String labelKind = "Entrez Gene ID";
		String labelText = "47767";
		List<LsThing> results = LsThing.findLsThingByLabelText(lsType, lsKind, labelType, labelKind, labelText).getResultList();
		logger.info("query labelText: " + labelText + " number of results: " + results.size());
		if (results.size()>0){
			for(LsThing result : results){
				//12597
				logger.info("attempting to output JSON: -----------------  " + result.getCodeName() + "  " + result.getId());
//				LsThing newThing = LsThing.findLsThing(result.getId());
//				Set<LsTag> tags = newThing.getLsTags();
//				for (LsTag tag:tags){
//					logger.info("tag here: " + tag.toJson());
//				}
				logger.info(result.toJson());
			}
		}
	}
	
		@Test
	@Transactional
	public void QueryAnalysisGroupValueByExpIdAndStateTypeKind(){
			
		Long experimentId = 9L;
		String stateType = "data";
		String stateKind = "Generic";
		List<AnalysisGroupValue> results = AnalysisGroupValue.findAnalysisGroupValuesByExptIDAndStateTypeKind(experimentId, stateType, stateKind).getResultList();
		logger.info(AnalysisGroupValue.toJsonArray(results));
		assert(results.size() == 11);
	}
	
	//@Test
	@Transactional
	public void QueryAnalysisGroupValueByExpIdAndStateTypeKindWithBadData() {
		Long experimentId = 9L;
		String stateType = "";
		String stateKind = "experiment metadata";
		List<AnalysisGroupValue> results = new ArrayList<AnalysisGroupValue>();
		try {
			results = AnalysisGroupValue.findAnalysisGroupValuesByExptIDAndStateTypeKind(experimentId, stateType, stateKind).getResultList();
		} catch(IllegalArgumentException ex ) {
			logger.info(ex.getMessage());
		}
		assert(results.size() == 0);
	}
	
	//@Test
	@Transactional
	public void QueryAnalysisGroupValueByExpIdAndStateTypeKindWithCodeName() {
		String experimentCodeName = "EXPT-00000003";
		String stateType = "data";
		String stateKind = "Generic";
		Experiment experiment = null;
		boolean didCatch = false;
		try {
			experiment = Experiment.findExperimentsByCodeNameEquals(experimentCodeName).getSingleResult();
		} catch(NoResultException nre) {
			logger.info(nre.getMessage());
			didCatch = true;
		}
		List<AnalysisGroupValue> results = new ArrayList<AnalysisGroupValue>();
		try {
			results = AnalysisGroupValue.findAnalysisGroupValuesByExptIDAndStateTypeKind(experiment.getId(), stateType, stateKind).getResultList();
		} catch(IllegalArgumentException ex ) {
			logger.info(ex.getMessage());
			assert(results.size() == 0);
			didCatch = true;
		}
		if(!didCatch) assert(results.size() == 11);
	}
	
	//@Test
	@Transactional
	public void QueryAnalysisGroupValueByExpIdAndStateTypeKindAndValueTypeKind(){
			
		Long experimentId = 9L;
		String stateType = "data";
		String stateKind = "Generic";
		String valueType = "numericValue";
		String valueKind = "solubility";
		List<AnalysisGroupValue> results = AnalysisGroupValue.findAnalysisGroupValuesByExptIDAndStateTypeKindAndValueTypeKind(experimentId, stateType, stateKind, valueType, valueKind).getResultList();
		logger.info(AnalysisGroupValue.toJsonArray(results));
		assert(results.size() == 2);
	}
	
	//@Test
	@Transactional
	public void QueryAnalysisGroupValueByAnalysisGroupIdStateTypeAndKind() {
		Long analysisGroupId = 10L;
		String stateType = "data";
		String stateKind = "Generic";
		List<AnalysisGroupValue> analysisGroupValues = AnalysisGroupValue.findAnalysisGroupValuesByAnalysisGroupIDAndStateTypeKind(analysisGroupId, stateType, stateKind).getResultList();
		logger.info(String.valueOf(analysisGroupValues.size()));
	}
	
	//@Test
	@Transactional
	public void QueryAnalysisGroupValueByAnalysisGroupIdStateTypeAndKindAndValueTypeKind() {
		Long analysisGroupId = 10L;
		String stateType = "data";
		String stateKind = "Generic";
		String valueType = "stringValue";
		String valueKind = "Assay Comment";
		List<AnalysisGroupValue> analysisGroupValues = AnalysisGroupValue.findAnalysisGroupValuesByAnalysisGroupIDAndStateTypeKindAndValueTypeKind(analysisGroupId, stateType, stateKind, valueType, valueKind).getResultList();
		logger.info(String.valueOf(analysisGroupValues.size()));
	}
	
	@Test
	@Transactional
	public void queryAGDataTest() {
		Set<String> finalUniqueBatchCodes = new HashSet<String>();
		finalUniqueBatchCodes.add("CMPD-0000001-01A");
		finalUniqueBatchCodes.add("CMPD-0000002-01A");
		finalUniqueBatchCodes.add("CMPD-0000003-01A");

		ExperimentSearchRequestDTO searchRequest = new ExperimentSearchRequestDTO();
		Set<String> experimentCodeList = new HashSet<String>();
		experimentCodeList.add("EXPT-00000036");
		searchRequest.setExperimentCodeList(experimentCodeList);
		searchRequest.setBatchCodeList(finalUniqueBatchCodes);
		logger.info(searchRequest.toJson());
		boolean onlyPublicData = true;
		List<AnalysisGroupValueDTO> results = AnalysisGroupValue.findAnalysisGroupValueDTO(finalUniqueBatchCodes, searchRequest.getExperimentCodeList(), onlyPublicData);
		logger.info("number of results found: " + results.size());
		logger.info(AnalysisGroupValueDTO.toPrettyJsonArray(results));
		
//		Experiment experiment = Experiment.findExperiment(17151L);
//		logger.info(experiment.toJson());
		
	}

	
	
}
