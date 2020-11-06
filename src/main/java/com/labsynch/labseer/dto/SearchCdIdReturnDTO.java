package com.labsynch.labseer.dto;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class SearchCdIdReturnDTO{

    private String corpName;
    
	private int CdId;
    
	private String stereoCategoryName;

	private String stereoComment;


	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static SearchCdIdReturnDTO fromJsonToSearchCdIdReturnDTO(String json) {
        return new JSONDeserializer<SearchCdIdReturnDTO>()
        .use(null, SearchCdIdReturnDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<SearchCdIdReturnDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<SearchCdIdReturnDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<SearchCdIdReturnDTO> fromJsonArrayToSearchCdIdReturnDTO(String json) {
        return new JSONDeserializer<List<SearchCdIdReturnDTO>>()
        .use("values", SearchCdIdReturnDTO.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String getCorpName() {
        return this.corpName;
    }

	public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

	public int getCdId() {
        return this.CdId;
    }

	public void setCdId(int CdId) {
        this.CdId = CdId;
    }

	public String getStereoCategoryName() {
        return this.stereoCategoryName;
    }

	public void setStereoCategoryName(String stereoCategoryName) {
        this.stereoCategoryName = stereoCategoryName;
    }

	public String getStereoComment() {
        return this.stereoComment;
    }

	public void setStereoComment(String stereoComment) {
        this.stereoComment = stereoComment;
    }
}
