package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class BulkTransferPart1DTO {

    private String sourceBarcode;
    private String sourceWell;
    private String destinationBarcode;
    private String destinationWell;
    private String destinationPlateSize;
    private String amountTransferred;
    private String amountUnits;
    private String finalConcentration;
    private String concUnits;
    private String finalVolume;
    private String volumeUnits;
    private String liquidType;
    private Date dateTime;

    private String transferProtocol;
    private boolean isNewPlate;
    private String possibleTransferError;

    // Source Barcode,Source Well,Destination Barcode,Destination Plate Size,
    // Destination Well,Amount Transferred,Amount Units,Final Concentration,
    // Concentration Units,Final Volume,Final Volume Units,Liquid Type,Date Time,
    // Protocol,Is New Plate,Possible Transfer Error

    public String toJson() {
        return new JSONSerializer()
                .exclude("*.class").serialize(this);
    }

    public String toJson(String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(this);
    }

    public static BulkTransferPart1DTO fromJsonToBulkTransferPart1DTO(String json) {
        return new JSONDeserializer<BulkTransferPart1DTO>()
                .use(null, BulkTransferPart1DTO.class).deserialize(json);
    }

    public static String toJsonArray(Collection<BulkTransferPart1DTO> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<BulkTransferPart1DTO> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<BulkTransferPart1DTO> fromJsonArrayToBulkTransferPart1DTO(String json) {
        return new JSONDeserializer<List<BulkTransferPart1DTO>>()
                .use("values", BulkTransferPart1DTO.class).deserialize(json);
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public String getSourceBarcode() {
        return this.sourceBarcode;
    }

    public void setSourceBarcode(String sourceBarcode) {
        this.sourceBarcode = sourceBarcode;
    }

    public String getSourceWell() {
        return this.sourceWell;
    }

    public void setSourceWell(String sourceWell) {
        this.sourceWell = sourceWell;
    }

    public String getDestinationBarcode() {
        return this.destinationBarcode;
    }

    public void setDestinationBarcode(String destinationBarcode) {
        this.destinationBarcode = destinationBarcode;
    }

    public String getDestinationWell() {
        return this.destinationWell;
    }

    public void setDestinationWell(String destinationWell) {
        this.destinationWell = destinationWell;
    }

    public String getDestinationPlateSize() {
        return this.destinationPlateSize;
    }

    public void setDestinationPlateSize(String destinationPlateSize) {
        this.destinationPlateSize = destinationPlateSize;
    }

    public String getAmountTransferred() {
        return this.amountTransferred;
    }

    public void setAmountTransferred(String amountTransferred) {
        this.amountTransferred = amountTransferred;
    }

    public String getAmountUnits() {
        return this.amountUnits;
    }

    public void setAmountUnits(String amountUnits) {
        this.amountUnits = amountUnits;
    }

    public String getFinalConcentration() {
        return this.finalConcentration;
    }

    public void setFinalConcentration(String finalConcentration) {
        this.finalConcentration = finalConcentration;
    }

    public String getConcUnits() {
        return this.concUnits;
    }

    public void setConcUnits(String concUnits) {
        this.concUnits = concUnits;
    }

    public String getFinalVolume() {
        return this.finalVolume;
    }

    public void setFinalVolume(String finalVolume) {
        this.finalVolume = finalVolume;
    }

    public String getVolumeUnits() {
        return this.volumeUnits;
    }

    public void setVolumeUnits(String volumeUnits) {
        this.volumeUnits = volumeUnits;
    }

    public String getLiquidType() {
        return this.liquidType;
    }

    public void setLiquidType(String liquidType) {
        this.liquidType = liquidType;
    }

    public Date getDateTime() {
        return this.dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getTransferProtocol() {
        return this.transferProtocol;
    }

    public void setTransferProtocol(String transferProtocol) {
        this.transferProtocol = transferProtocol;
    }

    public boolean isIsNewPlate() {
        return this.isNewPlate;
    }

    public void setIsNewPlate(boolean isNewPlate) {
        this.isNewPlate = isNewPlate;
    }

    public String getPossibleTransferError() {
        return this.possibleTransferError;
    }

    public void setPossibleTransferError(String possibleTransferError) {
        this.possibleTransferError = possibleTransferError;
    }
}
