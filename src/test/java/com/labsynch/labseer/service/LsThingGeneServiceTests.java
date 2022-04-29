
package com.labsynch.labseer.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.labsynch.labseer.domain.AnalysisGroupValue;
import com.labsynch.labseer.dto.AnalysisGroupValueDTO;
import com.labsynch.labseer.dto.ExperimentSearchRequestDTO;
import com.labsynch.labseer.dto.PreferredNameDTO;
import com.labsynch.labseer.dto.PreferredNameResultsDTO;
import com.labsynch.labseer.utils.PropertiesUtilService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import flexjson.JSONDeserializer;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/applicationContext.xml",
		"classpath:/META-INF/spring/applicationContext-security.xml",
		"file:src/main/webapp/WEB-INF/spring/webmvc-config-test.xml" })
@Configurable
@Transactional
public class LsThingGeneServiceTests {

	private static final Logger logger = LoggerFactory.getLogger(LsThingGeneServiceTests.class);

	@Autowired
	private ExperimentService experimentService;

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Autowired
	private LsThingService lsThingService;

	// @Test
	public void Test_1() {

		String json = "{\"requests\":[{\"requestName\":\"CRA-025995-1\"},{\"requestName\":\"CMPD-0000052-01\"}]}";
		PreferredNameDTO request = PreferredNameDTO.fromJsonToPreferredNameDTO(json);

		logger.info("#############################################");

	}

	// @Test
	public void Test_2() {

		String json = "{\"requests\":[{\"requestName\":\"CRA-025995-1\"},{\"requestName\":\"9\"}]}";

		PreferredNameResultsDTO results = lsThingService.getGeneCodeNameFromName(json);
		logger.info("#############################################");
		logger.info("results: " + results.toJson());

	}

	// @Test
	public void Test_3() {

		String json = "{\"requests\":[{\"requestName\":\"31248\"}, {\"requestName\":\"2\"}, {\"requestName\":\"15\"}, {\"requestName\":\"blah\"}]}";

		String thingType = "gene";
		String thingKind = "entrez gene";
		String labelType = "name";
		String labelKind = "Entrez Gene ID";
		logger.info("getGeneCodeNameFromNameRequest incoming json: " + json);
		PreferredNameResultsDTO results = lsThingService.getPreferredNameFromName(thingType, thingKind, labelType,
				labelKind, json);
		logger.info("#############################################");
		logger.info("results: " + results.toJson());

	}

