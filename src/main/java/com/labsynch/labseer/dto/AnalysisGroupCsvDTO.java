package com.labsynch.labseer.dto;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;

@RooJavaBean
@RooToString
@RooJson
public class AnalysisGroupCsvDTO {

	
	private static final Logger logger = LoggerFactory.getLogger(AnalysisGroupCsvDTO.class);

	public AnalysisGroupCsvDTO(){
		//empty constructor
	}

	//	valueType	valueKind	numericValue	stringValue	dateValue	clobValue	urlValue	fileValue	codeValue	valueUnit	valueOperator	publicData	stateType	stateKind	stateID	analysisGroupID	experimentID	experimentVersion	lsTransaction	recordedBy	codeName
	//	stringValue	Rendering Hint	NULL	4 parameter D-R	NULL	NULL	NULL	NULL	NULL	NULL	NULL	FALSE	data	results	1	1	1007	1	86	smeyer	AG-1
	//	stringValue	Rendering Hint	NULL	4 parameter D-R	NULL	NULL	NULL	NULL	NULL	NULL	NULL	FALSE	data	results	5	2	1007	1	86	smeyer	AG-2
	//	valueType,valueKind,numericValue,stringValue,dateValue,clobValue,urlValue,fileValue,codeType,codeKind,codeValue,unitKind,unitType,operatorKind,operatorType,publicData,stateType,stateKind,tempStateId,stateId,id,tempId,parentId,tempParentId,lsTransaction,recordedBy,codeName,lsType,lsKind

	private Long experimentID;
	private String experimentCodeName;
	private Integer experimentVersion;

	private String analysisGroupID;	
	private Long id;	
	private Integer version;
	private String lsType;
	private String lsKind;
	private String codeName;

	private Long stateID;	
	private String stateType;
	private String stateKind;

	private Long valueId;	
	private String valueType;
	private String valueKind;
	private String codeType;
	private String codeKind;
	private String stringValue;
	protected String codeValue;
	private String fileValue;
	private String urlValue;
	private Date dateValue;
	private String clobValue;
	private String valueOperator;
	private BigDecimal numericValue;
	private Integer sigFigs;
	private BigDecimal uncertainty;
	private Integer numberOfReplicates;
	private String uncertaintyType;
	private String valueUnit;
	private Double concentration;
	private String concUnit;
	private String comments;
	private boolean ignored;
	private Long lsTransaction;
	private Date recordedDate;
	private String recordedBy;
	private Date modifiedDate;
	private String modifiedBy;
	private boolean publicData;

	public void setLsTransaction(String lsTransaction) {
		if (lsTransaction.equalsIgnoreCase("NULL")){
			this.lsTransaction = null;
		} else {
		this.lsTransaction = Long.valueOf(lsTransaction);
	}
		}

	public void setExperimentVersion(String experimentVersion) {
		if (experimentVersion.equalsIgnoreCase("NULL")){
			this.experimentVersion = null;
		} else {
		this.experimentVersion = Integer.valueOf(experimentVersion);
		}
	}

	public void setExperimentID(String experimentID) {
		if (experimentID.equalsIgnoreCase("NULL")){
			this.experimentID = null;
		} else {
			this.experimentID = Long.valueOf(experimentID);
		}
	}

	public void setStateID(String stateID) {
		this.stateID = Long.valueOf(stateID);
	}

	public void setPublicData(String publicData) {
		if (publicData.equalsIgnoreCase("FALSE")){
			this.publicData = false;
		} else {
			this.publicData = true;
		}
	}

	public void setDateValue(String dateValue) throws ParseException {
		if (dateValue.equalsIgnoreCase("NULL")){
			this.dateValue = null;
		} else {
			DateFormat df = new SimpleDateFormat("mm/dd/yyyy");
			this.dateValue = df.parse(dateValue);
		}
	}

	public void setNumericValue(String numericValue) {
		if (numericValue.equalsIgnoreCase("NULL")){
			this.numericValue = null;
		} else {
			this.numericValue = new BigDecimal(numericValue);
		}
	}


	public static String[] getColumns(){
		String[] headerColumns = new String[] {
				"id", 
				"protocolId",
				"experimentId",
				"experimentCodeName",
				"experimentName",
				"lsType",
				"lsKind",
				"testedLot",
				"result"};

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


}


