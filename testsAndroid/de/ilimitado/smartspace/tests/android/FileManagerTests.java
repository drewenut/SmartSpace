package de.ilimitado.smartspace.tests.android;

import java.util.ArrayList;
import java.util.List;

import android.test.AndroidTestCase;
import de.ilimitado.smartspace.EventSynchronizer;
import de.ilimitado.smartspace.SensorEvent;
import de.ilimitado.smartspace.persistance.PersistanceManager;
import de.ilimitado.smartspace.sensor.sensor80211.DataBuffer;
import de.ilimitado.smartspace.tests.junit.MockSensorHandler;
import de.ilimitado.smartspace.tests.junit.MockSyncStrategy;
import de.ilimitado.smartspace.tests.junit.config.MockConfigTranslator;

public class FileManagerTests extends AndroidTestCase {
	
	private PersistanceManager persMngr;

	public void setUp() {
		persMngr = new PersistanceManager(getContext());
	}
	
	public void testwriteFile() {
		DataBuffer db = new DataBuffer("FileManagerTests");
		db.buffer = new StringBuffer().append("timestamp; AP1; AP2;");
		persMngr.save(PersistanceManager.GATEWAY_FILE_SYSTEM, db);
		assertEquals(6, eventHandler1.getEventQueueSize());
		assertEquals(7, eventHandler2.getEventQueueSize());
		assertEquals(1, eventHandler1.getCommitDataCalls());
		assertEquals(1, eventHandler2.getCommitDataCalls());
		assertEquals(1, syncStrategy.getProcessDataCalls());
		assertEquals(1, syncStrategy.getFusionateSamplesCalls());
		assertEquals(1, syncStrategy.getDeploySampleDataCalls());
		
		eventSync.stopSync();
		evtSyncWorker.interrupt();
	}
}