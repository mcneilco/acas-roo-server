package com.labsynch.labseer.service;

import java.util.Collection;

import com.labsynch.labseer.domain.Parent;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.ParentEditDTO;
import com.labsynch.labseer.dto.ParentValidationDTO;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.exceptions.StandardizerException;

public interface ParentService {

	ParentValidationDTO validateUniqueParent(Parent queryParent)
			throws CmpdRegMolFormatException, StandardizerException;

	Collection<CodeTableDTO> updateParent(Parent parent);

	Parent updateParentMeta(ParentEditDTO parentDTO);

	String updateParentMetaArray(String jsonInput);

}
