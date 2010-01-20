package de.ilimitado.smartspace.tests.junit;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import de.ilimitado.smartspace.FPT;
import de.ilimitado.smartspace.SensorDataSample;
import de.ilimitado.smartspace.persistance.ValueMap;
import de.ilimitado.smartspace.persistance.ValueMapContainer;
import de.ilimitado.smartspace.tests.junit.config.MockConfigTranslator;

public class FPTTests extends TestCase{

	private static final int QUANTIZED_ORIENTATION = 90;
	private static final double ORIENTATION = 98.76;
	private static final int TIMESTAMP = 123456789;

	@Before
	public void setUp() throws Exception {
		MockConfigTranslator.getInstance().translate();
	}

	@Test
	public void testFPTConstructor(){
		FPT fpt = new FPT(TIMESTAMP, ORIENTATION);
		assertEquals(TIMESTAMP, fpt.getCreationTime());
		assertEquals(QUANTIZED_ORIENTATION, fpt.getQuantizedOrientation());
	}
	
	@Test
	public void testToValueWithoutScanSamples() {
		FPT fpt = new FPT(TIMESTAMP, ORIENTATION);
		ValueMapContainer values = fpt.toValue();
		assertTrue("ValueMapContainer must contain default data value map", values.containsKey(SensorDataSample.VALUE_MAP_DEFAULT_DATA));
		
		ValueMap valueMap = (ValueMap) values.get(SensorDataSample.VALUE_MAP_DEFAULT_DATA);
		assertTrue("ValueMap must contain timestamp", valueMap.containsKey(SensorDataSample.VALUE_CREATION_TIME));
		assertTrue("ValueMap must contain orientation", valueMap.containsKey(FPT.VALUE_ORIENTATION));
		assertEquals(TIMESTAMP, valueMap.getAsLong(SensorDataSample.VALUE_CREATION_TIME));
		assertEquals(QUANTIZED_ORIENTATION, valueMap.getAsLong(FPT.VALUE_ORIENTATION));
	}
	
	@Test
	public void testQuantizedOrientation() {
		FPT fpt = new FPT(TIMESTAMP, 0);
		assertEquals(0, fpt.getQuantizedOrientation());
		fpt = new FPT(TIMESTAMP, 45);
		assertEquals(0, fpt.getQuantizedOrientation());
		fpt = new FPT(TIMESTAMP, 89);
		assertEquals(0, fpt.getQuantizedOrientation());
		fpt = new FPT(TIMESTAMP, 90);
		assertEquals(90, fpt.getQuantizedOrientation());
		fpt = new FPT(TIMESTAMP, 135);
		assertEquals(90, fpt.getQuantizedOrientation());
		fpt = new FPT(TIMESTAMP, 179);
		assertEquals(90, fpt.getQuantizedOrientation());
		fpt = new FPT(TIMESTAMP, 180);
		assertEquals(180, fpt.getQuantizedOrientation());
		fpt = new FPT(TIMESTAMP, 225);
		assertEquals(180, fpt.getQuantizedOrientation());
		fpt = new FPT(TIMESTAMP, 270);
		assertEquals(270, fpt.getQuantizedOrientation());
		fpt = new FPT(TIMESTAMP, 315);
		assertEquals(270, fpt.getQuantizedOrientation());
		fpt = new FPT(TIMESTAMP, 359);
		assertEquals(270, fpt.getQuantizedOrientation());
		fpt = new FPT(TIMESTAMP, 360);
		assertEquals(0, fpt.getQuantizedOrientation());
	}

	@Test
	public void testMerge(){
		FPT fpt1 = new FPT(123456789, 20){};
		FPT fpt2 = new FPT(987654321, 24){};
		fpt1.merge(fpt2);
		assertEquals(987654321, fpt1.getCreationTime());
		assertEquals(0, fpt1.getQuantizedOrientation());
		
		fpt1 = new FPT(123456789, 20){};
		fpt2 = new FPT(987654321, 24){};
		fpt2.merge(fpt1);
		assertEquals(987654321, fpt2.getCreationTime());
		assertEquals(0, fpt2.getQuantizedOrientation());
	}
}
