package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.service.ErrorMessage;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;

@RooJavaBean
@RooToString
@RooJson
public class MetalotReturn {
	
	Metalot metalot;
	ArrayList<ErrorMessage> errors;
	
	@Transactional
	public String toJson() {
        String json = new JSONSerializer().include("errors","metalot.isosalts", "metalot.fileLists", "metalot.lot", "metalot.lot.lotAliases", "metalot.lot.saltForm.parent.parentAliases").exclude("*.class", "metalot.isosalts.saltForm", "metalot.fileList.lot")
        		.transform( new DateTransformer( "MM/dd/yyyy"), Date.class)
        		.serialize(this);
//        System.out.println("fromMetaLotToJson");
//        System.out.println(json);
        return json;
	}


	public Metalot getMetalot() {
        return this.metalot;
    }

	public void setMetalot(Metalot metalot) {
        this.metalot = metalot;
    }

	public ArrayList<ErrorMessage> getErrors() {
        return this.errors;
    }

	public void setErrors(ArrayList<ErrorMessage> errors) {
        this.errors = errors;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public static MetalotReturn fromJsonToMetalotReturn(String json) {
        return new JSONDeserializer<MetalotReturn>()
        .use(null, MetalotReturn.class).deserialize(json);
    }

	public static String toJsonArray(Collection<MetalotReturn> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<MetalotReturn> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<MetalotReturn> fromJsonArrayToMetalotReturns(String json) {
        return new JSONDeserializer<List<MetalotReturn>>()
        .use("values", MetalotReturn.class).deserialize(json);
    }
}
