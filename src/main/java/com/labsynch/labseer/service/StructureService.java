package com.labsynch.labseer.service;

import java.io.IOException;
import java.util.Collection;

import org.openscience.cdk.exception.CDKException;
import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.Structure;
import com.labsynch.labseer.dto.MolPropertiesDTO;

@Service
public interface StructureService {

	byte[] renderMolStructure(String molStructure, Integer hSize, Integer wSize, String format) throws IOException, CDKException;

	MolPropertiesDTO calculateMoleculeProperties(String molStructure) throws IOException, CDKException;

	Structure saveStructure(Structure structure) throws IOException, CDKException;

	Structure updateStructure(Structure structure);

	byte[] renderStructureByCodeName(String codeName, Integer height,
			Integer width, String format) throws IOException, CDKException;

	Collection<Structure> searchStructures(String queryMol, String searchType,
			Integer maxResults, Float similarity);

	Collection<String> searchStructuresCodes(String queryMol, String searchType, Integer maxResults, Float similarity);

	Collection<Structure> searchStructuresByTypeKind(String queryMol, String lsType, String lsKind, String searchType,
			Integer maxResults, Float similarity);

	String renderMolStructureBase64(String molStructure, Integer hSize, Integer wSize, String format)
			throws IOException, CDKException;

}
