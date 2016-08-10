package com.labsynch.labseer.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@Entity
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

	@Id
    @SequenceGenerator(name = "thingTypeGen", sequenceName = "THING_TYPE_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "thingTypeGen")
    @Column(name = "id")
    private Long id;

	@Version
    @Column(name = "version")
    private Integer version;

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public Integer getVersion() {
        return this.version;
    }

	public void setVersion(Integer version) {
        this.version = version;
    }
}
