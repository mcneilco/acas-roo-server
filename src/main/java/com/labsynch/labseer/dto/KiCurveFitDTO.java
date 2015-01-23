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
public class KiCurveFitDTO {
	
	private static final Logger logger = LoggerFactory.getLogger(KiCurveFitDTO.class);

	public KiCurveFitDTO(){
		//empty constructor
	}
	
	public KiCurveFitDTO(String curveId) {
		this.curveId = curveId;
	}
	
	
	
	public KiCurveFitDTO(HashMap<String, String> stringMap, HashMap<String, BigDecimal> numericMap)
			{
		// These keys must be exactly the same as what is used in the database. Case sensitive.
				this.curveId = stringMap.get("curve id");
				this.batchCode = stringMap.get("batch code");
				this.category = stringMap.get("category");
				this.renderingHint = stringMap.get("Rendering Hint");
				this.min = String.valueOf(numericMap.get("Min"));
				if (this.min.equals("null")) this.min = stringMap.get("Min");
				this.max = String.valueOf(numericMap.get("Max"));
				if (this.max.equals("null")) this.max = stringMap.get("Max");
				this.ki = String.valueOf(numericMap.get("Ki"));
				if (this.ki.equals("null")) this.ki = stringMap.get("Ki");
				this.minUnits = stringMap.get("Min units");
				this.maxUnits = stringMap.get("Max units");
				this.kiUnits = stringMap.get("Ki units");
				this.kd = numericMap.get("Kd");
				this.kdUnits = stringMap.get("Kd units");
				this.kdUncertainty = numericMap.get("Kd uncertainty");
				this.kdUncertaintyType = stringMap.get("Kd uncertainty type");
				this.ligandConc = numericMap.get("Ligand Conc");
				this.ligandConcUnits = stringMap.get("Ligand Conc units");
				this.ligandConcUncertainty = numericMap.get("Ligand Conc uncertainty");
				this.ligandConcUncertaintyType = stringMap.get("Ligand Conc uncertainty type");
				this.minUncertainty = numericMap.get("Min uncertainty");
				this.maxUncertainty = numericMap.get("Max uncertainty");
				this.kiUncertainty = numericMap.get("Ki uncertainty");
				this.minUncertaintyType = stringMap.get("Min uncertainty type");
				this.maxUncertaintyType = stringMap.get("Max uncertainty type");
				this.kiUncertaintyType = stringMap.get("Ki uncertainty type");
				this.minOperatorKind = stringMap.get("Min operator kind");
				this.maxOperatorKind = stringMap.get("Max operator kind");
				this.kiOperatorKind = stringMap.get("Ki operator kind");
				this.ligandConcOperatorKind = stringMap.get("Ligand Conc operator kind");
				this.kdOperatorKind = stringMap.get("Kd operator kind");
				this.fittedMin = numericMap.get("Fitted Min");
				this.fittedMax = numericMap.get("Fitted Max");
				this.fittedKi = numericMap.get("Fitted Ki");
				this.fittedMinUncertainty = numericMap.get("Fitted Min uncertainty");
				this.fittedMaxUncertainty = numericMap.get("Fitted Max uncertainty");
				this.fittedKiUncertainty = numericMap.get("Fitted Ki uncertainty");
				this.fittedMinUncertaintyType = stringMap.get("Fitted Min uncertainty type");
				this.fittedMaxUncertaintyType = stringMap.get("Fitted Max uncertainty type");
				this.fittedKiUncertaintyType = stringMap.get("Fitted Ki uncertainty type");
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
	private String minUnits;
	private String maxUnits;
	private String ki;
	private String kiUnits;
	private BigDecimal ligandConc;
	private String ligandConcUnits;
	private BigDecimal kd;
	private String kdUnits;
	private BigDecimal minUncertainty;
	private BigDecimal maxUncertainty;
	private BigDecimal kiUncertainty;
	private BigDecimal ligandConcUncertainty;
	private BigDecimal kdUncertainty;
	private String minUncertaintyType;
	private String maxUncertaintyType;
	private String kiUncertaintyType;
	private String ligandConcUncertaintyType;
	private String kdUncertaintyType;
	private String minOperatorKind;
	private String maxOperatorKind;
	private String kiOperatorKind;
	private String ligandConcOperatorKind;
	private String kdOperatorKind;
	private BigDecimal fittedMin;
	private BigDecimal fittedMax;
	private BigDecimal fittedKi;
	private BigDecimal fittedMinUncertainty;
	private BigDecimal fittedMaxUncertainty;
	private BigDecimal fittedKiUncertainty;
	private String fittedMinUncertaintyType;
	private String fittedMaxUncertaintyType;
	private String fittedKiUncertaintyType;
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
				"ki",
				"ligandConc",
				"kd",
				"minUnits",
				"maxUnits",
				"kiUnits",
				"ligandConcUnits",
				"kdUnits",
				"minUncertainty",
				"maxUncertainty",
				"kiUncertainty",
				"ligandConcUncertainty",
				"kdUncertainty",
				"minUncertaintyType",
				"maxUncertaintyType",
				"kiUncertaintyType",
				"ligandConcUncertaintyType",
				"kdUncertaintyType",
				"minOperatorKind",
				"maxOperatorKind",
				"kiOperatorKind",
				"ligandConcOperatorKind",
				"kdOperatorKind",
				"fittedMin",
				"fittedMax",
				"fittedKi",
				"fittedMinUncertainty",
				"fittedMaxUncertainty",
				"fittedKiUncertainty",
				"fittedMinUncertaintyType",
				"fittedMaxUncertaintyType",
				"fittedKiUncertaintyType",
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
				new Optional(),
				new Optional()
		};

