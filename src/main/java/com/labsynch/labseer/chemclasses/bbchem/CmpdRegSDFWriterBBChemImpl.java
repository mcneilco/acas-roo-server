package com.labsynch.labseer.chemclasses.bbchem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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

    private FileWriter writer;

    private File tempFile;

    public CmpdRegSDFWriterBBChemImpl(String fileName) {
        try {
            this.writer = new FileWriter(fileName);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error("Unable to write sdf", e);
        }
    }

    public CmpdRegSDFWriterBBChemImpl() {
        try {
            this.tempFile = File.createTempFile("bbchem-molwriter-", ".sdf");
            this.tempFile.deleteOnExit();
            this.writer = new FileWriter(this.tempFile.getAbsolutePath());
        } catch (IOException e) {
            logger.error("Unable to write sdf", e);
        }

    }

    @Override
    public boolean writeMol(CmpdRegMolecule molecule) throws CmpdRegMolFormatException, IOException {
        CmpdRegMoleculeBBChemImpl molWrapper = (CmpdRegMoleculeBBChemImpl) molecule;
        try {
            this.writer.write(bbChemStructureServices.getSDF(molWrapper.molecule));
        } catch (Exception e) {
            logger.error("Unable to write mol", e);
            return false;
        }
        return true;
    }

    @Override
    public void close() throws IOException {
        this.writer.close();
        if (this.tempFile != null && this.tempFile.exists()) {
            this.tempFile.delete();
        }
    }

    @Override
    public String getBufferString() {
        return this.writer.toString();
    }

}
