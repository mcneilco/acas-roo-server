package com.labsynch.labseer.utils;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml")
@Configurable
public class SimpleUtilTest {

	private static final Logger logger = LoggerFactory.getLogger(SimpleUtilTest.class);
	
	@Test
	public void splitStringTest(){
		String searchString = "\"Fiona Test Experiment 1\" completed";
		List<String> expectedResults = new ArrayList<String>();
		expectedResults.add("Fiona Test Experiment 1");
		expectedResults.add("completed");
		List<String> results = SimpleUtil.splitSearchString(searchString);
		logger.info(results.toString());
		Assert.assertEquals(expectedResults, results);
	}

}
