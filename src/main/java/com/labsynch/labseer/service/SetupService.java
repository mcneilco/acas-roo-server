package com.labsynch.labseer.service;

import java.io.FileNotFoundException;

public interface SetupService {

	void loadCorpNames(String corpFileName) throws FileNotFoundException;

	void loadCorpNames() throws FileNotFoundException;

}
