package com.labsynch.labseer.dto;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;


public class BatchCodeDTO {
	
	private String batchCode;
	
	public static String[] getColumns(){
		String[] headerColumns = new String[] {
				"batchCode"};

		return headerColumns;

	}

	public static CellProcessor[] getProcessors() {
		final CellProcessor[] processors = new CellProcessor[] { 
				new Optional()
		};

		return processors;
	}


	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static BatchCodeDTO fromJsonToBatchCodeDTO(String json) {
        return new JSONDeserializer<BatchCodeDTO>()
        .use(null, BatchCodeDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<BatchCodeDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<BatchCodeDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<BatchCodeDTO> fromJsonArrayToBatchCoes(String json) {
        return new JSONDeserializer<List<BatchCodeDTO>>()
        .use("values", BatchCodeDTO.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String getBatchCode() {
        return this.batchCode;
    }

	public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }
}


