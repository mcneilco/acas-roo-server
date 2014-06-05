package com.labsynch.labseer.web;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.Protocol;
import com.labsynch.labseer.dto.ExperimentGuiStubDTO;
import com.labsynch.labseer.service.ExperimentService;
import com.labsynch.labseer.utils.PropertiesUtilService;

@Controller
@RequestMapping("api/v1/experiments")
@RooWebScaffold(path = "experiments", formBackingObject = Experiment.class)
@RooWebFinder
@Transactional
@RooWebJson(jsonObject = Experiment.class)
public class ApiExperimentController {

	private static final Logger logger = LoggerFactory.getLogger(ApiExperimentController.class);

	@Autowired
	private ExperimentService experimentService;

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@RequestMapping(value = "/dto", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<java.lang.String> listJson(
			@RequestParam(value = "protocolKind", required = false) String protocolKind,
			@RequestParam(value = "protocolName", required = false) String protocolName) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		List<ExperimentGuiStubDTO> result = new ArrayList<ExperimentGuiStubDTO>();
		
		if (protocolKind != null && protocolName != null){
			List<Protocol> protocols = Protocol.findProtocolsByLsKindEquals(protocolKind).getResultList();
			for (Protocol protocol:protocols){
				if (protocol.findPreferredName().equals(protocolName)){
					List<Experiment> experiments = Experiment.findExperimentsByProtocol(protocol).getResultList();
					for (Experiment experiment:experiments){
						ExperimentGuiStubDTO exptGui = new ExperimentGuiStubDTO(experiment);
						result.add(exptGui);
					}
				}
			}
		} else if (protocolKind != null && protocolName == null){
			List<Protocol> protocols = Protocol.findProtocolsByLsKindEquals(protocolKind).getResultList();
			for (Protocol protocol:protocols){
				if (protocol.findPreferredName().equals(protocolName)){
					List<Experiment> experiments = Experiment.findExperimentsByProtocol(protocol).getResultList();
					for (Experiment experiment:experiments){
						ExperimentGuiStubDTO exptGui = new ExperimentGuiStubDTO(experiment);
						result.add(exptGui);
					}
				}
			}
		}  else if (protocolKind == null && protocolName != null){
			List<Protocol> protocols = Protocol.findProtocolByProtocolName(protocolName);
			for (Protocol protocol:protocols){
				List<Experiment> experiments = Experiment.findExperimentsByProtocol(protocol).getResultList();
				for (Experiment experiment:experiments){
					ExperimentGuiStubDTO exptGui = new ExperimentGuiStubDTO(experiment);
					result.add(exptGui);
				}
			}
		} else {
			List<Experiment> experiments = Experiment.findAllExperiments();
			for (Experiment experiment:experiments){
				ExperimentGuiStubDTO exptGui = new ExperimentGuiStubDTO(experiment);
				result.add(exptGui);
			}
		}

		return new ResponseEntity<String>(ExperimentGuiStubDTO.toJsonArray(result), headers, HttpStatus.OK);
	}

}
