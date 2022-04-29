

package com.labsynch.labseer.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import com.labsynch.labseer.domain.Author;
import com.labsynch.labseer.domain.AuthorRole;
import com.labsynch.labseer.domain.LabelKind;
import com.labsynch.labseer.domain.LabelSequence;
import com.labsynch.labseer.domain.LabelType;
import com.labsynch.labseer.domain.LsRole;
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
import com.labsynch.labseer.dto.TypeDTO;
import com.labsynch.labseer.dto.TypeKindDTO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class CreateProjectsTests {

	private static final Logger logger = LoggerFactory.getLogger(CreateProjectsTests.class);

	@Autowired
	private AutoLabelService autoLabelService;

	@Test
	//@Transactional
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

		LabelType.getOrCreate("name");
		LabelType labelType = LabelType.findLabelTypesByTypeNameEquals("name").getSingleResult();
		LabelKind.getOrCreate(labelType, "project name");
		LabelKind.getOrCreate(labelType, "project alias");

		StateType stateType = StateType.getOrCreate("metadata");
		StateKind.getOrCreate(stateType, "project metadata");

		ValueType valueType = ValueType.getOrCreate("stringValue");
		ValueKind.getOrCreate(valueType, "project status");

		ValueType valueTypeDate = ValueType.getOrCreate("dateValue");
		ValueKind.getOrCreate(valueTypeDate, "creation date");
		ValueKind.getOrCreate(valueTypeDate, "modification date");

		ValueType valueTypeString = ValueType.getOrCreate("stringValue");
		ValueKind.getOrCreate(valueTypeString, "short description");
		
		TypeDTO roleType = new TypeDTO("Project");
		List<TypeDTO> roleTypes = new ArrayList<TypeDTO>();
		roleTypes.add(roleType);
		TypeDTO.getOrCreateRoleTypes(roleTypes);
		
		List<TypeKindDTO> roleKinds = new ArrayList<TypeKindDTO>();

		String thingTypeAndKind = "project_project";
		String labelTypeAndKind = "name_project name";
		List<LabelSequence> labelSeqs = LabelSequence.findLabelSequencesByThingTypeAndKindEqualsAndLabelTypeAndKindEquals(thingTypeAndKind, labelTypeAndKind).getResultList();
		if (labelSeqs.size() == 0){
			LabelSequence newLabelSeq = new LabelSequence();
			newLabelSeq.setDigits(4);
			newLabelSeq.setLabelPrefix("PROJ");
			newLabelSeq.setLabelSeparator("-");
			newLabelSeq.setStartingNumber(1L);
			newLabelSeq.setLabelTypeAndKind(labelTypeAndKind);
			newLabelSeq.setThingTypeAndKind(thingTypeAndKind);
			newLabelSeq.persist();
		} 

		
		////////////////////////////////

		Long numberOfLabels = 1L;
		List<AutoLabelDTO> thingCodes = autoLabelService.getAutoLabels(thingTypeAndKind, labelTypeAndKind, numberOfLabels);			

		LsThing lsThing = new LsThing();
		lsThing.setCodeName(thingCodes.get(0).getAutoLabel());
		lsThing.setLsType(thingTypeString);
		lsThing.setLsKind(thingKindString);
		lsThing.setLsTransaction(lsTransaction.getId());
		lsThing.setRecordedBy("acas admin");
		lsThing.persist();

		LsThingLabel.saveLsThingLabel(lsTransaction, "acas admin", lsThing, "name", "project name", true, "Apple");
		LsThingLabel.saveLsThingLabel(lsTransaction, "acas admin", lsThing, "name", "project alias", false, "app");

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
		LsThingValue val3 = LsThingValue.saveLsThingValue(lsTransaction, lsThingState, "stringValue", "short description", "project short description");
		val3.setRecordedBy("acas admin");
		val3.persist();
		
		TypeKindDTO roleKind = new TypeKindDTO("Project", lsThing.getCodeName());
		roleKinds.add(roleKind);
		
		////////////////////////////////


		numberOfLabels = 1L;
		thingCodes = autoLabelService.getAutoLabels(thingTypeAndKind, labelTypeAndKind, numberOfLabels);	

		lsThing = new LsThing();
		lsThing.setCodeName(thingCodes.get(0).getAutoLabel());
		lsThing.setLsType(thingTypeString);
		lsThing.setLsKind(thingKindString);
		lsThing.setLsTransaction(lsTransaction.getId());
		lsThing.setRecordedBy("acas admin");
		lsThing.persist();

		LsThingLabel.saveLsThingLabel(lsTransaction, "acas admin", lsThing, "name", "project name", true, "Banana");
		LsThingLabel.saveLsThingLabel(lsTransaction, "acas admin", lsThing, "name", "project alias", false, "bann");

		lsThingState = new LsThingState();
		lsThingState.setLsThing(lsThing);
		lsThingState.setLsType("metadata");
		lsThingState.setLsKind("project metadata");
		lsThingState.setLsTransaction(lsTransaction.getId());
		lsThingState.setRecordedBy("acas admin");
		lsThingState.persist();
		logger.info("thing state: " + lsThingState.toJson());

		val1 = LsThingValue.saveLsThingValue(lsTransaction, lsThingState, "stringValue", "project status", "active");
		val1.setRecordedBy("acas admin");
		val1.persist();
		val2 = LsThingValue.saveLsThingValue(lsTransaction, lsThingState, "dateValue", "creation date", new Date());
		val2.setRecordedBy("acas admin");
		val2.persist();
		val3 = LsThingValue.saveLsThingValue(lsTransaction, lsThingState, "stringValue", "short description", "project short description");
		val3.setRecordedBy("acas admin");
		val3.persist();		
		
		roleKind = new TypeKindDTO("Project", lsThing.getCodeName());
		roleKinds.add(roleKind);
		
		////////////////////////////////

		numberOfLabels = 1L;
		thingCodes = autoLabelService.getAutoLabels(thingTypeAndKind, labelTypeAndKind, numberOfLabels);	

		lsThing = new LsThing();
		lsThing.setCodeName(thingCodes.get(0).getAutoLabel());
		lsThing.setLsType(thingTypeString);
		lsThing.setLsKind(thingKindString);
		lsThing.setLsTransaction(lsTransaction.getId());
		lsThing.setRecordedBy("acas admin");
		lsThing.persist();

		LsThingLabel.saveLsThingLabel(lsTransaction, "acas admin", lsThing, "name", "project name", true, "Pear");
		LsThingLabel.saveLsThingLabel(lsTransaction, "acas admin", lsThing, "name", "project alias", false, "pear");

		lsThingState = new LsThingState();
		lsThingState.setLsThing(lsThing);
		lsThingState.setLsType("metadata");
		lsThingState.setLsKind("project metadata");
		lsThingState.setLsTransaction(lsTransaction.getId());
		lsThingState.setRecordedBy("acas admin");
		lsThingState.persist();
		logger.info("thing state: " + lsThingState.toJson());

		val1 = LsThingValue.saveLsThingValue(lsTransaction, lsThingState, "stringValue", "project status", "active");
		val1.setRecordedBy("acas admin");
		val1.persist();
		val2 = LsThingValue.saveLsThingValue(lsTransaction, lsThingState, "dateValue", "creation date", new Date());
		val2.setRecordedBy("acas admin");
		val2.persist();
		val3 = LsThingValue.saveLsThingValue(lsTransaction, lsThingState, "stringValue", "short description", "project short description");
		val3.setRecordedBy("acas admin");
		val3.persist();
		
		roleKind = new TypeKindDTO("Project", lsThing.getCodeName());
		roleKinds.add(roleKind);
		
		//////////////////////////////////
		TypeKindDTO.getOrCreateRoleKinds(roleKinds);
	}

	//@Test
	@Transactional
	public void GetProject_Test5() throws IOException{

		LsThing lsThing = LsThing.findLsThingsByCodeNameEquals("PROJ-000003").getSingleResult();
		logger.info(lsThing.toPrettyJsonStub());

	}
	
	@Test
