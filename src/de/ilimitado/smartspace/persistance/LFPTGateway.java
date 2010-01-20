package de.ilimitado.smartspace.persistance;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import de.ilimitado.smartspace.LFPT;
import de.ilimitado.smartspace.RadioMap;
import de.ilimitado.smartspace.config.Configuration;
import de.ilimitado.smartspace.utils.L;

public class LFPTGateway implements Gateway{

	private static final String LOG_TAG = "LFPTGateway";
	
	private final long REFRESH_INTERVAL;
	private final int BUFFER_THRESHOLD;
	
	private AtomicBoolean isAlive = new AtomicBoolean(false);
	private long nextLoadTime = 0;
	private LinkedBlockingQueue<LFPT> queue = new LinkedBlockingQueue<LFPT>(); 

	
	private LFPTPersistanceStrategy strategy = null;

	private Thread thread;
	
	public LFPTGateway() {
		REFRESH_INTERVAL = Configuration.getInstance().persistence.refreshInterval;
		BUFFER_THRESHOLD = Configuration.getInstance().persistence.bufferSize;
	}
	
	public RadioMap getRadioMap() {
		return strategy.getRadioMap();
	}

	public void loadRadioMap() {
		strategy.loadRadioMap();
		long currTime = System.currentTimeMillis();
		nextLoadTime = currTime + REFRESH_INTERVAL;
		L.d(LOG_TAG, "(Re-)Loaded radio map from DB");
	}
	
	public boolean isLoaded(){
		long currTime = System.currentTimeMillis();
		return currTime > nextLoadTime ? false : true;
	}

	public void save(Object fingerprint) {
		try {
			queue.put( (LFPT) fingerprint);
			L.d(LOG_TAG, "Adding LFPT to buffer, now waiting: " + queue.size());
		} catch (InterruptedException e) {
	    	//TODO must be fixed so that state self transition is triggered
	    	e.printStackTrace();
	    	L.e(LOG_TAG, e.getMessage());
		}
	}
	
	@Override
	public void setStrategy(Object strategy) {
		this.strategy = (LFPTPersistanceStrategy) strategy;
	}

	public void startGateway() {
		isAlive.set(true);
		thread = new Thread(new Runnable() {
 
			@Override
			public void run() {
				L.startC(LOG_TAG);
				while(isAlive.get()){
					try {
						LFPT lfpt = queue.take();
						for(int lfptBufferCount=0; lfptBufferCount<BUFFER_THRESHOLD; lfptBufferCount++){
							LFPT lfptToMerge = queue.take();
							lfpt.merge(lfptToMerge);
						}
						strategy.save(lfpt);
						L.incC(LOG_TAG, "Saved LFPT to DB: ");
					} catch (InterruptedException e) {
						L.d(LOG_TAG, "Received interrupt for LFPTGateway, shutting down");
						break;
					}
				}
			}
		},"LFPTGateway");
		thread.start();
	}
	
	public void stopGateway() {
		isAlive.set(false);
		thread.interrupt();
	}

}