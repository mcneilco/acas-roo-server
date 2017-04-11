package com.labsynch.labseer.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(sequenceName = "CRON_JOB_PKSEQ", finders={"findCronJobsByCodeNameEquals"})
public class CronJob {

	private static final Logger logger = LoggerFactory.getLogger(CronJob.class);

    @NotNull
    String schedule;
    
    @NotNull
    String scriptType;
    
    @NotNull
    String scriptFile;
    
    String functionName;
    
    @Column(columnDefinition="text")
    String scriptJSONData;
    
    @NotNull
    boolean active;
    
    @NotNull
    boolean ignored;
    
    @NotNull
    String runUser;
    
    @NotNull
    @Column(unique=true)
    String codeName;
    
    Date lastStartTime;
    
    Long lastDuration;
    
    @Column(columnDefinition="text")
    String lastResultJSON;
    
    Integer numberOfExecutions;
    
    public static CronJob update(CronJob cronJob) {
        CronJob updatedCronJob = CronJob.findCronJob(cronJob.getId());
        updatedCronJob.setSchedule(cronJob.getSchedule());
        updatedCronJob.setScriptType(cronJob.getScriptType());
        updatedCronJob.setScriptFile(cronJob.getScriptFile());
        updatedCronJob.setFunctionName(cronJob.getFunctionName());
        updatedCronJob.setScriptJSONData(cronJob.getScriptJSONData());
        updatedCronJob.setActive(cronJob.isActive());
        updatedCronJob.setIgnored(cronJob.isIgnored());
        updatedCronJob.setRunUser(cronJob.getRunUser());
        updatedCronJob.setCodeName(cronJob.getCodeName());
        updatedCronJob.setLastStartTime(cronJob.getLastStartTime());
        updatedCronJob.setLastDuration(cronJob.getLastDuration());
        updatedCronJob.setLastResultJSON(cronJob.getLastResultJSON());
        updatedCronJob.setNumberOfExecutions(cronJob.getNumberOfExecutions());
        updatedCronJob.merge();
        updatedCronJob.setVersion(CronJob.findCronJob(cronJob.getId()).getVersion());
        return updatedCronJob;
    }
    

	@Id
    @SequenceGenerator(name = "cronJobGen", sequenceName = "CRON_JOB_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "cronJobGen")
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