//	@Transactional
	public void createProjectRoles() {
		//create users
		Author jappleseed = null;
		try{
			jappleseed = Author.findAuthorsByUserName("jappleseed").getSingleResult();
		}catch (NoResultException e){
			jappleseed = new Author();
			jappleseed.setFirstName("Johnny");
			jappleseed.setLastName("Appleseed");
			jappleseed.setUserName("jappleseed");
			jappleseed.setEmailAddress("jappleseed@mcneilco.com");
			jappleseed.setEnabled(true);
			jappleseed.setPassword("0L4txCG+T80BcuWvzuo5cOLz2UA="); //apple
			jappleseed.persist();
		}
		Author bsplit = null;
		try{
			bsplit = Author.findAuthorsByUserName("bsplit").getSingleResult();
		}catch (NoResultException e){
			bsplit = new Author();
			bsplit.setFirstName("Banana");
			bsplit.setLastName("Split");
			bsplit.setUserName("bsplit");
			bsplit.setEmailAddress("bsplit@mcneilco.com");
			bsplit.setEnabled(true);
			bsplit.setPassword("JQ538SpatpcqCJXSkMR5Lwoybqg="); //banana
			bsplit.persist();
		}
		//create roles based on existing projects
		String appleCodeName = LsThingLabel.findLsThingLabelsByLabelTextEqualsAndIgnoredNot("Apple", true).getSingleResult().getLsThing().getCodeName();
		String bananaCodeName = LsThingLabel.findLsThingLabelsByLabelTextEqualsAndIgnoredNot("Banana", true).getSingleResult().getLsThing().getCodeName();;
		LsRole appleUser = null;
		try{
			appleUser = LsRole.findLsRolesByLsTypeEqualsAndLsKindEqualsAndRoleNameEquals("Project", appleCodeName, "User").getSingleResult();
		}catch(NoResultException e){
			appleUser = new LsRole();
			appleUser.setLsType("Project");
			appleUser.setLsKind(appleCodeName);
			appleUser.setRoleName("User");
			appleUser.persist();
		}
		LsRole appleAdministrator = null;
		try{
			appleAdministrator = LsRole.findLsRolesByLsTypeEqualsAndLsKindEqualsAndRoleNameEquals("Project", appleCodeName, "Administrator").getSingleResult();
		}catch(NoResultException e){
			appleAdministrator = new LsRole();
			appleAdministrator.setLsType("Project");
			appleAdministrator.setLsKind(appleCodeName);
			appleAdministrator.setRoleName("Administrator");
			appleAdministrator.persist();
		}
		LsRole bananaUser = null;
		try{
			bananaUser = LsRole.findLsRolesByLsTypeEqualsAndLsKindEqualsAndRoleNameEquals("Project", bananaCodeName, "User").getSingleResult();
		}catch(NoResultException e){
			bananaUser = new LsRole();
			bananaUser.setLsType("Project");
			bananaUser.setLsKind(bananaCodeName);
			bananaUser.setRoleName("User");
			bananaUser.persist();
		}
		LsRole bananaAdministrator = null;
		try{
			bananaAdministrator = LsRole.findLsRolesByLsTypeEqualsAndLsKindEqualsAndRoleNameEquals("Project", bananaCodeName, "Administrator").getSingleResult();
		}catch(NoResultException e){
			bananaAdministrator = new LsRole();
			bananaAdministrator.setLsType("Project");
			bananaAdministrator.setLsKind(bananaCodeName);
			bananaAdministrator.setRoleName("Administrator");
			bananaAdministrator.persist();
		}
		//create author roles
		AuthorRole appleUserRole = null;
		try{
			appleUserRole = AuthorRole.findAuthorRolesByRoleEntryAndUserEntry(appleUser, jappleseed).getSingleResult();
		}catch (NoResultException e){
			appleUserRole = new AuthorRole();
			appleUserRole.setRoleEntry(appleUser);
			appleUserRole.setUserEntry(jappleseed);
			appleUserRole.persist();
		}
		AuthorRole appleAdministratorRole = null;
		try{
			appleAdministratorRole = AuthorRole.findAuthorRolesByRoleEntryAndUserEntry(appleAdministrator, jappleseed).getSingleResult();
		}catch (NoResultException e){
			appleAdministratorRole = new AuthorRole();
			appleAdministratorRole.setRoleEntry(appleAdministrator);
			appleAdministratorRole.setUserEntry(jappleseed);
			appleAdministratorRole.persist();
		}
		AuthorRole bananaUserRole = null;
		try{
			bananaUserRole = AuthorRole.findAuthorRolesByRoleEntryAndUserEntry(bananaUser, bsplit).getSingleResult();
		}catch (NoResultException e){
			bananaUserRole = new AuthorRole();
			bananaUserRole.setRoleEntry(bananaUser);
			bananaUserRole.setUserEntry(bsplit);
			bananaUserRole.persist();
		}
	}


}
