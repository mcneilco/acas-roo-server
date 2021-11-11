package com.labsynch.labseer.service;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.labsynch.labseer.dto.LDStandardizerActionDTO;
import com.labsynch.labseer.dto.LDStandardizerInputDTO;
import com.labsynch.labseer.dto.LDStandardizerOutputDTO;
import com.labsynch.labseer.dto.SimpleStandardizerLiveDesignPropertyDTO;
import com.labsynch.labseer.exceptions.StandardizerException;
import com.labsynch.labseer.utils.Configuration;
import com.labsynch.labseer.utils.SimpleUtil;
import com.labsynch.labseer.utils.SimpleUtil.PostResponse;

@Service
public class LDStandardizerServiceImpl implements LDStandardizerService {

	private static final Logger logger = LoggerFactory.getLogger(LDStandardizerService.class);
	private static final SimpleStandardizerLiveDesignPropertyDTO ldStandardizerSettings = Configuration.getConfigInfo().getStandardizerSettings().getLivedesignSettings();
	private static final String standardizerURL = ldStandardizerSettings.getUrl();
	private static final String standardizerToken = ldStandardizerSettings.getToken();
	private static final String standardizerOutputFormat = ldStandardizerSettings.getOutputFormat();
	private static final int standardizerTimeout = ldStandardizerSettings.getTimeout();
	private static final Collection<LDStandardizerActionDTO> ldStandardizerActions = ldStandardizerSettings.getActions();


	@Override
	public String standardizeStructure(String molfile) throws StandardizerException {

    	LDStandardizerInputDTO ldStandardizerDTO = new LDStandardizerInputDTO();


    	HashMap<String, String> structures = new HashMap<String, String>();
    	structures.put("key1", molfile);
    	ldStandardizerDTO.setStructures(structures);
    	ldStandardizerDTO.setActions(ldStandardizerActions);
    	ldStandardizerDTO.setAuth_token(standardizerToken);
    	ldStandardizerDTO.setTimeout(standardizerTimeout);
    	ldStandardizerDTO.setOutput_format(standardizerOutputFormat);

    	logger.info(ldStandardizerDTO.toJson());
    	String json = ldStandardizerDTO.toJson();

		try {
			PostResponse responseObject = SimpleUtil.postRequestToExternalServerReturnObject(standardizerURL, json, logger);
			logger.info("Standardizer Response:" + responseObject.getJson());

			if (responseObject.getStatus() == 200 ){
				LDStandardizerOutputDTO response = LDStandardizerOutputDTO.fromJsonToLDStandardizerOutputDTO(responseObject.getJson());
				switch (response.getJob_status()) {
					case "SUCCESS":
						return response.getStructures().get("key1").getStructure();
					case "ERROR":
						throw new StandardizerException("Unknown error returned from standardizer:" + responseObject.getJson());
					default: 
						throw new StandardizerException("Unknown job status response returned from Live Design: " + response.getJob_status());
				}

			} else {
				throw new StandardizerException("Unknown error during standardization: " + responseObject.getJson());
			}
		} catch (IOException e){
			throw new StandardizerException("Error connecting to the Live Design standardizer " + e.getMessage());
		}

	}
}