	// @Test
	public void getGeneCodeDataTest() throws Exception {
		String json = "[\"GENE-202547\"]";
		String responseJson = this.mockMvc
				.perform(post("/api/v1/analysisgroupvalues/geneCodeData?format=csv&onlyPublicData=true")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn().getResponse().getContentAsString();
		logger.info(responseJson);
	}

	// @Test
	public void getGeneCodeDataFullTest() {
		String json = "[\"GENE-202547\"]";
		String onlyPublicData = "true";
		logger.debug("incoming json: " + json);
		Collection<String> batchCodes = new JSONDeserializer<List<String>>().use(null, ArrayList.class)
				.use("values", String.class).deserialize(json);
		for (String bc : batchCodes) {
			logger.debug("batch code: " + bc);
		}
		Set<String> geneCodeList = new HashSet<String>();
		geneCodeList.addAll(batchCodes);

		Boolean publicData = false;
		if (onlyPublicData != null && onlyPublicData.equalsIgnoreCase("true")) {
			publicData = true;
		}

		List<AnalysisGroupValueDTO> agValues = null;
		try {
			if (publicData) {
				agValues = AnalysisGroupValue.findAnalysisGroupValueDTO(geneCodeList, publicData).getResultList();
			} else {
				agValues = AnalysisGroupValue.findAnalysisGroupValueDTO(geneCodeList).getResultList();
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}

		logger.info("publicData is " + publicData);
		logger.info("number of values: " + agValues.size());
		logger.info(AnalysisGroupValueDTO.toJsonArray(agValues));

	}

	@Test
	public void getGeneDataTest2() {
		// String json =
		// "{\"experimentCodeList\":[\"EXPT-00000039\"],\"batchCodeList\":[\"GENE-202547\"],\"booleanFilter\":\"and\",\"advancedFilter\":\"\",\"advancedFilterSQL\":\"\"}";
		// String json =
		// "{\"experimentCodeList\":[\"EXPT-00000521\"],\"batchCodeList\":[\"GENE-202547\"],\"booleanFilter\":\"and\",\"advancedFilter\":\"\",\"advancedFilterSQL\":\"\"}";
		String json = "{\"experimentCodeList\":[\"PROT-00000333\",\"EXPT-00000333\",\"tags_EXPT-00000333\",\"PROT-00000283\",\"EXPT-00000283\",\"tags_EXPT-00000283\",\"PROT-00000299\",\"EXPT-00000299\",\"tags_EXPT-00000299\",\"PROT-00000308\",\"EXPT-00000308\",\"tags_EXPT-00000308\",\"PROT-00000302\",\"EXPT-00000302\",\"tags_EXPT-00000302\"],\"batchCodeList\":[],\"searchFilters\":[{\"experimentCode\":\"EXPT-00000283\",\"lsType\":\"numericValue\",\"lsKind\":\"Hit\",\"operator\":\"!=\",\"filterValue\":\"0\",\"termName\":\"Q1\"},{\"experimentCode\":\"EXPT-00000308\",\"lsType\":\"numericValue\",\"lsKind\":\"Hit\",\"operator\":\"!=\",\"filterValue\":\"0\",\"termName\":\"Q2\"},{\"experimentCode\":\"EXPT-00000333\",\"lsType\":\"numericValue\",\"lsKind\":\"Hit\",\"operator\":\"!=\",\"filterValue\":\"0\",\"termName\":\"Q3\"},{\"experimentCode\":\"EXPT-00000299\",\"lsType\":\"numericValue\",\"lsKind\":\"Hit\",\"operator\":\"!=\",\"filterValue\":\"0\",\"termName\":\"Q4\"},{\"experimentCode\":\"EXPT-00000302\",\"lsType\":\"numericValue\",\"lsKind\":\"Hit\",\"operator\":\"!=\",\"filterValue\":\"0\",\"termName\":\"Q5\"}],\"booleanFilter\":\"or\",\"advancedFilter\":\"\"}";
		String onlyPublicData = "true";
		Boolean publicData = false;
		if (onlyPublicData != null && onlyPublicData.equalsIgnoreCase("true")) {
			publicData = true;
		}

		try {
			logger.info("here is the json: " + json);
			ExperimentSearchRequestDTO searchRequest = ExperimentSearchRequestDTO
					.fromJsonToExperimentSearchRequestDTO(json);
			logger.debug("converted json: " + searchRequest.toJson());
			List<AnalysisGroupValueDTO> agValues = null;
			agValues = experimentService.getFilteredAGData(searchRequest, publicData);
			logger.debug("number of agvalues found: " + agValues.size());

			// for (AnalysisGroupValueDTO agValue : agValues){
			// logger.debug(agValue.toJson());
			// }

		} catch (Exception e) {
			logger.error(e.toString());
		}
	}

	// @Test
	public void getFilteredGeneDataTest() throws Exception {
		String json = "{\"experimentCodeList\":[\"EXPT-00000039\"],\"batchCodeList\":[\"GENE-202547\"],\"booleanFilter\":\"and\",\"advancedFilter\":\"\"}";
		String responseJson = this.mockMvc.perform(
				post("/api/v1/experiments/agdata/batchcodelist/experimentcodelist?format=csv&onlyPublicData=true")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn().getResponse().getContentAsString();
		logger.info(responseJson);
	}
}
