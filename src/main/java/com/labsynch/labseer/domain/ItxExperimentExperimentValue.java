package com.labsynch.labseer.domain;

import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.utils.CustomBigDecimalFactory;
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@RooJson
@Table(name="ITX_EXPT_EXPT_VALUE")
public class ItxExperimentExperimentValue extends AbstractValue {
		
    @ManyToOne
    private ItxExperimentExperimentState lsState;
    
    public static ItxExperimentExperimentValue update(ItxExperimentExperimentValue object) {
    	ItxExperimentExperimentValue updatedObject = new JSONDeserializer<ItxExperimentExperimentValue>().use(null, ItxExperimentExperimentValue.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).deserializeInto(object.toJson(), 
        				ItxExperimentExperimentValue.findItxExperimentExperimentValue(object.getId()));
    	updatedObject.setModifiedDate(new Date());
    	updatedObject.merge();
        return updatedObject;
    }
    
    
    public static ItxExperimentExperimentValue fromJsonToItxExperimentExperimentValue(String json) {
        return new JSONDeserializer<ItxExperimentExperimentValue>().
        		use(null, ItxExperimentExperimentValue.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).
        		deserialize(json);
    }
    
    public static Collection<ItxExperimentExperimentValue> fromJsonArrayToItxExperimentExperimentValues(String json) {
        return new JSONDeserializer<List<ItxExperimentExperimentValue>>().
        		use(null, ArrayList.class).
        		use("values", ItxExperimentExperimentValue.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).
        		deserialize(json);
    }
    
    public static Collection<ItxExperimentExperimentValue> fromJsonArrayToItxExperimentExperimentValues(Reader json) {
        return new JSONDeserializer<List<ItxExperimentExperimentValue>>().
        		use(null, ArrayList.class).
        		use("values", ItxExperimentExperimentValue.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).
        		deserialize(json);
    }
    
	@Transactional
    public String toJson() {
        return new JSONSerializer().exclude("*.class")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(this);
    }
    
	@Transactional
    public static String toJsonArray(Collection<ItxExperimentExperimentValue> collection) {
        return new JSONSerializer().exclude("*.class")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(collection);
    }

	@Transactional
    public static String toJsonArrayStub(Collection<ItxExperimentExperimentValue> collection) {
        return new JSONSerializer().exclude("*.class", "lsState")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(collection);
    }
	
	public static ItxExperimentExperimentValue create(ItxExperimentExperimentValue lsThingValue) {
        ItxExperimentExperimentValue newItxExperimentExperimentValue = new JSONDeserializer<ItxExperimentExperimentValue>().use(null, ItxExperimentExperimentValue.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserializeInto(lsThingValue.toJson(), new ItxExperimentExperimentValue());
        return newItxExperimentExperimentValue;
    }
}
