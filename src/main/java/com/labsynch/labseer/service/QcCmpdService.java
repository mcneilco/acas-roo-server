package com.labsynch.labseer.service;

import java.io.IOException;

import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;



public interface QcCmpdService {

	int qcCheckParentStructures() throws CmpdRegMolFormatException, IOException;

	int dupeCheckQCStructures() throws CmpdRegMolFormatException;

	void exportQCReport(String csvFilePathName, String exportType) throws IOException, CmpdRegMolFormatException;


}
