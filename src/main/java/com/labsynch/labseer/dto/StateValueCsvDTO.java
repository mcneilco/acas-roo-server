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

import com.labsynch.labseer.domain.ExperimentValue;

@RooJavaBean
@RooToString
@RooJson
public class StateValueCsvDTO {

	private static final Logger logger = LoggerFactory.getLogger(StateValueCsvDTO.class);

	public StateValueCsvDTO(){
		//empty constructor
	}


	public StateValueCsvDTO(ExperimentValue entityValue) {
		this.setStateId(entityValue.getLsState().getId());
		this.setId(entityValue.getId());
		this.setLsType(entityValue.getLsType());
		this.setLsKind(entityValue.getLsKind());
		this.setCodeValue(entityValue.getCodeValue());
		this.setCodeType(entityValue.getCodeType());
		this.setCodeKind(entityValue.getCodeKind());
		this.setCodeOrigin(entityValue.getCodeOrigin());

		this.setStringValue(entityValue.getStringValue());
		this.setFileValue(entityValue.getFileValue());
		this.setUrlValue(entityValue.getUrlValue());
		this.setDateValue(entityValue.getDateValue());
		this.setClobValue(entityValue.getClobValue());

		this.setOperatorType(entityValue.getOperatorType());
		this.setOperatorKind(entityValue.getOperatorKind());
		this.setNumericValue(entityValue.getNumericValue());
		this.setSigFigs(entityValue.getSigFigs());
		this.setUncertainty(entityValue.getUncertainty());
		this.setUncertaintyType(entityValue.getUncertaintyType());
		this.setUnitType(entityValue.getUnitType());
		this.setUnitKind(entityValue.getUnitKind());
		this.setNumberOfReplicates(entityValue.getNumberOfReplicates());

		this.setComments(entityValue.getComments());
		this.setIgnored(entityValue.isIgnored());
		this.setLsTransaction(entityValue.getLsTransaction());

		this.setRecordedDate(entityValue.getRecordedDate());
		this.setRecordedBy(entityValue.getRecordedBy());
		this.setModifiedDate(entityValue.getModifiedDate());
		this.setModifiedBy(entityValue.getModifiedBy());
		this.setPublicData(entityValue.isPublicData());	
		}


	private Long stateId;	

	private Long id;
	private String lsType;
	private String lsKind;
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

	private String comments;
	private boolean ignored;
	private Long lsTransaction;
	private Date recordedDate;
	private String recordedBy;
	private Date modifiedDate;
	private String modifiedBy;
	private boolean publicData;

//29 fields
	
    public boolean getIgnored() {
        return this.ignored;
    }

    public boolean getPublicData() {
        return this.publicData;
    }
	
	public void setLsTransaction(String lsTransaction) {
		if (lsTransaction.equalsIgnoreCase("NULL")){
			this.lsTransaction = null;
		} else {
			this.lsTransaction = Long.valueOf(lsTransaction);
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
	
	public void setId(String id) {
		if (id.equalsIgnoreCase("NULL")){
			this.id = null;
		} else {
			this.id = Long.valueOf(id);
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
				"stateId",

				"id",
				"lsType",
				"lsKind",
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
				"comments",
				"ignored",
				"lsTransaction",
				"recordedDate",
				"recordedBy",
				"modifiedDate",
				"modifiedBy",
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


