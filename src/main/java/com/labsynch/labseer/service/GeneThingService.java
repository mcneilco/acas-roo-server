package com.labsynch.labseer.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

@Service
public interface GeneThingService {

	void RegisterGeneThingsFromCSV(String inputFile) throws IOException;


}
