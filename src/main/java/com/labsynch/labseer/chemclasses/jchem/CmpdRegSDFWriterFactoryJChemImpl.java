package com.labsynch.labseer.chemclasses.jchem;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.labsynch.labseer.chemclasses.CmpdRegSDFWriter;
import com.labsynch.labseer.chemclasses.CmpdRegSDFWriterFactory;
import com.labsynch.labseer.chemclasses.jchem.CmpdRegSDFWriterJChemImpl;

@Component
public class CmpdRegSDFWriterFactoryJChemImpl implements CmpdRegSDFWriterFactory{
	
	@Override
	public CmpdRegSDFWriter getCmpdRegSDFWriter(String fileName) throws IllegalArgumentException, IOException {
		return new CmpdRegSDFWriterJChemImpl(fileName);
	}

	@Override
	public CmpdRegSDFWriter getCmpdRegSDFBufferWriter() throws IllegalArgumentException, IOException {
		return new CmpdRegSDFWriterJChemImpl();
	}

}
