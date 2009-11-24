package de.ilimitado.smartspace.tests.junit;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import de.ilimitado.smartspace.FPT;
import de.ilimitado.smartspace.IGeoPoint;
import de.ilimitado.smartspace.LFPT;
import de.ilimitado.smartspace.SensorDataSample;
import de.ilimitado.smartspace.persistance.ValueMap;
import de.ilimitado.smartspace.persistance.ValueMapContainer;
import de.ilimitado.smartspace.tests.junit.config.MockConfigTranslator;

public class LFPTTests extends TestCase{

	private static final int POS_Y = 2;
	private static final int POS_X = 1;
	private static final int QUANTIZED_ORIENTATION = 90;
	private static final double ORIENTATION = 98.76;
	private static final int TIMESTAMP = 123456789;

	@Before
	public void setUp() throws Exception {
		MockConfigTranslator.getInstance().translate();
	}

	@Test
	public void testFPTConstructor(){
		LFPT lfpt = new LFPT(TIMESTAMP, ORIENTATION, new IGeoPoint(POS_X, POS_Y));
		assertEquals(TIMESTAMP, lfpt.getCreationTime());
		assertEquals(QUANTIZED_ORIENTATION, lfpt.getQuantizedOrientation());
		assertEquals(POS_X, lfpt.getIndoorGP().position_x);
		assertEquals(POS_Y, lfpt.getIndoorGP().position_y);
	}
	
	@Test
	public void testToValueWithoutScanSamples() {
		LFPT lfpt = new LFPT(TIMESTAMP, ORIENTATION, new IGeoPoint(POS_X, POS_Y));
		ValueMapContainer values = lfpt.toValue();
		assertTrue("ValueMapContainer must contain default data value map", values.containsKey(SensorDataSample.VALUE_MAP_DEFAULT_DATA));
		
		ValueMap valueMap = (ValueMap) values.get(SensorDataSample.VALUE_MAP_DEFAULT_DATA);
		assertTrue("ValueMap must contain timestamp", valueMap.containsKey(SensorDataSample.VALUE_CREATION_TIME));
		assertTrue("ValueMap must contain orientation", valueMap.containsKey(FPT.VALUE_ORIENTATION));
		assertTrue("ValueMap must contain pos. x", valueMap.containsKey(LFPT.VALUE_POS_X));
		assertTrue("ValueMap must contain pos. y", valueMap.containsKey(LFPT.VALUE_POS_Y));
		assertEquals(TIMESTAMP, valueMap.getAsLong(SensorDataSample.VALUE_CREATION_TIME));
		assertEquals(QUANTIZED_ORIENTATION, valueMap.getAsLong(FPT.VALUE_ORIENTATION));
		assertEquals(POS_X, valueMap.getAsInteger(LFPT.VALUE_POS_X));
		assertEquals(POS_Y, valueMap.getAsInteger(LFPT.VALUE_POS_Y));
	}
	
	@Test
	public void testMerge(){
		LFPT lFpt1 = new LFPT(123456789, 20, new IGeoPoint(1, 2));
		LFPT lFpt2 = new LFPT(987654321, 24, new IGeoPoint(1, 2));
		lFpt1.merge(lFpt2);
		assertEquals(987654321, lFpt1.getCreationTime());
		assertEquals(0, lFpt1.getQuantizedOrientation());
		
		lFpt1 = new LFPT(123456789, 20, new IGeoPoint(1, 2));
		lFpt2 = new LFPT(987654321, 24, new IGeoPoint(1, 2));
		lFpt2.merge(lFpt1);
		assertEquals(987654321, lFpt2.getCreationTime());
		assertEquals(0, lFpt2.getQuantizedOrientation());
	}
	
	@Test
	public void testGetID(){
		LFPT lFpt = new LFPT(123456789, 20, new IGeoPoint(1, 2));
		assertEquals("120", lFpt.getID());
	}
	
}