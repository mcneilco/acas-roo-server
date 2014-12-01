package com.labsynch.labseer.dto;

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
public class TgDataDTOTest {
	
	private static final Logger logger = LoggerFactory.getLogger(TgDataDTO.class);
	
	@Test
	@Transactional
	public void getTgDataTest() {
		String json = "{curveId: \"a_AG-00347957\"}";
		TgDataDTO tgDataDTO = TgDataDTO.fromJsonToTgDataDTO(json);
		List<TgDataDTO> resultList = TgDataDTO.getTgData(tgDataDTO);
		logger.debug(resultList.toString());
		for (TgDataDTO result : resultList) {
			Assert.assertNotNull(result.getCurveId());
			Assert.assertNotNull(result.getTgvId());
			Assert.assertNotNull(result.getLsType());
			Assert.assertNotNull(result.getLsKind());
			Assert.assertNotNull(result.getNumericValue());
			Assert.assertNotNull(result.getUnitKind());
			Assert.assertNotNull(result.getUncertaintyType());
			Assert.assertNotNull(result.getUncertainty());
			Assert.assertNotNull(result.getPublicData());
		}
		
	}

}
