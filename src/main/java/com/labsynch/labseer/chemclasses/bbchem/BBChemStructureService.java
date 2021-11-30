package com.labsynch.labseer.chemclasses.bbchem;
import java.io.IOException;
import java.util.List;

import com.labsynch.labseer.domain.BBChemStructure;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;

import org.RDKit.ROMol;
import org.RDKit.RWMol;
import org.codehaus.jackson.JsonNode;
import org.springframework.stereotype.Service;

@Service
public interface BBChemStructureService {

    public JsonNode getPreprocessorSettings() throws IOException;

    public void populateDescriptors(BBChemStructure structure);

    public BBChemStructure getProcessedStructure(String molfile) throws CmpdRegMolFormatException;
    
    public String getSDF(BBChemStructure structure) throws IOException;

    public List<BBChemStructure> parseSDF(String molfile) throws CmpdRegMolFormatException;
    
    public RWMol getPartiallySanitizedRWMol(String molfile);

    public String getMolStructureFromRDKMol(ROMol rdkMol);

}
