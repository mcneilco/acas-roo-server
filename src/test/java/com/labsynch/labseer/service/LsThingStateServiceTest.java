package com.labsynch.labseer.service;

import com.labsynch.labseer.domain.LsThingState;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import junit.framework.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/applicationContext.xml",
		"classpath:/META-INF/spring/applicationContext-security.xml" })
@Configurable
public class LsThingStateServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(LsThingStateServiceTest.class);

	@Autowired
	private LsThingStateService lsThingStateService;

	@Test
	@Transactional
	public void createLsThingStateByLsThingIdAndStateTypeKindTest() {
		Long lsThingId = 18311L;
		String lsType = "metadata";
		String lsKind = "gene metadata";
		LsThingState lsThingState = lsThingStateService.createLsThingStateByLsThingIdAndStateTypeKind(lsThingId, lsType,
				lsKind);
		Assert.assertNotNull(lsThingState);
		logger.info(lsThingState.toJson());
	}
}
