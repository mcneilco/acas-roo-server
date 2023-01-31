package com.labsynch.labseer.service;

import java.util.Set;

import com.labsynch.labseer.domain.Parent;
import com.labsynch.labseer.domain.ParentAlias;
import com.labsynch.labseer.exceptions.NonUniqueAliasException;

public interface ParentAliasService {

	public Parent updateParentAliases(Parent parent) throws NonUniqueAliasException;

	public Parent updateParentAliases(Parent parent,
			Set<ParentAlias> parentAliases) throws NonUniqueAliasException;

	Parent updateParentLiveDesignAlias(Parent parent, String aliasList);

	Parent updateParentCommonNameAlias(Parent parent, String aliasList);

	Parent updateParentDefaultAlias(Parent parent, String aliasList);

	Parent updateParentAliasByTypeAndKind(Parent parent, String lsType, String lsKind, String aliasList);

	void validateParentAliases(Long parentId, Set<ParentAlias> aliasesToBeSaved) throws NonUniqueAliasException;
}
