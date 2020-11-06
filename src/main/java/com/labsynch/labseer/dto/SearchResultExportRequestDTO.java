package com.labsynch.labseer.dto;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.labsynch.labseer.utils.ExcludeNulls;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;

public class SearchResultExportRequestDTO {

    private String filePath;
    
    private SearchFormReturnDTO searchFormResultsDTO;
    
    public SearchResultExportRequestDTO(){
    	
    }
    
    public SearchResultExportRequestDTO(String filePath, SearchFormReturnDTO searchFormResultsDTO){
    	this.filePath = filePath;
    	this.searchFormResultsDTO = searchFormResultsDTO;
    }
    
    public String toJson() {
        return new JSONSerializer().include("searchFormResultsDTO.foundCompounds.lotIDs").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }
    

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String getFilePath() {
        return this.filePath;
    }

	public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

	public SearchFormReturnDTO getSearchFormResultsDTO() {
        return this.searchFormResultsDTO;
    }

	public void setSearchFormResultsDTO(SearchFormReturnDTO searchFormResultsDTO) {
        this.searchFormResultsDTO = searchFormResultsDTO;
    }

	public static SearchResultExportRequestDTO fromJsonToSearchResultExportRequestDTO(String json) {
        return new JSONDeserializer<SearchResultExportRequestDTO>()
        .use(null, SearchResultExportRequestDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<SearchResultExportRequestDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<SearchResultExportRequestDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<SearchResultExportRequestDTO> fromJsonArrayToSearchResultExpoes(String json) {
        return new JSONDeserializer<List<SearchResultExportRequestDTO>>()
        .use("values", SearchResultExportRequestDTO.class).deserialize(json);
    }
}
