package com.labsynch.labseer.service;

import java.io.FileNotFoundException;
import java.util.Collection;

import com.labsynch.labseer.dto.ExportResultDTO;
import com.labsynch.labseer.dto.SearchResultExportRequestDTO;

public interface ExportService {

	ExportResultDTO exportSearchResults(SearchResultExportRequestDTO searchResultExportRequestDTO)
			throws FileNotFoundException;

	ExportResultDTO exportLots(String filePath, Collection<String> lotCorpNames) throws FileNotFoundException;

}
