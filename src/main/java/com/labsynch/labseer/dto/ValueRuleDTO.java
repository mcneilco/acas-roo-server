package com.labsynch.labseer.dto;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.ItxLsThingLsThing;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.service.LsThingServiceImpl;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


@RooJavaBean
@RooToString
@RooJson
public class ValueRuleDTO {
	
    public ValueRuleDTO() {

    }

	private static final Logger logger = LoggerFactory.getLogger(ValueRuleDTO.class);
    
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
			if (firstValue != null && secondValue != null && firstValue.compareTo(secondValue) == 0) return true;
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
			logger.debug("Comparing "+firstValue.toString() + " to "+secondValue.toString());
			if (firstValue != null && secondValue != null && firstValue.compareTo(secondValue) == 0) return true;
		}
		return false;
	}
	
	public boolean checkRuleApplies(ItxLsThingLsThing itx){
		if (this.comparisonMethod.equalsIgnoreCase("string exact match")){
			String stringValue = value.extractStringValue(itx);
			if (stringValue != null) return true;
		}
		if (this.comparisonMethod.equalsIgnoreCase("numeric exact match")){
			BigDecimal numericValue = value.extractNumericValue(itx);
			if (numericValue != null) return true;
		}
		return false;
	}
	

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

	public static ValueRuleDTO fromJsonToValueRuleDTO(String json) {
        return new JSONDeserializer<ValueRuleDTO>()
        .use(null, ValueRuleDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<ValueRuleDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<ValueRuleDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<ValueRuleDTO> fromJsonArrayToValueRuleDTO(String json) {
        return new JSONDeserializer<List<ValueRuleDTO>>()
        .use("values", ValueRuleDTO.class).deserialize(json);
    }

	public String getComparisonMethod() {
        return this.comparisonMethod;
    }

	public void setComparisonMethod(String comparisonMethod) {
        this.comparisonMethod = comparisonMethod;
    }

	public BigDecimal getComparisonRange() {
        return this.comparisonRange;
    }

	public void setComparisonRange(BigDecimal comparisonRange) {
        this.comparisonRange = comparisonRange;
    }

	public ValuePathDTO getValue() {
        return this.value;
    }

	public void setValue(ValuePathDTO value) {
        this.value = value;
    }
}


