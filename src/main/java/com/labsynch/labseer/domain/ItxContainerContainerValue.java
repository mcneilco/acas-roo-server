package com.labsynch.labseer.domain;

import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.ManyToOne;

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
public class ItxContainerContainerValue extends AbstractValue {
		
    @ManyToOne
    private ItxContainerContainerState lsState;
    
    public static ItxContainerContainerValue update(ItxContainerContainerValue object) {
    	ItxContainerContainerValue updatedObject = new JSONDeserializer<ItxContainerContainerValue>().use(null, ItxContainerContainerValue.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).deserializeInto(object.toJson(), 
        				ItxContainerContainerValue.findItxContainerContainerValue(object.getId()));
    	updatedObject.setModifiedDate(new Date());
    	updatedObject.merge();
        return updatedObject;
    }
    
    
    public static ItxContainerContainerValue fromJsonToItxContainerContainerValue(String json) {
        return new JSONDeserializer<ItxContainerContainerValue>().
        		use(null, ItxContainerContainerValue.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).
        		deserialize(json);
    }
    
    public static Collection<ItxContainerContainerValue> fromJsonArrayToItxContainerContainerValues(String json) {
        return new JSONDeserializer<List<ItxContainerContainerValue>>().
        		use(null, ArrayList.class).
        		use("values", ItxContainerContainerValue.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).
        		deserialize(json);
    }
    
    public static Collection<ItxContainerContainerValue> fromJsonArrayToItxContainerContainerValues(Reader json) {
        return new JSONDeserializer<List<ItxContainerContainerValue>>().
        		use(null, ArrayList.class).
        		use("values", ItxContainerContainerValue.class).
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
    public static String toJsonArray(Collection<ItxContainerContainerValue> collection) {
        return new JSONSerializer().exclude("*.class")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(collection);
    }

	@Transactional
    public static String toJsonArrayStub(Collection<ItxContainerContainerValue> collection) {
        return new JSONSerializer().exclude("*.class", "lsState")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(collection);
    }
	
	
	public static ItxContainerContainerValue create(ItxContainerContainerValue lsThingValue) {
        ItxContainerContainerValue newItxContainerContainerValue = new JSONDeserializer<ItxContainerContainerValue>().use(null, ItxContainerContainerValue.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserializeInto(lsThingValue.toJson(), new ItxContainerContainerValue());
        return newItxContainerContainerValue;
    }
}
