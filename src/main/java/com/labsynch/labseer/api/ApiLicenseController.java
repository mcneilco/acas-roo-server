package com.labsynch.labseer.api;

import java.io.IOException;
import java.net.URISyntaxException;

import org.bouncycastle.openpgp.PGPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.labsynch.labseer.dto.LicenseDTO;
import com.labsynch.labseer.service.LicenseService;

@Controller
@RequestMapping("/api/v1/license")
@Transactional

public class ApiLicenseController {

	private static final Logger logger = LoggerFactory.getLogger(ApiLicenseController.class);

	@Autowired
	private LicenseService licenseService;
	
    @RequestMapping(value = "/checkLicense", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> getLicenseInfo() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
    	LicenseDTO licenseInfo = null;
      	try {
      		licenseInfo = licenseService.getLicenseInfo();
      	} catch (IOException e) {
      		logger.error(e.toString());
      		return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
      	} catch (PGPException  e) {
      		logger.error(e.toString());
      		return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
      	} catch (URISyntaxException e) {
      		logger.error(e.toString());
      		return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
      	}
        if (licenseInfo == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(licenseInfo.toJson(), headers, HttpStatus.OK);
    }


}
