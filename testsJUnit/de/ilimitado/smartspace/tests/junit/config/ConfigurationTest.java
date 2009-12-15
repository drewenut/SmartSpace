package de.ilimitado.smartspace.tests.junit.config;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import de.ilimitado.smartspace.config.Configuration;
import de.ilimitado.smartspace.config.ConfigSensing;
import de.ilimitado.smartspace.config.ConfigLocalization;

public class ConfigurationTest {

	@Before
	public void setUp() throws Exception {
		MockConfigTranslator.getInstance().translate();
	}
	
	@Test()
	public void testInstance() {
		try {
			Configuration configuration = Configuration.getInstance();
			assertNotNull("Configuration should have been initialized", configuration);
		} catch (Exception e) {
			fail("No exception expected, but got: " + e.getMessage());
		}
	}
	
	@Test()
	public void testPositionAlgorithms() {
		ConfigLocalization posAlgos = Configuration.getInstance().localization;
		assertNotNull("PositionAlgorithms should have been initialized", posAlgos);
		assertTrue("Euklidean should be TRUE", posAlgos.euklideanDist);
	}
	
	@Test()
	public void testFPTCollection() {
		ConfigSensing posAlgos = Configuration.getInstance().sensing;
		assertNotNull("FPTCollection should have been initialized", posAlgos);
		assertEquals(4, posAlgos.orientationQuantizationCount);
	}

}
