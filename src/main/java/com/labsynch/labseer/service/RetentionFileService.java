package com.labsynch.labseer.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Deletes experiment folders from the mounted data-files volume during a retention purge.
 *
 * The experiment folder path convention lives in acas config (ControllerRedirectConf), not roo,
 * so roo asks acas to resolve code -> folder path via a read-only route, then deletes the folders
 * itself (roo has the volume mounted). Every resolved path is validated to live inside the
 * configured data-files root before any recursive delete.
 */
@Service
public class RetentionFileService {

    private static final Logger logger = LoggerFactory.getLogger(RetentionFileService.class);

    /** Absolute path of the experiment data-files root mounted into roo. */
    @Value("${acas.experiment.retention.dataFilesRoot:}")
    private String dataFilesRoot;

    /** Base URL of the acas node service, e.g. http://acas:3000 */
    @Value("${acas.experiment.retention.acasBaseUrl:}")
    private String acasBaseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Resolves and deletes the folders for the given experiment codes.
     *
     * @return true if the file step completed (so the caller may proceed to delete the DB shell);
     *         false if it was skipped because configuration is missing or acas was unreachable
     *         (the caller should leave the shells in place and retry on the next run).
     */
    public boolean deleteFolders(List<String> codes) {
        if (codes == null || codes.isEmpty()) {
            return true;
        }
        if (StringUtils.isBlank(dataFilesRoot) || StringUtils.isBlank(acasBaseUrl)) {
            logger.warn("Retention file deletion skipped: dataFilesRoot or acasBaseUrl not configured "
                + "(dataFilesRoot='{}', acasBaseUrl='{}').", dataFilesRoot, acasBaseUrl);
            return false;
        }

        Map<String, String> codeToPath;
        try {
            codeToPath = resolveFolderPaths(codes);
        } catch (Exception e) {
            logger.error("Retention file deletion skipped: failed to resolve folder paths from acas at {}.",
                acasBaseUrl, e);
            return false;
        }

        File root = new File(dataFilesRoot);
        String canonicalRoot;
        try {
            canonicalRoot = root.getCanonicalPath();
        } catch (IOException e) {
            logger.error("Retention file deletion skipped: cannot canonicalize dataFilesRoot '{}'.", dataFilesRoot, e);
            return false;
        }

        for (String code : codes) {
            String path = codeToPath.get(code);
            if (StringUtils.isBlank(path)) {
                logger.warn("No folder path resolved for experiment {} — skipping its folder.", code);
                continue;
            }
            try {
                File folder = new File(path);
                String canonicalFolder = folder.getCanonicalPath();
                // Safety: only delete inside the configured data-files root.
                if (!canonicalFolder.startsWith(canonicalRoot + File.separator)) {
                    logger.error("Refusing to delete folder for {} — resolved path '{}' is outside data root '{}'.",
                        code, canonicalFolder, canonicalRoot);
                    continue;
                }
                if (!folder.exists()) {
                    logger.info("Folder for experiment {} already absent: {}", code, canonicalFolder);
                    continue;
                }
                deleteRecursively(folder.toPath());
                logger.info("Deleted folder for experiment {}: {}", code, canonicalFolder);
            } catch (IOException e) {
                logger.error("Failed to delete folder for experiment {} (path '{}').", code, path, e);
                // Do not fail the whole run for one folder; continue with the rest.
            }
        }
        return true;
    }

    private Map<String, String> resolveFolderPaths(List<String> codes) {
        String url = acasBaseUrl.replaceAll("/+$", "") + "/api/experiments/folders-for-codes";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> body = new HashMap<>();
        body.put("codes", codes);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map<String, String>> response = restTemplate.exchange(
            url, HttpMethod.POST, request, new ParameterizedTypeReference<Map<String, String>>() {});
        Map<String, String> result = response.getBody();
        return result == null ? new HashMap<>() : result;
    }

    private void deleteRecursively(Path path) throws IOException {
        try (var stream = Files.walk(path)) {
            stream.sorted(Comparator.reverseOrder())
                  .forEach(p -> {
                      try {
                          Files.delete(p);
                      } catch (IOException e) {
                          throw new RuntimeException("Failed to delete " + p, e);
                      }
                  });
        }
    }
}
