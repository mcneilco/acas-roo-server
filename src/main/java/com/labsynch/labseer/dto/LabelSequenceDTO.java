package com.labsynch.labseer.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class LabelSequenceDTO {
	
    private Long numberOfLabels;

    private String thingTypeAndKind;

    private String labelTypeAndKind;

    private String labelPrefix;

    private String labelSeparator;

    private boolean groupDigits;

    private Integer digits;

    private Long latestNumber;

}
