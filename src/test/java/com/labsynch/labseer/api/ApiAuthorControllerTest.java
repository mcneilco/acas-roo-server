package com.labsynch.labseer.api;


import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.springframework.test.web.ModelAndViewAssert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.junit.runners.JUnit4;
import org.springframework.mock.staticmock.MockStaticEntityMethods;

import com.labsynch.labseer.domain.Author;
import com.labsynch.labseer.domain.AuthorLabel;
import com.labsynch.labseer.domain.AuthorState;
import com.labsynch.labseer.domain.AuthorValue;
import com.labsynch.labseer.domain.ProtocolValue;
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
    public void nestedSave() throws Exception{
    	Author author = new Author();
    	author.setRecordedBy("bob");
    	author.setRecordedDate(new Date());
    	author.setUserName("bob");
    	author.setEmailAddress("bob@mcneilco.com");
    	author.setFirstName("Bob");
    	author.setLastName("Roberts");
    	AuthorState authorState = new AuthorState();
    	authorState.setRecordedBy("bob");
    	authorState.setRecordedDate(new Date());
    	authorState.setLsType("metadata");
    	authorState.setLsKind("preferences");
    	AuthorValue authorValue = new AuthorValue();
    	authorValue.setLsType("clobValue");
    	authorValue.setLsKind("example module preferences");
    	authorValue.setClobValue("{\"testJson\":\"Test\"}");
    	AuthorLabel authorLabel = new AuthorLabel();
    	authorLabel.setLsType("name");
    	authorLabel.setLsKind("nickname");
    	authorLabel.setRecordedBy("bob");
    	authorLabel.setRecordedDate(new Date());
    	authorLabel.setLabelText("Bob");
    	
    	Set<AuthorState> states = new HashSet<AuthorState>();
    	states.add(authorState);
    	Set<AuthorValue> values = new HashSet<AuthorValue>();
    	values.add(authorValue);
    	Set<AuthorLabel> labels = new HashSet<AuthorLabel>();
    	labels.add(authorLabel);
    	author.setLsStates(states);
    	authorState.setLsValues(values);
    	author.setLsLabels(labels);
    	
    	String json = author.toJson();
    	logger.info(json);
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/authors")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(json))
    			.andExpect(status().isCreated())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Author savedAuthor = Author.fromJsonToAuthor(responseJson);
    }
    
    @Test
    @Rollback(value=true)
    public void nestedSave_fromJson() throws Exception{
    	String json = "{  \"firstName\": \"Bob\",  \"lastName\": \"Roberts\",  \"userName\": \"bob\",  \"emailAddress\": \"bob@mcneilco.com\",  \"version\": 0,  \"enabled\": true,  \"locked\": false,  \"password\": \"5en6G6MezRroT3XKqkdPOmY/BfQ=\",  \"recordedBy\": \"bob\",  \"recordedDate\": 1457542406000,  \"lsType\":\"author\",  \"lsKind\":\"author\"}";
    	logger.info(json);
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/authors")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(json))
    			.andExpect(status().isCreated())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Author savedAuthor = Author.fromJsonToAuthor(responseJson);
    }
    
    @Test
    @Rollback(value=true)
    public void getOrCreate() throws Exception{
    	Author author = new Author();
    	author.setRecordedBy("bob");
    	author.setRecordedDate(new Date());
    	author.setUserName("bob");
    	author.setEmailAddress("bob@mcneilco.com");
    	author.setFirstName("Bob");
    	author.setLastName("Roberts");
    	AuthorState authorState = new AuthorState();
    	authorState.setRecordedBy("bob");
    	authorState.setRecordedDate(new Date());
    	authorState.setLsType("metadata");
    	authorState.setLsKind("preferences");
    	AuthorValue authorValue = new AuthorValue();
    	authorValue.setLsType("clobValue");
    	authorValue.setLsKind("example module preferences");
    	authorValue.setClobValue("{\"testJson\":\"Test\"}");
    	AuthorLabel authorLabel = new AuthorLabel();
    	authorLabel.setLsType("name");
    	authorLabel.setLsKind("nickname");
    	authorLabel.setRecordedBy("bob");
    	authorLabel.setRecordedDate(new Date());
    	authorLabel.setLabelText("Bob");
    	
    	Set<AuthorState> states = new HashSet<AuthorState>();
    	states.add(authorState);
    	Set<AuthorValue> values = new HashSet<AuthorValue>();
    	values.add(authorValue);
    	Set<AuthorLabel> labels = new HashSet<AuthorLabel>();
    	labels.add(authorLabel);
    	author.setLsStates(states);
    	authorState.setLsValues(values);
    	author.setLsLabels(labels);
    	
    	String json = author.toJson();
    	logger.info(json);
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/authors")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(json))
    			.andExpect(status().isCreated())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Author savedAuthor = Author.fromJsonToAuthor(responseJson);
    	Assert.assertNotSame(author.getRecordedDate(), savedAuthor.getRecordedDate());
    }
    
    
}
