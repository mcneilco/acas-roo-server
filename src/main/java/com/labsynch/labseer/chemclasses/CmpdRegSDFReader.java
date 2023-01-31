package com.labsynch.labseer.chemclasses;

import java.io.IOException;
import java.util.Collection;

import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;

public interface CmpdRegSDFReader {

	public void close() throws IOException;

	public CmpdRegMolecule readNextMol() throws IOException, CmpdRegMolFormatException;

	public Collection<CmpdRegMolecule> readNextMols(int nMols) throws IOException, CmpdRegMolFormatException;

}
