// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.dto;

import com.labsynch.labseer.dto.LotAliasDTO;
import com.labsynch.labseer.dto.SearchLotDTO;
import java.util.Date;
import java.util.List;

privileged aspect SearchLotDTO_Roo_JavaBean {
    
    public String SearchLotDTO.getCorpName() {
        return this.corpName;
    }
    
    public void SearchLotDTO.setCorpName(String corpName) {
        this.corpName = corpName;
    }
    
    public int SearchLotDTO.getLotNumber() {
        return this.lotNumber;
    }
    
    public void SearchLotDTO.setLotNumber(int lotNumber) {
        this.lotNumber = lotNumber;
    }
    
    public long SearchLotDTO.getBuid() {
        return this.buid;
    }
    
    public void SearchLotDTO.setBuid(long buid) {
        this.buid = buid;
    }
    
    public List<LotAliasDTO> SearchLotDTO.getLotAliases() {
        return this.lotAliases;
    }
    
    public void SearchLotDTO.setLotAliases(List<LotAliasDTO> lotAliases) {
        this.lotAliases = lotAliases;
    }
    
    public Date SearchLotDTO.getRegistrationDate() {
        return this.registrationDate;
    }
    
    public void SearchLotDTO.setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }
    
    public Date SearchLotDTO.getSynthesisDate() {
        return this.synthesisDate;
    }
    
    public void SearchLotDTO.setSynthesisDate(Date synthesisDate) {
        this.synthesisDate = synthesisDate;
    }
    
}