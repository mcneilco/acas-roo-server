package com.labsynch.labseer.service;

import java.io.IOException;
import java.util.List;

import com.labsynch.labseer.domain.StandardizationHistory;
import com.labsynch.labseer.dto.StandardizationDryRunSearchDTO;
import com.labsynch.labseer.dto.StandardizationSettingsConfigCheckResponseDTO;
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

	public int recalculateLotMolWeights(List<Long> parentIds);

	int restandardizeParentsOfStandardizationDryRunCompounds()
			throws CmpdRegMolFormatException, IOException, StandardizerException;

	String standardizeSingleMol(String mol) throws CmpdRegMolFormatException, StandardizerException, IOException;

	List<StandardizationHistory> getStandardizationHistory();

	void executeDryRun() throws CmpdRegMolFormatException, IOException, StandardizerException;

	StandardizationSettingsConfigCheckResponseDTO checkStandardizationState() throws StandardizerException;

	void failRunnningStandardization();

}