package com.labsynch.labseer.domain;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@RooJson
public class LsInteraction extends AbstractThing {

    @NotNull
    @Column(name = "first_thing_id")
    private Long firstThing;

    @NotNull
    @Column(name = "second_thing_id")
    private Long secondThing;
}
