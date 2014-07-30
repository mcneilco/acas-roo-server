package com.labsynch.labseer.service;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.dto.TempThingDTO;

@Service
public interface AnalysisGroupService {

	AnalysisGroup saveLsAnalysisGroup(AnalysisGroup analysisGroup);

	AnalysisGroup updateLsAnalysisGroup(AnalysisGroup analysisGroup);

	HashMap<String, TempThingDTO> createAnalysisGroupsFromCSV(String absoluteFilePath) throws IOException;

	boolean saveLsAnalysisGroupFromCsv(String analysisGroupFilePath,
			String treatmentGroupFilePath, String subjectFilePath);
}
