package com.labsynch.labseer.service;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.chemclasses.CmpdRegMolecule.RegistrationStatus;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;

import org.junit.Assert;
import org.junit.Test;

public class BulkLoadServiceImplTest {

	@Test
	public void createParentRejectsStructuresTheChemistryServiceCannotProcess() throws Exception {
		String processorMessage = "Atom with R Label found";
		CmpdRegMolecule mol = proxy((proxy, method, args) -> {
			if (method.getName().equals("getMolStructure")) {
				return "submitted structure";
			}
			throw new AssertionError("Unexpected input molecule call: " + method.getName());
		});
		// Mimics BBChem, which only populates the registration status once a property
		// read triggers processing (e.g. when external standardization is disabled)
		AtomicBoolean processed = new AtomicBoolean(false);
		CmpdRegMolecule standardizedMol = proxy((proxy, method, args) -> {
			switch (method.getName()) {
				case "getExactMass":
					processed.set(true);
					return null;
				case "getRegistrationStatus":
					return processed.get() ? RegistrationStatus.ERROR : null;
				case "getRegistrationComment":
					return processed.get() ? processorMessage : null;
				default:
					throw new AssertionError("Unexpected standardized molecule call: " + method.getName());
			}
		});

		try {
			new BulkLoadServiceImpl().createParent(mol, standardizedMol, false, new ArrayList<>(), "testuser",
					null, new ArrayList<>(), 2);
			Assert.fail("Expected an invalid structure error");
		} catch (CmpdRegMolFormatException e) {
			Assert.assertEquals(
					"This structure cannot be registered: " + processorMessage
							+ ". Please modify the structure and try again.",
					e.getMessage());
		}
	}

	private CmpdRegMolecule proxy(java.lang.reflect.InvocationHandler handler) {
		return (CmpdRegMolecule) Proxy.newProxyInstance(getClass().getClassLoader(),
				new Class[] { CmpdRegMolecule.class }, handler);
	}
}
