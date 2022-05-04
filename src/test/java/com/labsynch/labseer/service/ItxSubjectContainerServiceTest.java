
package com.labsynch.labseer.service;

import com.labsynch.labseer.domain.ItxSubjectContainer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/applicationContext.xml",
		"classpath:/META-INF/spring/applicationContext-security.xml" })
@Configurable
public class ItxSubjectContainerServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(ItxSubjectContainerServiceTest.class);

	@Autowired
	private ItxSubjectContainerService itxSubjectContainerService;

	@Test
	@Transactional
	@Rollback(value = false)
	public void saveItxSubjectContainer() {
		String json = "{\"subject\":{\"id\":6},\"container\":{\"id\":96},\"lsType\":\"default\",\"lsKind\":\"default\",\"recordedBy\":\"jmcneil\",\"recordedDate\":1455732120597,\"lsTransaction\":152}";
		ItxSubjectContainer itxSubjectContainer = ItxSubjectContainer.fromJsonToItxSubjectContainer(json);
		itxSubjectContainerService.saveLsItxSubjectContainer(itxSubjectContainer);
	}

}
