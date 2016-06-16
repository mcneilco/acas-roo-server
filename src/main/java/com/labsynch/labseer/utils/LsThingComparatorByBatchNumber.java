package com.labsynch.labseer.utils;

import java.util.Comparator;

import com.labsynch.labseer.domain.LsThing;

public class LsThingComparatorByBatchNumber implements Comparator<LsThing> {
	
	@Override
	public int compare(LsThing o1, LsThing o2){
		String o1CorpName = o1.pickBestCorpName().getLabelText();
		int o1BatchNumber = Integer.parseInt(o1CorpName.split("-")[1]);
		String o2CorpName = o2.pickBestCorpName().getLabelText();
		int o2BatchNumber = Integer.parseInt(o2CorpName.split("-")[1]);
		return o1BatchNumber - o2BatchNumber;
	}

}
