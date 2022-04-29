
package com.labsynch.labseer.service;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.dto.IdCollectionDTO;
import com.labsynch.labseer.dto.TsvLoaderResponseDTO;
import com.labsynch.labseer.exceptions.NotFoundException;
import com.labsynch.labseer.utils.PropertiesUtilService;

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
@ContextConfiguration(locations = { "classpath:/META-INF/spring/applicationContext.xml",
		"classpath:/META-INF/spring/applicationContext-security.xml" })
@Configurable
public class AnalysisGroupServiceTests {

	private static final Logger logger = LoggerFactory.getLogger(AnalysisGroupServiceTests.class);

	@Autowired
	private AnalysisGroupService analysisGroupService;

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	// @Test
	@Transactional
	public void CreateAnalysisGroupsFromNestedJson() throws NotFoundException {
		// String json = "{\"codeName\": \"AG-00000014\",\"experiment\":
		// {\"analysisGroups\": [],\"codeName\": \"EXPT-000007\",\"experimentStates\":
		// [],\"id\": 37,\"ignored\": false,\"lsTransaction\": {\"comments\":
		// \"experiment 102 transactions\",\"id\": 22,\"recordedDate\":
		// 1.3541e+12,\"version\":0 },\"modifiedBy\": null,\"modifiedDate\":
		// null,\"protocol\": {\"codeName\": \"PROT-000006\",\"id\": 34,\"ignored\":
		// false,\"lsTransaction\": {\"comments\": \"protocol 201 transactions\",\"id\":
		// 19,\"recordedDate\": 1.3541e+12,\"version\":0 },\"modifiedBy\":
		// null,\"modifiedDate\": null,\"recordedBy\": \"userName\",\"recordedDate\":
		// 1.3541e+12,\"shortDescription\": \"protocol short description goes
		// here\",\"version\":0 },\"recordedBy\": \"userName\",\"recordedDate\":
		// null,\"shortDescription\": \"experiment short description\",\"version\":0
		// },\"recordedBy\": \"smeyer\",\"lsTransaction\": {\"comments\":
		// \"samtest101\",\"id\": 23,\"recordedDate\": 1.3541e+12,\"version\":0
		// },\"treatmentGroups\": [{\"codeName\": \"TG-000010\",\"subjects\":
		// [{\"codeName\": \"SUBJ-000011\",\"subjectStates\": [{\"subjectValues\":
		// [{\"valueType\": \"numericValue\",\"valueKind\": \"maximum\",\"stringValue\":
		// null,\"fileValue\": null,\"urlValue\": null,\"dateValue\":
		// null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\":
		// null,\"numericValue\": 226,\"sigFigs\": null,\"uncertainty\":
		// null,\"valueUnit\": \"counts\",\"concValue\": null,\"concUnit\":
		// \"\",\"comments\": null,\"ignored\": false,\"publicData\":
		// true,\"batchCode\": null,\"recordedDate\": 1.3541e+12,\"lsTransaction\":
		// {\"comments\": \"samtest101\",\"id\": 23,\"recordedDate\":
		// 1.3541e+12,\"version\":0 } },{\"valueType\": \"numericValue\",\"valueKind\":
		// \"minimum\",\"stringValue\": null,\"fileValue\": null,\"urlValue\":
		// null,\"dateValue\": null,\"clobValue\": null,\"blobValue\":
		// null,\"valueOperator\": null,\"numericValue\": 173,\"sigFigs\":
		// null,\"uncertainty\": null,\"valueUnit\": \"counts\",\"concValue\":
		// null,\"concUnit\": \"\",\"comments\": null,\"ignored\": false,\"publicData\":
		// true,\"batchCode\": null,\"recordedDate\": 1.3541e+12,\"lsTransaction\":
		// {\"comments\": \"samtest101\",\"id\": 23,\"recordedDate\":
		// 1.3541e+12,\"version\":0 } },{\"valueType\": \"numericValue\",\"valueKind\":
		// \"transformed\",\"stringValue\": null,\"fileValue\": null,\"urlValue\":
		// null,\"dateValue\": null,\"clobValue\": null,\"blobValue\":
		// null,\"valueOperator\": null,\"numericValue\": 0.30636,\"sigFigs\":
		// null,\"uncertainty\": null,\"valueUnit\": \"Eff\",\"concValue\":
		// null,\"concUnit\": \"\",\"comments\": null,\"ignored\": false,\"publicData\":
		// true,\"batchCode\": null,\"recordedDate\": 1.3541e+12,\"lsTransaction\":
		// {\"comments\": \"samtest101\",\"id\": 23,\"recordedDate\":
		// 1.3541e+12,\"version\":0 } },{\"valueType\": \"numericValue\",\"valueKind\":
		// \"normalized\",\"stringValue\": null,\"fileValue\": null,\"urlValue\":
		// null,\"dateValue\": null,\"clobValue\": null,\"blobValue\":
		// null,\"valueOperator\": null,\"numericValue\": -0.0099332,\"sigFigs\":
		// null,\"uncertainty\": null,\"valueUnit\": \"Eff\",\"concValue\":
		// null,\"concUnit\": \"\",\"comments\": null,\"ignored\": false,\"publicData\":
		// true,\"batchCode\": null,\"recordedDate\": 1.3541e+12,\"lsTransaction\":
		// {\"comments\": \"samtest101\",\"id\": 23,\"recordedDate\":
		// 1.3541e+12,\"version\":0 } } ],\"recordedBy\": \"smeyer\",\"stateType\":
		// \"data\",\"stateKind\": \"maxMinData\",\"comments\": \"\",\"lsTransaction\":
		// {\"comments\": \"samtest101\",\"id\": 23,\"recordedDate\":
		// 1.3541e+12,\"version\":0 },\"ignored\": false,\"recordedDate\": 1.3541e+12
		// },{\"subjectValues\": [{\"valueType\": \"stringValue\",\"valueKind\":
		// \"well\",\"stringValue\": \"A01\",\"fileValue\": null,\"urlValue\":
		// null,\"dateValue\": null,\"clobValue\": null,\"blobValue\":
		// null,\"valueOperator\": null,\"numericValue\": null,\"sigFigs\":
		// null,\"uncertainty\": null,\"valueUnit\": null,\"concValue\":
		// null,\"concUnit\": \"\",\"comments\": null,\"ignored\": false,\"publicData\":
		// true,\"batchCode\": null,\"recordedDate\": 1.3541e+12,\"lsTransaction\":
		// {\"comments\": \"samtest101\",\"id\": 23,\"recordedDate\":
		// 1.3541e+12,\"version\":0 } },{\"valueType\": \"stringValue\",\"valueKind\":
		// \"barcode\",\"stringValue\": \"FD-384-00399-C\",\"fileValue\":
		// null,\"urlValue\": null,\"dateValue\": null,\"clobValue\":
		// null,\"blobValue\": null,\"valueOperator\": null,\"numericValue\":
		// null,\"sigFigs\": null,\"uncertainty\": null,\"valueUnit\":
		// null,\"concValue\": null,\"concUnit\": \"\",\"comments\": null,\"ignored\":
		// false,\"publicData\": true,\"batchCode\": null,\"recordedDate\":
		// 1.3541e+12,\"lsTransaction\": {\"comments\": \"samtest101\",\"id\":
		// 23,\"recordedDate\": 1.3541e+12,\"version\":0 } },{\"valueType\":
		// \"batchCode\",\"valueKind\": \"NC\",\"stringValue\": null,\"fileValue\":
		// null,\"urlValue\": null,\"dateValue\": null,\"clobValue\":
		// null,\"blobValue\": null,\"valueOperator\": null,\"numericValue\":
		// null,\"sigFigs\": null,\"uncertainty\": null,\"valueUnit\":
		// null,\"concValue\": null,\"concUnit\": \"\",\"comments\": null,\"ignored\":
		// false,\"publicData\": true,\"batchCode\": \"CMPD000001\",\"recordedDate\":
		// 1.3541e+12,\"lsTransaction\": {\"comments\": \"samtest101\",\"id\":
		// 23,\"recordedDate\": 1.3541e+12,\"version\":0 } },{\"valueType\":
		// \"stringValue\",\"valueKind\": \"efficacyThreshold\",\"stringValue\":
		// \"false\",\"fileValue\": null,\"urlValue\": null,\"dateValue\":
		// null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\":
		// null,\"numericValue\": null,\"sigFigs\": null,\"uncertainty\":
		// null,\"valueUnit\": null,\"concValue\": null,\"concUnit\": \"\",\"comments\":
		// null,\"ignored\": false,\"publicData\": true,\"batchCode\":
		// null,\"recordedDate\": 1.3541e+12,\"lsTransaction\": {\"comments\":
		// \"samtest101\",\"id\": 23,\"recordedDate\": 1.3541e+12,\"version\":0 } }
		// ],\"recordedBy\": \"smeyer\",\"stateType\": \"metadata\",\"stateKind\":
		// \"assay plate info\",\"comments\": \"\",\"lsTransaction\": {\"comments\":
		// \"samtest101\",\"id\": 23,\"recordedDate\": 1.3541e+12,\"version\":0
		// },\"ignored\": false,\"recordedDate\": 1.3541e+12 } ],\"recordedBy\":
		// \"smeyer\",\"comments\": \"\",\"lsTransaction\": {\"comments\":
		// \"samtest101\",\"id\": 23,\"recordedDate\": 1.3541e+12,\"version\":0
		// },\"ignored\": false,\"recordedDate\": 1.3541e+12 }
		// ],\"treatmentGroupStates\": [{\"treatmentGroupValues\": [{\"valueType\":
		// \"numericValue\",\"valueKind\": \"average\",\"stringValue\":
		// null,\"fileValue\": null,\"urlValue\": null,\"dateValue\":
		// null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\":
		// null,\"numericValue\": -0.0099332,\"sigFigs\": null,\"uncertainty\":
		// null,\"valueUnit\": null,\"concValue\": null,\"concUnit\": \"\",\"comments\":
		// null,\"ignored\": false,\"publicData\": true,\"batchCode\":
		// null,\"recordedDate\": 1.3541e+12,\"lsTransaction\": {\"comments\":
		// \"samtest101\",\"id\": 23,\"recordedDate\": 1.3541e+12,\"version\":0 }
		// },{\"valueType\": \"numericValue\",\"valueKind\": \"standard
		// deviation\",\"stringValue\": null,\"fileValue\": null,\"urlValue\":
		// null,\"dateValue\": null,\"clobValue\": null,\"blobValue\":
		// null,\"valueOperator\": null,\"numericValue\": null,\"sigFigs\":
		// null,\"uncertainty\": null,\"valueUnit\": null,\"concValue\":
		// null,\"concUnit\": \"\",\"comments\": null,\"ignored\": false,\"publicData\":
		// true,\"batchCode\": null,\"recordedDate\": 1.3541e+12,\"lsTransaction\":
		// {\"comments\": \"samtest101\",\"id\": 23,\"recordedDate\":
		// 1.3541e+12,\"version\":0 } } ],\"recordedBy\": \"smeyer\",\"stateType\":
		// \"data\",\"stateKind\": \"aggregated subject data\",\"comments\":
		// \"\",\"lsTransaction\": {\"comments\": \"samtest101\",\"id\":
		// 23,\"recordedDate\": 1.3541e+12,\"version\":0 },\"ignored\":
		// false,\"recordedDate\": 1.3541e+12 } ],\"recordedBy\":
		// \"smeyer\",\"comments\": \"no comments\",\"lsTransaction\": {\"comments\":
		// \"samtest101\",\"id\": 23,\"recordedDate\": 1.3541e+12,\"version\":0
		// },\"ignored\": false,\"recordedDate\": 1.3541e+12 }
		// ],\"analysisGroupStates\": null
		// }cat(toJSON(exampleAnalysisGroup,digit=15)){\"codeName\":
		// \"AG-00000014\",\"experiment\": {\"analysisGroups\": [],\"codeName\":
		// \"EXPT-000007\",\"experimentStates\": [],\"id\": 36,\"ignored\":
		// false,\"lsTransaction\": {\"comments\": \"experiment 102
		// transactions\",\"id\": 22,\"recordedDate\":1354065956000,\"version\":0
		// },\"modifiedBy\": null,\"modifiedDate\": null,\"protocol\": {\"codeName\":
		// \"PROT-000006\",\"id\": 34,\"ignored\": false,\"lsTransaction\":
		// {\"comments\": \"protocol 201 transactions\",\"id\":
		// 19,\"recordedDate\":1354064630000,\"version\":0 },\"modifiedBy\":
		// null,\"modifiedDate\": null,\"recordedBy\":
		// \"userName\",\"recordedDate\":1354064630000,\"shortDescription\": \"protocol
		// short description goes here\",\"version\":0 },\"recordedBy\":
		// \"userName\",\"recordedDate\": null,\"shortDescription\": \"experiment short
		// description\",\"version\":0 },\"recordedBy\": \"smeyer\",\"lsTransaction\":
		// {\"comments\": \"samtest101\",\"id\":
		// 23,\"recordedDate\":1354066044000,\"version\":0 },\"treatmentGroups\":
		// [{\"codeName\": \"TG-000010\",\"subjects\": [{\"codeName\":
		// \"SUBJ-000011\",\"subjectStates\": [{\"subjectValues\": [{\"valueType\":
		// \"numericValue\",\"valueKind\": \"maximum\",\"stringValue\":
		// null,\"fileValue\": null,\"urlValue\": null,\"dateValue\":
		// null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\":
		// null,\"numericValue\": 226,\"sigFigs\": null,\"uncertainty\":
		// null,\"valueUnit\": \"counts\",\"concValue\": null,\"concUnit\":
		// \"\",\"comments\": null,\"ignored\": false,\"publicData\":
		// true,\"batchCode\": null,\"recordedDate\":1354066044000,\"lsTransaction\":
		// {\"comments\": \"samtest101\",\"id\":
		// 23,\"recordedDate\":1354066044000,\"version\":0 } },{\"valueType\":
		// \"numericValue\",\"valueKind\": \"minimum\",\"stringValue\":
		// null,\"fileValue\": null,\"urlValue\": null,\"dateValue\":
		// null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\":
		// null,\"numericValue\": 173,\"sigFigs\": null,\"uncertainty\":
		// null,\"valueUnit\": \"counts\",\"concValue\": null,\"concUnit\":
		// \"\",\"comments\": null,\"ignored\": false,\"publicData\":
		// true,\"batchCode\": null,\"recordedDate\":1354066044000,\"lsTransaction\":
		// {\"comments\": \"samtest101\",\"id\":
		// 23,\"recordedDate\":1354066044000,\"version\":0 } },{\"valueType\":
		// \"numericValue\",\"valueKind\": \"transformed\",\"stringValue\":
		// null,\"fileValue\": null,\"urlValue\": null,\"dateValue\":
		// null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\":
		// null,\"numericValue\": 0.30635838150289,\"sigFigs\": null,\"uncertainty\":
		// null,\"valueUnit\": \"Eff\",\"concValue\": null,\"concUnit\":
		// \"\",\"comments\": null,\"ignored\": false,\"publicData\":
		// true,\"batchCode\": null,\"recordedDate\":1354066044000,\"lsTransaction\":
		// {\"comments\": \"samtest101\",\"id\":
		// 23,\"recordedDate\":1354066044000,\"version\":0 } },{\"valueType\":
		// \"numericValue\",\"valueKind\": \"normalized\",\"stringValue\":
		// null,\"fileValue\": null,\"urlValue\": null,\"dateValue\":
		// null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\":
		// null,\"numericValue\": -0.00993319001154032,\"sigFigs\":
		// null,\"uncertainty\": null,\"valueUnit\": \"Eff\",\"concValue\":
		// null,\"concUnit\": \"\",\"comments\": null,\"ignored\": false,\"publicData\":
		// true,\"batchCode\": null,\"recordedDate\":1354066044000,\"lsTransaction\":
		// {\"comments\": \"samtest101\",\"id\":
		// 23,\"recordedDate\":1354066044000,\"version\":0 } } ],\"recordedBy\":
		// \"smeyer\",\"stateType\": \"data\",\"stateKind\":
		// \"maxMinData\",\"comments\": \"\",\"lsTransaction\": {\"comments\":
		// \"samtest101\",\"id\": 23,\"recordedDate\":1354066044000,\"version\":0
		// },\"ignored\": false,\"recordedDate\":1354066044000 },{\"subjectValues\":
		// [{\"valueType\": \"stringValue\",\"valueKind\": \"well\",\"stringValue\":
		// \"A01\",\"fileValue\": null,\"urlValue\": null,\"dateValue\":
		// null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\":
		// null,\"numericValue\": null,\"sigFigs\": null,\"uncertainty\":
		// null,\"valueUnit\": null,\"concValue\": null,\"concUnit\": \"\",\"comments\":
		// null,\"ignored\": false,\"publicData\": true,\"batchCode\":
		// null,\"recordedDate\":1354066044000,\"lsTransaction\": {\"comments\":
		// \"samtest101\",\"id\": 23,\"recordedDate\":1354066044000,\"version\":0 }
		// },{\"valueType\": \"stringValue\",\"valueKind\": \"barcode\",\"stringValue\":
		// \"FD-384-00399-C\",\"fileValue\": null,\"urlValue\": null,\"dateValue\":
		// null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\":
		// null,\"numericValue\": null,\"sigFigs\": null,\"uncertainty\":
		// null,\"valueUnit\": null,\"concValue\": null,\"concUnit\": \"\",\"comments\":
		// null,\"ignored\": false,\"publicData\": true,\"batchCode\":
		// null,\"recordedDate\":1354066044000,\"lsTransaction\": {\"comments\":
		// \"samtest101\",\"id\": 23,\"recordedDate\":1354066044000,\"version\":0 }
		// },{\"valueType\": \"batchCode\",\"valueKind\": \"NC\",\"stringValue\":
		// null,\"fileValue\": null,\"urlValue\": null,\"dateValue\":
		// null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\":
		// null,\"numericValue\": null,\"sigFigs\": null,\"uncertainty\":
		// null,\"valueUnit\": null,\"concValue\": null,\"concUnit\": \"\",\"comments\":
		// null,\"ignored\": false,\"publicData\": true,\"batchCode\":
		// \"CMPD000001\",\"recordedDate\":1354066044000,\"lsTransaction\":
		// {\"comments\": \"samtest101\",\"id\":
		// 23,\"recordedDate\":1354066044000,\"version\":0 } },{\"valueType\":
		// \"stringValue\",\"valueKind\": \"efficacyThreshold\",\"stringValue\":
		// false,\"fileValue\": null,\"urlValue\": null,\"dateValue\":
		// null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\":
		// null,\"numericValue\": null,\"sigFigs\": null,\"uncertainty\":
		// null,\"valueUnit\": null,\"concValue\": null,\"concUnit\": \"\",\"comments\":
		// null,\"ignored\": false,\"publicData\": true,\"batchCode\":
		// null,\"recordedDate\":1354066044000,\"lsTransaction\": {\"comments\":
		// \"samtest101\",\"id\": 23,\"recordedDate\":1354066044000,\"version\":0 } }
		// ],\"recordedBy\": \"smeyer\",\"stateType\": \"metadata\",\"stateKind\":
		// \"assay plate info\",\"comments\": \"\",\"lsTransaction\": {\"comments\":
		// \"samtest101\",\"id\": 23,\"recordedDate\":1354066044000,\"version\":0
		// },\"ignored\": false,\"recordedDate\":1354066044000 } ],\"recordedBy\":
		// \"smeyer\",\"comments\": \"\",\"lsTransaction\": {\"comments\":
		// \"samtest101\",\"id\": 23,\"recordedDate\":1354066044000,\"version\":0
		// },\"ignored\": false,\"recordedDate\":1354066044000 }
		// ],\"treatmentGroupStates\": [{\"treatmentGroupValues\": [{\"valueType\":
		// \"numericValue\",\"valueKind\": \"average\",\"stringValue\":
		// null,\"fileValue\": null,\"urlValue\": null,\"dateValue\":
		// null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\":
		// null,\"numericValue\": -0.00993319001154032,\"sigFigs\":
		// null,\"uncertainty\": null,\"valueUnit\": null,\"concValue\":
		// null,\"concUnit\": \"\",\"comments\": null,\"ignored\": false,\"publicData\":
		// true,\"batchCode\": null,\"recordedDate\":1354066044000,\"lsTransaction\":
		// {\"comments\": \"samtest101\",\"id\":
		// 23,\"recordedDate\":1354066044000,\"version\":0 } },{\"valueType\":
		// \"numericValue\",\"valueKind\": \"standard deviation\",\"stringValue\":
		// null,\"fileValue\": null,\"urlValue\": null,\"dateValue\":
		// null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\":
		// null,\"numericValue\": null,\"sigFigs\": null,\"uncertainty\":
		// null,\"valueUnit\": null,\"concValue\": null,\"concUnit\": \"\",\"comments\":
		// null,\"ignored\": false,\"publicData\": true,\"batchCode\":
		// null,\"recordedDate\":1354066044000,\"lsTransaction\": {\"comments\":
		// \"samtest101\",\"id\": 23,\"recordedDate\":1354066044000,\"version\":0 } }
		// ],\"recordedBy\": \"smeyer\",\"stateType\": \"data\",\"stateKind\":
		// \"aggregated subject data\",\"comments\": \"\",\"lsTransaction\":
		// {\"comments\": \"samtest101\",\"id\":
		// 23,\"recordedDate\":1354066044000,\"version\":0 },\"ignored\":
		// false,\"recordedDate\":1354066044000 } ],\"recordedBy\":
		// \"smeyer\",\"comments\": \"no comments\",\"lsTransaction\": {\"comments\":
		// \"samtest101\",\"id\": 23,\"recordedDate\":1354066044000,\"version\":0
		// },\"ignored\": false,\"recordedDate\":1354066044000 }
		// ],\"analysisGroupStates\": null }";
		String json1 = " { \"codeName\": \"AG-00000076test\",\"kind\": \"Dose Response\",\"experiment\":  { \"codeName\": \"EXPT-00000008\",\"id\": 97,\"ignored\": false,\"kind\": \"dose response\",\"lsTransaction\": { \"comments\": \"experiment 201 transactions\",\"id\": 10,\"recordedDate\":1362105852000,\"version\":0 },\"modifiedBy\": null,\"modifiedDate\": null,\"protocol\": { \"codeName\": \"PROT-00000003\",\"id\": 96,\"ignored\": false,\"kind\": null,\"lsTransaction\": { \"comments\": \"protocol 201 transactions\",\"id\":9,\"recordedDate\":1362105852000,\"version\":0 },\"modifiedBy\": null,\"modifiedDate\": null,\"recordedBy\": \"userName\",\"recordedDate\":1362105852000,\"shortDescription\": \"protocol short description goes here\",\"version\":0 },\"recordedBy\": \"userName\",\"recordedDate\": null,\"shortDescription\": \"experiment short description\",\"version\":0 } ,\"recordedBy\": \"userName\",\"lsTransaction\": null,\"treatmentGroups\": null,\"analysisGroupStates\": null }";
		String json = "{     \"codeName\": \"AG-00000002test\",     \"experiment\": {         \"codeName\": \"EXPT-00000002\",         \"id\": 46693,         \"ignored\": false,         \"lsKind\": \"default\",         \"lsTransaction\": 3,         \"lsType\": \"default\",         \"lsTypeAndKind\": \"default_default\",         \"recordedBy\": \"goshiro\",         \"recordedDate\": 1398186174000,         \"shortDescription\": \"Protein abundance, WSN, 18h\",         \"version\": 1     },     \"ignored\": false,     \"lsKind\": \"Gene ID Data\",     \"lsTransaction\": 3,     \"lsType\": \"default\",     \"lsTypeAndKind\": \"default_Gene ID Data\",     \"recordedBy\": \"goshiro\",     \"version\": 0 }";
		logger.info(AnalysisGroup.fromJsonToAnalysisGroup(json).toString());
		AnalysisGroup output = analysisGroupService.saveLsAnalysisGroup(AnalysisGroup.fromJsonToAnalysisGroup(json));
		logger.info(output.toJson());
	}

