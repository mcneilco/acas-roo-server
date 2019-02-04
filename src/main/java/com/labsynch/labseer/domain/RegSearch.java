package com.labsynch.labseer.domain;

import javax.persistence.Lob;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class RegSearch {

	
    private String corpName;

    @Lob
    //@Size(max = 10485760)
    private String molStructure;

}
