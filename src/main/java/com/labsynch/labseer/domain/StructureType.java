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
@RooJpaActiveRecord(sequenceName = "STATE_TYPE_PKSEQ", finders = { "findStructureTypesByTypeNameEquals" })
@RooJson
public class StructureType {

    private static final Logger logger = LoggerFactory.getLogger(StructureType.class);

    @NotNull
    @Column(unique = true)
    @Size(max = 64)
    private String typeName;

    public static com.labsynch.labseer.domain.StructureType getOrCreate(String name) {
        StructureType lsType = null;
        List<StructureType> lsTypes = StructureType.findStructureTypesByTypeNameEquals(name).getResultList();
        if (lsTypes.size() == 0) {
            lsType = new StructureType();
            lsType.setTypeName(name);
            lsType.persist();
        } else if (lsTypes.size() == 1) {
            lsType = lsTypes.get(0);
        } else if (lsTypes.size() > 1) {
            logger.error("ERROR: multiple Structure types with the same name");
        }
        return lsType;
    }

	@Id
    @SequenceGenerator(name = "structureTypeGen", sequenceName = "STRUCTURE_TYPE_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "structureTypeGen")
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
