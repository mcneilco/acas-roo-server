package com.labsynch.labseer.chemclasses.jchem;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.chemclasses.CmpdRegSDFReader;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;

import chemaxon.formats.MolFormatException;
import chemaxon.formats.MolImporter;
import chemaxon.struc.Molecule;

public class CmpdRegSDFReaderJChemImpl implements CmpdRegSDFReader {

	private MolImporter molImporter;

	public CmpdRegSDFReaderJChemImpl(String fileName)
			throws CmpdRegMolFormatException, FileNotFoundException, IOException {
		FileInputStream fis;
		fis = new FileInputStream(fileName);
		try {
			this.molImporter = new MolImporter(fis);
		} catch (MolFormatException e) {
			throw new CmpdRegMolFormatException(e);
		}
	};

	@Override
	public void close() throws IOException {
		this.molImporter.close();
	}

	@Override
	public CmpdRegMolecule readNextMol() throws IOException, CmpdRegMolFormatException {
		Molecule mol = this.molImporter.read();
		if (mol == null)
			return null;
		CmpdRegMoleculeJChemImpl molecule = new CmpdRegMoleculeJChemImpl(mol);
		return molecule;
	}

	@Override
	public Collection<CmpdRegMolecule> readNextMols(int nMols) throws IOException, CmpdRegMolFormatException {
		int molsRead = 0;
		Collection<CmpdRegMolecule> cmpdRegMols = new ArrayList<CmpdRegMolecule>();
		// read nMols MOL strings from the SDF
		while (molsRead < nMols){
			Molecule mol = this.molImporter.read();
			if(mol != null) {
				CmpdRegMoleculeJChemImpl molecule = new CmpdRegMoleculeJChemImpl(mol);
				cmpdRegMols.add(molecule);
				molsRead++;
			} else {
				break;
			}
		}
		return cmpdRegMols;
		}
}
