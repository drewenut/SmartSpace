package de.ilimitado.smartspace.tests.android;

import android.test.AndroidTestCase;
import de.ilimitado.smartspace.persistance.FileGateway;
import de.ilimitado.smartspace.persistance.PersistanceManager;
import de.ilimitado.smartspace.sensor.sensor80211.DataBuffer;

public class FileManagerTests extends AndroidTestCase {
	
	private PersistanceManager persMngr;

	public void setUp() {
		persMngr = new PersistanceManager(getContext());
	}
	
	public void testReadWriteFile() {
		String fileName = "FileManagerTests";
		DataBuffer db = new DataBuffer(fileName);
		String fileContent = "timestamp; AP1; AP2;";
		db.buffer = new StringBuffer().append(fileContent);
		persMngr.save(PersistanceManager.GATEWAY_FILE_SYSTEM, db);
		FileGateway fileGW = (FileGateway) persMngr.get(PersistanceManager.GATEWAY_FILE_SYSTEM);
		assertNotNull(fileGW);
		StringBuffer result = fileGW.load(fileName);
		assertEquals(fileContent, result.toString());
	}
}