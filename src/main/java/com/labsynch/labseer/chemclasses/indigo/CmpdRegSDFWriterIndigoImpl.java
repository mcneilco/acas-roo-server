package com.labsynch.labseer.chemclasses.indigo;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.indigo.Indigo;
import com.epam.indigo.IndigoObject;
import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.chemclasses.CmpdRegSDFWriter;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;

public class CmpdRegSDFWriterIndigoImpl implements CmpdRegSDFWriter {

	Logger logger = LoggerFactory.getLogger(CmpdRegSDFWriterIndigoImpl.class);

	private Indigo indigo = new Indigo();

	private IndigoObject writer;

	public CmpdRegSDFWriterIndigoImpl(String fileName) {
		this.writer = indigo.writeFile(fileName);
	}

	public CmpdRegSDFWriterIndigoImpl() {
		this.writer = indigo.writeBuffer();
	}

	@Override
	public boolean writeMol(CmpdRegMolecule molecule) throws CmpdRegMolFormatException, IOException {
		CmpdRegMoleculeIndigoImpl molWrapper = (CmpdRegMoleculeIndigoImpl) molecule;
		String molStructure = molWrapper.molecule.molfile();
		try {
			IndigoObject mol = indigo.loadMolecule(molStructure);
			for (IndigoObject prop : molWrapper.molecule.iterateProperties()) {
				mol.setProperty(prop.name(), prop.rawData());
			}
			this.writer.sdfAppend(mol);
		} catch (Exception e) {
			logger.error("Unable to write mol", e);
			return false;
		}
		return true;
	}

	@Override
	public void close() throws IOException {
		writer.close();
	}

	@Override
	public String getBufferString() {
		return this.writer.toString();
	}

}
