package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.Container;
import com.labsynch.labseer.domain.ContainerLabel;
import com.labsynch.labseer.service.ContainerService;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class WellStubDTO {
	
	private String wellName;
	private String codeName;
	
	private Integer rowIndex;
	
	private Integer columnIndex;
	
	public WellStubDTO(){
	}
	
	public WellStubDTO(String wellName, String codeName, Integer rowIndex, Integer columnIndex){
		this.wellName = wellName;
		this.codeName = codeName;
		this.rowIndex = rowIndex;
		this.columnIndex = columnIndex;
	}

	public static Collection<WellStubDTO> convertToWellStubDTOs(
			Collection<Container> wells) {
		Collection<WellStubDTO> wellStubs = new ArrayList<WellStubDTO>();
		for (Container well : wells){
			String wellName = ContainerLabel.pickBestLabel(well.getLsLabels()).getLabelText();
			WellStubDTO wellStub = new WellStubDTO(wellName, well.getCodeName(), well.getRowIndex(), well.getColumnIndex());
			wellStubs.add(wellStub);
		}
		return wellStubs;
	}
	
	public static Collection<WellStubDTO> convertToWellStubDTOs(
			List<Container> wells, List<ContainerLabel> wellNames) {
		Collection<WellStubDTO> wellStubs = new ArrayList<WellStubDTO>();
		int i = 0;
		for (Container well : wells){
			String wellName = wellNames.get(i).getLabelText();
			WellStubDTO wellStub = new WellStubDTO(wellName, well.getCodeName(), well.getRowIndex(), well.getColumnIndex());
			wellStubs.add(wellStub);
			i++;
		}
		return wellStubs;
	}
	


	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String getWellName() {
        return this.wellName;
    }

	public void setWellName(String wellName) {
        this.wellName = wellName;
    }

	public String getCodeName() {
        return this.codeName;
    }

	public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

	public Integer getRowIndex() {
        return this.rowIndex;
    }

	public void setRowIndex(Integer rowIndex) {
        this.rowIndex = rowIndex;
    }

	public Integer getColumnIndex() {
        return this.columnIndex;
    }

	public void setColumnIndex(Integer columnIndex) {
        this.columnIndex = columnIndex;
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static WellStubDTO fromJsonToWellStubDTO(String json) {
        return new JSONDeserializer<WellStubDTO>()
        .use(null, WellStubDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<WellStubDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<WellStubDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<WellStubDTO> fromJsonArrayToWellStubDTO(String json) {
        return new JSONDeserializer<List<WellStubDTO>>()
        .use("values", WellStubDTO.class).deserialize(json);
    }
}


