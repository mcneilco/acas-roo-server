package com.labsynch.labseer.chemclasses.rdkit;

import java.io.IOException;

import com.labsynch.labseer.chemclasses.CmpdRegSDFWriter;
import com.labsynch.labseer.chemclasses.CmpdRegSDFWriterFactory;

import org.springframework.stereotype.Component;

@Component
public class CmpdRegSDFWriterFactoryRDKitImpl implements CmpdRegSDFWriterFactory {

  @Override
  public CmpdRegSDFWriter getCmpdRegSDFWriter(String fileName) throws IllegalArgumentException, IOException {
    return new CmpdRegSDFWriterRDKitImpl(fileName);
  }

  @Override
  public CmpdRegSDFWriter getCmpdRegSDFBufferWriter() throws IllegalArgumentException, IOException {
    return new CmpdRegSDFWriterRDKitImpl();
  }

}
