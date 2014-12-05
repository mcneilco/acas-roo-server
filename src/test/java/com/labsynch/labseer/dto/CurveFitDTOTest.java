package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.Collection;

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
	
	@Test
	@Transactional
	public void getFitDataThenSaveTest() {
		String curveId = "AG-00344271_1640";
		String analysisGroupCode = "AG-00344271";
		String recordedBy = "bfielder";
		CurveFitDTO curveFitDTO = new CurveFitDTO(curveId);
		curveFitDTO = CurveFitDTO.getFitData(curveFitDTO);
		curveFitDTO.setAnalysisGroupCode(analysisGroupCode);
		curveFitDTO.setRecordedBy(recordedBy);
		logger.debug("initial: " + curveFitDTO.toJson());
		Assert.assertNotNull(curveFitDTO.getRenderingHint());
		Collection<CurveFitDTO> curveFitDTOCollection = new ArrayList<CurveFitDTO>();
		curveFitDTOCollection.add(curveFitDTO);
		CurveFitDTO.updateFitData(curveFitDTOCollection);
//		CurveFitDTO curveFitDTO2 = new CurveFitDTO(curveId);
//		curveFitDTO2 = CurveFitDTO.getFitData(curveFitDTO2);
//		Assert.assertEquals(curveFitDTO.toJson(), curveFitDTO2.toJson());
		
	}
	
	@Test
	@Transactional
	public void getFitDataArrayTest() {
		String curveId = "a_AG-00347957";
		String curveId2 = "AG-00344271_1640";
		CurveFitDTO curveFitDTO = new CurveFitDTO(curveId);
		CurveFitDTO curveFitDTO2 = new CurveFitDTO(curveId2);
		Collection<CurveFitDTO> curveFitDTOs = new ArrayList<CurveFitDTO>();
		curveFitDTOs.add(curveFitDTO);
		curveFitDTOs.add(curveFitDTO2);
		curveFitDTOs = CurveFitDTO.getFitData(curveFitDTOs);
		logger.debug(CurveFitDTO.toJsonArray(curveFitDTOs));
	}
	
}
