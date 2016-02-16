package com.labsynch.labseer.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.openpgp.PGPException;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.labsynch.labseer.domain.Author;
import com.labsynch.labseer.domain.AuthorRole;
import com.labsynch.labseer.domain.DDictValue;
import com.labsynch.labseer.domain.LsRole;
import com.labsynch.labseer.dto.AutoLabelDTO;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.LicenseDTO;
import com.labsynch.labseer.utils.PropertiesUtilService;
import com.verhas.licensor.License;

@Service
@Transactional
public class LicenseServiceImpl implements LicenseService {


	@Autowired
	private PropertiesUtilService propertiesUtilService;

	
	private static final Logger logger = LoggerFactory.getLogger(LicenseServiceImpl.class);
	
	public void checkValidLicense() throws IOException, PGPException{
		String licenseFileName = propertiesUtilService.getAcaslicenseFile();
	}
	
	@Override
	public LicenseDTO getLicenseInfo() throws IOException, PGPException, URISyntaxException{
		
		String licenseFileName = propertiesUtilService.getAcaslicenseFile();
		logger.info("licenseFileName is " + licenseFileName);
	    License lic = new License();
	    
	    byte [] keyDigest = getKeyDigest();

	    URI keyFilePath = LicenseServiceImpl.class.getClassLoader().getResource("acas_public_key.gpg").toURI();
	    logger.info("------------------------here is the filepath: " + keyFilePath.getPath());
	    
	    File publicKeyFile = new File(keyFilePath.getPath());
	    
	    lic.loadKeyRing(publicKeyFile, null);
	    lic.setLicenseEncodedFromFile(licenseFileName);
	    String edition = lic.getFeature("edition");
	    logger.info("The acas edition is: " + edition);
	    logger.info("The acas features are: " + lic.getFeature("features"));

	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    String validDateString = lic.getFeature("valid-until");
	    logger.info("validDateString until " + validDateString);
	    Date validDate = null;
	    try {
			validDate = df.parse(validDateString);
		} catch (ParseException e) {
			logger.error("ERROR: Unable to parse the date: " + validDateString);
			e.printStackTrace();
		}
	    logger.info("validate date: " + validDate);
	    
	    Date todayDate = new Date();
	    
	    boolean isValidDate = todayDate.before(validDate);
	    logger.info("is the date valid: " + isValidDate);
	    Days daysLeft = Days.daysBetween(LocalDate.fromDateFields(todayDate), LocalDate.fromDateFields(validDate));
	    
	    logger.info("Days left to expiration: " + daysLeft);	    	    
	    logger.info("Days left to expiration: " + daysLeft.getDays());
	    
	    LicenseDTO licenseInfo = new LicenseDTO();
	    licenseInfo.setNumberOfDaysLeft(daysLeft.getDays());
	    licenseInfo.setValidDateString(validDateString);
	    licenseInfo.setEdition(edition);
	    licenseInfo.setValid(isValidDate);
	    licenseInfo.setFeatures(lic.getFeature("features"));
	    licenseInfo.setLicensee(lic.getFeature("licensee"));
	    licenseInfo.setNumberOfUsers(Integer.valueOf(lic.getFeature("numberOfUsers")));

	    	    	    
	    return licenseInfo;
		
	}

	private byte[] getKeyDigest() {
	    byte [] digest = new byte[] {
	    		(byte)0xE5, 
	    		(byte)0x4B, (byte)0x39, (byte)0x1B, (byte)0x59, (byte)0x82, (byte)0xF7, (byte)0x9C, (byte)0x9C, 
	    		(byte)0xCC, (byte)0x9C, (byte)0x8D, (byte)0x90, (byte)0x1F, (byte)0x0B, (byte)0xFE, (byte)0x01, 
	    		(byte)0xD9, (byte)0x66, (byte)0x05, (byte)0xF2, (byte)0x8A, (byte)0x75, (byte)0xDA, (byte)0xFE, 
	    		(byte)0x0D, (byte)0x29, (byte)0x66, (byte)0x31, (byte)0xE9, (byte)0x73, (byte)0x5F, (byte)0x72, 
	    		(byte)0x18, (byte)0x7C, (byte)0xAB, (byte)0x1A, (byte)0x92, (byte)0xF3, (byte)0xE1, (byte)0xD4, 
	    		(byte)0x92, (byte)0xC1, (byte)0xC5, (byte)0x47, (byte)0x81, (byte)0x86, (byte)0xC0, (byte)0xF5, 
	    		(byte)0x19, (byte)0x8D, (byte)0x5A, (byte)0x29, (byte)0x7D, (byte)0x4F, (byte)0x50, (byte)0xE5, 
	    		(byte)0x3C, (byte)0x33, (byte)0x21, (byte)0xE5, (byte)0x69, (byte)0x2B, (byte)0x6E, 
	    		};
	    return digest;
	}


}