	@Test
	@Transactional
	public void CreateAnalysisGroupsFromNestedJson2() {
		String json = "{ \"codeName\": \"AG-00000084\",\"kind\": \"Dose Response\",\"experiment\": { \"codeName\": \"EXPT-00000008\",\"id\":        97,\"ignored\": false,\"kind\": \"dose response\",\"lsTransaction\": { \"comments\": \"experiment 201 transactions\",\"id\":        10,\"recordedDate\":  1362077052000,\"version\":        0 },\"modifiedBy\": null,\"modifiedDate\": null,\"protocol\": { \"codeName\": \"PROT-00000003\",\"id\":        96,\"ignored\": false,\"kind\": null,\"lsTransaction\": { \"comments\": \"protocol 201 transactions\",\"id\":        9,\"recordedDate\":  1362077052000,\"version\":        0 },\"modifiedBy\": null,\"modifiedDate\": null,\"recordedBy\": \"userName\",\"recordedDate\":  1362077052000,\"shortDescription\": \"protocol short description goes here\",\"version\":        0 },\"recordedBy\": \"userName\",\"recordedDate\": null,\"shortDescription\": \"experiment short description\",\"version\":        0 },\"recordedBy\": \"userName\",\"lsTransaction\": null,\"treatmentGroups\": null,\"analysisGroupLabels\": null,\"analysisGroupStates\": [ { \"analysisGroupValues\": null,\"recordedBy\": \"userName\",\"stateType\": \"data\",\"stateKind\": \"inhibition\",\"comments\": \"\",\"lsTransaction\": { \"comments\": \"experiment 202 transactions\",\"id\":        37,\"recordedDate\":  1362113789000,\"version\":        0 },\"ignored\": false,\"recordedDate\":  1362116855000 } ],\"recordedDate\":  1362116856000,\"modifiedBy\": \"userName\",\"modifiedDate\":  1362116856000 }";
		logger.info(AnalysisGroup.fromJsonToAnalysisGroup(json).toString());
		// AnalysisGroup output =
		// analysisGroupService.saveLsAnalysisGroup(AnalysisGroup.fromJsonToAnalysisGroup(json));
		// logger.info(output.toJson());
	}

