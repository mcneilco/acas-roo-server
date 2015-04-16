package com.labsynch.labseer.utils;

import java.util.Comparator;

import com.labsynch.labseer.domain.LsThing;

public class LsThingComparatorByBatchNumber implements Comparator<LsThing> {
	
	@Override
	public int compare(LsThing o1, LsThing o2){
		String o1CodeName = o1.getCodeName();
		int o1BatchNumber = Integer.parseInt(o1CodeName.split("-")[1]);
		String o2CodeName = o2.getCodeName();
		int o2BatchNumber = Integer.parseInt(o2CodeName.split("-")[1]);
		return o1BatchNumber - o2BatchNumber;
	}

}
