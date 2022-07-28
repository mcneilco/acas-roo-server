package com.labsynch.labseer.api;

import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.LotDTO;
import com.labsynch.labseer.dto.LotsByProjectDTO;
import com.labsynch.labseer.dto.ParentLotCodeDTO;
import com.labsynch.labseer.dto.ReparentLotDTO;
import com.labsynch.labseer.dto.ReparentLotResponseDTO;
import com.labsynch.labseer.exceptions.DupeLotException;
import com.labsynch.labseer.service.LotService;
import com.labsynch.labseer.service.ParentLotService;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping(value = { "/api/v1/parentLot" })
@Controller
public class ApiParentLotController {

	Logger logger = LoggerFactory.getLogger(ApiParentLotController.class);

	@Autowired
	private ParentLotService parentLotService;

	@Autowired
	private LotService lotService;

	@Transactional
	@RequestMapping(value = "/getLotsByParent", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> getParentByAliasName(@RequestParam String parentCorpName,
			@RequestParam(value = "with", required = false) String with) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		try {
			boolean fullObject = false;
			if (with != null) {
				if (with.equalsIgnoreCase("fullobject")) {
					fullObject = true;
				}
			}
			if (fullObject) {
				Collection<Lot> lots = parentLotService.getLotsByParentCorpName(parentCorpName);
				return new ResponseEntity<String>(Lot.toJsonArray(lots), headers, HttpStatus.OK);
			} else {
				Collection<CodeTableDTO> codeTableLots = parentLotService
						.getCodeTableLotsByParentCorpName(parentCorpName);
				return new ResponseEntity<String>(CodeTableDTO.toJsonArray(codeTableLots), headers, HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
	}

	@Transactional
	@RequestMapping(value = "/getLotsByParentAlias", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> getLotCodesByParentAlias(@RequestBody Collection<ParentLotCodeDTO> requestDTOs) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		try {
			Collection<ParentLotCodeDTO> responseDTO = parentLotService.getLotCodesByParentAlias(requestDTOs);
			return new ResponseEntity<String>(ParentLotCodeDTO.toJsonArray(responseDTO), headers, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Caught exception searching for lots by parent alias", e);
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
	}

	@Transactional
	@RequestMapping(value = "/updateLot", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> updateLotAndParent(@RequestBody Collection<ParentLotCodeDTO> requestDTOs) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		try {
			Collection<ParentLotCodeDTO> responseDTO = parentLotService.getLotCodesByParentAlias(requestDTOs);
			return new ResponseEntity<String>(ParentLotCodeDTO.toJsonArray(responseDTO), headers, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Caught exception searching for lots by parent alias", e);
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
	}

	@Transactional
	@RequestMapping(value = "/updateLot/metadata", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> updateLotMetadata(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		try {
			LotDTO lotDTO = LotDTO.fromJsonToLotDTO(json);
			Lot lot = lotService.updateLotMeta(lotDTO);
			return new ResponseEntity<String>(lot.toJsonIncludeAliases(), headers, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Caught exception updating lot metadata", e);
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
	}

	@Transactional
	@RequestMapping(value = "/updateLot/metadata/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> updateLotMetaArray(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		try {
			String results = lotService.updateLotMetaArray(json);
			return new ResponseEntity<String>(results, headers, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Caught error trying to update lot metadata", e);
			return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/reparentLot", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> reparentLot(@RequestBody String json, @RequestParam(value = "dryRun", required = false, defaultValue = "true") Boolean dryRun) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		try {
			ReparentLotDTO lotDTO = ReparentLotDTO.fromJsonToReparentLotDTO(json);
			ReparentLotResponseDTO reparentLotDTO = lotService.reparentLot(lotDTO.getLotCorpName(), lotDTO.getParentCorpName(),
				lotDTO.getModifiedBy(), true, true, dryRun);

			return new ResponseEntity<String>(reparentLotDTO.toJson(), headers, HttpStatus.OK);
		} catch (DupeLotException e) {
			logger.error("Error saving lot with duplicate name", e);
			return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.CONFLICT);
		} catch (Exception e) {
			logger.error("Caught exception updating lot metadata", e);
			return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Transactional
	@RequestMapping(value = "/reparentLot/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> reparentLotArray(@RequestBody String json, @RequestParam(value = "dryRun", required = false, defaultValue = "true") Boolean dryRun) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		int lotCount = 0;
		try {
			Collection<ReparentLotDTO> lotDTOs = ReparentLotDTO.fromJsonArrayToReparentLoes(json);
			for (ReparentLotDTO lotDTO : lotDTOs) {
				lotService.reparentLot(lotDTO.getLotCorpName(), lotDTO.getParentCorpName(), lotDTO.getModifiedBy(), true, true, dryRun);
				lotCount++;
			}
			return new ResponseEntity<String>("number of lots reparented: " + lotCount, headers, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Caught exception updating lot metadata", e);
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
	}

	@Transactional
	@RequestMapping(value = "/getLotsByProjectsList", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> getLotsByProjectsList(@RequestBody List<String> allowedProjects) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		try {
			Collection<LotsByProjectDTO> lots = lotService.getLotsByProjectsList(allowedProjects);
			return new ResponseEntity<String>(LotsByProjectDTO.toJsonArray(lots), headers, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Caught exception updating lot metadata", e);
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
	}

}
