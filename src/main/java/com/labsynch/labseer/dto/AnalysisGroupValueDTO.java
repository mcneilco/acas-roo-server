package com.labsynch.labseer.dto;

import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class AnalysisGroupValueDTO {

	private static final Logger logger = LoggerFactory.getLogger(AnalysisGroupValueDTO.class);

	public AnalysisGroupValueDTO() {
		// empty constructor
	}
	// String sqlQuery = "select new
	// com.labsynch.labseer.dto.AnalysisGroupValueDTO(agv.id, prot.id as protocolId,
	// protLabel.labelText as protocolName, "
	// + "expt.id as experimentId, expt.codeName, el.labelText as prefName, "
	// + "ag.id as agId, ags.id as agStateId, "
	// + "agv.lsType as lsType, agv.lsKind as lsKind, "
	// + "agv.stringValue as stringValue, agv.numericValue as numericValue,
	// agv.codeValue as codeValue, agv.dateValue as dateValue, agv.fileValue as
	// fileValue, "
	// + "agv2.codeValue AS testedLot "
	// + ", agv2.codeValue as geneId "
	// + ", agv.unitKind as resultUnit "
	// + ", agv.operatorKind as operator "
	// + ", agv.uncertainty as uncertainty "
	// + ", agv.uncertaintyType as uncertaintyUnit "
	// + ", agv2.concentration as testedConcentration "
	// + ", agv2.concUnit as testedConcentrationUnit "
	// + ", agv3.numericValue as testedTime "
	// + ", agv3.unitKind as testedTimeUnit "

	public AnalysisGroupValueDTO(
			Long id,
			Long protocolId,
			String protocolName,
			Long experimentId,
			String codeName,
			String prefName,
			Long agId,
			Long agStateId,
			String lsType,
			String lsKind,
			String stringValue,
			BigDecimal numericValue,
			String codeValue,
			Date dateValue,
			String fileValue,
			String testedLot,
			String geneId,
			String resultUnit,
			String operator,
			BigDecimal uncertainty,
			String uncertaintyUnit,
			Double testedConcentration,
			String testedConcentrationUnit,
			BigDecimal testedTime,
			String testedTimeUnit) {

		this.id = id;

		this.protocolId = protocolId;
		this.protocolName = protocolName;
		this.experimentId = experimentId;
		// this.codeName = codeName;
		this.experimentCodeName = codeName;
		this.agId = agId;
		this.agStateId = agStateId;

		this.lsType = lsType;
		this.lsKind = lsKind;
		this.testedLot = testedLot;
		this.geneId = geneId;
		this.resultUnit = resultUnit;
		this.operator = operator;
		this.testedTimeUnit = testedTimeUnit;
		if (testedConcentration != null)
			this.testedConcentration = testedConcentration.toString();
		this.testedConcentrationUnit = testedConcentrationUnit;
		if (uncertainty != null)
			this.uncertainty = uncertainty.toString();
		this.uncertaintyUnit = uncertaintyUnit;
		if (testedTime != null)
			this.testedTime = testedTime.toString();

		if (testedLot.startsWith("GENE")) {
			this.testedLot = geneId;
			// Long lsThingId =
			// LsThing.findLsThingsByCodeNameEquals(testedLot).getSingleResult().getId();
			// List<LsThingLabel> thingNames =
			// LsThingLabel.findLsThingPreferredName(lsThingId, "name", "Entrez Gene
			// ID").getResultList();
			// if (thingNames != null){
			// if (thingNames.size() == 1){
			// this.testedLot = thingNames.get(0).getLabelText();
			// } else if (thingNames.size() > 1) {
			// logger.error("found mulitiple preferred names");
			// } else {
			// logger.error("no preferred names");
			// }
			// }
		} else {
			this.testedLot = testedLot;
		}

		if (lsType != null) {
			if (lsType.equals("stringValue")) {
				this.result = stringValue;
			} else if (numericValue != null) {
				this.result = String.valueOf(numericValue.doubleValue());
			} else if (stringValue != null) {
				this.result = stringValue;
			} else if (lsType.equals("codeValue")) {
				this.result = codeValue;
			} else if (lsType.equals("inlineFileValue")) {
				this.result = fileValue;
			} else if (lsType.equals("fileValue")) {
				this.result = fileValue;
			} else if (lsType.equals("dateValue")) {
				Format formatter = new SimpleDateFormat("yyyy-MM-dd");
				String dateString = formatter.format(dateValue);
				this.result = dateString;
			}
		}

		// List<ExperimentLabel> experimentNames =
		// ExperimentLabel.findExperimentPreferredName(experimentId).getResultList();
		// if (experimentNames != null){
		// if (experimentNames.size() == 1){
		// this.experimentName = experimentNames.get(0).getLabelText();
		// } else if (experimentNames.size() > 1) {
		// logger.error("found mulitiple preferred names");
		// } else {
		// logger.error("no preferred names");
		// }
		// }

		this.experimentName = prefName;

		// Protocol protocol =
		// Protocol.findProtocol(Experiment.findExperiment(experimentId).getProtocol().getId());
		// this.protocolId = protocol.getId();

	}

	// Long id,
	// Long protocolId,
	// String protocolName,
	// Long experimentId,
	// String codeName,
	// String prefName,
	// Long agId,
	// Long agStateId,
	// String lsType,
	// String lsKind,
	// String stringValue,
	// BigDecimal numericValue,
	// String codeValue,
	// Date dateValue,
	// String fileValue,
	// String testedLot,
	// String geneId,
	// String resultUnit,
	// String operator,
	// BigDecimal uncertainty,
	// String uncertaintyUnit,
	// Double testedConcentration,
	// String testedConcentrationUnit,
	// BigDecimal testedTime,
	// String testedTimeUnit

	private Long id;
	private Long protocolId;
	private String protocolName;
	private Long experimentId;
	private String experimentCodeName;
	private String experimentName;
	private Long agId;
	private Long agStateId;
	private String lsType;
	private String lsKind;
	private String testedLot;
	private String geneId;
	// private String stringValue;
	// private BigDecimal numericValue;
	private String result;
	private String resultUnit;
	private String operator;
	private String testedConcentration;
	private String testedConcentrationUnit;
	private String uncertainty;
	private String uncertaintyUnit;
	private String testedTime;
	private String testedTimeUnit;

	public static String[] getColumns() {
		String[] headerColumns = new String[] {
				"id",
				"protocolId",
				"protocolName",
				"experimentId",
				"experimentCodeName",

				"experimentName",
				"agId",
				"agStateId",
				"lsType",
				"lsKind",

				"testedLot",
				"geneId",
				"result",
				"resultUnit",
				"operator",

				"testedConcentration",
				"testedConcentrationUnit",
				"uncertainty",
				"uncertaintyUnit",
				"testedTime",

				"testedTimeUnit"
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

				new Optional()
		};

		return processors;
	}

	public String toJson() {
		return new JSONSerializer().exclude("*.class").serialize(this);
	}

	public String toPrettyJson() {
		return new JSONSerializer().exclude("*.class").prettyPrint(true).serialize(this);
	}

	public static AnalysisGroupValueDTO fromJsonToAnalysisGroupValueDTO(String json) {
		return new JSONDeserializer<AnalysisGroupValueDTO>().use(null, AnalysisGroupValueDTO.class).deserialize(json);
	}

	public static String toJsonArray(Collection<AnalysisGroupValueDTO> collection) {
		return new JSONSerializer().exclude("*.class").serialize(collection);
	}

	public static String toPrettyJsonArray(Collection<AnalysisGroupValueDTO> collection) {
		return new JSONSerializer().exclude("*.class").prettyPrint(true).serialize(collection);
	}

	public static Collection<AnalysisGroupValueDTO> fromJsonArrayToAnalysisGroes(String json) {
		return new JSONDeserializer<List<AnalysisGroupValueDTO>>().use(null, ArrayList.class)
				.use("values", AnalysisGroupValueDTO.class).deserialize(json);
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProtocolId() {
		return this.protocolId;
	}

	public void setProtocolId(Long protocolId) {
		this.protocolId = protocolId;
	}

	public String getProtocolName() {
		return this.protocolName;
	}

	public void setProtocolName(String protocolName) {
		this.protocolName = protocolName;
	}

	public Long getExperimentId() {
		return this.experimentId;
	}

	public void setExperimentId(Long experimentId) {
		this.experimentId = experimentId;
	}

	public String getExperimentCodeName() {
		return this.experimentCodeName;
	}

	public void setExperimentCodeName(String experimentCodeName) {
		this.experimentCodeName = experimentCodeName;
	}

	public String getExperimentName() {
		return this.experimentName;
	}

	public void setExperimentName(String experimentName) {
		this.experimentName = experimentName;
	}

	public Long getAgId() {
		return this.agId;
	}

	public void setAgId(Long agId) {
		this.agId = agId;
	}

	public Long getAgStateId() {
		return this.agStateId;
	}

	public void setAgStateId(Long agStateId) {
		this.agStateId = agStateId;
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

	public String getTestedLot() {
		return this.testedLot;
	}

	public void setTestedLot(String testedLot) {
		this.testedLot = testedLot;
	}

	public String getGeneId() {
		return this.geneId;
	}

	public void setGeneId(String geneId) {
		this.geneId = geneId;
	}

	public String getResult() {
		return this.result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getResultUnit() {
		return this.resultUnit;
	}

	public void setResultUnit(String resultUnit) {
		this.resultUnit = resultUnit;
	}

	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getTestedConcentration() {
		return this.testedConcentration;
	}

	public void setTestedConcentration(String testedConcentration) {
		this.testedConcentration = testedConcentration;
	}

	public String getTestedConcentrationUnit() {
		return this.testedConcentrationUnit;
	}

	public void setTestedConcentrationUnit(String testedConcentrationUnit) {
		this.testedConcentrationUnit = testedConcentrationUnit;
	}

	public String getUncertainty() {
		return this.uncertainty;
	}

	public void setUncertainty(String uncertainty) {
		this.uncertainty = uncertainty;
	}

	public String getUncertaintyUnit() {
		return this.uncertaintyUnit;
	}

	public void setUncertaintyUnit(String uncertaintyUnit) {
		this.uncertaintyUnit = uncertaintyUnit;
	}

	public String getTestedTime() {
		return this.testedTime;
	}

	public void setTestedTime(String testedTime) {
		this.testedTime = testedTime;
	}

	public String getTestedTimeUnit() {
		return this.testedTimeUnit;
	}

	public void setTestedTimeUnit(String testedTimeUnit) {
		this.testedTimeUnit = testedTimeUnit;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
