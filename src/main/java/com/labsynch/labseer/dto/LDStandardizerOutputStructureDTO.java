package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;

@RooJavaBean
@RooToString
@RooJson
public class LDStandardizerOutputStructureDTO {
    private String status;

    private String structure;

    private String format;

}