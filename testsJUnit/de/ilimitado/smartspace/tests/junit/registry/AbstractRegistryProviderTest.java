package de.ilimitado.smartspace.tests.junit.registry;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import de.ilimitado.smartspace.registry.AbstractRegistryProvider;
import de.ilimitado.smartspace.registry.RegistryProviderException;

public class AbstractRegistryProviderTest extends TestCase{

	@Before
	public void setUp() throws Exception {
	}

	@SuppressWarnings("serial")
	@Test
	public void testPutOneItem() throws RegistryProviderException {
		AbstractRegistryProvider<String, MockObject> regProv = new AbstractRegistryProvider<String, MockObject>(){};
		regProv.putItem("TestItem", MockObject.class);
		assertEquals(1, regProv.size());
		
		MockObject item = regProv.getItem("TestItem");
		assertTrue(item instanceof MockObject);
	}
	
	@SuppressWarnings("serial")
	@Test
	public void testPutTwoItems() throws RegistryProviderException {
		AbstractRegistryProvider<String, MockObject> regProv = new AbstractRegistryProvider<String, MockObject>(){};
		regProv.putItem("TestItem1", MockObject.class);
		regProv.putItem("TestItem2", ExtendedMockObject.class);
		assertEquals(2, regProv.size());
		
		MockObject item1 = regProv.getItem("TestItem1");
		ExtendedMockObject item2 = (ExtendedMockObject) regProv.getItem("TestItem2");
		assertTrue(item1 instanceof MockObject);
		assertTrue(item2 instanceof ExtendedMockObject);
	}

}
