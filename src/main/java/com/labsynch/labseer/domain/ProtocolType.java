package com.labsynch.labseer.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@Entity
@RooJavaBean
@RooToString
@RooJpaActiveRecord(sequenceName = "PROTOCOL_TYPE_PKSEQ", finders = { "findProtocolTypesByTypeNameEquals" })
@RooJson
public class ProtocolType {

    @NotNull
    @Column(unique = true)
    @Size(max = 128)
    private String typeName;

	@Id
    @SequenceGenerator(name = "protocolTypeGen", sequenceName = "PROTOCOL_TYPE_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "protocolTypeGen")
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
