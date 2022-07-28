package com.labsynch.labseer.service;

import com.labsynch.labseer.domain.Salt;
import com.labsynch.labseer.dto.PurgeSaltDependencyCheckResponseDTO;


public interface SaltService {

	int loadSalts(String saltSD_fileName);

	String exportSalts();

	PurgeSaltDependencyCheckResponseDTO checkDependentData(Salt salt);
}
