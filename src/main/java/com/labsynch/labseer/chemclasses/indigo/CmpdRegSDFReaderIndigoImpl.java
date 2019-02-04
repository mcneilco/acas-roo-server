package com.labsynch.labseer.chemclasses.indigo;

import java.io.IOException;

import com.epam.indigo.Indigo;
import com.epam.indigo.IndigoObject;
import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.chemclasses.CmpdRegSDFReader;

public class CmpdRegSDFReaderIndigoImpl implements CmpdRegSDFReader {
	
	private Indigo indigo = new Indigo();
	
	private IndigoObject reader;
	
	public CmpdRegSDFReaderIndigoImpl(String fileName) {
		this.reader = indigo.iterateSDFile(fileName);
	}

	@Override
	public void close() throws IOException {
		reader.close();
	}

	@Override
	public CmpdRegMolecule readNextMol() throws IOException {
		if (reader.hasNext()) {
			CmpdRegMoleculeIndigoImpl molecule = new CmpdRegMoleculeIndigoImpl(reader.next());
			return molecule;
		}else {
			return null;
		}
	}

}
