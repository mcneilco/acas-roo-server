package com.labsynch.labseer.dto.configuration;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class ServerConnectionConfigDTO {

    private boolean connectToServer;

    private String baseServerURL;
    
    private String logoutURL;
    
    private String acasURL;

	private String acasAppURL;

}
