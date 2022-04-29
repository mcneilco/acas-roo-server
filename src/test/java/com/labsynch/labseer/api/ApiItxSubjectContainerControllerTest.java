package com.labsynch.labseer.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.Collection;

import com.labsynch.labseer.domain.ItxSubjectContainer;

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
public class ApiItxSubjectContainerControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(ApiItxSubjectContainerControllerTest.class);

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	@Transactional
	public void createFromJson() throws Exception {
		String json = "[{\"subject\":{\"id\":6},\"container\":{\"id\":96},\"lsType\":\"default\",\"lsKind\":\"default\",\"recordedBy\":\"jmcneil\",\"recordedDate\":1455732120597,\"lsTransaction\":152}]";
		MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/itxsubjectcontainers/jsonArray")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.accept(MediaType.APPLICATION_JSON))
				// .andExpect(status().isCreated())
				// .andExpect(content().contentType("application/json"))
				.andReturn().getResponse();
		String responseJson = response.getContentAsString();
		logger.info(responseJson);
		Collection<ItxSubjectContainer> postedItx = ItxSubjectContainer
				.fromJsonArrayToItxSubjectContainers(responseJson);
		logger.info(ItxSubjectContainer.toJsonArray(postedItx));
	}

}