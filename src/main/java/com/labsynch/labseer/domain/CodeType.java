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
@RooJpaActiveRecord(sequenceName = "CODE_TYPE_PKSEQ", finders = { "findCodeTypesByTypeNameEquals" })
public class CodeType {

	private static final Logger logger = LoggerFactory.getLogger(CodeType.class);

	
	@NotNull
	@Column(unique = true)
	@Size(max = 128)
	private String typeName;

	public static CodeType getOrCreate(String name) {

		CodeType codeType = null;
		List<CodeType> codeTypes = CodeType.findCodeTypesByTypeNameEquals(name).getResultList();
		if (codeTypes.size() == 0){
			codeType = new CodeType();
			codeType.setTypeName(name);
			codeType.persist();
		} else if (codeTypes.size() == 1){
			codeType = codeTypes.get(0);
		} else if (codeTypes.size() > 1){
			logger.error("ERROR: multiple code types with the same name");
		}
		

		return codeType;
	}
}
