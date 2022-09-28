package com.labsynch.labseer.dto;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.LsThingLabel;
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

    public String toFlattenedJsonArray(LsThingReturnDTO valueReturnDTO) {
        // Flatten the ls thing into an array of objects with key value pairs 
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = new JSONSerializer()
                .exclude("*.class", "results").serialize(this);
        ObjectNode returnObject;
        try {
            returnObject = (ObjectNode) mapper.readTree(jsonString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse lsThingJson", e);
        }

        // If results empty, then return empty results
        if(this.getResults()== null) {
            return returnObject.toString();
        }
		ArrayNode resultArray = mapper.createArrayNode();

        // Get a list of thing attributes to return with the flattened objects (these are reserved words which cannot be used as keys)
        Set<String> allThingAttributesReservedWords = new HashSet<>(Arrays.asList("codeName", "id", "recordedBy", "recordedDate", "modifiedBy", "modifiedDate", "lsType", "lsKind", "lsTransaction"));
        Set<String> thingAttributesToAdd = new HashSet<>();
        if(valueReturnDTO.getThingAttributes() != null) {
            // Print a warning if a passed thing attribute isn't a thing attribute
            for(String thingAttribute : valueReturnDTO.getThingAttributes()) {
                if(!allThingAttributesReservedWords.contains(thingAttribute)) {
                    System.out.println("WARNING: " + thingAttribute + " is not a thing attribute. It will be ignored.");
                } else {
                    thingAttributesToAdd.add(thingAttribute);
                }
            }
        } else {
            // If no thing attributes are passed, use all reserved words
            thingAttributesToAdd = allThingAttributesReservedWords;
        }
        
        for(LsThing lsThing : this.getResults()) {
            ObjectNode resultObject = mapper.createObjectNode();

            // Convert the LsThing to a JsonNode so that we can dynamically fetch it's attributes by string name
            // This json serialization excludes the lsStates and lsLabels and other nested attributes
            String lsThingJson = lsThing.toJsonNoNestedAttributes();
            ObjectNode lsThingObject;
            try {
                lsThingObject = (ObjectNode) mapper.readTree(lsThingJson);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to parse lsThingJson", e);
            }

            // Add thing attributes to the result object first
            for(String thingAttribute : thingAttributesToAdd) {
                if(lsThingObject.has(thingAttribute)) {
                    resultObject.replace(thingAttribute, lsThingObject.get(thingAttribute));
                }
            }
            
            for (LsThingReturnValueDTO returnQuery : valueReturnDTO.getThingValues()) {

                // Validate the key uniqueness and reserved thing words
                String key;
                key = returnQuery.getKey();
                if(key == null) {
                    throw new RuntimeException("Key is required for thing values");
                } else {
                    // Make sure key isn't in the list of thing attributes already being returned
                    if(thingAttributesToAdd.contains(key)) {
                        throw new RuntimeException("Key '" + key + "' is already being returned as a thing attribute");
                    }
                    // Make sure this isn't a duplicate key
                    if(resultObject.has(key)) {
                        throw new RuntimeException("Key '" + key + "' is a duplicate. All keys must be unique.");
                    }
                }

                // If the state type/kind and value type/kind are not null then it's a value return listing
                if(returnQuery.getStateType() != null && returnQuery.getStateKind() != null && returnQuery.getValueType() != null && returnQuery.getValueKind() != null) {
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
