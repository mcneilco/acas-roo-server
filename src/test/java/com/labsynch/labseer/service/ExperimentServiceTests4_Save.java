package com.labsynch.labseer.service;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.exceptions.NotFoundException;
import com.labsynch.labseer.exceptions.UniqueNameException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/applicationContext.xml",
		"classpath:/META-INF/spring/applicationContext-security.xml" })
@Configurable
public class ExperimentServiceTests4_Save {

	private static final Logger logger = LoggerFactory
			.getLogger(ExperimentServiceTests4_Save.class);

	@Autowired
	private ExperimentService experimentService;
	
	@Autowired
	private ExperimentValueService experimentValueService;

	
	@Test
	@Transactional
	public void uniqueNameExceptionTest() throws UniqueNameException{
		String json = "{\"lsKind\":\"gx depot\",\"lsLabels\":[{\"labelText\":\"Guy TEST DUPE 101\",\"lsKind\":\"experiment name\",\"lsType\":\"name\",\"preferred\":true,\"recordedBy\":\"goshiro\"}],\"lsTransaction\":8,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_gx depot\",\"protocol\":{\"codeName\":\"PROT-00000001\",\"deleted\":false,\"id\":1,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":5,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"recordedBy\":\"smeyer\",\"recordedDate\":1425511298000,\"shortDescription\":\"gx depot\",\"version\":1},\"recordedBy\":\"goshiro\",\"shortDescription\":\"test dupe experiments\"}";
		Experiment experiment = Experiment.fromJsonToExperiment(json);
		try {
			experimentService.saveLsExperiment(experiment);
		} catch (UniqueNameException e){
			logger.info(e.toString());
			Assert.assertNotNull(e);
		} catch (NotFoundException e) {
			logger.info(e.toString());
			Assert.assertNotNull(e);
		}
	}

