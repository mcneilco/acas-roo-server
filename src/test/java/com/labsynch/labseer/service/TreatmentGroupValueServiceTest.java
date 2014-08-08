

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
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.LsThingLabel;
import com.labsynch.labseer.domain.TreatmentGroupValue;
import com.labsynch.labseer.dto.AnalysisGroupValueDTO;
import com.labsynch.labseer.dto.PreferredNameDTO;
import com.labsynch.labseer.dto.PreferredNameResultsDTO;

import flexjson.JSONTokener;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class TreatmentGroupValueServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(TreatmentGroupValueServiceTest.class);

	@Autowired
	private TreatmentGroupValueService treatmentGroupValueService;

	@Test
	@Transactional
	public void QueryTreatmentGroupValueByExpIdAndStateTypeKind(){
			
		Long experimentId = 14L;
		String stateType = "data";
		String stateKind = "test compound treatment";
		List<TreatmentGroupValue> results = treatmentGroupValueService.getTreatmentGroupValuesByExperimentIdAndStateTypeKind(experimentId, stateType, stateKind);
		logger.info(TreatmentGroupValue.toJsonArray(results));
		assert(results.size() == 120);
	}
	
	@Test
	@Transactional
	public void QueryTreatmentGroupValueByExpIdAndStateTypeKindWithBadData() {
		Long experimentId = 14L;
		String stateType = "";
		String stateKind = "experiment metadata";
		List<TreatmentGroupValue> results = new ArrayList<TreatmentGroupValue>();
		try {
			results = treatmentGroupValueService.getTreatmentGroupValuesByExperimentIdAndStateTypeKind(experimentId, stateType, stateKind);
		} catch(IllegalArgumentException ex ) {
			logger.info(ex.getMessage());
		}
		assert(results.size() == 0);
	}
	
	@Test
	@Transactional
	public void QueryTreatmentGroupValueByExpIdAndStateTypeKindWithCodeName() {
		String experimentCodeName = "EXPT-00000004";
		String stateType = "data";
		String stateKind = "test compound treatment";
		Experiment experiment = null;
		boolean didCatch = false;
		try {
			experiment = Experiment.findExperimentsByCodeNameEquals(experimentCodeName).getSingleResult();
		} catch(NoResultException nre) {
			logger.info(nre.getMessage());
			didCatch = true;
		}
		List<TreatmentGroupValue> results = new ArrayList<TreatmentGroupValue>();
		try {
			results = treatmentGroupValueService.getTreatmentGroupValuesByExperimentIdAndStateTypeKind(experiment.getId(), stateType, stateKind);
		} catch(IllegalArgumentException ex ) {
			logger.info(ex.getMessage());
			assert(results.size() == 0);
			didCatch = true;
		}
		if(!didCatch) assert(results.size() == 120);
	}
	
	@Test
	@Transactional
	public void QueryTreatmentGroupValueByExpIdAndStateTypeKindAndValueTypeKind(){
			
		Long experimentId = 14L;
		String stateType = "data";
		String stateKind = "test compound treatment";
		String valueType = "numericValue";
		String valueKind = "Dose";
		List<TreatmentGroupValue> results = treatmentGroupValueService.getTreatmentGroupValuesByExperimentIdAndStateTypeKindAndValueTypeKind(experimentId, stateType, stateKind, valueType, valueKind);
		logger.info(TreatmentGroupValue.toJsonArray(results));
		assert(results.size() == 120);
	}
	
	@Test
	@Transactional
	public void TreatmentGroupValuesToCsv() {
		List<TreatmentGroupValue> treatmentGroupValues = treatmentGroupValueService.getTreatmentGroupValuesByExperimentIdAndStateTypeKind(9l, "metadata", "experiment metadata");
		String csvString = treatmentGroupValueService.getCsvList(treatmentGroupValues);
		assert(csvString != null && csvString.compareTo("") != 0);
		logger.info(csvString);
	}

}
