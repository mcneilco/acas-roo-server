package com.labsynch.labseer.api;


import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.labsynch.labseer.domain.ContainerKind;
import com.labsynch.labseer.domain.ContainerType;
import com.labsynch.labseer.domain.DDictKind;
import com.labsynch.labseer.domain.DDictType;
import com.labsynch.labseer.domain.ExperimentKind;
import com.labsynch.labseer.domain.ExperimentType;
import com.labsynch.labseer.domain.InteractionKind;
import com.labsynch.labseer.domain.InteractionType;
import com.labsynch.labseer.domain.LabelKind;
import com.labsynch.labseer.domain.LabelSequence;
import com.labsynch.labseer.domain.LabelType;
import com.labsynch.labseer.domain.OperatorKind;
import com.labsynch.labseer.domain.OperatorType;
import com.labsynch.labseer.domain.ProtocolKind;
import com.labsynch.labseer.domain.ProtocolType;
import com.labsynch.labseer.domain.RoleKind;
import com.labsynch.labseer.domain.RoleType;
import com.labsynch.labseer.domain.StateKind;
import com.labsynch.labseer.domain.StateType;
import com.labsynch.labseer.domain.ThingKind;
import com.labsynch.labseer.domain.ThingType;
import com.labsynch.labseer.domain.UnitKind;
import com.labsynch.labseer.domain.UnitType;
import com.labsynch.labseer.domain.ValueKind;
import com.labsynch.labseer.domain.ValueType;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.TypeDTO;
import com.labsynch.labseer.dto.TypeKindDTO;
import com.labsynch.labseer.service.DataDictionaryService;
import com.labsynch.labseer.service.LabelSequenceService;

@Controller
@RequestMapping("api/v1/setup")
@Transactional
public class ApiSetupController {
	
	private static final Logger logger = LoggerFactory.getLogger(ApiSetupController.class);
	
	@Autowired
	private DataDictionaryService dataDictionaryService;
	
	@Autowired
	private LabelSequenceService labelSequenceService;
	
	@Transactional
    @RequestMapping(value = "/protocoltypes", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> getOrCreateProtocolTypes(@RequestBody List<TypeDTO> types) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		Collection<ProtocolType> results = TypeDTO.getOrCreateProtocolTypes(types);
		return new ResponseEntity<String>(ProtocolType.toJsonArray(results), headers, HttpStatus.CREATED);
	}
	
	@Transactional
    @RequestMapping(value = "/protocolkinds", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> getOrCreateProtocolKinds(@RequestBody List<TypeKindDTO> typeKinds) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		Collection<ProtocolKind> results = TypeKindDTO.getOrCreateProtocolKinds(typeKinds);
		return new ResponseEntity<String>(ProtocolKind.toJsonArray(results), headers, HttpStatus.CREATED);
	}
	
	@Transactional
    @RequestMapping(value = "/experimenttypes", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> getOrCreateExperimentTypes(@RequestBody List<TypeDTO> types) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		Collection<ExperimentType> results = TypeDTO.getOrCreateExperimentTypes(types);
		return new ResponseEntity<String>(ExperimentType.toJsonArray(results), headers, HttpStatus.CREATED);
	}
	
	@Transactional
    @RequestMapping(value = "/experimentkinds", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> getOrCreateExperimentKinds(@RequestBody List<TypeKindDTO> typeKinds) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		Collection<ExperimentKind> results = TypeKindDTO.getOrCreateExperimentKinds(typeKinds);
		return new ResponseEntity<String>(ExperimentKind.toJsonArray(results), headers, HttpStatus.CREATED);
	}
	
	@Transactional
    @RequestMapping(value = "/interactiontypes", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> getOrCreateInteractionTypes(@RequestBody List<InteractionType> types) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		Collection<InteractionType> results = TypeDTO.getOrCreateInteractionTypes(types);
		return new ResponseEntity<String>(InteractionType.toJsonArray(results), headers, HttpStatus.CREATED);
	}
	
