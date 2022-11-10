package com.labsynch.labseer.chemclasses.bbchem;

import java.io.Writer;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.chemclasses.CmpdRegSDFWriter;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
public class CmpdRegSDFWriterBBChemImpl implements CmpdRegSDFWriter {

    Logger logger = LoggerFactory.getLogger(CmpdRegSDFWriterBBChemImpl.class);

    @Autowired
    private BBChemStructureService bbChemStructureServices;

    private Writer writer;

    private Collection<CmpdRegMoleculeBBChemImpl> molCache;

    public CmpdRegSDFWriterBBChemImpl(String fileName) {
        try {
            this.writer = new FileWriter(fileName);
            this.molCache = new ArrayList<CmpdRegMoleculeBBChemImpl>();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error("Unable to write sdf", e);
        }
    }

    public CmpdRegSDFWriterBBChemImpl() {
        this.writer = new StringWriter();
        this.molCache = new ArrayList<CmpdRegMoleculeBBChemImpl>();
    }

    @Override
    public boolean writeMol(CmpdRegMolecule molecule) throws CmpdRegMolFormatException, IOException {
        CmpdRegMoleculeBBChemImpl molWrapper = (CmpdRegMoleculeBBChemImpl) molecule;
        this.molCache.add(molWrapper);
        return true;
    }

    @Override
    public boolean writeMols(List<CmpdRegMolecule> cmpdRegMoleculeList) throws CmpdRegMolFormatException, IOException {
        for (CmpdRegMolecule molecule : cmpdRegMoleculeList) {
            CmpdRegMoleculeBBChemImpl molWrapper = (CmpdRegMoleculeBBChemImpl) molecule;
            this.molCache.add(molWrapper);
        }
        return true;
    }

    private String getSDFText() {
        // Grab all of the BBChemParentStructures out of the molCache
        List<BBChemParentStructure> cmpdRegMoleculeBBChemImplList = new ArrayList<BBChemParentStructure>();
        for (CmpdRegMolecule molecule : this.molCache) {
            CmpdRegMoleculeBBChemImpl molWrapper = (CmpdRegMoleculeBBChemImpl) molecule;
            cmpdRegMoleculeBBChemImplList.add(molWrapper.molecule);
        }
        // reset the molCache now that we've taken these molecules out
        this.molCache = new ArrayList<CmpdRegMoleculeBBChemImpl>();
        // Convert them all to SDF text
        try{
            String sdfText = bbChemStructureServices.getSDF(cmpdRegMoleculeBBChemImplList);
            return sdfText;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void close() throws IOException {
        String sdfText = this.getSDFText();
        // Then write them to the file
        this.writer.write(sdfText);
        this.writer.close();
    }

    @Override
    public String getBufferString() {
        String sdfText = this.getSDFText();
        try{
            this.writer.write(sdfText);
            return this.writer.toString();
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

}
