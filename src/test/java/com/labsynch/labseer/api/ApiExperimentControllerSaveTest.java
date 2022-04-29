package com.labsynch.labseer.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/applicationContext.xml",
		"classpath:/META-INF/spring/applicationContext-security.xml",
		"file:src/main/webapp/WEB-INF/spring/webmvc-config-test.xml" })
@Transactional
public class ApiExperimentControllerSaveTest {

	private static final Logger logger = LoggerFactory.getLogger(ApiExperimentControllerSaveTest.class);

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void saveAnalysisGroupsFromTsvFileTest() throws Exception {
		String json = "{\"analysisGroupCsvFilePath\":\"/opt/acas-dataexplorer/acas/privateUploads/csvUpload40427fc79ead.tsv\"}";
		logger.info("********" + json);

		MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/experiments/analysisgroup/savefromtsv")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn().getResponse();
		logger.info(response.getContentAsString());

	}

	// MockHttpServletResponse response =
	// this.mockMvc.perform(post("/api/v1/authors")
	// .contentType(MediaType.APPLICATION_JSON)
	// .accept(MediaType.APPLICATION_JSON)
	// .content(json))
	// .andExpect(status().isCreated())
	// .andExpect(content().contentType("application/json"))
	// .andReturn().getResponse();
	// String responseJson = response.getContentAsString();
	// logger.info(responseJson);
	// Author savedAuthor = Author.fromJsonToAuthor(responseJson);

}