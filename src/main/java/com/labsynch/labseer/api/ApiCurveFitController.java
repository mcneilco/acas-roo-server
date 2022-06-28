package com.labsynch.labseer.api;

import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.domain.ProtocolValue;
import com.labsynch.labseer.dto.CurveFitDTO;
import com.labsynch.labseer.dto.FlagWellDTO;
import com.labsynch.labseer.dto.KiCurveFitDTO;
import com.labsynch.labseer.dto.RawCurveDataDTO;
import com.labsynch.labseer.dto.TgDataDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("api/v1/curvefit")
@Transactional
public class ApiCurveFitController {

	private static final Logger logger = LoggerFactory.getLogger(ApiCurveFitController.class);

	// no longer needed - preparing to deprecate
	@Transactional
	@RequestMapping(value = "/fitdata", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> getFitDataByCurveId(@RequestBody List<String> curveIds,
			@RequestParam(value = "format", required = false) String format) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		try {
			// This route will not pull back the correct data if a mix of rendering hints is
			// expected. It will handle mixed existence of rendering hints.
			String renderingHint = CurveFitDTO.findFirstRenderingHint(curveIds);
			if (renderingHint.equalsIgnoreCase("4 parameter D-R")) {
				Collection<CurveFitDTO> filledCurveFitDTOs = CurveFitDTO.getFitData(curveIds);
				if (format != null && (format.equalsIgnoreCase("csv") || format.equalsIgnoreCase("tsv"))) {
					String outFileString = CurveFitDTO.getCsvList(filledCurveFitDTOs, format);
					outFileString = outFileString.replaceAll("\"\"", "\\\"");
					// this very confusing regex replaces "" with \" to reverse an unintended
					// conversion by supercsv.
					return new ResponseEntity<String>(outFileString, headers, HttpStatus.OK);
				} else {
					return new ResponseEntity<String>(CurveFitDTO.toJsonArray(filledCurveFitDTOs), headers,
							HttpStatus.OK);
				}
			} else if (renderingHint.equalsIgnoreCase("Ki Fit")) {
				Collection<KiCurveFitDTO> filledKiCurveFitDTOs = KiCurveFitDTO.getFitData(curveIds);
				if (format != null && (format.equalsIgnoreCase("csv") || format.equalsIgnoreCase("tsv"))) {
					String outFileString = KiCurveFitDTO.getCsvList(filledKiCurveFitDTOs, format);
					outFileString = outFileString.replaceAll("\"\"", "\\\"");
					// this very confusing regex replaces "" with \" to reverse an unintended
					// conversion by supercsv.
					return new ResponseEntity<String>(outFileString, headers, HttpStatus.OK);
				} else {
					return new ResponseEntity<String>(KiCurveFitDTO.toJsonArray(filledKiCurveFitDTOs), headers,
							HttpStatus.OK);
				}
			} else {
				String errorMessage = "ERROR: Rendering hint: " + renderingHint + " not recognized.";
				logger.error(errorMessage);
				return new ResponseEntity<String>(errorMessage, headers, HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (Exception e) {
			// String error = e.getMessage() + e.getStackTrace();
			logger.error("Caught error: " + e.toString());
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Transactional
	@RequestMapping(value = "/rawdata", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> getRawCurveDataByCurveId(@RequestBody List<String> curveIds,
			@RequestParam(value = "format", required = false) String format,
			@RequestParam(value = "response", required = false) String responseName) {
		try {
			// This route currently assumes that all the curveIds specified have the same
			// rendering hint. It will not pull back the correct data if a mix of rendering
			// hints is expected.
			Collection<RawCurveDataDTO> filledRawCurveDataDTOs = RawCurveDataDTO.getRawCurveData(curveIds,
					responseName);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "application/json; charset=utf-8");
			if (format != null && (format.equalsIgnoreCase("csv") || format.equalsIgnoreCase("tsv"))) {
				String outFileString = RawCurveDataDTO.getCsvList(filledRawCurveDataDTOs, format);
				outFileString = outFileString.replaceAll("\"\"", "\\\"");
				return new ResponseEntity<String>(outFileString, headers, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>(RawCurveDataDTO.toJsonArray(filledRawCurveDataDTOs), headers,
						HttpStatus.OK);
			}
		} catch (Exception e) {
			// String error = e.getMessage() + e.getStackTrace();
			logger.error("Caught error: " + e.toString());
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Transactional
	@RequestMapping(value = "/rawdataagonist", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> getRawCurveDataAgonistByCurveId(@RequestBody List<String> curveIds,
			@RequestParam(value = "format", required = false) String format,
			@RequestParam(value = "response", required = false) String responseName) {
		try {
			// This route currently assumes that all the curveIds specified have the same
			// rendering hint. It will not pull back the correct data if a mix of rendering
			// hints is expected.
			Collection<RawCurveDataDTO> filledRawCurveDataDTOs = RawCurveDataDTO.getRawCurveDataAgonist(curveIds,
					responseName);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "application/json; charset=utf-8");
			if (format != null && (format.equalsIgnoreCase("csv") || format.equalsIgnoreCase("tsv"))) {
				String outFileString = RawCurveDataDTO.getCsvList(filledRawCurveDataDTOs, format);
				outFileString = outFileString.replaceAll("\"\"", "\\\"");
				return new ResponseEntity<String>(outFileString, headers, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>(RawCurveDataDTO.toJsonArray(filledRawCurveDataDTOs), headers,
						HttpStatus.OK);
			}
		} catch (Exception e) {
			// String error = e.getMessage() + e.getStackTrace();
			logger.error("Caught error: " + e.toString());
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Transactional
	@RequestMapping(value = "/tgdata", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> getTgDataByCurveId(@RequestBody List<String> curveIds,
			@RequestParam(value = "format", required = false) String format) {
		try {
			Collection<TgDataDTO> filledTgDataDTOs = TgDataDTO.getTgData(curveIds);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "application/json; charset=utf-8");
			if (format != null && (format.equalsIgnoreCase("csv") || format.equalsIgnoreCase("tsv"))) {
				String outFileString = TgDataDTO.getCsvList(filledTgDataDTOs, format);
				outFileString = outFileString.replaceAll("\"\"", "\\\"");
				return new ResponseEntity<String>(outFileString, headers, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>(TgDataDTO.toJsonArray(filledTgDataDTOs), headers, HttpStatus.OK);
			}
		} catch (Exception e) {
			// String error = e.getMessage() + e.getStackTrace();
			logger.error("Caught error: " + e.toString());
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// no longer needed - preparing to deprecate
	@Transactional
	@RequestMapping(value = "/fitdata", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<String> getFitDataByExperimentIdOrCodeName(
			@RequestParam(value = "experiment") String experimentIdOrCodeName,
			@RequestParam(value = "modelFitType", required = true) String renderingHint,
			@RequestParam(value = "format", required = false) String format) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		try {
			List<String> curveIds = (List<String>) CurveFitDTO.findAllCurveIdsByExperiment(experimentIdOrCodeName);
			if (renderingHint.equalsIgnoreCase("4 parameter D-R")) {
				Collection<CurveFitDTO> filledCurveFitDTOs = CurveFitDTO.getFitData(curveIds);
				if (format != null && (format.equalsIgnoreCase("csv") || format.equalsIgnoreCase("tsv"))) {
					String outFileString = CurveFitDTO.getCsvList(filledCurveFitDTOs, format);
					outFileString = outFileString.replaceAll("\"\"", "\\\"");
					// this very confusing regex replaces "" with \" to reverse an unintended
					// conversion by supercsv.
					return new ResponseEntity<String>(outFileString, headers, HttpStatus.OK);
				} else {
					return new ResponseEntity<String>(CurveFitDTO.toJsonArray(filledCurveFitDTOs), headers,
							HttpStatus.OK);
				}
			} else if (renderingHint.equalsIgnoreCase("Ki Fit")) {
				Collection<KiCurveFitDTO> filledKiCurveFitDTOs = KiCurveFitDTO.getFitData(curveIds);
				if (format != null && (format.equalsIgnoreCase("csv") || format.equalsIgnoreCase("tsv"))) {
					String outFileString = KiCurveFitDTO.getCsvList(filledKiCurveFitDTOs, format);
					outFileString = outFileString.replaceAll("\"\"", "\\\"");
					// this very confusing regex replaces "" with \" to reverse an unintended
					// conversion by supercsv.
					return new ResponseEntity<String>(outFileString, headers, HttpStatus.OK);
				} else {
					return new ResponseEntity<String>(KiCurveFitDTO.toJsonArray(filledKiCurveFitDTOs), headers,
							HttpStatus.OK);
				}
			} else {
				String errorMessage = "ERROR: Rendering hint: " + renderingHint + " not recognized.";
				logger.error(errorMessage);
				return new ResponseEntity<String>(errorMessage, headers, HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (Exception e) {
			// String error = e.getMessage() + e.getStackTrace();
			logger.error("Caught error: " + e.toString());
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Transactional
	@RequestMapping(value = "/rawdataagonist", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<String> getRawDataAgonistByExperimentIdOrCodeName(
			@RequestParam(value = "experiment") String experimentIdOrCodeName,
			@RequestParam(value = "format", required = false) String format,
			@RequestParam(value = "response", required = false) String responseName) {
		try {
			Collection<RawCurveDataDTO> rawCurveDataDTOs = RawCurveDataDTO
					.getRawAgonistCurveDataByExperiment(experimentIdOrCodeName, responseName);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "application/json; charset=utf-8");
			if (format != null && (format.equalsIgnoreCase("csv") || format.equalsIgnoreCase("tsv"))) {
				String outFileString = RawCurveDataDTO.getCsvList(rawCurveDataDTOs, format);
				outFileString = outFileString.replaceAll("\"\"", "\\\"");
				return new ResponseEntity<String>(outFileString, headers, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>(RawCurveDataDTO.toJsonArray(rawCurveDataDTOs), headers,
						HttpStatus.OK);
			}
		} catch (Exception e) {
			// String error = e.getMessage() + e.getStackTrace();
			logger.error("Caught error: " + e.toString());
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Transactional
	@RequestMapping(value = "/rawdata", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<String> getRawDataByExperimentIdOrCodeName(
			@RequestParam(value = "experiment") String experimentIdOrCodeName,
			@RequestParam(value = "format", required = false) String format,
			@RequestParam(value = "response", required = false) String responseName) {
		try {
			Collection<RawCurveDataDTO> rawCurveDataDTOs = RawCurveDataDTO
					.getRawCurveDataByExperiment(experimentIdOrCodeName, responseName);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "application/json; charset=utf-8");
			if (format != null && (format.equalsIgnoreCase("csv") || format.equalsIgnoreCase("tsv"))) {
				String outFileString = RawCurveDataDTO.getCsvList(rawCurveDataDTOs, format);
				outFileString = outFileString.replaceAll("\"\"", "\\\"");
				return new ResponseEntity<String>(outFileString, headers, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>(RawCurveDataDTO.toJsonArray(rawCurveDataDTOs), headers,
						HttpStatus.OK);
			}
		} catch (Exception e) {
			// String error = e.getMessage() + e.getStackTrace();
			logger.error("Caught error: " + e.toString());
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Transactional
	@RequestMapping(value = "/tgdata", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<String> getTgDataByExperimentIdOrCodeName(
			@RequestParam(value = "experiment") String experimentIdOrCodeName,
			@RequestParam(value = "format", required = false) String format) {
		try {
			Collection<TgDataDTO> tgDataDTOs = TgDataDTO.getTgDataByExperiment(experimentIdOrCodeName);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "application/json; charset=utf-8");
			if (format != null && (format.equalsIgnoreCase("csv") || format.equalsIgnoreCase("tsv"))) {
				String outFileString = TgDataDTO.getCsvList(tgDataDTOs, format);
				outFileString = outFileString.replaceAll("\"\"", "\\\"");
				return new ResponseEntity<String>(outFileString, headers, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>(TgDataDTO.toJsonArray(tgDataDTOs), headers, HttpStatus.OK);
			}
		} catch (Exception e) {
			// String error = e.getMessage() + e.getStackTrace();
			logger.error("Caught error: " + e.toString());
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// no longer needed - preparing to deprecate
	@Transactional
	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> updateFitDataFromJson(@RequestBody List<CurveFitDTO> curveFitDTOs) {
		try {
			CurveFitDTO.updateFitData(curveFitDTOs);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "application/json; charset=utf-8");
			return new ResponseEntity<String>(headers, HttpStatus.OK);
		} catch (Exception e) {
			// String error = e.getMessage() + e.getStackTrace();
			logger.error("Caught error: " + e.toString());
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// no longer needed - preparing to deprecate
	@Transactional
	@RequestMapping(value = "/ki", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> updateKiFitDataFromJson(@RequestBody List<KiCurveFitDTO> curveFitDTOs) {
		try {
			KiCurveFitDTO.updateFitData(curveFitDTOs);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "application/json; charset=utf-8");
			return new ResponseEntity<String>(headers, HttpStatus.OK);
		} catch (Exception e) {
			// String error = e.getMessage() + e.getStackTrace();
			logger.error("Caught error: " + e.toString());
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Transactional
	@RequestMapping(value = "/flagWells", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> updateWellFlags(@RequestBody List<FlagWellDTO> flagWellDTOs) {
		try {
			FlagWellDTO.updateWellFlags(flagWellDTOs);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "application/json; charset=utf-8");
			return new ResponseEntity<String>(headers, HttpStatus.OK);
		} catch (Exception e) {
			// String error = e.getMessage() + e.getStackTrace();
			logger.error("Caught error: " + e.toString());
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Transactional
	@RequestMapping(value = "/displayminmax", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> getProtocolCurveDisplayMinAndCurveDisplayMaxByCurveId(@RequestBody String curveId,
			@RequestParam(value = "format", required = false) String format) {
		try {
			// logger.debug("From the ApiCurveFitController displayminmax route");
			// logger.debug("curveId: "+ curveId);
			Collection<ProtocolValue> protocolValues = CurveFitDTO.findDisplayMinMaxByCurveId(curveId);
			// logger.debug("Found: "+ ProtocolValue.toJsonArray(protocolValues));
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "application/json; charset=utf-8");
			return new ResponseEntity<String>(ProtocolValue.toJsonArray(protocolValues), headers, HttpStatus.OK);
		} catch (Exception e) {
			// String error = e.getMessage() + e.getStackTrace();
			logger.error("Caught error: " + e.toString());
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
