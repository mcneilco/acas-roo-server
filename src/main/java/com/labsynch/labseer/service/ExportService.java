package com.labsynch.labseer.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.dto.ExportResultDTO;
import com.labsynch.labseer.dto.SearchResultExportRequestDTO;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;

public interface ExportService {

	ExportResultDTO exportSearchResults(SearchResultExportRequestDTO searchResultExportRequestDTO)
			throws FileNotFoundException;

	ExportResultDTO exportLots(String filePath, Collection<String> lotCorpNames) throws FileNotFoundException;

	File exportLots(List<String> lotCorpNames) throws IllegalArgumentException, IOException, CmpdRegMolFormatException;

}