	@Test
	@Transactional
	public void saveExperimentReuseAGTest() throws UniqueNameException{
		String json = "{\"protocol\":{\"codeName\":\"PROT-00000001\",\"deleted\":false,\"id\":1,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[{\"deleted\":false,\"id\":1,\"ignored\":false,\"labelText\":\"TEST\",\"lsKind\":\"protocol name\",\"lsTransaction\":5,\"lsType\":\"name\",\"lsTypeAndKind\":\"name_protocol name\",\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"smeyer\",\"recordedDate\":1425511298000,\"version\":0}],\"lsTags\":[],\"lsTransaction\":5,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"recordedBy\":\"smeyer\",\"recordedDate\":1425511298000,\"shortDescription\":\"gx depot\",\"version\":1},\"lsType\":\"default\",\"lsKind\":\"default\",\"shortDescription\":\"GUY TESTING\",\"lsTransaction\":2618,\"recordedBy\":\"cs_rest\",\"recordedDate\":1432762518000,\"modifiedBy\":\"cs_rest\",\"modifiedDate\":1432762518000,\"lsLabels\":null,\"lsStates\":[{\"experiment\":null,\"lsValues\":[{\"lsState\":null,\"lsType\":\"codeValue\",\"lsKind\":\"project\",\"stringValue\":\"9345\",\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"concentration\":null,\"concUnit\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"codeOrigin\":null,\"codeType\":null,\"codeKind\":null,\"recordedBy\":\"cs_rest\",\"recordedDate\":1432762518000,\"lsTransaction\":2618},{\"lsState\":null,\"lsType\":\"stringValue\",\"lsKind\":\"experiment name\",\"stringValue\":\"3condTest\",\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"concentration\":null,\"concUnit\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"codeOrigin\":null,\"codeType\":null,\"codeKind\":null,\"recordedBy\":\"cs_rest\",\"recordedDate\":1432762518000,\"lsTransaction\":2618},{\"lsState\":null,\"lsType\":\"stringValue\",\"lsKind\":\"parent experiment name\",\"stringValue\":\"6.02BAM\",\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"concentration\":null,\"concUnit\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"codeOrigin\":null,\"codeType\":null,\"codeKind\":null,\"recordedBy\":\"cs_rest\",\"recordedDate\":1432762518000,\"lsTransaction\":2618},{\"lsState\":null,\"lsType\":\"stringValue\",\"lsKind\":\"master experiment name\",\"stringValue\":\"DAL testLoad_10_3_3cond_RIN_Lane\",\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"concentration\":null,\"concUnit\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"codeOrigin\":null,\"codeType\":null,\"codeKind\":null,\"recordedBy\":\"cs_rest\",\"recordedDate\":1432762518000,\"lsTransaction\":2618},{\"lsState\":null,\"lsType\":\"codeValue\",\"lsKind\":\"analysis program\",\"stringValue\":null,\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"concentration\":null,\"concUnit\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":\"DESeq2\",\"codeOrigin\":null,\"codeType\":null,\"codeKind\":null,\"recordedBy\":\"cs_rest\",\"recordedDate\":1432762518000,\"lsTransaction\":2618},{\"lsState\":null,\"lsType\":\"codeValue\",\"lsKind\":\"analysis type\",\"stringValue\":null,\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"concentration\":null,\"concUnit\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":\"differential expression\",\"codeOrigin\":null,\"codeType\":null,\"codeKind\":null,\"recordedBy\":\"cs_rest\",\"recordedDate\":1432762518000,\"lsTransaction\":2618},{\"lsState\":null,\"lsType\":\"stringValue\",\"lsKind\":\"experimental condition\",\"stringValue\":\"Mass\",\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"concentration\":null,\"concUnit\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"codeOrigin\":null,\"codeType\":null,\"codeKind\":null,\"recordedBy\":\"cs_rest\",\"recordedDate\":1432762518000,\"lsTransaction\":2618},{\"lsState\":null,\"lsType\":\"stringValue\",\"lsKind\":\"control condition\",\"stringValue\":\"Spaced\",\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"concentration\":null,\"concUnit\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"codeOrigin\":null,\"codeType\":null,\"codeKind\":null,\"recordedBy\":\"cs_rest\",\"recordedDate\":1432762518000,\"lsTransaction\":2618}],\"recordedBy\":\"cs_rest\",\"lsType\":\"metadata\",\"lsKind\":\"experiment metadata\",\"comments\":\"\",\"lsTransaction\":2618,\"ignored\":false,\"recordedDate\":1432762518000},{\"experiment\":null,\"lsValues\":[],\"recordedBy\":\"cs_rest\",\"lsType\":\"data\",\"lsKind\":\"results\",\"comments\":\"\",\"lsTransaction\":2618,\"ignored\":false,\"recordedDate\":1432762518000}],\"lsTags\":[],\"analysisGroups\":[{\"codeName\":\"AG-00003348\",\"deleted\":false,\"id\":9349,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":2550,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1432762518023,\"recordedBy\":\"userName\",\"recordedDate\":1432750563000},{\"codeName\":\"AG-00003352\",\"deleted\":false,\"id\":9353,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":2550,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1432762518067,\"recordedBy\":\"userName\",\"recordedDate\":1432750567000},{\"codeName\":\"AG-00003351\",\"deleted\":false,\"id\":9352,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":2550,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1432762517986,\"recordedBy\":\"userName\",\"recordedDate\":1432750566000},{\"codeName\":\"AG-00003347\",\"deleted\":false,\"id\":9348,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":2550,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1432762518037,\"recordedBy\":\"userName\",\"recordedDate\":1432750561000},{\"codeName\":\"AG-00003349\",\"deleted\":false,\"id\":9350,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":2550,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1432762518083,\"recordedBy\":\"userName\",\"recordedDate\":1432750564000},{\"codeName\":\"AG-00003350\",\"deleted\":false,\"id\":9351,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":2550,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1432762518009,\"recordedBy\":\"userName\",\"recordedDate\":1432750565000},{\"codeName\":\"AG-00003346\",\"deleted\":false,\"id\":9347,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":2550,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1432762518052,\"recordedBy\":\"userName\",\"recordedDate\":1432750560000}]}";
		Experiment experiment = Experiment.fromJsonToExperiment(json);
		try {
			experimentService.saveLsExperiment(experiment);
		} catch (UniqueNameException e){
			logger.info(e.toString());
			Assert.assertNotNull(e);
		} catch (NotFoundException e) {
			logger.info(e.toString());
			Assert.assertNotNull(e);
		}
	}
}
