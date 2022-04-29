package com.labsynch.labseer.service;

import java.io.IOException;
import java.net.URISyntaxException;

import com.labsynch.labseer.dto.LicenseDTO;

import org.bouncycastle.openpgp.PGPException;
import org.springframework.stereotype.Service;

@Service
public interface LicenseService {

	LicenseDTO getLicenseInfo() throws IOException, PGPException, URISyntaxException;

}
