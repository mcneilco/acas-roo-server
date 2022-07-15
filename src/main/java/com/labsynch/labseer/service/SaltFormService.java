package com.labsynch.labseer.service;

import java.util.ArrayList;
import java.util.Set;

import com.labsynch.labseer.domain.IsoSalt;
import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.domain.Parent;
import com.labsynch.labseer.domain.SaltForm;
import com.labsynch.labseer.exceptions.DupeSaltFormStructureException;
import com.labsynch.labseer.exceptions.SaltFormMolFormatException;

public interface SaltFormService {

	public void updateSaltWeight(SaltForm saltForm);

	public SaltForm updateSaltForm(Parent parent, SaltForm saltForm, Set<IsoSalt> isoSalts, Lot lot,
			double totalSaltWeight, ArrayList<ErrorMessage> errors)
			throws SaltFormMolFormatException, DupeSaltFormStructureException;

	public double calculateSaltWeight(SaltForm saltForm);

	public void deleteSaltForm(SaltForm saltForm);
}
