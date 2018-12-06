package com.labsynch.labseer.domain;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.dto.BulkLoadPropertyMappingDTO;
import com.labsynch.labseer.dto.BulkLoadTemplateDTO;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders={"findBulkLoadTemplatesByRecordedByEquals", "findBulkLoadTemplatesByTemplateNameEqualsAndRecordedByEquals", "findBulkLoadTemplatesByTemplateNameEquals"})
public class BulkLoadTemplate {

    @Size(max = 255)
    private String templateName;
    
	@Column(columnDefinition="text")
    private String jsonTemplate;
	
	@NotNull
	private String recordedBy;
	
	@NotNull
	private boolean ignored;
    
	public BulkLoadTemplate(){
		this.ignored =false;
	}
	
	public BulkLoadTemplate(String templateName, String jsonTemplate, String recordedBy){
		this.templateName = templateName;
		this.jsonTemplate = jsonTemplate;
		this.recordedBy = recordedBy;
		this.ignored = false;
	}

	public void update(BulkLoadTemplate templateToSave) {
		this.templateName = templateToSave.getTemplateName();
		this.jsonTemplate = templateToSave.getJsonTemplate();
		this.recordedBy = templateToSave.getRecordedBy();
		this.ignored = templateToSave.isIgnored();
		this.merge();
	}
	
	public BulkLoadTemplate(BulkLoadTemplateDTO templateDTO){
		this.templateName = templateDTO.getTemplateName();
		this.jsonTemplate = BulkLoadPropertyMappingDTO.toJsonArray(templateDTO.getMappings());
		this.recordedBy = templateDTO.getRecordedBy();
		this.ignored = templateDTO.isIgnored();
	}
	
}
