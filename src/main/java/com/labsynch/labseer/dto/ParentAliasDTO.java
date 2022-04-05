package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.persistence.NoResultException;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.Parent;
import com.labsynch.labseer.domain.ParentAlias;

@RooJavaBean
@RooToString
@RooJson
public class ParentAliasDTO {

	private String parentCorpName;

	private String lsType;

	private String lsKind;

	private String aliasName;

	private boolean preferred;
	
	private Integer sortId;
	
	private Long id; 
	
	private Boolean ignored;
	
	private Integer version;

	public ParentAliasDTO(){

	}

	public ParentAliasDTO(ParentAlias parentAlias){
		this.parentCorpName = parentAlias.getParent().getCorpName();
		this.lsType = parentAlias.getLsType();
		this.lsKind = parentAlias.getLsKind();
		this.aliasName = parentAlias.getAliasName();
		this.preferred = parentAlias.isPreferred();
		this.sortId = parentAlias.getSortId();
		this.id = parentAlias.getId();
		this.ignored = parentAlias.isIgnored();
		this.version = parentAlias.getVersion();
	}

	public boolean getPreferred(){
		return this.preferred;
	}

	public static ParentAliasDTO getParentByAlias(ParentAliasDTO parentAliasDTO) {
		ParentAlias parentAlias;
		try {
			parentAlias = ParentAlias.findParentAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals(parentAliasDTO.getAliasName(), parentAliasDTO.getLsType(), parentAliasDTO.getLsKind()).getSingleResult();
		} catch (NoResultException e){
			parentAlias = null;
		}
		return new ParentAliasDTO(parentAlias);
	}

	public static Collection<ParentAliasDTO> getParentsByAlias(Collection<ParentAliasDTO> parentAliasDTOs) {
		Collection<ParentAliasDTO> results = new HashSet<ParentAliasDTO>();
		Collection<ParentAlias> parentAliases = null;
		for (ParentAliasDTO parentAliasDTO : parentAliasDTOs){
			parentAliases = ParentAlias.findParentAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals(parentAliasDTO.getAliasName(), parentAliasDTO.getLsType(), parentAliasDTO.getLsKind()).getResultList();
			for (ParentAlias alias : parentAliases){
				results.add(new ParentAliasDTO(alias));
			}
		}
		return results;
	}

}
