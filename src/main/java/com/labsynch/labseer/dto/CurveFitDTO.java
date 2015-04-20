package com.labsynch.labseer.dto;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.AnalysisGroupState;
import com.labsynch.labseer.domain.AnalysisGroupValue;
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ProtocolValue;
import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.domain.SubjectValue;
import com.labsynch.labseer.domain.TreatmentGroup;
import com.labsynch.labseer.utils.SimpleUtil;

@RooJavaBean
@RooToString
@RooJson
public class CurveFitDTO {
	
	private static final Logger logger = LoggerFactory.getLogger(CurveFitDTO.class);

	public CurveFitDTO(){
		//empty constructor
	}
	
	public CurveFitDTO(String curveId) {
		this.curveId = curveId;
	}
	
	public CurveFitDTO(Map dataMap){
		this.curveId = (String) dataMap.get("curveId");
		this.analysisGroupCode = (String) dataMap.get("analysisGroupCode");
		this.recordedBy = (String) dataMap.get("recordedBy");
		this.recordedDate = (Date) dataMap.get("recordedDate");
		this.lsTransaction = (Long) dataMap.get("lsTransaction");
		this.batchCode = (String) dataMap.get("batchCode");
		this.category = (String) dataMap.get("category");
		this.renderingHint = (String) dataMap.get("renderingHint");
		if (dataMap.get("minNumeric") != null) this.min = ((BigDecimal) dataMap.get("minNumeric")).toString();
		else this.min = (String) dataMap.get("minString");
		if (dataMap.get("maxNumeric") != null) this.max = ((BigDecimal) dataMap.get("maxNumeric")).toString();
		else this.max = (String) dataMap.get("maxString");
		if (dataMap.get("ec50Numeric") != null) this.ec50 = ((BigDecimal) dataMap.get("ec50Numeric")).toString();
		else this.ec50 = (String) dataMap.get("ec50String");
		if (dataMap.get("slopeNumeric") != null) this.slope = ((BigDecimal) dataMap.get("slopeNumeric")).toString();
		else this.slope = (String) dataMap.get("slopeString");
		this.minUnits = (String) dataMap.get("minUnits");
		this.maxUnits = (String) dataMap.get("maxUnits");
		this.ec50Units = (String) dataMap.get("ec50Units");
		this.slope = (String) dataMap.get("slope");
		this.minUncertainty = (BigDecimal) dataMap.get("minUncertainty");
		this.maxUncertainty = (BigDecimal) dataMap.get("maxUncertainty");
		this.ec50Uncertainty = (BigDecimal) dataMap.get("ec50Uncertainty");
		this.slopeUncertainty = (BigDecimal) dataMap.get("slopeUncertainty");
		this.minUncertaintyType = (String) dataMap.get("minUncertaintyType");
		this.maxUncertaintyType = (String) dataMap.get("maxUncertaintyType");
		this.ec50UncertaintyType = (String) dataMap.get("ec50UncertaintyType");
		this.slopeUncertaintyType = (String) dataMap.get("slopeUncertaintyType");
		this.minOperatorKind = (String) dataMap.get("minOperatorKind");
		this.maxOperatorKind = (String) dataMap.get("maxOperatorKind");
		this.ec50OperatorKind = (String) dataMap.get("ec50OperatorKind");
		this.slopeOperatorKind = (String) dataMap.get("slopeOperatorKind");
		this.fittedMin = (BigDecimal) dataMap.get("fittedMin");
		this.fittedMax = (BigDecimal) dataMap.get("fittedMax");
		this.fittedEC50 = (BigDecimal) dataMap.get("fittedEC50");
		this.fittedSlope = (BigDecimal) dataMap.get("fittedSlope");
		this.fittedMinUncertainty = (BigDecimal) dataMap.get("fittedMinUncertainty");
		this.fittedMaxUncertainty = (BigDecimal) dataMap.get("fittedMaxUncertainty");
		this.fittedEc50Uncertainty = (BigDecimal) dataMap.get("fittedEc50Uncertainty");
		this.fittedSlopeUncertainty = (BigDecimal) dataMap.get("fittedSlopeUncertainty");
		this.fittedMinUncertaintyType = (String) dataMap.get("fittedMinUncertaintyType");
		this.fittedMaxUncertaintyType = (String) dataMap.get("fittedMaxUncertaintyType");
		this.fittedEc50UncertaintyType = (String) dataMap.get("fittedEc50UncertaintyType");
		this.fittedSlopeUncertaintyType = (String) dataMap.get("fittedSlopeUncertaintyType");
		this.sse = (BigDecimal) dataMap.get("sse");
		this.sst = (BigDecimal) dataMap.get("sst");
		this.rsquared = (BigDecimal) dataMap.get("rsquared");
		this.curveErrorsClob = (String) dataMap.get("curveErrorsClob");
		this.reportedValuesClob = (String) dataMap.get("reportedValuesClob");
		this.parameterStdErrorsClob = (String) dataMap.get("parameterStdErrorsClob");
		this.fitSettings = (String) dataMap.get("fitSettings");
		this.fitSummaryClob = (String) dataMap.get("fitSummaryClob");
		this.userFlagStatus = (String) dataMap.get("userFlagStatus");
		this.algorithmFlagStatus = (String) dataMap.get("algorithmFlagStatus");
	}
	
