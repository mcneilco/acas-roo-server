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
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.Protocol;
import com.labsynch.labseer.domain.ProtocolValue;
import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.domain.SubjectValue;
import com.labsynch.labseer.domain.TreatmentGroup;
import com.labsynch.labseer.service.AnalysisGroupStateService;
import com.labsynch.labseer.service.AnalysisGroupValueService;
import com.labsynch.labseer.utils.SimpleUtil;

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
				new Optional()
		};

		return processors;
	}

	public static Collection<CurveFitDTO> getFitData(
			Collection<CurveFitDTO> curveFitDTOs) {
		Collection<CurveFitDTO> filledCurveFitDTOs = new HashSet<CurveFitDTO>();
		for (CurveFitDTO curveFitDTO : curveFitDTOs) {
			filledCurveFitDTOs.add(getFitData(curveFitDTO));
		}
		return filledCurveFitDTOs;
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
//				oldState.flush();
			} catch(EmptyResultDataAccessException e) {
				logger.debug("Old state of typekind data/dose response not found for AG Code " + curveFitDTO.getAnalysisGroupCode() + " , creating new one");
			}
			AnalysisGroupState newState = createCurveFitState(analysisGroup.getId(), "data", "dose response", curveFitDTO.getRecordedBy());
			saveFitData(newState, curveFitDTO);
		}
	}
	
	@Transactional
	private static void saveFitData(AnalysisGroupState state, CurveFitDTO curveFitDTO) {
		Collection<AnalysisGroupValue> newValues = new HashSet<AnalysisGroupValue>();
		//non-optional fields
		String recordedBy = curveFitDTO.getRecordedBy();
		String batchCode = curveFitDTO.getBatchCode();
		AnalysisGroupValue batchCodeValue = createCurveFitValue(state, "codeValue", "batch code", curveFitDTO.getBatchCode(), recordedBy);
		batchCodeValue.setPublicData(true);
		newValues.add(batchCodeValue);
		AnalysisGroupValue curveIdValue = createCurveFitValue(state, "stringValue", "curve id", curveFitDTO.getCurveId(), recordedBy);
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
			AnalysisGroupValue categoryValue = createCurveFitValue(state, "stringValue", "category", category, recordedBy);
			categoryValue.setCodeValue(batchCode);
			newValues.add(categoryValue);
		}
		if (!(renderingHint==null)){
			AnalysisGroupValue renderingHintValue = createCurveFitValue(state, "stringValue", "Rendering Hint", renderingHint, recordedBy);
			renderingHintValue.setCodeValue(batchCode);
			newValues.add(renderingHintValue);
		}
		//Min, Max, EC50, and Slope are special cases. They must be filled in with something for Seurat,
		//so we fill them in as stringValues if their value is not numeric
		if (!(min==null) && SimpleUtil.isDecimalNumeric(min)) {
			AnalysisGroupValue minValue = createCurveFitValue(state, "numericValue", "Min", new BigDecimal(min), recordedBy);
			minValue.setUnitKind(curveFitDTO.getMinUnits());
			minValue.setUncertainty(curveFitDTO.getMinUncertainty());
			minValue.setUncertaintyType(curveFitDTO.getMinUncertaintyType());
			minValue.setOperatorKind(curveFitDTO.getMinOperatorKind());
			minValue.setCodeValue(batchCode);
			minValue.setPublicData(true);
			newValues.add(minValue);
		} else {
			AnalysisGroupValue minValue = createCurveFitValue(state, "stringValue", "Min", min, recordedBy);
			minValue.setCodeValue(batchCode);
			minValue.setPublicData(true);
			newValues.add(minValue);
		}
		if (!(max==null) && SimpleUtil.isDecimalNumeric(max)) {
			AnalysisGroupValue maxValue = createCurveFitValue(state, "numericValue", "Max", new BigDecimal(max), recordedBy);
			maxValue.setUnitKind(curveFitDTO.getMaxUnits());
			maxValue.setUncertainty(curveFitDTO.getMaxUncertainty());
			maxValue.setUncertaintyType(curveFitDTO.getMaxUncertaintyType());
			maxValue.setOperatorKind(curveFitDTO.getMaxOperatorKind());
			maxValue.setCodeValue(batchCode);
			maxValue.setPublicData(true);
			newValues.add(maxValue);
		} else {
			AnalysisGroupValue maxValue = createCurveFitValue(state, "stringValue", "Max", max, recordedBy);
			maxValue.setCodeValue(batchCode);
			maxValue.setPublicData(true);
			newValues.add(maxValue);
		}
		if (!(ec50==null) && SimpleUtil.isDecimalNumeric(ec50)) {
			AnalysisGroupValue ec50Value = createCurveFitValue(state, "numericValue", "EC50", new BigDecimal(ec50), recordedBy);
			ec50Value.setUnitKind(curveFitDTO.getEc50Units());
			ec50Value.setUncertainty(curveFitDTO.getEc50Uncertainty());
			ec50Value.setUncertaintyType(curveFitDTO.getEc50UncertaintyType());
			ec50Value.setOperatorKind(curveFitDTO.getEc50OperatorKind());
			ec50Value.setCodeValue(batchCode);
			ec50Value.setPublicData(true);
			newValues.add(ec50Value);
		} else {
			AnalysisGroupValue ec50Value = createCurveFitValue(state, "stringValue", "EC50", ec50, recordedBy);
			ec50Value.setCodeValue(batchCode);
			ec50Value.setPublicData(true);
			newValues.add(ec50Value);
		}
		if (!(slope==null) && SimpleUtil.isDecimalNumeric(slope)) {
			AnalysisGroupValue slopeValue = createCurveFitValue(state, "numericValue", "Slope", new BigDecimal(slope), recordedBy);
			slopeValue.setCodeValue(batchCode);
			slopeValue.setUncertainty(curveFitDTO.getSlopeUncertainty());
			slopeValue.setUncertaintyType(curveFitDTO.getSlopeUncertaintyType());
			slopeValue.setOperatorKind(curveFitDTO.getSlopeOperatorKind());
			slopeValue.setPublicData(true);
			newValues.add(slopeValue);
		} else {
			AnalysisGroupValue slopeValue = createCurveFitValue(state, "stringValue", "Slope", slope, recordedBy);
			slopeValue.setCodeValue(batchCode);
			slopeValue.setPublicData(true);
			newValues.add(slopeValue);
		}
		//Remaining non-special numericValues
		if (!(fittedMin==null)){
			AnalysisGroupValue fittedMinValue = createCurveFitValue(state, "numericValue", "Fitted Min", fittedMin, recordedBy);
			fittedMinValue.setCodeValue(batchCode);
			fittedMinValue.setUncertainty(curveFitDTO.getFittedMinUncertainty());
			fittedMinValue.setUncertaintyType(curveFitDTO.getFittedMinUncertaintyType());
			newValues.add(fittedMinValue);
		}
		if (!(fittedMax==null)){
			AnalysisGroupValue fittedMaxValue = createCurveFitValue(state, "numericValue", "Fitted Max", fittedMax, recordedBy);
			fittedMaxValue.setCodeValue(batchCode);
			fittedMaxValue.setUncertainty(curveFitDTO.getFittedMaxUncertainty());
			fittedMaxValue.setUncertaintyType(curveFitDTO.getFittedMaxUncertaintyType());
			newValues.add(fittedMaxValue);
		}
		if (!(fittedEC50==null)){
			AnalysisGroupValue fittedEC50Value = createCurveFitValue(state, "numericValue", "Fitted EC50", fittedEC50, recordedBy);
			fittedEC50Value.setCodeValue(batchCode);
			fittedEC50Value.setUncertainty(curveFitDTO.getFittedEc50Uncertainty());
			fittedEC50Value.setUncertaintyType(curveFitDTO.getFittedEc50UncertaintyType());
			newValues.add(fittedEC50Value);
		}
		if (!(fittedSlope==null)){
			AnalysisGroupValue fittedSlopeValue = createCurveFitValue(state, "numericValue", "Fitted Slope", fittedSlope, recordedBy);
			fittedSlopeValue.setCodeValue(batchCode);
			fittedSlopeValue.setUncertainty(curveFitDTO.getFittedSlopeUncertainty());
			fittedSlopeValue.setUncertaintyType(curveFitDTO.getFittedSlopeUncertaintyType());
			newValues.add(fittedSlopeValue);
		}
		if (!(sse==null)){
			AnalysisGroupValue sseValue = createCurveFitValue(state, "numericValue", "SSE", sse, recordedBy);
			sseValue.setCodeValue(batchCode);
			newValues.add(sseValue);
		}
		if (!(sst==null)){
			AnalysisGroupValue sstValue = createCurveFitValue(state, "numericValue", "SST", sst, recordedBy);
			sstValue.setCodeValue(batchCode);
			newValues.add(sstValue);
		}
		if (!(rSquared==null)){
			AnalysisGroupValue rSquaredValue = createCurveFitValue(state, "numericValue", "rSquared", rSquared, recordedBy);
			rSquaredValue.setCodeValue(batchCode);
			newValues.add(rSquaredValue);
		}
		//clob values
		if (!(curveErrorsClob==null)){
			AnalysisGroupValue curveErrorsClobValue = createCurveFitValue(state, "clobValue", "curveErrorsClob", curveErrorsClob, recordedBy);
			curveErrorsClobValue.setCodeValue(batchCode);
			newValues.add(curveErrorsClobValue);
		}
		if (!(reportedValuesClob==null)){
			AnalysisGroupValue reportedValuesClobValue = createCurveFitValue(state, "clobValue", "reportedValuesClob", reportedValuesClob, recordedBy);
			reportedValuesClobValue.setCodeValue(batchCode);
			newValues.add(reportedValuesClobValue);
		}
		if (!(parameterStdErrorsClob==null)){
			AnalysisGroupValue parameterStdErrorsClobValue = createCurveFitValue(state, "clobValue", "parameterStdErrorsClob", parameterStdErrorsClob, recordedBy);
			parameterStdErrorsClobValue.setCodeValue(batchCode);
			newValues.add(parameterStdErrorsClobValue);
		}
		if (!(fitSettings==null)){
			AnalysisGroupValue fitSettingsValue = createCurveFitValue(state, "clobValue", "fitSettings", fitSettings, recordedBy);
			fitSettingsValue.setCodeValue(batchCode);
			newValues.add(fitSettingsValue);
		}
		if (!(fitSummaryClob==null)){
			AnalysisGroupValue fitSummaryClobValue = createCurveFitValue(state, "clobValue", "fitSummaryClob", fitSummaryClob, recordedBy);
			fitSummaryClobValue.setCodeValue(batchCode);
			newValues.add(fitSummaryClobValue);
		}
		//flags
		if (!(userFlagStatus==null)){
			AnalysisGroupValue userFlagStatusValue = createCurveFitValue(state, "codeValue", "user flag status", userFlagStatus, recordedBy);
			userFlagStatusValue.setCodeType("user well flags");
			userFlagStatusValue.setCodeKind("flag status");
			userFlagStatusValue.setCodeOrigin("ACAS Curve Curator");
			newValues.add(userFlagStatusValue);
		}
		if (!(algorithmFlagStatus==null)){
			AnalysisGroupValue algorithmFlagStatusValue = createCurveFitValue(state, "codeValue", "algorithm flag status", algorithmFlagStatus, recordedBy);
			algorithmFlagStatusValue.setCodeType("algorithm well flags");
			algorithmFlagStatusValue.setCodeKind("flag status");
			algorithmFlagStatusValue.setCodeOrigin("ACAS Curve Fit Module");
		}
		
		//persist and flush all the new values
		for (AnalysisGroupValue value: newValues){
			value.persist();
//			value.flush();
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
        TypedQuery<AnalysisGroupValue> q = em.createQuery("SELECT o FROM AnalysisGroupValue AS o JOIN o.lsState AS state JOIN state.analysisGroup AS ag WHERE o.lsType = :lsType  AND o.lsKind = :lsKind  AND LOWER(o.stringValue) LIKE LOWER(:stringValue)  AND o.ignored IS NOT :ignored AND state.ignored IS NOT :ignored AND ag.ignored IS NOT :ignored AND state.lsType = :stateType AND state.lsKind = :stateKind", AnalysisGroupValue.class);
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
	
	public static Collection<CurveFitDTO> getFitDataByExperiment(String experimentIdOrCodeName){
		Collection<CurveFitDTO> curveFitDTOs = makeCurveFitDTOsFromCurveIdList(findAllCurveIdsByExperiment(experimentIdOrCodeName));
		curveFitDTOs = getFitData(curveFitDTOs);
		return curveFitDTOs;
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
		AnalysisGroup analysisGroup = curveIdValue.getLsState().getAnalysisGroup();
		return findRenderingHint(analysisGroup);
	}

	public static Collection<CurveFitDTO> getFitData(List<String> curveIds) {
		return getFitData(makeCurveFitDTOsFromCurveIdList(curveIds));
	}
	
	private static Collection<Long> findProtocolIdsByCurveId(String curveId) {
		Collection<Long> protocolIds = new HashSet<Long>();
		AnalysisGroupValue curveIdValue = findCurveIdValue(curveId);
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
