package com.labsynch.labseer.dto;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.labsynch.labseer.domain.SubjectValue;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class RawCurveDataDTO {
	
	private static final Logger logger = LoggerFactory.getLogger(RawCurveDataDTO.class);

	public RawCurveDataDTO(){
		//empty constructor
	}
	
	public RawCurveDataDTO(String curveId) {
		this.curveId = curveId;
	}
	
	public RawCurveDataDTO(String curveId, Map dataMap) {
		this.curveId = curveId;
		this.recordedBy = (String) dataMap.get("recordedBy");
		this.lsTransaction = (Long) dataMap.get("lsTransaction");
		this.responseSubjectValueId = (Long) dataMap.get("responseSubjectValueId");
		this.response = (BigDecimal) dataMap.get("response");
		this.responseKind = (String) dataMap.get("responseKind");
		this.responseUnits = (String) dataMap.get("responseUnits");
		try {
			this.dose = (BigDecimal) dataMap.get("dose");
		}catch(ClassCastException e){
			this.dose = BigDecimal.valueOf((Double) dataMap.get("dose"));
		}
		this.doseUnits = (String) dataMap.get("doseUnits");
		this.algorithmFlagStatus = (String) dataMap.get("algorithmFlagStatus");
		this.algorithmFlagObservation = (String) dataMap.get("algorithmFlagObservation");
		this.algorithmFlagCause = (String) dataMap.get("algorithmFlagCause");
		this.algorithmFlagComment = (String) dataMap.get("algorithmFlagComment");
		this.preprocessFlagStatus = (String) dataMap.get("preprocessFlagStatus");
		this.preprocessFlagObservation = (String) dataMap.get("preprocessFlagObservation");
		this.preprocessFlagCause = (String) dataMap.get("preprocessFlagCause");
		this.preprocessFlagComment = (String) dataMap.get("preprocessFlagComment");
		this.userFlagStatus = (String) dataMap.get("userFlagStatus");
		this.userFlagObservation = (String) dataMap.get("userFlagObservation");
		this.userFlagCause = (String) dataMap.get("userFlagCause");
		this.userFlagComment = (String) dataMap.get("userFlagComment");
	}
	
	public RawCurveDataDTO(Map dataMap) {
		this.curveId = (String) dataMap.get("curveId");
		this.recordedBy = (String) dataMap.get("recordedBy");
		this.lsTransaction = (Long) dataMap.get("lsTransaction");
		this.responseSubjectValueId = (Long) dataMap.get("responseSubjectValueId");
		this.response = (BigDecimal) dataMap.get("response");
		this.responseKind = (String) dataMap.get("responseKind");
		this.responseUnits = (String) dataMap.get("responseUnits");
		try {
			this.dose = (BigDecimal) dataMap.get("dose");
		}catch(ClassCastException e){
			this.dose = BigDecimal.valueOf((Double) dataMap.get("dose"));
		}
		this.doseUnits = (String) dataMap.get("doseUnits");
		this.algorithmFlagStatus = (String) dataMap.get("algorithmFlagStatus");
		this.algorithmFlagObservation = (String) dataMap.get("algorithmFlagObservation");
		this.algorithmFlagCause = (String) dataMap.get("algorithmFlagCause");
		this.algorithmFlagComment = (String) dataMap.get("algorithmFlagComment");
		this.preprocessFlagStatus = (String) dataMap.get("preprocessFlagStatus");
		this.preprocessFlagObservation = (String) dataMap.get("preprocessFlagObservation");
		this.preprocessFlagCause = (String) dataMap.get("preprocessFlagCause");
		this.preprocessFlagComment = (String) dataMap.get("preprocessFlagComment");
		this.userFlagStatus = (String) dataMap.get("userFlagStatus");
		this.userFlagObservation = (String) dataMap.get("userFlagObservation");
		this.userFlagCause = (String) dataMap.get("userFlagCause");
		this.userFlagComment = (String) dataMap.get("userFlagComment");
	}
	


	private String curveId; //location: provided
	private String recordedBy;
	private Long lsTransaction;
	private Long responseSubjectValueId; // location: subject value, SS: data_results, SV: numericValue_Response, id attribute
	private BigDecimal response; // location: same as responseSubjectValueId, but in numericValue field
	private String responseKind;
	private String responseUnits; //location same as response, but in unitKind field
	private BigDecimal dose; // location: subject value, SS: data_test compound treatment, SV: numericValue_Dose
	private String doseUnits; //location, same as above, but in unitKind field
	private String algorithmFlagStatus;
	private String algorithmFlagObservation;
	private String algorithmFlagCause;
	private String algorithmFlagComment;
	private String preprocessFlagStatus;
	private String preprocessFlagObservation;
	private String preprocessFlagCause;
	private String preprocessFlagComment;
	private String userFlagStatus;
	private String userFlagObservation;
	private String userFlagCause;
	private String userFlagComment;
	
	


	public static String[] getColumns(){
		String[] headerColumns = new String[] {
				"curveId",
				"recordedBy",
				"lsTransaction",
				"responseSubjectValueId",
				"dose",
				"doseUnits",
				"response",
				"responseKind",
				"responseUnits",
				"algorithmFlagStatus",
				"algorithmFlagObservation",
				"algorithmFlagCause",
				"algorithmFlagComment",
				"preprocessFlagStatus",
				"preprocessFlagObservation",
				"preprocessFlagCause",
				"preprocessFlagComment",
				"userFlagStatus",
				"userFlagObservation",
				"userFlagCause",
				"userFlagComment"
				};

		return headerColumns;

	}

	public static CellProcessor[] getProcessors() {
		final CellProcessor[] processors = new CellProcessor[] { 
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional()
		};

		return processors;
	}
	
	public static List<RawCurveDataDTO> getRawCurveData(Collection<String> curveIds, String responseName){
		EntityManager em = SubjectValue.entityManager();
		String queryString = "SELECT NEW MAP( rsv.id as responseSubjectValueId, "
				+ "rsv.recordedBy as recordedBy, "
				+ "rsv.lsTransaction as lsTransaction, "
        		+ "rsv.numericValue as response, "
        		+ "rsv.unitKind as responseUnits, "
        		+ "rsv.lsKind as responseKind, "
        		+ "bcsv.concentration as dose, "
        		+ "bcsv.concUnit as doseUnits, "
        		+ "afsv.codeValue as algorithmFlagStatus, "
        		+ "afov.codeValue as algorithmFlagObservation, "
        		+ "afcv.codeValue as algorithmFlagCause, "
        		+ "afcomv.stringValue as algorithmFlagComment, "
        		+ "pfsv.codeValue as preprocessFlagStatus, "
        		+ "pfov.codeValue as preprocessFlagObservation, "
        		+ "pfcv.codeValue as preprocessFlagCause, "
        		+ "afs.lsKind as algorithmFlagLsKind, "
        		+ "pfs.lsKind as preprocessFlagLsKind, "
        		+ "ufs.lsKind as userFlagLsKind, "
        		+ "pfcomv.stringValue as preprocessFlagComment, "
        		+ "ufsv.codeValue as userFlagStatus, "
        		+ "ufov.codeValue as userFlagObservation, "
        		+ "ufcv.codeValue as userFlagCause, "
        		+ "ufcomv.stringValue as userFlagComment,"
        		+ "agv.stringValue as curveId "
        		+ " ) " 
        		+ "FROM AnalysisGroupValue agv "
        		+ "JOIN agv.lsState as ags "
        		+ "JOIN ags.analysisGroup as ag "
        		+ "JOIN ags.analysisGroup.treatmentGroups as treat "
        		+ "JOIN treat.subjects as subj "
        		+ "JOIN subj.lsStates as rss "
        		+ "JOIN rss.lsValues as rsv "
        		+ "JOIN rss.lsValues as bcsv "
        		+ "LEFT JOIN subj.lsStates as afs WITH afs.lsKind = 'auto flag' AND afs.ignored = false "
        		+ "LEFT JOIN afs.lsValues as afsv WITH afsv.lsKind = 'flag status' "
        		+ "LEFT JOIN afs.lsValues as afov WITH afov.lsKind = 'flag observation' "
        		+ "LEFT JOIN afs.lsValues as afcv WITH afcv.lsKind = 'flag cause' "
        		+ "LEFT JOIN afs.lsValues as afcomv WITH afcomv.lsKind = 'comment' "
        		+ "LEFT JOIN subj.lsStates as pfs WITH pfs.lsKind = 'preprocess flag' AND pfs.ignored = false "
        		+ "LEFT JOIN pfs.lsValues as pfsv WITH pfsv.lsKind = 'flag status' "
        		+ "LEFT JOIN pfs.lsValues as pfov WITH pfov.lsKind = 'flag observation' "
        		+ "LEFT JOIN pfs.lsValues as pfcv WITH pfcv.lsKind = 'flag cause' "
        		+ "LEFT JOIN pfs.lsValues as pfcomv WITH pfcomv.lsKind = 'comment' "
        		+ "LEFT JOIN subj.lsStates as ufs WITH ufs.lsKind = 'user flag' AND ufs.ignored = false "
        		+ "LEFT JOIN ufs.lsValues as ufsv WITH ufsv.lsKind = 'flag status' "
        		+ "LEFT JOIN ufs.lsValues as ufov WITH ufov.lsKind = 'flag observation' "
        		+ "LEFT JOIN ufs.lsValues as ufcv WITH ufcv.lsKind = 'flag cause' "
        		+ "LEFT JOIN ufs.lsValues as ufcomv WITH ufcomv.lsKind = 'comment' "
        		+ "WHERE rss.lsType = 'data' "
        		+ "AND rss.lsKind = 'results' "
        		+ "AND rsv.lsType = 'numericValue' "
        		+ "AND rsv.lsKind = :responseKind "
        		+ "AND bcsv.lsType = 'codeValue' "
        		+ "AND bcsv.lsKind = 'batch code' "
        		+ "AND ags.ignored = false "
        		+ "AND agv.lsType = 'stringValue' "
        		+ "AND agv.lsKind = 'curve id' "
        		+ "AND agv.ignored = false "
        		+ "AND ag.ignored = false "
        		+ "AND treat.ignored = false "
        		+ "AND subj.ignored = false "
        		+ "AND (";
        Map<String, Collection<String>> sqlCurveIdMap = new HashMap<String, Collection<String>>();
    	List<String> allCurveIds = new ArrayList<String>();
    	allCurveIds.addAll(curveIds);
    	int startIndex = 0;
    	while (startIndex < curveIds.size()){
    		int endIndex;
    		if (startIndex+999 < curveIds.size()) endIndex = startIndex+999;
    		else endIndex = curveIds.size();
    		List<String> nextCurveIds = allCurveIds.subList(startIndex, endIndex);
    		String groupName = "curveIds"+startIndex;
    		String sqlClause = " agv.stringValue IN (:"+groupName+")";
    		sqlCurveIdMap.put(sqlClause, nextCurveIds);
    		startIndex=endIndex;
    	}
    	int numClause = 1;
    	for (String sqlClause : sqlCurveIdMap.keySet()){
    		if (numClause == 1){
    			queryString = queryString + sqlClause;
    		}else{
    			queryString = queryString + " OR " + sqlClause;
    		}
    		numClause++;
    	}
    	queryString = queryString + " )";
		TypedQuery<Map> q = em.createQuery(queryString, Map.class);
        for (String sqlClause : sqlCurveIdMap.keySet()){
        	String groupName = sqlClause.split(":")[1].replace(")","");
        	q.setParameter(groupName, sqlCurveIdMap.get(sqlClause));
        }
        logger.debug("Querying with string: \n"+queryString);
        if (responseName != null && responseName.length()>0){
            q.setParameter("responseKind", responseName);
        }
        else{
        	q.setParameter("responseKind", "efficacy");
        }
        List<Map> queryResults = q.getResultList();
        List<RawCurveDataDTO> rawCurveDataList = new ArrayList<RawCurveDataDTO>();
		for (Map result : queryResults) {
			RawCurveDataDTO rawCurveDataDTO = new RawCurveDataDTO(result);
			rawCurveDataList.add(rawCurveDataDTO);
		}
		return rawCurveDataList;
	}
	
	public static List<RawCurveDataDTO> getRawCurveDataAgonist(Collection<String> curveIds, String responseName){
		EntityManager em = SubjectValue.entityManager();
		String queryString = "SELECT NEW MAP( rsv.id as responseSubjectValueId, "
				+ "rsv.recordedBy as recordedBy, "
				+ "rsv.lsTransaction as lsTransaction, "
        		+ "rsv.numericValue as response, "
        		+ "rsv.unitKind as responseUnits, "
        		+ "rsv.lsKind as responseKind, "
        		+ "bcsv.concentration as dose, "
        		+ "bcsv.concUnit as doseUnits, "
        		+ "afsv.codeValue as algorithmFlagStatus, "
        		+ "afov.codeValue as algorithmFlagObservation, "
        		+ "afcv.codeValue as algorithmFlagCause, "
        		+ "afcomv.stringValue as algorithmFlagComment, "
        		+ "pfsv.codeValue as preprocessFlagStatus, "
        		+ "pfov.codeValue as preprocessFlagObservation, "
        		+ "pfcv.codeValue as preprocessFlagCause, "
        		+ "afs.lsKind as algorithmFlagLsKind, "
        		+ "pfs.lsKind as preprocessFlagLsKind, "
        		+ "ufs.lsKind as userFlagLsKind, "
        		+ "pfcomv.stringValue as preprocessFlagComment, "
        		+ "ufsv.codeValue as userFlagStatus, "
        		+ "ufov.codeValue as userFlagObservation, "
        		+ "ufcv.codeValue as userFlagCause, "
        		+ "ufcomv.stringValue as userFlagComment,"
        		+ "agv.stringValue as curveId "
        		+ " ) " 
        		+ "FROM AnalysisGroupValue agv "
        		+ "JOIN agv.lsState as ags "
        		+ "JOIN ags.analysisGroup as ag "
        		+ "JOIN ags.analysisGroup.treatmentGroups as treat "
        		+ "JOIN treat.subjects as subj "
        		+ "JOIN subj.lsStates as rss "
        		+ "JOIN rss.lsValues as rsv "
        		+ "JOIN rss.lsValues as bcsv "
        		+ "LEFT JOIN subj.lsStates as afs WITH afs.lsKind = 'auto flag' AND afs.ignored = false "
        		+ "LEFT JOIN afs.lsValues as afsv WITH afsv.lsKind = 'flag status' "
        		+ "LEFT JOIN afs.lsValues as afov WITH afov.lsKind = 'flag observation' "
        		+ "LEFT JOIN afs.lsValues as afcv WITH afcv.lsKind = 'flag cause' "
        		+ "LEFT JOIN afs.lsValues as afcomv WITH afcomv.lsKind = 'comment' "
        		+ "LEFT JOIN subj.lsStates as pfs WITH pfs.lsKind = 'preprocess flag' AND pfs.ignored = false "
        		+ "LEFT JOIN pfs.lsValues as pfsv WITH pfsv.lsKind = 'flag status' "
        		+ "LEFT JOIN pfs.lsValues as pfov WITH pfov.lsKind = 'flag observation' "
        		+ "LEFT JOIN pfs.lsValues as pfcv WITH pfcv.lsKind = 'flag cause' "
        		+ "LEFT JOIN pfs.lsValues as pfcomv WITH pfcomv.lsKind = 'comment' "
        		+ "LEFT JOIN subj.lsStates as ufs WITH ufs.lsKind = 'user flag' AND ufs.ignored = false "
        		+ "LEFT JOIN ufs.lsValues as ufsv WITH ufsv.lsKind = 'flag status' "
        		+ "LEFT JOIN ufs.lsValues as ufov WITH ufov.lsKind = 'flag observation' "
        		+ "LEFT JOIN ufs.lsValues as ufcv WITH ufcv.lsKind = 'flag cause' "
        		+ "LEFT JOIN ufs.lsValues as ufcomv WITH ufcomv.lsKind = 'comment' "
        		+ "WHERE rss.lsType = 'data' "
        		+ "AND rss.lsKind = 'results' "
        		+ "AND rsv.lsType = 'numericValue' "
        		+ "AND rsv.lsKind = :responseKind "
        		+ "AND bcsv.lsType = 'codeValue' "
        		+ "AND bcsv.lsKind = 'agonist batch code' "
        		+ "AND ags.ignored = false "
        		+ "AND agv.lsType = 'stringValue' "
        		+ "AND agv.lsKind = 'curve id' "
        		+ "AND agv.ignored = false "
        		+ "AND ag.ignored = false "
        		+ "AND treat.ignored = false "
        		+ "AND subj.ignored = false "
        		+ "AND (";
        Map<String, Collection<String>> sqlCurveIdMap = new HashMap<String, Collection<String>>();
    	List<String> allCurveIds = new ArrayList<String>();
    	allCurveIds.addAll(curveIds);
    	int startIndex = 0;
    	while (startIndex < curveIds.size()){
    		int endIndex;
    		if (startIndex+999 < curveIds.size()) endIndex = startIndex+999;
    		else endIndex = curveIds.size();
    		List<String> nextCurveIds = allCurveIds.subList(startIndex, endIndex);
    		String groupName = "curveIds"+startIndex;
    		String sqlClause = " agv.stringValue IN (:"+groupName+")";
    		sqlCurveIdMap.put(sqlClause, nextCurveIds);
    		startIndex=endIndex;
    	}
    	int numClause = 1;
    	for (String sqlClause : sqlCurveIdMap.keySet()){
    		if (numClause == 1){
    			queryString = queryString + sqlClause;
    		}else{
    			queryString = queryString + " OR " + sqlClause;
    		}
    		numClause++;
    	}
    	queryString = queryString + " )";
		TypedQuery<Map> q = em.createQuery(queryString, Map.class);
        for (String sqlClause : sqlCurveIdMap.keySet()){
        	String groupName = sqlClause.split(":")[1].replace(")","");
        	q.setParameter(groupName, sqlCurveIdMap.get(sqlClause));
        }
        logger.debug("Querying with string: \n"+queryString);
        if (responseName != null && responseName.length()>0){
            q.setParameter("responseKind", responseName);
        }
        else{
        	q.setParameter("responseKind", "normalized activity");
        }
        List<Map> queryResults = q.getResultList();
        List<RawCurveDataDTO> rawCurveDataList = new ArrayList<RawCurveDataDTO>();
		for (Map result : queryResults) {
			RawCurveDataDTO rawCurveDataDTO = new RawCurveDataDTO(result);
			rawCurveDataList.add(rawCurveDataDTO);
		}
		return rawCurveDataList;
	}

	
	public static String getCsvList(Collection<RawCurveDataDTO> rawCurveDataDTOs, String format) {
		//format is ALWAYS tsv or csv (not case sensitive)
		StringWriter outFile = new StringWriter();
        ICsvBeanWriter beanWriter = null;
        try {
            if (format.equalsIgnoreCase("csv")) beanWriter = new CsvBeanWriter(outFile, CsvPreference.STANDARD_PREFERENCE);
            else beanWriter = new CsvBeanWriter(outFile, CsvPreference.TAB_PREFERENCE);
            final String[] header = RawCurveDataDTO.getColumns();
            final CellProcessor[] processors = RawCurveDataDTO.getProcessors();
            beanWriter.writeHeader(header);
            for (final RawCurveDataDTO rawCurveDataDTO : rawCurveDataDTOs) {
                beanWriter.write(rawCurveDataDTO, header, processors);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (beanWriter != null) {
                try {
                    beanWriter.close();
                    outFile.flush();
                    outFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return outFile.toString();
	}
	
	public static Collection<RawCurveDataDTO> getRawCurveDataByExperiment(String experimentIdOrCodeName, String responseName){
		long startTime = System.currentTimeMillis();
		Collection<String> curveIds = CurveFitDTO.findAllCurveIdsByExperiment(experimentIdOrCodeName);
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		logger.debug("time to get curve id list and rendering hint = " + totalTime + " miliseconds.");
		long startTime2 = System.currentTimeMillis();
		Collection<RawCurveDataDTO> rawCurveDataDTOs = getRawCurveData(curveIds, responseName);
		long endTime2 = System.currentTimeMillis();
		long totalTime2 = endTime2 - startTime2;
		logger.debug("time to fill in raw curve data = " + totalTime2 + " miliseconds.");
		return rawCurveDataDTOs;
	}
	
	public static Collection<RawCurveDataDTO> getRawAgonistCurveDataByExperiment(String experimentIdOrCodeName, String responseName){
		long startTime = System.currentTimeMillis();
		Collection<String> curveIds = CurveFitDTO.findAllCurveIdsByExperiment(experimentIdOrCodeName);
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		logger.debug("time to get curve id list and rendering hint = " + totalTime + " miliseconds.");
		long startTime2 = System.currentTimeMillis();
		Collection<RawCurveDataDTO> rawCurveDataDTOs = getRawCurveDataAgonist(curveIds, responseName);
		long endTime2 = System.currentTimeMillis();
		long totalTime2 = endTime2 - startTime2;
		logger.debug("time to fill in raw curve data = " + totalTime2 + " miliseconds.");
		return rawCurveDataDTOs;
	}


	public String getCurveId() {
        return this.curveId;
    }

	public void setCurveId(String curveId) {
        this.curveId = curveId;
    }

	public String getRecordedBy() {
        return this.recordedBy;
    }

	public void setRecordedBy(String recordedBy) {
        this.recordedBy = recordedBy;
    }

	public Long getLsTransaction() {
        return this.lsTransaction;
    }

	public void setLsTransaction(Long lsTransaction) {
        this.lsTransaction = lsTransaction;
    }

	public Long getResponseSubjectValueId() {
        return this.responseSubjectValueId;
    }

	public void setResponseSubjectValueId(Long responseSubjectValueId) {
        this.responseSubjectValueId = responseSubjectValueId;
    }

	public BigDecimal getResponse() {
        return this.response;
    }

	public void setResponse(BigDecimal response) {
        this.response = response;
    }

	public String getResponseKind() {
        return this.responseKind;
    }

	public void setResponseKind(String responseKind) {
        this.responseKind = responseKind;
    }

	public String getResponseUnits() {
        return this.responseUnits;
    }

	public void setResponseUnits(String responseUnits) {
        this.responseUnits = responseUnits;
    }

	public BigDecimal getDose() {
        return this.dose;
    }

	public void setDose(BigDecimal dose) {
        this.dose = dose;
    }

	public String getDoseUnits() {
        return this.doseUnits;
    }

	public void setDoseUnits(String doseUnits) {
        this.doseUnits = doseUnits;
    }

	public String getAlgorithmFlagStatus() {
        return this.algorithmFlagStatus;
    }

	public void setAlgorithmFlagStatus(String algorithmFlagStatus) {
        this.algorithmFlagStatus = algorithmFlagStatus;
    }

	public String getAlgorithmFlagObservation() {
        return this.algorithmFlagObservation;
    }

	public void setAlgorithmFlagObservation(String algorithmFlagObservation) {
        this.algorithmFlagObservation = algorithmFlagObservation;
    }

	public String getAlgorithmFlagCause() {
        return this.algorithmFlagCause;
    }

	public void setAlgorithmFlagCause(String algorithmFlagCause) {
        this.algorithmFlagCause = algorithmFlagCause;
    }

	public String getAlgorithmFlagComment() {
        return this.algorithmFlagComment;
    }

	public void setAlgorithmFlagComment(String algorithmFlagComment) {
        this.algorithmFlagComment = algorithmFlagComment;
    }

	public String getPreprocessFlagStatus() {
        return this.preprocessFlagStatus;
    }

	public void setPreprocessFlagStatus(String preprocessFlagStatus) {
        this.preprocessFlagStatus = preprocessFlagStatus;
    }

	public String getPreprocessFlagObservation() {
        return this.preprocessFlagObservation;
    }

	public void setPreprocessFlagObservation(String preprocessFlagObservation) {
        this.preprocessFlagObservation = preprocessFlagObservation;
    }

	public String getPreprocessFlagCause() {
        return this.preprocessFlagCause;
    }

	public void setPreprocessFlagCause(String preprocessFlagCause) {
        this.preprocessFlagCause = preprocessFlagCause;
    }

	public String getPreprocessFlagComment() {
        return this.preprocessFlagComment;
    }

	public void setPreprocessFlagComment(String preprocessFlagComment) {
        this.preprocessFlagComment = preprocessFlagComment;
    }

	public String getUserFlagStatus() {
        return this.userFlagStatus;
    }

	public void setUserFlagStatus(String userFlagStatus) {
        this.userFlagStatus = userFlagStatus;
    }

	public String getUserFlagObservation() {
        return this.userFlagObservation;
    }

	public void setUserFlagObservation(String userFlagObservation) {
        this.userFlagObservation = userFlagObservation;
    }

	public String getUserFlagCause() {
        return this.userFlagCause;
    }

	public void setUserFlagCause(String userFlagCause) {
        this.userFlagCause = userFlagCause;
    }

	public String getUserFlagComment() {
        return this.userFlagComment;
    }

	public void setUserFlagComment(String userFlagComment) {
        this.userFlagComment = userFlagComment;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static RawCurveDataDTO fromJsonToRawCurveDataDTO(String json) {
        return new JSONDeserializer<RawCurveDataDTO>()
        .use(null, RawCurveDataDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<RawCurveDataDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<RawCurveDataDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<RawCurveDataDTO> fromJsonArrayToRawCurveDataDTO(String json) {
        return new JSONDeserializer<List<RawCurveDataDTO>>()
        .use("values", RawCurveDataDTO.class).deserialize(json);
    }
}
