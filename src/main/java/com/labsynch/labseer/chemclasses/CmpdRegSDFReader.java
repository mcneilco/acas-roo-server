package com.labsynch.labseer.chemclasses;

import java.io.IOException;

public interface CmpdRegSDFReader {
	
	public void close() throws IOException;
	
	public CmpdRegMolecule readNextMol() throws IOException;

}
