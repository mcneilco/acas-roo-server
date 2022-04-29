package com.labsynch.labseer.manytomany;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.Container;
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ItxContainerContainer;
import com.labsynch.labseer.domain.ItxExperimentExperiment;
import com.labsynch.labseer.domain.ItxProtocolProtocol;
import com.labsynch.labseer.domain.ItxSubjectContainer;
import com.labsynch.labseer.domain.Protocol;
import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.domain.TreatmentGroup;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import junit.framework.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/applicationContext.xml",
		"classpath:/META-INF/spring/applicationContext-security.xml" })
@Configurable
public class ItxTest {

	private static final Logger logger = LoggerFactory.getLogger(ItxTest.class);

	public HashMap<String, Long> makeTestStack() {
		// each of these methods creates the full stack from protocol to subject.
		Date now = new Date();
		// create 2 Protocols
		Protocol protocol = new Protocol();
		protocol.setRecordedBy("test user");
		protocol.setRecordedDate(now);
		protocol.setLsKind("default");
		protocol.setLsType("default");
		protocol.setLsTransaction(98765L);
		protocol.persist();

		Protocol protocol2 = new Protocol();
		protocol2.setRecordedBy("test user 2");
		protocol2.setRecordedDate(now);
		protocol2.setLsKind("default");
		protocol2.setLsType("default");
		protocol2.setLsTransaction(98765L);
		protocol2.persist();

		// create 3 Experiments
		Experiment e1 = new Experiment();
		e1.setCodeName("T-EXPT-1111111");
		Experiment e2 = new Experiment();
		e2.setCodeName("T-EXPT-22222222");
		Experiment e3 = new Experiment();
		e3.setCodeName("T-EXPT-33333333");
		Set<Experiment> allExperiments = new HashSet<Experiment>();
		allExperiments.add(e1);
		allExperiments.add(e2);
		allExperiments.add(e3);
		for (Experiment experiment : allExperiments) {
			experiment.setIgnored(false);
			experiment.setLsKind(protocol.getLsKind());
			experiment.setLsType(protocol.getLsType());
			experiment.setRecordedBy(protocol.getRecordedBy());
			experiment.setRecordedDate(protocol.getRecordedDate());
			experiment.setLsTransaction(protocol.getLsTransaction());
			experiment.setProtocol(protocol);
		}

		// create 3 AnalysisGroups
		AnalysisGroup a1 = new AnalysisGroup();
		a1.setCodeName("T-AG-11111111");
		AnalysisGroup a2 = new AnalysisGroup();
		a2.setCodeName("T-AG-22222222");
		AnalysisGroup a3 = new AnalysisGroup();
		a3.setCodeName("T-AG-33333333");
		Set<AnalysisGroup> allAnalysisGroups = new HashSet<AnalysisGroup>();
		allAnalysisGroups.add(a1);
		allAnalysisGroups.add(a2);
		allAnalysisGroups.add(a3);
		for (AnalysisGroup analysisGroup : allAnalysisGroups) {
			analysisGroup.setIgnored(e1.isIgnored());
			analysisGroup.setLsKind(e1.getLsKind());
			analysisGroup.setLsType(e1.getLsType());
			analysisGroup.setRecordedBy(e1.getRecordedBy());
			analysisGroup.setRecordedDate(e1.getRecordedDate());
			analysisGroup.setLsTransaction(e1.getLsTransaction());
		}

		// create 3 TreatmentGroups
		TreatmentGroup t1 = new TreatmentGroup();
		t1.setCodeName("T-TG-11111111");
		TreatmentGroup t2 = new TreatmentGroup();
		t2.setCodeName("T-TG-22222222");
		TreatmentGroup t3 = new TreatmentGroup();
		t3.setCodeName("T-TG-33333333");
		Set<TreatmentGroup> allTreatmentGroups = new HashSet<TreatmentGroup>();
		allTreatmentGroups.add(t1);
		allTreatmentGroups.add(t2);
		allTreatmentGroups.add(t3);
		for (TreatmentGroup treatmentGroup : allTreatmentGroups) {
			treatmentGroup.setIgnored(a1.isIgnored());
			treatmentGroup.setLsKind(a1.getLsKind());
			treatmentGroup.setLsType(a1.getLsType());
			treatmentGroup.setRecordedBy(a1.getRecordedBy());
			treatmentGroup.setRecordedDate(a1.getRecordedDate());
			treatmentGroup.setLsTransaction(a1.getLsTransaction());
		}

		// create 3 Subjects
		Subject s1 = new Subject();
		s1.setCodeName("T-SUBJ-11111111");
		Subject s2 = new Subject();
		s2.setCodeName("T-SUBJ-22222222");
		Subject s3 = new Subject();
		s3.setCodeName("T-SUBJ-33333333");
		Set<Subject> allSubjects = new HashSet<Subject>();
		allSubjects.add(s1);
		allSubjects.add(s2);
		allSubjects.add(s3);
		for (Subject subject : allSubjects) {
			subject.setIgnored(t1.isIgnored());
			subject.setLsKind(t1.getLsKind());
			subject.setLsType(t1.getLsType());
			subject.setRecordedBy(t1.getRecordedBy());
			subject.setRecordedDate(t1.getRecordedDate());
			subject.setLsTransaction(t1.getLsTransaction());
		}

		// Create 3 Containers
		Container c1 = new Container();
		c1.setCodeName("T-CONT-11111111");
		Container c2 = new Container();
		c2.setCodeName("T-CONT-22222222");
		Container c3 = new Container();
		c3.setCodeName("T-CONT-33333333");
		Set<Container> allContainers = new HashSet<Container>();
		allContainers.add(c1);
		allContainers.add(c2);
		allContainers.add(c3);
		for (Container container : allContainers) {
			container.setIgnored(s1.isIgnored());
			container.setLsKind(s1.getLsKind());
			container.setLsType(s1.getLsType());
			container.setRecordedBy(s1.getRecordedBy());
			container.setRecordedDate(s1.getRecordedDate());
			container.setLsTransaction(s1.getLsTransaction());
		}

		// Create Itx (interactions) objects
		// ItxExperimentExperiment
		ItxExperimentExperiment itxe1e2 = new ItxExperimentExperiment();
		itxe1e2.setFirstExperiment(e1);
		itxe1e2.setSecondExperiment(e2);
		ItxExperimentExperiment itxe2e3 = new ItxExperimentExperiment();
		itxe2e3.setFirstExperiment(e2);
		itxe2e3.setSecondExperiment(e3);
		ItxExperimentExperiment itxe1e3 = new ItxExperimentExperiment();
		itxe1e3.setFirstExperiment(e1);
		itxe1e3.setSecondExperiment(e3);
		Set<ItxExperimentExperiment> allItxExperimentExperiments = new HashSet<ItxExperimentExperiment>();
		allItxExperimentExperiments.add(itxe1e2);
		allItxExperimentExperiments.add(itxe2e3);
		allItxExperimentExperiments.add(itxe1e3);
		for (ItxExperimentExperiment itxExperimentExperiment : allItxExperimentExperiments) {
			itxExperimentExperiment.setLsKind(e1.getLsKind());
			itxExperimentExperiment.setLsType(e1.getLsType());
			itxExperimentExperiment.setRecordedBy(e1.getRecordedBy());
			itxExperimentExperiment.setRecordedDate(e1.getRecordedDate());
		}

		// ItxProtocolProtocol
		ItxProtocolProtocol itxp1p2 = new ItxProtocolProtocol();
		itxp1p2.setFirstProtocol(protocol);
		itxp1p2.setSecondProtocol(protocol2);
		itxp1p2.setLsType(protocol.getLsType());
		itxp1p2.setLsKind(protocol.getLsKind());
		itxp1p2.setRecordedBy(protocol.getRecordedBy());
		itxp1p2.setRecordedDate(protocol.getRecordedDate());

		// ItxContainerContainer
		ItxContainerContainer itxc1c2 = new ItxContainerContainer();
		itxc1c2.setFirstContainer(c1);
		itxc1c2.setSecondContainer(c2);
		ItxContainerContainer itxc2c3 = new ItxContainerContainer();
		itxc2c3.setFirstContainer(c2);
		itxc2c3.setSecondContainer(c3);
		ItxContainerContainer itxc1c3 = new ItxContainerContainer();
		itxc1c3.setFirstContainer(c1);
		itxc1c3.setSecondContainer(c3);
		Set<ItxContainerContainer> allItxContainerContainers = new HashSet<ItxContainerContainer>();
		allItxContainerContainers.add(itxc1c2);
		allItxContainerContainers.add(itxc2c3);
		allItxContainerContainers.add(itxc1c3);
		for (ItxContainerContainer itxContainerContainer : allItxContainerContainers) {
			itxContainerContainer.setLsKind(c1.getLsKind());
			itxContainerContainer.setLsType(c1.getLsType());
			itxContainerContainer.setRecordedBy(c1.getRecordedBy());
			itxContainerContainer.setRecordedDate(c1.getRecordedDate());
		}
		// ItxSubjectContainer
		ItxSubjectContainer itxs1c2 = new ItxSubjectContainer();
		itxs1c2.setSubject(s1);
		itxs1c2.setContainer(c2);
		ItxSubjectContainer itxs2c3 = new ItxSubjectContainer();
		itxs2c3.setSubject(s2);
		itxs2c3.setContainer(c3);
		ItxSubjectContainer itxs1c3 = new ItxSubjectContainer();
		itxs1c3.setSubject(s1);
		itxs1c3.setContainer(c3);
		ItxSubjectContainer itxs3c1 = new ItxSubjectContainer();
		itxs3c1.setSubject(s3);
		itxs3c1.setContainer(c1);
		Set<ItxSubjectContainer> allItxSubjectContainer = new HashSet<ItxSubjectContainer>();
		allItxSubjectContainer.add(itxs1c2);
		allItxSubjectContainer.add(itxs2c3);
		allItxSubjectContainer.add(itxs1c3);
		allItxSubjectContainer.add(itxs3c1);
		for (ItxSubjectContainer itxSubjectContainer : allItxSubjectContainer) {
			itxSubjectContainer.setLsKind(c1.getLsKind());
			itxSubjectContainer.setLsType(c1.getLsType());
			itxSubjectContainer.setRecordedBy(c1.getRecordedBy());
			itxSubjectContainer.setRecordedDate(c1.getRecordedDate());
		}

		// Create the various Sets for mapping
		Set<AnalysisGroup> e1AnalysisGroups = new HashSet<AnalysisGroup>();
		e1AnalysisGroups.add(a1);
		e1AnalysisGroups.add(a3);
		Set<AnalysisGroup> e2AnalysisGroups = new HashSet<AnalysisGroup>();
		e2AnalysisGroups.add(a1);
		e2AnalysisGroups.add(a2);
		Set<AnalysisGroup> e3AnalysisGroups = new HashSet<AnalysisGroup>();
		e3AnalysisGroups.add(a2);
		e3AnalysisGroups.add(a3);
		Set<Experiment> a1Experiments = new HashSet<Experiment>();
		a1Experiments.add(e1);
		a1Experiments.add(e2);
		Set<Experiment> a2Experiments = new HashSet<Experiment>();
		a2Experiments.add(e2);
		a2Experiments.add(e3);
		Set<Experiment> a3Experiments = new HashSet<Experiment>();
		a3Experiments.add(e1);
		a3Experiments.add(e3);
		Set<TreatmentGroup> a1TreatmentGroups = new HashSet<TreatmentGroup>();
		a1TreatmentGroups.add(t1);
		a1TreatmentGroups.add(t3);
		Set<TreatmentGroup> a2TreatmentGroups = new HashSet<TreatmentGroup>();
		a2TreatmentGroups.add(t1);
		a2TreatmentGroups.add(t2);
		Set<TreatmentGroup> a3TreatmentGroups = new HashSet<TreatmentGroup>();
		a3TreatmentGroups.add(t2);
		a3TreatmentGroups.add(t3);
		Set<AnalysisGroup> t1AnalysisGroups = new HashSet<AnalysisGroup>();
		t1AnalysisGroups.add(a1);
		t1AnalysisGroups.add(a2);
		Set<AnalysisGroup> t2AnalysisGroups = new HashSet<AnalysisGroup>();
		t2AnalysisGroups.add(a2);
		t2AnalysisGroups.add(a3);
		Set<AnalysisGroup> t3AnalysisGroups = new HashSet<AnalysisGroup>();
		t3AnalysisGroups.add(a1);
		t3AnalysisGroups.add(a3);
		Set<Subject> t1Subjects = new HashSet<Subject>();
		t1Subjects.add(s1);
		t1Subjects.add(s3);
		Set<Subject> t2Subjects = new HashSet<Subject>();
		t2Subjects.add(s1);
		t2Subjects.add(s2);
		Set<Subject> t3Subjects = new HashSet<Subject>();
		t3Subjects.add(s2);
		t3Subjects.add(s3);
		Set<TreatmentGroup> s1TreatmentGroups = new HashSet<TreatmentGroup>();
		s1TreatmentGroups.add(t1);
		s1TreatmentGroups.add(t2);
		Set<TreatmentGroup> s2TreatmentGroups = new HashSet<TreatmentGroup>();
		s2TreatmentGroups.add(t2);
		s2TreatmentGroups.add(t3);
		Set<TreatmentGroup> s3TreatmentGroups = new HashSet<TreatmentGroup>();
		s3TreatmentGroups.add(t1);
		s3TreatmentGroups.add(t3);

		// Make groups of interactions
		Set<ItxExperimentExperiment> e1FirstExperiments = new HashSet<ItxExperimentExperiment>();
		e1FirstExperiments.add(itxe1e2);
		e1FirstExperiments.add(itxe1e3);
		Set<ItxExperimentExperiment> e2FirstExperiments = new HashSet<ItxExperimentExperiment>();
		e2FirstExperiments.add(itxe2e3);
		Set<ItxExperimentExperiment> e2SecondExperiments = new HashSet<ItxExperimentExperiment>();
		e2SecondExperiments.add(itxe1e2);
		Set<ItxExperimentExperiment> e3SecondExperiments = new HashSet<ItxExperimentExperiment>();
		e3SecondExperiments.add(itxe1e3);
		e3SecondExperiments.add(itxe2e3);

		Set<ItxProtocolProtocol> p1FirstProtocols = new HashSet<ItxProtocolProtocol>();
		p1FirstProtocols.add(itxp1p2);
		Set<ItxProtocolProtocol> p2SecondProtocols = new HashSet<ItxProtocolProtocol>();
		p2SecondProtocols.add(itxp1p2);

		Set<ItxContainerContainer> c1FirstContainers = new HashSet<ItxContainerContainer>();
		c1FirstContainers.add(itxc1c3);
		c1FirstContainers.add(itxc1c2);
		Set<ItxContainerContainer> c2FirstContainers = new HashSet<ItxContainerContainer>();
		c2FirstContainers.add(itxc2c3);
		Set<ItxContainerContainer> c2SecondContainers = new HashSet<ItxContainerContainer>();
		c2SecondContainers.add(itxc1c2);
		Set<ItxContainerContainer> c3SecondContainers = new HashSet<ItxContainerContainer>();
		c3SecondContainers.add(itxc1c3);
		c3SecondContainers.add(itxc2c3);

		Set<ItxSubjectContainer> s1Containers = new HashSet<ItxSubjectContainer>();
		s1Containers.add(itxs1c3);
		s1Containers.add(itxs1c2);
		Set<ItxSubjectContainer> s2Containers = new HashSet<ItxSubjectContainer>();
		s2Containers.add(itxs2c3);
		Set<ItxSubjectContainer> s3Containers = new HashSet<ItxSubjectContainer>();
		s3Containers.add(itxs3c1);
		Set<ItxSubjectContainer> c1Subjects = new HashSet<ItxSubjectContainer>();
		c1Subjects.add(itxs3c1);
		Set<ItxSubjectContainer> c2Subjects = new HashSet<ItxSubjectContainer>();
		c2Subjects.add(itxs1c2);
		Set<ItxSubjectContainer> c3Subjects = new HashSet<ItxSubjectContainer>();
		c3Subjects.add(itxs1c3);
		c3Subjects.add(itxs2c3);

		// Then assign all the groups to their owners to create the mappings
		e1.setAnalysisGroups(e1AnalysisGroups);
		e2.setAnalysisGroups(e2AnalysisGroups);
		e3.setAnalysisGroups(e3AnalysisGroups);
		a1.setExperiments(a1Experiments);
		a2.setExperiments(a2Experiments);
		a3.setExperiments(a3Experiments);
		a1.setTreatmentGroups(a1TreatmentGroups);
		a2.setTreatmentGroups(a2TreatmentGroups);
		a3.setTreatmentGroups(a3TreatmentGroups);
		t1.setAnalysisGroups(t1AnalysisGroups);
		t2.setAnalysisGroups(t2AnalysisGroups);
		t3.setAnalysisGroups(t3AnalysisGroups);
		t1.setSubjects(t1Subjects);
		t2.setSubjects(t2Subjects);
		t3.setSubjects(t3Subjects);
		s1.setTreatmentGroups(s1TreatmentGroups);
		s2.setTreatmentGroups(s2TreatmentGroups);
		s3.setTreatmentGroups(s3TreatmentGroups);

		// Then assign interactions to their owners
		e1.setFirstExperiments(e1FirstExperiments);
		e2.setFirstExperiments(e2FirstExperiments);
		e2.setSecondExperiments(e2SecondExperiments);
		e3.setSecondExperiments(e3SecondExperiments);

		protocol.setFirstProtocols(p1FirstProtocols);
		protocol2.setSecondProtocols(p2SecondProtocols);

		c1.setFirstContainers(c1FirstContainers);
		c2.setFirstContainers(c2FirstContainers);
		c2.setSecondContainers(c2SecondContainers);
		c3.setSecondContainers(c3SecondContainers);

		s1.setContainers(s1Containers);
		s2.setContainers(s2Containers);
		s3.setContainers(s3Containers);
		c1.setSubjects(c1Subjects);
		c2.setSubjects(c2Subjects);
		c3.setSubjects(c3Subjects);

		// Then persist and flush everything to the database
		e1.persist();
		// all the other objects are taken care of by our CascadeType.PERSIST setting

		HashMap<String, Long> idMap = new HashMap<String, Long>();
		idMap.put("e1", e1.getId());
		idMap.put("e2", e2.getId());
		idMap.put("e3", e3.getId());
		idMap.put("p1", protocol.getId());
		idMap.put("p2", protocol2.getId());
		idMap.put("a1", a1.getId());
		idMap.put("a2", a2.getId());
		idMap.put("a3", a3.getId());
		idMap.put("t1", t1.getId());
		idMap.put("t2", t2.getId());
		idMap.put("t3", t3.getId());
		idMap.put("s1", s1.getId());
		idMap.put("s2", s2.getId());
		idMap.put("s3", s3.getId());
		idMap.put("c1", c1.getId());
		idMap.put("c2", c2.getId());
		idMap.put("c3", c3.getId());

		return idMap;
	}

