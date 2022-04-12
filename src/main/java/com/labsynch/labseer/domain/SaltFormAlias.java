package com.labsynch.labseer.domain;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.dto.SaltFormAliasDTO;
import com.labsynch.labseer.exceptions.ParentNotFoundException;
import com.labsynch.labseer.exceptions.SaltFormNotFoundException;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders={"findSaltFormAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals", "findSaltFormAliasesBySaltForm"})
public class SaltFormAlias {

	private static final Logger logger = LoggerFactory.getLogger(SaltFormAlias.class);
	
	@ManyToOne
    @org.hibernate.annotations.Index(name="SaltFormAlias_SaltForm_IDX")
	@JoinColumn(name = "salt_form")
	private SaltForm saltForm;
	
    private String lsType;

    private String lsKind;
    
    private String aliasName;
    
	private boolean preferred;
	
	private boolean ignored;
	
	private boolean deleted;
    
	public SaltFormAlias(){
	}
	
	public SaltFormAlias(SaltForm saltForm, String lsType, String lsKind, String aliasName, boolean preferred){
		this.saltForm = saltForm;
		this.lsType = lsType;
		this.lsKind = lsKind;
		this.aliasName = aliasName;
		this.preferred = preferred;
	}
	
	public SaltFormAlias(SaltFormAliasDTO saltFormAliasDTO) throws SaltFormNotFoundException{
		try{
			SaltForm saltForm = SaltForm.findSaltFormsByCorpNameEquals(saltFormAliasDTO.getSaltFormCorpName()).getSingleResult();
			this.saltForm = saltForm;
		}catch (Exception e){
			logger.error("SaltForm"+saltFormAliasDTO.getSaltFormCorpName()+" could not be found.",e);
			throw new SaltFormNotFoundException("SaltForm "+saltFormAliasDTO.getSaltFormCorpName()+" could not be found.");
		}
		this.lsType = saltFormAliasDTO.getLsType();
		this.lsKind = saltFormAliasDTO.getLsKind();
		this.aliasName = saltFormAliasDTO.getAliasName();
		this.preferred = saltFormAliasDTO.isPreferred();
	}
}
