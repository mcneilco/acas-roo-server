package com.labsynch.labseer.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.TreatmentGroup;
import com.labsynch.labseer.dto.TempThingDTO;

import org.springframework.stereotype.Service;

@Service
public interface TreatmentGroupService {

	TreatmentGroup updateTreatmentGroup(TreatmentGroup treatmentGroup);

	void saveLsTreatmentGroups(AnalysisGroup newAnalysisGroup, Set<TreatmentGroup> treatmentGroups, Date recordedDate);

	TreatmentGroup saveLsTreatmentGroup(TreatmentGroup treatmentGroup);

	TreatmentGroup saveLsTreatmentGroup(Set<AnalysisGroup> analysisGroups, TreatmentGroup treatmentGroup, Date recordedDate);


	HashMap<String, TempThingDTO> createLsTreatmentGroupsFromCSV(
			HashMap<String, TempThingDTO> analysisGroupMap,
			String treatmentGroupCSV, String subjectCSV) throws IOException;

	HashMap<String, TempThingDTO> createTreatmentGroupsFromCSV(
			String treatmentGroupFilePath, HashMap<String, TempThingDTO> output) throws IOException;
	
}
