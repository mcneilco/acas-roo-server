package com.labsynch.labseer.chemclasses.indigo;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.labsynch.labseer.chemclasses.CmpdRegSDFWriter;
import com.labsynch.labseer.chemclasses.CmpdRegSDFWriterFactory;
import com.labsynch.labseer.chemclasses.indigo.CmpdRegSDFWriterIndigoImpl;

@Component
public class CmpdRegSDFWriterFactoryIndigoImpl implements CmpdRegSDFWriterFactory {

	public CmpdRegSDFWriter getCmpdRegSDFWriter(String fileName) throws IllegalArgumentException, IOException {
		return new CmpdRegSDFWriterIndigoImpl(fileName);
	}

	@Override
	public CmpdRegSDFWriter getCmpdRegSDFBufferWriter() {
		return new CmpdRegSDFWriterIndigoImpl();
	}

}
