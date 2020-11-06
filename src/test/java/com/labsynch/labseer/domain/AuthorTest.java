package com.labsynch.labseer.domain;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthorTest {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthorTest.class);

	@Test
	public void CreateAuthor_Test001(){
		Author auth = new Author();
		auth.setFirstName("guytest");
		auth.setLastName("oshiro");
		auth.setUserName("goshirotest");
		auth.setPassword("apple");
		auth.setEmailAddress("guy@test.com");
		auth.persist();
		logger.info(auth.toJson());
	}
}
