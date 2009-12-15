package de.ilimitado.smartspace;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import android.os.Looper;
import android.util.Log;

import de.ilimitado.smartspace.fsm.SynchronizerStrategy;
import de.ilimitado.smartspace.utils.L;

public class EventSynchronizer implements Runnable{
	
	private static final String LOG_TAG = "EventSynchronizer";
	
	private AtomicBoolean isAlive = new AtomicBoolean(false);
	private LinkedBlockingQueue<Map<String, List<List<?>>>> sensorDataQueue = new LinkedBlockingQueue<Map<String, List<List<?>>>>();
	private SynchronizerStrategy strategy;
	private Map<String, List<List<?>>> sensorData;
	private HashMap<String, Syncable> sensorsReady;
	private volatile SensorDataSample result = null;
	private List<String> currentSyncSet= null;
	
	public EventSynchronizer() {
		restoreInvariants();
	}

	private void restoreInvariants() {
		sensorsReady = new HashMap<String, Syncable>();
		sensorData = new HashMap<String, List<List<?>>>();
	}
	
	@Override
	public void run() {
		Looper.prepare();
		while(isAlive.get()){
		     try {
		    	 Map<String, List<List<?>>> sensorDataToProcess = sensorDataQueue.take();
		    	 HashMap<String, ScanSampleList> resultSamples = processSampleData(sensorDataToProcess);
		    	 result = fusionateSampleData(resultSamples);
		    	 deploySampleDataSample(result);
		    	 L.d(LOG_TAG, "Queue item processed");
		    	 restoreInvariants();
		     } catch (InterruptedException e) {
		    	 restoreInvariants();
		    	 L.d(LOG_TAG, "Received interrupt for EventSynchronizer, shutting down");
		    	 break;
		     }
		}
	}
	
	private void deploySampleDataSample(SensorDataSample sDSpl) {
		strategy.deploySampleDataSample(sDSpl);
	}

	private HashMap<String, ScanSampleList> processSampleData(Map<String, List<List<?>>> sensorData) {
		return strategy.processData(sensorData);
	}
	
	private SensorDataSample fusionateSampleData(HashMap<String, ScanSampleList> resultSamples){
		return strategy.fusionate(resultSamples);
	}

	public synchronized void isReady(String eventType, Syncable sensorHandler) {
		sensorsReady.put(eventType, sensorHandler);
		if(isReadyToCommit()){
			L.d(LOG_TAG, "All sensor handlers ready to commit data");
			commit();
		}
	}
	
	private boolean isReadyToCommit(){
		Set<String> sensorsReady = this.sensorsReady.keySet();
		return sensorsReady.containsAll(currentSyncSet);
	}
	
	private void commit(){
		for(Syncable sHdl : sensorsReady.values()){
			sHdl.commitData();
		}
		try {
			sensorDataQueue.put(sensorData);
			L.d(LOG_TAG, "Sensor data queued for sensor-fusion processing, sensorDataQueue size: " + sensorDataQueue.size());
		} catch (InterruptedException e) {
			//We just drop sensorData an invariants will be restored in next statement.
			e.printStackTrace();
		}
		restoreInvariants();
	}

	public void setStrategy(SynchronizerStrategy sStr) {
		this.strategy = sStr;
		currentSyncSet = strategy.getSyncSet();
	}
	
	public SensorDataSample getResult() {
		return result;
	}

	public void startSync() {
		isAlive.set(true);
	}
	
	public void stopSync() {
		isAlive.set(false);
	}

	public synchronized void receiveSensorData(String eventType, List<List<?>> sensorEventData) {
		sensorData.put(eventType, sensorEventData);
	}
}