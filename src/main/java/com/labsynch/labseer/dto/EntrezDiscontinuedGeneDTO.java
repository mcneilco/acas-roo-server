package com.labsynch.labseer.dto;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class EntrezDiscontinuedGeneDTO {
	
	//tax_id  GeneID  Discontinued_GeneID     Discontinued_Symbol     Discontinue_Date
	
    private String taxId;
    private String geneId;
    private String discontinuedGeneId;
    private String discontinuedSymbol;    
    private Date discontinuedDate;


	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static EntrezDiscontinuedGeneDTO fromJsonToEntrezDiscontinuedGeneDTO(String json) {
        return new JSONDeserializer<EntrezDiscontinuedGeneDTO>()
        .use(null, EntrezDiscontinuedGeneDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<EntrezDiscontinuedGeneDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<EntrezDiscontinuedGeneDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<EntrezDiscontinuedGeneDTO> fromJsonArrayToEntrezDiscoes(String json) {
        return new JSONDeserializer<List<EntrezDiscontinuedGeneDTO>>()
        .use("values", EntrezDiscontinuedGeneDTO.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String getTaxId() {
        return this.taxId;
    }

	public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

	public String getGeneId() {
        return this.geneId;
    }

	public void setGeneId(String geneId) {
        this.geneId = geneId;
    }

	public String getDiscontinuedGeneId() {
        return this.discontinuedGeneId;
    }

	public void setDiscontinuedGeneId(String discontinuedGeneId) {
        this.discontinuedGeneId = discontinuedGeneId;
    }

	public String getDiscontinuedSymbol() {
        return this.discontinuedSymbol;
    }

	public void setDiscontinuedSymbol(String discontinuedSymbol) {
        this.discontinuedSymbol = discontinuedSymbol;
    }

	public Date getDiscontinuedDate() {
        return this.discontinuedDate;
    }

	public void setDiscontinuedDate(Date discontinuedDate) {
        this.discontinuedDate = discontinuedDate;
    }
}
