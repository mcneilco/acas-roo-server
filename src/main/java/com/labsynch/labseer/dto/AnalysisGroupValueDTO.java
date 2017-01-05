package com.labsynch.labseer.dto;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;

import java.math.BigDecimal;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class AnalysisGroupValueDTO {

	private static final Logger logger = LoggerFactory.getLogger(AnalysisGroupValueDTO.class);

	public AnalysisGroupValueDTO(){
		//empty constructor
	}

	//	select new com.labsynch.labseer.dto.AnalysisGroupValueDTO(agv.id, expt.id as experimentId, expt.codeName, 
	//			el.labelText as prefName, agv.lsType as lsType, agv.lsKind as lsKind, agv.stringValue as stringValue, 
	//			agv.numericValue as numericValue, agv2.codeValue AS testedLot  



	public AnalysisGroupValueDTO(
			Long id,
			Long protocolId,
			String protocolName,
			Long experimentId, 
			String codeName, 
			String prefName,
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
			String testedTimeUnit
			){

		this.id = id;
		this.protocolId = protocolId;
		this.protocolName = protocolName;
		this.experimentId = experimentId;
		this.protocolId = protocolId;
		this.stateId = stateId;
		//		this.codeName = codeName;
		this.experimentCodeName = codeName;
		this.lsType = lsType;
		this.lsKind = lsKind;
		this.testedLot = testedLot;
		this.resultUnit = resultUnit;
		this.operator = operator;
		this.testedTimeUnit = testedTimeUnit;
		if (testedConcentration != null) this.testedConcentration = testedConcentration.toString();
		this.testedConcentrationUnit = testedConcentrationUnit;
		if (uncertainty != null) this.uncertainty = uncertainty.toString();
		this.uncertaintyUnit = uncertaintyUnit;
		if (testedTime != null) this.testedTime = testedTime.toString();

		if (testedLot.startsWith("GENE")){
			this.testedLot = geneId;
			//			Long lsThingId = LsThing.findLsThingsByCodeNameEquals(testedLot).getSingleResult().getId();
			//			List<LsThingLabel> thingNames = LsThingLabel.findLsThingPreferredName(lsThingId, "name", "Entrez Gene ID").getResultList();
			//			if (thingNames != null){
			//				if (thingNames.size() == 1){
			//					this.testedLot = thingNames.get(0).getLabelText();
			//				} else if (thingNames.size() > 1) {
			//					logger.error("found mulitiple preferred names");
			//				} else {
			//					logger.error("no preferred names");
			//				}
			//			}	
		} else {
			this.testedLot = testedLot;
		}

		if (lsType != null){
			if (lsType.equals("stringValue")){
				this.result=stringValue;				
			} else if (numericValue != null) {
				this.result = String.valueOf(numericValue.doubleValue());
			} else if (stringValue != null) {
				this.result = stringValue;
			} else if (lsType.equals("codeValue")){
				this.result=codeValue;
			} else if (lsType.equals("inlineFileValue")){
				this.result=fileValue;
			} else if (lsType.equals("fileValue")){
				this.result=fileValue;
			} else if (lsType.equals("dateValue")){
				Format formatter = new SimpleDateFormat("yyyy-MM-dd");
				String dateString = formatter.format(dateValue);
				this.result=dateString;
			}
		}

		//		List<ExperimentLabel> experimentNames = ExperimentLabel.findExperimentPreferredName(experimentId).getResultList();
		//		if (experimentNames != null){
		//			if (experimentNames.size() == 1){
		//				this.experimentName = experimentNames.get(0).getLabelText();
		//			} else if (experimentNames.size() > 1) {
		//				logger.error("found mulitiple preferred names");
		//			} else {
		//				logger.error("no preferred names");
		//			}			
		//		}

		this.experimentName  = prefName;

		//		Protocol protocol = Protocol.findProtocol(Experiment.findExperiment(experimentId).getProtocol().getId());
		//		this.protocolId = protocol.getId();

	}


	private Long id;	
	private Long stateId;
	private Long protocolId;
	private String protocolName;
	private Long experimentId;
	private String experimentCodeName;
	private String experimentName;
	private String lsType;
	private String lsKind;
	private String testedLot;
	private String geneId;
	//	private String stringValue;
	//	private BigDecimal numericValue;
	private String result;
	private String resultUnit;
	private String operator;
	private String testedConcentration;
	private String testedConcentrationUnit;
	private String uncertainty;
	private String uncertaintyUnit;
	private String testedTime;
	private String testedTimeUnit;


	public static String[] getColumns(){
		String[] headerColumns = new String[] {
				"id", 
				"stateId",
				"protocolId",
				"protocolName",
				"experimentId",
				"experimentCodeName",
				"experimentName",
				"lsType",
				"lsKind",
				"testedLot",
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
		return new JSONDeserializer<List<AnalysisGroupValueDTO>>().use(null, ArrayList.class).use("values", AnalysisGroupValueDTO.class).deserialize(json);
	}
}


