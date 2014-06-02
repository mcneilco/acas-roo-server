package com.labsynch.labseer.dto;

import java.util.Date;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ExperimentState;
import com.labsynch.labseer.domain.ExperimentValue;
import com.labsynch.labseer.domain.Protocol;

@RooJavaBean
@RooToString
@RooJson
public class ExperimentGuiStubDTO {
	
	private static final Logger logger = LoggerFactory.getLogger(ExperimentGuiStubDTO.class);
	
	private Long experimentId;

	private Long protocolId;

	private String protocolName;

	private String code;
	
	private String name;
	
	private String lsType;
	
	private String lsKind;
	
	private String scientist;

	private Date completionDate;

	private String notebook;

	private String notebookPage;

	private String status;

	private String analysisStatus;

	private String shortDescription;

	private String longDescription;


	public ExperimentGuiStubDTO(){
		//empty constructor
	}
	
	public ExperimentGuiStubDTO(Experiment experiment) {
		// TODO Auto-generated constructor stub
		this.experimentId = experiment.getId();
		this.protocolId = experiment.getProtocol().getId();
		this.protocolName = Protocol.findProtocol(protocolId).findPreferredName();
		this.lsType = experiment.getLsType();
		this.lsKind = experiment.getLsKind();
		this.code = experiment.getCodeName();
		this.name = Experiment.findExperiment(experiment.getId()).findPreferredName();
		this.shortDescription = experiment.getShortDescription();

		//get experiment meta data
		Set<ExperimentState> states = experiment.getLsStates();
		for (ExperimentState state : states){
			if (state.getLsType().equals("metadata") && state.getLsKind().equals("experiment metadata") && !state.isIgnored()){
				Set<ExperimentValue> values = state.getLsValues();
				for (ExperimentValue value : values){
					if (value.getLsType().equals("stringValue") && value.getLsKind().equals("scientist") && !value.isIgnored()){
						this.scientist = value.getStringValue();
					} else if (value.getLsType().equals("dateValue") && value.getLsKind().equals("completion date") && !value.isIgnored()){
						this.completionDate = value.getDateValue();
					} else if (value.getLsType().equals("stringValue") && value.getLsKind().equals("status") && !value.isIgnored()){
						this.status = value.getStringValue();
					} else if (value.getLsType().equals("stringValue") && value.getLsKind().equals("analysis status") && !value.isIgnored()){
						this.analysisStatus = value.getStringValue();
					} else if (value.getLsType().equals("stringValue") && value.getLsKind().equals("notebook") && !value.isIgnored()){
						this.notebook = value.getStringValue();
					} else if (value.getLsType().equals("stringValue") && value.getLsKind().equals("notebook page") && !value.isIgnored()){
						this.notebookPage = value.getStringValue();
					} else if (value.getLsType().equals("clobValue") && value.getLsKind().equals("long description") && !value.isIgnored()){
						this.longDescription = value.getClobValue();
					}				
				}
			}
		}
	}



}


