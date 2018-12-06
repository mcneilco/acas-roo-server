package com.labsynch.labseer.chemclasses;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;

public interface CmpdRegSDFReaderFactory {
	
	public CmpdRegSDFReader getCmpdRegSDFReader(String fileName) throws FileNotFoundException, CmpdRegMolFormatException, IOException;

}
