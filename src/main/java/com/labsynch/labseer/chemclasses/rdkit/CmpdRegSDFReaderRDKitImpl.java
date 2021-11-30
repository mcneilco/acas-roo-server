package com.labsynch.labseer.chemclasses.rdkit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.chemclasses.CmpdRegSDFReader;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;

public class CmpdRegSDFReaderRDKitImpl implements CmpdRegSDFReader {

    private Scanner scanner;

    private BBChemStructureService bbChemStructureService;

    public CmpdRegSDFReaderRDKitImpl(String fileName, BBChemStructureService bbChemStructureService) {
        this.bbChemStructureService = bbChemStructureService;
        Scanner scanner;
		try {
			scanner = new Scanner(new File(fileName));
            // \\R was added in java 8 and it matches any combination of line endings \r, \n, \r\n, \n\r
            scanner.useDelimiter("\\$\\$\\$\\$\\R");
            this.scanner = scanner;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    public void close() throws IOException {
        this.scanner.close();

    }

    @Override
    public CmpdRegMolecule readNextMol() throws IOException, CmpdRegMolFormatException {
        if (this.scanner.hasNext()) {
            CmpdRegMoleculeRDKitImpl molecule = new CmpdRegMoleculeRDKitImpl(scanner.next()+"$$$$", this.bbChemStructureService);
            return molecule;
        } else {
            return null;
        }
    }

}
