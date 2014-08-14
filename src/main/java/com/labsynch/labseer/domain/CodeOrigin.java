package com.labsynch.labseer.domain;

import java.util.List;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(sequenceName = "CODE_ORIGIN_PKSEQ", finders = { "findCodeOriginsByNameEquals" })
public class CodeOrigin {

	private static final Logger logger = LoggerFactory.getLogger(CodeOrigin.class);

	
	@NotNull
	@Column(unique = true)
	@Size(max = 256)
	private String name;

	public static CodeOrigin getOrCreate(String name) {

		CodeOrigin codeOrigin = null;
		List<CodeOrigin> codeOrigins = CodeOrigin.findCodeOriginsByNameEquals(name).getResultList();
		if (codeOrigins.size() == 0){
			codeOrigin = new CodeOrigin();
			codeOrigin.setName(name);
			codeOrigin.persist();
		} else if (codeOrigins.size() == 1){
			codeOrigin = codeOrigins.get(0);
		} else if (codeOrigins.size() > 1){
			logger.error("ERROR: multiple code types with the same name");
		}
		

		return codeOrigin;
	}
}
