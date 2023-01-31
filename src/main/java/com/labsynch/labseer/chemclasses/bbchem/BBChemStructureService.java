package com.labsynch.labseer.chemclasses.bbchem;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.domain.AbstractBBChemStructure;
import com.labsynch.labseer.dto.StandardizationSettingsConfigCheckResponseDTO;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;

import org.springframework.stereotype.Service;

@Service
public interface BBChemStructureService {

    public JsonNode getPreprocessorSettings() throws IOException;

    public JsonNode postToProcessService(String molfile) throws IOException;

    public BBChemParentStructure getProcessedStructure(String molfile, Boolean includeFingerprints)
            throws CmpdRegMolFormatException;

    public HashMap<String, BBChemParentStructure> getProcessedStructures(HashMap<String, String> structures,
            Boolean includeFingerprints) throws CmpdRegMolFormatException;

    public String getSDF(BBChemParentStructure structure) throws IOException;

    public String getSDF(List<BBChemParentStructure> bbchemStructures) throws IOException;

    public List<BBChemParentStructure> parseSDF(String molfile) throws CmpdRegMolFormatException;

    public List<List<String>> getMolFragments(List<String> molfile) throws CmpdRegMolFormatException;

    public HashMap<? extends AbstractBBChemStructure, Boolean> substructureMatch(String queryMol,
            List<? extends AbstractBBChemStructure> needMatchMolFiles) throws CmpdRegMolFormatException;

    public byte[] callImageService(CmpdRegMolecule molecule, String imageFormat, String hSize, String wSize) throws IOException;


    public String convert(String structure, String inputFormat, String outputFormat)
			throws IOException, CmpdRegMolFormatException;

    public StandardizationSettingsConfigCheckResponseDTO configCheck(String oldConfig, String newConfig, String newTatutomerHash, String oldTatuomerHash, String oldPreprocessorVersion, String oldSchrodingerSuiteVersion) throws IOException;

    public StandardizationSettingsConfigCheckResponseDTO configFix(JsonNode jsonNode) throws IOException;

    public JsonNode health() throws IOException;

}