	public CurveFitDTO(HashMap<String, String> stringMap, HashMap<String, BigDecimal> numericMap)
			{
		// These keys must be exactly the same as what is used in the database. Case sensitive.
		//TODO: finalize these kinds, make sure they match exactly what is being used.
				this.curveId = stringMap.get("curve id");
				this.batchCode = stringMap.get("batch code");
				this.category = stringMap.get("category");
				this.renderingHint = stringMap.get("Rendering Hint");
				this.min = String.valueOf(numericMap.get("Min"));
				if (this.min.equals("null")) this.min = stringMap.get("Min");
				this.max = String.valueOf(numericMap.get("Max"));
				if (this.max.equals("null")) this.max = stringMap.get("Max");
				this.ec50 = String.valueOf(numericMap.get("EC50"));
				if (this.ec50.equals("null")) this.ec50 = stringMap.get("EC50");
				this.minUnits = stringMap.get("Min units");
				this.maxUnits = stringMap.get("Max units");
				this.ec50Units = stringMap.get("EC50 units");
				this.slope = String.valueOf(numericMap.get("Slope"));
				if (this.slope.equals("null")) this.slope = stringMap.get("Slope");
				this.minUncertainty = numericMap.get("Min uncertainty");
				this.maxUncertainty = numericMap.get("Max uncertainty");
				this.ec50Uncertainty = numericMap.get("EC50 uncertainty");
				this.slopeUncertainty = numericMap.get("Slope uncertainty");
				this.minUncertaintyType = stringMap.get("Min uncertainty type");
				this.maxUncertaintyType = stringMap.get("Max uncertainty type");
				this.ec50UncertaintyType = stringMap.get("EC50 uncertainty type");
				this.slopeUncertaintyType = stringMap.get("Slope uncertainty type");
				this.minOperatorKind = stringMap.get("Min operator kind");
				this.maxOperatorKind = stringMap.get("Max operator kind");
				this.ec50OperatorKind = stringMap.get("EC50 operator kind");
				this.slopeOperatorKind = stringMap.get("Slope operator kind");
				this.fittedMin = numericMap.get("Fitted Min");
				this.fittedMax = numericMap.get("Fitted Max");
				this.fittedEC50 = numericMap.get("Fitted EC50");
				this.fittedSlope = numericMap.get("Fitted Slope");
				this.fittedMinUncertainty = numericMap.get("Fitted Min uncertainty");
				this.fittedMaxUncertainty = numericMap.get("Fitted Max uncertainty");
				this.fittedEc50Uncertainty = numericMap.get("Fitted EC50 uncertainty");
				this.fittedSlopeUncertainty = numericMap.get("Fitted Slope uncertainty");
				this.fittedMinUncertaintyType = stringMap.get("Fitted Min uncertainty type");
				this.fittedMaxUncertaintyType = stringMap.get("Fitted Max uncertainty type");
				this.fittedEc50UncertaintyType = stringMap.get("Fitted EC50 uncertainty type");
				this.fittedSlopeUncertaintyType = stringMap.get("Fitted Slope uncertainty type");
				this.sse = numericMap.get("SSE");
				this.sst = numericMap.get("SST");
				this.rsquared = numericMap.get("rSquared");
				this.curveErrorsClob = stringMap.get("curveErrorsClob");
				this.reportedValuesClob = stringMap.get("reportedValuesClob");
				this.parameterStdErrorsClob = stringMap.get("parameterStdErrorsClob");
				this.fitSettings = stringMap.get("fitSettings");
				this.fitSummaryClob = stringMap.get("fitSummaryClob");
				this.userFlagStatus = stringMap.get("user flag status");
				this.algorithmFlagStatus = stringMap.get("algorithm flag status");
	}


	private String curveId;
	private String analysisGroupCode;
	private String recordedBy;
	private Date recordedDate;
	private Long lsTransaction;
	private String batchCode;
	private String category;
	private String renderingHint;
	private String min;
	private String max;
	private String ec50;
	private String minUnits;
	private String maxUnits;
	private String ec50Units;
	private String slope;
	private BigDecimal minUncertainty;
	private BigDecimal maxUncertainty;
	private BigDecimal ec50Uncertainty;
	private BigDecimal slopeUncertainty;
	private String minUncertaintyType;
	private String maxUncertaintyType;
	private String ec50UncertaintyType;
	private String slopeUncertaintyType;
	private String minOperatorKind;
	private String maxOperatorKind;
	private String ec50OperatorKind;
	private String slopeOperatorKind;
	private BigDecimal fittedMin;
	private BigDecimal fittedMax;
	private BigDecimal fittedEC50;
	private BigDecimal fittedSlope;
	private BigDecimal fittedMinUncertainty;
	private BigDecimal fittedMaxUncertainty;
	private BigDecimal fittedEc50Uncertainty;
	private BigDecimal fittedSlopeUncertainty;
	private String fittedMinUncertaintyType;
	private String fittedMaxUncertaintyType;
	private String fittedEc50UncertaintyType;
	private String fittedSlopeUncertaintyType;
	private BigDecimal sse;
	private BigDecimal sst;
	private BigDecimal rsquared;
	private String curveErrorsClob;
	private String reportedValuesClob;
	private String parameterStdErrorsClob;
	private String fitSettings;
	private String fitSummaryClob;
	private String userFlagStatus;
	private String algorithmFlagStatus;
	
	


