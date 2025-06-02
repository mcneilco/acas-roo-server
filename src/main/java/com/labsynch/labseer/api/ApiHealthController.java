package com.labsynch.labseer.api;

import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

import com.fasterxml.jackson.annotation.JacksonInject.Value;
import com.labsynch.labseer.domain.ValueType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("api/v1/health")
@Transactional
public class ApiHealthController {

	private static final Logger logger = LoggerFactory.getLogger(ApiHealthController.class);

	public long measureTimeForSingleRun(Boolean logData) {
		long startTime = System.currentTimeMillis();
		List<ValueType> valueTypes = ValueType.findAllValueTypes();
		// Read through all value types to force a database read
		for (ValueType valueType : valueTypes) {
			String valueTypeString = valueType.toString();
			if (logData) {
				logger.info("Value type: " + valueTypeString);
			}
		}
		long endTime = System.currentTimeMillis();
		return endTime - startTime;
	}

	@Transactional
	@RequestMapping(value = "/checkDatabaseSpeed", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<String> checkDatabaseSpeed(@RequestParam(value="testsToRun", required=false) Integer testsToRun, @RequestParam(value="logData", required=false) Boolean logData) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		if(testsToRun == null) {
			testsToRun = 1000;
		}

		if(logData == null) {
			logData = false;
		}
		
		final boolean finalLogData = logData;
		long totalTime = IntStream.range(0, testsToRun)
				.mapToLong(i -> measureTimeForSingleRun(finalLogData))
				.sum();

		double averageTime = (double) totalTime / testsToRun;
		logger.info("Average time per run: " + averageTime + "ms");

		// Return json response with {"averageMsTime": averageTime, "totalTime": totalTime, "testsToRun": testsToRun, "logData": logData}
		return new ResponseEntity<String>("{\"averageMsTime\": " + averageTime + ", \"totalTime\": " + totalTime + ", \"testsToRun\": " + testsToRun + ", \"logData\": " + finalLogData + "}", headers, HttpStatus.OK);
	}

    @RequestMapping(value = "/live", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> liveness() {
        return ResponseEntity.ok("{\"status\":\"UP\"}");
    }

    @RequestMapping(value = "/ready", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> readiness() {
        try {
            // Quick DB check: try to fetch one ValueType
            List<ValueType> valueTypes = ValueType.findAllValueTypes();
            boolean dbOk = valueTypes != null;
            return dbOk
                ? ResponseEntity.ok("{\"status\":\"UP\"}")
                : ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("{\"status\":\"DOWN\"}");
        } catch (Exception e) {
            logger.error("Readiness check failed", e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("{\"status\":\"DOWN\"}");
        }
    }

}
