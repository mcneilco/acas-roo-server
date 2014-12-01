package com.labsynch.labseer.dto;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
				this.minUnits = stringMap.get("");
				this.maxUnits = stringMap.get("");
				this.ec50Units = stringMap.get("");
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
				this.flagStatus = stringMap.get("");
				this.flagObservation = stringMap.get("");
				this.flagReason = stringMap.get("");
				this.flagComment = stringMap.get("");
	}


	private String curveId;
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

	public static CurveFitDTO getFitData(CurveFitDTO curveFitDTO){
		AnalysisGroupValue curveIdValue = AnalysisGroupValue.findAnalysisGroupValuesByLsTypeEqualsAndLsKindEqualsAndStringValueLike("stringValue", "curve id", curveFitDTO.getCurveId()).getSingleResult();
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
	
}
