package com.labsynch.labseer.chemclasses.indigo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import com.epam.indigo.Indigo;
import com.epam.indigo.IndigoObject;
import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.chemclasses.CmpdRegSDFReader;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;

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
	public CmpdRegMolecule readNextMol() throws IOException, CmpdRegMolFormatException {
		if (reader.hasNext()) {
			CmpdRegMoleculeIndigoImpl molecule = new CmpdRegMoleculeIndigoImpl(reader.next());
			return molecule;
		} else {
			return null;
		}
	}

	@Override
	public Collection<CmpdRegMolecule> readNextMols(int nMols) throws IOException, CmpdRegMolFormatException {
		int molsRead = 0;
		Collection<CmpdRegMolecule> cmpdRegMols = new ArrayList<CmpdRegMolecule>();
        // read nMols MOL strings from the SDF
        while (molsRead < nMols){
            if(this.reader.hasNext()) {
                CmpdRegMoleculeIndigoImpl molecule = new CmpdRegMoleculeIndigoImpl(reader.next());
				cmpdRegMols.add(molecule);
                molsRead++;
            } else {
                break;
            }
        }
        return cmpdRegMols;
	}

}
