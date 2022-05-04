package com.labsynch.labseer.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.dto.SubjectCodeNameDTO;

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
public class ApiSubjectControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(ApiSubjectControllerTest.class);

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Transactional
	@Test
	public void createFromJsonArray() throws Exception {
		String json = "[{\"treatmentGroups\":[{\"id\":1357818}],\"lsType\":\"default\",\"lsKind\":\"default\",\"codeName\":\"SUBJ-00825725\",\"recordedBy\":\"smeyer\",\"lsTransaction\":7311,\"ignored\":false,\"recordedDate\":1424106740000}]";
		MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/subjects/jsonArray")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(content().contentType("application/json"))
				.andReturn().getResponse();
		String responseJson = response.getContentAsString();
		logger.info(responseJson);
		LsThing postedLsThing = LsThing.fromJsonToLsThing(responseJson);
		logger.info(postedLsThing.toJson());
	}

	@Transactional
	@Test
	public void getByIdOrCodeName() throws Exception {
		Subject subject = Subject.findSubjectEntries(0, 1).get(0);
		MockHttpServletResponse response = this.mockMvc.perform(get("/api/v1/subjects/" + subject.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=utf-8"))
				.andReturn().getResponse();
		String responseJson = response.getContentAsString();
		logger.info(responseJson);
		MockHttpServletResponse response2 = this.mockMvc.perform(get("/api/v1/subjects/" + subject.getCodeName())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=utf-8"))
				.andReturn().getResponse();
		String responseJson2 = response2.getContentAsString();
		logger.info(responseJson2);
		Assert.assertEquals(responseJson, responseJson2);
	}

	@Transactional
	@Test
	public void getSubjectsByCodeNames() throws Exception {
		List<Subject> subjects = Subject.findSubjectEntries(0, 100);
		List<String> codeNames = new ArrayList<String>();
		for (Subject subject : subjects) {
			codeNames.add("\"" + subject.getCodeName() + "\"");
		}
		codeNames.add("\"BAD-CODENAME\"");
		String json = codeNames.toString();
		MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/subjects/getSubjectsByCodeNames")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=utf-8"))
				.andReturn().getResponse();
		String responseJson = response.getContentAsString();
		logger.info(responseJson);
		Collection<SubjectCodeNameDTO> results = SubjectCodeNameDTO.fromJsonArrayToSubjectCoes(responseJson);
		Assert.assertEquals(codeNames.size(), results.size());
		for (SubjectCodeNameDTO result : results) {
			Assert.assertNotNull(result.getRequestCodeName());
			if (result.getRequestCodeName().equals("BAD-CODENAME"))
				Assert.assertNull(result.getSubject());
			else
				Assert.assertNotNull(result.getSubject());
		}
	}
}