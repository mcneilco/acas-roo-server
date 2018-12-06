package com.labsynch.labseer.service;

import java.io.IOException;

import com.labsynch.labseer.dto.RegSearchDTO;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;

public interface RegSearchService {

	public RegSearchDTO getParentsbyParams(String searchParamsString) throws IOException, CmpdRegMolFormatException;

}
