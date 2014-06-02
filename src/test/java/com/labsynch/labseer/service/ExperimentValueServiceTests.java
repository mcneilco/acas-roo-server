

package com.labsynch.labseer.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.labsynch.labseer.domain.ExperimentValue;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
public class ExperimentValueServiceTests {
	
	private static final Logger logger = LoggerFactory.getLogger(ExperimentValueServiceTests.class);
	
	@Autowired
	private ExperimentService experimentService;
	
	@Test
	//@Transactional
	public void ExperimentValueTest_1(){
		
		List<ExperimentValue> values = ExperimentValue.findExperimentValueEntries(0, 3);
		for (ExperimentValue ev : values){
			logger.info(ev.toJson());
		}

		ExperimentValue value = ExperimentValue.findExperimentValue(5010L);
		value.setIgnored(false);
		value.setComments("testing update of value 103");
		ExperimentValue updateValue = ExperimentValue.update(value);
		logger.info("#############################################");

		logger.info(updateValue.toJson());
		
	}

}
