package com.labsynch.labseer.service;

import java.util.Set;

import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.domain.LotAlias;


public interface LotAliasService {


	public Lot updateLotAliases(Lot lot);

	public Lot updateLotAliases(Lot lot,
			Set<LotAlias> lotAliases);

	Lot updateLotDefaultAlias(Lot lot, String aliasList);

	Lot updateLotAliasByTypeAndKind(Lot lot, String lsType, String lsKind, String aliasList);

}