	@Transactional
	@Test
	public void itxExperimentRemoveTest() {
		HashMap<String, Long> idMap = makeTestStack();
		Experiment e1 = Experiment.findExperiment(idMap.get("e1"));
		Experiment e2 = Experiment.findExperiment(idMap.get("e2"));
		// logger.debug("--------e2 first experiments:" +
		// e2.getFirstExperiments().size());
		// logger.debug("--------e2 second experiments:" +
		// e2.getSecondExperiments().size());
		// Experiment.removeExperimentItxAware(e1.getId());
		e1.remove();
		Experiment check = Experiment.findExperiment(idMap.get("e1"));
		Assert.assertNull(check);
	}

	@Test
	@Transactional
	public void runMakeTestStack() {
		HashMap<String, Long> idMap = makeTestStack();
		// Collection<Long> experimentIds = new HashSet<Long>();
		// experimentIds.add(idMap.get("e1"));
		// experimentIds.add(idMap.get("e2"));
		// experimentIds.add(idMap.get("e3"));
		// for (Long id: experimentIds) {
		// Collection<Long> analysisGroups =
		// Experiment.removeExperimentCascadeAware(id);
		// Collection<Long> treatmentGroups =
		// AnalysisGroup.removeOrphans(analysisGroups);
		// Collection<Long> subjects = TreatmentGroup.removeOrphans(treatmentGroups);
		// Subject.removeOrphans(subjects);
		// }
	}

