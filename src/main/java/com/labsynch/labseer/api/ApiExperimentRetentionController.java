package com.labsynch.labseer.api;

import java.util.List;

import com.labsynch.labseer.service.AuthorService;
import com.labsynch.labseer.service.ExperimentRetentionService;
import com.labsynch.labseer.utils.SecurityUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Manual trigger for the experiment retention purge. The purge normally runs on roo's internal
 * schedule; this endpoint lets an admin kick it off on demand. Both paths funnel through the same
 * cluster-locked service method, so concurrent runs are still serialized.
 */
@RestController
@RequestMapping("/api/v1/experiments/retention")
public class ApiExperimentRetentionController {

    @Autowired
    private ExperimentRetentionService experimentRetentionService;

    @Autowired
    private AuthorService authorService;

    /**
     * POST /api/v1/experiments/retention/run
     * Admin-only. Triggers a retention purge and returns the experiment codes purged in this run.
     */
    @PostMapping("/run")
    public ResponseEntity<List<String>> runRetentionPurge() {
        if (!authorService.isAdmin(SecurityUtil.getLoginUser())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(experimentRetentionService.purgeExpiredExperiments());
    }
}
