package com.labsynch.labseer.service;

import java.io.IOException;
import java.util.HashMap;

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.dto.TempThingDTO;
import com.labsynch.labseer.dto.TsvLoaderResponseDTO;
import com.labsynch.labseer.exceptions.NotFoundException;

import org.springframework.stereotype.Service;

@Service
public interface AnalysisGroupService {

	AnalysisGroup saveLsAnalysisGroup(AnalysisGroup analysisGroup) throws NotFoundException;

	AnalysisGroup updateLsAnalysisGroup(AnalysisGroup analysisGroup);

	HashMap<String, TempThingDTO> createAnalysisGroupsFromCSV(String absoluteFilePath) throws IOException;

	TsvLoaderResponseDTO saveLsAnalysisGroupFromCsv(String analysisGroupFilePath,
			String treatmentGroupFilePath, String subjectFilePath) throws IOException;
}
