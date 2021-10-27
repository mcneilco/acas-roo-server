package com.labsynch.labseer.chemclasses.rdkit;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.labsynch.labseer.chemclasses.CmpdRegSDFReader;
import com.labsynch.labseer.chemclasses.CmpdRegSDFReaderFactory;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;

import org.springframework.stereotype.Component;

@Component
public class CmpdRegSDFReaderFactoryRDKitImpl implements CmpdRegSDFReaderFactory{

    @Override
    public CmpdRegSDFReader getCmpdRegSDFReader(String fileName)
            throws FileNotFoundException, CmpdRegMolFormatException, IOException {
        return new CmpdRegSDFReaderRDKitImpl(fileName);

    }


}
