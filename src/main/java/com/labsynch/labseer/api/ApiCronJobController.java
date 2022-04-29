package com.labsynch.labseer.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.domain.CronJob;
import com.labsynch.labseer.service.CronJobService;
import com.labsynch.labseer.utils.SimpleUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Transactional
@RequestMapping("api/v1/cronjobs")
@Controller


public class ApiCronJobController {

	private static final Logger logger = LoggerFactory.getLogger(ApiCronJobController.class);

	@Autowired
	private CronJobService cronJobService;


	@Transactional
	@RequestMapping(method = RequestMethod.GET, value = "/{idOrCodeName}", headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> showJson(@PathVariable("idOrCodeName") String idOrCodeName) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		
		CronJob cronJob = null;
		if (SimpleUtil.isNumeric(idOrCodeName)){
			cronJob = CronJob.findCronJob(Long.valueOf(idOrCodeName));
		} else {
			try{
				cronJob = CronJob.findCronJobsByCodeNameEquals(idOrCodeName).getSingleResult();
			}catch (Exception e){
				cronJob = null;
			}
		}

		if (cronJob == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(cronJob.toJson(), headers, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> listJson() {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		List<CronJob> cronJobs = CronJob.findAllCronJobs();
		return new ResponseEntity<String>(CronJob.toJsonArray(cronJobs), headers, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createFromJson(@RequestBody CronJob cronJob) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		try{
			CronJob savedCronJob = cronJobService.saveCronJob(cronJob);
			return new ResponseEntity<String>(savedCronJob.toJson(), headers, HttpStatus.CREATED);
		}catch (Exception e){
			return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createFromJsonArray(
			@RequestBody List<CronJob> cronJobs) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		List<CronJob> savedCronJobs = new ArrayList<CronJob>();
		try{
			for (CronJob cronJob : cronJobs){
				CronJob savedCronJob = cronJobService.saveCronJob(cronJob);
				savedCronJobs.add(savedCronJob);
			}
			return new ResponseEntity<String>(CronJob.toJsonArray(savedCronJobs), headers, HttpStatus.CREATED);
		}catch (Exception e){
			return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
	public ResponseEntity<String> updateFromJson(@RequestBody CronJob cronJob) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		try{
			CronJob updatedCronJob = cronJobService.updateCronJob(cronJob);
			return new ResponseEntity<String>(updatedCronJob.toJson(), headers, HttpStatus.OK);
		}catch (Exception e){
			return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
	public ResponseEntity<String> updateFromJsonArray(@RequestBody List<CronJob> cronJobs) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		Collection<CronJob> updatedCronJobs = new ArrayList<CronJob>();
		try{
			for (CronJob cronJob : cronJobs){
				CronJob updatedCronJob = cronJobService.updateCronJob(cronJob);
				updatedCronJobs.add(updatedCronJob);
			}
			return new ResponseEntity<String>(CronJob.toJsonArray(updatedCronJobs), headers, HttpStatus.OK);
		}catch (Exception e){
			return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


}


