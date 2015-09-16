package com.labsynch.labseer.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.CronJob;
import com.labsynch.labseer.dto.AutoLabelDTO;

@Service
@Transactional
public class CronJobServiceImpl implements CronJobService {

	@Autowired
	private AutoLabelService autoLabelService;

	private static final Logger logger = LoggerFactory.getLogger(CronJobServiceImpl.class);


	@Override
	public CronJob saveCronJob(CronJob cronJob) {
		logger.debug("here is the incoming cron job: " + cronJob.toJson());
		
		if (cronJob.getCodeName() == null){
			String thingTypeAndKind = "document_cronJob";
			String labelTypeAndKind = "id_codeName";
			Long numberOfLabels = 1L;
			List<AutoLabelDTO> labels = autoLabelService.getAutoLabels(thingTypeAndKind, labelTypeAndKind, numberOfLabels );
			cronJob.setCodeName(labels.get(0).getAutoLabel());
		}
		
		cronJob.persist();
		
		return cronJob;
	}

	@Override
	public CronJob updateCronJob(CronJob cronJob) {
		logger.debug("here is the incoming cron job: " + cronJob.toJson());
		cronJob.merge();
		return cronJob;
	}

}