	@Transactional
	@Test
	public void itxFullRemoveTest() {
		HashMap<String, Long> idMap = makeTestStack();
		Experiment e1 = Experiment.findExperiment(idMap.get("e1"));
		Experiment e2 = Experiment.findExperiment(idMap.get("e2"));
		Experiment e3 = Experiment.findExperiment(idMap.get("e3"));
		Protocol p1 = Protocol.findProtocol(idMap.get("p1"));
		Protocol p2 = Protocol.findProtocol(idMap.get("p2"));
		AnalysisGroup a1 = AnalysisGroup.findAnalysisGroup(idMap.get("a1"));
		AnalysisGroup a2 = AnalysisGroup.findAnalysisGroup(idMap.get("a2"));
		AnalysisGroup a3 = AnalysisGroup.findAnalysisGroup(idMap.get("a3"));
		TreatmentGroup t1 = TreatmentGroup.findTreatmentGroup(idMap.get("t1"));
		TreatmentGroup t2 = TreatmentGroup.findTreatmentGroup(idMap.get("t2"));
		TreatmentGroup t3 = TreatmentGroup.findTreatmentGroup(idMap.get("t3"));
		Subject s1 = Subject.findSubject(idMap.get("s1"));
		Subject s2 = Subject.findSubject(idMap.get("s2"));
		Subject s3 = Subject.findSubject(idMap.get("s3"));
		Container c1 = Container.findContainer(idMap.get("c1"));
		Container c2 = Container.findContainer(idMap.get("c2"));
		Container c3 = Container.findContainer(idMap.get("c3"));

		e1.remove();
		e2.remove();
		e3.remove();
		p1.remove();
		p2.remove();
		a1.remove();
		a2.remove();
		a3.remove();
		t1.remove();
		t2.remove();
		t3.remove();
		s1.remove();
		s2.remove();
		s3.remove();
		c1.remove();
		c2.remove();
		c3.remove();

		Experiment e1check = Experiment.findExperiment(idMap.get("e1"));
		Experiment e2check = Experiment.findExperiment(idMap.get("e2"));
		Experiment e3check = Experiment.findExperiment(idMap.get("e3"));
		Protocol p1check = Protocol.findProtocol(idMap.get("p1"));
		Protocol p2check = Protocol.findProtocol(idMap.get("p2"));
		AnalysisGroup a1check = AnalysisGroup.findAnalysisGroup(idMap.get("a1"));
		AnalysisGroup a2check = AnalysisGroup.findAnalysisGroup(idMap.get("a2"));
		AnalysisGroup a3check = AnalysisGroup.findAnalysisGroup(idMap.get("a3"));
		TreatmentGroup t1check = TreatmentGroup.findTreatmentGroup(idMap.get("t1"));
		TreatmentGroup t2check = TreatmentGroup.findTreatmentGroup(idMap.get("t2"));
		TreatmentGroup t3check = TreatmentGroup.findTreatmentGroup(idMap.get("t3"));
		Subject s1check = Subject.findSubject(idMap.get("s1"));
		Subject s2check = Subject.findSubject(idMap.get("s2"));
		Subject s3check = Subject.findSubject(idMap.get("s3"));
		Container c1check = Container.findContainer(idMap.get("c1"));
		Container c2check = Container.findContainer(idMap.get("c2"));
		Container c3check = Container.findContainer(idMap.get("c3"));

		Assert.assertNull(e1check);
		Assert.assertNull(e2check);
		Assert.assertNull(e3check);
		Assert.assertNull(p1check);
		Assert.assertNull(p2check);
		Assert.assertNull(a1check);
		Assert.assertNull(a2check);
		Assert.assertNull(a3check);
		Assert.assertNull(t1check);
		Assert.assertNull(t2check);
		Assert.assertNull(t3check);
		Assert.assertNull(s1check);
		Assert.assertNull(s2check);
		Assert.assertNull(s3check);
		Assert.assertNull(c1check);
		Assert.assertNull(c2check);
		Assert.assertNull(c3check);
	}

}
