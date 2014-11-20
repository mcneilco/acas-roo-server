package com.labsynch.labseer.dto;

import junit.framework.Assert;

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
public class CurveFitDTOTest {
	
	private static final Logger logger = LoggerFactory.getLogger(CurveFitDTOTest.class);
	
	@Test
	@Transactional
	public void getFitDataTest() {
		String curveId = "a_AG-00347957";
		CurveFitDTO curveFitDTO = new CurveFitDTO(curveId);
		curveFitDTO = CurveFitDTO.getFitData(curveFitDTO);
		logger.debug(curveFitDTO.toJson());
		Assert.assertNotNull(curveFitDTO.getRenderingHint());
	}
}
