package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.BulkLoadTemplate;
import com.labsynch.labseer.domain.CorpName;
import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.domain.PhysicalState;

import com.labsynch.labseer.service.ErrorMessage;
import com.labsynch.labseer.utils.PropertiesUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class BulkLoadPropertiesDTO {
	
	private static final Logger logger = LoggerFactory.getLogger(BulkLoadPropertiesDTO.class);

    private Collection<SimpleBulkLoadPropertyDTO> sdfProperties;
    
    private Collection<SimpleBulkLoadPropertyDTO> dbProperties;
    
    private Collection<BulkLoadPropertyMappingDTO> bulkLoadProperties;
    
    private Collection<ErrorMessage> errors;
    
    private int numRecordsRead;
    
    private String templateName;
    
    public BulkLoadPropertiesDTO(String dbPropertiesJSON){
		this.dbProperties = SimpleBulkLoadPropertyDTO.fromJsonToLsThingQueryDTO(dbPropertiesJSON);
    }
    
    public String toJson() {
        return new JSONSerializer()
        .include("sdfProperties", "dbProperties", "bulkLoadProperties", "errors").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }
    
    private List<String> listAllMappedSdfProperties(){
    	List<String> allPropNames = new ArrayList<String>();
    	for (BulkLoadPropertyMappingDTO prop : this.bulkLoadProperties){
    		allPropNames.add(prop.getSdfProperty());
    	}
    	return allPropNames;
    }
    
    private List<String> listAllMappedDbProperties(){
    	List<String> allPropNames = new ArrayList<String>();
    	for (BulkLoadPropertyMappingDTO prop : this.bulkLoadProperties){
    		allPropNames.add(prop.getDbProperty());
    	}
    	return allPropNames;
    }

	public void autoAssignMappings() {
		if (this.bulkLoadProperties == null) this.bulkLoadProperties = new HashSet<BulkLoadPropertyMappingDTO>();
		List<String> mappedSdfProperties = listAllMappedSdfProperties();
		List<String> mappedDbProperties = listAllMappedDbProperties();
		for (SimpleBulkLoadPropertyDTO sdfProp : this.sdfProperties){
			//if this sdf property has not already been mapped
			if (!mappedSdfProperties.contains(sdfProp.getName())){
				SimpleBulkLoadPropertyDTO dbPropMatch = null;
				//we look for a matching db property that has not already been used
				for (SimpleBulkLoadPropertyDTO dbProp : this.dbProperties){
					if (sdfProp.getName().equalsIgnoreCase(dbProp.getName()) && !mappedDbProperties.contains(dbProp.getName())){
						dbPropMatch = dbProp;
						break;
					}
				}
				//if there was a new match, add the mapping
				if (dbPropMatch != null && (dbPropMatch.getIgnored() == null || !dbPropMatch.getIgnored())) {
					BulkLoadPropertyMappingDTO newMapping = new BulkLoadPropertyMappingDTO(dbPropMatch.getName(), sdfProp.getName(), dbPropMatch.getRequired(), null, null, false);
					this.bulkLoadProperties.add(newMapping);
				}
			}
		}
		
	}

	public void applyTemplateMappings(String templateName, String userName) {
		if (this.bulkLoadProperties == null) this.bulkLoadProperties = new HashSet<BulkLoadPropertyMappingDTO>();
		BulkLoadTemplate foundTemplate = null;
		try{
			foundTemplate = BulkLoadTemplate.findBulkLoadTemplatesByTemplateNameEqualsAndRecordedByEquals(templateName, userName).getSingleResult();
		}catch (EmptyResultDataAccessException e){
			logger.error("Template name: "+templateName+" with userName: "+userName+" could not be found.");
			return;
		}
		Collection<BulkLoadPropertyMappingDTO> templateMappings = BulkLoadPropertyMappingDTO.fromJsonArrayToBulkLoadProes(foundTemplate.getJsonTemplate());
		
	}

	public void cleanMappings() {
		Collection<String> sdfPropertyNames = new HashSet<String>();
		for (SimpleBulkLoadPropertyDTO prop : this.sdfProperties){
			sdfPropertyNames.add(prop.getName());
		}
		Collection<BulkLoadPropertyMappingDTO> cleanedMappings = new HashSet<BulkLoadPropertyMappingDTO>();
		for (BulkLoadPropertyMappingDTO mapping : this.bulkLoadProperties){
			if (mapping.getSdfProperty() != null && mapping.getSdfProperty().length() > 0 && !sdfPropertyNames.contains(mapping.getSdfProperty())) continue;
			else cleanedMappings.add(mapping);
		}
		this.bulkLoadProperties = cleanedMappings;
		
	}

	public void checkAgainstTemplate(String templateName) {
		try{
			Collection<BulkLoadPropertyMappingDTO> templateMappings = null;
			HashSet<String> templateSdfProperties = new HashSet<String>();
			HashSet<String> mappingSdfProperties = new HashSet<String>();
			
			if (templateName != null ){
				BulkLoadTemplate template = BulkLoadTemplate.findBulkLoadTemplatesByTemplateNameEquals(templateName).getSingleResult();	
				templateMappings = BulkLoadPropertyMappingDTO.fromJsonArrayToBulkLoadProes(template.getJsonTemplate());

				for (BulkLoadPropertyMappingDTO mapping : templateMappings){
					if (mapping.getSdfProperty() != null) templateSdfProperties.add(mapping.getSdfProperty());
				}
				for (BulkLoadPropertyMappingDTO mapping : this.bulkLoadProperties){
					if (mapping.getSdfProperty() != null) mappingSdfProperties.add(mapping.getSdfProperty());
				}
				templateSdfProperties.removeAll(mappingSdfProperties);
			}
			
			if (templateSdfProperties.size() != 0){
				ErrorMessage error = new ErrorMessage("warning", "");
				String message = "The following sdf properties differ between the template and the mappings: ";
				for (String sdfPropertyName : templateSdfProperties){
					message += sdfPropertyName+", ";
				}
				message = message.substring(0,message.length()-2);
				error.setMessage(message);
				this.errors.add(error);
			}else{
				//all sdf properties match. We check if their mapped db properties align.
				for (BulkLoadPropertyMappingDTO mapping : this.bulkLoadProperties){
					if (mapping.getSdfProperty() != null && templateMappings != null){
						BulkLoadPropertyMappingDTO templateMapping = BulkLoadPropertyMappingDTO.findMappingBySdfPropertyEquals(templateMappings, mapping.getSdfProperty());
						if (templateMapping != null && !mapping.getDbProperty().equals(templateMapping.getDbProperty())){
							ErrorMessage error = new ErrorMessage("warning","");
							String message = "There is a mismatch in sdfProperty:dbProperty mappings between the template and the mappings: ";
							message += templateMapping.getSdfProperty()+":"+templateMapping.getDbProperty()+" in the template vs. ";
							message += mapping.getSdfProperty()+":"+mapping.getDbProperty()+" in the current mapping.";
							error.setMessage(message);
							this.errors.add(error);
						}
					}
				}
			}
		}catch (Exception e){
			logger.error("Caught exception comparing template to mappings.",e);
		}
	}
}