	// @Test
	// @Transactional
	public void CreateAnalysisGroups() throws NotFoundException {
		// String json = "{\"codeName\": \"AG-00000014\",\"experiment\":
		// {\"analysisGroups\": [],\"codeName\": \"EXPT-000007\",\"experimentStates\":
		// [],\"id\": 36,\"ignored\": false,\"lsTransaction\": {\"comments\":
		// \"experiment 102 transactions\",\"id\": 22,\"recordedDate\":
		// 1.3541e+12,\"version\":0 },\"modifiedBy\": null,\"modifiedDate\":
		// null,\"protocol\": {\"codeName\": \"PROT-000006\",\"id\": 34,\"ignored\":
		// false,\"lsTransaction\": {\"comments\": \"protocol 201 transactions\",\"id\":
		// 19,\"recordedDate\": 1.3541e+12,\"version\":0 },\"modifiedBy\":
		// null,\"modifiedDate\": null,\"recordedBy\": \"userName\",\"recordedDate\":
		// 1.3541e+12,\"shortDescription\": \"protocol short description goes
		// here\",\"version\":0 },\"recordedBy\": \"userName\",\"recordedDate\":
		// null,\"shortDescription\": \"experiment short description\",\"version\":0
		// },\"recordedBy\": \"smeyer\",\"lsTransaction\": {\"comments\":
		// \"samtest101\",\"id\": 23,\"recordedDate\": 1.3541e+12,\"version\":0
		// },\"treatmentGroups\": [{\"codeName\": \"TG-000010\",\"subjects\":
		// [{\"codeName\": \"SUBJ-000011\",\"subjectStates\": [{\"subjectValues\":
		// [{\"valueType\": \"numericValue\",\"valueKind\": \"maximum\",\"stringValue\":
		// null,\"fileValue\": null,\"urlValue\": null,\"dateValue\":
		// null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\":
		// null,\"numericValue\": 226,\"sigFigs\": null,\"uncertainty\":
		// null,\"valueUnit\": \"counts\",\"concValue\": null,\"concUnit\":
		// \"\",\"comments\": null,\"ignored\": false,\"publicData\":
		// true,\"batchCode\": null,\"recordedDate\": 1.3541e+12,\"lsTransaction\":
		// {\"comments\": \"samtest101\",\"id\": 23,\"recordedDate\":
		// 1.3541e+12,\"version\":0 } },{\"valueType\": \"numericValue\",\"valueKind\":
		// \"minimum\",\"stringValue\": null,\"fileValue\": null,\"urlValue\":
		// null,\"dateValue\": null,\"clobValue\": null,\"blobValue\":
		// null,\"valueOperator\": null,\"numericValue\": 173,\"sigFigs\":
		// null,\"uncertainty\": null,\"valueUnit\": \"counts\",\"concValue\":
		// null,\"concUnit\": \"\",\"comments\": null,\"ignored\": false,\"publicData\":
		// true,\"batchCode\": null,\"recordedDate\": 1.3541e+12,\"lsTransaction\":
		// {\"comments\": \"samtest101\",\"id\": 23,\"recordedDate\":
		// 1.3541e+12,\"version\":0 } },{\"valueType\": \"numericValue\",\"valueKind\":
		// \"transformed\",\"stringValue\": null,\"fileValue\": null,\"urlValue\":
		// null,\"dateValue\": null,\"clobValue\": null,\"blobValue\":
		// null,\"valueOperator\": null,\"numericValue\": 0.30636,\"sigFigs\":
		// null,\"uncertainty\": null,\"valueUnit\": \"Eff\",\"concValue\":
		// null,\"concUnit\": \"\",\"comments\": null,\"ignored\": false,\"publicData\":
		// true,\"batchCode\": null,\"recordedDate\": 1.3541e+12,\"lsTransaction\":
		// {\"comments\": \"samtest101\",\"id\": 23,\"recordedDate\":
		// 1.3541e+12,\"version\":0 } },{\"valueType\": \"numericValue\",\"valueKind\":
		// \"normalized\",\"stringValue\": null,\"fileValue\": null,\"urlValue\":
		// null,\"dateValue\": null,\"clobValue\": null,\"blobValue\":
		// null,\"valueOperator\": null,\"numericValue\": -0.0099332,\"sigFigs\":
		// null,\"uncertainty\": null,\"valueUnit\": \"Eff\",\"concValue\":
		// null,\"concUnit\": \"\",\"comments\": null,\"ignored\": false,\"publicData\":
		// true,\"batchCode\": null,\"recordedDate\": 1.3541e+12,\"lsTransaction\":
		// {\"comments\": \"samtest101\",\"id\": 23,\"recordedDate\":
		// 1.3541e+12,\"version\":0 } } ],\"recordedBy\": \"smeyer\",\"stateType\":
		// \"data\",\"stateKind\": \"maxMinData\",\"comments\": \"\",\"lsTransaction\":
		// {\"comments\": \"samtest101\",\"id\": 23,\"recordedDate\":
		// 1.3541e+12,\"version\":0 },\"ignored\": false,\"recordedDate\": 1.3541e+12
		// },{\"subjectValues\": [{\"valueType\": \"stringValue\",\"valueKind\":
		// \"well\",\"stringValue\": \"A01\",\"fileValue\": null,\"urlValue\":
		// null,\"dateValue\": null,\"clobValue\": null,\"blobValue\":
		// null,\"valueOperator\": null,\"numericValue\": null,\"sigFigs\":
		// null,\"uncertainty\": null,\"valueUnit\": null,\"concValue\":
		// null,\"concUnit\": \"\",\"comments\": null,\"ignored\": false,\"publicData\":
		// true,\"batchCode\": null,\"recordedDate\": 1.3541e+12,\"lsTransaction\":
		// {\"comments\": \"samtest101\",\"id\": 23,\"recordedDate\":
		// 1.3541e+12,\"version\":0 } },{\"valueType\": \"stringValue\",\"valueKind\":
		// \"barcode\",\"stringValue\": \"FD-384-00399-C\",\"fileValue\":
		// null,\"urlValue\": null,\"dateValue\": null,\"clobValue\":
		// null,\"blobValue\": null,\"valueOperator\": null,\"numericValue\":
		// null,\"sigFigs\": null,\"uncertainty\": null,\"valueUnit\":
		// null,\"concValue\": null,\"concUnit\": \"\",\"comments\": null,\"ignored\":
		// false,\"publicData\": true,\"batchCode\": null,\"recordedDate\":
		// 1.3541e+12,\"lsTransaction\": {\"comments\": \"samtest101\",\"id\":
		// 23,\"recordedDate\": 1.3541e+12,\"version\":0 } },{\"valueType\":
		// \"batchCode\",\"valueKind\": \"NC\",\"stringValue\": null,\"fileValue\":
		// null,\"urlValue\": null,\"dateValue\": null,\"clobValue\":
		// null,\"blobValue\": null,\"valueOperator\": null,\"numericValue\":
		// null,\"sigFigs\": null,\"uncertainty\": null,\"valueUnit\":
		// null,\"concValue\": null,\"concUnit\": \"\",\"comments\": null,\"ignored\":
		// false,\"publicData\": true,\"batchCode\": \"CMPD000001\",\"recordedDate\":
		// 1.3541e+12,\"lsTransaction\": {\"comments\": \"samtest101\",\"id\":
		// 23,\"recordedDate\": 1.3541e+12,\"version\":0 } },{\"valueType\":
		// \"stringValue\",\"valueKind\": \"efficacyThreshold\",\"stringValue\":
		// \"false\",\"fileValue\": null,\"urlValue\": null,\"dateValue\":
		// null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\":
		// null,\"numericValue\": null,\"sigFigs\": null,\"uncertainty\":
		// null,\"valueUnit\": null,\"concValue\": null,\"concUnit\": \"\",\"comments\":
		// null,\"ignored\": false,\"publicData\": true,\"batchCode\":
		// null,\"recordedDate\": 1.3541e+12,\"lsTransaction\": {\"comments\":
		// \"samtest101\",\"id\": 23,\"recordedDate\": 1.3541e+12,\"version\":0 } }
		// ],\"recordedBy\": \"smeyer\",\"stateType\": \"metadata\",\"stateKind\":
		// \"assay plate info\",\"comments\": \"\",\"lsTransaction\": {\"comments\":
		// \"samtest101\",\"id\": 23,\"recordedDate\": 1.3541e+12,\"version\":0
		// },\"ignored\": false,\"recordedDate\": 1.3541e+12 } ],\"recordedBy\":
		// \"smeyer\",\"comments\": \"\",\"lsTransaction\": {\"comments\":
		// \"samtest101\",\"id\": 23,\"recordedDate\": 1.3541e+12,\"version\":0
		// },\"ignored\": false,\"recordedDate\": 1.3541e+12 }
		// ],\"treatmentGroupStates\": [{\"treatmentGroupValues\": [{\"valueType\":
		// \"numericValue\",\"valueKind\": \"average\",\"stringValue\":
		// null,\"fileValue\": null,\"urlValue\": null,\"dateValue\":
		// null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\":
		// null,\"numericValue\": -0.0099332,\"sigFigs\": null,\"uncertainty\":
		// null,\"valueUnit\": null,\"concValue\": null,\"concUnit\": \"\",\"comments\":
		// null,\"ignored\": false,\"publicData\": true,\"batchCode\":
		// null,\"recordedDate\": 1.3541e+12,\"lsTransaction\": {\"comments\":
		// \"samtest101\",\"id\": 23,\"recordedDate\": 1.3541e+12,\"version\":0 }
		// },{\"valueType\": \"numericValue\",\"valueKind\": \"standard
		// deviation\",\"stringValue\": null,\"fileValue\": null,\"urlValue\":
		// null,\"dateValue\": null,\"clobValue\": null,\"blobValue\":
		// null,\"valueOperator\": null,\"numericValue\": null,\"sigFigs\":
		// null,\"uncertainty\": null,\"valueUnit\": null,\"concValue\":
		// null,\"concUnit\": \"\",\"comments\": null,\"ignored\": false,\"publicData\":
		// true,\"batchCode\": null,\"recordedDate\": 1.3541e+12,\"lsTransaction\":
		// {\"comments\": \"samtest101\",\"id\": 23,\"recordedDate\":
		// 1.3541e+12,\"version\":0 } } ],\"recordedBy\": \"smeyer\",\"stateType\":
		// \"data\",\"stateKind\": \"aggregated subject data\",\"comments\":
		// \"\",\"lsTransaction\": {\"comments\": \"samtest101\",\"id\":
		// 23,\"recordedDate\": 1.3541e+12,\"version\":0 },\"ignored\":
		// false,\"recordedDate\": 1.3541e+12 } ],\"recordedBy\":
		// \"smeyer\",\"comments\": \"no comments\",\"lsTransaction\": {\"comments\":
		// \"samtest101\",\"id\": 23,\"recordedDate\": 1.3541e+12,\"version\":0
		// },\"ignored\": false,\"recordedDate\": 1.3541e+12 }
		// ],\"analysisGroupStates\": null
		// }cat(toJSON(exampleAnalysisGroup,digit=15)){\"codeName\":
		// \"AG-00000014\",\"experiment\": {\"analysisGroups\": [],\"codeName\":
		// \"EXPT-000007\",\"experimentStates\": [],\"id\": 36,\"ignored\":
		// false,\"lsTransaction\": {\"comments\": \"experiment 102
		// transactions\",\"id\": 22,\"recordedDate\":1354065956000,\"version\":0
		// },\"modifiedBy\": null,\"modifiedDate\": null,\"protocol\": {\"codeName\":
		// \"PROT-000006\",\"id\": 34,\"ignored\": false,\"lsTransaction\":
		// {\"comments\": \"protocol 201 transactions\",\"id\":
		// 19,\"recordedDate\":1354064630000,\"version\":0 },\"modifiedBy\":
		// null,\"modifiedDate\": null,\"recordedBy\":
		// \"userName\",\"recordedDate\":1354064630000,\"shortDescription\": \"protocol
		// short description goes here\",\"version\":0 },\"recordedBy\":
		// \"userName\",\"recordedDate\": null,\"shortDescription\": \"experiment short
		// description\",\"version\":0 },\"recordedBy\": \"smeyer\",\"lsTransaction\":
		// {\"comments\": \"samtest101\",\"id\":
		// 23,\"recordedDate\":1354066044000,\"version\":0 },\"treatmentGroups\":
		// [{\"codeName\": \"TG-000010\",\"subjects\": [{\"codeName\":
		// \"SUBJ-000011\",\"subjectStates\": [{\"subjectValues\": [{\"valueType\":
		// \"numericValue\",\"valueKind\": \"maximum\",\"stringValue\":
		// null,\"fileValue\": null,\"urlValue\": null,\"dateValue\":
		// null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\":
		// null,\"numericValue\": 226,\"sigFigs\": null,\"uncertainty\":
		// null,\"valueUnit\": \"counts\",\"concValue\": null,\"concUnit\":
		// \"\",\"comments\": null,\"ignored\": false,\"publicData\":
		// true,\"batchCode\": null,\"recordedDate\":1354066044000,\"lsTransaction\":
		// {\"comments\": \"samtest101\",\"id\":
		// 23,\"recordedDate\":1354066044000,\"version\":0 } },{\"valueType\":
		// \"numericValue\",\"valueKind\": \"minimum\",\"stringValue\":
		// null,\"fileValue\": null,\"urlValue\": null,\"dateValue\":
		// null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\":
		// null,\"numericValue\": 173,\"sigFigs\": null,\"uncertainty\":
		// null,\"valueUnit\": \"counts\",\"concValue\": null,\"concUnit\":
		// \"\",\"comments\": null,\"ignored\": false,\"publicData\":
		// true,\"batchCode\": null,\"recordedDate\":1354066044000,\"lsTransaction\":
		// {\"comments\": \"samtest101\",\"id\":
		// 23,\"recordedDate\":1354066044000,\"version\":0 } },{\"valueType\":
		// \"numericValue\",\"valueKind\": \"transformed\",\"stringValue\":
		// null,\"fileValue\": null,\"urlValue\": null,\"dateValue\":
		// null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\":
		// null,\"numericValue\": 0.30635838150289,\"sigFigs\": null,\"uncertainty\":
		// null,\"valueUnit\": \"Eff\",\"concValue\": null,\"concUnit\":
		// \"\",\"comments\": null,\"ignored\": false,\"publicData\":
		// true,\"batchCode\": null,\"recordedDate\":1354066044000,\"lsTransaction\":
		// {\"comments\": \"samtest101\",\"id\":
		// 23,\"recordedDate\":1354066044000,\"version\":0 } },{\"valueType\":
		// \"numericValue\",\"valueKind\": \"normalized\",\"stringValue\":
		// null,\"fileValue\": null,\"urlValue\": null,\"dateValue\":
		// null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\":
		// null,\"numericValue\": -0.00993319001154032,\"sigFigs\":
		// null,\"uncertainty\": null,\"valueUnit\": \"Eff\",\"concValue\":
		// null,\"concUnit\": \"\",\"comments\": null,\"ignored\": false,\"publicData\":
		// true,\"batchCode\": null,\"recordedDate\":1354066044000,\"lsTransaction\":
		// {\"comments\": \"samtest101\",\"id\":
		// 23,\"recordedDate\":1354066044000,\"version\":0 } } ],\"recordedBy\":
		// \"smeyer\",\"stateType\": \"data\",\"stateKind\":
		// \"maxMinData\",\"comments\": \"\",\"lsTransaction\": {\"comments\":
		// \"samtest101\",\"id\": 23,\"recordedDate\":1354066044000,\"version\":0
		// },\"ignored\": false,\"recordedDate\":1354066044000 },{\"subjectValues\":
		// [{\"valueType\": \"stringValue\",\"valueKind\": \"well\",\"stringValue\":
		// \"A01\",\"fileValue\": null,\"urlValue\": null,\"dateValue\":
		// null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\":
		// null,\"numericValue\": null,\"sigFigs\": null,\"uncertainty\":
		// null,\"valueUnit\": null,\"concValue\": null,\"concUnit\": \"\",\"comments\":
		// null,\"ignored\": false,\"publicData\": true,\"batchCode\":
		// null,\"recordedDate\":1354066044000,\"lsTransaction\": {\"comments\":
		// \"samtest101\",\"id\": 23,\"recordedDate\":1354066044000,\"version\":0 }
		// },{\"valueType\": \"stringValue\",\"valueKind\": \"barcode\",\"stringValue\":
		// \"FD-384-00399-C\",\"fileValue\": null,\"urlValue\": null,\"dateValue\":
		// null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\":
		// null,\"numericValue\": null,\"sigFigs\": null,\"uncertainty\":
		// null,\"valueUnit\": null,\"concValue\": null,\"concUnit\": \"\",\"comments\":
		// null,\"ignored\": false,\"publicData\": true,\"batchCode\":
		// null,\"recordedDate\":1354066044000,\"lsTransaction\": {\"comments\":
		// \"samtest101\",\"id\": 23,\"recordedDate\":1354066044000,\"version\":0 }
		// },{\"valueType\": \"batchCode\",\"valueKind\": \"NC\",\"stringValue\":
		// null,\"fileValue\": null,\"urlValue\": null,\"dateValue\":
		// null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\":
		// null,\"numericValue\": null,\"sigFigs\": null,\"uncertainty\":
		// null,\"valueUnit\": null,\"concValue\": null,\"concUnit\": \"\",\"comments\":
		// null,\"ignored\": false,\"publicData\": true,\"batchCode\":
		// \"CMPD000001\",\"recordedDate\":1354066044000,\"lsTransaction\":
		// {\"comments\": \"samtest101\",\"id\":
		// 23,\"recordedDate\":1354066044000,\"version\":0 } },{\"valueType\":
		// \"stringValue\",\"valueKind\": \"efficacyThreshold\",\"stringValue\":
		// false,\"fileValue\": null,\"urlValue\": null,\"dateValue\":
		// null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\":
		// null,\"numericValue\": null,\"sigFigs\": null,\"uncertainty\":
		// null,\"valueUnit\": null,\"concValue\": null,\"concUnit\": \"\",\"comments\":
		// null,\"ignored\": false,\"publicData\": true,\"batchCode\":
		// null,\"recordedDate\":1354066044000,\"lsTransaction\": {\"comments\":
		// \"samtest101\",\"id\": 23,\"recordedDate\":1354066044000,\"version\":0 } }
		// ],\"recordedBy\": \"smeyer\",\"stateType\": \"metadata\",\"stateKind\":
		// \"assay plate info\",\"comments\": \"\",\"lsTransaction\": {\"comments\":
		// \"samtest101\",\"id\": 23,\"recordedDate\":1354066044000,\"version\":0
		// },\"ignored\": false,\"recordedDate\":1354066044000 } ],\"recordedBy\":
		// \"smeyer\",\"comments\": \"\",\"lsTransaction\": {\"comments\":
		// \"samtest101\",\"id\": 23,\"recordedDate\":1354066044000,\"version\":0
		// },\"ignored\": false,\"recordedDate\":1354066044000 }
		// ],\"treatmentGroupStates\": [{\"treatmentGroupValues\": [{\"valueType\":
		// \"numericValue\",\"valueKind\": \"average\",\"stringValue\":
		// null,\"fileValue\": null,\"urlValue\": null,\"dateValue\":
		// null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\":
		// null,\"numericValue\": -0.00993319001154032,\"sigFigs\":
		// null,\"uncertainty\": null,\"valueUnit\": null,\"concValue\":
		// null,\"concUnit\": \"\",\"comments\": null,\"ignored\": false,\"publicData\":
		// true,\"batchCode\": null,\"recordedDate\":1354066044000,\"lsTransaction\":
		// {\"comments\": \"samtest101\",\"id\":
		// 23,\"recordedDate\":1354066044000,\"version\":0 } },{\"valueType\":
		// \"numericValue\",\"valueKind\": \"standard deviation\",\"stringValue\":
		// null,\"fileValue\": null,\"urlValue\": null,\"dateValue\":
		// null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\":
		// null,\"numericValue\": null,\"sigFigs\": null,\"uncertainty\":
		// null,\"valueUnit\": null,\"concValue\": null,\"concUnit\": \"\",\"comments\":
		// null,\"ignored\": false,\"publicData\": true,\"batchCode\":
		// null,\"recordedDate\":1354066044000,\"lsTransaction\": {\"comments\":
		// \"samtest101\",\"id\": 23,\"recordedDate\":1354066044000,\"version\":0 } }
		// ],\"recordedBy\": \"smeyer\",\"stateType\": \"data\",\"stateKind\":
		// \"aggregated subject data\",\"comments\": \"\",\"lsTransaction\":
		// {\"comments\": \"samtest101\",\"id\":
		// 23,\"recordedDate\":1354066044000,\"version\":0 },\"ignored\":
		// false,\"recordedDate\":1354066044000 } ],\"recordedBy\":
		// \"smeyer\",\"comments\": \"no comments\",\"lsTransaction\": {\"comments\":
		// \"samtest101\",\"id\": 23,\"recordedDate\":1354066044000,\"version\":0
		// },\"ignored\": false,\"recordedDate\":1354066044000 }
		// ],\"analysisGroupStates\": null }";
		String json = "{ 'codeName': 'AG-00000075', 'kind': 'Dose Response', 'experiment': [ { 'codeName': 'EXPT-00000008', 'id':    97, 'ignored': false, 'kind': 'dose response', 'lsTransaction': { 'comments': 'experiment 201 transactions', 'id':    10, 'recordedDate': 1362105852000, 'version':    0 }, 'modifiedBy': null, 'modifiedDate': null, 'protocol': { 'codeName': 'PROT-00000003', 'id':    96, 'ignored': false, 'kind': null, 'lsTransaction': { 'comments': 'protocol 201 transactions', 'id':    9, 'recordedDate': 1362105852000, 'version':    0 }, 'modifiedBy': null, 'modifiedDate': null, 'recordedBy': 'userName', 'recordedDate': 1362105852000, 'shortDescription': 'protocol short description goes here', 'version':    0 }, 'recordedBy': 'userName', 'recordedDate': null, 'shortDescription': 'experiment short description', 'version':    0 } ], 'recordedBy': 'userName', 'lsTransaction': null, 'treatmentGroups': null, 'analysisGroupStates': null }";
		AnalysisGroup output = analysisGroupService.saveLsAnalysisGroup(AnalysisGroup.fromJsonToAnalysisGroup(json));
		logger.info(output.toJson());
	}

