package com.labsynch.labseer.service;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.Author;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.dto.AuthGroupsAndProjectsDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/applicationContext.xml",
		"classpath:/META-INF/spring/applicationContext-security.xml" })
@Configurable
public class AuthorServiceTests {

	private static final Logger logger = LoggerFactory
			.getLogger(AuthorServiceTests.class);

	@Autowired
	private AuthorService authorService;
	
	
	@Test
	@Transactional
	public void getAuthorsByRoleTest() {

		String authorRoleName = "User";
		Collection<Author> authors = authorService.findAuthorsByAuthorRoleName(authorRoleName);
		logger.info("------- Results from getAuthorsByRoleTest ----------");
		logger.info(Author.toJsonArray(authors));

	}
	
	@Test
	@Transactional
	public void getAuthorProjectsTest() {

		String userName = "jblow";
		Collection<LsThing> results = authorService.getUserProjects(userName);
		logger.info("------- Results from getAuthorProjectsTest ----------");

		logger.info(LsThing.toJsonArrayStub(results));
	}

	
	@Test
	@Transactional
	public void getAuthorizeGroupsAndProjectTest() {
		AuthGroupsAndProjectsDTO results = authorService.getAuthGroupsAndProjects();
		logger.info("------- Results from getAuthorizeGroupsAndProjectTest ----------");
		logger.info(results.toJson());
	}
}
