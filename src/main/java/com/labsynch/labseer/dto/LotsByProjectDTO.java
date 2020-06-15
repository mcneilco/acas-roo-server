package com.labsynch.labseer.dto;

import java.util.Date;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;


@RooJavaBean
@RooJson
public class LotsByProjectDTO {
	private Long id;
	private String lotCorpName;
	private Integer lotNumber;
	private Date registrationDate;
	private String parentCorpName;
	private String project;
	
	public LotsByProjectDTO(
		long id,
		String lotCorpName,
		int lotNumber,
		Date registrationDate,
		String parentCorpName,
		String project
			) {
		
				this.id = id;
				this.lotCorpName = lotCorpName;
				this.lotNumber = lotNumber;
				this.registrationDate = registrationDate;
				this.parentCorpName = parentCorpName;
				this.project = project;
	}

	public LotsByProjectDTO() {
		// Empty constructor
	}
}
