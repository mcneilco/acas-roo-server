package com.labsynch.labseer.chemclasses.indigo;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.stereotype.Component;

import com.labsynch.labseer.chemclasses.CmpdRegSDFReader;
import com.labsynch.labseer.chemclasses.CmpdRegSDFReaderFactory;
import com.labsynch.labseer.chemclasses.indigo.CmpdRegSDFReaderIndigoImpl;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;

@Component
public class CmpdRegSDFReaderFactoryIndigoImpl implements CmpdRegSDFReaderFactory {

	public CmpdRegSDFReader getCmpdRegSDFReader(String fileName)
			throws FileNotFoundException, CmpdRegMolFormatException, IOException {
		return new CmpdRegSDFReaderIndigoImpl(fileName);
	}

}
