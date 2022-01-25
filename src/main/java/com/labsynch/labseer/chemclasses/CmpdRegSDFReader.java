package com.labsynch.labseer.chemclasses;

import java.io.IOException;

import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;

public interface CmpdRegSDFReader {
	
	public void close() throws IOException;
	
	public CmpdRegMolecule readNextMol() throws IOException, CmpdRegMolFormatException;

}
