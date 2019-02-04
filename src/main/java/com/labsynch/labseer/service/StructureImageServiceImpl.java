package com.labsynch.labseer.service;

import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.chemclasses.CmpdRegMoleculeFactory;
import com.labsynch.labseer.chemclasses.CmpdRegSDFWriter;
import com.labsynch.labseer.chemclasses.CmpdRegSDFWriterFactory;
import com.labsynch.labseer.dto.SearchCompoundReturnDTO;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.utils.MoleculeUtil;


@Service
public class StructureImageServiceImpl implements StructureImageService {

	Logger logger = LoggerFactory.getLogger(StructureImageServiceImpl.class);

	@Autowired
	CmpdRegMoleculeFactory cmpdRegMoleculeFactory;

	@Autowired
	CmpdRegSDFWriterFactory cmpdRegSDFWriterFactory;


	@Override
	public byte[] convertMolToImage(String molStructure, Integer hSize, Integer wSize, String format) {

		if (format == null){
			format = "png";
		}
		if (hSize == null){
			hSize = 200;
		}
		if (wSize == null){
			wSize = 200;
		}

		String imageFormat = format + ":" + "h" + hSize + ",w" + wSize + ",maxScale28";

		CmpdRegMolecule mol = null;
		try {
			mol = cmpdRegMoleculeFactory.getCmpdRegMolecule(molStructure);
		} catch (CmpdRegMolFormatException e) {
			logger.error("molformat exception. bad structure:" + molStructure);
			e.printStackTrace();
		}
		mol.dearomatize();

		byte[] d4 = null;
		try {
			d4 = MoleculeUtil.exportMolAsBin(mol, format, hSize.toString(), wSize.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //just defaults

		return d4;
	}


	@Override
	public byte[] displayImage(String molStructure) {

		CmpdRegMolecule mol = null;
		try {
			mol = cmpdRegMoleculeFactory.getCmpdRegMolecule(molStructure);
		} catch (CmpdRegMolFormatException e) {
			logger.error("molformat exception. bad structure:" + molStructure);
			e.printStackTrace();
		}
		mol.dearomatize();

		byte[] d4 = null;
		try {
			d4 = MoleculeUtil.exportMolAsBin(mol, "png", "200", "200");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //just defaults

		return d4;
	}

	@Override
	public String convertMolfilesToSDFile(String molfileJsonArray){

		Collection<SearchCompoundReturnDTO> compounds = SearchCompoundReturnDTO.fromJsonArrayToSearchCompoes(molfileJsonArray);
		CmpdRegSDFWriter molExporter = null;
		try {
			molExporter = cmpdRegSDFWriterFactory.getCmpdRegSDFBufferWriter();
		}catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		for (SearchCompoundReturnDTO compound : compounds){
			logger.debug("compound: " + compound.getCorpName());
			try {
				CmpdRegMolecule mol = cmpdRegMoleculeFactory.getCmpdRegMolecule(compound.getMolStructure());
				logger.debug("here is the imported mol: " + MoleculeUtil.exportMolAsText(mol, "smiles"));
				mol.setProperty("corpName", compound.getCorpName());
				mol.setProperty("stereoCategoryName", compound.getStereoCategoryName());
				mol.setProperty("stereoComment", compound.getStereoComment());
				mol.setProperty("lotIDs", compound.getLotIDs().toString());
				molExporter.writeMol(mol);

			} catch (CmpdRegMolFormatException e) {
				logger.error("bad structure error: " + compound.getMolStructure());
			} catch (IOException e) {
				logger.error("IO error reading in molfile");
				e.printStackTrace();
			}

		}
		try {
			molExporter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.debug("output SDF: " + molExporter.getBufferString());
		return molExporter.getBufferString();

	}




}
