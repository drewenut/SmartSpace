package de.ilimitado.smartspace.tests.junit.easymock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import de.ilimitado.smartspace.ScanSampleList;
import de.ilimitado.smartspace.SensorDataSample;
import de.ilimitado.smartspace.Syncable;
import de.ilimitado.smartspace.fsm.SynchronizerStrategy;

public class EventSyncronizerTestable implements Runnable, TestableEventSynchro{
	
	private final Lock lock = new ReentrantLock();
	private final Condition allThresholdReached  = lock.newCondition();
	private boolean syncActive = false;
	private SynchronizerStrategy strategy;
	private Map<String, List<List<?>>> sensorData;
	private HashMap<String, Syncable> sensorsReady;
	private volatile SensorDataSample result = null;
	private List<String> currentSyncSet= null;
	
	public EventSyncronizerTestable() {
		restoreInvariants();
	}

	private void restoreInvariants() {
		sensorsReady = new HashMap<String, Syncable>();
		sensorData = new HashMap<String, List<List<?>>>();
	}
	
	@Override
	public void run() {
		for(;;){
			lock.lock();
		     try {
		    	 if(!isReadyToCommit()) 
		    	   allThresholdReached.await();
		    	 HashMap<String, ScanSampleList> resultSamples = processSampleData(sensorData);
		    	 result = fusionateSampleData(resultSamples);
		    	 restoreInvariants();
		     } catch (InterruptedException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} finally {
		       lock.unlock();
		     }
		}
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
			commit();
		}
	}
	
	private boolean isReadyToCommit(){
		return currentSyncSet.containsAll(sensorsReady.keySet());
	}
	
	private void commit(){
		for(Syncable sHdl : sensorsReady.values()){
			sHdl.commitData();
		}
		allThresholdReached.signal();
	}

	public void setStrategy(SynchronizerStrategy sStr) {
		this.strategy = sStr;
	}
	
	public SensorDataSample getResult() {
		return result;
	}
	
	public void startSync(List<String> syncSet) {
		currentSyncSet = syncSet;
		syncActive = true;
	}
	
	public void stopSync() {
		syncActive  = false;
	}

	public void receiveSensorData(String eventType, List<List<?>> sensorEventData) {
		sensorData.put(eventType, sensorEventData);
	}
}