package com.labsynch.labseer.chemclasses.rdkit;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.chemclasses.CmpdRegSDFWriter;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;

import org.RDKit.SDWriter;

public class CmpdRegSDFWriterRDKitImpl implements CmpdRegSDFWriter {

    Logger logger = LoggerFactory.getLogger(CmpdRegSDFWriterRDKitImpl.class);

    private SDWriter writer;

    private File tempFile;

    public CmpdRegSDFWriterRDKitImpl(String fileName) {
        this.writer = new SDWriter(fileName);
    }

    public CmpdRegSDFWriterRDKitImpl() {
        // RDKIT doesn't have the ability to write in memory so we create a file
        // which gets deleted on exit
        try {
            this.tempFile = File.createTempFile("rdkit-molwriter-", ".sdf");
            this.tempFile.deleteOnExit();
            this.writer = new SDWriter(this.tempFile.getAbsolutePath());
        } catch (IOException e) {
            logger.error("Unable to write RDKit sdf", e);
        }

    }

    @Override
    public boolean writeMol(CmpdRegMolecule molecule) throws CmpdRegMolFormatException, IOException {
        CmpdRegMoleculeRDKitImpl molWrapper = (CmpdRegMoleculeRDKitImpl) molecule;
        try {
            this.writer.write(molWrapper.molecule);
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
