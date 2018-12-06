package com.labsynch.labseer.service;

import java.io.IOException;
import java.util.Collection;
import java.util.List;


import com.labsynch.labseer.domain.Parent;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.ParentEditDTO;
import com.labsynch.labseer.dto.ParentValidationDTO;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;



public interface ParentService {

	ParentValidationDTO validateUniqueParent(Parent queryParent) throws CmpdRegMolFormatException;

	Collection<CodeTableDTO> updateParent(Parent parent);

	public int restandardizeAllParentStructures() throws CmpdRegMolFormatException, IOException;
	Parent updateParentMeta(ParentEditDTO parentDTO, String modifiedByUser);

	void qcCheckParentStructures() throws CmpdRegMolFormatException, IOException;

	void dupeCheckQCStructures() throws CmpdRegMolFormatException;

	int findPotentialDupeParentStructures(String dupeCheckFile);

	int findDupeParentStructures(String dupeCheckFile);

	int restandardizeParentStructures(List<Long> parentIds) throws CmpdRegMolFormatException, IOException;

	int restandardizeParentStructsWithDisplayChanges() throws CmpdRegMolFormatException, IOException;


	String updateParentMetaArray(String jsonInput, String modifiedByUser);


}
