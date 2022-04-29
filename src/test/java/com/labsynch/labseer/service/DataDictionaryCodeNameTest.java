/**
 * 
 */
package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.domain.DDictValue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author fairway
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class DataDictionaryCodeNameTest {
	
	private static final Logger logger = LoggerFactory.getLogger(DataDictionaryCodeNameTest.class);

	
	@Autowired
	private DataDictionaryService dataDictionaryService;

	/**
	 * Test method for {@link com.labsynch.labseer.domain.DDictValue#validate()}.
	 */
	//@Test
	@Transactional
	public void testValidate() {
		DDictValue theValue = new DDictValue();
		theValue.setLsType("random");
		theValue.setLsKind("random");
		theValue.setLabelText("random");
		theValue.setIgnored(false);
		dataDictionaryService.saveDataDictionaryValue(theValue, true);
		theValue.persist();
	}

//	@Test
	public void getValues() {
		List<DDictValue> dDictValues = DDictValue.findAllDDictValues();
		logger.info("---------------------" + DDictValue.toJsonArray(dDictValues));
		
		String output = dataDictionaryService.getCsvList(dDictValues);
		logger.info("---------------------" + output);

	}

	@Test
	public void saveValues() {
		String json = "[{\"shortName\":\"biochemical\",\"comments\":\"\",\"description\":\"\",\"displayOrder\":1,\"ignored\":false,\"labelText\":\"Biochemical\",\"lsKind\":\"type\",\"lsType\":\"assay\"},{\"shortName\":\"cellular\",\"comments\":\"\",\"description\":\"\",\"displayOrder\":2,\"ignored\":false,\"labelText\":\"Cellular\",\"lsKind\":\"type\",\"lsType\":\"assay\"}]";

		Collection<DDictValue> dDictValues = DDictValue.fromJsonArrayToDDictValues(json);
		Boolean createTypeAndKind = true;
		 List<DDictValue> output = dataDictionaryService.saveDataDictionaryValues(dDictValues, createTypeAndKind);
		
		logger.info(DDictValue.toJsonArray(output));

	}

	
}
