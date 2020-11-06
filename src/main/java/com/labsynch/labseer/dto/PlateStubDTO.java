package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class PlateStubDTO {
	
	private String barcode;
	private String codeName;
	private String plateType;
	
	private Collection<WellStubDTO> wells;
	
	public PlateStubDTO(){
	}
	
	public PlateStubDTO(String barcode, String codeName, String plateType){
		this.barcode = barcode;
		this.codeName = codeName;
		this.plateType = plateType;
	}
	
	public String toJson() {
        return new JSONSerializer().include("wells").exclude("*.class").serialize(this);
    }
	


	public String getBarcode() {
        return this.barcode;
    }

	public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

	public String getCodeName() {
        return this.codeName;
    }

	public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

	public String getPlateType() {
        return this.plateType;
    }

	public void setPlateType(String plateType) {
        this.plateType = plateType;
    }

	public Collection<WellStubDTO> getWells() {
        return this.wells;
    }

	public void setWells(Collection<WellStubDTO> wells) {
        this.wells = wells;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public static PlateStubDTO fromJsonToPlateStubDTO(String json) {
        return new JSONDeserializer<PlateStubDTO>()
        .use(null, PlateStubDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<PlateStubDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<PlateStubDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<PlateStubDTO> fromJsonArrayToPlateStubDTO(String json) {
        return new JSONDeserializer<List<PlateStubDTO>>()
        .use("values", PlateStubDTO.class).deserialize(json);
    }
}


