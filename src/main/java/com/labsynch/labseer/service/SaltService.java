package com.labsynch.labseer.service;

import java.util.ArrayList;

import com.labsynch.labseer.domain.Salt;
import com.labsynch.labseer.dto.PurgeSaltDependencyCheckResponseDTO;


public interface SaltService {

	int loadSalts(String saltSD_fileName);

	String exportSalts();

	ArrayList<Long> getAllParentIDs(Salt salt);

	PurgeSaltDependencyCheckResponseDTO checkDependentData(Salt salt);

	boolean isValidSaltEdit(Salt oldSalt, Salt newSalt);

	public void updateDependencies(Salt salt);

	ArrayList<ErrorMessage> validateSaltEdit(Salt oldSalt, Salt newSalt);
}
