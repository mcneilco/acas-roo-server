package com.labsynch.labseer.dto.configuration;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class MainConfigDTO {

    private ServerConnectionConfigDTO serverConnection;

    private ClientUILabelsDTO clientUILabels;
    
    private MarvinDTO marvin;

    private MetaLotDTO metaLot;
    
    private ServerSettingsConfigDTO serverSettings;
    
    private BulkLoadSettingsConfigDTO bulkLoadSettings;

}
