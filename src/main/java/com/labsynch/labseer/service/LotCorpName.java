package com.labsynch.labseer.service;

public class LotCorpName {

    private String parentCorpName;
    private int lotNumber;
    private String saltCode;

    public String getParentCorpName() {
        return this.parentCorpName;
    }

    public void setParentCorpName(String parentCorpName) {
        this.parentCorpName = parentCorpName;
    }

    public int getLotNumber() {
        return this.lotNumber;
    }

    public void setLotNumber(int lotNumber) {
        this.lotNumber = lotNumber;
    }

    public String getSaltCode() {
        return this.saltCode;
    }

    public void setSaltCode(String saltCode) {
        this.saltCode = saltCode;
    }

}
