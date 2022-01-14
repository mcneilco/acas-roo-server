package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;


@RooJavaBean
@RooToString
@RooJson

public class StandardizationDryRunSearchDTO {

    class NumericWithOperator {
        Double numeric;
        String operator;
    }
    
    private NumericWithOperator deltaMolWeight;
    

}
