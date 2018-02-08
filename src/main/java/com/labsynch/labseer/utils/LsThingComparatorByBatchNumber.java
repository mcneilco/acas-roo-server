package com.labsynch.labseer.utils;

import java.util.Comparator;

import com.labsynch.labseer.domain.LsThing;

public class LsThingComparatorByBatchNumber implements Comparator<LsThing> {
	
	@Override
	public int compare(LsThing o1, LsThing o2){
		String o1CorpName = o1.pickBestCorpName().getLabelText();
		String[] splitO1CorpName = o1CorpName.split("\\D+");
		int o1BatchNumber = Integer.parseInt(splitO1CorpName[splitO1CorpName.length-1]);
		String o2CorpName = o2.pickBestCorpName().getLabelText();
		String[] splitO2CorpName = o2CorpName.split("\\D+");
		int o2BatchNumber = Integer.parseInt(splitO2CorpName[splitO2CorpName.length-1]);
		return o1BatchNumber - o2BatchNumber;
	}

}
