package de.ilimitado.smartspace.tests.junitsuites;

import junit.framework.Test;
import junit.framework.TestSuite;
import de.ilimitado.smartspace.tests.android.ScanSample80211Tests;
import de.ilimitado.smartspace.tests.android.ScanSampleListTests;
import de.ilimitado.smartspace.tests.android.SensorDataSampleTests;
import de.ilimitado.smartspace.tests.junit.FPTTests;
import de.ilimitado.smartspace.tests.junit.LFPTTests;

public class AllLFPTTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for de.ilimitado.smartspace: All LFPT relevant tests");
		//$JUnit-BEGIN$
		suite.addTestSuite(ScanSampleListTests.class);
		suite.addTestSuite(SensorDataSampleTests.class);
		suite.addTestSuite(FPTTests.class);
		suite.addTestSuite(LFPTTests.class);		
		suite.addTestSuite(ScanSample80211Tests.class);
		//$JUnit-END$
		return suite;
	}

}
