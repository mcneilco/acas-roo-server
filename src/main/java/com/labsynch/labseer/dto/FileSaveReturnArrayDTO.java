package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


public class FileSaveReturnArrayDTO{
			 
	private List<FileSaveReturnDTO> returnFileList = new ArrayList<FileSaveReturnDTO>();



	public String toJson() {
        return new JSONSerializer().exclude("*.class")
        		.exclude("returnFileList.file")
        		.serialize(this);
    }

	public static FileSaveReturnArrayDTO fromJsonToFileSaveReturnArrayDTO(String json) {
        return new JSONDeserializer<FileSaveReturnArrayDTO>().use(null, FileSaveReturnArrayDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<FileSaveReturnArrayDTO> collection) {
        return new JSONSerializer().exclude("*.class")
        		.exclude("returnFileList.file")
        		.serialize(collection);
    }

	public static Collection<FileSaveReturnArrayDTO> fromJsonArrayToFileSaveReturnArrayDTO(String json) {
        return new JSONDeserializer<List<FileSaveReturnArrayDTO>>().use(null, ArrayList.class).use("values", FileSaveReturnArrayDTO.class).deserialize(json);
    }

	public List<FileSaveReturnDTO> getReturnFileList() {
        return this.returnFileList;
    }

	public void setReturnFileList(List<FileSaveReturnDTO> returnFileList) {
        this.returnFileList = returnFileList;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
