package com.labsynch.labseer.domain;

import java.util.List;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(sequenceName = "THING_TYPE_PKSEQ", finders = { "findThingTypesByTypeNameEquals" })
public class ThingType {

	private static final Logger logger = LoggerFactory.getLogger(ThingType.class);

	
	@NotNull
	@Column(unique = true)
	@Size(max = 128)
	private String typeName;

	public static ThingType getOrCreate(String name) {

		ThingType thingType = null;
		List<ThingType> thingTypes = ThingType.findThingTypesByTypeNameEquals(name).getResultList();
		if (thingTypes.size() == 0){
			thingType = new ThingType();
			thingType.setTypeName(name);
			thingType.persist();
		} else if (thingTypes.size() == 1){
			thingType = thingTypes.get(0);
		} else if (thingTypes.size() > 1){
			logger.error("ERROR: multiple thing types with the same name");
		}
		

		return thingType;
	}
}
