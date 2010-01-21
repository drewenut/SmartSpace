package de.ilimitado.smartspace;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Looper;
import de.ilimitado.smartspace.sensor.sensorIMU.ScanResultIMU;
import de.ilimitado.smartspace.utils.L;

public class MotionDetector implements Runnable{
	
	private static final String LOG_TAG = "MotionDetector";

	//TODO add to configurations
	private static final double MTN_SENSITIVITY = 0.4d;
	private static final int QUEUE_CAPACITY = 4;
	
	private AtomicBoolean isAlive = new AtomicBoolean(false);
	private final LinkedBlockingQueue<ArrayList<ScanResultIMU>> mtnDataQueue = new LinkedBlockingQueue<ArrayList<ScanResultIMU>>();
	private final LinkedList<Boolean> mtnStream = new LinkedList<Boolean>();
	
	private ArrayList<MotionListener> mtnListeners = new ArrayList<MotionListener>();
	double currentAcc = 0d;

	public LinkedBlockingQueue<ArrayList<ScanResultIMU>> getQueue() {
		return mtnDataQueue;
	}
	
	@Override
	public void run() {
		Looper.prepare();
		while(isAlive.get()){
		     try {
		    	 ArrayList<ScanResultIMU> mtnEvts = mtnDataQueue.take();
		    	 processMotionData(mtnEvts);
		    	 enqueueMotionEvent(translateMotion());
		    	 notifyMotionDetected(detectMotion());
		    	 L.d(LOG_TAG, "Motion Queue item processed");
		     } catch (InterruptedException e) {
		    	 L.d(LOG_TAG, "Received interrupt for " + LOG_TAG + ", shutting down");
		    	 break;
		     }
		}
	}
	
	private void processMotionData(ArrayList<ScanResultIMU> motionEvents) {
			
        float[] R = new float[16];
        float[] I = new float[16];
		
		float[] accData = null;
		float[] magnFieldData = null;
		
		for (ScanResultIMU event : motionEvents) {

			if(event.sensorType == Sensor.TYPE_ACCELEROMETER)
				accData = event.values;
			else if(event.sensorType == Sensor.TYPE_MAGNETIC_FIELD)
				magnFieldData = event.values;
			
			if(accData != null && magnFieldData != null){
			
				SensorManager.getRotationMatrix(R, I, accData, magnFieldData);
				
	        	//Substract gravity from every axis
				float xAccWithoutG = accData[0] - R[8] * SensorManager.GRAVITY_EARTH;
				float yAccWithoutG = accData[1] - R[9] * SensorManager.GRAVITY_EARTH;
				float zAccWithoutG = accData[2] - R[10] * SensorManager.GRAVITY_EARTH;

				//Vector Product to get acceleration sqrt(x² + y² + z²)
				double accWithoutG = Math.sqrt(xAccWithoutG * xAccWithoutG + yAccWithoutG * yAccWithoutG + zAccWithoutG * zAccWithoutG);
				
				//Acceleration on all axis
				currentAcc = (currentAcc + accWithoutG)/2;
				L.d(LOG_TAG, "Current total acceleration: " + currentAcc);
			}
		}
	}

	private Boolean translateMotion() {
		return currentAcc >= MTN_SENSITIVITY ? true : false;
	}
	
	private boolean detectMotion() {
		byte mtnDetects = 0;
		for(Boolean mtn : mtnStream) {
			L.d(LOG_TAG, "Motion Stream array Value " + mtnDetects + " : " + Boolean.toString(mtn));
			if(mtn == true)
				++mtnDetects;
		}
		return mtnDetects >= QUEUE_CAPACITY/2 ? true : false;
	}

	private void enqueueMotionEvent(Boolean mtn) {
		L.d(LOG_TAG, "Motion Stream size: " + mtnStream.size());
		if(mtnStream.size() >= QUEUE_CAPACITY) {
			mtnStream.poll();
		}
		mtnStream.offer(mtn);
	}
	
	public void registerListener(MotionListener mL){
		mtnListeners.add(mL);
	}
	
	public void unregisterListener(MotionListener mL){
		if(mtnListeners.contains(mL))
			mtnListeners.remove(mL);
	}
	
	public boolean listenersEmpty(){
		return mtnListeners.isEmpty();
	}
	
	private void notifyMotionDetected(boolean mtn){
		if(mtn)
			L.sd(LOG_TAG, "motion detected");
		for(MotionListener listener : mtnListeners){
			listener.onMotionDetected(mtn);
		}
	}
	
	public void startMotionDetection() {
		isAlive.set(true);		
	}
	
	public void stopMotionDetection() {
		isAlive.set(false);
	}
}
