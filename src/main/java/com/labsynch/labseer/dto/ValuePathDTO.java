package com.labsynch.labseer.dto;

import java.math.BigDecimal;
import java.util.Set;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.ItxLsThingLsThing;
import com.labsynch.labseer.domain.ItxLsThingLsThingState;
import com.labsynch.labseer.domain.ItxLsThingLsThingValue;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.LsThingState;
import com.labsynch.labseer.domain.LsThingValue;


@RooJavaBean
@RooToString
@RooJson
public class ValuePathDTO {
	
    public ValuePathDTO() {

    }

    
    private String entity;
    private String entityType;
    private String entityKind;
    private String stateType;
    private String stateKind;
    private String valueType;
    private String valueKind;
    
    public String extractStringValue(LsThing lsThing){
    	if (entity.equalsIgnoreCase("LsThing")
    			&& entityType.equals(lsThing.getLsType())
    			&& entityKind.equals(lsThing.getLsKind())){
    		Set<LsThingState> lsStates = lsThing.getLsStates();
    		for (LsThingState lsState : lsStates){
    			if (stateType.equals(lsState.getLsType()) && stateKind.equals(lsState.getLsKind())){
    				Set<LsThingValue> lsValues = lsState.getLsValues();
    				for (LsThingValue lsValue : lsValues){
    					if (valueType.equals(lsValue.getLsType()) && valueKind.equals(lsValue.getLsKind())){
    						if (valueType.equals("stringValue")) return lsValue.getStringValue();
    						if (valueType.equals("codeValue")) return lsValue.getCodeValue();
    					}
    				}
    			}
    		}
    	}
    	return null;
    }
    
    public BigDecimal extractNumericValue(LsThing lsThing){
    	if (entity.equalsIgnoreCase("LsThing")
    			&& entityType.equals(lsThing.getLsType())
    			&& entityKind.equals(lsThing.getLsKind())){
    		Set<LsThingState> lsStates = lsThing.getLsStates();
    		for (LsThingState lsState : lsStates){
    			if (stateType.equals(lsState.getLsType()) && stateKind.equals(lsState.getLsKind())){
    				Set<LsThingValue> lsValues = lsState.getLsValues();
    				for (LsThingValue lsValue : lsValues){
    					if (valueType.equals(lsValue.getLsType()) && valueKind.equals(lsValue.getLsKind())){
    						if (valueType.equals("stringValue")) return lsValue.getNumericValue();
    					}
    				}
    			}
    		}
    	}
    	return null;
    }
    
    public String extractStringValue(ItxLsThingLsThing ItxLsThingLsThing){
    	if (entity.equalsIgnoreCase("ItxLsThingLsThing")
    			&& entityType.equals(ItxLsThingLsThing.getLsType())
    			&& entityKind.equals(ItxLsThingLsThing.getLsKind())){
    		Set<ItxLsThingLsThingState> lsStates = ItxLsThingLsThing.getLsStates();
    		for (ItxLsThingLsThingState lsState : lsStates){
    			if (stateType.equals(lsState.getLsType()) && stateKind.equals(lsState.getLsKind())){
    				Set<ItxLsThingLsThingValue> lsValues = lsState.getLsValues();
    				for (ItxLsThingLsThingValue lsValue : lsValues){
    					if (valueType.equals(lsValue.getLsType()) && valueKind.equals(lsValue.getLsKind())){
    						if (valueType.equals("stringValue")) return lsValue.getStringValue();
    						if (valueType.equals("codeValue")) return lsValue.getCodeValue();
    					}
    				}
    			}
    		}
    	}
    	return null;
    }
    
    public BigDecimal extractNumericValue(ItxLsThingLsThing ItxLsThingLsThing){
    	if (entity.equalsIgnoreCase("ItxLsThingLsThing")
    			&& entityType.equals(ItxLsThingLsThing.getLsType())
    			&& entityKind.equals(ItxLsThingLsThing.getLsKind())){
    		Set<ItxLsThingLsThingState> lsStates = ItxLsThingLsThing.getLsStates();
    		for (ItxLsThingLsThingState lsState : lsStates){
    			if (stateType.equals(lsState.getLsType()) && stateKind.equals(lsState.getLsKind())){
    				Set<ItxLsThingLsThingValue> lsValues = lsState.getLsValues();
    				for (ItxLsThingLsThingValue lsValue : lsValues){
    					if (valueType.equals(lsValue.getLsType()) && valueKind.equals(lsValue.getLsKind())){
    						if (valueType.equals("stringValue")) return lsValue.getNumericValue();
    					}
    				}
    			}
    		}
    	}
    	return null;
    }
    
    
	
}


