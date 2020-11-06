package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;


import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;

@RooJavaBean
@RooToString
@RooJson
public class SearchFormDTO{

	private String corpNameList;
	
	private List<String> projects;

	private List<String> formattedCorpNameList;

	private String corpNameFrom;

	private String corpNameTo;
	
	private String aliasContSelect;
	
	private String alias;
	
	private String dateFrom;
    
	private String dateTo;

	private Date minSynthDate;
    
	private Date maxSynthDate;
	
	private String searchType;

	private Float percentSimilarity;

	private String chemist;
	
	private String molStructure;
	
	private Long buidNumber;

	private Long minParentNumber;
	
	private Long maxParentNumber;
	
	private String parentCorpName;
	
	private String saltFormCorpName;
	
	private String lotCorpName;
	
	private boolean valuesSet;
	
	private String loggedInUser;
	
	private Integer maxResults;
  

	public String toJson() {
        return new JSONSerializer().exclude("*.class")
        		.transform( new DateTransformer( "MM/dd/yyyy"), Date.class)
        		.serialize(this);
    }

	public static SearchFormDTO fromJsonToSearchFormDTO(String json) {
        return new JSONDeserializer<SearchFormDTO>().use(null, SearchFormDTO.class)
        		.use( Date.class, new DateTransformer( "MM/dd/yyyy"))
        		.deserialize(json);
    }

	public static String toJsonArray(Collection<SearchFormDTO> collection) {
        return new JSONSerializer().exclude("*.class")
        		.transform( new DateTransformer( "MM/dd/yyyy"), Date.class)
        		.serialize(collection);
    }

	public static Collection<SearchFormDTO> fromJsonArrayToSearchFoes(String json) {
        return new JSONDeserializer<List<SearchFormDTO>>().use(null, ArrayList.class).use("values", SearchFormDTO.class)
        		.use( Date.class, new DateTransformer( "MM/dd/yyyy"))
        		.deserialize(json);
    }

	public String getCorpNameList() {
        return this.corpNameList;
    }

	public void setCorpNameList(String corpNameList) {
        this.corpNameList = corpNameList;
    }

	public List<String> getProjects() {
        return this.projects;
    }

	public void setProjects(List<String> projects) {
        this.projects = projects;
    }

	public List<String> getFormattedCorpNameList() {
        return this.formattedCorpNameList;
    }

	public void setFormattedCorpNameList(List<String> formattedCorpNameList) {
        this.formattedCorpNameList = formattedCorpNameList;
    }

	public String getCorpNameFrom() {
        return this.corpNameFrom;
    }

	public void setCorpNameFrom(String corpNameFrom) {
        this.corpNameFrom = corpNameFrom;
    }

	public String getCorpNameTo() {
        return this.corpNameTo;
    }

	public void setCorpNameTo(String corpNameTo) {
        this.corpNameTo = corpNameTo;
    }

	public String getAliasContSelect() {
        return this.aliasContSelect;
    }

	public void setAliasContSelect(String aliasContSelect) {
        this.aliasContSelect = aliasContSelect;
    }

	public String getAlias() {
        return this.alias;
    }

	public void setAlias(String alias) {
        this.alias = alias;
    }

	public String getDateFrom() {
        return this.dateFrom;
    }

	public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

	public String getDateTo() {
        return this.dateTo;
    }

	public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

	public Date getMinSynthDate() {
        return this.minSynthDate;
    }

	public void setMinSynthDate(Date minSynthDate) {
        this.minSynthDate = minSynthDate;
    }

	public Date getMaxSynthDate() {
        return this.maxSynthDate;
    }

	public void setMaxSynthDate(Date maxSynthDate) {
        this.maxSynthDate = maxSynthDate;
    }

	public String getSearchType() {
        return this.searchType;
    }

	public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

	public Float getPercentSimilarity() {
        return this.percentSimilarity;
    }

	public void setPercentSimilarity(Float percentSimilarity) {
        this.percentSimilarity = percentSimilarity;
    }

	public String getChemist() {
        return this.chemist;
    }

	public void setChemist(String chemist) {
        this.chemist = chemist;
    }

	public String getMolStructure() {
        return this.molStructure;
    }

	public void setMolStructure(String molStructure) {
        this.molStructure = molStructure;
    }

	public Long getBuidNumber() {
        return this.buidNumber;
    }

	public void setBuidNumber(Long buidNumber) {
        this.buidNumber = buidNumber;
    }

	public Long getMinParentNumber() {
        return this.minParentNumber;
    }

	public void setMinParentNumber(Long minParentNumber) {
        this.minParentNumber = minParentNumber;
    }

	public Long getMaxParentNumber() {
        return this.maxParentNumber;
    }

	public void setMaxParentNumber(Long maxParentNumber) {
        this.maxParentNumber = maxParentNumber;
    }

	public String getParentCorpName() {
        return this.parentCorpName;
    }

	public void setParentCorpName(String parentCorpName) {
        this.parentCorpName = parentCorpName;
    }

	public String getSaltFormCorpName() {
        return this.saltFormCorpName;
    }

	public void setSaltFormCorpName(String saltFormCorpName) {
        this.saltFormCorpName = saltFormCorpName;
    }

	public String getLotCorpName() {
        return this.lotCorpName;
    }

	public void setLotCorpName(String lotCorpName) {
        this.lotCorpName = lotCorpName;
    }

	public boolean isValuesSet() {
        return this.valuesSet;
    }

	public void setValuesSet(boolean valuesSet) {
        this.valuesSet = valuesSet;
    }

	public String getLoggedInUser() {
        return this.loggedInUser;
    }

	public void setLoggedInUser(String loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

	public Integer getMaxResults() {
        return this.maxResults;
    }

	public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
