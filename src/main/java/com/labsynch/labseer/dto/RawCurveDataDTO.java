package com.labsynch.labseer.dto;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.labsynch.labseer.domain.AnalysisGroupState;
import com.labsynch.labseer.domain.AnalysisGroupValue;
import com.labsynch.labseer.domain.SubjectValue;

@RooJavaBean
@RooToString
@RooJson
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
		this.algorithmFlagReason = (String) dataMap.get("algorithmFlagReason");
		this.algorithmFlagComment = (String) dataMap.get("algorithmFlagComment");
		this.preprocessFlagStatus = (String) dataMap.get("preprocessFlagStatus");
		this.preprocessFlagObservation = (String) dataMap.get("preprocessFlagObservation");
		this.preprocessFlagReason = (String) dataMap.get("preprocessFlagReason");
		this.preprocessFlagComment = (String) dataMap.get("preprocessFlagComment");
		this.userFlagStatus = (String) dataMap.get("userFlagStatus");
		this.userFlagObservation = (String) dataMap.get("userFlagObservation");
		this.userFlagReason = (String) dataMap.get("userFlagReason");
		this.userFlagComment = (String) dataMap.get("userFlagComment");
	}
	
	public RawCurveDataDTO(Map dataMap) {
		this.curveId = (String) dataMap.get("curveId");
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
		this.algorithmFlagReason = (String) dataMap.get("algorithmFlagReason");
		this.algorithmFlagComment = (String) dataMap.get("algorithmFlagComment");
		this.preprocessFlagStatus = (String) dataMap.get("preprocessFlagStatus");
		this.preprocessFlagObservation = (String) dataMap.get("preprocessFlagObservation");
		this.preprocessFlagReason = (String) dataMap.get("preprocessFlagReason");
		this.preprocessFlagComment = (String) dataMap.get("preprocessFlagComment");
		this.userFlagStatus = (String) dataMap.get("userFlagStatus");
		this.userFlagObservation = (String) dataMap.get("userFlagObservation");
		this.userFlagReason = (String) dataMap.get("userFlagReason");
		this.userFlagComment = (String) dataMap.get("userFlagComment");
	}
	


	private String curveId; //location: provided
	private Long responseSubjectValueId; // location: subject value, SS: data_results, SV: numericValue_Response, id attribute
	private BigDecimal response; // location: same as responseSubjectValueId, but in numericValue field
	private String responseKind;
	private String responseUnits; //location same as response, but in unitKind field
	private BigDecimal dose; // location: subject value, SS: data_test compound treatment, SV: numericValue_Dose
	private String doseUnits; //location, same as above, but in unitKind field
	private String algorithmFlagStatus;
	private String algorithmFlagObservation;
	private String algorithmFlagReason;
	private String algorithmFlagComment;
	private String preprocessFlagStatus;
	private String preprocessFlagObservation;
	private String preprocessFlagReason;
	private String preprocessFlagComment;
	private String userFlagStatus;
	private String userFlagObservation;
	private String userFlagReason;
	private String userFlagComment;
	
	


	public static String[] getColumns(){
		String[] headerColumns = new String[] {
				"curveId",
				"responseSubjectValueId",
				"dose",
				"doseUnits",
				"response",
				"responseKind",
				"responseUnits",
				"algorithmFlagStatus",
				"algorithmFlagObservation",
				"algorithmFlagReason",
				"algorithmFlagComment",
				"preprocessFlagStatus",
				"preprocessFlagObservation",
				"preprocessFlagReason",
				"preprocessFlagComment",
				"userFlagStatus",
				"userFlagObservation",
				"userFlagReason",
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
				new Optional()
		};

		return processors;
	}
	
	public static List<RawCurveDataDTO> getRawCurveData(Collection<String> curveIds){
		EntityManager em = SubjectValue.entityManager();
		TypedQuery<Map> q = em.createQuery("SELECT NEW MAP( rsv.id as responseSubjectValueId, "
        		+ "rsv.numericValue as response, "
        		+ "rsv.unitKind as responseUnits, "
        		+ "rsv.lsKind as responseKind, "
        		+ "bcsv.concentration as dose, "
        		+ "bcsv.concUnit as doseUnits, "
        		+ "afsv.codeValue as algorithmFlagStatus, "
        		+ "afov.codeValue as algorithmFlagObservation, "
        		+ "afrv.codeValue as algorithmFlagReason, "
        		+ "afcv.stringValue as algorithmFlagComment, "
        		+ "pfsv.codeValue as preprocessFlagStatus, "
        		+ "pfov.codeValue as preprocessFlagObservation, "
        		+ "pfrv.codeValue as preprocessFlagReason, "
        		+ "afs.lsKind as algorithmFlagLsKind, "
        		+ "pfs.lsKind as preprocessFlagLsKind, "
        		+ "ufs.lsKind as userFlagLsKind, "
        		+ "pfcv.stringValue as preprocessFlagComment, "
        		+ "ufsv.codeValue as userFlagStatus, "
        		+ "ufov.codeValue as userFlagObservation, "
        		+ "ufrv.codeValue as userFlagReason, "
        		+ "ufcv.stringValue as userFlagComment,"
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
        		+ "LEFT JOIN afs.lsValues as afsv WITH afsv.lsKind = 'algorithm flag status' "
        		+ "LEFT JOIN afs.lsValues as afov WITH afov.lsKind = 'algorithm flag observation' "
        		+ "LEFT JOIN afs.lsValues as afrv WITH afrv.lsKind = 'algorithm flag reason' "
        		+ "LEFT JOIN afs.lsValues as afcv WITH afcv.lsKind = 'comment' "
        		+ "LEFT JOIN subj.lsStates as pfs WITH pfs.lsKind = 'preprocess flag' AND pfs.ignored = false "
        		+ "LEFT JOIN pfs.lsValues as pfsv WITH pfsv.lsKind = 'preprocess flag status' "
        		+ "LEFT JOIN pfs.lsValues as pfov WITH pfov.lsKind = 'preprocess flag observation' "
        		+ "LEFT JOIN pfs.lsValues as pfrv WITH pfrv.lsKind = 'preprocess flag reason' "
        		+ "LEFT JOIN pfs.lsValues as pfcv WITH pfcv.lsKind = 'comment' "
        		+ "LEFT JOIN subj.lsStates as ufs WITH ufs.lsKind = 'user flag' AND ufs.ignored = false "
        		+ "LEFT JOIN ufs.lsValues as ufsv WITH ufsv.lsKind = 'user flag status' "
        		+ "LEFT JOIN ufs.lsValues as ufov WITH ufov.lsKind = 'user flag observation' "
        		+ "LEFT JOIN ufs.lsValues as ufrv WITH ufrv.lsKind = 'user flag reason' "
        		+ "LEFT JOIN ufs.lsValues as ufcv WITH ufcv.lsKind = 'comment' "
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
        		+ "AND agv.stringValue IN :curveIds", Map.class);
        q.setParameter("curveIds", curveIds);
        q.setParameter("responseKind", "transformed efficacy");
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
	
	public static Collection<RawCurveDataDTO> getRawCurveDataByExperiment(String experimentIdOrCodeName){
		long startTime = System.currentTimeMillis();
		Collection<String> curveIds = CurveFitDTO.findAllCurveIdsByExperiment(experimentIdOrCodeName);
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		logger.debug("time to get curve id list and rendering hint = " + totalTime + " miliseconds.");
		long startTime2 = System.currentTimeMillis();
		Collection<RawCurveDataDTO> rawCurveDataDTOs = getRawCurveData(curveIds);
		long endTime2 = System.currentTimeMillis();
		long totalTime2 = endTime2 - startTime2;
		logger.debug("time to fill in raw curve data = " + totalTime2 + " miliseconds.");
		return rawCurveDataDTOs;
	}
}