	// @Test
	// @Transactional
	public void CreateAnalysisGroupsFromNestedJson5() {
		String json = "[   {     \"codeName\": \"AG-00219240\",     \"lsType\": \"default\",     \"lsKind\": \"Generic\",     \"experiment\": {       \"id\": 206273,       \"version\": 1     },     \"recordedBy\": \"smeyer\",     \"lsTransaction\": 12426,     \"treatmentGroups\": null,     \"lsStates\": [       {         \"analysisGroup\": null,         \"lsValues\": [           {             \"lsState\": null,             \"lsType\": \"numericValue\",             \"lsKind\": \"%Control\",             \"stringValue\": null,             \"fileValue\": null,             \"urlValue\": null,             \"dateValue\": null,             \"clobValue\": null,             \"blobValue\": null,             \"operatorKind\": null,             \"operatorType\": \"comparison\",             \"numericValue\": 68.87,             \"sigFigs\": null,             \"uncertainty\": null,             \"uncertaintyType\": null,             \"numberOfReplicates\": null,             \"unitKind\": null,             \"comments\": null,             \"ignored\": false,             \"publicData\": true,             \"codeValue\": null,             \"recordedBy\": \"smeyer\",             \"recordedDate\": 1380132462000,             \"lsTransaction\": 12426           }         ],         \"recordedBy\": \"smeyer\",         \"lsType\": \"data\",         \"lsKind\": \"Generic\",         \"comments\": \"\",         \"lsTransaction\": 12426,         \"ignored\": false,         \"recordedDate\": 1380132462000       }     ],     \"recordedDate\": 1380132462000   } ]";

		logger.info("------------------ MAGIC OUTPUT -----------------------------------------------");
		logger.info("-----------------------------------------------------------------");
		logger.info(AnalysisGroup.fromJsonArrayToAnalysisGroups(json).toString());
		logger.info("-----------------------------------------------------------------");
		logger.info("-----------------------------------------------------------------");

		Collection<AnalysisGroup> analysisGroups = AnalysisGroup.fromJsonArrayToAnalysisGroups(json);
		Collection<AnalysisGroup> savedanalysisGroups = new ArrayList<AnalysisGroup>();
		for (AnalysisGroup analysisGroup : analysisGroups) {
			logger.info(analysisGroup.toPrettyJson());
			// savedanalysisGroups.add(analysisGroupService.saveLsAnalysisGroup(analysisGroup));
		}
		// AnalysisGroup output =
		// analysisGroupService.saveLsAnalysisGroup(AnalysisGroup.fromJsonToAnalysisGroup(json));

		// logger.info(AnalysisGroup.toJsonArray(savedanalysisGroups));
	}