		return processors;
	}

	public static Collection<KiCurveFitDTO> getFitData(
			Collection<KiCurveFitDTO> curveFitDTOs) {
		Collection<KiCurveFitDTO> filledCurveFitDTOs = new HashSet<KiCurveFitDTO>();
		for (KiCurveFitDTO curveFitDTO : curveFitDTOs) {
			filledCurveFitDTOs.add(getFitData(curveFitDTO));
		}
		return filledCurveFitDTOs;
	}
	
	@Transactional
	public static KiCurveFitDTO getFitData(KiCurveFitDTO curveFitDTO){
		AnalysisGroupValue curveIdValue = findCurveIdValue(curveFitDTO.getCurveId());
		if (curveIdValue.getStateId() == null) {
			logger.debug("No data found for curve id: " + curveFitDTO.getCurveId());
			return new KiCurveFitDTO();
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
		curveFitDTO = new KiCurveFitDTO(stringMap, numericMap);
		curveFitDTO.setAnalysisGroupCode(doseResponseState.getAnalysisGroup().getCodeName());
		return curveFitDTO;
	}
	
	public static String getCsvList(Collection<KiCurveFitDTO> curveFitDTOs, String format) {
		//format is ALWAYS tsv or csv (not case sensitive)
		StringWriter outFile = new StringWriter();
        ICsvBeanWriter beanWriter = null;
        try {
            if (format.equalsIgnoreCase("csv")) beanWriter = new CsvBeanWriter(outFile, CsvPreference.STANDARD_PREFERENCE);
            else beanWriter = new CsvBeanWriter(outFile, CsvPreference.TAB_PREFERENCE);
            final String[] header = KiCurveFitDTO.getColumns();
            final CellProcessor[] processors = KiCurveFitDTO.getProcessors();
            beanWriter.writeHeader(header);
            for (final KiCurveFitDTO curveFitDTO : curveFitDTOs) {
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

	public static void updateFitData( Collection<KiCurveFitDTO> curveFitDTOs) {
		for (KiCurveFitDTO curveFitDTO : curveFitDTOs) {
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
	private static void saveFitData(AnalysisGroupState state, KiCurveFitDTO curveFitDTO) {
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
		String ki = curveFitDTO.getKi();
		BigDecimal ligandConc = curveFitDTO.getLigandConc();
		BigDecimal kd = curveFitDTO.getKd();
		BigDecimal fittedMin = curveFitDTO.getFittedMin();
		BigDecimal fittedMax = curveFitDTO.getFittedMax();
		BigDecimal fittedKi = curveFitDTO.getFittedKi();
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
		//Min, Max, and Ki are special cases. They must be filled in with something for Seurat,
		//so we fill them in as stringValues if their value is not numeric
		if (!(min==null) && SimpleUtil.isDecimalNumeric(min)) {
			AnalysisGroupValue minValue = createCurveFitValue(state, "numericValue", "Min", new BigDecimal(min), recordedBy);
			minValue.setUnitKind(curveFitDTO.getMinUnits());
			minValue.setUncertainty(curveFitDTO.getMinUncertainty());
			minValue.setUncertaintyType(curveFitDTO.getMinUncertaintyType());
			minValue.setOperatorKind(curveFitDTO.getMinOperatorKind());
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
			maxValue.setPublicData(true);
			newValues.add(maxValue);
		} else {
			AnalysisGroupValue maxValue = createCurveFitValue(state, "stringValue", "Max", max, recordedBy);
			maxValue.setPublicData(true);
			newValues.add(maxValue);
		}
		if (!(ki==null) && SimpleUtil.isDecimalNumeric(ki)) {
			AnalysisGroupValue kiValue = createCurveFitValue(state, "numericValue", "Ki", new BigDecimal(ki), recordedBy);
			kiValue.setUnitKind(curveFitDTO.getKiUnits());
			kiValue.setUncertainty(curveFitDTO.getKiUncertainty());
			kiValue.setUncertaintyType(curveFitDTO.getKiUncertaintyType());
			kiValue.setOperatorKind(curveFitDTO.getKiOperatorKind());
			kiValue.setPublicData(true);
			newValues.add(kiValue);
		} else {
			AnalysisGroupValue kiValue = createCurveFitValue(state, "stringValue", "Ki", ki, recordedBy);
			kiValue.setCodeValue(batchCode);
			kiValue.setPublicData(true);
			newValues.add(kiValue);
		}
		//Remaining non-special numericValues
		if (!(ligandConc==null)){
			AnalysisGroupValue ligandConcValue = createCurveFitValue(state, "numericValue", "Ligand Conc", ligandConc, recordedBy);
			ligandConcValue.setCodeValue(batchCode);
			ligandConcValue.setUncertainty(curveFitDTO.getLigandConcUncertainty());
			ligandConcValue.setUncertaintyType(curveFitDTO.getLigandConcUncertaintyType());
			newValues.add(ligandConcValue);
		}
		if (!(kd==null)){
			AnalysisGroupValue kdValue = createCurveFitValue(state, "numericValue", "Kd", kd, recordedBy);
			kdValue.setCodeValue(batchCode);
			kdValue.setUncertainty(curveFitDTO.getKdUncertainty());
			kdValue.setUncertaintyType(curveFitDTO.getKdUncertaintyType());
			newValues.add(kdValue);
		}
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
		if (!(fittedKi==null)){
			AnalysisGroupValue fittedKiValue = createCurveFitValue(state, "numericValue", "Fitted Ki", fittedKi, recordedBy);
			fittedKiValue.setCodeValue(batchCode);
			fittedKiValue.setUncertainty(curveFitDTO.getFittedKiUncertainty());
			fittedKiValue.setUncertaintyType(curveFitDTO.getFittedKiUncertaintyType());
			newValues.add(fittedKiValue);
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
	
	public static Collection<KiCurveFitDTO> getFitDataByExperiment(String experimentIdOrCodeName){
		Collection<KiCurveFitDTO> curveFitDTOs = makeCurveFitDTOsFromCurveIdList(findAllCurveIdsByExperiment(experimentIdOrCodeName));
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
	
	private static Collection<KiCurveFitDTO> makeCurveFitDTOsFromCurveIdList(Collection<String> curveIdList) {
		Collection<KiCurveFitDTO> curveFitDTOs = new HashSet<KiCurveFitDTO>();
		for (String curveId : curveIdList) {
			curveFitDTOs.add(new KiCurveFitDTO(curveId));
		}
		return curveFitDTOs;
	}
	
	public static Collection<KiCurveFitDTO> getFitData(List<String> curveIds) {
		Collection<KiCurveFitDTO> curveFitDTOs = new HashSet<KiCurveFitDTO>();
		for (String curveId : curveIds){
			KiCurveFitDTO curveFitDTO = new KiCurveFitDTO(curveId);
			curveFitDTOs.add(curveFitDTO);
		}
		return getFitData(curveFitDTOs);
	}
	
}
