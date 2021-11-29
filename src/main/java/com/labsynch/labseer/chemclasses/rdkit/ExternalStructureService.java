package com.labsynch.labseer.chemclasses.rdkit;
import java.io.IOException;
import java.util.List;

import com.labsynch.labseer.domain.RDKitStructure;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;

import org.RDKit.ROMol;
import org.RDKit.RWMol;
import org.codehaus.jackson.JsonNode;
import org.springframework.stereotype.Service;

@Service
public interface ExternalStructureService {

    public JsonNode getPreprocessorSettings() throws IOException;

    public void populateDescriptors(RDKitStructure rdKitStructure);

    public RDKitStructure getProcessedStructure(String molfile) throws CmpdRegMolFormatException;
    
    public String getSDFFromRDkitStructure(RDKitStructure rdkitStructure) throws IOException;

    public List<RDKitStructure> getRDKitStructuresFromSDFService(String molfile) throws CmpdRegMolFormatException;
    
    public RWMol getPartialiallySanitizedRWMol(String molfile);

    public String getMolStructureFromRDKMol(ROMol rdkMol);

}
