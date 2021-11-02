package com.labsynch.labseer.chemclasses.indigo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.indigo.Indigo;
import com.epam.indigo.IndigoException;
import com.epam.indigo.IndigoObject;
import com.epam.indigo.IndigoRenderer;
import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;

public class CmpdRegMoleculeIndigoImpl implements CmpdRegMolecule {
	
	Logger logger = LoggerFactory.getLogger(CmpdRegMoleculeIndigoImpl.class);
	
	private Indigo indigo = new Indigo();
	
	IndigoObject molecule;
	
	public CmpdRegMoleculeIndigoImpl(String molStructure) throws CmpdRegMolFormatException{
		try{
			this.molecule = indigo.loadMolecule(molStructure);
		}catch (IndigoException e) {
			throw new CmpdRegMolFormatException(e);
		}
	}
	
	public CmpdRegMoleculeIndigoImpl(IndigoObject molecule){
		this.molecule = molecule;
	}
	
	public String getProperty(String key){
		if (molecule.hasProperty(key)){
			return molecule.getProperty(key);
		}else {
			return null;
		}
	}
	
	public void setProperty(String key, String value){
		this.molecule.setProperty(key, value);
	}

	@Override
	public String[] getPropertyKeys() {
		List<String> propertyKeys = new ArrayList<String>();
		for (IndigoObject prop:  this.molecule.iterateProperties()) {
			propertyKeys.add(prop.name());
		}
		String[] keys = new String[propertyKeys.size()];
		return propertyKeys.toArray(keys);
	}

	@Override
	public String getPropertyType(String key) {
		//Does not seem to exist
		return null;
	}

	@Override
	public String getMolStructure() throws CmpdRegMolFormatException {
		try{
			return this.molecule.molfile();
		}catch (IndigoException e) {
			throw new CmpdRegMolFormatException(e);
		}
	}
	
	@Override
	public String getSmiles() {
		return this.molecule.smiles();
	}
	
	@Override
	public String getMrvStructure() {
		// not implemented in Indigo
		return null;
	}; 

	@Override
	public String getFormula() {
		return this.molecule.grossFormula();
	}

	@Override
	public Double getExactMass() {
		return (double) this.molecule.monoisotopicMass();
	}

	@Override
	public Double getMass() {
		return (double) this.molecule.molecularWeight();
	}

	@Override
	public int getTotalCharge() {
		int totalCharge = 0;
		for (IndigoObject atom : this.molecule.iterateAtoms()) {
			totalCharge += atom.charge();
		}
		return totalCharge;
	}

	@Override
	public CmpdRegMolecule replaceStructure(String newStructure) throws CmpdRegMolFormatException {
		CmpdRegMoleculeIndigoImpl newMol = new CmpdRegMoleculeIndigoImpl(newStructure);
		for (IndigoObject prop : this.molecule.iterateProperties()) {
			newMol.setProperty(prop.name(), prop.rawData());
		}
		return newMol;
	}

	@Override
	public byte[] toBinary(CmpdRegMolecule molecule, String imageFormat, String hSize, String wSize) throws IOException {
		IndigoObject mol = ((CmpdRegMoleculeIndigoImpl) molecule).molecule;
		try {
			IndigoRenderer renderer = new IndigoRenderer(indigo);
			indigo.setOption("render-output-format", imageFormat);
			indigo.setOption("render-image-height", hSize);
			indigo.setOption("render-image-width", wSize);
			return renderer.renderToBuffer(mol);
		}
		catch (Exception e) {
			logger.error("cannot render",e);
			return null;
		}
	}

	@Override
	public void dearomatize() {
		this.molecule.dearomatize();
	}

}