	@Transactional
    @RequestMapping(value = "/interactionkinds", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> getOrCreateInteractionKinds(@RequestBody List<TypeKindDTO> typeKinds) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		Collection<InteractionKind> results = TypeKindDTO.getOrCreateInteractionKinds(typeKinds);
		return new ResponseEntity<String>(InteractionKind.toJsonArray(results), headers, HttpStatus.CREATED);
	}
	
	@Transactional
    @RequestMapping(value = "/containertypes", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> getOrCreateContainerTypes(@RequestBody List<TypeDTO> types) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		Collection<ContainerType> results = TypeDTO.getOrCreateContainerTypes(types);
		return new ResponseEntity<String>(ContainerType.toJsonArray(results), headers, HttpStatus.CREATED);
	}
	
	@Transactional
    @RequestMapping(value = "/containerkinds", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> getOrCreateContainerKinds(@RequestBody List<TypeKindDTO> typeKinds) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		Collection<ContainerKind> results = TypeKindDTO.getOrCreateContainerKinds(typeKinds);
		return new ResponseEntity<String>(ContainerKind.toJsonArray(results), headers, HttpStatus.CREATED);
	}
	
	@Transactional
    @RequestMapping(value = "/statetypes", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> getOrCreateStateTypes(@RequestBody List<TypeDTO> types) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		Collection<StateType> results = TypeDTO.getOrCreateStateTypes(types);
		return new ResponseEntity<String>(StateType.toJsonArray(results), headers, HttpStatus.CREATED);
	}
	
	@Transactional
    @RequestMapping(value = "/statekinds", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> getOrCreateStateKinds(@RequestBody List<TypeKindDTO> typeKinds) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		Collection<StateKind> results = TypeKindDTO.getOrCreateStateKinds(typeKinds);
		return new ResponseEntity<String>(StateKind.toJsonArray(results), headers, HttpStatus.CREATED);
	}
	
	@Transactional
    @RequestMapping(value = "/valuetypes", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> getOrCreateValueTypes(@RequestBody List<TypeDTO> types) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		Collection<ValueType> results = TypeDTO.getOrCreateValueTypes(types);
		return new ResponseEntity<String>(ValueType.toJsonArray(results), headers, HttpStatus.CREATED);
	}
	
	@Transactional
    @RequestMapping(value = "/valuekinds", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> getOrCreateValueKinds(@RequestBody List<TypeKindDTO> typeKinds) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		Collection<ValueKind> results = TypeKindDTO.getOrCreateValueKinds(typeKinds);
		return new ResponseEntity<String>(ValueKind.toJsonArray(results), headers, HttpStatus.CREATED);
	}
	
	@Transactional
    @RequestMapping(value = "/labeltypes", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> getOrCreateLabelTypes(@RequestBody List<TypeDTO> types) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		Collection<LabelType> results = TypeDTO.getOrCreateLabelTypes(types);
		return new ResponseEntity<String>(LabelType.toJsonArray(results), headers, HttpStatus.CREATED);
	}
	
	@Transactional
    @RequestMapping(value = "/labelkinds", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> getOrCreateLabelKinds(@RequestBody List<TypeKindDTO> typeKinds) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		Collection<LabelKind> results = TypeKindDTO.getOrCreateLabelKinds(typeKinds);
		return new ResponseEntity<String>(LabelKind.toJsonArray(results), headers, HttpStatus.CREATED);
	}
	
	@Transactional
    @RequestMapping(value = "/thingtypes", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> getOrCreateThingTypes(@RequestBody List<TypeDTO> types) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		Collection<ThingType> results = TypeDTO.getOrCreateThingTypes(types);
		return new ResponseEntity<String>(ThingType.toJsonArray(results), headers, HttpStatus.CREATED);
	}
	
	@Transactional
    @RequestMapping(value = "/thingkinds", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> getOrCreateThingKinds(@RequestBody List<TypeKindDTO> typeKinds) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		Collection<ThingKind> results = TypeKindDTO.getOrCreateThingKinds(typeKinds);
		return new ResponseEntity<String>(ThingKind.toJsonArray(results), headers, HttpStatus.CREATED);
	}
	
