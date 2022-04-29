package com.labsynch.labseer.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.Set;

import com.labsynch.labseer.domain.AnalysisGroupValue;
import com.labsynch.labseer.domain.ExperimentValue;
import com.labsynch.labseer.domain.LsThingValue;
import com.labsynch.labseer.domain.ProtocolValue;
import com.labsynch.labseer.domain.SubjectValue;
import com.labsynch.labseer.domain.TreatmentGroupValue;
import com.labsynch.labseer.dto.IdSetDTO;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import junit.framework.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@Transactional
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/applicationContext.xml",
		"classpath:/META-INF/spring/applicationContext-security.xml",
		"file:src/main/webapp/WEB-INF/spring/webmvc-config-test.xml" })
public class ApiValueControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(ApiValueControllerTest.class);

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	// @Test
	public void getProtocolValue() throws Exception {
		String entity = "protocol";
		String idOrCodeName = "PROT-00000005";
		String stateType = "metadata";
		String stateKind = "protocol metadata";
		String valueType = "stringValue";
		String valueKind = "notebook";
		MockHttpServletResponse response = this.mockMvc
				.perform(get("/api/v1/values/" + entity + "/" + idOrCodeName + "/bystate/" + stateType + "/" + stateKind
						+ "/byvalue/" + valueType + "/" + valueKind + "/")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=utf-8"))
				.andReturn().getResponse();
		String responseJson = response.getContentAsString();
		logger.info(responseJson);
		ProtocolValue value = ProtocolValue.fromJsonToProtocolValue(responseJson);
		Assert.assertNotNull(value.getId());
	}

	// @Test
	public void getExperimentValue() throws Exception {
		String entity = "experiment";
		String idOrCodeName = "EXPT-00000532";
		String stateType = "metadata";
		String stateKind = "experiment metadata";
		String valueType = "codeValue";
		String valueKind = "scientist";
		MockHttpServletResponse response = this.mockMvc
				.perform(get("/api/v1/values/" + entity + "/" + idOrCodeName + "/bystate/" + stateType + "/" + stateKind
						+ "/byvalue/" + valueType + "/" + valueKind + "/")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=utf-8"))
				.andReturn().getResponse();
		String responseJson = response.getContentAsString();
		logger.info(responseJson);
		ExperimentValue value = ExperimentValue.fromJsonToExperimentValue(responseJson);
		Assert.assertNotNull(value.getId());
	}

	// @Test
	public void getAnalysisGroupValue() throws Exception {
		String entity = "analysisGroup";
		String idOrCodeName = "AG-00000099";
		String stateType = "data";
		String stateKind = "Generic";
		String valueType = "codeValue";
		String valueKind = "batch code";
		MockHttpServletResponse response = this.mockMvc
				.perform(get("/api/v1/values/" + entity + "/" + idOrCodeName + "/bystate/" + stateType + "/" + stateKind
						+ "/byvalue/" + valueType + "/" + valueKind + "/")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=utf-8"))
				.andReturn().getResponse();
		String responseJson = response.getContentAsString();
		logger.info(responseJson);
		AnalysisGroupValue value = AnalysisGroupValue.fromJsonToAnalysisGroupValue(responseJson);
		Assert.assertNotNull(value.getId());
	}

	// @Test
	public void getTreatmentGroupValue() throws Exception {
		String entity = "treatmentGroup";
		String idOrCodeName = "TG-00033284";
		String stateType = "data";
		String stateKind = "results";
		String valueType = "numericValue";
		String valueKind = "Dose";
		MockHttpServletResponse response = this.mockMvc
				.perform(get("/api/v1/values/" + entity + "/" + idOrCodeName + "/bystate/" + stateType + "/" + stateKind
						+ "/byvalue/" + valueType + "/" + valueKind + "/")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=utf-8"))
				.andReturn().getResponse();
		String responseJson = response.getContentAsString();
		logger.info(responseJson);
		TreatmentGroupValue value = TreatmentGroupValue.fromJsonToTreatmentGroupValue(responseJson);
		Assert.assertNotNull(value.getId());
	}

	// @Test
	public void getSubjectValue() throws Exception {
		String entity = "subject";
		String idOrCodeName = "SUBJ-00551063";
		String stateType = "data";
		String stateKind = "test compound treatment";
		String valueType = "numericValue";
		String valueKind = "concentration";
		MockHttpServletResponse response = this.mockMvc
				.perform(get("/api/v1/values/" + entity + "/" + idOrCodeName + "/bystate/" + stateType + "/" + stateKind
						+ "/byvalue/" + valueType + "/" + valueKind + "/")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=utf-8"))
				.andReturn().getResponse();
		String responseJson = response.getContentAsString();
		logger.info(responseJson);
		SubjectValue value = SubjectValue.fromJsonToSubjectValue(responseJson);
		Assert.assertNotNull(value.getId());
	}

	// @Test
	public void getLsThingValue() throws Exception {
		String entity = "lsThing";
		String idOrCodeName = "PEG000010";
		String stateType = "metadata";
		String stateKind = "peg parent";
		String valueType = "numericValue";
		String valueKind = "molecular weight";
		MockHttpServletResponse response = this.mockMvc
				.perform(get("/api/v1/values/" + entity + "/" + idOrCodeName + "/bystate/" + stateType + "/" + stateKind
						+ "/byvalue/" + valueType + "/" + valueKind + "/")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=utf-8"))
				.andReturn().getResponse();
		String responseJson = response.getContentAsString();
		logger.info(responseJson);
		LsThingValue value = LsThingValue.fromJsonToLsThingValue(responseJson);
		Assert.assertNotNull(value.getId());
	}

	@Test
	public void updateProtocolValue() throws Exception {
		String entity = "protocol";
		String idOrCodeName = "PROT-00000005";
		String stateType = "metadata";
		String stateKind = "protocol metadata";
		String valueType = "stringValue";
		String valueKind = "notebook";
		MockHttpServletResponse response = this.mockMvc
				.perform(put("/api/v1/values/" + entity + "/" + idOrCodeName + "/bystate/" + stateType + "/" + stateKind
						+ "/byvalue/" + valueType + "/" + valueKind + "/")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.content("test"))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=utf-8"))
				.andReturn().getResponse();
		String responseJson = response.getContentAsString();
		logger.info(responseJson);
		ProtocolValue value = ProtocolValue.fromJsonToProtocolValue(responseJson);
		Assert.assertTrue(value.getStringValue().equals("test"));
		responseJson = this.mockMvc
				.perform(get("/api/v1/values/" + entity + "/" + idOrCodeName + "/bystate/" + stateType + "/" + stateKind
						+ "/byvalue/" + valueType + "/" + valueKind + "/")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=utf-8"))
				.andReturn().getResponse().getContentAsString();
		value = ProtocolValue.fromJsonToProtocolValue(responseJson);
		Assert.assertTrue(value.getStringValue().equals("test"));
	}

	@Test
	@Transactional
	public void getAGValue() throws Exception {
		Set<Long> idList = new HashSet<Long>();
		idList.add(3202L);
		idList.add(3203L);
		idList.add(3209L);
		IdSetDTO idSet = new IdSetDTO();
		idSet.setIdSet(idList);
		logger.info("here is the ID list: " + idSet.toJson());
		MockHttpServletResponse response = this.mockMvc
				.perform(post("/api/v1/analysisgroupvalues/getValues/byIdList?onlyPublicData=false&format=tsv")
						.contentType(MediaType.APPLICATION_JSON)
						.content(idSet.toJson())
						.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		String responseJson = response.getContentAsString();
		logger.info(responseJson);
		// LsThingValue value = LsThingValue.fromJsonToLsThingValue(responseJson);
		// Assert.assertNotNull(value.getId());
	}

}