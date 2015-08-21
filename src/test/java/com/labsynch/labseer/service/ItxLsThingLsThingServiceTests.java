

package com.labsynch.labseer.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.ItxLsThingLsThing;
import com.labsynch.labseer.utils.PropertiesUtilService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class ItxLsThingLsThingServiceTests {

	private static final Logger logger = LoggerFactory.getLogger(ItxLsThingLsThingServiceTests.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;
	
	@Autowired
	private ItxLsThingLsThingService itxLsThingLsThingService;
	
	@Test
	@Transactional
	public void update_itx_in_place() {
		String json = "{\"lsType\":\"supports\",\"lsKind\":\"reference_gene\",\"lsTypeAndKind\":\"supports_reference_gene\",\"lsStates\":[{\"deleted\":false,\"id\":288343,\"ignored\":false,\"lsKind\":\"in vivo default\",\"lsType\":\"evidence\",\"lsTypeAndKind\":\"evidence_in vivo default\",\"lsValues\":[{\"clobValue\":\"test 2 e\",\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":2351553,\"ignored\":false,\"lsKind\":\"excerpt\",\"lsType\":\"clobValue\",\"lsTypeAndKind\":\"clobValue_excerpt\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"acas admin\",\"recordedDate\":1438883499520,\"unitTypeAndKind\":\"null_null\",\"value\":\"test 2 e\"},{\"clobValue\":\"test 2 c\",\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":2351554,\"ignored\":false,\"lsKind\":\"conclusion\",\"lsType\":\"clobValue\",\"lsTypeAndKind\":\"clobValue_conclusion\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"acas admin\",\"recordedDate\":1438883499520,\"unitTypeAndKind\":\"null_null\",\"value\":\"test 2 c\"}],\"recordedBy\":\"acas admin\",\"recordedDate\":1438883499520}],\"recordedBy\":\"egao\",\"recordedDate\":1438883481884,\"firstLsThing\":{\"id\":252891},\"secondLsThing\":{\"id\":6},\"deleted\":false,\"id\":287855,\"ignored\":false,\"cid\":\"c166\"}";
		ItxLsThingLsThing jsonItxLsThingLsThing = ItxLsThingLsThing.fromJsonToItxLsThingLsThing(json);
		ItxLsThingLsThing updatedItxLsThingLsThing = itxLsThingLsThingService.updateItxLsThingLsThing(jsonItxLsThingLsThing);
		logger.info(updatedItxLsThingLsThing.toJson());
		updatedItxLsThingLsThing.flush();
		updatedItxLsThingLsThing.clear();
	}
	
	@Test
	@Transactional
	public void save_itx() {
		String json = "{\"lsType\":\"supports\",\"lsKind\":\"reference_gene\",\"lsTypeAndKind\":\"supports_reference_gene\",\"lsStates\":[{\"lsType\":\"evidence\",\"lsKind\":\"in vitro default\",\"lsValues\":[{\"lsType\":\"clobValue\",\"lsKind\":\"excerpt\",\"ignored\":false,\"recordedDate\":1440111005789,\"recordedBy\":\"egao\",\"value\":\"test aug 20 excerpt\",\"clobValue\":\"test aug 20 excerpt\"},{\"lsType\":\"clobValue\",\"lsKind\":\"conclusion\",\"ignored\":false,\"recordedDate\":1440111005790,\"recordedBy\":\"egao\",\"value\":\"test aug20 conclusion\",\"clobValue\":\"test aug20 conclusion\"}],\"ignored\":false,\"recordedDate\":1440111005789,\"recordedBy\":\"egao\"}],\"recordedBy\":\"egao\",\"recordedDate\":1440111005789,\"firstLsThing\":{\"id\":667047},\"cid\":\"c1263\",\"_changing\":false,\"_previousAttributes\":{\"lsType\":\"supports\",\"lsKind\":\"reference_gene\",\"lsTypeAndKind\":\"supports_reference_gene\",\"lsStates\":[{\"lsType\":\"evidence\",\"lsKind\":\"in vitro default\",\"lsValues\":[{\"lsType\":\"clobValue\",\"lsKind\":\"excerpt\",\"ignored\":false,\"recordedDate\":1440111005789,\"recordedBy\":\"egao\",\"value\":\"test aug 20 excerpt\",\"clobValue\":\"test aug 20 excerpt\"},{\"lsType\":\"clobValue\",\"lsKind\":\"conclusion\",\"ignored\":false,\"recordedDate\":1440111005790,\"recordedBy\":\"egao\",\"value\":\"test aug20 conclusion\",\"clobValue\":\"test aug20 conclusion\"}],\"ignored\":false,\"recordedDate\":1440111005789,\"recordedBy\":\"egao\"}],\"recordedBy\":\"egao\",\"recordedDate\":1440110988652,\"firstLsThing\":{}},\"changed\":{\"evidenceClass\":\"general\"},\"_pending\":false,\"className\":\"BaseEvidence\",\"lsProperties\":{\"defaultValues\":[{\"key\":\"thumbnail\",\"type\":\"fileValue\",\"kind\":\"thumbnail\"},{\"key\":\"excerpt\",\"type\":\"clobValue\",\"kind\":\"excerpt\"},{\"key\":\"conclusion\",\"type\":\"clobValue\",\"kind\":\"conclusion\"}]},\"validationError\":null,\"idAttribute\":\"id\",\"secondLsThing\":{\"id\":4}}";
		ItxLsThingLsThing jsonItxLsThingLsThing = ItxLsThingLsThing.fromJsonToItxLsThingLsThing(json);
		ItxLsThingLsThing updatedItxLsThingLsThing = itxLsThingLsThingService.saveItxLsThingLsThing(jsonItxLsThingLsThing);
		logger.info(updatedItxLsThingLsThing.toJson());
		updatedItxLsThingLsThing.flush();
		updatedItxLsThingLsThing.clear();
	}

}
