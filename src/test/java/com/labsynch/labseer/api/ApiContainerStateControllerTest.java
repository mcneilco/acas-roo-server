package com.labsynch.labseer.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collection;
import java.util.HashSet;

import com.labsynch.labseer.domain.Container;
import com.labsynch.labseer.domain.ContainerState;

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
public class ApiContainerStateControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(ApiContainerStateControllerTest.class);

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	@Transactional
	public void findValidContainerStates() throws Exception {
		Collection<Container> allContainers = Container.findAllContainers();
		Collection<Container> someContainers = new HashSet<Container>();
		int count = 0;
		for (Container container : allContainers) {
			if (count < 10) {
				Container emptyContainer = new Container();
				emptyContainer.setId(container.getId());
				someContainers.add(emptyContainer);
				count++;
			}
		}
		Assert.assertFalse(someContainers.isEmpty());
		String json = Container.toJsonArray(someContainers);
		logger.info(json);
		Assert.assertFalse(json.equals("{}"));
		MockHttpServletResponse response = this.mockMvc
				.perform(post("/api/v1/containerstates/findValidContainerStates/jsonArray")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn().getResponse();
		String responseJson = response.getContentAsString();
		logger.info(responseJson);
		Collection<ContainerState> validStates = ContainerState.fromJsonArrayToContainerStates(responseJson);
		Assert.assertFalse(validStates.isEmpty());
		for (ContainerState validState : validStates) {
			Assert.assertFalse(validState.isIgnored());
		}
	}

	@Test
	@Transactional
	public void findValidContainerStatesAndIgnore() throws Exception {
		Collection<Container> allContainers = Container.findAllContainers();
		Collection<Container> someContainers = new HashSet<Container>();
		int count = 0;
		for (Container container : allContainers) {
			if (count < 10) {
				Container emptyContainer = new Container();
				emptyContainer.setId(container.getId());
				someContainers.add(emptyContainer);
				count++;
			}
		}
		Assert.assertFalse(someContainers.isEmpty());
		String json = Container.toJsonArray(someContainers);
		logger.info(json);
		Assert.assertFalse(json.equals("{}"));
		MockHttpServletResponse response = this.mockMvc
				.perform(post("/api/v1/containerstates/findValidContainerStates/jsonArray")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn().getResponse();
		String responseJson = response.getContentAsString();
		logger.info(responseJson);
		Collection<ContainerState> validStates = ContainerState.fromJsonArrayToContainerStates(responseJson);
		Assert.assertFalse(validStates.isEmpty());
		for (ContainerState validState : validStates) {
			Assert.assertFalse(validState.isIgnored());
		}

		Collection<ContainerState> statesToIgnore = new HashSet<ContainerState>();
		for (ContainerState validState : validStates) {
			ContainerState stateToIgnore = new ContainerState();
			stateToIgnore.setId(validState.getId());
			statesToIgnore.add(stateToIgnore);
		}
		Assert.assertFalse(statesToIgnore.isEmpty());
		String jsonToIgnore = ContainerState.toJsonArray(statesToIgnore);
		logger.info(jsonToIgnore);
		Assert.assertFalse(jsonToIgnore.equals("{}"));
		MockHttpServletResponse response2 = this.mockMvc.perform(put("/api/v1/containerstates/ignore/jsonArray")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn().getResponse();
		String responseJson2 = response2.getContentAsString();
		logger.info(responseJson2);
		Assert.assertEquals("[]", responseJson2);
	}

}