

package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.HashSet;

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.AnalysisGroupState;
import com.labsynch.labseer.domain.AnalysisGroupValue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import junit.framework.Assert;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class AnalysisGroupStateServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(AnalysisGroupStateServiceTest.class);

	@Autowired
	private AnalysisGroupStateService analysisGroupStateService;

	//@Test
	public void SimpleTest_1(){
		AnalysisGroupState analysisGroupState = AnalysisGroupState.findAnalysisGroupState(5527L);
		logger.info(analysisGroupState.toJson());
		logger.info(AnalysisGroupValue.toJsonArray(AnalysisGroupValue.findAnalysisGroupValuesByLsState(analysisGroupState).getResultList()));
		Collection<AnalysisGroupState> analysisGroupStates = new HashSet<AnalysisGroupState>();
		analysisGroupStates.add(analysisGroupState);
		Collection<AnalysisGroupState> results = analysisGroupStateService.ignoreAllAnalysisGroupStates(analysisGroupStates);
		logger.info(AnalysisGroupState.toJsonArray(results));
	}

	//@Test
	public void SimpleTest_2(){

		String json1 = "{\"id\":12250,\"ignored\":false,\"lsKind\":\"Dose Response\",\"lsTransaction\":98,\"lsType\":\"data\",\"lsTypeAndKind\":\"data_Dose Response\",\"lsValues\":[{\"id\":21090,\"ignored\":false,\"lsKind\":\"curve id\",\"lsTransaction\":98,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_curve id\",\"operatorType\":\"comparison\",\"operatorTypeAndKind\":\"comparison_null\",\"publicData\":true,\"recordedBy\":\"jmcneil\",\"recordedDate\":1378922450000,\"stringValue\":\"b_AG-00000504\",\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"id\":21094,\"ignored\":false,\"lsKind\":\"EC50\",\"lsTransaction\":98,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_EC50\",\"numericValue\":0.525047,\"operatorType\":\"comparison\",\"operatorTypeAndKind\":\"comparison_null\",\"publicData\":true,\"recordedBy\":\"jmcneil\",\"recordedDate\":1378922450000,\"unitKind\":\"uM\",\"unitTypeAndKind\":\"null_uM\",\"version\":0},{\"id\":21093,\"ignored\":false,\"lsKind\":\"Min\",\"lsTransaction\":98,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_Min\",\"numericValue\":0,\"operatorType\":\"comparison\",\"operatorTypeAndKind\":\"comparison_null\",\"publicData\":true,\"recordedBy\":\"jmcneil\",\"recordedDate\":1378922450000,\"unitKind\":\"efficacy\",\"unitTypeAndKind\":\"null_efficacy\",\"version\":0},{\"id\":21095,\"ignored\":false,\"lsKind\":\"Max\",\"lsTransaction\":98,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_Max\",\"numericValue\":100,\"operatorType\":\"comparison\",\"operatorTypeAndKind\":\"comparison_null\",\"publicData\":true,\"recordedBy\":\"jmcneil\",\"recordedDate\":1378922450000,\"unitKind\":\"efficacy\",\"unitTypeAndKind\":\"null_efficacy\",\"version\":0},{\"id\":21097,\"ignored\":false,\"lsKind\":\"Hill slope\",\"lsTransaction\":98,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_Hill slope\",\"numericValue\":0.986075,\"operatorType\":\"comparison\",\"operatorTypeAndKind\":\"comparison_null\",\"publicData\":true,\"recordedBy\":\"jmcneil\",\"recordedDate\":1378922450000,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"id\":21092,\"ignored\":false,\"lsKind\":\"Rendering Hint\",\"lsTransaction\":98,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_Rendering Hint\",\"operatorType\":\"comparison\",\"operatorTypeAndKind\":\"comparison_null\",\"publicData\":false,\"recordedBy\":\"jmcneil\",\"recordedDate\":1378922450000,\"stringValue\":\"4 parameter D-R\",\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeValue\":\"CMPD-0000001-02\",\"id\":21096,\"ignored\":false,\"lsKind\":\"batch code\",\"lsTransaction\":98,\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_batch code\",\"operatorType\":\"comparison\",\"operatorTypeAndKind\":\"comparison_null\",\"publicData\":false,\"recordedBy\":\"jmcneil\",\"recordedDate\":1378922450000,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"id\":21091,\"ignored\":false,\"lsKind\":\"category\",\"lsTransaction\":98,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_Category\",\"operatorType\":\"comparison\",\"operatorTypeAndKind\":\"comparison_null\",\"publicData\":true,\"recordedBy\":\"jmcneil\",\"recordedDate\":1378922450000,\"stringValue\":\"good\",\"unitTypeAndKind\":\"null_null\",\"version\":0}],\"recordedBy\":\"jmcneil\",\"recordedDate\":1378922450000,\"version\":null}";
		String json = "{\"id\":12250,\"ignored\":false,\"lsKind\":\"Dose Response\",\"lsTransaction\":98,\"lsType\":\"data\",\"lsTypeAndKind\":\"data_Dose Response\",\"lsValues\":[],\"recordedBy\":\"jmcneil\",\"recordedDate\":1378897250000,\"version\":1,\"id\":669045,\"ignored\":false,\"lsKind\":\"Dose Response\",\"lsTransaction\":98,\"lsType\":\"data\",\"lsTypeAndKind\":\"data_Dose Response\",\"lsValues\":[{\"id\":21091,\"ignored\":false,\"lsKind\":\"category\",\"lsTransaction\":98,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_Category\",\"operatorType\":\"comparison\",\"operatorTypeAndKind\":\"comparison_null\",\"publicData\":true,\"recordedBy\":\"jmcneil\",\"recordedDate\":1378897250000,\"stringValue\":\"good\",\"unitTypeAndKind\":\"null_null\",\"version\":1},{\"id\":21093,\"ignored\":false,\"lsKind\":\"Min\",\"lsTransaction\":98,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_Min\",\"numericValue\":0,\"operatorType\":\"comparison\",\"operatorTypeAndKind\":\"comparison_null\",\"publicData\":true,\"recordedBy\":\"jmcneil\",\"recordedDate\":1378897250000,\"unitKind\":\"efficacy\",\"unitTypeAndKind\":\"null_efficacy\",\"version\":1},{\"id\":21095,\"ignored\":false,\"lsKind\":\"Max\",\"lsTransaction\":98,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_Max\",\"numericValue\":100,\"operatorType\":\"comparison\",\"operatorTypeAndKind\":\"comparison_null\",\"publicData\":true,\"recordedBy\":\"jmcneil\",\"recordedDate\":1378897250000,\"unitKind\":\"efficacy\",\"unitTypeAndKind\":\"null_efficacy\",\"version\":1},{\"id\":21090,\"ignored\":false,\"lsKind\":\"curve id\",\"lsTransaction\":98,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_curve id\",\"operatorType\":\"comparison\",\"operatorTypeAndKind\":\"comparison_null\",\"publicData\":true,\"recordedBy\":\"jmcneil\",\"recordedDate\":1378897250000,\"stringValue\":\"b_AG-00000504\",\"unitTypeAndKind\":\"null_null\",\"version\":1},{\"id\":21092,\"ignored\":false,\"lsKind\":\"Rendering Hint\",\"lsTransaction\":98,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_Rendering Hint\",\"operatorType\":\"comparison\",\"operatorTypeAndKind\":\"comparison_null\",\"publicData\":false,\"recordedBy\":\"jmcneil\",\"recordedDate\":1378897250000,\"stringValue\":\"4 parameter D-R\",\"unitTypeAndKind\":\"null_null\",\"version\":1},{\"codeValue\":\"CMPD-0000001-02\",\"id\":21096,\"ignored\":false,\"lsKind\":\"batch code\",\"lsTransaction\":98,\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_batch code\",\"operatorType\":\"comparison\",\"operatorTypeAndKind\":\"comparison_null\",\"publicData\":false,\"recordedBy\":\"jmcneil\",\"recordedDate\":1378897250000,\"unitTypeAndKind\":\"null_null\",\"version\":1},{\"id\":21094,\"ignored\":false,\"lsKind\":\"EC50\",\"lsTransaction\":98,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_EC50\",\"numericValue\":0.525047,\"operatorType\":\"comparison\",\"operatorTypeAndKind\":\"comparison_null\",\"publicData\":true,\"recordedBy\":\"jmcneil\",\"recordedDate\":1378897250000,\"unitKind\":\"uM\",\"unitTypeAndKind\":\"null_uM\",\"version\":1},{\"id\":21097,\"ignored\":false,\"lsKind\":\"Hill slope\",\"lsTransaction\":98,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_Hill slope\",\"numericValue\":0.986075,\"operatorType\":\"comparison\",\"operatorTypeAndKind\":\"comparison_null\",\"publicData\":true,\"recordedBy\":\"jmcneil\",\"recordedDate\":1378897250000,\"unitTypeAndKind\":\"null_null\",\"version\":1}],\"recordedBy\":\"jmcneil\",\"recordedDate\":1378897250000,\"version\":1}";

		AnalysisGroupState analysisGroupState = AnalysisGroupState.fromJsonToAnalysisGroupState(json);
		if (analysisGroupState.getAnalysisGroup() == null){
			AnalysisGroupState oldAGS = AnalysisGroupState.findAnalysisGroupState(analysisGroupState.getId());
			AnalysisGroup ag = AnalysisGroup.findAnalysisGroup(oldAGS.getAnalysisGroup().getId());
			analysisGroupState.setAnalysisGroup(ag);			
		}
		for (AnalysisGroupValue agv : analysisGroupState.getLsValues()){
			if (agv.getLsState() == null){
				agv.setLsState(analysisGroupState);
			}
		}

		logger.info(analysisGroupState.toJson());
		if (analysisGroupState.merge() == null) {
			logger.error("unable to merge");
		} else {
			logger.info("OK");
		}

	}

	@Test
	public void SimpleTest_POST_3(){

		//String json = "[{\"analysisGroup\":{\"id\":485682,\"version\":0},\"lsValues\":[{\"lsState\":null,\"lsType\":\"numericValue\",\"lsKind\":\"Max\",\"stringValue\":null,\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":89.2453333333333,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":\"Standard Error\",\"numberOfReplicates\":null,\"unitKind\":\"efficacy\",\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"recordedBy\":\"bbolt\",\"recordedDate\":1394138174000,\"lsTransaction\":347},{\"lsState\":null,\"lsType\":\"numericValue\",\"lsKind\":\"EC50\",\"stringValue\":null,\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"operatorKind\":\">\",\"operatorType\":\"comparison\",\"numericValue\":20,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":\"Standard Error\",\"numberOfReplicates\":null,\"unitKind\":\"uM\",\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"recordedBy\":\"bbolt\",\"recordedDate\":1394138174000,\"lsTransaction\":347},{\"lsState\":null,\"lsType\":\"codeValue\",\"lsKind\":\"batch code\",\"stringValue\":null,\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":\"Standard Error\",\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":\"CMPD-0000008-01\",\"recordedBy\":\"bbolt\",\"recordedDate\":1394138174000,\"lsTransaction\":347},{\"lsState\":null,\"lsType\":\"stringValue\",\"lsKind\":\"curve id\",\"stringValue\":\"AG-00000511_347\",\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":\"Standard Error\",\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"recordedBy\":\"bbolt\",\"recordedDate\":1394138174000,\"lsTransaction\":347},{\"lsState\":null,\"lsType\":\"numericValue\",\"lsKind\":\"Fitted Slope\",\"stringValue\":null,\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":-0.424495356173893,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":\"Standard Error\",\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"recordedBy\":\"bbolt\",\"recordedDate\":1394138174000,\"lsTransaction\":347},{\"lsState\":null,\"lsType\":\"numericValue\",\"lsKind\":\"Fitted Min\",\"stringValue\":null,\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":-66.4989493376197,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":\"Standard Error\",\"numberOfReplicates\":null,\"unitKind\":\"efficacy\",\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"recordedBy\":\"bbolt\",\"recordedDate\":1394138174000,\"lsTransaction\":347},{\"lsState\":null,\"lsType\":\"numericValue\",\"lsKind\":\"Fitted Max\",\"stringValue\":null,\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":124.570888685824,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":\"Standard Error\",\"numberOfReplicates\":null,\"unitKind\":\"efficacy\",\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"recordedBy\":\"bbolt\",\"recordedDate\":1394138174000,\"lsTransaction\":347},{\"lsState\":null,\"lsType\":\"numericValue\",\"lsKind\":\"Fitted EC50\",\"stringValue\":null,\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":0.467531341507942,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":\"Standard Error\",\"numberOfReplicates\":null,\"unitKind\":\"uM\",\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"recordedBy\":\"bbolt\",\"recordedDate\":1394138174000,\"lsTransaction\":347},{\"lsState\":null,\"lsType\":\"numericValue\",\"lsKind\":\"SSE\",\"stringValue\":null,\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":1446.67303943487,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":\"Standard Error\",\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"recordedBy\":\"bbolt\",\"recordedDate\":1394138174000,\"lsTransaction\":347},{\"lsState\":null,\"lsType\":\"numericValue\",\"lsKind\":\"SST\",\"stringValue\":null,\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":41819.4245830937,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":\"Standard Error\",\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"recordedBy\":\"bbolt\",\"recordedDate\":1394138174000,\"lsTransaction\":347},{\"lsState\":null,\"lsType\":\"numericValue\",\"lsKind\":\"rSquared\",\"stringValue\":null,\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":0.965406672763744,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":\"Standard Error\",\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"recordedBy\":\"bbolt\",\"recordedDate\":1394138174000,\"lsTransaction\":347},{\"lsState\":null,\"lsType\":\"stringValue\",\"lsKind\":\"category\",\"stringValue\":\"Inactive\",\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":\"Standard Error\",\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"recordedBy\":\"bbolt\",\"recordedDate\":1394138174000,\"lsTransaction\":347},{\"lsState\":null,\"lsType\":\"stringValue\",\"lsKind\":\"flag\",\"stringValue\":\"algorithm\",\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":\"Standard Error\",\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"recordedBy\":\"bbolt\",\"recordedDate\":1394138174000,\"lsTransaction\":347}],\"recordedBy\":\"bbolt\",\"lsType\":\"data\",\"lsKind\":\"Dose Response\",\"comments\":\"\",\"lsTransaction\":347,\"ignored\":false,\"recordedDate\":1394138890000}]";
		String json = "[{\"analysisGroup\":{\"id\":233,\"version\":0},\"lsValues\":null,\"recordedBy\":\"smeyer\",\"lsType\":\"data\",\"lsKind\":\"results\",\"lsTransaction\":71,\"ignored\":false,\"recordedDate\":1381939115000},{\"analysisGroup\":{\"id\":234,\"version\":0},\"lsValues\":null,\"recordedBy\":\"smeyer\",\"lsType\":\"data\",\"lsKind\":\"results\",\"lsTransaction\":71,\"ignored\":false,\"recordedDate\":1381939115000},{\"analysisGroup\":{\"id\":235,\"version\":0},\"lsValues\":null,\"recordedBy\":\"smeyer\",\"lsType\":\"data\",\"lsKind\":\"results\",\"lsTransaction\":71,\"ignored\":false,\"recordedDate\":1381939115000},{\"analysisGroup\":{\"id\":236,\"version\":0},\"lsValues\":null,\"recordedBy\":\"smeyer\",\"lsType\":\"data\",\"lsKind\":\"results\",\"lsTransaction\":71,\"ignored\":false,\"recordedDate\":1381939115000},{\"analysisGroup\":{\"id\":237,\"version\":0},\"lsValues\":null,\"recordedBy\":\"smeyer\",\"lsType\":\"data\",\"lsKind\":\"results\",\"lsTransaction\":71,\"ignored\":false,\"recordedDate\":1381939115000},{\"analysisGroup\":{\"id\":238,\"version\":0},\"lsValues\":null,\"recordedBy\":\"smeyer\",\"lsType\":\"data\",\"lsKind\":\"results\",\"lsTransaction\":71,\"ignored\":false,\"recordedDate\":1381939115000},{\"analysisGroup\":{\"id\":239,\"version\":0},\"lsValues\":null,\"recordedBy\":\"smeyer\",\"lsType\":\"data\",\"lsKind\":\"results\",\"lsTransaction\":71,\"ignored\":false,\"recordedDate\":1381939115000},{\"analysisGroup\":{\"id\":240,\"version\":0},\"lsValues\":null,\"recordedBy\":\"smeyer\",\"lsType\":\"data\",\"lsKind\":\"results\",\"lsTransaction\":71,\"ignored\":false,\"recordedDate\":1381939115000}]";

		Collection<AnalysisGroupState> analysisGroupStates = AnalysisGroupState.fromJsonArrayToAnalysisGroupStates(json);
		logger.info(AnalysisGroupState.toJsonArray(analysisGroupStates));
		for (AnalysisGroupState analysisGroupState : analysisGroupStates){
			if (analysisGroupState.getAnalysisGroup() == null){
				logger.info("the analysis group is null");
			}
			if (analysisGroupState.getLsValues() != null){
				logger.error("the lsValues are null");
				logger.info("number of stateValues: " + analysisGroupState.getLsValues().size());
				for (AnalysisGroupValue agv : analysisGroupState.getLsValues()){
					if (agv.getLsState() == null){
						logger.info("the analysis group state in values is null");
						//						agv.setLsState(analysisGroupState);
					}
				}			
			}

			//analysisGroupState.persist();
			logger.info(analysisGroupState.toJson());
		}

	}
	
	@Test
	@Transactional
	public void createAnalysisGroupStateByAnalysisGroupIdAndStateTypeKindTest() {
		Long analysisGroupId = 3L;
		String lsType = "metadata";
		String lsKind = "analysisGroup metadata";
		AnalysisGroupState analysisGroupState = analysisGroupStateService.createAnalysisGroupStateByAnalysisGroupIdAndStateTypeKind(analysisGroupId, lsType, lsKind);
		Assert.assertNotNull(analysisGroupState);
		logger.info(analysisGroupState.toJson());
	}
}
