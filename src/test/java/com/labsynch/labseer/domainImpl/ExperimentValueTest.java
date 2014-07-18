package com.labsynch.labseer.domainImpl;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.ExperimentValue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class ExperimentValueTest {
	
	private static final Logger logger = LoggerFactory.getLogger(ExperimentValueTest.class);

	@Test
	@Transactional
	public void QueryExperimentValueByKinds(){
		
		Long experimentId = 9L;
		String stateType = "metadata";
		String stateKind = "experiment metadata";
		String valueType = "stringValue";
		String valueKind = "analysis status";
		List<ExperimentValue> results = ExperimentValue.findExperimentValuesByExptIDAndStateTypeKindAndValueTypeKind(experimentId, stateType, stateKind, valueType, valueKind).getResultList();
		logger.info(ExperimentValue.toJsonArray(results));
		
	}
}
