package com.labsynch.labseer.chemclasses.rdkit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Autowired;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.chemclasses.CmpdRegSDFWriter;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;


@Configurable
public class CmpdRegSDFWriterRDKitImpl implements CmpdRegSDFWriter {

    Logger logger = LoggerFactory.getLogger(CmpdRegSDFWriterRDKitImpl.class);

	@Autowired
	private ExternalStructureService bbChemStructureServices;

    private FileWriter writer;

    private File tempFile;

    public CmpdRegSDFWriterRDKitImpl(String fileName) {
        try {
			this.writer = new FileWriter(fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
            logger.error("Unable to write RDKit sdf", e);
		}
    }

    public CmpdRegSDFWriterRDKitImpl() {
        try {
            this.tempFile = File.createTempFile("rdkit-molwriter-", ".sdf");
            this.tempFile.deleteOnExit();
            this.writer = new FileWriter(this.tempFile.getAbsolutePath());
        } catch (IOException e) {
            logger.error("Unable to write RDKit sdf", e);
        }

    }

    @Override
    public boolean writeMol(CmpdRegMolecule molecule) throws CmpdRegMolFormatException, IOException {
        CmpdRegMoleculeRDKitImpl molWrapper = (CmpdRegMoleculeRDKitImpl) molecule;
        try {
            this.writer.write(bbChemStructureServices.getSDFFromRDkitStructure(molWrapper.molecule));
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