	@Test
	// @Transactional
	public void CreateAnalysisGroupFromNestedJson8() throws NotFoundException {
		String json = "	{\"lsType\":\"data\",\"lsKind\":\"results\",\"experiments\":[{\"id\":55,\"version\":1}],\"recordedBy\":\"smeyer\",\"lsTransaction\":null,\"treatmentGroups\":null,\"lsStates\":null,\"recordedDate\":1403114845000}";
		AnalysisGroup analysisGroup = AnalysisGroup.fromJsonToAnalysisGroup(json);
		AnalysisGroup output = analysisGroupService.saveLsAnalysisGroup(analysisGroup);
		logger.debug(output.toPrettyFullJson());
		Assert.assertFalse("experiment should exist", output.getExperiments().isEmpty());
		Assert.assertTrue("found experiment", output.getExperiments().contains(Experiment.findExperiment(55L)));
	}

	@Test
	// @Transactional
	public void CreateAnalysisGroupFromCSVTest() throws Exception {
		// String analysisGroupFilePath =
		// "src/test/resources/csvUploadAnalysisGroupDateValueTest.csv";

		// String analysisGroupFilePath =
		// "src/test/resources/csvUploadAnalysisGroup100curves.csv";
		// String treatmentGroupFilePath =
		// "src/test/resources/csvUploadTreatmentGroup100curves.csv";
		// String subjectFilePath = "src/test/resources/csvUploadSubject100curves.csv";
		String analysisGroupFilePath = "src/test/resources/csvUploadAnalysisGroup.csv";
		String treatmentGroupFilePath = "src/test/resources/csvUploadTreatmentGroup.csv";
		String subjectFilePath = "src/test/resources/csvUploadSubject.csv";

		// String treatmentGroupFilePath = null;
		// String subjectFilePath = null;

		long startTime = new Date().getTime();
		TsvLoaderResponseDTO result = analysisGroupService.saveLsAnalysisGroupFromCsv(analysisGroupFilePath,
				treatmentGroupFilePath, subjectFilePath);
		long endTime = new Date().getTime();
		long totalTime = endTime - startTime;
		logger.info("dataLoaded: " + "true" + "   total elapsed time: " + totalTime + " ms");
		logger.info(result.toJson());
		Assert.assertNotNull(result.getAnalysisGroups());
		Assert.assertNotNull(result.getTreatmentGroups());
		Assert.assertNotNull(result.getSubjects());

		// timing notes: running locally
		// AG - 0.9 s
		// TG - 6.0 s
		// S - 17.5 s
		// Total: 24.4 s

		// Change: removing extra linking statements (Experiment is already being linked
		// to AnalysisGroup)
		// AG - 0.86 s
		// TG - 5.8 s
		// S - 13.0 s
		// Total: 19.7 s

		// Combining merges into a list to be merged at the end
		// AG - 0.74 s
		// TG - 2.3 s
		// S - 9.7 s
		// Total: 12.7 s
	}

