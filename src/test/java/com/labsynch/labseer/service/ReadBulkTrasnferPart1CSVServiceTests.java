

package com.labsynch.labseer.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseBool;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import com.labsynch.labseer.domain.Container;
import com.labsynch.labseer.domain.ContainerState;
import com.labsynch.labseer.domain.LsTransaction;
import com.labsynch.labseer.dto.BulkTransferPart1DTO;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
public class ReadBulkTrasnferPart1CSVServiceTests {
	
	private static final Logger logger = LoggerFactory.getLogger(ReadBulkTrasnferPart1CSVServiceTests.class);
	private String fieldDelimiter = ",";
	
	@Test
	//@Transactional
	public void ReadCSVFile_Test1() throws IOException{
		
		logger.info("read csv file");
		String testFileName = "Primary Screen Part 1 Transfer Log 20130820_110701.csv";
		InputStream is = ReadBulkTrasnferPart1CSVServiceTests.class.getClassLoader().getResourceAsStream(testFileName);
//		InputStream is = IOUtils.toInputStream(inputFile);

//	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//	    logger.debug(IOUtils.toString(is));
//	    String csvFile = IOUtils.toString(is, "UTF-8");	    
//	    logger.info(csvFile);
	    
		InputStreamReader isr = new InputStreamReader(is);  
		BufferedReader br = new BufferedReader(isr);
//		String headerLine = br.readLine();
//		logger.debug("HeaderLine: " + headerLine);
//
//		StringWriter csvFileOut = new StringWriter();
//		ICsvMapWriter writer = new CsvMapWriter(csvFileOut, CsvPreference.EXCEL_PREFERENCE);
//		
		
		String nameMapping;
		BulkTransferPart1DTO bb = new BulkTransferPart1DTO();
//		beanReader.read(Experiment, nameMapping)
		
        
       // final CellProcessor[] processors = getProcessors();

//		BulkTransferPart1DTO clazz = new BulkTransferPart1DTO();
//		BulkTransferPart1DTO ick = beanReader.read(BulkTransferPart1DTO.class, header, processors);
		
        ICsvBeanReader beanReader = null;
        try {
        	LsTransaction lsTranscation = new LsTransaction();
    		beanReader = new CsvBeanReader(br, CsvPreference.EXCEL_PREFERENCE);
    		String[] headerText = beanReader.getHeader(true);
    		
    		for (String head : headerText){
    			logger.warn("header column: " + head);
    		}

                
                // the header elements are used to map the values to the bean (names must match)
                //final String[] header = beanReader.getHeader(true);
    		
            	final String[] header = new String[] { "sourceBarcode", "sourceWell", "destinationBarcode", "destinationPlateSize", "destinationWell", 
    				 "amountTransferred", "amountUnits", "finalConcentration", "concUnits", "finalVolume",
    				"volumeUnits", "liquidType", "dateTime", "transferProtocol", "isNewPlate", "possibleTransferError"};
            
                final CellProcessor[] processors = getProcessors();
                
                BulkTransferPart1DTO bulkData;
                while( (bulkData = beanReader.read(BulkTransferPart1DTO.class, header, processors)) != null ) {
                        System.out.println(String.format("lineNo=%s, rowNo=%s, bulkData=%s", beanReader.getLineNumber(), beanReader.getRowNumber(), bulkData));
                        
                	
                		Container destinationPlate = getPlate(bulkData.getDestinationBarcode(), bulkData.getDestinationPlateSize());
                		Container sourcePlate = getPlate(bulkData.getDestinationBarcode(), bulkData.getDestinationPlateSize());
                        
                        
                }
                
        }
        finally {
                if( beanReader != null ) {
                        beanReader.close();
                }
        }
		
		

		//beanReader.read(asd, nameMapping);

//		String inputLine = null;
//		while( (inputLine = br.readLine()) != null) {
//			//break delimiter separated line using fieldDelimiter
//			String[] tempString = inputLine.split(fieldDelimiter);
//		}
	    
  
	   
		
//	    Source Barcode,Source Well,Destination Barcode,Destination Plate Size,
//	    Destination Well,Amount Transferred,Amount Units,Final Concentration,
//	    Concentration Units,Final Volume,Final Volume Units,Liquid Type,Date Time,
//	    Protocol,Is New Plate,Possible Transfer Error
	    
		logger.info("Registering samples");
	//	ICsvMapReader inputMapFile = new CsvMapReader(new StringReader(csvFile), CsvPreference.EXCEL_PREFERENCE);
		Map<String, String> dataMapIn = new HashMap<String, String>();
		StringWriter outFile = new StringWriter();
		//ICsvMapWriter writer = new CsvMapWriter(outFile, CsvPreference.EXCEL_PREFERENCE);
        try {
//			String[] inputCSVheader = inputMapFile.getHeader(true);
//			writer.writeHeader(inputCSVheader);
        } catch (Exception e){
        	e.printStackTrace();
		} 
	    
		logger.info(outFile.toString());
	}
	
