package com.labsynch.labseer.domain;

import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;


@RooJavaBean
@RooToString
@RooJpaActiveRecord
@RooJson
public class CorpName {

	@Size(max = 50)
	private String parentCorpName;

	@Size(max = 50)
	private String comment;

	private Boolean ignore;

}
