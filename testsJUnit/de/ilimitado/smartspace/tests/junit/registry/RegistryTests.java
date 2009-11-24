package de.ilimitado.smartspace.tests.junit.registry;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import de.ilimitado.smartspace.persistance.ScanSampleDBPersistanceProvider;
import de.ilimitado.smartspace.registry.DataCommandProvider;
import de.ilimitado.smartspace.registry.Registry;
import de.ilimitado.smartspace.registry.ScanSampleProvider;
import de.ilimitado.smartspace.registry.SensorHandlerProvider;

public class RegistryTests extends TestCase{
	
	private Registry reg;

	@Before
	public void setUp() {
		reg = new Registry();
	}
	
	@Test
	public void testgetScanSampleProvider() {
		ScanSampleProvider sSplProv = (ScanSampleProvider) reg.get(Registry.SENSOR_SCANSAMPLE_PROVIDER);
		assertTrue(sSplProv instanceof ScanSampleProvider);
	}
	
	@Test
	public void testgetSensorHandlerProvider() {
		SensorHandlerProvider sHdlProv = (SensorHandlerProvider) reg.get(Registry.SENSOR_HANDLER_PROVIDER);
		assertTrue(sHdlProv instanceof SensorHandlerProvider);
	}
	
	@Test
	public void testgetDataCommandProvider() {
		DataCommandProvider dCmdProv = (DataCommandProvider) reg.get(Registry.SENSOR_DATA_CMD_PROVIDER);
		assertTrue(dCmdProv instanceof DataCommandProvider);
	}
	
	@Test
	public void testGetScanSamplePersistanceProvider() {
		ScanSampleDBPersistanceProvider sSplPersProv = (ScanSampleDBPersistanceProvider) reg.get(Registry.SENSOR_SCANSAMPLE_DBPERS_PROVIDER);
		assertTrue(sSplPersProv instanceof ScanSampleDBPersistanceProvider);
	}
	
}
