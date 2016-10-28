package com.labsynch.labseer.domain;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(sequenceName = "APPSETTING_PKSEQ", finders = { "findApplicationSettingsByPropNameEquals", "findApplicationSettingsByPropNameEqualsAndIgnoredNot" })
public class ApplicationSetting {

	//private static final Logger logger = LoggerFactory.getLogger(ApplicationSetting.class);

    @Size(max = 255)
    private String propName;

    @Size(max = 255)
    private String propValue;

    @Size(max = 512)
    private String comments;

    private boolean ignored;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "MM")
    private Date recordedDate;
    
//    @PostConstruct
//    public void init(){
//    	logger.info("calling the appSetting 201 init method upon startup");
//    	this.setPropName("testing201");
//    	this.setPropValue("testingValue201");
//    	this.persist();
//    }
}
