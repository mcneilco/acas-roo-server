package com.labsynch.labseer.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.exceptions.StandardizerException;

@Service
public interface LDStandardizerService {

	public String standardizeStructure(String molfile) throws CmpdRegMolFormatException, StandardizerException;

}