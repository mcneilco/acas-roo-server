package com.labsynch.labseer.service;

import java.util.List;

import javax.persistence.NonUniqueResultException;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.dto.AutoLabelDTO;

@Service
public interface AutoLabelService {


	List<AutoLabelDTO> getAutoLabels(String json) throws NonUniqueResultException;

	List<AutoLabelDTO> getAutoLabels(String thingTypeAndKind, String labelTypeAndKind, Long numberOfLabels) throws NonUniqueResultException;

	String getSubjectCodeName();

	String getTreatmentGroupCodeName();

	String getAnalysisGroupCodeName();

	String getExperimentCodeName();

	
	
	
}
