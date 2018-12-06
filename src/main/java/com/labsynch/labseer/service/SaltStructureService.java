package com.labsynch.labseer.service;

import com.labsynch.labseer.domain.Salt;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;



public interface SaltStructureService {

	public Salt saveStructure(Salt salt) throws CmpdRegMolFormatException;

	public Salt update(Salt salt) throws CmpdRegMolFormatException;


}
