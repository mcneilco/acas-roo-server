package com.labsynch.labseer.dto.configuration;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class ClientUILabelsDTO {

    private String corpNameLabel;
    
    private String applicationNameForTitleBar;

}
