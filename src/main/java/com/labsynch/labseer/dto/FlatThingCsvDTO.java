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
public class FlatThingCsvDTO {

	private static final Logger logger = LoggerFactory.getLogger(FlatThingCsvDTO.class);

	public FlatThingCsvDTO(){
		//empty constructor
	}


	//	valueType	valueKind	numericValue	stringValue	dateValue	clobValue	urlValue	fileValue	codeValue	valueUnit	valueOperator	publicData	stateType	stateKind	stateID	analysisGroupID	experimentID	experimentVersion	lsTransaction	recordedBy	codeName
	//	stringValue	Rendering Hint	NULL	4 parameter D-R	NULL	NULL	NULL	NULL	NULL	NULL	NULL	FALSE	data	results	1	1	1007	1	86	smeyer	AG-1
	//	stringValue	Rendering Hint	NULL	4 parameter D-R	NULL	NULL	NULL	NULL	NULL	NULL	NULL	FALSE	data	results	5	2	1007	1	86	smeyer	AG-2
	//	valueType,valueKind,numericValue,stringValue,dateValue,clobValue,urlValue,
	//	fileValue,codeType,codeKind,codeValue,unitKind,unitType,operatorKind,operatorType,
	//	publicData,stateType,stateKind,tempStateId,stateId,id,tempId,parentId,tempParentId,
	//	lsTransaction,recordedBy,codeName,lsType,lsKind


	private Long parentId;
	private String tempParentId;

	private Long id;	
	private String tempId;	
	private String codeName;
	private String lsType;
	private String lsKind;

	private Long stateId;	
	private String tempStateId;	
	private String stateType;
	private String stateKind;

	private Long valueId;
	private String tempValueId;	
	private String valueType;
	private String valueKind;
	private String codeOrigin;
	private String codeType;
	private String codeKind;
	protected String codeValue;
	private String stringValue;
	private String fileValue;
	private String urlValue;
	private Date dateValue;
	private String clobValue;
	private String operatorType;
	private String operatorKind;
	private BigDecimal numericValue;
	private Integer sigFigs;
	private BigDecimal uncertainty;
	private Integer numberOfReplicates;
	private String uncertaintyType;
	private String unitType;
	private String unitKind;
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

	public void setCodeName(String codeName) {
		if (codeName.equalsIgnoreCase("NULL")){
			this.codeName = null;
		} else {
			this.codeName = codeName;
		}
	}
	
	public void setParentId(String parentId) {
		if (parentId.equalsIgnoreCase("NULL")){
			this.parentId = null;
		} else {
			this.parentId = Long.valueOf(parentId);
		}
	}

	public void setStateId(String stateId) {
		this.stateId = Long.valueOf(stateId);
	}

	public void setPublicData(String publicData) {
		if (publicData.equalsIgnoreCase("FALSE")){
			this.publicData = false;
		} else {
			this.publicData = true;
		}
	}
	
	public void setIgnored(String ignored) {
		if (ignored.equalsIgnoreCase("FALSE")){
			this.ignored = false;
		} else {
			this.ignored = true;
		}
	}

	public void setDateValue(String dateValue) throws ParseException {
		if (dateValue.equalsIgnoreCase("NULL")){
			this.dateValue = null;
		} else {
			try {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				this.dateValue = df.parse(dateValue);
			} catch(Exception e) {
				try {
					this.dateValue = new Date(Long.parseLong(dateValue));
				} catch(Exception e2) {
					DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
					this.dateValue = df.parse(dateValue);
				}
				
			}
		}
	}

	public void setNumericValue(String numericValue) {
		if (numericValue.equalsIgnoreCase("NULL")){
			this.numericValue = null;
		} else {
			this.numericValue = new BigDecimal(numericValue);
		}
	}

	public void setUncertainty(String uncertainty) {
		if (uncertainty.equalsIgnoreCase("NULL")){
			this.uncertainty = null;
		} else {
			this.uncertainty = new BigDecimal(uncertainty);
		}
	}
	
	public void setConcentration(String concentration) {
		if (concentration.equalsIgnoreCase("NULL")){
			this.concentration = null;
		} else {
			this.concentration = new Double(concentration);
		}
	}
	
	public void setId(String id) {
		if (id.equalsIgnoreCase("NULL")){
			this.id = null;
		} else {
			this.id = Long.valueOf(id);
		}
	}
	
	public void setValueId(String valueId) {
		if (valueId.equalsIgnoreCase("NULL")){
			this.valueId = null;
		} else {
			this.valueId = Long.valueOf(valueId);
		}
	}
	
	public void setSigFigs(String sigFigs) {
		if (sigFigs.equalsIgnoreCase("NULL")){
			this.sigFigs = null;
		} else {
			this.sigFigs = Integer.valueOf(sigFigs);
		}
	}

	public void setNumberOfReplicates(String numberOfReplicates) {
		if (numberOfReplicates.equalsIgnoreCase("NULL")){
			this.numberOfReplicates = null;
		} else {
			this.numberOfReplicates = Integer.valueOf(numberOfReplicates);
		}
	}
	
	
	public static String[] getColumns(){
		String[] headerColumns = new String[] {
				"parentId", 
				"tempParentId",
				"id",
				"tempId",
				"codeName",
				"lsType",
				"lsKind",
				"stateId",
				"tempStateId",
				"stateType",
				
				"stateKind",
				"tempValueId",
				"valueType",
				"valueKind",
				"codeOrigin",
				"codeType",
				"codeKind",
				"codeValue",
				"stringValue",
				"fileValue",
				"urlValue",
				
				"dateValue",
				"clobValue",
				"operatorType",
				"operatorKind",
				"numericValue",
				"sigFigs",
				"uncertainty",
				"numberOfReplicates",
				"uncertaintyType",
				"unitType",
				
				"unitKind",
				"concentration",
				"concUnit",
				"comments",
				//				"ignored",
				"lsTransaction",
				//				"recordedDate",
				"recordedBy",
				//				"modifiedDate",
				//				"modifiedBy",
				"publicData"
				// 37 values
		};

		//		valueType,valueKind,numericValue,stringValue,dateValue,clobValue,urlValue,
		//		fileValue,codeType,codeKind,codeValue,unitKind,unitType,operatorKind,operatorType,
		//		publicData,stateType,stateKind,tempStateId,stateId,id,tempId,parentId,tempParentId,
		//		lsTransaction,recordedBy,codeName,lsType,lsKind

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
				new Optional()


		};

		return processors;
	}


}


