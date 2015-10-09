package com.labsynch.labseer.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

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
        return updatedCronJob;
    }
    
}
