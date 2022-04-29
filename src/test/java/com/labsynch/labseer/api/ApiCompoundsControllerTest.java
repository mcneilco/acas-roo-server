package com.labsynch.labseer.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collection;
import java.util.HashSet;

import com.labsynch.labseer.dto.CmpdRegBatchCodeDTO;

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
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/applicationContext.xml",
		"classpath:/META-INF/spring/applicationContext-security.xml",
		"file:src/main/webapp/WEB-INF/spring/webmvc-config-test.xml" })
public class ApiCompoundsControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(ApiCompoundsControllerTest.class);

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	@Transactional
	public void checkBatchCodeDependencies() throws Exception {
		Collection<String> batchCodes = new HashSet<String>();
		batchCodes.add("CMPD-0000001-01A");
		batchCodes.add("CMPD-0000002-01A");
		batchCodes.add("CMPD-0000003-01A");
		batchCodes.add("NOT-A-VALID-BATCH-CODE");

		CmpdRegBatchCodeDTO cmpdRegBatchCodeDTO = new CmpdRegBatchCodeDTO(batchCodes);
		String json = cmpdRegBatchCodeDTO.toJson();
		logger.info(json);
		Assert.assertFalse(json.equals("{}"));
		MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/compounds/checkBatchDependencies")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn().getResponse();
		String responseJson = response.getContentAsString();
		logger.info(responseJson);
		CmpdRegBatchCodeDTO result = CmpdRegBatchCodeDTO.fromJsonToCmpdRegBatchCodeDTO(responseJson);
		Assert.assertTrue(result.getLinkedDataExists());
		Assert.assertNotNull(result.getLinkedExperiments());
		Assert.assertFalse(result.getLinkedExperiments().isEmpty());
	}

}