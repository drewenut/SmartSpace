package de.ilimitado.smartspace.tests.junit;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import de.ilimitado.smartspace.positioning.Accuracy;

public class AccuracyTests extends TestCase{
     
	private int mockAcc;
	
     @Before()
     public void setUp() {
    	 mockAcc = 5; 
     }

	@Test()
	 public void testAccuracy() {
		Accuracy acc = new Accuracy(mockAcc);
		assertEquals(5, acc.accuracy);
	}
}