package com.labsynch.labseer.dto;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.labsynch.labseer.domain.SubjectValue;
import com.labsynch.labseer.domain.TreatmentGroupValue;

@RooJavaBean
@RooToString
@RooJson
public class TgDataDTO {
	
	private String curveId;
	private Long tgvId;
	private String lsType;
	private String lsKind;
	private Integer numberOfReplicates;
	private BigDecimal numericValue;
	private String unitType;
	private String unitKind;
	private String uncertaintyType;
	private BigDecimal uncertainty;
	private Boolean publicData;
	
	public TgDataDTO(){
		//empty constructor
	}
	
	public TgDataDTO(String curveId, TreatmentGroupValue result) {
		this.curveId = curveId;
		this.tgvId = result.getId();
		this.lsType = result.getLsType();
		this.lsKind = result.getLsKind();
		this.numberOfReplicates = result.getNumberOfReplicates();
		this.numericValue = result.getNumericValue();
		this.unitType = result.getUnitType();
		this.unitKind = result.getUnitKind();
		this.uncertaintyType = result.getUncertaintyType();
		this.uncertainty = result.getUncertainty();
		this.publicData = result.isPublicData();
		
	}

	public static String[] getColumns(){
		String[] headerColumns = new String[] {
				"curveId",
				"tgvId",
				"lsType",
				"lsKind",
				"numberOfReplicates",
				"numericValue",
				"unitType",
				"unitKind",
				"uncertaintyType",
				"uncertainty",
				"publicData"
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
				new Optional()
		};

		return processors;
	}
	
	public static Collection<TgDataDTO> getTgData(
			Collection<TgDataDTO> tgDataDTOs) {
		List<TgDataDTO> resultList = new ArrayList<TgDataDTO>();
		for (TgDataDTO tgDataDTO : tgDataDTOs) {
			resultList.addAll(getTgData(tgDataDTO));
		}
		return resultList;
	}

	public static List<TgDataDTO> getTgData(TgDataDTO emptyTgDataDTO){
		String curveId = emptyTgDataDTO.getCurveId();
		EntityManager em = SubjectValue.entityManager();
        TypedQuery<TreatmentGroupValue> q = em.createQuery("SELECT tgv "
        		+ "FROM AnalysisGroupValue agv "
        		+ "JOIN agv.lsState as ags "
        		+ "JOIN ags.analysisGroup.treatmentGroups as treat "
        		+ "JOIN treat.lsStates as tgs "
        		+ "JOIN tgs.lsValues as tgv "
        		+ "WHERE tgs.lsType = 'data' "
        		+ "AND tgs.lsKind = 'results' "
        		+ "AND tgv.lsType = 'numericValue' "
        		+ "AND ags.ignored = false "
        		+ "AND agv.lsType = 'stringValue' "
        		+ "AND agv.lsKind = 'curve id' "
        		+ "AND agv.ignored = false "
        		+ "AND agv.stringValue = :curveId", TreatmentGroupValue.class);
        q.setParameter("curveId", curveId);
        List<TreatmentGroupValue> queryResults = q.getResultList();
        List<TgDataDTO> tgDataList = new ArrayList<TgDataDTO>();
        for (TreatmentGroupValue result : queryResults) {
			TgDataDTO tgDataDTO = new TgDataDTO(curveId, result);
			tgDataList.add(tgDataDTO);
		}
		return tgDataList;
	}
	
	public static String getCsvList(Collection<TgDataDTO> tgDataDTOs, String format) {
		//format is ALWAYS tsv or csv (not case sensitive)
		StringWriter outFile = new StringWriter();
        ICsvBeanWriter beanWriter = null;
        try {
            if (format.equalsIgnoreCase("csv")) beanWriter = new CsvBeanWriter(outFile, CsvPreference.STANDARD_PREFERENCE);
            else beanWriter = new CsvBeanWriter(outFile, CsvPreference.TAB_PREFERENCE);
            final String[] header = TgDataDTO.getColumns();
            final CellProcessor[] processors = TgDataDTO.getProcessors();
            beanWriter.writeHeader(header);
            for (final TgDataDTO tgDataDTO : tgDataDTOs) {
                beanWriter.write(tgDataDTO, header, processors);
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
