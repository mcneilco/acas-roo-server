package com.labsynch.labseer.service;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicBoolean;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.chemclasses.CmpdRegMolecule.RegistrationStatus;
import com.labsynch.labseer.chemclasses.CmpdRegMoleculeFactory;
import com.labsynch.labseer.dto.RegSearchDTO;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.utils.PropertiesUtilService;

import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;

public class RegSearchServiceImplTest {

	private static final String AS_DRAWN_STRUCTURE = "as-drawn structure";
	private static final String PROCESSED_STRUCTURE = "processed structure";
	private static final String SEARCH_PARAMS = "{\"corpName\":\"\",\"molStructure\":\"" + AS_DRAWN_STRUCTURE + "\"}";

	@Test
	public void rejectsStructuresTheChemistryServiceCannotProcess() throws Exception {
		String processorMessage = "Query features are not supported";
		RegSearchServiceImpl service = new RegSearchServiceImpl();
		setField(service, "cmpdRegMoleculeFactory",
				moleculeFactory(lazilyProcessedMolecule(RegistrationStatus.ERROR, processorMessage)));

		try {
			service.getParentsbyParams(SEARCH_PARAMS);
			Assert.fail("Expected an invalid structure error");
		} catch (CmpdRegMolFormatException e) {
			Assert.assertEquals(
					"This structure cannot be registered: " + processorMessage
							+ ". Please modify the structure and try again.",
					e.getMessage());
		}
	}

	@Test
	public void returnsTheSubmittedDrawingAsTheAsDrawnStructureAndImage() throws Exception {
		RegSearchServiceImpl service = new RegSearchServiceImpl();
		setField(service, "cmpdRegMoleculeFactory",
				moleculeFactory(lazilyProcessedMolecule(RegistrationStatus.SUCCESS, null)));
		setField(service, "propertiesUtilService", proxy(PropertiesUtilService.class, (proxy, method, args) -> {
			if (method.getName().equals("getUseExactMass")) {
				return true;
			}
			throw new AssertionError("Unexpected properties call: " + method.getName());
		}));
		setField(service, "chemStructureService", proxy(ChemStructureService.class, (proxy, method, args) -> {
			if (method.getName().equals("searchMolStructures")) {
				return new int[0];
			}
			throw new AssertionError("Unexpected chem structure service call: " + method.getName());
		}));

		RegSearchDTO result = service.getParentsbyParams(SEARCH_PARAMS);

		Assert.assertEquals(AS_DRAWN_STRUCTURE, result.getAsDrawnStructure());
		Assert.assertEquals(AS_DRAWN_STRUCTURE, new String(Base64.decodeBase64(result.getAsDrawnImage())));
	}

	// Mimics BBChem, which only processes the structure on first property access,
	// replacing the as-drawn structure and populating the registration status
	private CmpdRegMolecule lazilyProcessedMolecule(RegistrationStatus statusAfterProcessing,
			String commentAfterProcessing) {
		AtomicBoolean processed = new AtomicBoolean(false);
		return proxy(CmpdRegMolecule.class, (proxy, method, args) -> {
			switch (method.getName()) {
				case "getMolStructure":
					return processed.get() ? PROCESSED_STRUCTURE : AS_DRAWN_STRUCTURE;
				case "toBinary":
					// Render the image as the structure it was drawn from
					return (processed.get() ? PROCESSED_STRUCTURE : AS_DRAWN_STRUCTURE).getBytes();
				case "getExactMass":
				case "getMass":
					processed.set(true);
					return statusAfterProcessing == RegistrationStatus.ERROR ? null : 78.05;
				case "getFormula":
					processed.set(true);
					return statusAfterProcessing == RegistrationStatus.ERROR ? null : "C6H6";
				case "getRegistrationStatus":
					return processed.get() ? statusAfterProcessing : null;
				case "getRegistrationComment":
					return processed.get() ? commentAfterProcessing : null;
				default:
					throw new AssertionError("Unexpected molecule method call: " + method.getName());
			}
		});
	}

	private CmpdRegMoleculeFactory moleculeFactory(CmpdRegMolecule molecule) {
		return molStructure -> molecule;
	}

	@SuppressWarnings("unchecked")
	private <T> T proxy(Class<T> type, java.lang.reflect.InvocationHandler handler) {
		return (T) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { type }, handler);
	}

	private void setField(Object target, String name, Object value) throws Exception {
		Field field = target.getClass().getDeclaredField(name);
		field.setAccessible(true);
		field.set(target, value);
	}
}
