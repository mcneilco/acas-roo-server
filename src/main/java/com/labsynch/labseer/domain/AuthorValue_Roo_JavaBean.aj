// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.AuthorState;
import com.labsynch.labseer.domain.AuthorValue;

privileged aspect AuthorValue_Roo_JavaBean {
    
    public AuthorState AuthorValue.getLsState() {
        return this.lsState;
    }
    
    public void AuthorValue.setLsState(AuthorState lsState) {
        this.lsState = lsState;
    }
    
}
