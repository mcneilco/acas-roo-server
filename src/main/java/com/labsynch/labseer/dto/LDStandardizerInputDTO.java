package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import flexjson.JSONSerializer;
import com.labsynch.labseer.utils.ExcludeNulls;

@RooJavaBean
@RooToString
@RooJson
public class LDStandardizerInputDTO {
    private Collection<LDStandardizerActionDTO> actions;

    private HashMap<String, String> structures;

    private String auth_token;

    private Integer timeout;

    private String output_format;

	public String toJson() {
        return new JSONSerializer().include("actions")
        		.exclude("*.class")
        		.transform(new ExcludeNulls(), void.class)
        		.serialize(this);
    }

}