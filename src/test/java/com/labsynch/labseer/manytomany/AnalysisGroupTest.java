package com.labsynch.labseer.manytomany;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ExperimentLabel;
import com.labsynch.labseer.domain.Protocol;
import com.labsynch.labseer.domain.TreatmentGroup;
import com.labsynch.labseer.exceptions.UniqueExperimentNameException;
import com.labsynch.labseer.service.AnalysisGroupService;
import com.labsynch.labseer.service.ExperimentService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class AnalysisGroupTest {
	
	private static final Logger logger = LoggerFactory.getLogger(AnalysisGroupTest.class);
	
	@Autowired
	private AnalysisGroupService analysisGroupService;

	@Autowired
	private ExperimentService experimentService;
	
	

	@Transactional
	@Test
	public void Analysis_Group_Many_To_Many_Test() {
		AnalysisGroup analysisGroup = AnalysisGroup.findAnalysisGroup(235L);
		logger.debug(analysisGroup.toJson());
		
		int theSize = analysisGroup.getTreatmentGroups().size();
		logger.debug(String.valueOf(theSize));
		System.out.println("" + theSize);
		
		Assert.assertEquals(1, theSize);
	}

	
	@Transactional
	@Test
	public void saveWithExistingTreatmentGroups() throws UniqueExperimentNameException {
		
		Protocol protocol = Protocol.findProtocolEntries(0, 1).get(0);
		
		Experiment experiment = new Experiment();
		experiment.setLsType("default");
		experiment.setLsKind("default");
		experiment.setRecordedBy("test user");
		experiment.setRecordedDate(new Date());
		experiment.setProtocol(protocol);
		Experiment savedExperiment = experimentService.saveLsExperiment(experiment);
		
		AnalysisGroup analysisGroup = new AnalysisGroup();
		analysisGroup.setLsType("data");
		analysisGroup.setLsKind("results");
		analysisGroup.setRecordedBy("test user");
		analysisGroup.setRecordedDate(new Date());
		
		Set<Experiment> experiments = new HashSet<Experiment>();
		experiments.add(Experiment.findExperiment(savedExperiment.getId()));
		analysisGroup.setExperiments(experiments);
		
		Set<TreatmentGroup> treatmentGroups = new HashSet<TreatmentGroup>();
		treatmentGroups.add(TreatmentGroup.findTreatmentGroup(268L));
		treatmentGroups.add(TreatmentGroup.findTreatmentGroup(276L));
		analysisGroup.setTreatmentGroups(treatmentGroups);
		
		AnalysisGroup output = analysisGroupService.saveLsAnalysisGroup(analysisGroup);
		output.flush();
		//Assert.assertEquals("AG-00000238", output.getCodeName());
		Assert.assertEquals(2, analysisGroup.getTreatmentGroups().size());

		logger.info(AnalysisGroup.findAnalysisGroup(output.getId()).toPrettyFullJson());

		
	}
}
