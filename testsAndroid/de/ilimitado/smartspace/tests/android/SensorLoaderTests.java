package de.ilimitado.smartspace.tests.android;


import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

import android.test.AndroidTestCase;
import de.ilimitado.smartspace.AbstractSensorDevice;
import de.ilimitado.smartspace.Dependencies;
import de.ilimitado.smartspace.EventSynchronizer;
import de.ilimitado.smartspace.MotionDetector;
import de.ilimitado.smartspace.PositionManager;
import de.ilimitado.smartspace.SensingReactor;
import de.ilimitado.smartspace.SensorDependencies;
import de.ilimitado.smartspace.SensorEvent;
import de.ilimitado.smartspace.SensorLoader;
import de.ilimitado.smartspace.SensorManager;
import de.ilimitado.smartspace.persistance.PersistanceManager;
import de.ilimitado.smartspace.registry.Registry;
import de.ilimitado.smartspace.sensor.sensor80211.SensorDevice;
import de.ilimitado.smartspace.tests.junit.config.MockConfigTranslator;

public class SensorLoaderTests extends AndroidTestCase{

	private Dependencies appDep;
	private SensorLoader sL;
	private SensorManager sMng;

	@Override
	public void setUp() throws Exception {
		MockConfigTranslator.getInstance().translate();
		LinkedBlockingQueue<SensorEvent<?>> systemRawDataQueue = new LinkedBlockingQueue<SensorEvent<?>>();
		MotionDetector mtnDet = new MotionDetector();
		Registry reg = new Registry();
		PersistanceManager persMngr = new PersistanceManager();
		sMng = new SensorManager();
		PositionManager posMngr = new PositionManager();
		EventSynchronizer evtSync = new EventSynchronizer();
		SensingReactor sReact = new SensingReactor(reg, systemRawDataQueue);
		SensorDependencies sDep = new SensorDependencies(sReact, evtSync, sMng, systemRawDataQueue, reg);
		appDep = new Dependencies(getContext(), sDep, mtnDet, persMngr, posMngr);
		sL = new SensorLoader(appDep);
	}
	
	public void testloadSensors(){
		sL.loadSensors();
		
		HashMap<String, AbstractSensorDevice> sensorDeviceList = sMng.getSensorDeviceList();
		assertEquals(1, sensorDeviceList.size());
		assertTrue(sensorDeviceList.containsKey("Sensor80211"));
		assertTrue(sensorDeviceList.get("Sensor80211") instanceof SensorDevice);
	}

}