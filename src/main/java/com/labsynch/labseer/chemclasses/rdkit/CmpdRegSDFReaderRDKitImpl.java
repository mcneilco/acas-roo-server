package com.labsynch.labseer.chemclasses.rdkit;

import java.io.IOException;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.chemclasses.CmpdRegSDFReader;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;

import org.RDKit.SDMolSupplier;

public class CmpdRegSDFReaderRDKitImpl implements CmpdRegSDFReader {

    private SDMolSupplier molSupplier;

    public CmpdRegSDFReaderRDKitImpl(String fileName) {
        this.molSupplier = new SDMolSupplier(fileName);
    }

    @Override
    public void close() throws IOException {
        this.molSupplier.close();

    }

    @Override
    public CmpdRegMolecule readNextMol() throws IOException, CmpdRegMolFormatException {
        if (!this.molSupplier.atEnd()) {
            CmpdRegMoleculeRDKitImpl molecule = new CmpdRegMoleculeRDKitImpl(molSupplier.next());
            return molecule;
        } else {
            return null;
        }
    }

}
