
package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.Date;

import com.labsynch.labseer.domain.ContainerState;
import com.labsynch.labseer.domain.LsTransaction;
import com.labsynch.labseer.domain.UpdateLog;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
public class UpdateLogServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(UpdateLogServiceTest.class);

	@Test
	@Transactional
	public void SimpleTest_1() {
		String json = "[{\"id\":60722},{\"id\":60721}]";
		Collection<ContainerState> containerStates = ContainerState.fromJsonArrayToContainerStates(json);
		logger.info("initial container states: " + ContainerState.toJsonArray(containerStates));
		LsTransaction lst = new LsTransaction();
		lst.setComments("mark states to ignore");
		lst.setRecordedDate(new Date());
		lst.persist();

		for (ContainerState cs : containerStates) {
			UpdateLog uplog = new UpdateLog();
			uplog.setThing(cs.getId());
			uplog.setLsTransaction(lst.getId());
			uplog.setUpdateAction("ignore");
			uplog.setComments("mark states to ignore");
			uplog.setRecordedDate(new Date());
			uplog.persist();
		}

		int results = ContainerState.ignoreStates(lst.getId());
		logger.info("results int is " + results);

		for (ContainerState cs : containerStates) {
			logger.info(ContainerState.findContainerState(cs.getId()).toJsonStub());
		}

	}

}
