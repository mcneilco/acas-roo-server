// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.dto;

import com.labsynch.labseer.dto.LsThingBrowserQueryDTO;
import com.labsynch.labseer.dto.LsThingQueryDTO;

privileged aspect LsThingBrowserQueryDTO_Roo_JavaBean {
    
    public String LsThingBrowserQueryDTO.getQueryString() {
        return this.queryString;
    }
    
    public void LsThingBrowserQueryDTO.setQueryString(String queryString) {
        this.queryString = queryString;
    }
    
    public LsThingQueryDTO LsThingBrowserQueryDTO.getQueryDTO() {
        return this.queryDTO;
    }
    
    public void LsThingBrowserQueryDTO.setQueryDTO(LsThingQueryDTO queryDTO) {
        this.queryDTO = queryDTO;
    }
    
}