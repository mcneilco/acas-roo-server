// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.dto;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.domain.Salt;
import com.labsynch.labseer.dto.StrippedSaltDTO;
import java.util.Map;
import java.util.Set;

privileged aspect StrippedSaltDTO_Roo_JavaBean {
    
    public Map<Salt, Integer> StrippedSaltDTO.getSaltCounts() {
        return this.saltCounts;
    }
    
    public void StrippedSaltDTO.setSaltCounts(Map<Salt, Integer> saltCounts) {
        this.saltCounts = saltCounts;
    }
    
    public Set<? extends CmpdRegMolecule> StrippedSaltDTO.getUnidentifiedFragments() {
        return this.unidentifiedFragments;
    }
    
    public void StrippedSaltDTO.setUnidentifiedFragments(Set<? extends CmpdRegMolecule> unidentifiedFragments) {
        this.unidentifiedFragments = unidentifiedFragments;
    }
    
}
