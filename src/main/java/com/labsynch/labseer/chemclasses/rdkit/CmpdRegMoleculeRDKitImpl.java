package com.labsynch.labseer.chemclasses.rdkit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.RDKit.RDKFuncs;
import org.RDKit.ROMol;
import org.RDKit.RWMol;
import org.RDKit.Str_Vect;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;

public class CmpdRegMoleculeRDKitImpl implements CmpdRegMolecule {
	
	Logger logger = LoggerFactory.getLogger(CmpdRegMoleculeRDKitImpl.class);

	RWMol molecule;

	public CmpdRegMoleculeRDKitImpl(String molStructure) throws CmpdRegMolFormatException{
		this.molecule = RWMol.MolFromMolBlock(molStructure);
	}

	public CmpdRegMoleculeRDKitImpl(RWMol molecule) throws CmpdRegMolFormatException{
		this.molecule = molecule;
	}

	public CmpdRegMoleculeRDKitImpl(ROMol readOnlyMol) {
		this.molecule = (RWMol) readOnlyMol;
	}

	@Override
	public void setProperty(String key, String value) {
		molecule.setProp(key, value);		
	}

	@Override
	public String getProperty(String key) {
		return molecule.getProp(key);
	}

	@Override
	public String[] getPropertyKeys() {
		List<String> propertyKeys = new ArrayList<String>();
		Str_Vect propList = this.molecule.getPropList();
		for (int i = 0; i < propList.size(); i++) {
			propertyKeys.add(propList.get(i));
		}
		String[] keys = new String[propertyKeys.size()];
		return propertyKeys.toArray(keys);
	}

	@Override
	public String getPropertyType(String key) {
		// Does not exist
		return null;
	}

	@Override
	public String getMolStructure() throws CmpdRegMolFormatException {
		return RDKFuncs.MolToMolBlock(this.molecule);
	}

	@Override
	public String getFormula() {
		return RDKFuncs.calcMolFormula(this.molecule);

	}

	@Override
	public Double getExactMass() {
		return RDKFuncs.calcExactMW(this.molecule);
	}

	@Override
	public Double getMass() {
		return RDKFuncs.calcAMW(this.molecule);

	}

	@Override
	public int getTotalCharge() {
		return RDKFuncs.getFormalCharge(this.molecule);

	}

	@Override
	public String getSmiles() {
		return RDKFuncs.MolToSmiles(this.molecule);

	}

	@Override
	public CmpdRegMolecule replaceStructure(String newStructure) throws CmpdRegMolFormatException {
		CmpdRegMoleculeRDKitImpl newMol = new CmpdRegMoleculeRDKitImpl(newStructure);
		Str_Vect propList = this.molecule.getPropList();
		for (int i = 0; i < propList.size(); i++) {
			newMol.setProperty(propList.get(i), this.getProperty(propList.get(i)));
		}
		return newMol;
	}

	@Override
	public String getMrvStructure() {
		// not implemented in RDkit
		return null;
	}

	@Override
	public byte[] toBinary(CmpdRegMolecule molecule, String imageFormat, String hSize, String wSize) 
			throws IOException {
			// Currently only PNG is supported
			String svg = this.molecule.ToSVG();
			TranscoderInput input_svg_image = new TranscoderInput(svg);        
			//Step-2: Define OutputStream to PNG Image and attach to TranscoderOutput
            ByteArrayOutputStream ostream = new ByteArrayOutputStream();
			TranscoderOutput output_png_image = new TranscoderOutput(ostream);              
			// Step-3: Create PNGTranscoder and define hints if required
			PNGTranscoder my_converter = new PNGTranscoder();        
			// Step-4: Convert and Write output
			try {
				my_converter.transcode(input_svg_image, output_png_image);
			} catch (TranscoderException e) {
				e.printStackTrace();
			}
			ostream.flush();
			ostream.close();        
			return ostream.toByteArray();
	}

	@Override
	public void dearomatize() {
		this.molecule.Kekulize();
	}

	public CmpdRegMoleculeRDKitImpl clone() {
		CmpdRegMoleculeRDKitImpl newMol = null;
		try {
			newMol = new CmpdRegMoleculeRDKitImpl(RDKFuncs.MolToMolBlock(this.molecule));
			Str_Vect propList = this.molecule.getPropList();
			for (int i = 0; i < propList.size(); i++) {
				newMol.setProperty(propList.get(i), this.getProperty(propList.get(i)));
			}
			return newMol;
		} catch (CmpdRegMolFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newMol;
	}

}
