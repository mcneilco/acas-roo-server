package com.labsynch.labseer.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collection;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.labsynch.labseer.dto.AuthorRoleDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/applicationContext.xml",
		"classpath:/META-INF/spring/applicationContext-security.xml",
		"file:src/main/webapp/WEB-INF/spring/webmvc-config-test.xml"})
@Transactional
public class ApiAuthorRoleControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(ApiAuthorRoleControllerTest.class);
	
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
    
    @Test
    @Transactional
    @Rollback(value=true)
    public void saveAuthorRolesViaDTO() throws Exception {
    	String roleType = "System";
    	String roleKind = "ACAS";
    	String roleName = "ROLE_ACAS-USERS";
    	String userName = "bob";
    	AuthorRoleDTO dto = new AuthorRoleDTO();
    	dto.setRoleType(roleType);
    	dto.setRoleKind(roleKind);
    	dto.setRoleName(roleName);
    	dto.setUserName(userName);
    	Collection<AuthorRoleDTO> authorRoles = new HashSet<AuthorRoleDTO>();
    	authorRoles.add(dto);
    	String json = AuthorRoleDTO.toJsonArray(authorRoles);
    	ResultActions responseJson = this.mockMvc.perform(post("/api/v1/authorroles/saveRoles")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(json)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isCreated());
    }
    
    @Test
    @Transactional
    @Rollback(value=true)
    public void deleteAuthorRolesViaDTO() throws Exception {
    	String roleType = "System";
    	String roleKind = "ACAS";
    	String roleName = "ROLE_ACAS-USERS";
    	String userName = "bob";
    	AuthorRoleDTO dto = new AuthorRoleDTO();
    	dto.setRoleType(roleType);
    	dto.setRoleKind(roleKind);
    	dto.setRoleName(roleName);
    	dto.setUserName(userName);
    	Collection<AuthorRoleDTO> authorRoles = new HashSet<AuthorRoleDTO>();
    	authorRoles.add(dto);
    	String json = AuthorRoleDTO.toJsonArray(authorRoles);
    	ResultActions responseJson = this.mockMvc.perform(post("/api/v1/authorroles/deleteRoles")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(json)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk());
    }
    

    


}