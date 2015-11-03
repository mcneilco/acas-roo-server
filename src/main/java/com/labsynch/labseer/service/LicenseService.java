package com.labsynch.labseer.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;

import org.bouncycastle.openpgp.PGPException;
import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.Author;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.LicenseDTO;

@Service
public interface LicenseService {

	LicenseDTO getLicenseInfo() throws IOException, PGPException, URISyntaxException;



}
