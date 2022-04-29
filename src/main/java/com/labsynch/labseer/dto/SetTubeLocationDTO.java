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
public class SetTubeLocationDTO {

	private String barcode;
	
	private String locationBreadCrumb;
		
	private String user;
		
	private Date date;
	
	private String rootLabel;
	

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String getBarcode() {
        return this.barcode;
    }

	public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

	public String getLocationBreadCrumb() {
        return this.locationBreadCrumb;
    }

	public void setLocationBreadCrumb(String locationBreadCrumb) {
        this.locationBreadCrumb = locationBreadCrumb;
    }

	public String getUser() {
        return this.user;
    }

	public void setUser(String user) {
        this.user = user;
    }

	public Date getDate() {
        return this.date;
    }

	public void setDate(Date date) {
        this.date = date;
    }

	public String getRootLabel() {
        return this.rootLabel;
    }

	public void setRootLabel(String rootLabel) {
        this.rootLabel = rootLabel;
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static SetTubeLocationDTO fromJsonToSetTubeLocationDTO(String json) {
        return new JSONDeserializer<SetTubeLocationDTO>()
        .use(null, SetTubeLocationDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<SetTubeLocationDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<SetTubeLocationDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<SetTubeLocationDTO> fromJsonArrayToSetTubeLocatioes(String json) {
        return new JSONDeserializer<List<SetTubeLocationDTO>>()
        .use("values", SetTubeLocationDTO.class).deserialize(json);
    }
}
