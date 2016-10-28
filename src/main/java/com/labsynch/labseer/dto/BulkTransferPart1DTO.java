package com.labsynch.labseer.dto;

import java.util.Date;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
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
    
    
//    Source Barcode,Source Well,Destination Barcode,Destination Plate Size,
//    Destination Well,Amount Transferred,Amount Units,Final Concentration,
//    Concentration Units,Final Volume,Final Volume Units,Liquid Type,Date Time,
//    Protocol,Is New Plate,Possible Transfer Error


}
