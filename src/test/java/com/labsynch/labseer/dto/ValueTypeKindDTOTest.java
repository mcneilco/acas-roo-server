package com.labsynch.labseer.dto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import junit.framework.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/applicationContext.xml",
		"classpath:/META-INF/spring/applicationContext-security.xml" })
@Configurable
public class ValueTypeKindDTOTest {

	private static final Logger logger = LoggerFactory.getLogger(ValueTypeKindDTOTest.class);

	@Transactional
	@Test
	public void findValueKind() {
		ValueTypeKindDTO dto = new ValueTypeKindDTO("codeValue", "scientist");
		Assert.assertNull(dto.getValueKind());
		dto.findValueKind();
		Assert.assertNotNull(dto.getValueKind());
	}
}
