

package com.labsynch.labseer.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.LabelKind;
import com.labsynch.labseer.domain.LabelSequence;
import com.labsynch.labseer.domain.LabelType;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.LsThingLabel;
import com.labsynch.labseer.domain.LsThingState;
import com.labsynch.labseer.domain.LsThingValue;
import com.labsynch.labseer.domain.LsTransaction;
import com.labsynch.labseer.domain.StateKind;
import com.labsynch.labseer.domain.StateType;
import com.labsynch.labseer.domain.ThingKind;
import com.labsynch.labseer.domain.ThingType;
import com.labsynch.labseer.domain.ValueKind;
import com.labsynch.labseer.domain.ValueType;
import com.labsynch.labseer.dto.AutoLabelDTO;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class CreateProjectsTests {

	private static final Logger logger = LoggerFactory.getLogger(CreateProjectsTests.class);
	
    @Autowired
    private AutoLabelService autoLabelService;

	@Test
//	@Transactional
	public void SaveProject_Test1() throws IOException{

			LsTransaction lsTransaction = new LsTransaction();
			lsTransaction.setComments("saving new project info");
			lsTransaction.setRecordedDate(new Date());
			lsTransaction.persist();

			//set up project type and kinds (check if already exists)
			String thingTypeString = "project";
			String thingKindString = "project";
			ThingType geneType = ThingType.getOrCreate(thingTypeString);
			ThingKind.getOrCreate(geneType, thingKindString);

			LabelType labelType = LabelType.findLabelTypesByTypeNameEquals("name").getSingleResult();
			LabelKind.getOrCreate(labelType, "project name");

			StateType stateType = StateType.getOrCreate("metadata");
			StateKind.getOrCreate(stateType, "project metadata");

			ValueType valueType = ValueType.getOrCreate("stringValue");
			ValueKind.getOrCreate(valueType, "project status");

			ValueType valueTypeDate = ValueType.getOrCreate("dateValue");
			ValueKind.getOrCreate(valueTypeDate, "creation date");
			ValueKind.getOrCreate(valueTypeDate, "modification date");

			String thingTypeAndKind = "project_project";
			String labelTypeAndKind = "name_project name";
			List<LabelSequence> labelSeqs = LabelSequence.findLabelSequencesByThingTypeAndKindEqualsAndLabelTypeAndKindEquals(thingTypeAndKind, labelTypeAndKind).getResultList();
			if (labelSeqs.size() == 0){
				LabelSequence newLabelSeq = new LabelSequence();
				newLabelSeq.setDigits(6);
				newLabelSeq.setLabelPrefix("PROJ");
				newLabelSeq.setLabelSeparator("-");
				newLabelSeq.setLatestNumber(1L);
				newLabelSeq.setLabelTypeAndKind(labelTypeAndKind);
				newLabelSeq.setThingTypeAndKind(thingTypeAndKind);
				newLabelSeq.persist();
			} 
			
			Long numberOfLabels = 1L;
			List<AutoLabelDTO> thingCodes = autoLabelService.getAutoLabels(thingTypeAndKind, labelTypeAndKind, numberOfLabels);			
			
			LsThing lsThing = new LsThing();
			lsThing.setCodeName(thingCodes.get(0).getAutoLabel());
			lsThing.setLsType(thingTypeString);
			lsThing.setLsKind(thingKindString);
			lsThing.setLsTransaction(lsTransaction.getId());
			lsThing.setRecordedBy("acas admin");
			lsThing.persist();

			LsThingLabel.saveLsThingLabel(lsTransaction, "acas admin", lsThing, "name", "project name", true, "Fluomics Project 1");

			LsThingState lsThingState = new LsThingState();
			lsThingState.setLsThing(lsThing);
			lsThingState.setLsType("metadata");
			lsThingState.setLsKind("project metadata");
			lsThingState.setLsTransaction(lsTransaction.getId());
			lsThingState.setRecordedBy("acas admin");
			lsThingState.persist();
			logger.info("thing state: " + lsThingState.toJson());

			LsThingValue val1 = LsThingValue.saveLsThingValue(lsTransaction, lsThingState, "stringValue", "project status", "active");
			val1.setRecordedBy("acas admin");
			val1.persist();
			LsThingValue val2 = LsThingValue.saveLsThingValue(lsTransaction, lsThingState, "dateValue", "creation date", new Date());
			val2.setRecordedBy("acas admin");
			val2.persist();
			
logger.info("value 1: " + val1.toJson());
logger.info("value 1 nested json: " + LsThingState.toJsonArray(LsThingState.findLsThingStatesByLsTransactionEquals(lsTransaction.getId()).getResultList()));
logger.info("value 1 nested json: " + LsThingState.toJsonArray(LsThingState.findLsThingStatesByLsThing(lsThing).getResultList()));
List<LsThingState> lsStates = LsThingState.findLsThingStatesByLsThing(lsThing).getResultList();
Set<LsThingState> lsStatesSet = new HashSet<LsThingState>();
lsStatesSet.addAll(lsStates);
lsThing.setLsStates(lsStatesSet);
			//logger.info("Done saving the project" + (lsThing.toPrettyJsonStub()));

		}

	//@Test
	@Transactional
	public void GetProject_Test2() throws IOException{

		LsThing lsThing = LsThing.findLsThingsByCodeNameEquals("PROJ-000003").getSingleResult();
		logger.info(lsThing.toPrettyJsonStub());
		
	}


}
