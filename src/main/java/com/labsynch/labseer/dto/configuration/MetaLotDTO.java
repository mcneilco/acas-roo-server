package com.labsynch.labseer.dto.configuration;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class MetaLotDTO {

    private boolean saltBeforeLot;

    private boolean lotCalledBatch;

    private boolean showBuid;
    
    private boolean useExactMass;

}
