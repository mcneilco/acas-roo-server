package com.labsynch.labseer.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class AutoLabelDTO {
	
    private String autoLabel;

    private Long labelNumber;
    
    public AutoLabelDTO () {
    	
    }
    
    public AutoLabelDTO (String autoLabel, Long labelNumber){
    		this.autoLabel = autoLabel;
    		this.labelNumber = labelNumber;
    }

}
