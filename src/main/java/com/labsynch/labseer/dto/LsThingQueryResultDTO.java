package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.labsynch.labseer.domain.AbstractThing;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.LsThingLabel;
import com.labsynch.labseer.domain.LsThingState;
import com.labsynch.labseer.domain.LsThingValue;
import com.labsynch.labseer.utils.ExcludeNulls;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class LsThingQueryResultDTO {

    Integer maxResults;

    Integer numberOfResults;

    Collection<LsThing> results;

    public LsThingQueryResultDTO() {

    }

    @Transactional
    public String toJson() {
        return new JSONSerializer().exclude("*.class")
                .include("results.lsTags", "results.lsLabels", "results.lsStates.lsValues")
                .transform(new ExcludeNulls(), void.class).serialize(this);
    }

    @Transactional
    public String toJsonWithNestedStubs() {
        return new JSONSerializer().exclude("*.class")
                .include("results.lsTags", "results.lsLabels", "results.lsStates.lsValues",
                        "results.firstLsThings.firstLsThing.lsLabels", "results.secondLsThings.secondLsThing.lsLabels")
                .transform(new ExcludeNulls(), void.class).serialize(this);
    }

    @Transactional
    public String toJsonWithNestedFull() {
        return new JSONSerializer().exclude("*.class").include("results.lsTags", "results.lsLabels",
                "results.lsStates.lsValues", "results.firstLsThings.firstLsThing.lsStates.lsValues",
                "results.firstLsThings.firstLsThing.lsLabels", "results.secondLsThings.secondLsThing.lsStates.lsValues",
                "results.secondLsThings.secondLsThing.lsLabels", "results.firstLsThings.lsStates.lsValues",
                "results.firstLsThings.lsLabels", "results.secondLsThings.lsStates.lsValues",
                "results.secondLsThings.lsLabels").transform(new ExcludeNulls(), void.class).serialize(this);
    }

    @Transactional
    public String toPrettyJson() {
        return new JSONSerializer()
                .exclude("*.class", "results.lsStates.lsValues.lsState", "results.lsStates.lsThing",
                        "results.lsLabels.lsThing")
                .include("results.lsTags", "results.lsLabels", "results.lsStates.lsValues").prettyPrint(true)
                .transform(new ExcludeNulls(), void.class).serialize(this);
    }

    @Transactional
    public String toJsonStub() {
        return new JSONSerializer().exclude("*.class", "results.lsStates").include("results.lsTags", "results.lsLabels")
                .prettyPrint(false).transform(new ExcludeNulls(), void.class).serialize(this);
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public static LsThingQueryResultDTO fromJsonToLsThingQueryResultDTO(String json) {
        return new JSONDeserializer<LsThingQueryResultDTO>()
                .use(null, LsThingQueryResultDTO.class).deserialize(json);
    }

    public static String toJsonArray(Collection<LsThingQueryResultDTO> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<LsThingQueryResultDTO> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<LsThingQueryResultDTO> fromJsonArrayToLsThingQueryResultDTO(String json) {
        return new JSONDeserializer<List<LsThingQueryResultDTO>>()
                .use("values", LsThingQueryResultDTO.class).deserialize(json);
    }

    public String toFlattenedJsonArray(Collection<LsThingReturnDTO> valueReturnDTO) {
        // Flatten the ls thing into an array of objects with key value pairs 
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode returnObject = mapper.createObjectNode();
		ArrayNode resultArray = mapper.createArrayNode();
        for(LsThing lsThing : this.getResults()) {
            ObjectNode resultObject = mapper.createObjectNode();
            resultObject.put("codeName", lsThing.getCodeName());
            resultObject.put("id", lsThing.getId());
            resultObject.put("recordedBy", lsThing.getRecordedBy());
            resultObject.put("recordedDate", lsThing.getRecordedDate().getTime());
            for (LsThingReturnDTO returnQuery : valueReturnDTO) {
                String key;
                key = returnQuery.getKey();

                // If the state type/kind and value type/kind are not null then it's a value return listing
                if(returnQuery.getStateType() != null && returnQuery.getStateKind() != null && returnQuery.getValueType() != null && returnQuery.getValueKind() != null) {
                    if(returnQuery.getKey() == null) {
                        //If key is not provided then use the lsKind of the value key
                        key = returnQuery.getValueKind();
                    }

                    // codeName is always returned so this is a reserved word.
                    if(key.equals("codeName")) {
                        throw new RuntimeException("codeName is a reserved word and cannot be used as a key");
                    }

                    // It's not allowed to pass the same key twice so throw and error if the key already exists
                    if (resultObject.get(key) != null) {
                        throw new RuntimeException("Duplicate key found in query: " + key);
                    }
                    
                    // Get the first matching value from the thing that is not ignored
                    LsThingValue v = lsThing.getLsStates().stream()
                        .filter(state -> state.getLsType().equalsIgnoreCase(returnQuery.getStateType()) && state.getLsKind().equalsIgnoreCase(returnQuery.getStateKind()) && !state.isIgnored())
                        .flatMap(state -> state.getLsValues().stream())
                        .filter(value -> value.getLsType().equalsIgnoreCase(returnQuery.getValueType()) && value.getLsKind().equalsIgnoreCase(returnQuery.getValueKind()) && !value.isIgnored())
                        .findFirst().orElse(null);
                    if(v != null) {
                        switch (returnQuery.getValueType()) {
                            case "numericValue":
                                resultObject.put(key, v.getNumericValue());
                                break;
                            case "stringValue":
                                resultObject.put(key, v.getStringValue());
                                break;
                            case "codeValue":
                                resultObject.put(key, v.getCodeValue());
                                break;
                            case "dateValue":
                                resultObject.put(key, v.getDateValue().getTime());
                                break;
                            case "blobValue":
                                resultObject.put(key, v.getBlobValue().toString());
                                break;
                            case "urlValue":
                                resultObject.put(key, v.getUrlValue());
                                break;
                            default:
                                //Add null to key
                                resultObject.put(key, (String) null);
                                break;
                        }
                    }
                // Check to see if it's a label return listing
                } else if(returnQuery.getLabelType() != null && returnQuery.getLabelKind() != null) {

                    if(returnQuery.getKey() == null) {
                        //If key is not provided then use the lsKind of the value key
                        key = returnQuery.getLabelKind();
                    }

                    // Some words like recordedBy and recordedDate are always returned and are reserved words.
                    if(key.equals("recordedBy") || key.equals("recordedDate")) {
                        throw new RuntimeException(key + " is a reserved word and cannot be used as a key");
                    }
                    
                    // It's not allowed to pass the same key twice so throw and error if the key already exists
                    if (resultObject.get(key) != null) {
                        throw new RuntimeException("Duplicate key found in query: " + key);
                    }

                    // Get the first matching label from the thing that is not ignored
                    LsThingLabel l = lsThing.getLsLabels().stream()
                        .filter(label -> label.getLsType().equalsIgnoreCase(returnQuery.getLabelType()) && label.getLsKind().equalsIgnoreCase(returnQuery.getLabelKind()) && !label.isIgnored())
                        .findFirst().orElse(null);
                    if(l != null) {
                        resultObject.put(key, l.getLabelText());
                    }
                } else {
                    // If it's determined the requested return listing is not a value or a label then throw an error
                    throw new RuntimeException("Invalid return query, must be either a value or a label make sure to include for values: stateType, stateKind, valueType, valueKind and for labels: labelType, and labelKind");
                }
            }
            resultArray.add(resultObject);
        }
        returnObject.replace("results", resultArray);
        return returnObject.toString();
    }

    public Integer getMaxResults() {
        return this.maxResults;
    }

    public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }

    public Integer getNumberOfResults() {
        return this.numberOfResults;
    }

    public void setNumberOfResults(Integer numberOfResults) {
        this.numberOfResults = numberOfResults;
    }

    public Collection<LsThing> getResults() {
        return this.results;
    }

    public void setResults(Collection<LsThing> results) {
        this.results = results;
    }
}
