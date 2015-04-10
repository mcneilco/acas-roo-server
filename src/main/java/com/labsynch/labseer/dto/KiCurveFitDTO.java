package com.labsynch.labseer.dto;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import com.labsynch.labseer.domain.SubjectValue;
import com.labsynch.labseer.utils.SimpleUtil;

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
	
	public KiCurveFitDTO(Map dataMap){
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
		if (dataMap.get("kiNumeric") != null) this.ki = ((BigDecimal) dataMap.get("kiNumeric")).toString();
		else this.ki = (String) dataMap.get("kiString");
		this.minUnits = (String) dataMap.get("minUnits");
		this.maxUnits = (String) dataMap.get("maxUnits");
		this.kiUnits = (String) dataMap.get("kiUnits");
		this.kd = (BigDecimal) dataMap.get("kd");
		this.kdUnits = (String) dataMap.get("kdUnits");
		this.kdUncertainty = (BigDecimal) dataMap.get("kdUncertainty");
		this.kdUncertaintyType = (String) dataMap.get("kdUncertaintyType");
		this.ligandConc = (BigDecimal) dataMap.get("ligandConc");
		this.ligandConcUnits = (String) dataMap.get("ligandConcUnits");
		this.ligandConcUncertainty = (BigDecimal) dataMap.get("ligandConcUncertainty");
		this.ligandConcUncertaintyType = (String) dataMap.get("ligandConcUncertaintyType");
		this.minUncertainty = (BigDecimal) dataMap.get("minUncertainty");
		this.maxUncertainty = (BigDecimal) dataMap.get("maxUncertainty");
		this.kiUncertainty = (BigDecimal) dataMap.get("kiUncertainty");
		this.minUncertaintyType = (String) dataMap.get("minUncertaintyType");
		this.maxUncertaintyType = (String) dataMap.get("maxUncertaintyType");
		this.kiUncertaintyType = (String) dataMap.get("kiUncertaintyType");
		this.minOperatorKind = (String) dataMap.get("minOperatorKind");
		this.maxOperatorKind = (String) dataMap.get("maxOperatorKind");
		this.kiOperatorKind = (String) dataMap.get("kiOperatorKind");
		this.ligandConcOperatorKind = (String) dataMap.get("ligandConcOperatorKind");
		this.kdOperatorKind = (String) dataMap.get("kdOperatorKind");
		this.fittedMin = (BigDecimal) dataMap.get("fittedMin");
		this.fittedMax = (BigDecimal) dataMap.get("fittedMax");
		this.fittedKi = (BigDecimal) dataMap.get("fittedKi");
		this.fittedMinUncertainty = (BigDecimal) dataMap.get("fittedMinUncertainty");
		this.fittedMaxUncertainty = (BigDecimal) dataMap.get("fittedMaxUncertainty");
		this.fittedKiUncertainty = (BigDecimal) dataMap.get("fittedKiUncertainty");
		this.fittedMinUncertaintyType = (String) dataMap.get("fittedMinUncertaintyType");
		this.fittedMaxUncertaintyType = (String) dataMap.get("fittedMaxUncertaintyType");
		this.fittedKiUncertaintyType = (String) dataMap.get("fittedKiUncertaintyType");
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
				"recordedDate",
				"lsTransaction",
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
				new Optional(),
				new Optional(),
				new Optional()
		};

		return processors;
	}
	
	@Transactional
	public static Collection<KiCurveFitDTO> getFitData(Collection<String> curveIds){
		EntityManager em = SubjectValue.entityManager();
		if (curveIds.isEmpty()) return new ArrayList<KiCurveFitDTO>();
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
        		+ "kiValue.stringValue as kiString, "
        		+ "kiValue.numericValue as kiNumeric, "
        		+ "minValue.unitKind as minUnits, "
        		+ "maxValue.unitKind as maxUnits, "
        		+ "kiValue.unitKind as kiUnits, "
        		+ "kdValue.numericValue as kd, "
        		+ "kdValue.unitKind as kdUnits, "
        		+ "kdValue.uncertainty as kdUncertainty, "
        		+ "kdValue.uncertaintyType as kdUncertaintyType, "
        		+ "ligandConcValue.numericValue as ligandConc, "
        		+ "ligandConcValue.unitKind as ligandConcUnits, "
        		+ "ligandConcValue.uncertainty as ligandConcUncertainty, "
        		+ "ligandConcValue.uncertaintyType as ligandConcUncertaintyType, "
        		+ "minValue.uncertainty as minUncertainty, "
        		+ "maxValue.uncertainty as maxUncertainty, "
        		+ "kiValue.uncertainty as kiUncertainty, "
        		+ "minValue.uncertaintyType as minUncertaintyType, "
        		+ "maxValue.uncertaintyType as maxUncertaintyType, "
        		+ "kiValue.uncertaintyType as kiUncertaintyType, "
        		+ "minValue.operatorKind as minOperatorKind, "
        		+ "maxValue.operatorKind as maxOperatorKind, "
        		+ "kiValue.operatorKind as kiOperatorKind, "
        		+ "ligandConcValue.operatorKind as ligandConcOperatorKind, "
        		+ "kdValue.operatorKind as kdValueOperatorKind, "
        		+ "fittedMinValue.numericValue as fittedMin, "
        		+ "fittedMaxValue.numericValue as fittedMax, "
        		+ "fittedKiValue.numericValue as fittedKi, "
        		+ "fittedMinValue.uncertainty as fittedMinUncertainty, "
        		+ "fittedMaxValue.uncertainty as fittedMaxUncertainty, "
        		+ "fittedKiValue.uncertainty as fittedKiUncertainty, "
        		+ "fittedMinValue.uncertaintyType as fittedMinUncertaintyType, "
        		+ "fittedMaxValue.uncertaintyType as fittedMaxUncertaintyType, "
        		+ "fittedKiValue.uncertaintyType as fittedKiUncertaintyType, "
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
				+ "LEFT JOIN ags.lsValues as kiValue WITH kiValue.lsKind = 'Ki' "
				+ "LEFT JOIN ags.lsValues as kdValue WITH kdValue.lsKind = 'Kd' "
				+ "LEFT JOIN ags.lsValues as ligandConcValue WITH ligandConcValue.lsKind = 'Ligand Conc' "
				+ "LEFT JOIN ags.lsValues as fittedMinValue WITH fittedMinValue.lsKind = 'Fitted Min' "
				+ "LEFT JOIN ags.lsValues as fittedMaxValue WITH fittedMaxValue.lsKind = 'Fitted Max' "
				+ "LEFT JOIN ags.lsValues as fittedKiValue WITH fittedKiValue.lsKind = 'Fitted Ki' "
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
        List<KiCurveFitDTO> curveFitDTOList = new ArrayList<KiCurveFitDTO>();
		for (Map result : queryResults) {
			KiCurveFitDTO curveFitDTO = new KiCurveFitDTO(result);
			curveFitDTOList.add(curveFitDTO);
		}
		return curveFitDTOList;
		
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
			saveFitData(newState, curveFitDTO);
		}
	}
	
	@Transactional
	private static void saveFitData(AnalysisGroupState state, KiCurveFitDTO curveFitDTO) {
		Collection<AnalysisGroupValue> newValues = new HashSet<AnalysisGroupValue>();
		//non-optional fields
		String recordedBy = curveFitDTO.getRecordedBy();
		String batchCode = curveFitDTO.getBatchCode();
		Long lsTransaction = curveFitDTO.getLsTransaction();
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
			AnalysisGroupValue categoryValue = createCurveFitValue(state, "stringValue", "category", category, recordedBy, lsTransaction);
			categoryValue.setCodeValue(batchCode);
			newValues.add(categoryValue);
		}
		if (!(renderingHint==null)){
			AnalysisGroupValue renderingHintValue = createCurveFitValue(state, "stringValue", "Rendering Hint", renderingHint, recordedBy, lsTransaction);
			renderingHintValue.setCodeValue(batchCode);
			newValues.add(renderingHintValue);
		}
		//Min, Max, and Ki are special cases. They must be filled in with something for Seurat,
		//so we fill them in as stringValues if their value is not numeric
		if (!(min==null) && SimpleUtil.isDecimalNumeric(min)) {
			AnalysisGroupValue minValue = createCurveFitValue(state, "numericValue", "Min", new BigDecimal(min), recordedBy, lsTransaction);
			minValue.setUnitKind(curveFitDTO.getMinUnits());
			minValue.setUncertainty(curveFitDTO.getMinUncertainty());
			minValue.setUncertaintyType(curveFitDTO.getMinUncertaintyType());
			minValue.setOperatorKind(curveFitDTO.getMinOperatorKind());
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
			maxValue.setPublicData(true);
			newValues.add(maxValue);
		} else {
			AnalysisGroupValue maxValue = createCurveFitValue(state, "stringValue", "Max", max, recordedBy, lsTransaction);
			maxValue.setPublicData(true);
			newValues.add(maxValue);
		}
		if (!(ki==null) && SimpleUtil.isDecimalNumeric(ki)) {
			AnalysisGroupValue kiValue = createCurveFitValue(state, "numericValue", "Ki", new BigDecimal(ki), recordedBy, lsTransaction);
			kiValue.setUnitKind(curveFitDTO.getKiUnits());
			kiValue.setUncertainty(curveFitDTO.getKiUncertainty());
			kiValue.setUncertaintyType(curveFitDTO.getKiUncertaintyType());
			kiValue.setOperatorKind(curveFitDTO.getKiOperatorKind());
			kiValue.setPublicData(true);
			newValues.add(kiValue);
		} else {
			AnalysisGroupValue kiValue = createCurveFitValue(state, "stringValue", "Ki", ki, recordedBy, lsTransaction);
			kiValue.setCodeValue(batchCode);
			kiValue.setPublicData(true);
			newValues.add(kiValue);
		}
		//Remaining non-special numericValues
		if (!(ligandConc==null)){
			AnalysisGroupValue ligandConcValue = createCurveFitValue(state, "numericValue", "Ligand Conc", ligandConc, recordedBy, lsTransaction);
			ligandConcValue.setCodeValue(batchCode);
			ligandConcValue.setUncertainty(curveFitDTO.getLigandConcUncertainty());
			ligandConcValue.setUncertaintyType(curveFitDTO.getLigandConcUncertaintyType());
			newValues.add(ligandConcValue);
		}
		if (!(kd==null)){
			AnalysisGroupValue kdValue = createCurveFitValue(state, "numericValue", "Kd", kd, recordedBy, lsTransaction);
			kdValue.setCodeValue(batchCode);
			kdValue.setUncertainty(curveFitDTO.getKdUncertainty());
			kdValue.setUncertaintyType(curveFitDTO.getKdUncertaintyType());
			newValues.add(kdValue);
		}
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
		if (!(fittedKi==null)){
			AnalysisGroupValue fittedKiValue = createCurveFitValue(state, "numericValue", "Fitted Ki", fittedKi, recordedBy, lsTransaction);
			fittedKiValue.setCodeValue(batchCode);
			fittedKiValue.setUncertainty(curveFitDTO.getFittedKiUncertainty());
			fittedKiValue.setUncertaintyType(curveFitDTO.getFittedKiUncertaintyType());
			newValues.add(fittedKiValue);
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
	
}
