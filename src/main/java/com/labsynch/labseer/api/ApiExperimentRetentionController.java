package com.labsynch.labseer.api;

import com.labsynch.labseer.service.ExperimentRetentionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/experiments/retention")
public class ApiExperimentRetentionController {

    @Autowired
    private ExperimentRetentionService experimentRetentionService;

    /**
     * POST /api/experiments/retention/hard-delete
     * Triggers hard deletion of expired experiments. Returns list of deleted codes.
     */
    @PostMapping("/hard-delete")
    public ResponseEntity<java.util.List<String>> hardDeleteExpiredExperiments() {
        java.util.List<String> deletedCodes = experimentRetentionService.hardDeleteExpiredExperiments();
        return ResponseEntity.ok(deletedCodes);
    }

    /**
     * GET /api/experiments/retention/awaiting-files-deletion
     * Returns list of experiment codes that have database deleted date but missing files deleted date.
     */
    @GetMapping("/awaiting-files-deletion")
    public ResponseEntity<java.util.List<String>> getExperimentsAwaitingFilesDeletion() {
        java.util.List<String> experimentCodes = experimentRetentionService.getExperimentsAwaitingFilesDeletion();
        return ResponseEntity.ok(experimentCodes);
    }

    /**
     * POST /api/experiments/retention/complete-deletion
     * Completes the final deletion of experiments that have both database and files deleted dates.
     */
    @PostMapping("/complete-deletion")
    public ResponseEntity<java.util.List<String>> completeExperimentDeletion() {
        java.util.List<String> deletedCodes = experimentRetentionService.completeExperimentDeletion();
        return ResponseEntity.ok(deletedCodes);
    }
}
