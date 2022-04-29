package com.labsynch.labseer.chemclasses.jchem;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.stereotype.Component;

import com.labsynch.labseer.chemclasses.CmpdRegSDFReader;
import com.labsynch.labseer.chemclasses.CmpdRegSDFReaderFactory;
import com.labsynch.labseer.chemclasses.jchem.CmpdRegSDFReaderJChemImpl;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;

@Component
public class CmpdRegSDFReaderFactoryJChemImpl implements CmpdRegSDFReaderFactory {

	@Override
	public CmpdRegSDFReader getCmpdRegSDFReader(String fileName)
			throws FileNotFoundException, CmpdRegMolFormatException, IOException {
		return new CmpdRegSDFReaderJChemImpl(fileName);
	}

}