	@Transactional
    @RequestMapping(value = "/operatortypes", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> getOrCreateOperatorTypes(@RequestBody List<TypeDTO> types) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		Collection<OperatorType> results = TypeDTO.getOrCreateOperatorTypes(types);
		return new ResponseEntity<String>(OperatorType.toJsonArray(results), headers, HttpStatus.CREATED);
	}
	
	@Transactional
    @RequestMapping(value = "/operatorkinds", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> getOrCreateOperatorKinds(@RequestBody List<TypeKindDTO> typeKinds) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		Collection<OperatorKind> results = TypeKindDTO.getOrCreateOperatorKinds(typeKinds);
		return new ResponseEntity<String>(OperatorKind.toJsonArray(results), headers, HttpStatus.CREATED);
	}
	
	@Transactional
    @RequestMapping(value = "/unittypes", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> getOrCreateUnitTypes(@RequestBody List<TypeDTO> types) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		Collection<UnitType> results = TypeDTO.getOrCreateUnitTypes(types);
		return new ResponseEntity<String>(UnitType.toJsonArray(results), headers, HttpStatus.CREATED);
	}
	
	@Transactional
    @RequestMapping(value = "/unitkinds", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> getOrCreateUnitKinds(@RequestBody List<TypeKindDTO> typeKinds) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		Collection<UnitKind> results = TypeKindDTO.getOrCreateUnitKinds(typeKinds);
		return new ResponseEntity<String>(UnitKind.toJsonArray(results), headers, HttpStatus.CREATED);
	}
	
	@Transactional
    @RequestMapping(value = "/ddicttypes", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> getOrCreateDDictTypes(@RequestBody List<TypeDTO> types) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		Collection<DDictType> results = TypeDTO.getOrCreateDDictTypes(types);
		return new ResponseEntity<String>(DDictType.toJsonArray(results), headers, HttpStatus.CREATED);
	}
	
	@Transactional
    @RequestMapping(value = "/ddictkinds", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> getOrCreateDDictKinds(@RequestBody List<TypeKindDTO> typeKinds) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		Collection<DDictKind> results = TypeKindDTO.getOrCreateDDictKinds(typeKinds);
		return new ResponseEntity<String>(DDictKind.toJsonArray(results), headers, HttpStatus.CREATED);
	}
	
	@Transactional
    @RequestMapping(value = "/roletypes", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> getOrCreateRoleTypes(@RequestBody List<TypeDTO> types) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		Collection<RoleType> results = TypeDTO.getOrCreateRoleTypes(types);
		return new ResponseEntity<String>(RoleType.toJsonArray(results), headers, HttpStatus.CREATED);
	}
	
	@Transactional
    @RequestMapping(value = "/rolekinds", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> getOrCreateRoleKinds(@RequestBody List<TypeKindDTO> typeKinds) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		Collection<RoleKind> results = TypeKindDTO.getOrCreateRoleKinds(typeKinds);
		return new ResponseEntity<String>(RoleKind.toJsonArray(results), headers, HttpStatus.CREATED);
	}
	
	@Transactional
	@RequestMapping(value = "/codetables", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> getOrCreateCodeTables(@RequestBody List<CodeTableDTO> codeTableDTOs) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		Boolean createTypeKind = true;
		List<CodeTableDTO> savedCodeTableValues = dataDictionaryService.getOrCreateCodeTableArray(codeTableDTOs, createTypeKind);
		return new ResponseEntity<String>(CodeTableDTO.toJsonArray(savedCodeTableValues), headers, HttpStatus.CREATED);
	}
	
	@Transactional
	@RequestMapping(value = "/labelsequences", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<java.lang.String> getOrCreateLabelSequences(@RequestBody List<LabelSequence> labelSequences) {
		HttpHeaders headers = new HttpHeaders();
	    headers.add("Content-Type", "application/json");
	    Collection<LabelSequence> savedLabelSequences = labelSequenceService.saveLabelSequenceArray(labelSequences);
	    return new ResponseEntity<String>(LabelSequence.toJsonArray(savedLabelSequences), headers, HttpStatus.CREATED);
	}
	
	
}
