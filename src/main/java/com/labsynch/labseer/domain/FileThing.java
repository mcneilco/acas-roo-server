package com.labsynch.labseer.domain;

import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(sequenceName = "FILE_PKSEQ")
@RooJson
public class FileThing extends AbstractThing {

    @Size(max = 512)
    private String name;
    
    @Size(max = 512)
    private String description;

    @Size(max = 255)
    private String fileExtension;
    
    @Size(max = 512)
    private String applicationType;

    @Size(max = 512)
    private String mimeType;

    @Size(max = 1024)
    private String fileURL;

    private Long fileSize;
    
    public FileThing(FileThing fileThing) {
        super.setRecordedBy(fileThing.getRecordedBy());
        super.setRecordedDate(fileThing.getRecordedDate());
        super.setLsTransaction(fileThing.getLsTransaction());
        super.setModifiedBy(fileThing.getModifiedBy());
        super.setModifiedDate(fileThing.getModifiedDate());
        super.setCodeName(fileThing.getCodeName());
        super.setLsType(fileThing.getLsType());
        super.setLsKind(fileThing.getLsKind());
        super.setLsTypeAndKind(fileThing.getLsTypeAndKind());
        this.name = fileThing.getName();
        this.description = fileThing.getDescription();
        this.fileExtension = fileThing.getFileExtension();
        this.applicationType = fileThing.getApplicationType();
        this.mimeType = fileThing.getMimeType();
        this.fileURL = fileThing.getFileURL();
        this.fileSize = fileThing.getFileSize();        
    }


	
}
