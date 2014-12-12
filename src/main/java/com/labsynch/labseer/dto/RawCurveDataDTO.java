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
				new Optional()
		};

		return processors;
	}

	public static Collection<RawCurveDataDTO> getRawCurveData(
			Collection<RawCurveDataDTO> rawCurveDataDTOs) {
		List<RawCurveDataDTO> resultList = new ArrayList<RawCurveDataDTO>();
		for (RawCurveDataDTO rawCurveDataDTO : rawCurveDataDTOs) {
			String renderingHint = CurveFitDTO.findRenderingHint(rawCurveDataDTO.getCurveId());
			resultList.addAll(getRawCurveData(rawCurveDataDTO, renderingHint));
		}
		for (RawCurveDataDTO rawCurveDataDTO : resultList){
			Long responseSubjectValueId = rawCurveDataDTO.getResponseSubjectValueId();
			rawCurveDataDTO.setAlgorithmFlagStatus(getFlag("auto flag", "flag status", responseSubjectValueId));
			rawCurveDataDTO.setAlgorithmFlagStatus(getFlag("auto flag", "flag observation", responseSubjectValueId));
			rawCurveDataDTO.setAlgorithmFlagStatus(getFlag("auto flag", "flag comment", responseSubjectValueId));
			rawCurveDataDTO.setAlgorithmFlagComment(getFlagComment("auto flag", responseSubjectValueId));
			rawCurveDataDTO.setAlgorithmFlagStatus(getFlag("preprocess flag", "flag status", responseSubjectValueId));
			rawCurveDataDTO.setAlgorithmFlagStatus(getFlag("preprocess flag", "flag observation", responseSubjectValueId));
			rawCurveDataDTO.setAlgorithmFlagStatus(getFlag("preprocess flag", "flag comment", responseSubjectValueId));
			rawCurveDataDTO.setAlgorithmFlagComment(getFlagComment("preprocess flag", responseSubjectValueId));
			rawCurveDataDTO.setAlgorithmFlagStatus(getFlag("user flag", "flag status", responseSubjectValueId));
			rawCurveDataDTO.setAlgorithmFlagStatus(getFlag("user flag", "flag observation", responseSubjectValueId));
			rawCurveDataDTO.setAlgorithmFlagStatus(getFlag("user flag", "flag comment", responseSubjectValueId));
			rawCurveDataDTO.setAlgorithmFlagComment(getFlagComment("user flag", responseSubjectValueId));
		}
		return resultList;
	}

	public static List<RawCurveDataDTO> getRawCurveData(RawCurveDataDTO emptyRawCurveDataDTO, String renderingHint){
		String curveId = emptyRawCurveDataDTO.getCurveId();
		EntityManager em = SubjectValue.entityManager();
        TypedQuery<Map> q = em.createQuery("SELECT NEW MAP( rsv.id as responseSubjectValueId, "
        		+ "rsv.numericValue as response, "
        		+ "rsv.unitKind as responseUnits, "
        		+ "bcsv.concentration as dose, "
        		+ "bcsv.concUnit as doseUnits ) " 
        		+ "FROM AnalysisGroupValue agv "
        		+ "JOIN agv.lsState as ags "
        		+ "JOIN ags.analysisGroup.treatmentGroups as treat "
        		+ "JOIN treat.subjects as subj "
        		+ "JOIN subj.lsStates as rss "
        		+ "JOIN rss.lsValues as rsv "
        		+ "JOIN rss.lsValues as bcsv "
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
        		+ "AND agv.stringValue = :curveId", Map.class);
        q.setParameter("curveId", curveId);
        if (renderingHint.equalsIgnoreCase("4 parameter D-R")) q.setParameter("responseKind", "transformed efficacy");
        List<Map> queryResults = q.getResultList();
        List<RawCurveDataDTO> rawCurveDataList = new ArrayList<RawCurveDataDTO>();
		for (Map result : queryResults) {
			RawCurveDataDTO rawCurveDataDTO = new RawCurveDataDTO(curveId, result);
			rawCurveDataList.add(rawCurveDataDTO);
		}
		return rawCurveDataList;
	}
	
	public static String getFlag(String flagType, String flagKind, Long responseSubjectValueId){
		EntityManager em = SubjectValue.entityManager();
        TypedQuery<String> q = em.createQuery("SELECT flagvalue.codeValue " 
        		+ "FROM Subject as subj "
        		+ "JOIN subj.lsStates as flagstate "
        		+ "JOIN subj.lsStates as responsestate "
        		+ "JOIN responsestate.lsValues as responseValue "
        		+ "JOIN flagstate.lsValues as flagvalue "
        		+ "WHERE flagstate.lsType = 'data' "
        		+ "AND flagstate.lsKind = :flagType "
        		+ "AND flagvalue.lsType = 'codeValue' "
        		+ "AND flagvalue.lsKind = :flagKind "
        		+ "AND responseValue.id = :responseSubjectValueId "
        		+ "AND flagstate.ignored IS false ", String.class);
        q.setParameter("flagType", flagType);
        q.setParameter("flagKind", flagKind);
        q.setParameter("responseSubjectValueId", responseSubjectValueId);
        String queryResult = null;
        try {
        	queryResult = q.getSingleResult();
        } catch (EmptyResultDataAccessException e){
        	return null;
        }
		return queryResult;
	}
	
	public static String getFlagComment(String flagType, Long responseSubjectValueId){
		EntityManager em = SubjectValue.entityManager();
        TypedQuery<String> q = em.createQuery("SELECT flagvalue.stringValue " 
        		+ "FROM Subject as subj "
        		+ "JOIN subj.lsStates as flagstate "
        		+ "JOIN subj.lsStates as responsestate "
        		+ "JOIN responsestate.lsValues as responseValue "
        		+ "JOIN flagstate.lsValues as flagvalue "
        		+ "WHERE flagstate.lsType = 'data' "
        		+ "AND flagstate.lsKind = :flagType "
        		+ "AND flagvalue.lsType = 'codeValue' "
        		+ "AND flagvalue.lsKind = :flagKind "
        		+ "AND responseValue.id = :responseSubjectValueId "
        		+ "AND flagstate.ignored IS false ", String.class);
        q.setParameter("flagType", flagType);
        q.setParameter("flagKind", "comment");
        q.setParameter("responseSubjectValueId", responseSubjectValueId);
        String queryResult = null;
        try {
        	queryResult = q.getSingleResult();
        } catch (EmptyResultDataAccessException e){
        	return null;
        }
		return queryResult;
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
		Collection<RawCurveDataDTO> rawCurveDataDTOs = makeRawCurveDataDTOsFromCurveIdList(CurveFitDTO.findAllCurveIdsByExperiment(experimentIdOrCodeName));
		rawCurveDataDTOs = getRawCurveData(rawCurveDataDTOs);
		return rawCurveDataDTOs;
	}
	
	private static Collection<RawCurveDataDTO> makeRawCurveDataDTOsFromCurveIdList(Collection<String> curveIdList) {
		Collection<RawCurveDataDTO> rawCurveDataDTOs = new HashSet<RawCurveDataDTO>();
		for (String curveId : curveIdList) {
			rawCurveDataDTOs.add(new RawCurveDataDTO(curveId));
		}
		return rawCurveDataDTOs;
	}
}