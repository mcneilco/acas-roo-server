package com.labsynch.labseer.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.exceptions.NotFoundException;
import com.labsynch.labseer.exceptions.UniqueNameException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import junit.framework.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/applicationContext.xml",
		"classpath:/META-INF/spring/applicationContext-security.xml" })
@Configurable
public class ExperimentServiceTests4_Save {

	private static final Logger logger = LoggerFactory
			.getLogger(ExperimentServiceTests4_Save.class);

	@Autowired
	private ExperimentService experimentService;

	@Autowired
	private ExperimentValueService experimentValueService;

	@Autowired
	private AnalysisGroupService analysisGroupService;

	// @Test
	@Transactional
	public void uniqueNameExceptionTest() throws UniqueNameException {
		String json = "{\"lsKind\":\"gx depot\",\"lsLabels\":[{\"labelText\":\"Guy TEST DUPE 101\",\"lsKind\":\"experiment name\",\"lsType\":\"name\",\"preferred\":true,\"recordedBy\":\"goshiro\"}],\"lsTransaction\":8,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_gx depot\",\"protocol\":{\"codeName\":\"PROT-00000001\",\"deleted\":false,\"id\":1,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":5,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"recordedBy\":\"smeyer\",\"recordedDate\":1425511298000,\"shortDescription\":\"gx depot\",\"version\":1},\"recordedBy\":\"goshiro\",\"shortDescription\":\"test dupe experiments\"}";
		Experiment experiment = Experiment.fromJsonToExperiment(json);
		try {
			Experiment savedExperiment = experimentService.saveLsExperiment(experiment);

		} catch (UniqueNameException e) {
			logger.info(e.toString());
			Assert.assertNotNull(e);
		} catch (NotFoundException e) {
			logger.info(e.toString());
			Assert.assertNotNull(e);
		}
	}

