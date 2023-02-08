package com.labsynch.labseer.service;

import java.util.List;

import com.labsynch.labseer.domain.Salt;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.exceptions.StructureSaveException;

public interface SaltStructureService {

	public Salt saveStructure(Salt salt) throws CmpdRegMolFormatException;

	public Salt update(Salt salt) throws CmpdRegMolFormatException;

	public Salt saveStructureNoDupeCheck(Salt salt) throws CmpdRegMolFormatException;

	public Salt edit(Salt oldSalt, Salt newSalt);

	List<Salt> saveMissingStructures() throws StructureSaveException;

	public double calculateWeight(Salt salt);

	public String calculateFormula(Salt salt);

	public int calculateCharge(Salt salt);

}
