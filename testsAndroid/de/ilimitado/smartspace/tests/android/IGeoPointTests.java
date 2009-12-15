package de.ilimitado.smartspace.tests.android;

import org.json.JSONException;

import android.test.AndroidTestCase;
import de.ilimitado.smartspace.positioning.IGeoPoint;

public class IGeoPointTests extends AndroidTestCase{
	
	public void testIGeoPointConstructors() {
		IGeoPoint iGP = new IGeoPoint(1,2);
		assertNotNull(iGP);
		assertEquals(1, iGP.position_x);
		assertEquals(2, iGP.position_y);
		assertEquals("", iGP.name);
		assertEquals(-1, iGP.floor);
		
		IGeoPoint iGP2 = new IGeoPoint("sid",1,2);
		assertNotNull(iGP2);
		assertEquals(1, iGP2.position_x);
		assertEquals(2, iGP2.position_y);
		assertEquals("sid", iGP2.name);
		assertEquals(-1, iGP2.floor);
		
		IGeoPoint iGP3 = new IGeoPoint("sid",1,2,1);
		assertNotNull(iGP3);
		assertEquals(1, iGP3.position_x);
		assertEquals(2, iGP3.position_y);
		assertEquals("sid", iGP3.name);
		assertEquals(1, iGP3.floor);
	
		IGeoPoint iGP4 = new IGeoPoint(1,2,1);
		assertNotNull(iGP4);
		assertEquals(1, iGP4.position_x);
		assertEquals(2, iGP4.position_y);
		assertEquals("", iGP4.name);
		assertEquals(1, iGP4.floor);
	}
	
	public void testGeoPointEquals() {
		IGeoPoint iGP = new IGeoPoint("sid",1,1,0);
		IGeoPoint iGP2 = new IGeoPoint("sid",1,1,0);
		assertTrue(iGP.equals(iGP2));
		assertTrue(iGP2.equals(iGP));
		
		IGeoPoint iGP3 = new IGeoPoint("sid",1,1,1);
		assertFalse(iGP.equals(iGP3));
		assertFalse(iGP3.equals(iGP));
		
		IGeoPoint iGP4 = new IGeoPoint("sid",1,2,0);
		assertFalse(iGP.equals(iGP4));
		assertFalse(iGP4.equals(iGP));
		
		IGeoPoint iGP5 = new IGeoPoint("sid",2,1,0);
		assertFalse(iGP.equals(iGP5));
		assertFalse(iGP5.equals(iGP));
		
		IGeoPoint iGP6 = new IGeoPoint("sid2",1,1,0);
		assertFalse(iGP.equals(iGP6));
		assertFalse(iGP6.equals(iGP));
		
		IGeoPoint iGP7 = new IGeoPoint("sid2",2,2,1);
		assertFalse(iGP.equals(iGP7));
		assertFalse(iGP7.equals(iGP));
	}
	
	public void testCreateGeoPointFromStringUri() {
		String iGeoPointUri = "name:sid|igeo:1,2,1";
		IGeoPoint iGP = IGeoPoint.fromGeoUri(iGeoPointUri, IGeoPoint.CONVERT_TYPE_STRING);
		assertNotNull(iGP);
		assertEquals("sid", iGP.name);
		assertEquals(1, iGP.position_x);
		assertEquals(2, iGP.position_y);
		assertEquals((byte) 1, iGP.floor);
	}
	
	public void testCreateGeoPointFromUriException() {
		String iGeoPointUri = "name:sid|igeo:1,2,1";
		try {
			IGeoPoint.fromGeoUri(iGeoPointUri, (byte) 0x02);
			fail("IllegalArgumentExcepion expected");
		} catch (Exception e) { }
	}
	
	public void testCreateiGeoPointToJSON() {
		String jsonString = "{\"IGP_POS_X\":1,\"IGP_POS_Y\":1,\"IGP_NAME\":\"sid\",\"IGP_FLOOR\":0}";
		IGeoPoint iGP = new IGeoPoint("sid",1,1,0);
		try {
			assertEquals(jsonString, iGP.toJSON());
		} catch (JSONException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
	}
}
