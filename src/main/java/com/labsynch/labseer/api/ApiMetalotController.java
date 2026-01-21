package com.labsynch.labseer.api;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.labsynch.labseer.domain.FileList;
import com.labsynch.labseer.domain.IsoSalt;
import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.domain.Parent;
import com.labsynch.labseer.domain.SaltForm;
import com.labsynch.labseer.dto.CmpdRegBatchCodeDTO;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.ExperimentCodeDTO;
import com.labsynch.labseer.dto.Metalot;
import com.labsynch.labseer.dto.MetalotReturn;
import com.labsynch.labseer.service.ErrorMessage;
import com.labsynch.labseer.service.ExperimentService;
import com.labsynch.labseer.service.LotService;
import com.labsynch.labseer.service.MetalotService;
import com.labsynch.labseer.service.ParentLotService;
import com.labsynch.labseer.service.ParentService;
import com.labsynch.labseer.service.SaltFormService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping(value = "/api/v1/metalots")
@Controller
// @Transactional
public class ApiMetalotController {

	@Autowired
	private MetalotService metalotService;

	@Autowired
	private LotService lotService;

	@Autowired
	private ParentLotService parentLotService;

	
	private static final Logger logger = LoggerFactory.getLogger(ApiMetalotController.class);

	@Transactional
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); // HTTP 1.1
		headers.add("Pragma", "no-cache"); // HTTP 1.0
		headers.setExpires(0); // Expire the cache
		headers.setContentType(MediaType.APPLICATION_JSON);
		Metalot metaLot = new Metalot();

		try {
			Lot lot = Lot.findLot(id);

			if (lot == null) {
				return new ResponseEntity<String>(metaLot.toJson(), headers, HttpStatus.NOT_FOUND);
			} else {
				metaLot.setLot(lot);

				SaltForm saltForm = SaltForm.findSaltForm(metaLot.getLot().getSaltForm().getId());
				List<IsoSalt> isoSalts = IsoSalt.findIsoSaltsBySaltForm(saltForm).getResultList();
				metaLot.getIsosalts().addAll(isoSalts);

				List<FileList> fileLists = FileList.findFileListsByLot(lot).getResultList();
				metaLot.getFileList().addAll(fileLists);

				System.out.println(metaLot.toJson());
				return new ResponseEntity<String>(metaLot.toJson(), headers, HttpStatus.OK);
			}

		} catch (Exception e) {
			logger.error("hit an exception: " + e);

		}

		return new ResponseEntity<String>("ERROR", headers, HttpStatus.OK);

	}

	@RequestMapping(value = {"", "/"}, method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createFromJson(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_HTML);
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); // HTTP 1.1
		headers.add("Pragma", "no-cache"); // HTTP 1.0
		headers.setExpires(0); // Expire the cache

		logger.debug("here is the original json: " + json);

		MetalotReturn mr = metalotService.save(Metalot.fromJsonToMetalot(json));
		boolean hasError = false;
		for (ErrorMessage error : mr.getErrors()) {
			if (error.getLevel().equals("error"))
				hasError = true;
		}
		if (hasError) {
			headers.add("lot save error", "true");
			return new ResponseEntity<String>(ErrorMessage.toJsonArray(mr.getErrors()), headers,
					HttpStatus.BAD_REQUEST);
		} else {
			// MetalotReturnDTO miniLot = new MetalotReturnDTO();
			// miniLot.setId(mr.getMetalot().getLot().getId());
			// miniLot.setCorpName(mr.getMetalot().getLot().getCorpName());
			// miniLot.setBuid(mr.getMetalot().getLot().getBuid());
			// Metalot returnMetaLot = mr.getMetalot();
			return new ResponseEntity<String>(mr.toJson(), headers, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
		Parent parent = Parent.findParent(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/text");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		if (parent == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		parent.remove();
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

	@Transactional
	@RequestMapping(value = "/corpName/{corpName}", method = RequestMethod.GET, headers = "Accept=application/json, application/text, text/html")
	@ResponseBody
	public ResponseEntity<String> jsonFindMetalotsByCorpNameEquals(@PathVariable("corpName") String corpName) {
		logger.debug("search lot corpName = " + corpName);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/text");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); // HTTP 1.1
		headers.add("Pragma", "no-cache"); // HTTP 1.0
		headers.setExpires(0); // Expire the cache
		Metalot metaLot = new Metalot();
		List<Lot> lots = Lot.findLotsByCorpNameEquals(corpName).getResultList();
		System.out.println("Number of lots found = " + lots.size());

		if (lots.size() == 0) {
			System.out.println("Did not find a lot with corpName = " + corpName);
		}

		else {
			Lot lot = lots.get(0);
			metaLot.setLot(lot);

			SaltForm saltForm = SaltForm.findSaltForm(metaLot.getLot().getSaltForm().getId());
			List<IsoSalt> isoSalts = IsoSalt.findIsoSaltsBySaltForm(saltForm).getResultList();
			metaLot.getIsosalts().addAll(isoSalts);

			List<FileList> fileLists = FileList.findFileListsByLot(lot).getResultList();
			metaLot.getFileList().addAll(fileLists);
			System.out.println("From corpName/corpName --- here is the metalot for corpName = " + corpName);
			System.out.println(metaLot.toJson());
		}

		return new ResponseEntity<String>(metaLot.toJson(), headers, HttpStatus.OK);
	}

	/**
	 * Deletes a lot from the database by corp name.  This includes all depedencies such as file lists, experiment data, aliases, orphan iso salts, orphan salt forms and orphan parents.
	 * @param corpName
	 * @return
	 */
	@Transactional
	@RequestMapping(value = "/corpName/{corpName}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteMetalotByCorpName(@PathVariable("corpName") String corpName) {
		logger.debug("delete lot corpName = " + corpName);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); // HTTP 1.1
		headers.add("Pragma", "no-cache"); // HTTP 1.0
		headers.setExpires(0); // Expire the cache
		logger.info("Got delete request for corpName '" + corpName + "'");
		// Get the lot and check for depdencies
		List<Lot> lots = Lot.findLotsByCorpNameEquals(corpName).getResultList();

		logger.debug("Number of lots found = " + lots.size());		

		if(lots.size() == 0) {
			logger.info("Did not find a lot with corpName '" + corpName + "'");
			// Return a 404 error
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		} else {
			logger.debug("Found " + lots.size() + " lots with corpName '" + corpName + "'");

			Lot lot = lots.get(0);
			parentLotService.deleteLot(lot);
			return new ResponseEntity<String>(headers, HttpStatus.OK);

		}
	}

	/**  
	 * Gets dependencies for the lot with the given lot corp name. This includes experimental data (analysis, subject or treatment), containers, protocol values...etc.
	 * @param corpName
	 * @return
	 */
	@RequestMapping(value = "checkDependencies/corpName/{corpName}", method = RequestMethod.GET)
	public ResponseEntity<String> getDependenciesByLotCorpName(@PathVariable("corpName") String corpName) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); // HTTP 1.1
		headers.add("Pragma", "no-cache"); // HTTP 1.0
		headers.setExpires(0); // Expire the cache
		// Get the lot and check for depdencies
		List<Lot> lots = Lot.findLotsByCorpNameEquals(corpName).getResultList();

		if(lots.size() == 0) {
			logger.info("Did not find a lot with corpName '" + corpName + "'");
			// Return a 404 error
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		} else {
			logger.info("Found " + lots.size() + " lots with corpName '" + corpName + "'");
			Lot lot = lots.get(0);
			
			// Check for linked containers
			Set<String> batchCodeSet = new HashSet<String>();
			batchCodeSet.add(lot.getCorpName());
			CmpdRegBatchCodeDTO batchDTO = lotService.checkForDependentData(batchCodeSet);

			// Return json
			String json = batchDTO.toJson();
			return new ResponseEntity<String>(json, headers, HttpStatus.OK);
		}
	}

	@Transactional
	@RequestMapping(params = "corpName", method = RequestMethod.GET, headers = "Accept=application/json, application/text, text/html")
	@ResponseBody
	public ResponseEntity<String> jsonFindMetalotsByCorpNameParam(@RequestParam("corpName") String corpName) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Content-Type", "application/text");
		headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); // HTTP 1.1
		headers.add("Pragma", "no-cache"); // HTTP 1.0
		headers.setExpires(0); // Expire the cache

		Metalot metaLot = new Metalot();
		List<Lot> lots = Lot.findLotsByCorpNameEquals(corpName).getResultList();
		System.out.println("Number of lots found = " + lots.size());

		if (lots.size() == 0) {
			System.out.println("Did not find a lot with corpName = " + corpName);
		}

		else {
			Lot lot = lots.get(0);
			metaLot.setLot(lot);

			SaltForm saltForm = SaltForm.findSaltForm(metaLot.getLot().getSaltForm().getId());
			List<IsoSalt> isoSalts = IsoSalt.findIsoSaltsBySaltForm(saltForm).getResultList();
			metaLot.getIsosalts().addAll(isoSalts);

			List<FileList> fileLists = FileList.findFileListsByLot(lot).getResultList();
			metaLot.getFileList().addAll(fileLists);
			System.out.println("here is the metalot for corpName = " + corpName);
			System.out.println(metaLot.toJson());
		}

		return new ResponseEntity<String>(metaLot.toJson(), headers, HttpStatus.OK);
	}

}
