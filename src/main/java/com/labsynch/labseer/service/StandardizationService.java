package com.labsynch.labseer.service;

import java.io.IOException;
import java.util.List;

import com.labsynch.labseer.domain.StandardizationHistory;
import com.labsynch.labseer.domain.StandardizationSettings;
import com.labsynch.labseer.dto.StandardizationDryRunSearchDTO;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.exceptions.StandardizerException;

public interface StandardizationService {

	void reset();

	int populateStandardizationDryRunTable() throws CmpdRegMolFormatException, IOException, StandardizerException;

	int dupeCheckStandardizationStructures() throws CmpdRegMolFormatException;

	String getStandardizationDryRunReportFiles(String sdfFileName) throws IOException, CmpdRegMolFormatException;

	String getStandardizationDryRunReportFiles(StandardizationDryRunSearchDTO searchCriteria)
			throws IOException, CmpdRegMolFormatException;

	String getStandardizationDryRunReport() throws IOException, CmpdRegMolFormatException, StandardizerException;

	String getDryRunStats() throws StandardizerException;

	String executeStandardization(String username, String reason)
			throws IOException, CmpdRegMolFormatException, StandardizerException;

	public int restandardizeLots(List<Long> parentIds);

	int restandardizeParentStructures(List<Long> parentIds)
			throws CmpdRegMolFormatException, IOException, StandardizerException;

	String standardizeSingleMol(String mol) throws CmpdRegMolFormatException, StandardizerException, IOException;

	StandardizationSettings getStandardizationSettings();

	List<StandardizationHistory> getStandardizationHistory();

	void executeDryRun() throws CmpdRegMolFormatException, IOException, StandardizerException;

}