package com.labsynch.labseer.chemclasses;

import java.io.IOException;

import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;

public interface CmpdRegSDFWriter {

	public boolean writeMol(CmpdRegMolecule mol) throws CmpdRegMolFormatException, IOException;

	public void close() throws IOException;

	public String getBufferString();

}
