package com.labsynch.labseer.chemclasses.bbchem;
import java.io.IOException;
import java.util.List;
import java.net.HttpURLConnection;

import com.labsynch.labseer.domain.BBChemParentStructure;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;

import org.codehaus.jackson.JsonNode;
import org.springframework.stereotype.Service;

@Service
public interface BBChemStructureService {

    public JsonNode getPreprocessorSettings() throws IOException;

    public HttpURLConnection postToPreprocessorService(String molfile) throws IOException;

    public BBChemParentStructure getProcessedStructure(String molfile, Boolean includeFingerprint) throws CmpdRegMolFormatException;
    
    public String getSDF(BBChemParentStructure structure) throws IOException;

    public List<BBChemParentStructure> parseSDF(String molfile) throws CmpdRegMolFormatException;
    
    public List<String> getMolFragments(String molfile) throws CmpdRegMolFormatException;

}