	@Transactional
	@Test
	public void fromJsonArrayTest() {
		String json = "[{\"codeName\":\"AG-00017601\",\"lsType\":\"default\",\"lsKind\":\"default\",\"experiments\":[{\"id\":661993,\"version\":1}],\"recordedBy\":\"smeyer\",\"lsTransaction\":1694,\"treatmentGroups\":null,\"lsStates\":[{\"analysisGroup\":null,\"lsValues\":[{\"lsState\":null,\"lsType\":\"fileValue\",\"lsKind\":\"report file\",\"stringValue\":null,\"fileValue\":\"experiments/EXPT-00000208/3_Panel Screen.pdf\",\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":\"Panel Screen - Exp3\",\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"codeOrigin\":null,\"codeType\":null,\"codeKind\":null,\"recordedBy\":\"username\",\"recordedDate\":1412967173000,\"lsTransaction\":1694},{\"lsState\":null,\"lsType\":\"codeValue\",\"lsKind\":\"batch code\",\"stringValue\":null,\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":\"CMPD-0000001-01\",\"codeOrigin\":null,\"codeType\":null,\"codeKind\":null,\"recordedBy\":\"username\",\"recordedDate\":1412967173000,\"lsTransaction\":1694}],\"recordedBy\":\"smeyer\",\"lsType\":\"metadata\",\"lsKind\":\"report locations\",\"comments\":\"\",\"lsTransaction\":1694,\"ignored\":false,\"recordedDate\":1412967173000}],\"recordedDate\":1412967173000}]";
		List<IdCollectionDTO> idList = new ArrayList<IdCollectionDTO>();
		IdCollectionDTO idDTO = null;
		int i = 0;
		BufferedReader br = null;
		StringReader sr = null;
		try {
			sr = new StringReader(json);
			br = new BufferedReader(sr);
			for (AnalysisGroup analysisGroup : AnalysisGroup.fromJsonArrayToAnalysisGroups(br)) {
				logger.debug("saving analysis group number: " + i);
				AnalysisGroup saved = analysisGroupService.saveLsAnalysisGroup(analysisGroup);
				idDTO = new IdCollectionDTO();
				idDTO.setId(saved.getId());
				idDTO.setVersion(saved.getVersion());
				idList.add(idDTO);
				i++;
			}
			logger.info("saved!");
		} catch (Exception e) {
			logger.info("Caught exception " + e);
		}
	}