	@Test
	@Transactional
	// @Rollback(value=false)
	public void saveExperimentReuseAGTest() throws UniqueNameException {
		String json = "{\"protocol\":{\"codeName\":\"PROT-00000001\",\"deleted\":false,\"id\":1,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[{\"deleted\":false,\"id\":1,\"ignored\":false,\"labelText\":\"TEST\",\"lsKind\":\"protocol name\",\"lsTransaction\":5,\"lsType\":\"name\",\"lsTypeAndKind\":\"name_protocol name\",\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"smeyer\",\"recordedDate\":1425511298000,\"version\":0}],\"lsTags\":[],\"lsTransaction\":5,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"recordedBy\":\"smeyer\",\"recordedDate\":1425511298000,\"shortDescription\":\"gx depot\",\"version\":1},\"lsType\":\"default\",\"lsKind\":\"differential expression analysis\",\"shortDescription\":\"GUY TESTING 201\",\"lsTransaction\":3397,\"recordedBy\":\"cs_rest\",\"recordedDate\":1433364521000,\"modifiedBy\":\"cs_rest\",\"modifiedDate\":1433364521000,\"lsLabels\":[{\"experiment\":null,\"labelText\":\"GUY TESTING 201\",\"recordedBy\":\"goshiro\",\"lsType\":\"name\",\"lsKind\":\"experiment name\",\"preferred\":true,\"ignored\":false,\"lsTransaction\":3397,\"recordedDate\":1433364521000}],\"lsStates\":[{\"experiment\":null,\"lsValues\":[{\"lsState\":null,\"lsType\":\"codeValue\",\"lsKind\":\"project\",\"stringValue\":\"12291\",\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"concentration\":null,\"concUnit\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"codeOrigin\":null,\"codeType\":null,\"codeKind\":null,\"recordedBy\":\"cs_rest\",\"recordedDate\":1433364521000,\"lsTransaction\":3397},{\"lsState\":null,\"lsType\":\"stringValue\",\"lsKind\":\"experiment name\",\"stringValue\":\"NS1h_vs_all\",\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"concentration\":null,\"concUnit\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"codeOrigin\":null,\"codeType\":null,\"codeKind\":null,\"recordedBy\":\"cs_rest\",\"recordedDate\":1433364521000,\"lsTransaction\":3397},{\"lsState\":null,\"lsType\":\"stringValue\",\"lsKind\":\"parent experiment name\",\"stringValue\":\"RSC B without CI\",\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"concentration\":null,\"concUnit\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"codeOrigin\":null,\"codeType\":null,\"codeKind\":null,\"recordedBy\":\"cs_rest\",\"recordedDate\":1433364521000,\"lsTransaction\":3397},{\"lsState\":null,\"lsType\":\"stringValue\",\"lsKind\":\"master experiment name\",\"stringValue\":\"MINI Contextual Fear Conditioning B_3 -- retrosplenial cortex mouse n=4 v4\",\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"concentration\":null,\"concUnit\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"codeOrigin\":null,\"codeType\":null,\"codeKind\":null,\"recordedBy\":\"cs_rest\",\"recordedDate\":1433364521000,\"lsTransaction\":3397},{\"lsState\":null,\"lsType\":\"codeValue\",\"lsKind\":\"analysis program\",\"stringValue\":null,\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"concentration\":null,\"concUnit\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":\"DESeq2\",\"codeOrigin\":null,\"codeType\":null,\"codeKind\":null,\"recordedBy\":\"cs_rest\",\"recordedDate\":1433364521000,\"lsTransaction\":3397},{\"lsState\":null,\"lsType\":\"codeValue\",\"lsKind\":\"analysis type\",\"stringValue\":null,\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"concentration\":null,\"concUnit\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":\"differential expression\",\"codeOrigin\":null,\"codeType\":null,\"codeKind\":null,\"recordedBy\":\"cs_rest\",\"recordedDate\":1433364521000,\"lsTransaction\":3397},{\"lsState\":null,\"lsType\":\"stringValue\",\"lsKind\":\"experimental condition\",\"stringValue\":\"NS1h\",\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"concentration\":null,\"concUnit\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"codeOrigin\":null,\"codeType\":null,\"codeKind\":null,\"recordedBy\":\"cs_rest\",\"recordedDate\":1433364521000,\"lsTransaction\":3397},{\"lsState\":null,\"lsType\":\"stringValue\",\"lsKind\":\"control condition\",\"stringValue\":\"US3h\",\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"concentration\":null,\"concUnit\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"codeOrigin\":null,\"codeType\":null,\"codeKind\":null,\"recordedBy\":\"cs_rest\",\"recordedDate\":1433364521000,\"lsTransaction\":3397}],\"recordedBy\":\"cs_rest\",\"lsType\":\"metadata\",\"lsKind\":\"experiment metadata\",\"comments\":\"\",\"lsTransaction\":3397,\"ignored\":false,\"recordedDate\":1433364521000},{\"experiment\":null,\"lsValues\":[],\"recordedBy\":\"cs_rest\",\"lsType\":\"data\",\"lsKind\":\"results\",\"comments\":\"\",\"lsTransaction\":3397,\"ignored\":false,\"recordedDate\":1433364521000}],\"lsTags\":[],\"analysisGroups\":[{\"codeName\":\"AG-00004584\",\"deleted\":false,\"id\":12316,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":3211,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1433360346409,\"recordedBy\":\"userName\",\"recordedDate\":1433267698000},{\"codeName\":\"AG-00004598\",\"deleted\":false,\"id\":12330,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":3211,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1433360346977,\"recordedBy\":\"userName\",\"recordedDate\":1433267712000},{\"codeName\":\"AG-00004573\",\"deleted\":false,\"id\":12305,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":3211,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1433360346867,\"recordedBy\":\"userName\",\"recordedDate\":1433267687000},{\"codeName\":\"AG-00004561\",\"deleted\":false,\"id\":12293,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":3211,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1433360346491,\"recordedBy\":\"userName\",\"recordedDate\":1433267676000},{\"codeName\":\"AG-00004565\",\"deleted\":false,\"id\":12297,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":3211,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1433360346789,\"recordedBy\":\"userName\",\"recordedDate\":1433267680000},{\"codeName\":\"AG-00004566\",\"deleted\":false,\"id\":12298,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":3211,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1433360346352,\"recordedBy\":\"userName\",\"recordedDate\":1433267681000},{\"codeName\":\"AG-00004579\",\"deleted\":false,\"id\":12311,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":3211,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1433360346281,\"recordedBy\":\"userName\",\"recordedDate\":1433267693000},{\"codeName\":\"AG-00004581\",\"deleted\":false,\"id\":12313,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":3211,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1433360346316,\"recordedBy\":\"userName\",\"recordedDate\":1433267695000},{\"codeName\":\"AG-00004568\",\"deleted\":false,\"id\":12300,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":3211,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1433360346232,\"recordedBy\":\"userName\",\"recordedDate\":1433267683000},{\"codeName\":\"AG-00004589\",\"deleted\":false,\"id\":12321,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":3211,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1433360346691,\"recordedBy\":\"userName\",\"recordedDate\":1433267703000},{\"codeName\":\"AG-00004587\",\"deleted\":false,\"id\":12319,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":3211,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1433360346555,\"recordedBy\":\"userName\",\"recordedDate\":1433267701000},{\"codeName\":\"AG-00004591\",\"deleted\":false,\"id\":12323,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":3211,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1433360346370,\"recordedBy\":\"userName\",\"recordedDate\":1433267705000},{\"codeName\":\"AG-00004597\",\"deleted\":false,\"id\":12329,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":3211,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1433360346429,\"recordedBy\":\"userName\",\"recordedDate\":1433267711000},{\"codeName\":\"AG-00004594\",\"deleted\":false,\"id\":12326,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":3211,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1433360346599,\"recordedBy\":\"userName\",\"recordedDate\":1433267708000},{\"codeName\":\"AG-00004596\",\"deleted\":false,\"id\":12328,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":3211,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1433360346764,\"recordedBy\":\"userName\",\"recordedDate\":1433267710000},{\"codeName\":\"AG-00004578\",\"deleted\":false,\"id\":12310,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":3211,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1433360346513,\"recordedBy\":\"userName\",\"recordedDate\":1433267692000},{\"codeName\":\"AG-00004592\",\"deleted\":false,\"id\":12324,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":3211,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1433360346922,\"recordedBy\":\"userName\",\"recordedDate\":1433267706000},{\"codeName\":\"AG-00004574\",\"deleted\":false,\"id\":12306,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":3211,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1433360346715,\"recordedBy\":\"userName\",\"recordedDate\":1433267688000},{\"codeName\":\"AG-00004595\",\"deleted\":false,\"id\":12327,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":3211,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1433360346645,\"recordedBy\":\"userName\",\"recordedDate\":1433267709000},{\"codeName\":\"AG-00004600\",\"deleted\":false,\"id\":12332,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":3211,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1433360346469,\"recordedBy\":\"userName\",\"recordedDate\":1433267714000},{\"codeName\":\"AG-00004563\",\"deleted\":false,\"id\":12295,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":3211,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1433360346668,\"recordedBy\":\"userName\",\"recordedDate\":1433267678000},{\"codeName\":\"AG-00004583\",\"deleted\":false,\"id\":12315,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":3211,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1433360346949,\"recordedBy\":\"userName\",\"recordedDate\":1433267697000},{\"codeName\":\"AG-00004572\",\"deleted\":false,\"id\":12304,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":3211,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1433360346184,\"recordedBy\":\"userName\",\"recordedDate\":1433267686000},{\"codeName\":\"AG-00004576\",\"deleted\":false,\"id\":12308,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":3211,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1433360346739,\"recordedBy\":\"userName\",\"recordedDate\":1433267690000},{\"codeName\":\"AG-00004590\",\"deleted\":false,\"id\":12322,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":3211,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1433360346215,\"recordedBy\":\"userName\",\"recordedDate\":1433267704000},{\"codeName\":\"AG-00004569\",\"deleted\":false,\"id\":12301,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":3211,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1433360347005,\"recordedBy\":\"userName\",\"recordedDate\":1433267684000},{\"codeName\":\"AG-00004575\",\"deleted\":false,\"id\":12307,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":3211,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1433360346577,\"recordedBy\":\"userName\",\"recordedDate\":1433267689000},{\"codeName\":\"AG-00004588\",\"deleted\":false,\"id\":12320,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":3211,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1433360346159,\"recordedBy\":\"userName\",\"recordedDate\":1433267702000},{\"codeName\":\"AG-00004586\",\"deleted\":false,\"id\":12318,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":3211,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1433360346816,\"recordedBy\":\"userName\",\"recordedDate\":1433267700000},{\"codeName\":\"AG-00004593\",\"deleted\":false,\"id\":12325,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":3211,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1433360346264,\"recordedBy\":\"userName\",\"recordedDate\":1433267707000},{\"codeName\":\"AG-00004571\",\"deleted\":false,\"id\":12303,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":3211,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1433360346449,\"recordedBy\":\"userName\",\"recordedDate\":1433267686000},{\"codeName\":\"AG-00004564\",\"deleted\":false,\"id\":12296,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":3211,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1433360346200,\"recordedBy\":\"userName\",\"recordedDate\":1433267679000},{\"codeName\":\"AG-00004567\",\"deleted\":false,\"id\":12299,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":3211,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1433360346895,\"recordedBy\":\"userName\",\"recordedDate\":1433267682000},{\"codeName\":\"AG-00004570\",\"deleted\":false,\"id\":12302,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":3211,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1433360346841,\"recordedBy\":\"userName\",\"recordedDate\":1433267685000},{\"codeName\":\"AG-00004577\",\"deleted\":false,\"id\":12309,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":3211,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1433360346298,\"recordedBy\":\"userName\",\"recordedDate\":1433267691000},{\"codeName\":\"AG-00004580\",\"deleted\":false,\"id\":12312,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":3211,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1433360346248,\"recordedBy\":\"userName\",\"recordedDate\":1433267694000},{\"codeName\":\"AG-00004582\",\"deleted\":false,\"id\":12314,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":3211,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1433360346334,\"recordedBy\":\"userName\",\"recordedDate\":1433267696000},{\"codeName\":\"AG-00004585\",\"deleted\":false,\"id\":12317,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":3211,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1433360346534,\"recordedBy\":\"userName\",\"recordedDate\":1433267699000},{\"codeName\":\"AG-00004562\",\"deleted\":false,\"id\":12294,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":3211,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1433360346389,\"recordedBy\":\"userName\",\"recordedDate\":1433267677000},{\"codeName\":\"AG-00004599\",\"deleted\":false,\"id\":12331,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsTransaction\":3211,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1433360346622,\"recordedBy\":\"userName\",\"recordedDate\":1433267713000}]}";
		try {
			test(2, json);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void test(final int threadCount, final String json) throws InterruptedException, ExecutionException {
		Callable<Long> task = new Callable<Long>() {
			@Override
			public Long call() throws UniqueNameException, NotFoundException {
				Experiment experiment = Experiment.fromJsonToExperiment(json);
				Experiment savedExperiment = experimentService.saveLsExperiment(experiment);
				// Set<AnalysisGroup> inputAnalysisGroups = new HashSet<AnalysisGroup>();
				for (AnalysisGroup analysisGroup : experiment.getAnalysisGroups()) {
					// inputAnalysisGroups.add(AnalysisGroup.findAnalysisGroup(analysisGroup.getId()));
					logger.info("Saving AG in hash: " + analysisGroup.getId());
					analysisGroup.getExperiments().add(savedExperiment);
					AnalysisGroup updatedAnlysisGroup = analysisGroupService.saveLsAnalysisGroup(analysisGroup);
				}

				// for(AnalysisGroup analysisGroup : inputAnalysisGroups){
				// analysisGroup.getExperiments().add(savedExperiment);
				// try {
				// logger.info("Attempting to save: " + analysisGroup.getId());
				// analysisGroupService.saveLsAnalysisGroup(analysisGroup);
				// } catch (Exception e){
				// logger.error(e.toString());
				// }
				// }

				return null;
			}
		};
		List<Callable<Long>> tasks = Collections.nCopies(threadCount, task);
		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
		List<Future<Long>> futures = executorService.invokeAll(tasks);
		List<Long> resultList = new ArrayList<Long>(futures.size());
		// Check for exceptions
		// for (Future<Long> future : futures) {
		// // Throws an exception if an exception was thrown by the task.
		// resultList.add(future.get());
		// }
		// Validate the IDs
		// Assert.assertEquals(threadCount, futures.size());
		// List<Long> expectedList = new ArrayList<Long>(threadCount);
		// for (long i = 1; i <= threadCount; i++) {
		// expectedList.add(i);
		// }
		// Collections.sort(resultList);
		// Assert.assertEquals(expectedList, resultList);
	}
}
