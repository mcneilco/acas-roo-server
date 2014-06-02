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
public class ItxProtocolProtocolValue extends AbstractValue {
		
    @ManyToOne
    private ItxProtocolProtocolState lsState;
    
    public static ItxProtocolProtocolValue update(ItxProtocolProtocolValue object) {
    	ItxProtocolProtocolValue updatedObject = new JSONDeserializer<ItxProtocolProtocolValue>().use(null, ItxProtocolProtocolValue.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).deserializeInto(object.toJson(), 
        				ItxProtocolProtocolValue.findItxProtocolProtocolValue(object.getId()));
    	updatedObject.setModifiedDate(new Date());
    	updatedObject.merge();
        return updatedObject;
    }
    
    
    public static ItxProtocolProtocolValue fromJsonToItxProtocolProtocolValue(String json) {
        return new JSONDeserializer<ItxProtocolProtocolValue>().
        		use(null, ItxProtocolProtocolValue.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).
        		deserialize(json);
    }
    
    public static Collection<ItxProtocolProtocolValue> fromJsonArrayToItxProtocolProtocolValues(String json) {
        return new JSONDeserializer<List<ItxProtocolProtocolValue>>().
        		use(null, ArrayList.class).
        		use("values", ItxProtocolProtocolValue.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).
        		deserialize(json);
    }
    
    public static Collection<ItxProtocolProtocolValue> fromJsonArrayToItxProtocolProtocolValues(Reader json) {
        return new JSONDeserializer<List<ItxProtocolProtocolValue>>().
        		use(null, ArrayList.class).
        		use("values", ItxProtocolProtocolValue.class).
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
    public static String toJsonArray(Collection<ItxProtocolProtocolValue> collection) {
        return new JSONSerializer().exclude("*.class")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(collection);
    }

	@Transactional
    public static String toJsonArrayStub(Collection<ItxProtocolProtocolValue> collection) {
        return new JSONSerializer().exclude("*.class", "lsState")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(collection);
    }
}
