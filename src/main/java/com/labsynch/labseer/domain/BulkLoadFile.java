package com.labsynch.labseer.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders={"findBulkLoadFilesByRecordedByEquals","findBulkLoadFilesByFileNameEquals"})
public class BulkLoadFile {

    @Size(max = 1000)
    private String fileName;

    private int numberOfMols;
    
    private int fileSize;
    
	@Column(columnDefinition="text")
    private String jsonTemplate;
	
	@NotNull
	private String recordedBy;
	
	private Date fileDate;
	
	@NotNull
	private Date recordedDate;
    
	public BulkLoadFile(){
	}
	
	public BulkLoadFile(String fileName, int numberOfMols, int fileSize, String jsonTemplate, String recordedBy, Date recordedDate){
		this.fileName = fileName;
		this.numberOfMols = numberOfMols;
		this.fileSize = fileSize;
		this.jsonTemplate = jsonTemplate;
		this.recordedBy = recordedBy;
		this.recordedDate = recordedDate;
	}
}
