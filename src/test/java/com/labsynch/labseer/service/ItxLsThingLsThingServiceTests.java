
package com.labsynch.labseer.service;

import java.util.Set;

import com.labsynch.labseer.domain.ItxLsThingLsThing;
import com.labsynch.labseer.domain.ItxLsThingLsThingState;
import com.labsynch.labseer.domain.ItxLsThingLsThingValue;
import com.labsynch.labseer.utils.PropertiesUtilService;

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

import junit.framework.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/applicationContext.xml",
		"classpath:/META-INF/spring/applicationContext-security.xml" })
@Configurable
public class ItxLsThingLsThingServiceTests {

	private static final Logger logger = LoggerFactory.getLogger(ItxLsThingLsThingServiceTests.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Autowired
	private ItxLsThingLsThingService itxLsThingLsThingService;

	@Test
	@Transactional
	@Rollback(value = false)
	public void simple_update_itx_in_place() {

		String json = "{\"deleted\":false,\"firstLsThing\":{\"codeName\":\"REFF-000006\",\"deleted\":false,\"id\":287906,\"ignored\":false,\"lsKind\":\"file\",\"lsTransaction\":628,\"lsType\":\"reference\",\"lsTypeAndKind\":\"reference_file\",\"modifiedDate\":1439399721668,\"recordedBy\":\"egao\",\"recordedDate\":1439399728664,\"version\":21},\"id\":667115,\"ignored\":true,\"lsKind\":\"reference_gene\",\"lsStates\":[],\"lsType\":\"supports\",\"lsTypeAndKind\":\"supports_reference_gene\",\"modifiedDate\":1440611855937,\"recordedBy\":\"egao\",\"recordedDate\":1440432694364,\"secondLsThing\":{\"codeName\":\"GENE-000005\",\"deleted\":false,\"id\":4,\"ignored\":false,\"lsKind\":\"entrez gene\",\"lsTransaction\":696,\"lsType\":\"gene\",\"lsTypeAndKind\":\"gene_entrez gene\",\"modifiedDate\":1440539263211,\"recordedBy\":\"acas admin\",\"recordedDate\":1440539262284,\"version\":34}}";

		ItxLsThingLsThing jsonItxLsThingLsThing = ItxLsThingLsThing.fromJsonToItxLsThingLsThing(json);

		// logger.info("here is ignore value before the update: " +
		// jsonItxLsThingLsThing.isIgnored());
		// Assert.assertEquals(true, jsonItxLsThingLsThing.isIgnored());

		ItxLsThingLsThing updatedItxLsThingLsThing = itxLsThingLsThingService
				.updateItxLsThingLsThing(jsonItxLsThingLsThing);
		// logger.info("here is ignore value after the update: " +
		// updatedItxLsThingLsThing.isIgnored());
		//
		Assert.assertEquals(true, updatedItxLsThingLsThing.isIgnored());
		// logger.info(updatedItxLsThingLsThing.toJson());
		// updatedItxLsThingLsThing.flush();
		// updatedItxLsThingLsThing.clear();

		// logger.info(ItxLsThingLsThing.findItxLsThingLsThing(updatedItxLsThingLsThing.getId()).toJson());
	}

	@Test
	@Transactional
	@Rollback(value = false)
	public void update_itx_in_place() {

		// String json =
		// "{\"lsType\":\"supports\",\"lsKind\":\"reference_gene\",\"lsTypeAndKind\":\"supports_reference_gene\",\"lsStates\":[{\"deleted\":false,\"id\":288343,\"ignored\":false,\"lsKind\":\"in
		// vivo default\",\"lsType\":\"evidence\",\"lsTypeAndKind\":\"evidence_in vivo
		// default\",\"lsValues\":[{\"clobValue\":\"test 2
		// e\",\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":2351553,\"ignored\":false,\"lsKind\":\"excerpt\",\"lsType\":\"clobValue\",\"lsTypeAndKind\":\"clobValue_excerpt\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"acas
		// admin\",\"recordedDate\":1438883499520,\"unitTypeAndKind\":\"null_null\",\"value\":\"test
		// 2 e\"},{\"clobValue\":\"test 2
		// c\",\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":2351554,\"ignored\":false,\"lsKind\":\"conclusion\",\"lsType\":\"clobValue\",\"lsTypeAndKind\":\"clobValue_conclusion\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"acas
		// admin\",\"recordedDate\":1438883499520,\"unitTypeAndKind\":\"null_null\",\"value\":\"test
		// 2 c\"}],\"recordedBy\":\"acas
		// admin\",\"recordedDate\":1438883499520}],\"recordedBy\":\"egao\",\"recordedDate\":1438883481884,\"firstLsThing\":{\"id\":252891},\"secondLsThing\":{\"id\":6},\"deleted\":false,\"id\":287855,\"ignored\":false,\"cid\":\"c166\"}";

		// String json =
		// "{\"lsType\":\"supports\",\"lsKind\":\"reference_gene\",\"lsTypeAndKind\":\"supports_reference_gene\",\"lsStates\":[{\"deleted\":false,\"id\":674034,\"ignored\":false,\"lsKind\":\"in
		// vitro default\",\"lsType\":\"evidence\",\"lsTypeAndKind\":\"evidence_in vitro
		// default\",\"lsValues\":[{\"clobValue\":\"test in vitro aug 21 conclusion no
		// thumbnail\",\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":4457531,\"ignored\":true,\"lsKind\":\"conclusion\",\"lsType\":\"clobValue\",\"lsTypeAndKind\":\"clobValue_conclusion\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"egao\",\"recordedDate\":1440206102356,\"unitTypeAndKind\":\"null_null\",\"version\":0,\"value\":\"test
		// in vitro aug 21 conclusion no thumbnail e\"},{\"clobValue\":\"test in vitro
		// aug 21 excerpt no
		// thumbnail\",\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":4457530,\"ignored\":false,\"lsKind\":\"excerpt\",\"lsType\":\"clobValue\",\"lsTypeAndKind\":\"clobValue_excerpt\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"egao\",\"recordedDate\":1440206102356,\"unitTypeAndKind\":\"null_null\",\"version\":0,\"value\":\"test
		// in vitro aug 21 excerpt no
		// thumbnail\"},{\"lsType\":\"clobValue\",\"lsKind\":\"conclusion\",\"ignored\":false,\"recordedDate\":1440432694365,\"recordedBy\":\"egao\",\"clobValue\":\"test
		// in vitro aug 21 conclusion no thumbnail EDIT\",\"value\":\"test in vitro aug
		// 21 conclusion no thumbnail
		// EDIT\"}],\"recordedBy\":\"egao\",\"recordedDate\":1440206102356,\"version\":1}],\"recordedBy\":\"egao\",\"recordedDate\":1440432694364,\"deleted\":false,\"id\":667115,\"ignored\":false,\"version\":1,\"cid\":\"c221\",\"_changing\":false,\"_previousAttributes\":{\"lsType\":\"supports\",\"lsKind\":\"reference_gene\",\"lsTypeAndKind\":\"supports_reference_gene\",\"lsStates\":[],\"recordedBy\":\"egao\",\"recordedDate\":1440432675289,\"firstLsThing\":{}},\"changed\":{\"evidenceClass\":\"general\"},\"_pending\":false,\"className\":\"BaseEvidence\",\"lsProperties\":{\"defaultValues\":[{\"key\":\"thumbnail\",\"type\":\"fileValue\",\"kind\":\"thumbnail\"},{\"key\":\"excerpt\",\"type\":\"clobValue\",\"kind\":\"excerpt\"},{\"key\":\"conclusion\",\"type\":\"clobValue\",\"kind\":\"conclusion\"}]},\"validationError\":null,\"idAttribute\":\"id\",\"secondLsThing\":{\"id\":4},\"firstLsThing\":{\"id\":287906}}";

		// String json =
		// "{\"lsType\":\"supports\",\"lsKind\":\"reference_gene\",\"lsTypeAndKind\":\"supports_reference_gene\",\"lsStates\":[{\"deleted\":false,\"id\":674034,\"ignored\":false,\"lsKind\":\"in
		// vitro default\",\"lsType\":\"evidence\",\"lsTypeAndKind\":\"evidence_in vitro
		// default\",\"lsValues\":[{\"clobValue\":\"test in vitro aug 21 conclusion no
		// thumbnail\",\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":4457531,\"ignored\":true,\"lsKind\":\"conclusion\",\"lsType\":\"clobValue\",\"lsTypeAndKind\":\"clobValue_conclusion\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"egao\",\"recordedDate\":1440206102356,\"unitTypeAndKind\":\"null_null\",\"version\":null,\"value\":\"test
		// in vitro aug 21 conclusion no thumbnail e\"},{\"clobValue\":\"test in vitro
		// aug 21 excerpt no
		// thumbnail\",\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":4457530,\"ignored\":false,\"lsKind\":\"excerpt\",\"lsType\":\"clobValue\",\"lsTypeAndKind\":\"clobValue_excerpt\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"egao\",\"recordedDate\":1440206102356,\"unitTypeAndKind\":\"null_null\",\"version\":null,\"value\":\"test
		// in vitro aug 21 excerpt no
		// thumbnail\"},{\"lsType\":\"clobValue\",\"lsKind\":\"conclusion\",\"ignored\":false,\"recordedDate\":1440432694365,\"recordedBy\":\"egao\",\"clobValue\":\"test
		// in vitro aug 21 conclusion no thumbnail EDIT Version1\",\"value\":\"test in
		// vitro aug 21 conclusion no thumbnail EDIT
		// Version2\"}],\"recordedBy\":\"egao\",\"recordedDate\":1440206102356,\"version\":null}],\"recordedBy\":\"egao\",\"recordedDate\":1440432694364,\"deleted\":false,\"id\":667115,\"ignored\":false,\"version\":null,\"cid\":\"c221\",\"_changing\":false,\"_previousAttributes\":{\"lsType\":\"supports\",\"lsKind\":\"reference_gene\",\"lsTypeAndKind\":\"supports_reference_gene\",\"lsStates\":[],\"recordedBy\":\"egao\",\"recordedDate\":1440432675289,\"firstLsThing\":{}},\"changed\":{\"evidenceClass\":\"general\"},\"_pending\":false,\"className\":\"BaseEvidence\",\"lsProperties\":{\"defaultValues\":[{\"key\":\"thumbnail\",\"type\":\"fileValue\",\"kind\":\"thumbnail\"},{\"key\":\"excerpt\",\"type\":\"clobValue\",\"kind\":\"excerpt\"},{\"key\":\"conclusion\",\"type\":\"clobValue\",\"kind\":\"conclusion\"}]},\"validationError\":null,\"idAttribute\":\"id\",\"secondLsThing\":{\"id\":4},\"firstLsThing\":{\"id\":287906}}";

		String json = "{\"lsType\":\"supports\",\"lsKind\":\"reference_gene\",\"lsTypeAndKind\":\"supports_reference_gene\",\"lsStates\":[{\"deleted\":false,\"id\":674034,\"ignored\":false,\"lsKind\":\"in vitro default\",\"lsType\":\"evidence\",\"lsTypeAndKind\":\"evidence_in vitro default\",\"lsValues\":[{\"clobValue\":\"test in vitro aug 21 conclusion no thumbnail\",\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":4457531,\"ignored\":true,\"lsKind\":\"conclusion\",\"lsType\":\"clobValue\",\"lsTypeAndKind\":\"clobValue_conclusion\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"egao\",\"recordedDate\":1440206102356,\"unitTypeAndKind\":\"null_null\",\"version\":null,\"value\":\"test in vitro aug 21 conclusion no thumbnail e\"},{\"clobValue\":\"test in vitro aug 21 excerpt no thumbnail\",\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":4457530,\"ignored\":false,\"lsKind\":\"excerpt\",\"lsType\":\"clobValue\",\"lsTypeAndKind\":\"clobValue_excerpt\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"egao\",\"recordedDate\":1440206102356,\"unitTypeAndKind\":\"null_null\",\"version\":null,\"value\":\"test in vitro aug 21 excerpt no thumbnail\"},{\"lsType\":\"clobValue\",\"lsKind\":\"conclusion\",\"ignored\":false,\"recordedDate\":1440432694365,\"recordedBy\":\"egao\",\"clobValue\":\"test in vitro aug 21 conclusion no thumbnail EDIT Version1\",\"value\":\"test in vitro aug 21 conclusion no thumbnail EDIT Version2\"}],\"recordedBy\":\"egao\",\"recordedDate\":1440206102356,\"version\":null}],\"recordedBy\":\"egao\",\"recordedDate\":1440432694364,\"deleted\":false,\"id\":667115,\"ignored\":true,\"version\":null,\"cid\":\"c221\",\"_changing\":false,\"_previousAttributes\":{\"lsType\":\"supports\",\"lsKind\":\"reference_gene\",\"lsTypeAndKind\":\"supports_reference_gene\",\"lsStates\":[],\"recordedBy\":\"egao\",\"recordedDate\":1440432675289,\"firstLsThing\":{}},\"changed\":{\"evidenceClass\":\"general\"},\"_pending\":false,\"className\":\"BaseEvidence\",\"lsProperties\":{\"defaultValues\":[{\"key\":\"thumbnail\",\"type\":\"fileValue\",\"kind\":\"thumbnail\"},{\"key\":\"excerpt\",\"type\":\"clobValue\",\"kind\":\"excerpt\"},{\"key\":\"conclusion\",\"type\":\"clobValue\",\"kind\":\"conclusion\"}]},\"validationError\":null,\"idAttribute\":\"id\",\"secondLsThing\":{\"id\":4},\"firstLsThing\":{\"id\":287906}}";

		ItxLsThingLsThing jsonItxLsThingLsThing = ItxLsThingLsThing.fromJsonToItxLsThingLsThing(json);

		// logger.info("here is ignore value before the update: " +
		// jsonItxLsThingLsThing.isIgnored());
		// Assert.assertEquals(true, jsonItxLsThingLsThing.isIgnored());

		ItxLsThingLsThing updatedItxLsThingLsThing = itxLsThingLsThingService
				.updateItxLsThingLsThing(jsonItxLsThingLsThing);
		// logger.info("here is ignore value after the update: " +
		// updatedItxLsThingLsThing.isIgnored());
		//
		// Assert.assertEquals(true, updatedItxLsThingLsThing.isIgnored());
		// logger.info(updatedItxLsThingLsThing.toJson());
		// updatedItxLsThingLsThing.flush();
		// updatedItxLsThingLsThing.clear();

		// logger.info(ItxLsThingLsThing.findItxLsThingLsThing(updatedItxLsThingLsThing.getId()).toJson());
	}

	// @Test
	@Transactional
	public void checkItx() {

		logger.info(ItxLsThingLsThing.findItxLsThingLsThing(667115L).toJson());

		Assert.assertEquals(true, ItxLsThingLsThing.findItxLsThingLsThing(667115L).isIgnored());

		Set<ItxLsThingLsThingState> states = ItxLsThingLsThing.findItxLsThingLsThing(667115L).getLsStates();
		logger.info("------------ number of states found: " + states.size() + "  -----------------");
		for (ItxLsThingLsThingState state : states) {
			logger.info(state.toJson());
			Set<ItxLsThingLsThingValue> values = state.getLsValues();
			logger.info("------------ number of values found: " + values.size() + "  -----------------");

			for (ItxLsThingLsThingValue value : values) {
				logger.info(value.toJson());
			}
		}
	}

	// @Test
	@Transactional
	public void save_itx() {
		String json = "{\"lsType\":\"supports\",\"lsKind\":\"reference_gene\",\"lsTypeAndKind\":\"supports_reference_gene\",\"lsStates\":[{\"lsType\":\"evidence\",\"lsKind\":\"in vitro default\",\"lsValues\":[{\"lsType\":\"clobValue\",\"lsKind\":\"excerpt\",\"ignored\":false,\"recordedDate\":1440111005789,\"recordedBy\":\"egao\",\"value\":\"test aug 20 excerpt\",\"clobValue\":\"test aug 20 excerpt\"},{\"lsType\":\"clobValue\",\"lsKind\":\"conclusion\",\"ignored\":false,\"recordedDate\":1440111005790,\"recordedBy\":\"egao\",\"value\":\"test aug20 conclusion\",\"clobValue\":\"test aug20 conclusion\"}],\"ignored\":false,\"recordedDate\":1440111005789,\"recordedBy\":\"egao\"}],\"recordedBy\":\"egao\",\"recordedDate\":1440111005789,\"firstLsThing\":{\"id\":667047},\"cid\":\"c1263\",\"_changing\":false,\"_previousAttributes\":{\"lsType\":\"supports\",\"lsKind\":\"reference_gene\",\"lsTypeAndKind\":\"supports_reference_gene\",\"lsStates\":[{\"lsType\":\"evidence\",\"lsKind\":\"in vitro default\",\"lsValues\":[{\"lsType\":\"clobValue\",\"lsKind\":\"excerpt\",\"ignored\":false,\"recordedDate\":1440111005789,\"recordedBy\":\"egao\",\"value\":\"test aug 20 excerpt\",\"clobValue\":\"test aug 20 excerpt\"},{\"lsType\":\"clobValue\",\"lsKind\":\"conclusion\",\"ignored\":false,\"recordedDate\":1440111005790,\"recordedBy\":\"egao\",\"value\":\"test aug20 conclusion\",\"clobValue\":\"test aug20 conclusion\"}],\"ignored\":false,\"recordedDate\":1440111005789,\"recordedBy\":\"egao\"}],\"recordedBy\":\"egao\",\"recordedDate\":1440110988652,\"firstLsThing\":{}},\"changed\":{\"evidenceClass\":\"general\"},\"_pending\":false,\"className\":\"BaseEvidence\",\"lsProperties\":{\"defaultValues\":[{\"key\":\"thumbnail\",\"type\":\"fileValue\",\"kind\":\"thumbnail\"},{\"key\":\"excerpt\",\"type\":\"clobValue\",\"kind\":\"excerpt\"},{\"key\":\"conclusion\",\"type\":\"clobValue\",\"kind\":\"conclusion\"}]},\"validationError\":null,\"idAttribute\":\"id\",\"secondLsThing\":{\"id\":4}}";
		ItxLsThingLsThing jsonItxLsThingLsThing = ItxLsThingLsThing.fromJsonToItxLsThingLsThing(json);
		ItxLsThingLsThing updatedItxLsThingLsThing = itxLsThingLsThingService
				.saveItxLsThingLsThing(jsonItxLsThingLsThing);
		logger.info(updatedItxLsThingLsThing.toJson());
		updatedItxLsThingLsThing.flush();
		updatedItxLsThingLsThing.clear();
	}

}
