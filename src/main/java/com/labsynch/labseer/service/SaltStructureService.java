package com.labsynch.labseer.service;

import java.util.List;

import com.labsynch.labseer.domain.Salt;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.exceptions.StructureSaveException;

public interface SaltStructureService {

	public Salt saveStructure(Salt salt) throws CmpdRegMolFormatException;

	public Salt update(Salt salt) throws CmpdRegMolFormatException;

	List<Salt> saveMissingStructures() throws StructureSaveException;

}
