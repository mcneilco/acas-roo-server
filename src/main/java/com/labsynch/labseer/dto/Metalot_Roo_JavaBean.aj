// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.dto;

import com.labsynch.labseer.domain.FileList;
import com.labsynch.labseer.domain.IsoSalt;
import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.dto.Metalot;
import java.util.Set;

privileged aspect Metalot_Roo_JavaBean {
    
    public boolean Metalot.isSkipParentDupeCheck() {
        return this.skipParentDupeCheck;
    }
    
    public void Metalot.setSkipParentDupeCheck(boolean skipParentDupeCheck) {
        this.skipParentDupeCheck = skipParentDupeCheck;
    }
    
    public Lot Metalot.getLot() {
        return this.lot;
    }
    
    public void Metalot.setLot(Lot lot) {
        this.lot = lot;
    }
    
    public Set<IsoSalt> Metalot.getIsosalts() {
        return this.isosalts;
    }
    
    public void Metalot.setIsosalts(Set<IsoSalt> isosalts) {
        this.isosalts = isosalts;
    }
    
    public Set<FileList> Metalot.getFileList() {
        return this.fileList;
    }
    
    public void Metalot.setFileList(Set<FileList> fileList) {
        this.fileList = fileList;
    }
    
}