

package com.labsynch.labseer.service;

import com.labsynch.labseer.domain.LsThingValue;

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
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class LsThingValueServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(LsThingValueServiceTest.class);

	@Autowired
	private LsThingValueService lsThingValueService;

	
	@Test
	@Transactional
	public void updateLsThingValueTest() {
//		String idOrCodeName = "18311";
		String idOrCodeName = "GENE-000002";
		String stateType = "metadata";
		String stateKind = "gene metadata";
		String valueType = "stringValue";
		String valueKind = "status";
		String value = "Deleted";
		LsThingValue lsThingValue = lsThingValueService.updateLsThingValue(idOrCodeName, stateType, stateKind, valueType, valueKind, value);
		Assert.assertNotNull(lsThingValue);
		logger.info(lsThingValue.toJson());
	}

}
