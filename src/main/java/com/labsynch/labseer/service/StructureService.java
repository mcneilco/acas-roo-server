package com.labsynch.labseer.service;

import java.io.IOException;
import java.util.Collection;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.ChemStructure;
import com.labsynch.labseer.dto.MolPropertiesDTO;
import com.labsynch.labseer.service.ChemStructureService.SearchType;

@Service
public interface StructureService {

	byte[] renderMolStructure(String molStructure, Integer hSize, Integer wSize, String format)
			throws IOException, CDKException;

	MolPropertiesDTO calculateMoleculeProperties(String molStructure) throws IOException, CDKException;

	ChemStructure saveStructure(ChemStructure structure) throws IOException, CDKException;

	ChemStructure updateStructure(ChemStructure structure);

	byte[] renderStructureByCodeName(String codeName, Integer height,
			Integer width, String format) throws IOException, CDKException;

	Collection<ChemStructure> searchStructures(String queryMol, SearchType searchType,
			Integer maxResults, Float similarity);

	Collection<String> searchStructuresCodes(String queryMol, SearchType searchType, Integer maxResults,
			Float similarity);

	Collection<ChemStructure> searchStructuresByTypeKind(String queryMol, String lsType, String lsKind,
			SearchType searchType,
			Integer maxResults, Float similarity);

	String renderMolStructureBase64(String molStructure, Integer hSize, Integer wSize, String format)
			throws IOException, CDKException;

	String convertSmilesToMol(String smiles) throws InvalidSmilesException, Exception;

	String cleanMolStructure(String molStructure) throws Exception;

}
