package com.labsynch.labseer.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collection;

import junit.framework.Assert;

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

import com.labsynch.labseer.domain.Author;
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ExperimentLabel;
import com.labsynch.labseer.dto.CodeTableDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/applicationContext.xml",
		"classpath:/META-INF/spring/applicationContext-security.xml",
		"file:src/main/webapp/WEB-INF/spring/webmvc-config-test.xml"})
@Transactional
public class ApiAuthorControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(ApiAuthorControllerTest.class);
	
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
    
    @Test
    public void getAllAuthorsAsCodetable() throws Exception {
    	String searchString = "bob";
    	String format = "codeTable";
    	String responseJson =  this.mockMvc.perform(get("/api/v1/authors/="+format)
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse().getContentAsString();
    	logger.info(responseJson.toString());

    }
    
    @Test
    public void getAllAuthorsAsJson() throws Exception {
    	String searchString = "bob";
    	String format = "json";
    	String responseJson =  this.mockMvc.perform(get("/api/v1/authors/="+format)
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse().getContentAsString();
    	Collection<Author> authors = Author.fromJsonArrayToAuthors(responseJson);
    	logger.info(Author.toJsonArray(authors));

    }
    
    @Test
    public void getAuthorsByRoleTypeAndKindAndName() throws Exception {
    	String roleType = "System";
    	String roleKind = "ACAS";
    	String roleName = "ROLE_ACAS-USERS";
    	String responseJson =  this.mockMvc.perform(get("/api/v1/authors/findByRoleTypeKindAndName?roleType="+roleType+"&roleKind="+roleKind+"&roleName="+roleName)
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse().getContentAsString();
    	logger.info(responseJson.toString());
    	Collection<Author> foundAuthors = Author.fromJsonArrayToAuthors(responseJson);
    	Assert.assertEquals(1, foundAuthors.size());
    	Author bob = foundAuthors.iterator().next();
    	Assert.assertEquals("bob", bob.getUserName());
    }
    
    @Test
    public void getAuthorsByRoleTypeAndKindAndNameAsCodeTables() throws Exception {
    	String roleType = "System";
    	String roleKind = "ACAS";
    	String roleName = "ROLE_ACAS-USERS";
    	String format = "codetable";
    	String url = "/api/v1/authors/findByRoleTypeKindAndName?"
    			+ "format="+format+"&roleType="+roleType+"&roleKind="+roleKind+"&roleName="+roleName;
    	logger.info(url);
    	String responseJson =  this.mockMvc.perform(get(url)
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse().getContentAsString();
    	logger.info(responseJson.toString());
    	Collection<CodeTableDTO> foundAuthors = CodeTableDTO.fromJsonArrayToCoes(responseJson);
    	Assert.assertEquals(1, foundAuthors.size());
    	logger.info(CodeTableDTO.toJsonArray(foundAuthors));
    }
    

    


}