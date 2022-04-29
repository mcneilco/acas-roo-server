package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.labsynch.labseer.domain.SubjectValue;
import com.labsynch.labseer.domain.TreatmentGroup;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/applicationContext.xml",
		"classpath:/META-INF/spring/applicationContext-security.xml" })
@Configurable
public class FlagWellDTOTest {

	private static final Logger logger = LoggerFactory.getLogger(TgDataDTO.class);

	@Test
	@Transactional
	public void findResponseSubjectValuesByTreatmentGroup() {
		Long id = 523655L;
		TreatmentGroup treatmentGroup = TreatmentGroup.findTreatmentGroup(id);
		EntityManager em = SubjectValue.entityManager();
		TypedQuery<SubjectValue> q = em.createQuery("SELECT resultsValue "
				+ "FROM TreatmentGroup AS treatmentGroup "
				+ "JOIN treatmentGroup.subjects AS subject "
				+ "JOIN subject.lsStates AS resultsState "
				+ "JOIN resultsState.lsValues AS resultsValue "
				+ "WHERE treatmentGroup = :treatmentGroup "
				+ "AND subject.ignored IS NOT :ignored "
				+ "AND resultsState.ignored IS NOT :ignored "
				+ "AND resultsState.lsType = :resultsStateType "
				+ "AND resultsState.lsKind = :resultsStateKind "
				+ "AND resultsValue.lsType = :resultsValueType "
				+ "AND resultsValue.lsKind = :resultsValueKind ", SubjectValue.class);

		q.setParameter("resultsStateType", "data");
		q.setParameter("resultsStateKind", "results");
		q.setParameter("resultsValueType", "numericValue");
		q.setParameter("resultsValueKind", "Response");
		q.setParameter("treatmentGroup", treatmentGroup);
		q.setParameter("ignored", true);
		Collection<SubjectValue> results = q.getResultList();
		logger.debug(SubjectValue.toJsonArray(results));

	}

	@Test
	@Transactional
	public void findResponseSubjectValuesByTreatmentGroup_2() {
		Long id = 523655L;
		TreatmentGroup treatmentGroup = TreatmentGroup.findTreatmentGroup(id);
		Collection<SubjectValue> results = FlagWellDTO.findNotKONumericValueSubjectValues(treatmentGroup);
		logger.info(SubjectValue.toJsonArray(results));

	}

	@Test
	@Transactional
	public void findRenderingHintTest() {
		Long id = 1028705L;
		TreatmentGroup treatmentGroup = TreatmentGroup.findTreatmentGroup(id);
		String renderingHint = CurveFitDTO.findRenderingHint(treatmentGroup);
		logger.debug(renderingHint);

	}

	@Test
	@Transactional
	public void findBatchCodeSubjectValueTest() {
		Long id = 1132177L;
		TreatmentGroup treatmentGroup = TreatmentGroup.findTreatmentGroup(id);
		SubjectValue sv = FlagWellDTO.findBatchCodeSubjectValue(treatmentGroup);
		logger.info(sv.toJson());
	}

	@Test
	@Transactional
	public void updateWellFlagsTest() {
		Long id1 = 3536726L;
		Long id2 = 3536727L;
		Long id3 = 3536728L;
		Long id4 = 3536729L;
		Long id5 = 3536730L;
		FlagWellDTO flagWellDTO = new FlagWellDTO();
		flagWellDTO.setResponseSubjectValueId(id1);
		flagWellDTO.setRecordedBy("bfielder");
		flagWellDTO.setUserFlagStatus("knocked out");
		flagWellDTO.setUserFlagObservation("high signal");
		flagWellDTO.setUserFlagCause("tip clog");
		Collection<FlagWellDTO> flagWellDTOs = new HashSet<FlagWellDTO>();
		flagWellDTOs.add(flagWellDTO);
		FlagWellDTO.updateWellFlags(flagWellDTOs);

	}

}
