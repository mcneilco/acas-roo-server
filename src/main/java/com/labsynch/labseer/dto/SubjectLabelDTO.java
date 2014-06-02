package com.labsynch.labseer.dto;

import java.util.Date;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.SubjectLabel;

@RooJavaBean
@RooToString
@RooJson
public class SubjectLabelDTO {
	
    public SubjectLabelDTO(SubjectLabel subjectLabel) {
      this.setId(subjectLabel.getId());
      this.setSubject(new SubjectMiniDTO(subjectLabel.getSubject()));
      this.setLsType(subjectLabel.getLsType());
      this.setLsKind(subjectLabel.getLsKind());
      this.setLsTypeAndKind(subjectLabel.getLsTypeAndKind());
      this.setLabelText(subjectLabel.getLabelText());
      this.setPreferred(subjectLabel.isPreferred());
      this.setLsTransaction_Id(subjectLabel.getLsTransaction());
      this.setRecordedBy(subjectLabel.getRecordedBy());
      this.setRecordedDate(subjectLabel.getRecordedDate());
      this.setPhysicallyLabled(subjectLabel.isPhysicallyLabled());
    }

	private Long id;
    
	private SubjectMiniDTO subject;

	private String labelText;

	private String recordedBy;

	private Date recordedDate;

	private Date modifiedDate;

	private boolean physicallyLabled;

	private String imageFile;

	private String lsType;

	private String lsKind;

	private String lsTypeAndKind;

	private boolean preferred;

	private boolean ignored;

	private Long lsTransaction_Id;



}


//public SubjectLabel(com.labsynch.labseer.domain.SubjectLabel subjectLabel) {
//    super.setLabelType(subjectLabel.getLabelType());
//    super.setLabelKind(subjectLabel.getLabelKind());
//    super.setLabelTypeAndKind(subjectLabel.getLabelType() + "_" + subjectLabel.getLabelKind());
//    super.setLabelText(subjectLabel.getLabelText());
//    super.setPreferred(subjectLabel.isPreferred());
//    super.setLsTransaction(subjectLabel.getLsTransaction());
//    super.setRecordedBy(subjectLabel.getRecordedBy());
//    super.setRecordedDate(subjectLabel.getRecordedDate());
//    super.setPhysicallyLabled(subjectLabel.isPhysicallyLabled());
//}

