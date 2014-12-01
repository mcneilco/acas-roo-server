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
		this.dose = (BigDecimal) dataMap.get("dose");
		this.doseUnits = (String) dataMap.get("doseUnits");
//		this.flagAlgorithm;
//		this.flagOnLoad;
//		this.flagUser;
	}
	


	private String curveId; //location: provided
	private Long responseSubjectValueId; // location: subject value, SS: data_results, SV: numericValue_Response, id attribute
	private BigDecimal response; // location: same as responseSubjectValueId, but in numericValue field
	private String responseUnits; //location same as response, but in unitKind field
	private BigDecimal dose; // location: subject value, SS: data_test compound treatment, SV: numericValue_Dose
	private String doseUnits; //location, same as above, but in unitKind field
	private String flagAlgorithm; //location: TODO:ask Guy
	private String flagOnLoad;
	private String flagUser;
	
	


	public static String[] getColumns(){
		String[] headerColumns = new String[] {
				"curveId",
				"responseSubjectValueId",
				"dose",
				"doseUnits",
				"response",
				"responseUnits",
				"flagAlgorithm",
				"flagOnLoad",
				"flagUser"
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
				new Optional()
		};

		return processors;
	}

	public static Collection<RawCurveDataDTO> getRawCurveData(
			Collection<RawCurveDataDTO> rawCurveDataDTOs) {
		List<RawCurveDataDTO> resultList = new ArrayList<RawCurveDataDTO>();
		for (RawCurveDataDTO rawCurveDataDTO : rawCurveDataDTOs) {
			resultList.addAll(getRawCurveData(rawCurveDataDTO));
		}
		return resultList;
	}

	public static List<RawCurveDataDTO> getRawCurveData(RawCurveDataDTO emptyRawCurveDataDTO){
		//TODO: add flags
		String curveId = emptyRawCurveDataDTO.getCurveId();
		EntityManager em = SubjectValue.entityManager();
        TypedQuery<Map> q = em.createQuery("SELECT NEW MAP( rsv.id as responseSubjectValueId, "
        		+ "rsv.numericValue as response, "
        		+ "rsv.unitKind as responseUnits, "
        		+ "dsv.numericValue as dose, "
        		+ "dsv.unitKind as doseUnits ) "
//        		+ "flags"
        		+ "FROM AnalysisGroupValue agv "
        		+ "JOIN agv.lsState.analysisGroup.treatmentGroups as treat "
        		+ "JOIN treat.subjects as subj "
        		+ "JOIN subj.lsStates as rss "
        		+ "JOIN subj.lsStates as dss "
        		+ "JOIN rss.lsValues as rsv "
        		+ "JOIN dss.lsValues as dsv "
        		+ "WHERE rss.lsType = 'data' "
        		+ "AND rss.lsKind = 'results' "
        		+ "AND rsv.lsType = 'numericValue' "
        		+ "AND rsv.lsKind = 'Response' "
        		+ "AND dss.lsType = 'data' "
        		+ "AND dss.lsKind = 'test compound treatment' "
        		+ "AND dsv.lsType = 'numericValue' "
        		+ "AND dsv.lsKind = 'Dose' "
        		+ "AND agv.lsType = 'stringValue' "
        		+ "AND agv.lsKind = 'curve id' "
        		+ "AND agv.stringValue = :curveId", Map.class);
        q.setParameter("curveId", curveId);
        List<Map> queryResults = q.getResultList();
        List<RawCurveDataDTO> rawCurveDataList = new ArrayList<RawCurveDataDTO>();
		for (Map result : queryResults) {
			RawCurveDataDTO rawCurveDataDTO = new RawCurveDataDTO(curveId, result);
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
}