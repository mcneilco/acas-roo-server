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
public class SearchLotDTO {

    private String corpName;

    private int lotNumber;
    
    private long buid;

    private List<LotAliasDTO> lotAliases;
    
    private Date registrationDate;

    private Date synthesisDate;

    
	public String toJson() {
        return new JSONSerializer().exclude("*.class")
        		.transform( new DateTransformer( "MM/dd/yyyy"), Date.class)
        		.serialize(this);
    }

	public static SearchLotDTO fromJsonToSearchLotDTO(String json) {
        return new JSONDeserializer<SearchLotDTO>().use(null, SearchLotDTO.class)
        		.use( Date.class, new DateTransformer( "MM/dd/yyyy"))
        		.deserialize(json);
    }

	public static String toJsonArray(Collection<SearchLotDTO> collection) {
        return new JSONSerializer().exclude("*.class")
        		.transform( new DateTransformer( "MM/dd/yyyy"), Date.class)
        		.serialize(collection);
    }

	public static Collection<SearchLotDTO> fromJsonArrayToSearchLoes(String json) {
        return new JSONDeserializer<List<SearchLotDTO>>().use(null, ArrayList.class).use("values", SearchLotDTO.class)
        		.use( Date.class, new DateTransformer( "MM/dd/yyyy"))
        		.deserialize(json);
    }

	public String getCorpName() {
        return this.corpName;
    }

	public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

	public int getLotNumber() {
        return this.lotNumber;
    }

	public void setLotNumber(int lotNumber) {
        this.lotNumber = lotNumber;
    }

	public long getBuid() {
        return this.buid;
    }

	public void setBuid(long buid) {
        this.buid = buid;
    }

	public List<LotAliasDTO> getLotAliases() {
        return this.lotAliases;
    }

	public void setLotAliases(List<LotAliasDTO> lotAliases) {
        this.lotAliases = lotAliases;
    }

	public Date getRegistrationDate() {
        return this.registrationDate;
    }

	public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

	public Date getSynthesisDate() {
        return this.synthesisDate;
    }

	public void setSynthesisDate(Date synthesisDate) {
        this.synthesisDate = synthesisDate;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
