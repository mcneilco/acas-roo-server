package com.labsynch.labseer.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.supercsv.cellprocessor.ift.CellProcessor;

import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.LsTransaction;
import com.labsynch.labseer.dto.EntrezDbGeneDTO;
import com.labsynch.labseer.dto.EntrezDiscontinuedGeneDTO;

@Service
public interface GeneThingService {

	void RegisterGeneThingsFromCSV(String inputFile) throws IOException;

	void RegisterDiscontinuedGenesFromCSV(String inputFile) throws IOException;

	void setGeneDefaults();

	CellProcessor[] getProcessors();

	LsThing registerUpdatedGene(EntrezDbGeneDTO geneDTO, EntrezDiscontinuedGeneDTO entrezDiscontinuedGeneDTO,  LsTransaction lsTransaction);

	String getGeneCodeName();

	LsThing registerNewGene(EntrezDbGeneDTO geneDTO,
			EntrezDiscontinuedGeneDTO entrezDiscontinuedGeneDTO,
			LsTransaction lsTransaction);

	LsThing registerDiscontinuedGene(EntrezDiscontinuedGeneDTO geneDTO,
			LsTransaction lsTransaction);

	void updateEntrezGenes(String entrezGenesFile, String geneHistoryFile,
			String taxonomyId, LsTransaction lsTransaction) throws IOException;

	void updateEntrezGenes(String entrezGenesFile, String geneHistoryFile,
			String taxonomyId) throws IOException;


}
