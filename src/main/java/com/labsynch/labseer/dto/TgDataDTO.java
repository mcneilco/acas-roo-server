package com.labsynch.labseer.dto;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.labsynch.labseer.domain.SubjectValue;
import com.labsynch.labseer.domain.TreatmentGroupValue;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class TgDataDTO {
	
	private String curveId;
	private String recordedBy;
	private Long lsTransaction;
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
	
	public TgDataDTO(String curveId){
		this.curveId = curveId;
	}
	
	public TgDataDTO(String curveId, String recordedBy, Long lsTransaction, TreatmentGroupValue result) {
		this.curveId = curveId;
		this.recordedBy = recordedBy;
		this.lsTransaction = lsTransaction;
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
				"recordedBy",
				"lsTransaction",
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
        		+ "JOIN ags.analysisGroup as ag "
        		+ "JOIN ag.treatmentGroups as treat "
        		+ "JOIN treat.lsStates as tgs "
        		+ "JOIN tgs.lsValues as tgv "
        		+ "WHERE tgs.lsType = 'data' "
        		+ "AND tgs.lsKind = 'results' "
        		+ "AND tgv.lsType = 'numericValue' "
        		+ "AND ags.ignored = false "
        		+ "AND agv.lsType = 'stringValue' "
        		+ "AND agv.lsKind = 'curve id' "
        		+ "AND agv.ignored = false "
        		+ "AND ag.ignored IS NOT :ignored "
        		+ "AND treat.ignored IS NOT :ignored "
        		+ "AND agv.stringValue = :curveId", TreatmentGroupValue.class);
        q.setParameter("curveId", curveId);
        List<TreatmentGroupValue> queryResults = q.getResultList();
        List<TgDataDTO> tgDataList = new ArrayList<TgDataDTO>();
        for (TreatmentGroupValue result : queryResults) {
			TgDataDTO tgDataDTO = new TgDataDTO(curveId, result.getRecordedBy(), result.getLsTransaction(), result);
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

	public static Collection<TgDataDTO> getTgDataByExperiment(String experimentIdOrCodeName){
		Collection<TgDataDTO> tgDataDTOs = makeTgDataDTOsFromCurveIdList(CurveFitDTO.findAllCurveIdsByExperiment(experimentIdOrCodeName));
		tgDataDTOs = getTgData(tgDataDTOs);
		return tgDataDTOs;
	}
	
	private static Collection<TgDataDTO> makeTgDataDTOsFromCurveIdList(Collection<String> curveIdList) {
		Collection<TgDataDTO> tgDataDTOs = new HashSet<TgDataDTO>();
		for (String curveId : curveIdList) {
			tgDataDTOs.add(new TgDataDTO(curveId));
		}
		return tgDataDTOs;
	}

	public static Collection<TgDataDTO> getTgData(List<String> curveIds) {
		return getTgData(makeTgDataDTOsFromCurveIdList(curveIds));
	}
	

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static TgDataDTO fromJsonToTgDataDTO(String json) {
        return new JSONDeserializer<TgDataDTO>()
        .use(null, TgDataDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<TgDataDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<TgDataDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<TgDataDTO> fromJsonArrayToTgDataDTO(String json) {
        return new JSONDeserializer<List<TgDataDTO>>()
        .use("values", TgDataDTO.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String getCurveId() {
        return this.curveId;
    }

	public void setCurveId(String curveId) {
        this.curveId = curveId;
    }

	public String getRecordedBy() {
        return this.recordedBy;
    }

	public void setRecordedBy(String recordedBy) {
        this.recordedBy = recordedBy;
    }

	public Long getLsTransaction() {
        return this.lsTransaction;
    }

	public void setLsTransaction(Long lsTransaction) {
        this.lsTransaction = lsTransaction;
    }

	public Long getTgvId() {
        return this.tgvId;
    }

	public void setTgvId(Long tgvId) {
        this.tgvId = tgvId;
    }

	public String getLsType() {
        return this.lsType;
    }

	public void setLsType(String lsType) {
        this.lsType = lsType;
    }

	public String getLsKind() {
        return this.lsKind;
    }

	public void setLsKind(String lsKind) {
        this.lsKind = lsKind;
    }

	public Integer getNumberOfReplicates() {
        return this.numberOfReplicates;
    }

	public void setNumberOfReplicates(Integer numberOfReplicates) {
        this.numberOfReplicates = numberOfReplicates;
    }

	public BigDecimal getNumericValue() {
        return this.numericValue;
    }

	public void setNumericValue(BigDecimal numericValue) {
        this.numericValue = numericValue;
    }

	public String getUnitType() {
        return this.unitType;
    }

	public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

	public String getUnitKind() {
        return this.unitKind;
    }

	public void setUnitKind(String unitKind) {
        this.unitKind = unitKind;
    }

	public String getUncertaintyType() {
        return this.uncertaintyType;
    }

	public void setUncertaintyType(String uncertaintyType) {
        this.uncertaintyType = uncertaintyType;
    }

	public BigDecimal getUncertainty() {
        return this.uncertainty;
    }

	public void setUncertainty(BigDecimal uncertainty) {
        this.uncertainty = uncertainty;
    }

	public Boolean getPublicData() {
        return this.publicData;
    }

	public void setPublicData(Boolean publicData) {
        this.publicData = publicData;
    }
}
