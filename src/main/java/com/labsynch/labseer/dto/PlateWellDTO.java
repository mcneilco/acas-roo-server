package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


public class PlateWellDTO {
	
	private String plateBarcode;
	private String plateCodeName;
	
	private String wellCodeName;
	
	private String wellLabel;
	
	public PlateWellDTO(){
	}
	
	public PlateWellDTO(String plateBarcode, String plateCodeName, String wellCodeName, String wellLabel){
		this.plateBarcode = plateBarcode;
		this.plateCodeName = plateCodeName;
		this.wellCodeName = wellCodeName;
		this.wellLabel = wellLabel;
	}
	


	public String getPlateBarcode() {
        return this.plateBarcode;
    }

	public void setPlateBarcode(String plateBarcode) {
        this.plateBarcode = plateBarcode;
    }

	public String getPlateCodeName() {
        return this.plateCodeName;
    }

	public void setPlateCodeName(String plateCodeName) {
        this.plateCodeName = plateCodeName;
    }

	public String getWellCodeName() {
        return this.wellCodeName;
    }

	public void setWellCodeName(String wellCodeName) {
        this.wellCodeName = wellCodeName;
    }

	public String getWellLabel() {
        return this.wellLabel;
    }

	public void setWellLabel(String wellLabel) {
        this.wellLabel = wellLabel;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static PlateWellDTO fromJsonToPlateWellDTO(String json) {
        return new JSONDeserializer<PlateWellDTO>()
        .use(null, PlateWellDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<PlateWellDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<PlateWellDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<PlateWellDTO> fromJsonArrayToPlateWellDTO(String json) {
        return new JSONDeserializer<List<PlateWellDTO>>()
        .use("values", PlateWellDTO.class).deserialize(json);
    }
}


