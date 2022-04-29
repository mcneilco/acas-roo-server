package com.labsynch.labseer.domainImpl;

import java.util.List;

import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.Protocol;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class ExperimentTest {
	
	private static final Logger logger = LoggerFactory.getLogger(ExperimentTest.class);
	
	@Test
	@Transactional
	public void QueryExperimentByProtocolTypeKindAndExperimentTypeKind() {			
		Protocol protocol = Protocol.findProtocol(13l);
		List<Experiment> results = Experiment.findExperimentsByProtocolTypeAndKindAndExperimentTypeAndKind(protocol.getLsType(), protocol.getLsKind(), "default", "default").getResultList();
		logger.info(Experiment.toJsonArray(results));
		assert(results.size() == 1);
	}

}
