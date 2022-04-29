package com.labsynch.labseer.dto;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule.RegistrationStatus;
import com.labsynch.labseer.chemclasses.CmpdRegMolecule.StandardizationStatus;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;




public class StandardizationDryRunSearchDTO {

    public static class NumericWithOperator {
        Double value;
        String operator;

        public NumericWithOperator() {

        }
        public NumericWithOperator(Double value, String operator) {
            this.value = value;
            this.operator = operator;
        }

        public Double getValue() {
            return value;
        }

        public void setValue(Double value) {
            this.value = value;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

    }
    
    private NumericWithOperator oldMolWeight;

    private NumericWithOperator newMolWeight;

    private NumericWithOperator deltaMolWeight;

    private String[] corpNames;

    private Boolean includeCorpNames;

    private Boolean hasNewDuplicates;

    private Boolean hasExistingDuplicates;

    private Boolean asDrawnDisplayChange;

    private Boolean changedStructure;

    private Boolean displayChange;

    private String filePath;

    private Integer maxResults;

    private StandardizationStatus[] standardizationStatuses;

    private RegistrationStatus[] registrationStatuses;


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

	public static StandardizationDryRunSearchDTO fromJsonToStandardizationDryRunSearchDTO(String json) {
        return new JSONDeserializer<StandardizationDryRunSearchDTO>()
        .use(null, StandardizationDryRunSearchDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<StandardizationDryRunSearchDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<StandardizationDryRunSearchDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<StandardizationDryRunSearchDTO> fromJsonArrayToStandardizatioes(String json) {
        return new JSONDeserializer<List<StandardizationDryRunSearchDTO>>()
        .use("values", StandardizationDryRunSearchDTO.class).deserialize(json);
    }

	public NumericWithOperator getOldMolWeight() {
        return this.oldMolWeight;
    }

	public void setOldMolWeight(NumericWithOperator oldMolWeight) {
        this.oldMolWeight = oldMolWeight;
    }

	public NumericWithOperator getNewMolWeight() {
        return this.newMolWeight;
    }

	public void setNewMolWeight(NumericWithOperator newMolWeight) {
        this.newMolWeight = newMolWeight;
    }

	public NumericWithOperator getDeltaMolWeight() {
        return this.deltaMolWeight;
    }

	public void setDeltaMolWeight(NumericWithOperator deltaMolWeight) {
        this.deltaMolWeight = deltaMolWeight;
    }

	public String[] getCorpNames() {
        return this.corpNames;
    }

	public void setCorpNames(String[] corpNames) {
        this.corpNames = corpNames;
    }

	public Boolean getIncludeCorpNames() {
        return this.includeCorpNames;
    }

	public void setIncludeCorpNames(Boolean includeCorpNames) {
        this.includeCorpNames = includeCorpNames;
    }

	public Boolean getHasNewDuplicates() {
        return this.hasNewDuplicates;
    }

	public void setHasNewDuplicates(Boolean hasNewDuplicates) {
        this.hasNewDuplicates = hasNewDuplicates;
    }

	public Boolean getHasExistingDuplicates() {
        return this.hasExistingDuplicates;
    }

	public void setHasExistingDuplicates(Boolean hasExistingDuplicates) {
        this.hasExistingDuplicates = hasExistingDuplicates;
    }

	public Boolean getAsDrawnDisplayChange() {
        return this.asDrawnDisplayChange;
    }

	public void setAsDrawnDisplayChange(Boolean asDrawnDisplayChange) {
        this.asDrawnDisplayChange = asDrawnDisplayChange;
    }

	public Boolean getChangedStructure() {
        return this.changedStructure;
    }

	public void setChangedStructure(Boolean changedStructure) {
        this.changedStructure = changedStructure;
    }

	public Boolean getDisplayChange() {
        return this.displayChange;
    }

	public void setDisplayChange(Boolean displayChange) {
        this.displayChange = displayChange;
    }

	public String getFilePath() {
        return this.filePath;
    }

	public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

	public Integer getMaxResults() {
        return this.maxResults;
    }

	public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }

	public StandardizationStatus[] getStandardizationStatuses() {
        return this.standardizationStatuses;
    }

	public void setStandardizationStatuses(StandardizationStatus[] standardizationStatuses) {
        this.standardizationStatuses = standardizationStatuses;
    }

	public RegistrationStatus[] getRegistrationStatuses() {
        return this.registrationStatuses;
    }

	public void setRegistrationStatuses(RegistrationStatus[] registrationStatuses) {
        this.registrationStatuses = registrationStatuses;
    }
}