	@Transactional
	@Test
	public void toJsonFromJsonTest() {
		AnalysisGroup ag = AnalysisGroup.findAnalysisGroup(662214L);
		Set<AnalysisGroup> agSet = new HashSet<AnalysisGroup>();
		agSet.add(ag);
		Assert.assertNotNull(ag);
		// Testing the toJson methods
		String fullJson = ag.toFullJson();
		String prettyFullJson = ag.toPrettyFullJson();
		String json = ag.toJson();
		logger.info(json);
		String jsonStub = ag.toJsonStub();
		String prettyJson = ag.toPrettyJson();
		String prettyJsonStub = ag.toPrettyJsonStub();
		String jsonArray = AnalysisGroup.toJsonArray(agSet);

		// Testing the fromJson methods
		BufferedReader br = null;
		StringReader sr = null;
		sr = new StringReader(json);
		br = new BufferedReader(sr);
		AnalysisGroup ag2 = AnalysisGroup.fromJsonToAnalysisGroup(json);
		AnalysisGroup ag3 = AnalysisGroup.fromJsonToAnalysisGroup2(br);
		sr = new StringReader(jsonArray);
		br = new BufferedReader(sr);
		Collection<AnalysisGroup> ags2 = AnalysisGroup.fromJsonArrayToAnalysisGroups(br);
		Collection<AnalysisGroup> ags3 = AnalysisGroup.fromJsonArrayToAnalysisGroups(jsonArray);
	}

}
