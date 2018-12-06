package com.labsynch.labseer.service;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.BulkLoadFile;
import com.labsynch.labseer.domain.BulkLoadTemplate;
import com.labsynch.labseer.dto.BulkLoadPropertiesDTO;
import com.labsynch.labseer.dto.BulkLoadRegisterSDFRequestDTO;
import com.labsynch.labseer.dto.BulkLoadRegisterSDFResponseDTO;
import com.labsynch.labseer.dto.BulkLoadSDFPropertyRequestDTO;
import com.labsynch.labseer.dto.PurgeFileDependencyCheckResponseDTO;
import com.labsynch.labseer.dto.PurgeFileResponseDTO;

@Service
public interface BulkLoadService {


	public BulkLoadPropertiesDTO readSDFPropertiesFromFile(BulkLoadSDFPropertyRequestDTO requestDTO);

	BulkLoadTemplate saveBulkLoadTemplate(BulkLoadTemplate templateToSave);

	BulkLoadRegisterSDFResponseDTO registerSdf(BulkLoadRegisterSDFRequestDTO registerRequestDTO);

	public PurgeFileDependencyCheckResponseDTO checkPurgeFileDependencies(BulkLoadFile bulkLoadFile);

	public PurgeFileResponseDTO purgeFile(BulkLoadFile bulkLoadFile) throws MalformedURLException, IOException;


}
