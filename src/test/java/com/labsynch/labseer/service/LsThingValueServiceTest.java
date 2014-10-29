

package com.labsynch.labseer.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.NoResultException;

import junit.framework.Assert;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.AnalysisGroupState;
import com.labsynch.labseer.domain.AnalysisGroupValue;
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.LsThingLabel;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.LsThingValue;
import com.labsynch.labseer.dto.AnalysisGroupValueDTO;
import com.labsynch.labseer.dto.PreferredNameDTO;
import com.labsynch.labseer.dto.PreferredNameResultsDTO;

import flexjson.JSONTokener;


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
