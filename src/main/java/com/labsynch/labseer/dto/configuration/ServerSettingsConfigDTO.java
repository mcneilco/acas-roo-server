package com.labsynch.labseer.dto.configuration;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class ServerSettingsConfigDTO {

    private String corpPrefix;

    private String corpSeparator;
    
    private int numberCorpDigits;
    
    private int startingCorpNumber;
        
    private String batchSeparator;
    
    private String saltSeparator;

    private String notebookSavePath;
    
    private String exactMatchDef;
    
    private long maxSearchTime;

    private int maxSearchResults;
    
    private int formatBatchDigits;
    
    private boolean fancyCorpNumberFormat;
    
    private String corpParentFormat;

    private String corpBatchFormat;
    
    private boolean appendSaltCodeToLotName;
    
    private String noSaltCode;
    
    private boolean uniqueNotebook;

    private boolean unitTestDB;

    private boolean initalDBLoad;
    
    private boolean projectRestrictions;
    
    private boolean compoundInventory;
    
    private boolean disableTubeCreationIfNoBarcode;

    private String jchemVersion;
    
    private String databaseType;
    
    private boolean usePredefinedList;
    
    private boolean newUserIsChemist;
    
    private boolean orderSelectLists;
    
    private boolean checkACASDependenciesByContainerCode;
    
    private boolean registerNoStructureCompoundsAsUniqueParents;

}
