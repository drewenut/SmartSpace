package de.ilimitado.smartspace;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import de.ilimitado.smartspace.sensor.sensorIMU.ScanResultIMU;

public class MotionDetector {
	private final LinkedBlockingQueue<ArrayList<ScanResultIMU>> motionEventsQueue = new LinkedBlockingQueue<ArrayList<ScanResultIMU>>();

	public LinkedBlockingQueue<ArrayList<ScanResultIMU>> getQueue() {
		return motionEventsQueue;
	}
}
