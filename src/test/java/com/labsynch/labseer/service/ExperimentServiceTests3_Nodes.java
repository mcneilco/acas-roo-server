
package com.labsynch.labseer.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.labsynch.labseer.domain.ProtocolValue;
import com.labsynch.labseer.dto.ExperimentDataDTO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/applicationContext.xml",
		"classpath:/META-INF/spring/applicationContext-security.xml" })
@Configurable
public class ExperimentServiceTests3_Nodes {

	private static final Logger logger = LoggerFactory.getLogger(ExperimentServiceTests3_Nodes.class);

	@Autowired
	private ExperimentService experimentService;

	// @Test
	@Transactional
	public void GetExprerimentNodes_3() {

		Long protocolId = 51004L;
		List<ProtocolValue> protocolValues = ProtocolValue
				.findProtocolValuesByProtocolIDAndStateTypeKindAndValueTypeKind(protocolId, "metadata",
						"protocol metadata", "stringValue", "assay tree rule")
				.getResultList();
		String assayFolderRule = "";
		for (ProtocolValue value : protocolValues) {
			if (!value.getStringValue().equalsIgnoreCase("")) {
				assayFolderRule = value.getStringValue();
			}
			logger.info(value.toJson());

			String[] ruleSplit = assayFolderRule.split("/");
			for (String assayFolder : ruleSplit) {
				if (!assayFolder.equalsIgnoreCase("")) {
					logger.info("assay folder: " + assayFolder);
				}
			}

		}

		// Protocol protocol = Protocol.findProtocol(46686L);
		// Set<ProtocolState> protocolStates = protocol.getLsStates();
		// for (ProtocolState protocolState:protocolStates){
		// logger.info(protocolState.toJson());
		// ProtocolValue.findProtocolValuesByProtocolIDAndStateTypeKindAndValueTypeKind(protocolId,
		// stateType, stateKind, valueType, valueKind)
		// Set<ProtocolValue> protocolValues = protocolState.getLsValues();
		// for (ProtocolValue value : protocolValues){
		// logger.info(value.toJson());
		// }
		//
		// }

		// Collection<JSTreeNodeDTO> output =
		// experimentService.getExperimentNodes(null);
		// logger.debug("here is the output: " + output);

	}

	@Test
	@Transactional
	public void GetExprerimentNodes_4() {

		Set<String> batchCodes = new HashSet<String>();
		String batchCode = "GENE-001144";
		batchCodes.add(batchCode);

		boolean showOnlyPublicData = true;
		List<ExperimentDataDTO> exptDataSet = experimentService.getExperimentData(batchCode, showOnlyPublicData);
		logger.info("@@@@@@@@@@@@@@@@ printing the experiment metadata @@@@@@@@@@@@@");
		logger.info(ExperimentDataDTO.toJsonArray(exptDataSet));

	}

}
