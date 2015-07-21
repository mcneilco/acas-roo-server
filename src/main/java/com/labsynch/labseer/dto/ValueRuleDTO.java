package com.labsynch.labseer.dto;

import java.math.BigDecimal;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.ItxLsThingLsThing;
import com.labsynch.labseer.domain.LsThing;


@RooJavaBean
@RooToString
@RooJson
public class ValueRuleDTO {
	
    public ValueRuleDTO() {

    }

    
	private String comparisonMethod;
//	"string exact match" or "numeric exact match";
	
	private BigDecimal comparisonRange;
	
	private ValuePathDTO value;
	
	public boolean matchItxLsThingLsThings(ItxLsThingLsThing itxOne, ItxLsThingLsThing itxTwo){
		if (this.comparisonMethod.equalsIgnoreCase("string exact match")){
			String firstValue = value.extractStringValue(itxOne);
			String secondValue = value.extractStringValue(itxTwo);
			if (firstValue != null && secondValue != null && firstValue.equals(secondValue)) return true;
		}
		if (this.comparisonMethod.equalsIgnoreCase("numeric exact match")){
			BigDecimal firstValue = value.extractNumericValue(itxOne);
			BigDecimal secondValue = value.extractNumericValue(itxTwo);
			if (firstValue != null && secondValue != null && firstValue.equals(secondValue)) return true;
		}
		return false;
	}
	
	public boolean matchLsThings(LsThing thingOne, LsThing thingTwo){
		if (this.comparisonMethod.equalsIgnoreCase("string exact match")){
			String firstValue = value.extractStringValue(thingOne);
			String secondValue = value.extractStringValue(thingTwo);
			if (firstValue != null && secondValue != null && firstValue.equals(secondValue)) return true;
		}
		if (this.comparisonMethod.equalsIgnoreCase("numeric exact match")){
			BigDecimal firstValue = value.extractNumericValue(thingOne);
			BigDecimal secondValue = value.extractNumericValue(thingTwo);
			if (firstValue != null && secondValue != null && firstValue.equals(secondValue)) return true;
		}
		return false;
	}
	
}


