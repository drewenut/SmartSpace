package de.ilimitado.smartspace.tests.junit.dataprocessors;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import de.ilimitado.smartspace.ScanSample;
import de.ilimitado.smartspace.ScanSampleList;
import de.ilimitado.smartspace.sensor.sensor80211.MeanCommand80211;
import de.ilimitado.smartspace.sensor.sensor80211.ScanResult80211;
import de.ilimitado.smartspace.sensor.sensor80211.ScanSample80211;

public class MeanCommand80211Tests extends TestCase{

	private MeanCommand80211 mCmd;
	private ScanSampleList stdOut;
	private ArrayList<List<ScanResult80211>> stdIn;

	@Before
	public void setUp() throws Exception {
		stdIn = new ArrayList<List<ScanResult80211>>(3); 
		stdOut = new ScanSampleList();
		mCmd = new MeanCommand80211();
	}

	@Test
	public void testComputeMeanCollectionOfMockScanSampleList() {
		writeMockSampleListToStdIn(10, 5, false);
		mCmd.setStdIn(stdIn);
		mCmd.setStdOut(stdOut);
		mCmd.execute();
		assertEquals(5, stdIn.size());
		assertEquals(10, stdOut.size());
	}
	
	@Test
	public void testComputeMeanCollectionOfMockScanSampleListDifferentSizes() {
		List<List<ScanResult80211>> stdIn = new ArrayList<List<ScanResult80211>>(3);
		
		List<ScanResult80211> scSplsList1 = new ArrayList<ScanResult80211>();
		scSplsList1.add(new ScanResult80211("res0", "00:00:00:00:00", "caps", -23, 2410));
		scSplsList1.add(new ScanResult80211("res1", "00:00:00:00:01", "caps", -60, 2410));
		scSplsList1.add(new ScanResult80211("res2", "00:00:00:00:02", "caps", -32, 2410));
		scSplsList1.add(new ScanResult80211("res3", "00:00:00:00:03", "caps", -80, 2410));
		scSplsList1.add(new ScanResult80211("res4", "00:00:00:00:04", "caps", -41, 2410));
		
		List<ScanResult80211> scSplsList2 = new ArrayList<ScanResult80211>();
		scSplsList2.add(new ScanResult80211("res0", "00:00:00:00:00", "caps", -22, 2410));
		scSplsList2.add(new ScanResult80211("res1", "00:00:00:00:01", "caps", -74, 2410));
		scSplsList2.add(new ScanResult80211("res2", "00:00:00:00:02", "caps", -65, 2410));
		
		List<ScanResult80211> scSplsList3 = new ArrayList<ScanResult80211>();
		scSplsList3.add(new ScanResult80211("res1", "00:00:00:00:01", "caps", -37, 2410));
		scSplsList3.add(new ScanResult80211("res2", "00:00:00:00:02", "caps", -43, 2410));
		scSplsList3.add(new ScanResult80211("res3", "00:00:00:00:03", "caps", -61, 2410));
		scSplsList3.add(new ScanResult80211("res4", "00:00:00:00:04", "caps", -80, 2410));
		
		stdIn.add(scSplsList1);
		stdIn.add(scSplsList2);
		stdIn.add(scSplsList3);
		
		mCmd.setStdIn(stdIn);
		mCmd.setStdOut(stdOut);
		mCmd.execute();
		
		boolean containsRES0 = false;
		boolean containsRES1 = false;
		boolean containsRES2 = false;
		boolean containsRES3 = false;
		boolean containsRES4 = false;
		
		
		assertEquals(5, stdOut.size());
		
		for (ScanSample scnSpl : stdOut) {
			ScanSample80211 scnSpl80211 = (ScanSample80211) scnSpl;
			if(scnSpl80211.MAC == "00:00:00:00:00") {
				containsRES0 = true;
				assertEquals(-22.0, scnSpl80211.rss.mean);
			}
			else if(scnSpl80211.MAC == "00:00:00:00:01")  {
				containsRES1 = true;
				assertEquals(-57.0, scnSpl80211.rss.mean);
			}
			else if(scnSpl80211.MAC == "00:00:00:00:02")  {
				containsRES2 = true;
				assertEquals(-46.0, scnSpl80211.rss.mean);
			}
			else if(scnSpl80211.MAC == "00:00:00:00:03")  {
				containsRES3 = true;
				assertEquals(-70.0, scnSpl80211.rss.mean);
			}
			else if(scnSpl80211.MAC == "00:00:00:00:04")  {
				containsRES4 = true;
				assertEquals(-60.0, scnSpl80211.rss.mean);
			}
		}
		
		assertTrue(containsRES0 
				   && containsRES1 
				   && containsRES2
				   && containsRES3
				   && containsRES4);
	}
	
	private void writeMockSampleListToStdIn(int rss, int listSize, boolean rand) {
		for(int i=0; i< listSize; i++){
			int count = 10;
			if(rand){
				count = (int) ((Math.random() * 10000) % 9);
			}
			
			List<ScanResult80211> scSplsColl = new ArrayList<ScanResult80211>();
			for(int j=0; j<count; j++){
				scSplsColl.add(new ScanResult80211("res" + j, "00:00:00:00:0" + j, "caps",rss, 2410));
			}
			stdIn.add(scSplsColl);
		}
	}
}
