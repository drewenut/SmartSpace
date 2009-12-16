package de.ilimitado.smartspace.tests.android;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.AndroidTestCase;
import de.ilimitado.smartspace.AbstractSensorDevice;
import de.ilimitado.smartspace.AbstractSensorHandler;
import de.ilimitado.smartspace.Dependencies;
import de.ilimitado.smartspace.EventSynchronizer;
import de.ilimitado.smartspace.MotionDetector;
import de.ilimitado.smartspace.IndoorLocationManager;
import de.ilimitado.smartspace.SensingReactor;
import de.ilimitado.smartspace.SensorDependencies;
import de.ilimitado.smartspace.SensorEvent;
import de.ilimitado.smartspace.SensorManager;
import de.ilimitado.smartspace.android.AndroidConfigTranslator;
import de.ilimitado.smartspace.config.Configuration;
import de.ilimitado.smartspace.persistance.PersistanceManager;
import de.ilimitado.smartspace.registry.Registry;
import de.ilimitado.smartspace.sensor.sensor80211.SensorDevice80211;
import de.ilimitado.smartspace.sensor.sensor80211.EventHandler80211;

public class SensorTests extends AndroidTestCase{

	private SensorDevice80211 sD;
	private Dependencies appDep;
	private Configuration config;
	private SensorManager sMng;
	private LinkedBlockingQueue<SensorEvent<?>> systemRawDataQueue;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Context androidCtx = getContext();
		SharedPreferences androidPreferences = PreferenceManager.getDefaultSharedPreferences(androidCtx);
		AndroidConfigTranslator.getInstance(androidPreferences).translate();
		this.config = Configuration.getInstance();
		systemRawDataQueue = new LinkedBlockingQueue<SensorEvent<?>>();
		MotionDetector mtnDet = new MotionDetector();
		Registry reg = new Registry();
		PersistanceManager persMngr = new PersistanceManager();
		sMng = new SensorManager();
		IndoorLocationManager posMngr = new IndoorLocationManager();
		EventSynchronizer sSync = new EventSynchronizer();
		SensingReactor sReact = new SensingReactor(reg, systemRawDataQueue);
		SensorDependencies sDep = new SensorDependencies(sReact, sSync, sMng, systemRawDataQueue, reg);
		this.appDep = new Dependencies(androidCtx, sDep, mtnDet, persMngr, posMngr);
		this.sD = new SensorDevice80211(appDep);
	}
	
	public void testManualSensorLoadWithSensorDevice80211(){
		assertTrue(sD.deviceAvailable());
		sD.initSensorID();
		assertEquals(config.sensor80211.ID, sD.getID());
		sD.initSensorName();
		assertEquals(config.sensor80211.NAME, sD.getName());
		sD.registerEvents(appDep);
		Collection<AbstractSensorHandler> activeEventHandlers = appDep.sensorDependencies.reactor.getActiveEventHandlers();
		assertTrue(activeEventHandlers.size() == 1);
		for(AbstractSensorHandler aSH : activeEventHandlers) {
			assertTrue(aSH instanceof EventHandler80211);
		}
		
		appDep.sensorDependencies.sensorManager.addSensor(sD);
		HashMap<String, AbstractSensorDevice> sensorDeviceList = sMng.getSensorDeviceList();
		assertTrue(sensorDeviceList.containsKey(sD.getID()));
		assertEquals(sensorDeviceList.get(sD.getID()), sD);
	}
	
	
	public void testManualSensorManagerStartStopWithSensorDevice80211(){
		assertTrue(sD.deviceAvailable());
		sD.initSensorID();
		assertEquals(config.sensor80211.ID, sD.getID());
		sD.initSensorName();
		assertEquals(config.sensor80211.NAME, sD.getName());
		sD.registerEvents(appDep);
		Collection<AbstractSensorHandler> activeEventHandlers = appDep.sensorDependencies.reactor.getActiveEventHandlers();
		assertTrue(activeEventHandlers.size() == 1);
		for(AbstractSensorHandler aSH : activeEventHandlers) {
			assertTrue(aSH instanceof EventHandler80211);
		}
		
		appDep.sensorDependencies.sensorManager.addSensor(sD);
		HashMap<String, AbstractSensorDevice> sensorDeviceList = sMng.getSensorDeviceList();
		assertTrue(sensorDeviceList.containsKey(sD.getID()));
		assertEquals(sensorDeviceList.get(sD.getID()), sD);
		
		//manual calls! Normally this initialisation + sensor start is done via SensorManager initDevices(), startSensors() which calls init() {createRunnables()}, 
		//Runable.start() on all sensor device instances to create and start the Runnables!
		sD.createRunnables();
		List<Runnable> sensorRunnables = sD.getSensorRunnables();
		assertEquals(1, sensorRunnables.size());
		Runnable sensorRunnable = sensorRunnables.get(0);
		//SensorManager.startSensors()
		ThreadGroup sensorDeviceRunnables = new ThreadGroup(config.sensor80211.ID);
			new Thread(sensorDeviceRunnables, sensorRunnable).start();
		List<ThreadGroup> runningSensorsList = new ArrayList<ThreadGroup>(1);
		runningSensorsList.add(sensorDeviceRunnables);
		assertEquals(1, runningSensorsList.size());
		ThreadGroup threadGroup = runningSensorsList.get(0);
		assertTrue(threadGroup.activeCount() == 1);
		//SensorManager.stopSensors()
		threadGroup.interrupt();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			fail("Main Thread Interrupted while waiting");
			e.printStackTrace();
		}
		assertTrue(threadGroup.activeCount() == 0);
	}
	
	public void testManualSensorManagerStartWithRawDataEnqueuing(){
		assertTrue(sD.deviceAvailable());
		sD.initSensorID();
		assertEquals(config.sensor80211.ID, sD.getID());
		sD.initSensorName();
		assertEquals(config.sensor80211.NAME, sD.getName());
		sD.registerEvents(appDep);
		Collection<AbstractSensorHandler> activeEventHandlers = appDep.sensorDependencies.reactor.getActiveEventHandlers();
		assertTrue(activeEventHandlers.size() == 1);
		for(AbstractSensorHandler aSH : activeEventHandlers) {
			assertTrue(aSH instanceof EventHandler80211);
		}
		
		appDep.sensorDependencies.sensorManager.addSensor(sD);
		HashMap<String, AbstractSensorDevice> sensorDeviceList = sMng.getSensorDeviceList();
		assertTrue(sensorDeviceList.containsKey(sD.getID()));
		assertEquals(sensorDeviceList.get(sD.getID()), sD);
		
		//manual calls! Normally this initialisation + sensor start is done via SensorManager initDevices(), startSensors() which calls init() {createRunnables()}, 
		//Runable.start() on all sensor device instances to create and start the Runnables!
		sD.createRunnables();
		List<Runnable> sensorRunnables = sD.getSensorRunnables();
		assertEquals(1, sensorRunnables.size());
		Runnable sensorRunnable = sensorRunnables.get(0);
		//SensorManager.startSensors()
		ThreadGroup sensorDeviceRunnables = new ThreadGroup(config.sensor80211.ID);
			new Thread(sensorDeviceRunnables, sensorRunnable).start();
		List<ThreadGroup> runningSensorsList = new ArrayList<ThreadGroup>(1);
		runningSensorsList.add(sensorDeviceRunnables);
		assertEquals(1, runningSensorsList.size());
		ThreadGroup threadGroup = runningSensorsList.get(0);
		assertTrue(threadGroup.activeCount() == 1);
		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			fail("Main Thread Interrupted while waiting");
			e.printStackTrace();
		}
		assertTrue(!systemRawDataQueue.isEmpty());
	}
}