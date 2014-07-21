

package com.labsynch.labseer.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import com.labsynch.labseer.domain.AbstractThing;
import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.AnalysisGroupState;
import com.labsynch.labseer.domain.AnalysisGroupValue;
import com.labsynch.labseer.domain.Container;
import com.labsynch.labseer.domain.ContainerState;
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.LabelKind;
import com.labsynch.labseer.domain.LabelSequence;
import com.labsynch.labseer.domain.LabelType;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.LsThingLabel;
import com.labsynch.labseer.domain.LsThingState;
import com.labsynch.labseer.domain.LsThingValue;
import com.labsynch.labseer.domain.LsTransaction;
import com.labsynch.labseer.domain.StateKind;
import com.labsynch.labseer.domain.StateType;
import com.labsynch.labseer.domain.ThingKind;
import com.labsynch.labseer.domain.ThingType;
import com.labsynch.labseer.domain.ValueKind;
import com.labsynch.labseer.domain.ValueType;
import com.labsynch.labseer.dto.AnalysisGroupCsvDTO;
import com.labsynch.labseer.dto.AutoLabelDTO;
import com.labsynch.labseer.dto.EntrezDbGeneDTO;
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
		
		String absoluteFilePath = "/Users/goshiro2014/Documents/McNeilco_2012/clients/ACAS_ROO_GIT/git/acas-roo-server/src/test/resources/analysisGroupData_v3.csv";
		HashMap<String, TempThingDTO> output = analysisGroupService.createAnalysisGroupsFromCSV(absoluteFilePath );
		
		String treatmentGroupFilePath = "/Users/goshiro2014/Documents/McNeilco_2012/clients/ACAS_ROO_GIT/git/acas-roo-server/src/test/resources/treatmentGroupData_v3.csv";
		HashMap<String, TempThingDTO> output2 = treatmentGroupService.createTreatmentGroupsFromCSV(treatmentGroupFilePath, output);
		
		//TODO: Gregory please implement the following service
		// please look at the two services above for working examples
		String subjectFilePath = "/Users/goshiro2014/Documents/McNeilco_2012/clients/ACAS_ROO_GIT/git/acas-roo-server/src/test/resources/subjectData_v3.csv";
		HashMap<String, TempThingDTO> output3 = subjectService.createSubjectsFromCSV(subjectFilePath, output2);
		
		
		
	}
	


}
