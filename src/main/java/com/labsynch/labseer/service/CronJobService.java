package com.labsynch.labseer.service;

import com.labsynch.labseer.domain.CronJob;

import org.springframework.stereotype.Service;

@Service
public interface CronJobService {

	CronJob saveCronJob(CronJob cronJob);

	CronJob updateCronJob(CronJob cronJob);




}
