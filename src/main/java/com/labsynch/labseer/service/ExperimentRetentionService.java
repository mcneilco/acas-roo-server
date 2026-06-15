package com.labsynch.labseer.service;

import java.util.List;

public interface ExperimentRetentionService {
    /**
     * Hard deletes all experiments and related data that have expired per retention policy.
     * Returns the list of experiment codes deleted.
     */
    List<String> hardDeleteExpiredExperiments();
    
    /**
     * Returns a list of experiment codes that have a "database deleted date" but are missing
     * the "files deleted date" experiment value, indicating files still need to be cleaned up.
     */
    List<String> getExperimentsAwaitingFilesDeletion();
    
    /**
     * Completes the final deletion of experiments that have both database and files deleted dates.
     * This removes the experiment values, experiment states, and experiment records.
     * 
     * @return List of experiment codes that were fully deleted
     */
    List<String> completeExperimentDeletion();
}
