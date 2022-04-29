package com.labsynch.labseer.api;

import java.util.HashMap;

import com.labsynch.labseer.utils.LoadFullCompoundsUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping(value = {"/api/v1/registration"})
@Controller
public class RegisterCompoundsController {
	
	Logger logger = LoggerFactory.getLogger(RegisterCompoundsController.class);

	@Autowired
	private LoadFullCompoundsUtil lcu;
	
	@RequestMapping(value = "/cmpds", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> searchCmpdsByParamsPost(
			@RequestParam (value = "inputFileName", required = true) String inputFileName,
			@RequestParam (value = "errorFileName", required = true) String errorFileName,
			@RequestParam (value = "compoundAlias", required = false) String compoundAlias,
			@RequestParam (value = "supplierId", required = false) String supplierId,
			@RequestParam (value = "project", required = false) String project,
			@RequestParam (value = "supplier", required = false) String supplier,
			@RequestParam (value = "notebook", required = false) String notebook,
			@RequestParam (value = "stereoCategory", required = false) String stereoCategory,
			@RequestParam (value = "compoundChemist", required = false) String compoundChemist,
			@RequestParam (value = "compoundChemistCode", required = false) String compoundChemistCode

			) {
		
		logger.debug("hit the controller to register the compounds: " + inputFileName + " out: " + errorFileName);
		
        HttpHeaders headers= new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
		
		HashMap<String, String> propertiesMap = new HashMap<String, String>();
		if (compoundAlias != null) propertiesMap.put("compound_alias", compoundAlias);
		if (supplierId != null) propertiesMap.put("supplier_id", supplierId);
		if (project != null) propertiesMap.put("project", project);
		if (supplier != null) propertiesMap.put("supplier", supplier);
		if (notebook != null) propertiesMap.put("notebook", notebook);
		if (stereoCategory != null) propertiesMap.put("stereo_category", stereoCategory);
		if (compoundChemist != null) propertiesMap.put("compound_chemist", compoundChemist);
		if (compoundChemistCode != null) propertiesMap.put("compound_chemist_code", compoundChemistCode);
		
		lcu.loadCompounds(inputFileName, errorFileName, propertiesMap);	
		
        return new ResponseEntity<String>(headers, HttpStatus.OK);

//        example curl to call the method
//        curl -i -X POST -H "Accept: application/json" 'http://localhost:8080/cmpdreg/api/v1/registration/cmpds?inputFileName=/tmp/spark1.sdf&errorFileName=/tmp/spark1_errorMols.sdf&compoundAlias=ALIAS&supplierId=VENDORCATNO&project=PROJECT&supplier=IGNYTA&notebook=NOTEBOOK&stereoCategory=unknown&compoundChemist=CHEMIST&compoundChemistCode=chemadmin'        
	}

}
