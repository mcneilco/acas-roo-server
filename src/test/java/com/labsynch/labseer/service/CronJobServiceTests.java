

package com.labsynch.labseer.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.CronJob;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class CronJobServiceTests {

	private static final Logger logger = LoggerFactory.getLogger(CronJobServiceTests.class);
	
	@Autowired
	private CronJobService cronJobService;
	
	@Test
	@Transactional
	@Rollback(value=true)
	public void saveCronJob(){
		String json = "{\"schedule\":\"00 * * * * *\",\"scriptType\":\"R\",\"scriptFile\":\"src/r/Test/test.R\",\"functionName\":\"testInventoryOperationETL\",\"scriptJSONData\":\"{}\",\"active\":true,\"ignored\":false,\"runUser\":\"acas\",\"lastDuration\":null,\"lastResultJSON\":null,\"lastStartTime\":null,\"numberOfExecutions\":0,\"version\":0}";
		CronJob cronJob = CronJob.fromJsonToCronJob(json);
		CronJob savedCronJob = cronJobService.saveCronJob(cronJob);
	}
	
	
}
