// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.dto;

import com.labsynch.labseer.domain.ProtocolState;
import com.labsynch.labseer.dto.ProtocolStatePathDTO;
import java.util.Collection;

privileged aspect ProtocolStatePathDTO_Roo_JavaBean {
    
    public String ProtocolStatePathDTO.getIdOrCodeName() {
        return this.idOrCodeName;
    }
    
    public void ProtocolStatePathDTO.setIdOrCodeName(String idOrCodeName) {
        this.idOrCodeName = idOrCodeName;
    }
    
    public String ProtocolStatePathDTO.getStateType() {
        return this.stateType;
    }
    
    public void ProtocolStatePathDTO.setStateType(String stateType) {
        this.stateType = stateType;
    }
    
    public String ProtocolStatePathDTO.getStateKind() {
        return this.stateKind;
    }
    
    public void ProtocolStatePathDTO.setStateKind(String stateKind) {
        this.stateKind = stateKind;
    }
    
    public Collection<ProtocolState> ProtocolStatePathDTO.getStates() {
        return this.states;
    }
    
    public void ProtocolStatePathDTO.setStates(Collection<ProtocolState> states) {
        this.states = states;
    }
    
}