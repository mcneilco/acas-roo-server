package com.labsynch.labseer.manytomany;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.TypedQuery;

import junit.framework.Assert;
import oracle.net.aso.e;

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
import com.labsynch.labseer.domain.AnalysisGroupState;
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
	
	private Protocol makeTestingProtocol() {
		//initialize some entries to fill in the fields
		Date now = new Date();
		//create protocol
		Protocol protocol = new Protocol();
		protocol.setRecordedBy("test user");
		protocol.setLsKind("default");
		protocol.setLsType("default");
		protocol.persist();
		protocol.flush();
		return protocol;
	}
	
	private Experiment makeTestingExperiment() {
		Protocol protocol = makeTestingProtocol();
		Experiment experiment = new Experiment();
        experiment.setCodeName("EXPT-12345678");
        experiment.setIgnored(false);
        experiment.setLsKind(protocol.getLsKind());
        experiment.setLsType(protocol.getLsType());
        experiment.setRecordedBy(protocol.getRecordedBy());
        experiment.setRecordedDate(protocol.getRecordedDate());
        experiment.setProtocol(protocol);
        experiment.persist();
        experiment.flush();
        return experiment;
	}
	
	private AnalysisGroup makeTestingAnalysisGroup() {
		Experiment experiment = makeTestingExperiment();
		AnalysisGroup analysisgroup= new AnalysisGroup();
        analysisgroup.setCodeName("AG-12345678");
        analysisgroup.setIgnored(false);
        analysisgroup.setLsKind(experiment.getLsKind());
        analysisgroup.setLsType(experiment.getLsType());
        analysisgroup.setRecordedBy(experiment.getRecordedBy());
        Set<Experiment> experimentSet = new HashSet<Experiment>();
        experimentSet.add(experiment);
        analysisgroup.setExperiments(experimentSet);
        analysisgroup.persist();
        analysisgroup.flush();
        return analysisgroup;
	}

	@Transactional
	//@Test
	public void Analysis_Group_Many_To_Many_Test() {
		AnalysisGroup analysisGroup = AnalysisGroup.findAnalysisGroup(235L);
		logger.debug(analysisGroup.toJson());
		
		int theSize = analysisGroup.getTreatmentGroups().size();
		logger.debug(String.valueOf(theSize));
		System.out.println("" + theSize);
		
		Assert.assertEquals(1, theSize);
	}

	
	@Transactional
	//@Test
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
	
	//TODO:Implement test method stubs
	@Transactional
	@Test
	public void findAnalysisGroupsByExperimentIdAndIgnoredTest() {
		//generate an analysisgroup for testing
        AnalysisGroup analysisgroup = makeTestingAnalysisGroup();
        Long experimentId = analysisgroup.getExperiments().iterator().next().getId();
        boolean ignored = analysisgroup.isIgnored();
        //go get the analysisgroup by its ExperimentId and Ignored
		AnalysisGroup check = AnalysisGroup.findAnalysisGroupsByExperimentIdAndIgnored(experimentId, ignored).getSingleResult();
		assert(analysisgroup.toJson() == check.toJson());
	}
	
	@Transactional
	@Test
	public void findAnalysisGroupsByExperimentsAndIgnoredNotTest() {
		AnalysisGroup analysisgroup = makeTestingAnalysisGroup();
		Set<Experiment> experiments = analysisgroup.getExperiments();
		boolean ignored = analysisgroup.isIgnored();
		AnalysisGroup check = AnalysisGroup.findAnalysisGroupsByExperimentsAndIgnoredNot(experiments, !ignored).getSingleResult();
		assert(analysisgroup.toJson() == check.toJson());
	}
	
	@Transactional
	@Test
	public void findAnalysisGroupsByCodeNameEqualsTest() {
		AnalysisGroup analysisgroup = makeTestingAnalysisGroup();
		String codename = analysisgroup.getCodeName();
		AnalysisGroup check = AnalysisGroup.findAnalysisGroupsByCodeNameEquals(codename).getSingleResult();
		assert(check.toJson() == analysisgroup.toJson());
	}
	
	@Transactional
	@Test
	public void removeByExperimentIdTest() {
		AnalysisGroup analysisgroup = makeTestingAnalysisGroup();
		Long id = analysisgroup.getId();
		Long experimentId = analysisgroup.getExperiments().iterator().next().getId();
		AnalysisGroup checkbefore = AnalysisGroup.findAnalysisGroup(id);
		assert(analysisgroup.toJson() == checkbefore.toJson());
		AnalysisGroup.removeByExperimentID(experimentId);
		AnalysisGroup checkafter = AnalysisGroup.findAnalysisGroup(id);
		assert(analysisgroup.toJson() != checkafter.toJson());
	}

	@Transactional
	@Test
	public void deleteByExperimentIDTest1() {
		Long experimentId = 2180L;
		AnalysisGroup.deleteByExperimentID(experimentId);
		Assert.assertNull(AnalysisGroup.findAnalysisGroup(2181L));
	}
	
	
	@Transactional
	@Test
	public void deleteByExperimentIDTest() {
		AnalysisGroup analysisgroup = makeTestingAnalysisGroup();
		Long id = analysisgroup.getId();
		Long experimentId = analysisgroup.getExperiments().iterator().next().getId();
		logger.info("here is the experiment ID: " + experimentId);
		AnalysisGroup checkbefore = AnalysisGroup.findAnalysisGroup(id);
		assert(analysisgroup.toJson() == checkbefore.toJson());
		logger.info(analysisgroup.toPrettyFullJson());
		AnalysisGroup.deleteByExperimentID(experimentId);
		AnalysisGroup checkafter = AnalysisGroup.findAnalysisGroup(id);
		assert(analysisgroup.toJson() != checkafter.toJson());
	}
	
	@Transactional
	//@Test
	public void fromJsonToAnalysisGroupTest() {
		String json;
	}
	
	@Transactional
	@Test
    public void removeTest() {
		AnalysisGroup analysisgroup = makeTestingAnalysisGroup();
        //go get the analysisgroup by its ID
        AnalysisGroup check = AnalysisGroup.findAnalysisGroup(analysisgroup.getId());
        //logger.info(check.toPrettyFullJson());
        //check that the original and the fetched analysisgroups are the same
        assert(check.toJson() == analysisgroup.toJson());
        //remove the original
        analysisgroup.remove();
        //make sure the original doesn't matched the one fetched before the remove
        assert(check.toJson() != analysisgroup.toJson());
        
    }
}
