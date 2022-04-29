package com.labsynch.labseer.api;

import java.util.List;

import com.labsynch.labseer.domain.ExperimentLabel;
import com.labsynch.labseer.utils.PropertiesUtilService;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Transactional
@RequestMapping("api/v1/experimentlabels")
@Controller

public class ApiExperimentLabelController {

	private static final Logger logger = LoggerFactory.getLogger(ApiExperimentLabelController.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@RequestMapping(params = "find=ByLabelTextLikeAndLabelTypeAndKindEqualsAndPreferredNotAndIgnoredNot", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<java.lang.String> jsonFindExperimentLabelsByLabelTextLikeAndLabelTypeAndKindEqualsAndPreferredNotAndIgnoredNot(
			@RequestParam("labelText") String labelText, @RequestParam("labelTypeAndKind") String labelTypeAndKind,
			@RequestParam(value = "preferred", required = false) boolean preferred,
			@RequestParam(value = "ignored", required = false) boolean ignored) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<String>(
				ExperimentLabel.toJsonArray(ExperimentLabel
						.findExperimentLabelsByLabelTextLikeAndLsTypeAndKindEqualsAndPreferredNotAndIgnoredNot(
								labelText, labelTypeAndKind, preferred, ignored)
						.getResultList()),
				headers, HttpStatus.OK);
	}

	@Transactional
	@RequestMapping(params = "FindByName", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<java.lang.String> jsonFindExperimentLabelByNameGet(@RequestParam("name") String name,
			@RequestParam(value = "protocolId", required = false) Long protocolId) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		List<ExperimentLabel> experimentLabels;
		if (protocolId != null && protocolId != 0) {
			experimentLabels = ExperimentLabel.findExperimentLabelsByNameAndProtocol(name, protocolId).getResultList();
		} else {
			experimentLabels = ExperimentLabel.findExperimentLabelsByName(name).getResultList();
		}
		return new ResponseEntity<String>(ExperimentLabel.toJsonArrayStub(experimentLabels), headers, HttpStatus.OK);
	}
}
