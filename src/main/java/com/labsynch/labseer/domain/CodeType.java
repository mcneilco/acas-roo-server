package com.labsynch.labseer.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@Entity
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

	@Id
    @SequenceGenerator(name = "codeTypeGen", sequenceName = "CODE_TYPE_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "codeTypeGen")
    @Column(name = "id")
    private Long id;

	@Version
    @Column(name = "version")
    private Integer version;

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public Integer getVersion() {
        return this.version;
    }

	public void setVersion(Integer version) {
        this.version = version;
    }
}
