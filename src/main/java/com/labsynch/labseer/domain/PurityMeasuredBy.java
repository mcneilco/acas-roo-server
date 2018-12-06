package com.labsynch.labseer.domain;

import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findPurityMeasuredBysByCodeEquals", "findPurityMeasuredBysByNameEquals", "findPurityMeasuredBysByNameLike" })
@RooJson
public class PurityMeasuredBy {

	
    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String code;
}
