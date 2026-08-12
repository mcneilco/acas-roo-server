package com.labsynch.labseer.service;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.chemclasses.CmpdRegMoleculeFactory;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;

import org.junit.Assert;
import org.junit.Test;

public class RegSearchServiceImplTest {

	@Test
	public void rejectsStructuresWithoutAnExactMass() throws Exception {
		CmpdRegMolecule molecule = (CmpdRegMolecule) Proxy.newProxyInstance(getClass().getClassLoader(),
				new Class[] { CmpdRegMolecule.class }, (proxy, method, args) -> {
					if (method.getName().equals("getExactMass")) {
						return null;
					}
					throw new AssertionError("Unexpected molecule method call: " + method.getName());
				});
		CmpdRegMoleculeFactory moleculeFactory = molStructure -> molecule;
		RegSearchServiceImpl service = new RegSearchServiceImpl();
		setField(service, "cmpdRegMoleculeFactory", moleculeFactory);

		try {
			service.getParentsbyParams("{\"corpName\":\"\",\"molStructure\":\"R-group structure\"}");
			Assert.fail("Expected an invalid structure error");
		} catch (CmpdRegMolFormatException e) {
			Assert.assertEquals(
					"Structures containing wildcard atoms or bonds, including R-groups, are not supported for compound registration. Please modify the structure and try again.",
					e.getMessage());
		}
	}

	private void setField(Object target, String name, Object value) throws Exception {
		Field field = target.getClass().getDeclaredField(name);
		field.setAccessible(true);
		field.set(target, value);
	}
}
