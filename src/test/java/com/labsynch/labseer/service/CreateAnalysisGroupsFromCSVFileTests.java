

package com.labsynch.labseer.service;

import java.io.IOException;
import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.dto.TempThingDTO;
import com.labsynch.labseer.utils.PropertiesUtilService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml")
@Configurable
public class CreateAnalysisGroupsFromCSVFileTests {

	private static final Logger logger = LoggerFactory.getLogger(CreateAnalysisGroupsFromCSVFileTests.class);

	@Autowired
	private AutoLabelService autoLabelService;

	@Autowired
	private AnalysisGroupService analysisGroupService;
	
	@Autowired
	private TreatmentGroupService treatmentGroupService;

	@Autowired
	private SubjectService subjectService;
	
	@Autowired
	private PropertiesUtilService propertiesUtilService;


	private String fieldDelimiter = ",";

	
	@Test
	@Transactional
	public void ReadCSVFile_Test11() throws IOException{
		
		String absoluteFilePath = "/Users/fairway/Documents/McNeilCo/acas-roo-server/src/test/resources/analysisGroupData_v3.csv";
		HashMap<String, TempThingDTO> output = analysisGroupService.createAnalysisGroupsFromCSV(absoluteFilePath );
		
		String treatmentGroupFilePath = "/Users/fairway/Documents/McNeilCo/acas-roo-server/src/test/resources/treatmentGroupData_v3.csv";
		HashMap<String, TempThingDTO> output2 = treatmentGroupService.createTreatmentGroupsFromCSV(treatmentGroupFilePath, output);
		
		//TODO: Gregory please implement the following service
		// please look at the two services above for working examples
		String subjectFilePath = "/Users/fairway/Documents/McNeilCo/acas-roo-server/src/test/resources/subjectData_v3.csv";
		HashMap<String, TempThingDTO> output3 = subjectService.createSubjectsFromCSV(subjectFilePath, output2);
		
		
		
	}
	


}
