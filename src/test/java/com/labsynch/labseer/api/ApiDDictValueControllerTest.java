package com.labsynch.labseer.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.dto.CodeTableDTO;

import org.junit.Assert;
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
public class ApiDDictValueControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(ApiDDictValueControllerTest.class);

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void getOrCreateSingleCodeTableExisting() throws Exception {
		CodeTableDTO codeTable = new CodeTableDTO();
		codeTable.setCodeType("dry run");
		codeTable.setCodeKind("status");
		codeTable.setCode("running");
		String json = codeTable.toJson();
		MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/ddictvalues/codetable")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(status().isCreated())
				.andExpect(content().contentType("application/json"))
				.andReturn().getResponse();
		String responseJson = response.getContentAsString();
		logger.info(responseJson);
		CodeTableDTO savedCodeTable = CodeTableDTO.fromJsonToCodeTableDTO(responseJson);
		logger.info(savedCodeTable.toJson());
		Assert.assertEquals(codeTable.getCodeType(), savedCodeTable.getCodeType());
		Assert.assertEquals(codeTable.getCodeKind(), savedCodeTable.getCodeKind());
		Assert.assertEquals(codeTable.getCode(), savedCodeTable.getCode());
	}

	@Test
	public void getOrCreateSingleCodeTableNew() throws Exception {
		CodeTableDTO codeTable = new CodeTableDTO();
		codeTable.setCodeType("test type");
		codeTable.setCodeKind("test kind");
		codeTable.setCode("test code");
		String json = codeTable.toJson();
		MockHttpServletResponse response = this.mockMvc
				.perform(post("/api/v1/ddictvalues/codetable?createTypeKind=true")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isCreated())
				.andExpect(content().contentType("application/json"))
				.andReturn().getResponse();
		String responseJson = response.getContentAsString();
		logger.info(responseJson);
		CodeTableDTO savedCodeTable = CodeTableDTO.fromJsonToCodeTableDTO(responseJson);
		logger.info(savedCodeTable.toJson());
		Assert.assertEquals(codeTable.getCodeType(), savedCodeTable.getCodeType());
		Assert.assertEquals(codeTable.getCodeKind(), savedCodeTable.getCodeKind());
		Assert.assertEquals(codeTable.getCode(), savedCodeTable.getCode());
	}

	@Test
	public void getOrCreateCodeTableArray() throws Exception {
		CodeTableDTO codeTable = new CodeTableDTO();
		codeTable.setCodeType("dry run");
		codeTable.setCodeKind("status");
		codeTable.setCode("running");
		CodeTableDTO codeTable2 = new CodeTableDTO();
		codeTable2.setCodeType("test type");
		codeTable2.setCodeKind("test kind");
		codeTable2.setCode("test code");
		List<CodeTableDTO> codeTableArray = new ArrayList<CodeTableDTO>();
		codeTableArray.add(codeTable);
		codeTableArray.add(codeTable2);
		String json = CodeTableDTO.toJsonArray(codeTableArray);
		MockHttpServletResponse response = this.mockMvc
				.perform(post("/api/v1/ddictvalues/codetable/jsonArray?createTypeKind=true")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isCreated())
				.andExpect(content().contentType("application/json"))
				.andReturn().getResponse();
		logger.info(response.toString());
		String responseJson = response.getContentAsString();
		logger.info(responseJson);
		Collection<CodeTableDTO> savedCodeTables = CodeTableDTO.fromJsonArrayToCoes(responseJson);
		for (CodeTableDTO savedCodeTable : savedCodeTables) {
			logger.info(savedCodeTable.toJson());
			Assert.assertTrue(codeTable.getCodeType().equals(savedCodeTable.getCodeType())
					|| codeTable2.getCodeType().equals(savedCodeTable.getCodeType()));
			Assert.assertTrue(codeTable.getCodeKind().equals(savedCodeTable.getCodeKind())
					|| codeTable2.getCodeKind().equals(savedCodeTable.getCodeKind()));
			Assert.assertTrue(codeTable.getCode().equals(savedCodeTable.getCode())
					|| codeTable2.getCode().equals(savedCodeTable.getCode()));
		}
	}
}
