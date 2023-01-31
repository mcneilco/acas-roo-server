package com.labsynch.labseer.chemclasses.bbchem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.chemclasses.CmpdRegSDFReader;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;

public class CmpdRegSDFReaderBBChemImpl implements CmpdRegSDFReader {

    private Scanner scanner;

    private BBChemStructureService bbChemStructureService;

    public CmpdRegSDFReaderBBChemImpl(String fileName, BBChemStructureService bbChemStructureService) {
        this.bbChemStructureService = bbChemStructureService;
        Scanner scanner;
        try {
            scanner = new Scanner(new File(fileName));
            // \\R was added in java 8 and it matches any combination of line endings \r,
            // \n, \r\n, \n\r
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
            CmpdRegMoleculeBBChemImpl molecule = new CmpdRegMoleculeBBChemImpl(scanner.next() + "$$$$",
                    this.bbChemStructureService);
            return molecule;
        } else {
            return null;
        }
    }

    @Override
    public Collection<CmpdRegMolecule> readNextMols(int nMols) throws IOException, CmpdRegMolFormatException {
        int molsRead = 0;
        String sdfContents = "";
        // read nMols MOL strings from the SDF
        while (molsRead < nMols){
            if(this.scanner.hasNext()) {
                sdfContents += scanner.next();
                sdfContents += "$$$$\n";
                molsRead++;
            } else {
                break;
            }
        }
        Collection<CmpdRegMolecule> cmpdRegMols = new ArrayList<CmpdRegMolecule>();
        if (sdfContents.length() > 0){
            Collection<BBChemParentStructure> molecules = bbChemStructureService.parseSDF(sdfContents);
            for (BBChemParentStructure molecule : molecules) {
                CmpdRegMolecule cmpdRegMol = new CmpdRegMoleculeBBChemImpl(molecule, bbChemStructureService);
                cmpdRegMols.add(cmpdRegMol);
            }
        }
        
        return cmpdRegMols;
    }

}
