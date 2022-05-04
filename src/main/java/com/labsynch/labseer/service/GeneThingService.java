package com.labsynch.labseer.service;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.dto.GeneOrthologDTO;

import org.springframework.stereotype.Service;
import org.supercsv.cellprocessor.ift.CellProcessor;

@Service
public interface GeneThingService {

	void RegisterGeneThingsFromCSV(String inputFile) throws IOException;

	void RegisterDiscontinuedGenesFromCSV(String inputFile) throws IOException;

	void setGeneDefaults();

	CellProcessor[] getProcessors();

	String getGeneCodeName();

	void updateEntrezGenes(String entrezGenesFile, String geneHistoryFile, String taxonomyId) throws IOException;

	void registerGeneOrthologsFromCSV(String inputFile, String ortCodeName, String recordedBy, Long lsTransactionId)
			throws IOException;

	LsThing saveOrthologEntity(String versionName, String testFileName, String orthologType, Long curationLevel,
			String description, String curator, String recordedBy);

	List<GeneOrthologDTO> getOrthologsFromGeneID(String queryGeneID) throws IOException;

	GeneOrthologDTO saveOrthologInteraction(GeneOrthologDTO geneOrtholog);

	Collection<GeneOrthologDTO> saveOrthologInteractions(Collection<GeneOrthologDTO> geneOrthologs);

	// void fixDiscontinuedEntrezGeneIDs(String geneHistoryFile, String taxonomyId)
	// throws IOException;

}
