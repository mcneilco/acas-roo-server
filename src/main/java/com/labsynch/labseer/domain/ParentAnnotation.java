package com.labsynch.labseer.domain;

import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders={"findParentAnnotationsByCodeEquals"})
public class ParentAnnotation {

	@NotNull
    private String code;
    
    @NotNull
    private String name;
    
    private Integer displayOrder;
    
    private String comment;
  
    
}
