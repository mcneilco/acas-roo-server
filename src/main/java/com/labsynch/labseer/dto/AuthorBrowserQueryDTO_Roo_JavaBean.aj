// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.dto;

import com.labsynch.labseer.dto.AuthorBrowserQueryDTO;
import com.labsynch.labseer.dto.AuthorQueryDTO;

privileged aspect AuthorBrowserQueryDTO_Roo_JavaBean {
    
    public String AuthorBrowserQueryDTO.getQueryString() {
        return this.queryString;
    }
    
    public void AuthorBrowserQueryDTO.setQueryString(String queryString) {
        this.queryString = queryString;
    }
    
    public AuthorQueryDTO AuthorBrowserQueryDTO.getQueryDTO() {
        return this.queryDTO;
    }
    
    public void AuthorBrowserQueryDTO.setQueryDTO(AuthorQueryDTO queryDTO) {
        this.queryDTO = queryDTO;
    }
    
}
