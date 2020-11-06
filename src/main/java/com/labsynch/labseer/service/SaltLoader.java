package com.labsynch.labseer.service;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

public class SaltLoader {
	
    private String name;
	
    private String description;

    private long numberOfSalts;

    private long size;

    private Boolean uploaded;
    
    @Transient 
    private MultipartFile file; // added 

    private String fileName; // added 
	
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    private Date loadedDate;
}
