package com.labsynch.labseer.service;

import java.io.IOException;
import java.util.List;

import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.exceptions.StandardizerException;
import com.labsynch.labseer.domain.StandardizationHistory;
import com.labsynch.labseer.domain.StandardizationSettings;

public interface StandardizationService {

	void reset();

	int populateStanardizationDryRunTable() throws CmpdRegMolFormatException, IOException, StandardizerException;

	int dupeCheckStandardizationStructures() throws CmpdRegMolFormatException;

	String getStandardizationDryRunReport() throws IOException, CmpdRegMolFormatException, StandardizerException;

	String getDryRunStats();

	String executeStandardization(String username, String reason) throws IOException, CmpdRegMolFormatException, StandardizerException;

	int restandardizeParentStructures(List<Long> parentIds) throws CmpdRegMolFormatException, IOException, StandardizerException;

	String standardizeSingleMol(String mol) throws CmpdRegMolFormatException, StandardizerException, IOException;

	StandardizationSettings getStandardizationSettings();

	List<StandardizationHistory> getStanardizationHistory();

	void executeDryRun() throws CmpdRegMolFormatException, IOException, StandardizerException;

}