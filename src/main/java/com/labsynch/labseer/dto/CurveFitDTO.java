package com.labsynch.labseer.dto;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Transient;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
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
import com.labsynch.labseer.service.AnalysisGroupStateService;
import com.labsynch.labseer.service.AnalysisGroupValueService;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

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
	
	
	
	public CurveFitDTO(HashMap<String, String> stringMap, HashMap<String, BigDecimal> numericMap)
			{
		// These keys must be exactly the same as what is used in the database. Case sensitive.
		//TODO: finalize these kinds, make sure they match exactly what is being used.
				this.curveId = stringMap.get("curve id");
				this.batchCode = stringMap.get("batch code");
				this.category = stringMap.get("Category");
				this.renderingHint = stringMap.get("Rendering Hint");
				this.min = numericMap.get("Min");
				this.max = numericMap.get("Max");
				this.ec50 = numericMap.get("EC50");
				this.minUnits = stringMap.get("Min units");
				this.maxUnits = stringMap.get("Max units");
				this.ec50Units = stringMap.get("EC50 units");
				this.slope = numericMap.get("Slope");
				this.fittedMin = numericMap.get("Fitted Min");
				this.fittedMax = numericMap.get("Fitted Max");
				this.fittedEC50 = numericMap.get("Fitted EC50");
				this.fittedSlope = numericMap.get("Fitted Slope");
				this.sse = numericMap.get("SSE");
				this.sst = numericMap.get("SST");
				this.rSquared = numericMap.get("rSquared");
				this.curveErrorsClob = stringMap.get("curveErrorsClob");
				this.reportedValuesClob = stringMap.get("reportedValuesClob");
				this.parameterStdErrorsClob = stringMap.get("parameterStdErrorsClob");
				this.fitSettings = stringMap.get("fitSettings");
				this.fitSummaryClob = stringMap.get("fitSummaryClob");
				this.flagStatus = stringMap.get("flag status");
				this.flagObservation = stringMap.get("flag observation");
				this.flagReason = stringMap.get("flag reason");
				this.flagComment = stringMap.get("flag comment");
	}


	private String curveId;
	private String analysisGroupCode;
	private String recordedBy;
	private String batchCode;
	private String category;
	private String renderingHint;
	private BigDecimal min;
	private BigDecimal max;
	private BigDecimal ec50;
	private String minUnits;
	private String maxUnits;
	private String ec50Units;
	private BigDecimal slope;
	private BigDecimal fittedMin;
	private BigDecimal fittedMax;
	private BigDecimal fittedEC50;
	private BigDecimal fittedSlope;
	private BigDecimal sse;
	private BigDecimal sst;
	private BigDecimal rSquared;
	private String curveErrorsClob;
	private String reportedValuesClob;
	private String parameterStdErrorsClob;
	private String fitSettings;
	private String fitSummaryClob;
	private String flagStatus;
	private String flagObservation;
	private String flagReason;
	private String flagComment;
	
	


	public static String[] getColumns(){
		String[] headerColumns = new String[] {
				"id", 
				"curveId",
				"analysisGroupCode",
				"recordedBy",
				"batchCode",
				"Category",
				"renderingHint",
				"min",
				"max",
				"ec50",
				"minUnits",
				"maxUnits",
				"ec50Units",
				"slope",
				"fittedMin",
				"fittedMax",
				"fittedEC50",
				"fittedSlope",
				"sse",
				"sst",
				"rSquared",
				"curveErrorsClob",
				"reportedValuesClob",
				"parameterStdErrorsClob",
				"fitSettings",
				"fitSummaryClob",
				"flagStatus",
				"flagObservation",
				"flagReason",
				"flagComment"
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
				new Optional()
		};

		return processors;
	}

	public static Collection<CurveFitDTO> getFitData(
			Collection<CurveFitDTO> curveFitDTOs) {
		for (CurveFitDTO curveFitDTO : curveFitDTOs) {
			curveFitDTO = getFitData(curveFitDTO);
		}
		return curveFitDTOs;
	}
	
	@Transactional
	public static CurveFitDTO getFitData(CurveFitDTO curveFitDTO){
		AnalysisGroupValue curveIdValue = findCurveIdValue(curveFitDTO.getCurveId());
		if (curveIdValue.getStateId() == null) {
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
			}
			else if (agValue.getLsType().equals("clobValue")) {
				stringMap.put(agValue.getLsKind(), agValue.getClobValue());
			}
			else if (agValue.getLsType().equals("numericValue")) {
				numericMap.put(agValue.getLsKind(), agValue.getNumericValue());
				if(agValue.getUnitKind() != null) {
					stringMap.put(agValue.getUnitKind()+" units", agValue.getUnitKind());
				}
			}
		}
		curveFitDTO = new CurveFitDTO(stringMap, numericMap);
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
			AnalysisGroup analysisGroup = AnalysisGroup.findAnalysisGroupsByCodeNameEquals(curveFitDTO.getAnalysisGroupCode()).getSingleResult();
			try {
				AnalysisGroupState oldState = AnalysisGroupState.findAnalysisGroupStatesByAnalysisGroupAndLsTypeEqualsAndLsKindEqualsAndIgnoredNot(analysisGroup, "data", "dose response", true).getSingleResult();
				oldState.setIgnored(true);
				oldState.merge();
				oldState.flush();
			} catch(EmptyResultDataAccessException e) {
				logger.debug("Old state of typekind data/dose response not found for AG Code " + curveFitDTO.getAnalysisGroupCode() + " , creating new one");
			}
			AnalysisGroupState newState = createCurveFitState(analysisGroup.getId(), "data", "dose response", curveFitDTO.getRecordedBy());
			saveFitData(newState, curveFitDTO);
		}
	}
	
	@Transactional
	private static void saveFitData(AnalysisGroupState state, CurveFitDTO curveFitDTO) {
		String recordedBy = curveFitDTO.getRecordedBy();
		String batchCode = curveFitDTO.getBatchCode();
		AnalysisGroupValue batchCodeValue = createCurveFitValue(state, "codeValue", "batch code", curveFitDTO.getBatchCode(), recordedBy);
		AnalysisGroupValue curveIdValue = createCurveFitValue(state, "stringValue", "curve id", curveFitDTO.getCurveId(), recordedBy);
		AnalysisGroupValue categoryValue = createCurveFitValue(state, "stringValue", "Category", curveFitDTO.getCategory(), recordedBy);
		AnalysisGroupValue renderingHintValue = createCurveFitValue(state, "stringValue", "Rendering Hint", curveFitDTO.getRenderingHint(), recordedBy);
		AnalysisGroupValue minValue = createCurveFitValue(state, "numericValue", "Min", curveFitDTO.getMin(), recordedBy);
		minValue.setUnitKind(curveFitDTO.getMinUnits());
		AnalysisGroupValue maxValue = createCurveFitValue(state, "numericValue", "Max", curveFitDTO.getMax(), recordedBy);
		maxValue.setUnitKind(curveFitDTO.getMaxUnits());
		AnalysisGroupValue ec50Value = createCurveFitValue(state, "numericValue", "EC50", curveFitDTO.getEc50(), recordedBy);
		ec50Value.setUnitKind(curveFitDTO.getEc50Units());
		AnalysisGroupValue slopeValue = createCurveFitValue(state, "numericValue", "Slope", curveFitDTO.getSlope(), recordedBy);
		AnalysisGroupValue fittedMinValue = createCurveFitValue(state, "numericValue", "Fitted Min", curveFitDTO.getFittedMin(), recordedBy);
		AnalysisGroupValue fittedMaxValue = createCurveFitValue(state, "numericValue", "Fitted Max", curveFitDTO.getFittedMax(), recordedBy);
		AnalysisGroupValue fittedEC50Value = createCurveFitValue(state, "numericValue", "Fitted EC50", curveFitDTO.getFittedEC50(), recordedBy);
		AnalysisGroupValue fittedSlopeValue = createCurveFitValue(state, "numericValue", "Fitted Slope", curveFitDTO.getFittedSlope(), recordedBy);
		AnalysisGroupValue sseValue = createCurveFitValue(state, "numericValue", "SSE", curveFitDTO.getSse(), recordedBy);
		AnalysisGroupValue sstValue = createCurveFitValue(state, "numericValue", "SST", curveFitDTO.getSst(), recordedBy);
		AnalysisGroupValue rSquaredValue = createCurveFitValue(state, "numericValue", "rSquared", curveFitDTO.getRSquared(), recordedBy);
		//fill in batch code
		curveIdValue.setCodeValue(batchCode);
		categoryValue.setCodeValue(batchCode);
		renderingHintValue.setCodeValue(batchCode);
		minValue.setCodeValue(batchCode);
		maxValue.setCodeValue(batchCode);
		ec50Value.setCodeValue(batchCode);
		slopeValue.setCodeValue(batchCode);
		slopeValue.setCodeValue(batchCode);
		fittedMinValue.setCodeValue(batchCode);
		fittedMaxValue.setCodeValue(batchCode);
		fittedEC50Value.setCodeValue(batchCode);
		fittedSlopeValue.setCodeValue(batchCode);
		sseValue.setCodeValue(batchCode);
		sstValue.setCodeValue(batchCode);
		rSquaredValue.setCodeValue(batchCode);
		//clob values
		AnalysisGroupValue curveErrorsClobValue = createCurveFitValue(state, "clobValue", "curveErrorsClob", curveFitDTO.getCurveErrorsClob(), recordedBy);
		AnalysisGroupValue reportedValuesClobValue = createCurveFitValue(state, "clobValue", "reportedValuesClob", curveFitDTO.getReportedValuesClob(), recordedBy);
		AnalysisGroupValue parameterStdErrorsClobValue = createCurveFitValue(state, "clobValue", "parameterStdErrorsClob", curveFitDTO.getParameterStdErrorsClob(), recordedBy);
		AnalysisGroupValue fitSettingsClobValue = createCurveFitValue(state, "clobValue", "fitSettings", curveFitDTO.getFitSettings(), recordedBy);
		AnalysisGroupValue fitSummaryClobValue = createCurveFitValue(state, "clobValue", "fitSummaryClob", curveFitDTO.getFitSummaryClob(), recordedBy);
		//flags
		String flagComment = curveFitDTO.getFlagComment();
		AnalysisGroupValue flagStatusValue = createCurveFitValue(state, "codeValue", "flag status", curveFitDTO.getFlagStatus(), recordedBy);
		AnalysisGroupValue flagObservationValue = createCurveFitValue(state, "codeValue", "flag observation", curveFitDTO.getFlagObservation(), recordedBy);
		AnalysisGroupValue flagReasonValue = createCurveFitValue(state, "codeValue", "flag reason", curveFitDTO.getFlagReason(), recordedBy);
		AnalysisGroupValue flagCommentValue = createCurveFitValue(state, "stringValue", "flag comment", curveFitDTO.getFlagComment(), recordedBy);
		flagStatusValue.setComments(flagComment);
		flagObservationValue.setComments(flagComment);
		flagReasonValue.setComments(flagComment);
		flagStatusValue.setCodeType("user well flags");
		flagObservationValue.setCodeType("user well flags");
		flagReasonValue.setCodeType("user well flags");
		flagStatusValue.setCodeKind("flag status");
		flagObservationValue.setCodeKind("flag observation");
		flagReasonValue.setCodeKind("flag reason");
		//TODO: ask Guy about codeOrigin
		
		//collect all the new values
		Collection<AnalysisGroupValue> newValues = new HashSet<AnalysisGroupValue>();
		newValues.add(batchCodeValue);
		newValues.add(curveIdValue);
		newValues.add(categoryValue);
		newValues.add(renderingHintValue);
		newValues.add(minValue);
		newValues.add(maxValue);
		newValues.add(ec50Value);
		newValues.add(slopeValue);
		newValues.add(fittedMinValue);
		newValues.add(fittedMaxValue);
		newValues.add(fittedEC50Value);
		newValues.add(fittedSlopeValue);
		newValues.add(sseValue);
		newValues.add(sstValue);
		newValues.add(rSquaredValue);
		newValues.add(curveErrorsClobValue);
		newValues.add(reportedValuesClobValue);
		newValues.add(parameterStdErrorsClobValue);
		newValues.add(fitSettingsClobValue);
		newValues.add(fitSummaryClobValue);
		newValues.add(flagStatusValue);
		newValues.add(flagObservationValue);
		newValues.add(flagReasonValue);
		newValues.add(flagCommentValue);
		
		//persist and flush all the new values
		for (AnalysisGroupValue value: newValues){
			value.persist();
			value.flush();
		}		
	}
	
	@Transactional
	private static AnalysisGroupValue createCurveFitValue(AnalysisGroupState lsState, String lsType, String lsKind, String value, String recordedBy) {
		AnalysisGroupValue analysisGroupValue = new AnalysisGroupValue();
		analysisGroupValue.setLsState(lsState);
		analysisGroupValue.setLsType(lsType);
		analysisGroupValue.setLsKind(lsKind);
		if (lsType.equals("stringValue")) analysisGroupValue.setStringValue(value);
		if (lsType.equals("clobValue")) analysisGroupValue.setClobValue(value);
		if (lsType.equals("codeValue")) analysisGroupValue.setCodeValue(value);
		analysisGroupValue.setRecordedBy(recordedBy);
		logger.debug("Creating value kind: " + analysisGroupValue.toJson());
		analysisGroupValue.persist();
		return analysisGroupValue;
	}
	
	@Transactional
	private static AnalysisGroupValue createCurveFitValue(AnalysisGroupState lsState, String lsType, String lsKind, BigDecimal value, String recordedBy) {
		AnalysisGroupValue analysisGroupValue = new AnalysisGroupValue();
		analysisGroupValue.setLsState(lsState);
		analysisGroupValue.setLsType(lsType);
		analysisGroupValue.setLsKind(lsKind);
		if (lsType.equals("numericValue")) analysisGroupValue.setNumericValue(value);
		analysisGroupValue.setRecordedBy(recordedBy);
		logger.debug("Creating value kind: " + analysisGroupValue.toJson());
		analysisGroupValue.persist();
		return analysisGroupValue;
	}
	
	@Transactional
	private static AnalysisGroupState createCurveFitState(Long analysisGroupId, String stateType, String stateKind, String recordedBy) {
		AnalysisGroupState analysisGroupState = new AnalysisGroupState();
		AnalysisGroup analysisGroup = AnalysisGroup.findAnalysisGroup(analysisGroupId);
		analysisGroupState.setAnalysisGroup(analysisGroup);
		analysisGroupState.setLsType(stateType);
		analysisGroupState.setLsKind(stateKind);
		analysisGroupState.setRecordedBy(recordedBy);
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
        TypedQuery<AnalysisGroupValue> q = em.createQuery("SELECT o FROM AnalysisGroupValue AS o JOIN o.lsState AS state WHERE o.lsType = :lsType  AND o.lsKind = :lsKind  AND LOWER(o.stringValue) LIKE LOWER(:stringValue)  AND o.ignored IS NOT :ignored AND state.ignored IS NOT :ignored AND state.lsType = :stateType AND state.lsKind = :stateKind", AnalysisGroupValue.class);
        q.setParameter("lsType", "stringValue");
        q.setParameter("lsKind", "curve id");
        q.setParameter("stateType", "data");
        q.setParameter("stateKind", "dose response");
        q.setParameter("stringValue", curveId);
        q.setParameter("ignored", true);
        try{
        	return q.getSingleResult();
        }catch(Exception e){
        	logger.error("ERROR: More than one non-ignored curve id found");
        	logger.error("Specific error: " + e.getMessage());
        	logger.debug("Found: ");
        	List<AnalysisGroupValue> results = q.getResultList();
        	for (AnalysisGroupValue result: results){
        		logger.debug(result.toJson());
        	}
        	return new AnalysisGroupValue();
        }
	}
	
}