    private Container getPlate(String destinationBarcode,
			String destinationPlateSize) {
    	Container plate;
    	
        // check if plate exists
        List<Container> foundContainer = Container.findContainerByContainerLabel(destinationBarcode);
        if (foundContainer.size() == 0){
        	logger.warn("did not find the container");
			plate = createPlate(destinationBarcode, destinationPlateSize);
        } else {
        	logger.warn("found the container" + destinationBarcode + "  number of containers: " + foundContainer.size());
        	plate = foundContainer.get(0);
        }
        
		return plate;
	}


	private Container createPlate(String barcode, String plateSize) {
    	Container plate = new Container();
    	String codeName = "to be assigned";
    	plate.setCodeName(codeName);
    	plate.setLsType("plate");
    	plate.setLsKind("384 well compound plate");
    	plate.persist();
    	
    	ContainerState cs = new ContainerState();
    	cs.setLsType("constants");
    	cs.setLsKind("plate format");
    	cs.setContainer(plate);
    	cs.persist();
    	
		return plate;
	}

	
//	  plateCodeNameList <- getAutoLabels(thingTypeAndKind="material_container",
//              labelTypeAndKind="id_codeName", 
//              numberOfLabels=length(barcodes))
//	  return(createContainer(
//			    codeName = codeName[[1]],
//			    lsType = "plate", 
//			    lsKind = "384 well compound plate", 
//			    recordedBy = recordedBy,
//			    lsTransaction = lsTransaction,
//			    containerLabels = list(createContainerLabel(
//			      labelText = barcode,
//			      recordedBy = recordedBy,
//			      lsType = "barcode",
//			      lsKind = "plate barcode",
//			      lsTransaction = lsTransaction)),
//			    containerStates = list(createContainerState(
//			      lsType="constants",
//			      lsKind="plate format",
//			      recordedBy=recordedBy,
//			      lsTransaction=lsTransaction,
//			      containerValues= list(
//			        createStateValue(
//			          lsType="numericValue",
//			          lsKind="rows",
//			          numericValue=16,
//			          lsTransaction=lsTransaction),
//			        createStateValue(
//			          lsType="numericValue",
//			          lsKind="columns",
//			          numericValue=24,
//			          lsTransaction=lsTransaction),
//			        createStateValue(
//			          lsType="numericValue",
//			          lsKind="wells",
//			          numericValue=384,
//			          lsTransaction=lsTransaction)
//			      )
//			    ), createContainerState(
//			      lsType="metadata",
//			      lsKind="plate information",
//			      recordedBy=recordedBy,
//			      lsTransaction=lsTransaction,
//			      containerValues= list(
//			        createStateValue(
//			          lsType="fileValue",
//			          lsKind="source file",
//			          fileValue=sourceFile,
//			          lsTransaction=lsTransaction,
//			          recordedBy= recordedBy))
//			    ))
//			  ))
//			}	
	
	
	private static CellProcessor[] getProcessors() {
    	
//	    Source Barcode,Source Well,Destination Barcode,Destination Plate Size,
//	    Destination Well,Amount Transferred,Amount Units,Final Concentration,
//	    Concentration Units,Final Volume,Final Volume Units,Liquid Type,Date Time,
//	    Protocol,Is New Plate,Possible Transfer Error 16 cols
    	
//    	final String[] header = new String[] { "sourceBarcode", "sourceWell", "destinationBarcode", "destinationWell", 
//			"destinationPlateSize", "amountTransferred", "amountUnits", "finalConcentration", "concUnits", "finalVolume",
//			"volumeUnits", "liquidType", "dateTime", "transferProtocol", "isNewPlate", "possibleTransferError"};
    	
//    	"dd/MM/yyyy" (parses a date formatted as "25/12/2011")
//    	"dd-MMM-yy" (parses a date formatted as "25-Dec-11")
//    	"yyyy.MM.dd.HH.mm.ss" (parses a date formatted as "2011.12.25.08.36.33"
//    	"E, dd MMM yyyy HH:mm:ss Z" (parses a date formatted as "Tue, 25 Dec 2011 08:36:33 -0500")
    	
        final CellProcessor[] processors = new CellProcessor[] { 
                new Optional(),  // sourceBarcode
                new Optional(),  // 
                new Optional(),  // 
                new Optional(),  // 
                new Optional(),  // 
                new Optional(),  // 
                new Optional(),  // 
                new Optional(),  // 
                new Optional(), // 
                new Optional(), // 
                new Optional(), // 
                new Optional(), // 
                new Optional(new ParseDate("yyyy-MM-dd HH:mm")), // dateTime
                new Optional(), // transferProtocol
                new Optional(new ParseBool()), //isNewPlate 
                new Optional()   // 
        };
        
        return processors;
}
	

}
