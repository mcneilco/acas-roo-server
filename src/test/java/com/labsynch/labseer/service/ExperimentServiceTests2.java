

package com.labsynch.labseer.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.AnalysisGroupValue;
import com.labsynch.labseer.domain.ItxProtocolProtocol;
import com.labsynch.labseer.domain.Protocol;
import com.labsynch.labseer.dto.AnalysisGroupValueDTO;
import com.labsynch.labseer.dto.BatchCodeDTO;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.ExperimentFilterDTO;
import com.labsynch.labseer.dto.ExperimentFilterSearchDTO;
import com.labsynch.labseer.dto.ExperimentSearchRequestDTO;
import com.labsynch.labseer.dto.JSTreeNodeDTO;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class ExperimentServiceTests2 {

	private static final Logger logger = LoggerFactory.getLogger(ExperimentServiceTests2.class);

	@Autowired
	private ExperimentService experimentService;

	//@Test
	@Transactional
	public void GetExprerimentNodes_4(){

		Protocol protocol = Protocol.findProtocol(46684L);
		logger.debug(protocol.toJson());
		if (ItxProtocolProtocol.findItxProtocolProtocolsBySecondProtocol(protocol).getResultList().size() > 0){
			List<String> protocolNodeList = new ArrayList<String>();
			List<String> nodeList = lookupProtocolTree(protocol, protocolNodeList);
			for (String node : nodeList){
				logger.debug("here is the node: " + node);
			}
		} else {
			logger.debug("this protocol is not attached by a protocol tree. set to default node");
		}
	}

	//a recursive function to walk up the protocol tree
	private List<String> lookupProtocolTree(Protocol protocol, List<String> protocolNodeList) {
		List<ItxProtocolProtocol> ipps = ItxProtocolProtocol.findItxProtocolProtocolsBySecondProtocol(protocol).getResultList();
		if (ipps.size() > 1){
			logger.error("ERROR: there is more than a single protocol interacation. " + ipps.size());
		}
		for (ItxProtocolProtocol ipp : ipps){
			if (ipp.getFirstProtocol().getLsType().equalsIgnoreCase("protocolTree")){
				protocolNodeList.add(ipp.getFirstProtocol().getLsKind());
				lookupProtocolTree(ipp.getFirstProtocol(), protocolNodeList);
			}
		}
		return protocolNodeList;
	}

	//@Test
	@Transactional
	public void GetExprerimentNodes_3(){

		Collection<JSTreeNodeDTO> output = experimentService.getExperimentNodes(null);
		logger.debug("here is the output: " + output);

	}

	@SuppressWarnings({ "unchecked", "null" })
	@Test
	@Transactional
	public void GetExprerimentsFilters_2(){

		//	 converted json: {"advancedFilter":"","batchCodeList":["GENE-000013","GENE-000011","GENE-000002","GENE-000003"],"booleanFilter":"and","experimentCodeList":["EXPT-00000002","default","tags_EXPT-00000002","tags_EXPT-0013","tags_EXPT-0014","PROT-00000002","EXPT-0014","Root Node","EXPT-0013"],"searchFilters":[{"codeName":null,"experimentCode":"EXPT-0013","experimentId":null,"filterValue":"yes","lsKind":"24h hit","lsType":"stringValue","operator":"equals","queryId":null,"termName":"Q1"}]}


		Set<ExperimentFilterSearchDTO> searchFilters = new HashSet<ExperimentFilterSearchDTO>();
		ExperimentFilterSearchDTO searchFilter = new ExperimentFilterSearchDTO(); 
		searchFilter.setExperimentCode("EXPT-00003");
		searchFilter.setLsType("numericValue");
		searchFilter.setLsKind("Shapira hit");
		searchFilter.setOperator("=");
		searchFilter.setFilterValue("1");
		logger.info(searchFilter.toJson());
		searchFilters.add(searchFilter);

				ExperimentFilterSearchDTO searchFilter2 = new ExperimentFilterSearchDTO(); 
				searchFilter2.setExperimentCode("EXPT-00000002");
				searchFilter2.setLsType("numericValue");
				searchFilter2.setLsKind("18h Z-score diff.");
				searchFilter2.setOperator(">");
				searchFilter2.setFilterValue("2");
				logger.info(searchFilter2.toJson());
				searchFilters.add(searchFilter2);


		Set<String> batchCodeList = new HashSet<String>();
//				batchCodeList.add("GENE-008126");
//				batchCodeList.add("GENE-000008");
//				batchCodeList.add("GENE-000018");		
//				batchCodeList.add("GENE-000016");
		//		batchCodeList.add("GENE-000002");
		//		batchCodeList.add("GENE-000003");

		Set<String> experimentCodeList = new HashSet<String>();
		experimentCodeList.add("EXPT-00000002");
		experimentCodeList.add("EXPT-00003");
		experimentCodeList.add("EXPT-00004");
		//		experimentCodeList.add("EXPT-00000316");
		//		experimentCodeList.add("EXPT-00000441");


		String booleanFilter = "ADVANCED"; //choices AND, OR, ADVANCED
//		String advancedFilter = "((Q1 and Q2 and Q3) or (Q4 and Q5)) OR (Q6)"; //choices AND, OR, ADVANCED
		String advancedFilter = "(Q1 and Q2)"; //choices AND, OR, ADVANCED

//		String advancedSQL = "select distinct tested_lot from api_experiment_results where numeric_value > 3";
		String advancedSQL = "select distinct tested_lot from ((select tested_lot from api_experiment_results where experiment_id = '46693' " +
							"and ls_kind = '18h Z-score diff.' and numeric_value > 1) intersect (select tested_lot from api_experiment_results where " +
							"ls_kind = 'Shapira hit' and numeric_value = 1)) temp";

		ExperimentSearchRequestDTO searchRequest = new ExperimentSearchRequestDTO();
		searchRequest.setAdvancedFilter(advancedFilter);
		searchRequest.setAdvancedFilterSQL(advancedSQL);
		searchRequest.setBooleanFilter(booleanFilter);
		searchRequest.setBatchCodeList(batchCodeList);
		searchRequest.setExperimentCodeList(experimentCodeList);
		searchRequest.setSearchFilters(searchFilters);
		logger.info(searchRequest.toJson());

		//		List<AnalysisGroupValueDTO> results = experimentService.getFilteredAGData(searchRequest);
		//		logger.info("number of values found: " + results.size());
		//logger.info(AnalysisGroupValueDTO.toPrettyJsonArray(results));

		Set<String> uniqueBatchCodes = new HashSet<String>();
		//        List<String> batchCodes = AnalysisGroupValue.findBatchCodeBySearchFilters(searchRequest.getBatchCodeList(), searchRequest.getExperimentCodeList(), searchRequest.getSearchFilters()).getResultList();

		List<String> batchCodes = null;
		Collection<String> collectionOfCodes = null;


		boolean firstPass = true;		
		Collection<BatchCodeDTO> collectionOfBatchCodes = null;

		if (searchRequest.getBooleanFilter() != null && searchRequest.getBooleanFilter().equalsIgnoreCase("ADVANCED")){
			//DO SQL substitutions for now. Try to do something more elegant later
			collectionOfCodes = AnalysisGroupValue.findBatchCodeBySearchFilter(searchRequest.getAdvancedFilterSQL()).getResultList();
			logger.info("advance search returned number of codes: " + collectionOfCodes.size());
			//			collectionOfBatchCodes.removeAll(Collections.singleton(null));
			//			for (BatchCodeDTO batch : collectionOfBatchCodes){
			//				if (batch.getBatchCode() != null){
			//					collectionOfCodes.add(batch.getBatchCode());
			//				}
			//			}
		} else if (searchRequest.getBooleanFilter() != null){
			for (ExperimentFilterSearchDTO singleSearchFilter : searchFilters){
				if (firstPass){
					collectionOfCodes = AnalysisGroupValue.findBatchCodeBySearchFilter(searchRequest.getBatchCodeList(), searchRequest.getExperimentCodeList(), singleSearchFilter).getResultList();
					logger.info("size of firstBatchCodes: " + collectionOfCodes.size());
					firstPass = false;
				} else {
					batchCodes = AnalysisGroupValue.findBatchCodeBySearchFilter(searchRequest.getBatchCodeList(), searchRequest.getExperimentCodeList(), singleSearchFilter).getResultList();
					logger.info("size of firstBatchCodes: " + collectionOfCodes.size());
					logger.info("size of secondBatchCodes: " + batchCodes.size());

					if (booleanFilter.equalsIgnoreCase("AND")){
						collectionOfCodes = CollectionUtils.intersection(collectionOfCodes, batchCodes);
					} else {
						collectionOfCodes = CollectionUtils.union(collectionOfCodes, batchCodes);
					}
					logger.info("size of intersectCodes: " + collectionOfCodes.size());
				}
			}
		} else {
			logger.info("the searchRequest is null");
		}

		
		Set<String> finalUniqueBatchCodes = new HashSet<String>();


		if (collectionOfCodes != null){
			logger.debug("size of intersectCodes: " + collectionOfCodes.size());			
			finalUniqueBatchCodes.addAll(collectionOfCodes);
			logger.info("number of batchCodes found: " + finalUniqueBatchCodes.size());
		}


//		searchRequest.getBatchCodeList().removeAll(batchCodeList);
//		searchRequest.getBatchCodeList().addAll(finalUniqueBatchCodes);
//		searchRequest.getBatchCodeList().removeAll(Collections.singleton(null));

		logger.info("calling experiment service search: ");
		List<AnalysisGroupValueDTO> agValues = experimentService.getFilteredAGData(searchRequest);

		logger.info("number of agValues found: " + agValues.size());


		//        for (String bc : batchCodes){
		//        	logger.info(bc);
		//        }
		//        
		//        
		//        List<AnalysisGroupValueDTO> agValues;
		//		if (uniqueBatchCodes.size() > 0){
		//    		logger.debug("looking by experiment codes and batch codes in default AND block");
		//            agValues = AnalysisGroupValue.findAnalysisGroupValueDTO(uniqueBatchCodes, searchRequest.getExperimentCodeList()).getResultList();
		//        } else {
		//    		logger.debug("looking by experiment codes only");
		//            agValues = AnalysisGroupValue.findAnalysisGroupValueDTOByExperiments(searchRequest.getExperimentCodeList()).getResultList();	
		//        }
		//        logger.info("number of agValues found: " + agValues.size());
		//
		//		
		//		if (booleanFilter.equalsIgnoreCase("ADVANCED")){
		//			// not implemented yet -- do same as AND for now
		//			
		//		} else if (booleanFilter.equalsIgnoreCase("OR")){
		//			
		//		} else {
		//			//default is the AND
		//			
		//		}

		//		List<String> batchCodes = AnalysisGroupValue.findBatchCodeBySearchFilters(batchCodeList, experimentCodeList, searchFilters).getResultList();
		//
		//		
		////		List<String> batchCodes = AnalysisGroupValue.findBatchCodedBySearchFilter(searchFilters).getResultList();
		//		logger.info("********** number of batches found:   " + batchCodes.size());
		//		for(String batch : batchCodes){
		//			//String codeValue = (String) batch.get(0);
		//			logger.info("------------------------------------here is the codeValue: " + batch);
		//		}
		//		
		//		List<String> batchCodesOR = AnalysisGroupValue.findBatchCodeBySearchFilter(batchCodeList, experimentCodeList, searchFilter).getResultList();
		//		logger.info("********** number of batches found:   " + batchCodesOR.size());
		//		for(String batch : batchCodesOR){
		//			logger.info("------------------------------------here is the codeValue: " + batch);
		//		}
		//		
		//		List<String> batchList = new ArrayList<String>();
		//		batchList.addAll(batchCodeList);
		//		
		//		//List<AnalysisGroupValueDTO> agValues = AnalysisGroupValue.findAnalysisGroupValueDTO(batchList).getResultList();
		//		List<AnalysisGroupValueDTO> agValues = AnalysisGroupValue.findAnalysisGroupValueDTO(batchCodeList, experimentCodeList).getResultList();
		//		logger.info("********** number of AnalysisGroupValueDTO found:   " + agValues.size());
		//		for(AnalysisGroupValueDTO agValue : agValues){
		//			logger.info("------------------------------------here is the agValues: " + agValue);
		//		}


	}

	//@Test
	@Transactional
	public void GetExprerimentsFilters_1(){
		Collection<String> codeValues = new HashSet<String>();
		codeValues.add("EXPT-00000314");
		codeValues.add("EXPT-00000002-testingPut-101");

		Collection<CodeTableDTO> codeTables = new HashSet<CodeTableDTO>();
		for (String code : codeValues){
			CodeTableDTO ct = new CodeTableDTO();
			ct.setCode(code);
			ct.setName(code);
			ct.setCodeName(code);
			codeTables.add(ct);
		}

		Collection<ExperimentFilterDTO> results = experimentService.getExperimentFilters(codeValues);

		logger.info(ExperimentFilterDTO.toPrettyJsonArray(results));


	}

	@SuppressWarnings("unchecked")
	//@Test
	@Transactional
	public void GetExprerimentsWithBatchCodes_1(){
		Collection<String> codeValues = new HashSet<String>();
		//		codeValues.add("CMPD-0000124-01");
		//		codeValues.add("CMPD-0000125-01");
		//		codeValues.add("GENE-000002");
		//		codeValues.add("GENE-000017");

		Collection<JSTreeNodeDTO> results = experimentService.getExperimentNodes(codeValues);

		logger.info(JSTreeNodeDTO.toPrettyJsonArray(results));
	}


	//@Test
	public void hydrateJson_test(){
		String josn = "{\"protocol\":{\"codeName\":\"PROT-00000026\",\"id\":484909,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[{\"id\":11714,\"ignored\":false,\"labelText\":\"APMS\",\"lsKind\":\"protocol name\",\"lsTransaction\":302,\"lsType\":\"name\",\"lsTypeAndKind\":\"name_protocol name\",\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"nouser\",\"recordedDate\":1393216025000,\"version\":0}],\"lsStates\":[],\"lsTags\":[],\"lsTransaction\":302,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"recordedBy\":\"nouser\",\"recordedDate\":1393216025000,\"shortDescription\":\"protocol created by generic data parser\",\"version\":1},\"codeName\":\"EXPT-00000389\",\"lsType\":\"default\",\"lsKind\":\"default\",\"shortDescription\":\"NA\",\"recordedBy\":\"nouser\",\"lsTransaction\":306,\"lsLabels\":[{\"experiment\":null,\"labelText\":\"Sample AMPS Expt 1001-V2\",\"recordedBy\":\"nouser\",\"lsType\":\"name\",\"lsKind\":\"experiment name\",\"preferred\":true,\"ignored\":false,\"lsTransaction\":306,\"recordedDate\":1393218563000}],\"lsStates\":[{\"experiment\":null,\"lsValues\":[{\"lsState\":null,\"lsType\":\"stringValue\",\"lsKind\":\"notebook\",\"stringValue\":\"JAM-000033\",\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"recordedBy\":\"nouser\",\"recordedDate\":1393218563000,\"lsTransaction\":306},{\"lsState\":null,\"lsType\":\"stringValue\",\"lsKind\":\"notebook page\",\"stringValue\":\"6\",\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"recordedBy\":\"nouser\",\"recordedDate\":1393218563000,\"lsTransaction\":306},{\"lsState\":null,\"lsType\":\"dateValue\",\"lsKind\":\"completion date\",\"stringValue\":null,\"fileValue\":null,\"urlValue\":null,\"dateValue\":1373270400000,\"clobValue\":null,\"blobValue\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"recordedBy\":\"nouser\",\"recordedDate\":1393218563000,\"lsTransaction\":306},{\"lsState\":null,\"lsType\":\"stringValue\",\"lsKind\":\"scientist\",\"stringValue\":\"jmcneil\",\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"recordedBy\":\"nouser\",\"recordedDate\":1393218563000,\"lsTransaction\":306},{\"lsState\":null,\"lsType\":\"stringValue\",\"lsKind\":\"status\",\"stringValue\":\"Approved\",\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"recordedBy\":\"nouser\",\"recordedDate\":1393218563000,\"lsTransaction\":306},{\"lsState\":null,\"lsType\":\"stringValue\",\"lsKind\":\"analysis status\",\"stringValue\":\"running\",\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"recordedBy\":\"nouser\",\"recordedDate\":1393218563000,\"lsTransaction\":306},{\"lsState\":null,\"lsType\":\"clobValue\",\"lsKind\":\"analysis result html\",\"stringValue\":null,\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":\"<p>Analysis not yet completed</p>\",\"blobValue\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"recordedBy\":\"nouser\",\"recordedDate\":1393218563000,\"lsTransaction\":306},{\"lsState\":null,\"lsType\":\"codeValue\",\"lsKind\":\"project\",\"stringValue\":null,\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":\"Fluomics Project 3\",\"recordedBy\":\"nouser\",\"recordedDate\":1393218563000,\"lsTransaction\":306}],\"recordedBy\":\"nouser\",\"lsType\":\"metadata\",\"lsKind\":\"experiment metadata\",\"comments\":\"\",\"lsTransaction\":306,\"ignored\":false,\"recordedDate\":1393218563000}],\"lsTags\":[{\"tagText\":[\"apple\",\"banana\"],\"id\":null,\"version\":null,\"recordedDate\":1393218563000}],\"recordedDate\":1393218563000}";
	}

}
