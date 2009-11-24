package de.ilimitado.smartspace.tests.junitsuites;

import junit.framework.Test;
import junit.framework.TestSuite;
import de.ilimitado.smartspace.tests.android.PositionProviderTests;
import de.ilimitado.smartspace.tests.junit.AccuracyTests;
import de.ilimitado.smartspace.tests.junit.EuclideanDistanceTests;
import de.ilimitado.smartspace.tests.junit.WeightedGeoPointTest;

public class AllPositioningTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for de.ilimitado.smartspace: All Positioning tests");
		//$JUnit-BEGIN$
		suite.addTestSuite(AccuracyTests.class);
		suite.addTestSuite(WeightedGeoPointTest.class);
		suite.addTestSuite(PositionProviderTests.class);
		suite.addTestSuite(EuclideanDistanceTests.class);
		//$JUnit-END$
		return suite;
	}

}