	public static String[] getColumns(){
		String[] headerColumns = new String[] {
				"curveId",
				"analysisGroupCode",
				"recordedBy",
				"recordedDate",
				"lsTransaction",
				"batchCode",
				"category",
				"renderingHint",
				"min",
				"max",
				"ec50",
				"minUnits",
				"maxUnits",
				"ec50Units",
				"slope",
				"minUncertainty",
				"maxUncertainty",
				"ec50Uncertainty",
				"slopeUncertainty",
				"minUncertaintyType",
				"maxUncertaintyType",
				"ec50UncertaintyType",
				"slopeUncertaintyType",
				"minOperatorKind",
				"maxOperatorKind",
				"ec50OperatorKind",
				"slopeOperatorKind",
				"fittedMin",
				"fittedMax",
				"fittedEC50",
				"fittedSlope",
				"fittedMinUncertainty",
				"fittedMaxUncertainty",
				"fittedEc50Uncertainty",
				"fittedSlopeUncertainty",
				"fittedMinUncertaintyType",
				"fittedMaxUncertaintyType",
				"fittedEc50UncertaintyType",
				"fittedSlopeUncertaintyType",
				"sse",
				"sst",
				"rsquared",
				"curveErrorsClob",
				"reportedValuesClob",
				"parameterStdErrorsClob",
				"fitSettings",
				"fitSummaryClob",
				"userFlagStatus",
				"algorithmFlagStatus"
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
	
	@Transactional
	public static Collection<CurveFitDTO> getFitData(Collection<String> curveIds){
		EntityManager em = SubjectValue.entityManager();
		if (curveIds.isEmpty()) return new ArrayList<CurveFitDTO>();
		TypedQuery<Map> q = em.createQuery("SELECT NEW MAP( curveIdValue.stringValue as curveId, "
				+ "ag.codeName as analysisGroupCode, "
				+ "curveIdValue.recordedBy as recordedBy, "
        		+ "curveIdValue.lsTransaction as lsTransaction, "
        		+ "curveIdValue.recordedDate as recordedDate, "
        		+ "batchCodeValue.codeValue as batchCode, "
        		+ "categoryValue.stringValue as category, "
        		+ "renderingHintValue.stringValue as renderingHint, "
        		+ "minValue.stringValue as minString, "
        		+ "minValue.numericValue as minNumeric, "
        		+ "maxValue.stringValue as maxString, "
        		+ "maxValue.numericValue as maxNumeric, "
        		+ "ec50Value.stringValue as ec50String, "
        		+ "ec50Value.numericValue as ec50Numeric, "
        		+ "slopeValue.stringValue as slopeString, "
        		+ "slopeValue.numericValue as slopeNumeric, "
        		+ "minValue.unitKind as minUnits, "
        		+ "maxValue.unitKind as maxUnits, "
        		+ "ec50Value.unitKind as ec50Units, "
        		+ "minValue.uncertainty as minUncertainty, "
        		+ "maxValue.uncertainty as maxUncertainty, "
        		+ "ec50Value.uncertainty as ec50Uncertainty, "
        		+ "slopeValue.uncertainty as slopeUncertainty, "
        		+ "minValue.uncertaintyType as minUncertaintyType, "
        		+ "maxValue.uncertaintyType as maxUncertaintyType, "
        		+ "ec50Value.uncertaintyType as ec50UncertaintyType, "
        		+ "slopeValue.uncertaintyType as slopeUncertaintyType, "
        		+ "minValue.operatorKind as minOperatorKind, "
        		+ "maxValue.operatorKind as maxOperatorKind, "
        		+ "ec50Value.operatorKind as ec50OperatorKind, "
        		+ "slopeValue.operatorKind as slopeOperatorKind, "
        		+ "fittedMinValue.numericValue as fittedMin, "
        		+ "fittedMaxValue.numericValue as fittedMax, "
        		+ "fittedEc50Value.numericValue as fittedEC50, "
        		+ "fittedSlopeValue.numericValue as fittedSlope, "
        		+ "fittedMinValue.uncertainty as fittedMinUncertainty, "
        		+ "fittedMaxValue.uncertainty as fittedMaxUncertainty, "
        		+ "fittedEc50Value.uncertainty as fittedEc50Uncertainty, "
        		+ "fittedSlopeValue.uncertainty as fittedSlopeUncertainty, "
        		+ "fittedMinValue.uncertaintyType as fittedMinUncertaintyType, "
        		+ "fittedMaxValue.uncertaintyType as fittedMaxUncertaintyType, "
        		+ "fittedEc50Value.uncertaintyType as fittedEc50UncertaintyType, "
        		+ "fittedSlopeValue.uncertaintyType as fittedSlopeUncertaintyType, "
        		+ "sseValue.numericValue as sse, "
        		+ "sstValue.numericValue as sst, "
        		+ "rSquaredValue.numericValue as rsquared, "
        		+ "curveErrorsClobValue.clobValue as curveErrorsClob, "
        		+ "reportedValuesClobValue.clobValue as reportedValuesClob, "
        		+ "parameterStdErrorsClobValue.clobValue as parameterStdErrorsClob, "
        		+ "fitSettingsValue.clobValue as fitSettings, "
        		+ "fitSummaryClobValue.clobValue as fitSummaryClob, "
        		+ "userFlagStatusValue.codeValue as userFlagStatus, "
        		+ "algorithmFlagStatusValue.codeValue as algorithmFlagStatus "
        		+ " ) " 
        		+ "FROM AnalysisGroup ag "
        		+ "JOIN ag.lsStates as ags "
				+ "LEFT JOIN ags.lsValues as curveIdValue WITH curveIdValue.lsKind = 'curve id' "
				+ "LEFT JOIN ags.lsValues as batchCodeValue WITH batchCodeValue.lsKind = 'batch code' "
				+ "LEFT JOIN ags.lsValues as categoryValue WITH categoryValue.lsKind = 'category' "
				+ "LEFT JOIN ags.lsValues as renderingHintValue WITH renderingHintValue.lsKind = 'Rendering Hint' "
				+ "LEFT JOIN ags.lsValues as minValue WITH minValue.lsKind = 'Min' "
				+ "LEFT JOIN ags.lsValues as maxValue WITH maxValue.lsKind = 'Max' "
				+ "LEFT JOIN ags.lsValues as ec50Value WITH ec50Value.lsKind = 'EC50' "
				+ "LEFT JOIN ags.lsValues as slopeValue WITH slopeValue.lsKind = 'Slope' "
				+ "LEFT JOIN ags.lsValues as fittedMinValue WITH fittedMinValue.lsKind = 'Fitted Min' "
				+ "LEFT JOIN ags.lsValues as fittedMaxValue WITH fittedMaxValue.lsKind = 'Fitted Max' "
				+ "LEFT JOIN ags.lsValues as fittedEc50Value WITH fittedEc50Value.lsKind = 'Fitted EC50' "
				+ "LEFT JOIN ags.lsValues as fittedSlopeValue WITH fittedSlopeValue.lsKind = 'Fitted Slope' "
				+ "LEFT JOIN ags.lsValues as sseValue WITH sseValue.lsKind = 'SSE' "
				+ "LEFT JOIN ags.lsValues as sstValue WITH sstValue.lsKind = 'SST' "
				+ "LEFT JOIN ags.lsValues as rSquaredValue WITH rSquaredValue.lsKind = 'rSquared' "
				+ "LEFT JOIN ags.lsValues as curveErrorsClobValue WITH curveErrorsClobValue.lsKind = 'curveErrorsClob' "
				+ "LEFT JOIN ags.lsValues as parameterStdErrorsClobValue WITH parameterStdErrorsClobValue.lsKind = 'parameterStdErrorsClob' "
				+ "LEFT JOIN ags.lsValues as reportedValuesClobValue WITH reportedValuesClobValue.lsKind = 'reportedValuesClob' "
				+ "LEFT JOIN ags.lsValues as fitSettingsValue WITH fitSettingsValue.lsKind = 'fitSettings' "
				+ "LEFT JOIN ags.lsValues as fitSummaryClobValue WITH fitSummaryClobValue.lsKind = 'fitSummaryClob' "
				+ "LEFT JOIN ags.lsValues as userFlagStatusValue WITH userFlagStatusValue.lsKind = 'user flag status' "
				+ "LEFT JOIN ags.lsValues as algorithmFlagStatusValue WITH algorithmFlagStatusValue.lsKind = 'algorithm flag status' "
        		+ "WHERE ag.ignored = false " 
        		+ "AND ags.ignored = false "
        		+ "AND curveIdValue.ignored = false "
        		+ "AND curveIdValue.stringValue IN :curveIds", Map.class);
        q.setParameter("curveIds", curveIds);
        List<Map> queryResults = q.getResultList();
        logger.debug(queryResults.size()+" results found");
        List<CurveFitDTO> curveFitDTOList = new ArrayList<CurveFitDTO>();
		for (Map result : queryResults) {
			CurveFitDTO curveFitDTO = new CurveFitDTO(result);
			curveFitDTOList.add(curveFitDTO);
		}
		return curveFitDTOList;
		
	}
	
	@Transactional
	public static CurveFitDTO getFitData(CurveFitDTO curveFitDTO){
		AnalysisGroupValue curveIdValue = findCurveIdValue(curveFitDTO.getCurveId());
		if (curveIdValue == null || curveIdValue.getStateId() == null) {
			logger.debug("No data found for curve id: " + curveFitDTO.getCurveId());
			return new CurveFitDTO();
		}
		AnalysisGroupState doseResponseState = AnalysisGroupState.findAnalysisGroupState(curveIdValue.getStateId());
//		List<AnalysisGroupValue> agValues = AnalysisGroupValue.findAnalysisGroupValuesByLsState(doseResponseState).getResultList();
		Collection<AnalysisGroupValue> agValues = doseResponseState.getLsValues();
		HashMap<String, String> stringMap = new HashMap<String, String>();
		HashMap<String, BigDecimal> numericMap = new HashMap<String, BigDecimal>();
		for (AnalysisGroupValue agValue : agValues) {
			if (agValue.getLsType().equals("stringValue")) {
				stringMap.put(agValue.getLsKind(), agValue.getStringValue());
			} else if (agValue.getLsType().equals("clobValue")) {
				stringMap.put(agValue.getLsKind(), agValue.getClobValue());
			} else if (agValue.getLsType().equals("numericValue")) {
				numericMap.put(agValue.getLsKind(), agValue.getNumericValue());
				if(agValue.getUnitKind() != null) {
					stringMap.put(agValue.getLsKind()+" units", agValue.getUnitKind());
				}
				if(agValue.getUncertainty() != null){
					numericMap.put(agValue.getLsKind()+" uncertainty", agValue.getUncertainty());
				}
				if(agValue.getUncertaintyType() != null){
					stringMap.put(agValue.getLsKind()+" uncertainty type", agValue.getUncertaintyType());
				}
				if(agValue.getOperatorKind() != null){
					stringMap.put(agValue.getLsKind()+" operator kind", agValue.getOperatorKind());
				}
			} else if  (agValue.getLsType().equals("codeValue")) {
				stringMap.put(agValue.getLsKind(), agValue.getCodeValue());
			}
		}
		curveFitDTO = new CurveFitDTO(stringMap, numericMap);
		curveFitDTO.setAnalysisGroupCode(doseResponseState.getAnalysisGroup().getCodeName());
		curveFitDTO.setLsTransaction(curveIdValue.getLsTransaction());
		return curveFitDTO;
	}
	
	public static String getCsvList(Collection<CurveFitDTO> curveFitDTOs, String format) {
		//format is ALWAYS tsv or csv (not case sensitive)
		StringWriter outFile = new StringWriter();
        ICsvBeanWriter beanWriter = null;
        try {
            if (format.equalsIgnoreCase("csv")) beanWriter = new CsvBeanWriter(outFile, CsvPreference.STANDARD_PREFERENCE);
            else beanWriter = new CsvBeanWriter(outFile, CsvPreference.TAB_PREFERENCE);
            final String[] header = CurveFitDTO.getColumns();
            final CellProcessor[] processors = CurveFitDTO.getProcessors();
            beanWriter.writeHeader(header);
            for (final CurveFitDTO curveFitDTO : curveFitDTOs) {
                beanWriter.write(curveFitDTO, header, processors);
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

	public static void updateFitData( Collection<CurveFitDTO> curveFitDTOs) {
		for (CurveFitDTO curveFitDTO : curveFitDTOs) {
			if (curveFitDTO.getAnalysisGroupCode() == null) logger.error("FIELD MISSING: analysisGroupCode");
			if (curveFitDTO.getRecordedBy() == null) logger.error("FIELD MISSING: recordedBy");
			if (curveFitDTO.getLsTransaction() == null) logger.error("FIELD MISSING: lsTransaction");
			AnalysisGroup analysisGroup = AnalysisGroup.findAnalysisGroupsByCodeNameEquals(curveFitDTO.getAnalysisGroupCode()).getSingleResult();
			try {
				AnalysisGroupState oldState = AnalysisGroupState.findAnalysisGroupStatesByAnalysisGroupAndLsTypeEqualsAndLsKindEqualsAndIgnoredNot(analysisGroup, "data", "dose response", true).getSingleResult();
				oldState.setIgnored(true);
				oldState.merge();
//				oldState.flush();
			} catch(EmptyResultDataAccessException e) {
				logger.debug("Old state of typekind data/dose response not found for AG Code " + curveFitDTO.getAnalysisGroupCode() + " , creating new one");
			}
			AnalysisGroupState newState = createCurveFitState(analysisGroup.getId(), "data", "dose response", curveFitDTO.getRecordedBy(), curveFitDTO.getLsTransaction());
			newState.setLsTransaction(curveFitDTO.getLsTransaction());
			saveFitData(newState, curveFitDTO);
		}
	}
	
	@Transactional
	private static void saveFitData(AnalysisGroupState state, CurveFitDTO curveFitDTO) {
		Collection<AnalysisGroupValue> newValues = new HashSet<AnalysisGroupValue>();
		//non-optional fields
		String recordedBy = curveFitDTO.getRecordedBy();
		Long lsTransaction = curveFitDTO.getLsTransaction();
		String batchCode = curveFitDTO.getBatchCode();
		AnalysisGroupValue batchCodeValue = createCurveFitValue(state, "codeValue", "batch code", curveFitDTO.getBatchCode(), recordedBy, lsTransaction);
		batchCodeValue.setPublicData(true);
		newValues.add(batchCodeValue);
		AnalysisGroupValue curveIdValue = createCurveFitValue(state, "stringValue", "curve id", curveFitDTO.getCurveId(), recordedBy, lsTransaction);
		curveIdValue.setPublicData(true);
		newValues.add(curveIdValue);
		//all the rest of the fields (may be null)
		String category = curveFitDTO.getCategory();
		String renderingHint = curveFitDTO.getRenderingHint();
		String min = curveFitDTO.getMin();
		String max = curveFitDTO.getMax();
		String ec50 = curveFitDTO.getEc50();
		String slope = curveFitDTO.getSlope();
		BigDecimal fittedMin = curveFitDTO.getFittedMin();
		BigDecimal fittedMax = curveFitDTO.getFittedMax();
		BigDecimal fittedEC50 = curveFitDTO.getFittedEC50();
		BigDecimal fittedSlope = curveFitDTO.getFittedSlope();
		BigDecimal sse = curveFitDTO.getSse();
		BigDecimal sst = curveFitDTO.getSst();
		BigDecimal rSquared = curveFitDTO.getRsquared();
		String curveErrorsClob = curveFitDTO.getCurveErrorsClob();
		String reportedValuesClob = curveFitDTO.getReportedValuesClob();
		String parameterStdErrorsClob = curveFitDTO.getParameterStdErrorsClob();
		String fitSettings = curveFitDTO.getFitSettings();
		String fitSummaryClob = curveFitDTO.getFitSummaryClob();
		String userFlagStatus = curveFitDTO.getUserFlagStatus();
		String algorithmFlagStatus = curveFitDTO.getAlgorithmFlagStatus();
		
		//only create AnalysisGroupValues if they would not be empty/null

		if (!(category==null)){
			AnalysisGroupValue categoryValue = createCurveFitValue(state, "stringValue", "category", category, recordedBy, lsTransaction);
			categoryValue.setCodeValue(batchCode);
			newValues.add(categoryValue);
		}
		if (!(renderingHint==null)){
			AnalysisGroupValue renderingHintValue = createCurveFitValue(state, "stringValue", "Rendering Hint", renderingHint, recordedBy, lsTransaction);
			renderingHintValue.setCodeValue(batchCode);
			newValues.add(renderingHintValue);
		}
		//Min, Max, EC50, and Slope are special cases. They must be filled in with something for Seurat,
		//so we fill them in as stringValues if their value is not numeric
		if (!(min==null) && SimpleUtil.isDecimalNumeric(min)) {
			AnalysisGroupValue minValue = createCurveFitValue(state, "numericValue", "Min", new BigDecimal(min), recordedBy, lsTransaction);
			minValue.setUnitKind(curveFitDTO.getMinUnits());
			minValue.setUncertainty(curveFitDTO.getMinUncertainty());
			minValue.setUncertaintyType(curveFitDTO.getMinUncertaintyType());
			minValue.setOperatorKind(curveFitDTO.getMinOperatorKind());
			minValue.setCodeValue(batchCode);
			minValue.setPublicData(true);
			newValues.add(minValue);
		} else {
			AnalysisGroupValue minValue = createCurveFitValue(state, "stringValue", "Min", min, recordedBy, lsTransaction);
			minValue.setCodeValue(batchCode);
			minValue.setPublicData(true);
			newValues.add(minValue);
		}
		if (!(max==null) && SimpleUtil.isDecimalNumeric(max)) {
			AnalysisGroupValue maxValue = createCurveFitValue(state, "numericValue", "Max", new BigDecimal(max), recordedBy, lsTransaction);
			maxValue.setUnitKind(curveFitDTO.getMaxUnits());
			maxValue.setUncertainty(curveFitDTO.getMaxUncertainty());
			maxValue.setUncertaintyType(curveFitDTO.getMaxUncertaintyType());
			maxValue.setOperatorKind(curveFitDTO.getMaxOperatorKind());
			maxValue.setCodeValue(batchCode);
			maxValue.setPublicData(true);
			newValues.add(maxValue);
		} else {
			AnalysisGroupValue maxValue = createCurveFitValue(state, "stringValue", "Max", max, recordedBy, lsTransaction);
			maxValue.setCodeValue(batchCode);
			maxValue.setPublicData(true);
			newValues.add(maxValue);
		}
		if (!(ec50==null) && SimpleUtil.isDecimalNumeric(ec50)) {
			AnalysisGroupValue ec50Value = createCurveFitValue(state, "numericValue", "EC50", new BigDecimal(ec50), recordedBy, lsTransaction);
			ec50Value.setUnitKind(curveFitDTO.getEc50Units());
			ec50Value.setUncertainty(curveFitDTO.getEc50Uncertainty());
			ec50Value.setUncertaintyType(curveFitDTO.getEc50UncertaintyType());
			ec50Value.setOperatorKind(curveFitDTO.getEc50OperatorKind());
			ec50Value.setCodeValue(batchCode);
			ec50Value.setPublicData(true);
			newValues.add(ec50Value);
		} else {
			AnalysisGroupValue ec50Value = createCurveFitValue(state, "stringValue", "EC50", ec50, recordedBy, lsTransaction);
			ec50Value.setCodeValue(batchCode);
			ec50Value.setPublicData(true);
			newValues.add(ec50Value);
		}
		if (!(slope==null) && SimpleUtil.isDecimalNumeric(slope)) {
			AnalysisGroupValue slopeValue = createCurveFitValue(state, "numericValue", "Slope", new BigDecimal(slope), recordedBy, lsTransaction);
			slopeValue.setCodeValue(batchCode);
			slopeValue.setUncertainty(curveFitDTO.getSlopeUncertainty());
			slopeValue.setUncertaintyType(curveFitDTO.getSlopeUncertaintyType());
			slopeValue.setOperatorKind(curveFitDTO.getSlopeOperatorKind());
			slopeValue.setPublicData(true);
			newValues.add(slopeValue);
		} else {
			AnalysisGroupValue slopeValue = createCurveFitValue(state, "stringValue", "Slope", slope, recordedBy, lsTransaction);
			slopeValue.setCodeValue(batchCode);
			slopeValue.setPublicData(true);
			newValues.add(slopeValue);
		}
		//Remaining non-special numericValues
		if (!(fittedMin==null)){
			AnalysisGroupValue fittedMinValue = createCurveFitValue(state, "numericValue", "Fitted Min", fittedMin, recordedBy, lsTransaction);
			fittedMinValue.setCodeValue(batchCode);
			fittedMinValue.setUncertainty(curveFitDTO.getFittedMinUncertainty());
			fittedMinValue.setUncertaintyType(curveFitDTO.getFittedMinUncertaintyType());
			newValues.add(fittedMinValue);
		}
		if (!(fittedMax==null)){
			AnalysisGroupValue fittedMaxValue = createCurveFitValue(state, "numericValue", "Fitted Max", fittedMax, recordedBy, lsTransaction);
			fittedMaxValue.setCodeValue(batchCode);
			fittedMaxValue.setUncertainty(curveFitDTO.getFittedMaxUncertainty());
			fittedMaxValue.setUncertaintyType(curveFitDTO.getFittedMaxUncertaintyType());
			newValues.add(fittedMaxValue);
		}
		if (!(fittedEC50==null)){
			AnalysisGroupValue fittedEC50Value = createCurveFitValue(state, "numericValue", "Fitted EC50", fittedEC50, recordedBy, lsTransaction);
			fittedEC50Value.setCodeValue(batchCode);
			fittedEC50Value.setUncertainty(curveFitDTO.getFittedEc50Uncertainty());
			fittedEC50Value.setUncertaintyType(curveFitDTO.getFittedEc50UncertaintyType());
			newValues.add(fittedEC50Value);
		}
		if (!(fittedSlope==null)){
			AnalysisGroupValue fittedSlopeValue = createCurveFitValue(state, "numericValue", "Fitted Slope", fittedSlope, recordedBy, lsTransaction);
			fittedSlopeValue.setCodeValue(batchCode);
			fittedSlopeValue.setUncertainty(curveFitDTO.getFittedSlopeUncertainty());
			fittedSlopeValue.setUncertaintyType(curveFitDTO.getFittedSlopeUncertaintyType());
			newValues.add(fittedSlopeValue);
		}
		if (!(sse==null)){
			AnalysisGroupValue sseValue = createCurveFitValue(state, "numericValue", "SSE", sse, recordedBy, lsTransaction);
			sseValue.setCodeValue(batchCode);
			newValues.add(sseValue);
		}
		if (!(sst==null)){
			AnalysisGroupValue sstValue = createCurveFitValue(state, "numericValue", "SST", sst, recordedBy, lsTransaction);
			sstValue.setCodeValue(batchCode);
			newValues.add(sstValue);
		}
		if (!(rSquared==null)){
			AnalysisGroupValue rSquaredValue = createCurveFitValue(state, "numericValue", "rSquared", rSquared, recordedBy, lsTransaction);
			rSquaredValue.setCodeValue(batchCode);
			newValues.add(rSquaredValue);
		}
		//clob values
		if (!(curveErrorsClob==null)){
			AnalysisGroupValue curveErrorsClobValue = createCurveFitValue(state, "clobValue", "curveErrorsClob", curveErrorsClob, recordedBy, lsTransaction);
			curveErrorsClobValue.setCodeValue(batchCode);
			newValues.add(curveErrorsClobValue);
		}
		if (!(reportedValuesClob==null)){
			AnalysisGroupValue reportedValuesClobValue = createCurveFitValue(state, "clobValue", "reportedValuesClob", reportedValuesClob, recordedBy, lsTransaction);
			reportedValuesClobValue.setCodeValue(batchCode);
			newValues.add(reportedValuesClobValue);
		}
		if (!(parameterStdErrorsClob==null)){
			AnalysisGroupValue parameterStdErrorsClobValue = createCurveFitValue(state, "clobValue", "parameterStdErrorsClob", parameterStdErrorsClob, recordedBy, lsTransaction);
			parameterStdErrorsClobValue.setCodeValue(batchCode);
			newValues.add(parameterStdErrorsClobValue);
		}
		if (!(fitSettings==null)){
			AnalysisGroupValue fitSettingsValue = createCurveFitValue(state, "clobValue", "fitSettings", fitSettings, recordedBy, lsTransaction);
			fitSettingsValue.setCodeValue(batchCode);
			newValues.add(fitSettingsValue);
		}
		if (!(fitSummaryClob==null)){
			AnalysisGroupValue fitSummaryClobValue = createCurveFitValue(state, "clobValue", "fitSummaryClob", fitSummaryClob, recordedBy, lsTransaction);
			fitSummaryClobValue.setCodeValue(batchCode);
			newValues.add(fitSummaryClobValue);
		}
		//flags
		if (!(userFlagStatus==null)){
			AnalysisGroupValue userFlagStatusValue = createCurveFitValue(state, "codeValue", "user flag status", userFlagStatus, recordedBy, lsTransaction);
			userFlagStatusValue.setCodeType("user flags");
			userFlagStatusValue.setCodeKind("flag status");
			userFlagStatusValue.setCodeOrigin("ACAS DDICT");
			newValues.add(userFlagStatusValue);
		}
		if (!(algorithmFlagStatus==null)){
			AnalysisGroupValue algorithmFlagStatusValue = createCurveFitValue(state, "codeValue", "algorithm flag status", algorithmFlagStatus, recordedBy, lsTransaction);
			algorithmFlagStatusValue.setCodeType("algorithm flags");
			algorithmFlagStatusValue.setCodeKind("flag status");
			algorithmFlagStatusValue.setCodeOrigin("ACAS DDICT");
			newValues.add(algorithmFlagStatusValue);
		}
		
		//persist and flush all the new values
		for (AnalysisGroupValue value: newValues){
			value.persist();
//			value.flush();
		}		
	}
	
	@Transactional
	private static AnalysisGroupValue createCurveFitValue(AnalysisGroupState lsState, String lsType, String lsKind, String value, String recordedBy, Long lsTransaction) {
		AnalysisGroupValue analysisGroupValue = new AnalysisGroupValue();
		analysisGroupValue.setLsState(lsState);
		analysisGroupValue.setLsType(lsType);
		analysisGroupValue.setLsKind(lsKind);
		if (lsType.equals("stringValue")) analysisGroupValue.setStringValue(value);
		if (lsType.equals("clobValue")) analysisGroupValue.setClobValue(value);
		if (lsType.equals("codeValue")) analysisGroupValue.setCodeValue(value);
		analysisGroupValue.setRecordedBy(recordedBy);
		analysisGroupValue.setLsTransaction(lsTransaction);
		logger.debug("Creating value kind: " + analysisGroupValue.toJson());
		analysisGroupValue.persist();
		return analysisGroupValue;
	}
	
	@Transactional
	private static AnalysisGroupValue createCurveFitValue(AnalysisGroupState lsState, String lsType, String lsKind, BigDecimal value, String recordedBy, Long lsTransaction) {
		AnalysisGroupValue analysisGroupValue = new AnalysisGroupValue();
		analysisGroupValue.setLsState(lsState);
		analysisGroupValue.setLsType(lsType);
		analysisGroupValue.setLsKind(lsKind);
		if (lsType.equals("numericValue")) analysisGroupValue.setNumericValue(value);
		analysisGroupValue.setRecordedBy(recordedBy);
		analysisGroupValue.setLsTransaction(lsTransaction);
		logger.debug("Creating value kind: " + analysisGroupValue.toJson());
		analysisGroupValue.persist();
		return analysisGroupValue;
	}
	
	@Transactional
	private static AnalysisGroupState createCurveFitState(Long analysisGroupId, String stateType, String stateKind, String recordedBy, Long lsTransaction) {
		AnalysisGroupState analysisGroupState = new AnalysisGroupState();
		AnalysisGroup analysisGroup = AnalysisGroup.findAnalysisGroup(analysisGroupId);
		analysisGroupState.setAnalysisGroup(analysisGroup);
		analysisGroupState.setLsType(stateType);
		analysisGroupState.setLsKind(stateKind);
		analysisGroupState.setRecordedBy(recordedBy);
		analysisGroupState.setLsTransaction(lsTransaction);
		analysisGroupState.persist();
		return analysisGroupState;
	}
	
	@Transactional
	private static AnalysisGroupValue findCurveIdValue(String curveId){
		//		AnalysisGroupValue curveIdValue = AnalysisGroupValue.findAnalysisGroupValuesByLsTypeEqualsAndLsKindEqualsAndStringValueLikeAndIgnoredNot("stringValue", "curve id", curveFitDTO.getCurveId(), true).getSingleResult();
        if (curveId == null || curveId.length() == 0) throw new IllegalArgumentException("The curveId argument is required");
        curveId = curveId.replace('*', '%');
        if (curveId.charAt(0) != '%') {
            curveId = "%" + curveId;
        }
        if (curveId.charAt(curveId.length() - 1) != '%') {
            curveId = curveId + "%";
        }
        EntityManager em = AnalysisGroupValue.entityManager();
        TypedQuery<AnalysisGroupValue> q = em.createQuery("SELECT o FROM AnalysisGroupValue AS o JOIN o.lsState AS state JOIN state.analysisGroup AS ag WHERE o.lsType = :lsType  AND o.lsKind = :lsKind  AND LOWER(o.stringValue) LIKE LOWER(:stringValue)  AND o.ignored IS NOT :ignored AND state.ignored IS NOT :ignored AND ag.ignored IS NOT :ignored AND state.lsType = :stateType AND state.lsKind = :stateKind", AnalysisGroupValue.class);
        q.setParameter("lsType", "stringValue");
        q.setParameter("lsKind", "curve id");
        q.setParameter("stateType", "data");
        q.setParameter("stateKind", "dose response");
        q.setParameter("stringValue", curveId);
        q.setParameter("ignored", true);
        try{
        	return q.getSingleResult();
        }catch(NoResultException e1) {
        	logger.warn("No curve id found");
        	return null;
        }
        	catch(Exception e2){
	        	logger.error("Caught error trying to find curve id: " + curveId);
	        	logger.error("Specific error: " + e2.getMessage());
        	return null;
        }
	}
	
	@Transactional
	public static Collection<String> findAllCurveIdsByExperiment(String experimentIdOrCodeName){
		Experiment experiment = null;
		if(SimpleUtil.isNumeric(experimentIdOrCodeName)) {
			experiment = Experiment.findExperiment(Long.valueOf(experimentIdOrCodeName));
		} else {		
			experiment = Experiment.findExperimentsByCodeNameEquals(experimentIdOrCodeName).getSingleResult();
		}
        EntityManager em = AnalysisGroupValue.entityManager();
        TypedQuery<String> q = em.createQuery("SELECT agv.stringValue FROM AnalysisGroupValue AS agv "
        		+ "JOIN agv.lsState AS state "
        		+ "JOIN state.analysisGroup AS ag  "
        		+ "JOIN ag.experiments AS exp "
        		+ "WHERE exp = :experiment "
        		+ "AND agv.lsType = :lsType  "
        		+ "AND agv.lsKind = :lsKind  "
        		+ "AND agv.ignored IS NOT :ignored "
        		+ "AND state.ignored IS NOT :ignored "
        		+ "AND ag.ignored IS NOT :ignored "
        		+ "AND state.lsType = :stateType "
        		+ "AND state.lsKind = :stateKind", String.class);
        q.setParameter("lsType", "stringValue");
        q.setParameter("lsKind", "curve id");
        q.setParameter("stateType", "data");
        q.setParameter("stateKind", "dose response");
        q.setParameter("experiment", experiment);
        q.setParameter("ignored", true);
        return q.getResultList();
	}
	
	private static Collection<CurveFitDTO> makeCurveFitDTOsFromCurveIdList(Collection<String> curveIdList) {
		Collection<CurveFitDTO> curveFitDTOs = new HashSet<CurveFitDTO>();
		for (String curveId : curveIdList) {
			curveFitDTOs.add(new CurveFitDTO(curveId));
		}
		return curveFitDTOs;
	}

	public static String findRenderingHint(TreatmentGroup treatmentGroup) {
		EntityManager em = SubjectValue.entityManager();
		TypedQuery<String> q = em.createQuery("SELECT value.stringValue "
				+ "FROM TreatmentGroup AS treatmentGroup "
				+ "JOIN treatmentGroup.analysisGroups AS analysisGroup "
				+ "JOIN analysisGroup.lsStates AS state "
				+ "JOIN state.lsValues AS value "
				+ "WHERE treatmentGroup = :treatmentGroup "
				+ "AND analysisGroup.ignored IS NOT :ignored "
				+ "AND state.ignored IS NOT :ignored "
				+ "AND state.lsType = :stateType "
				+ "AND state.lsKind = :stateKind "
				+ "AND value.lsType = :valueType "
				+ "AND value.lsKind = :valueKind ", String.class);
		
		q.setParameter("stateType", "data");
		q.setParameter("stateKind", "dose response");
		q.setParameter("valueType", "stringValue");
		q.setParameter("valueKind", "Rendering Hint");
		q.setParameter("treatmentGroup", treatmentGroup);
		q.setParameter("ignored", true);
		return q.getSingleResult();
	}
	
	public static String findRenderingHint(AnalysisGroup analysisGroup) {
		EntityManager em = SubjectValue.entityManager();
		TypedQuery<String> q = em.createQuery("SELECT value.stringValue "
				+ "FROM AnalysisGroup AS analysisGroup "
				+ "JOIN analysisGroup.lsStates AS state "
				+ "JOIN state.lsValues AS value "
				+ "WHERE analysisGroup = :analysisGroup "
				+ "AND analysisGroup.ignored IS NOT :ignored "
				+ "AND state.ignored IS NOT :ignored "
				+ "AND state.lsType = :stateType "
				+ "AND state.lsKind = :stateKind "
				+ "AND value.lsType = :valueType "
				+ "AND value.lsKind = :valueKind ", String.class);
		
		q.setParameter("stateType", "data");
		q.setParameter("stateKind", "dose response");
		q.setParameter("valueType", "stringValue");
		q.setParameter("valueKind", "Rendering Hint");
		q.setParameter("analysisGroup", analysisGroup);
		q.setParameter("ignored", true);
		return q.getSingleResult();
	}


	public static String findRenderingHint(Subject subject) {
		EntityManager em = SubjectValue.entityManager();
		TypedQuery<String> q = em.createQuery("SELECT value.stringValue "
				+ "FROM Subject AS subject"
				+ "JOIN subject.treatmentGroup AS treatmentGroup "
				+ "JOIN treatmentGroup.analysisGroups AS analysisGroup "
				+ "JOIN analysisGroup.lsStates AS state "
				+ "JOIN state.lsValues AS value "
				+ "WHERE subject = :subject "
				+ "AND analysisGroup.ignored IS NOT :ignored "
				+ "AND state.ignored IS NOT :ignored "
				+ "AND state.lsType = :stateType "
				+ "AND state.lsKind = :stateKind "
				+ "AND value.lsType = :valueType "
				+ "AND value.lsKind = :valueKind ", String.class);
		
		q.setParameter("stateType", "data");
		q.setParameter("stateKind", "dose response");
		q.setParameter("valueType", "stringValue");
		q.setParameter("valueKind", "Rendering Hint");
		q.setParameter("subject", subject);
		q.setParameter("ignored", true);
		return q.getSingleResult();
	}
	
	public static String findRenderingHint(String curveId) {
		AnalysisGroupValue curveIdValue = findCurveIdValue(curveId);
		if (curveIdValue == null) return null;
		AnalysisGroup analysisGroup = curveIdValue.getLsState().getAnalysisGroup();
		return findRenderingHint(analysisGroup);
	}

	public static String findFirstRenderingHint(List<String> curveIds) {
		String renderingHint = "4 parameter D-R";
		for (String curveId: curveIds){
			try{
				String newRenderingHint = findRenderingHint(curveId);
				if (newRenderingHint != null){
					if (!newRenderingHint.equals(renderingHint)) logger.debug("Changing rendering hint from: " + renderingHint + " to: " + newRenderingHint);
					renderingHint = newRenderingHint;
				}
			} catch (EmptyResultDataAccessException e){
				logger.warn("No rendering hint found for curve Id: " + curveId);
			}
		}
		return renderingHint;
	}
	
	private static Collection<Long> findProtocolIdsByCurveId(String curveId) {
		Collection<Long> protocolIds = new HashSet<Long>();
		AnalysisGroupValue curveIdValue = findCurveIdValue(curveId);
		if (curveIdValue == null) return new ArrayList<Long>();
		AnalysisGroup analysisGroup = curveIdValue.getLsState().getAnalysisGroup();
		Collection<Experiment> experiments = analysisGroup.getExperiments();
		for (Experiment experiment: experiments){
			protocolIds.add(experiment.getProtocol().getId());
		}
		return protocolIds;
	}
	
	private static Collection<ProtocolValue> getDisplayMinMaxByProtocolIds(Collection<Long> protocolIds){
		Collection<ProtocolValue> protocolValues = new HashSet<ProtocolValue>();
		for (Long protocolId: protocolIds){
			protocolValues.addAll(ProtocolValue.findProtocolValuesByProtocolIDAndStateTypeKindAndValueTypeKind(protocolId, "metadata", "screening assay", "numericValue", "curve display min").getResultList());
			protocolValues.addAll(ProtocolValue.findProtocolValuesByProtocolIDAndStateTypeKindAndValueTypeKind(protocolId, "metadata", "screening assay", "numericValue", "curve display max").getResultList());
		}
		return protocolValues;
	}
	
	public static Collection<ProtocolValue> findDisplayMinMaxByCurveId(String curveId){
		return getDisplayMinMaxByProtocolIds(findProtocolIdsByCurveId(curveId));
	}
	
}
