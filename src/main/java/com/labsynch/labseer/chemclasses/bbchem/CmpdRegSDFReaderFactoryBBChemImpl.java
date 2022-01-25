package com.labsynch.labseer.chemclasses.bbchem;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.labsynch.labseer.chemclasses.CmpdRegSDFReader;
import com.labsynch.labseer.chemclasses.CmpdRegSDFReaderFactory;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CmpdRegSDFReaderFactoryBBChemImpl implements CmpdRegSDFReaderFactory {

    @Autowired
    private BBChemStructureService bbChemStructureService;

    @Override
    public CmpdRegSDFReader getCmpdRegSDFReader(String fileName)
            throws FileNotFoundException, CmpdRegMolFormatException, IOException {
        return new CmpdRegSDFReaderBBChemImpl(fileName, bbChemStructureService);

    }

}
