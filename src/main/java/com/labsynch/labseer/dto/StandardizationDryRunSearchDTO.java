package com.labsynch.labseer.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;


@RooJavaBean
@RooToString
@RooJson

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

    private String[] corpNames;

    private Boolean hasNewDuplicates;

    private Boolean hasExistingDuplicates;

    private Boolean asDrawnDisplayChange;

    private Boolean changedStructure;

    private Boolean displayChange;

}
