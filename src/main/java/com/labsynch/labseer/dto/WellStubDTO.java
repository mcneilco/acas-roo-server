package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.Container;
import com.labsynch.labseer.domain.ContainerLabel;
import com.labsynch.labseer.service.ContainerService;

@RooJavaBean
@RooToString
@RooJson
public class WellStubDTO {
	
	private String wellName;
	private String codeName;
	
	private Integer rowIndex;
	
	private Integer columnIndex;
	
	public WellStubDTO(){
	}
	
	public WellStubDTO(String wellName, String codeName, Integer rowIndex, Integer columnIndex){
		this.wellName = wellName;
		this.codeName = codeName;
		this.rowIndex = rowIndex;
		this.columnIndex = columnIndex;
	}

	public static Collection<WellStubDTO> convertToWellStubDTOs(
			Collection<Container> wells) {
		Collection<WellStubDTO> wellStubs = new ArrayList<WellStubDTO>();
		for (Container well : wells){
			String wellName = ContainerLabel.pickBestLabel(well.getLsLabels()).getLabelText();
			WellStubDTO wellStub = new WellStubDTO(wellName, well.getCodeName(), well.getRowIndex(), well.getColumnIndex());
			wellStubs.add(wellStub);
		}
		return wellStubs;
	}
	

}


