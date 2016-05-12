package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.SubjectValue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class RawCurveDataDTOTest {
	
	private static final Logger logger = LoggerFactory.getLogger(RawCurveDataDTOTest.class);
	
	@Test
	@Transactional
	public void getRawCurveDataTest() {
		String curveId = "15_AG-00348398";
		List<String> curveIdList = new ArrayList<String>();
		curveIdList.add(curveId);
		List<RawCurveDataDTO> resultList = RawCurveDataDTO.getRawCurveData(curveIdList, null);
		logger.debug(resultList.toString());
		for (RawCurveDataDTO result : resultList) {
			logger.debug(result.toJson());
			Assert.assertNotNull(result.getCurveId());
			Assert.assertNotNull(result.getResponseSubjectValueId());
			Assert.assertNotNull(result.getResponse());
//			Assert.assertNotNull(result.getResponseUnits());
			Assert.assertNotNull(result.getDose());
			Assert.assertNotNull(result.getDoseUnits());
		}
		
	}
	
	@Test
	@Transactional
	public void getAllRawCurveDataDataByExperimentTest() {
		String experimentCodeName = "EXPT-00000909";
		long startTime = System.currentTimeMillis();
		Collection<RawCurveDataDTO> results = RawCurveDataDTO.getRawCurveDataByExperiment(experimentCodeName, null);
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		logger.debug("total elapsed time = " + totalTime + " miliseconds.");
		logger.debug("total number of data points: " + results.size());
		for (RawCurveDataDTO result : results) {
			logger.debug(result.toJson());
			Assert.assertNotNull(result.getCurveId());
			Assert.assertNotNull(result.getResponseSubjectValueId());
			Assert.assertNotNull(result.getResponse());
//			Assert.assertNotNull(result.getResponseUnits());
			Assert.assertNotNull(result.getDose());
			Assert.assertNotNull(result.getDoseUnits());
		}
		Assert.assertTrue(!results.isEmpty());
	}

}
