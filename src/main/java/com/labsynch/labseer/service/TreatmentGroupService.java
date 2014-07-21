package com.labsynch.labseer.service;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.TreatmentGroup;
import com.labsynch.labseer.dto.TempThingDTO;

@Service
public interface TreatmentGroupService {

	TreatmentGroup updateTreatmentGroup(TreatmentGroup treatmentGroup);

	TreatmentGroup saveLsTreatmentGroup(TreatmentGroup treatmentGroup);

	HashMap<String, TempThingDTO> createLsTreatmentGroupsFromCSV(
			HashMap<String, TempThingDTO> analysisGroupMap,
			String treatmentGroupCSV, String subjectCSV);

	HashMap<String, TempThingDTO> createTreatmentGroupsFromCSV(
			String treatmentGroupFilePath, HashMap<String, TempThingDTO> output) throws IOException;
	
}
