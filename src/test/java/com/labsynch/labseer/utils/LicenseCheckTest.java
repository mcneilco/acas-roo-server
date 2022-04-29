package com.labsynch.labseer.utils;

import java.io.IOException;
import java.net.URISyntaxException;

import com.labsynch.labseer.dto.LicenseDTO;
import com.labsynch.labseer.service.LicenseService;

import org.bouncycastle.openpgp.PGPException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml")
@Configurable
public class LicenseCheckTest {

	private static final Logger logger = LoggerFactory.getLogger(LicenseCheckTest.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Autowired
	private LicenseService licenseService;

	@Test
	public void licenseCheckServiceTest() throws IOException, PGPException, URISyntaxException {
		LicenseDTO results = licenseService.getLicenseInfo();
		logger.info(results.toJson());

	}

}
