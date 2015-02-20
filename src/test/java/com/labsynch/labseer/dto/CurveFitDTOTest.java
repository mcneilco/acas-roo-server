package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.ProtocolValue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class CurveFitDTOTest {
	
	private static final Logger logger = LoggerFactory.getLogger(CurveFitDTOTest.class);
	
	@Test
	@Transactional
	public void getFitDataTest() {
		String curveId = "AG-00348384_3716";
		CurveFitDTO curveFitDTO = new CurveFitDTO(curveId);
		curveFitDTO = CurveFitDTO.getFitData(curveFitDTO);
		logger.debug(curveFitDTO.toJson());
		Assert.assertNotNull(curveFitDTO.getRenderingHint());
	}
	
	@Test
	@Transactional
	public void getFitDataThenSaveTest() {
		String curveId = "AG-00348384_3716";
		String analysisGroupCode = "AG-00348384";
		String recordedBy = "bfielder";
		Long lsTransaction = 99999L;
		CurveFitDTO curveFitDTO = new CurveFitDTO(curveId);
		curveFitDTO = CurveFitDTO.getFitData(curveFitDTO);
		curveFitDTO.setAnalysisGroupCode(analysisGroupCode);
		curveFitDTO.setRecordedBy(recordedBy);
		curveFitDTO.setLsTransaction(lsTransaction);
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
		String curveId = "AG-00348384_3716";
		String curveId2 = "AG-00348385_3716";
		CurveFitDTO curveFitDTO = new CurveFitDTO(curveId);
		CurveFitDTO curveFitDTO2 = new CurveFitDTO(curveId2);
		Collection<CurveFitDTO> curveFitDTOs = new ArrayList<CurveFitDTO>();
		curveFitDTOs.add(curveFitDTO);
		curveFitDTOs.add(curveFitDTO2);
		curveFitDTOs = CurveFitDTO.getFitData(curveFitDTOs);
		logger.debug(CurveFitDTO.toJsonArray(curveFitDTOs));
	}
	
	@Test
	@Transactional
	public void getAllCurveIdsByExperimentTest() {
		String experimentCodeName = "EXPT-00000909";
		long startTime = System.currentTimeMillis();
		Collection<String> results = CurveFitDTO.findAllCurveIdsByExperiment(experimentCodeName);
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		logger.debug("total elapsed time = " + totalTime + " miliseconds.");
		logger.debug("total number of curves: " + results.size());
		logger.debug(results.toString());
		Assert.assertTrue(!results.isEmpty());
	}
	
	@Test
	@Transactional
	public void getAllCurveFitDataByExperimentTest() {
		String experimentCodeName = "EXPT-00000909"; //29 curves
//		String experimentCodeName = "EXPT-00000078"; //1000 curves
		long startTime = System.currentTimeMillis();
		Collection<CurveFitDTO> results = CurveFitDTO.getFitDataByExperiment(experimentCodeName);
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		logger.debug("total elapsed time = " + totalTime + " miliseconds.");
		logger.debug("total number of curves: " + results.size());
		logger.debug(CurveFitDTO.toJsonArray(results));
		Assert.assertTrue(!results.isEmpty());
	}
	
	@Test
	@Transactional
	public void getDisplayMinMaxByCurveIdTest() {
		String curveId = "AG-00348388_3716";
		Collection<ProtocolValue> protocolValues = CurveFitDTO.findDisplayMinMaxByCurveId(curveId);
		logger.info(ProtocolValue.toJsonArray(protocolValues));
		Assert.assertTrue(!protocolValues.isEmpty());

	}
}
