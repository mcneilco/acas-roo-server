package com.labsynch.labseer.domain;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.dto.ParentAliasDTO;
import com.labsynch.labseer.exceptions.ParentNotFoundException;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders={"findParentAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals", 
		"findParentAliasesByParent", "findParentAliasesByAliasNameEquals", 
		"findParentAliasesByParentAndLsTypeEqualsAndLsKindEquals",
		"findParentAliasesByParentAndLsTypeEqualsAndLsKindEqualsAndAliasNameEquals"})
public class ParentAlias {

	private static final Logger logger = LoggerFactory.getLogger(ParentAlias.class);
	
	@ManyToOne
    @org.hibernate.annotations.Index(name="ParentAlias_Parent_IDX")
	@JoinColumn(name = "parent")
	private Parent parent;
	
    private String lsType;

    private String lsKind;
    
    private String aliasName;
    
	private boolean preferred;
	
	private boolean ignored;
	
	private boolean deleted;
	
	private Integer sortId;
	

	public ParentAlias(){
	}
	
	public ParentAlias(Parent parent, String lsType, String lsKind, String aliasName, boolean preferred){
		this.parent = parent;
		this.lsType = lsType;
		this.lsKind = lsKind;
		this.aliasName = aliasName;
		this.preferred = preferred;
	}
	
	public ParentAlias(ParentAliasDTO parentAliasDTO) throws ParentNotFoundException{
		try{
			Parent parent = Parent.findParentsByCorpNameEquals(parentAliasDTO.getParentCorpName()).getSingleResult();
			this.parent = parent;
		}catch (Exception e){
			logger.error("Parent "+parentAliasDTO.getParentCorpName()+" could not be found.",e);
			throw new ParentNotFoundException("Parent "+parentAliasDTO.getParentCorpName()+" could not be found.");
		}
		this.lsType = parentAliasDTO.getLsType();
		this.lsKind = parentAliasDTO.getLsKind();
		this.aliasName = parentAliasDTO.getAliasName();
		this.preferred = parentAliasDTO.isPreferred();
		this.ignored = parentAliasDTO.getIgnored();
		this.setId(parentAliasDTO.getId());
		this.setVersion(parentAliasDTO.getVersion());
	}
}
