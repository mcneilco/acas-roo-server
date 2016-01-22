package com.labsynch.labseer.service;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.CronJob;

@Service
public interface CronJobService {

	CronJob saveCronJob(CronJob cronJob);

	CronJob updateCronJob(CronJob cronJob);




}
