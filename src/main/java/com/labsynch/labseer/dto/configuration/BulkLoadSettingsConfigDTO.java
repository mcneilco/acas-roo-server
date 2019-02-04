package com.labsynch.labseer.dto.configuration;

import java.util.Collection;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.dto.SimpleBulkLoadPropertyDTO;

@RooJavaBean
@RooToString
@RooJson
public class BulkLoadSettingsConfigDTO {

	private Boolean useProjectRoles;
	
	private Collection<SimpleBulkLoadPropertyDTO> dbProperties;
    
}
