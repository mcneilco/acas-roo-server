/**
 * 
 */
package com.labsynch.labseer.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.DDictValue;

/**
 * @author fairway
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class DataDictionaryCodeNameTest {
	
	@Autowired
	private DataDictionaryService theDataDictionaryService;

	/**
	 * Test method for {@link com.labsynch.labseer.domain.DDictValue#validate()}.
	 */
	@Test
	@Transactional
	public void testValidate() {
		DDictValue theValue = new DDictValue();
		theValue.setLsType("random");
		theValue.setLsKind("random");
		theValue.setLabelText("random");
		theValue.setIgnored(false);
		theDataDictionaryService.saveDataDictionaryValue(theValue);
	}

}
