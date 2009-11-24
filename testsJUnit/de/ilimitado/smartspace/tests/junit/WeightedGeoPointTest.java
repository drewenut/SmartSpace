package de.ilimitado.smartspace.tests.junit;

import junit.framework.TestCase;

import org.junit.Test;

import de.ilimitado.smartspace.IGeoPoint;
import de.ilimitado.smartspace.positioning.WeightedIGeoPoint;

public class WeightedGeoPointTest extends TestCase{

	private static final int IS_EQUAL = 0;
	private static final int IS_BIGGER = 5;
	private static final int IS_SMALLER= -10;
	
	@Test
	public void testPositionProvider80211TestInstance() {
		IGeoPoint iGP = new IGeoPoint(1,1);
		
		WeightedIGeoPoint wGP1 = new WeightedIGeoPoint(iGP, 10);
		
		WeightedIGeoPoint wGP2 = new WeightedIGeoPoint(iGP, 10);
		
		WeightedIGeoPoint wGP3 = new WeightedIGeoPoint(iGP, 20);
		
		WeightedIGeoPoint wGP4 = new WeightedIGeoPoint(iGP, 5);
		
		assertEquals(IS_EQUAL, wGP1.compareTo(wGP2));
		assertEquals(IS_SMALLER, wGP1.compareTo(wGP3));
		assertEquals(IS_BIGGER, wGP1.compareTo(wGP4));
	}
}

