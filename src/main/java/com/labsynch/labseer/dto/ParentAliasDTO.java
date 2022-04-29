package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.persistence.NoResultException;

import com.labsynch.labseer.domain.ParentAlias;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


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


	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static ParentAliasDTO fromJsonToParentAliasDTO(String json) {
        return new JSONDeserializer<ParentAliasDTO>()
        .use(null, ParentAliasDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<ParentAliasDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<ParentAliasDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<ParentAliasDTO> fromJsonArrayToParentAliasDTO(String json) {
        return new JSONDeserializer<List<ParentAliasDTO>>()
        .use("values", ParentAliasDTO.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String getParentCorpName() {
        return this.parentCorpName;
    }

	public void setParentCorpName(String parentCorpName) {
        this.parentCorpName = parentCorpName;
    }

	public String getLsType() {
        return this.lsType;
    }

	public void setLsType(String lsType) {
        this.lsType = lsType;
    }

	public String getLsKind() {
        return this.lsKind;
    }

	public void setLsKind(String lsKind) {
        this.lsKind = lsKind;
    }

	public String getAliasName() {
        return this.aliasName;
    }

	public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

	public boolean isPreferred() {
        return this.preferred;
    }

	public void setPreferred(boolean preferred) {
        this.preferred = preferred;
    }

	public Integer getSortId() {
        return this.sortId;
    }

	public void setSortId(Integer sortId) {
        this.sortId = sortId;
    }

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public Boolean getIgnored() {
        return this.ignored;
    }

	public void setIgnored(Boolean ignored) {
        this.ignored = ignored;
    }

	public Integer getVersion() {
        return this.version;
    }

	public void setVersion(Integer version) {
        this.version = version;
    }
}
