package de.ilimitado.smartspace.fsm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import de.ilimitado.smartspace.ScanSampleList;
import de.ilimitado.smartspace.SensorDataSample;
import de.ilimitado.smartspace.dataprocessors.DataProcessor;
import de.ilimitado.smartspace.utils.L;

public abstract class SensingStrategy extends SynchronizerStrategy {
	
	protected static final String LOG_TAG = "SensingStrategy";
	
	protected final HashMap<String, DataProcessor<ScanSampleList>> sDataProcessors;
	
	public SensingStrategy(List<String> syncSet, HashMap<String, DataProcessor<ScanSampleList>> dataProcessorList) {
		super(syncSet);
		this.sDataProcessors = dataProcessorList;
	}

	@Override
	public HashMap<String, ScanSampleList> processData(Map<String, List<List<?>>> sensorData) {
		L.startT(LOG_TAG);
		
		HashMap<String, ScanSampleList> resultSamples = new HashMap<String, ScanSampleList>();
		ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
		HashMap<String, Future<DataProcessor<ScanSampleList>>> tasks = new HashMap<String, Future<DataProcessor<ScanSampleList>>>(sensorData.size());
		
		for (String scannerID : sDataProcessors.keySet()) {
			if(sensorData.containsKey(scannerID)) {
				DataProcessor<ScanSampleList> sDPr = sDataProcessors.get(scannerID);
				List<List<?>> data = sensorData.get(scannerID);
		    	sDPr.readyProcessor(data);
		    	Future<DataProcessor<ScanSampleList>> future = threadPool.submit(sDPr, sDPr);
			    tasks.put(scannerID, future);
			}
	    }
	    
	    try {
	    	for(String scannerID : tasks.keySet()){
		    	Future<DataProcessor<ScanSampleList>> future = tasks.get(scannerID);
		    	DataProcessor<ScanSampleList> dataProcessorResult = future.get();
		    	resultSamples.put(scannerID, dataProcessorResult.getProcessedData());
	    	}
	      threadPool.shutdownNow();
	    } catch (ExecutionException e) {
	    	//TODO must be fixed so that state self transition is triggered  
	    	e.printStackTrace();	
	    	L.e(LOG_TAG, "ExecutionException");
	    } catch (InterruptedException e) {
	    	//TODO must be fixed so that state self transition is triggered
	    	e.printStackTrace();
	    	L.e(LOG_TAG,"InterruptedException");
	    }
	    
		L.stopT(LOG_TAG, "TIME processData(): ");
		
	    return resultSamples;
	}
	
	@Override
	public abstract SensorDataSample fusionate(HashMap<String, ScanSampleList> resultSamples);
	
	@Override
	public abstract void deploySampleDataSample(SensorDataSample spl);

}
