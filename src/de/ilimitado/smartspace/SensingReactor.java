package de.ilimitado.smartspace;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import de.ilimitado.smartspace.registry.Registry;
import de.ilimitado.smartspace.registry.SensorHandlerProvider;
import de.ilimitado.smartspace.utils.L;


public class SensingReactor implements Runnable{

	private static final String LOG_TAG = "SensingReactor";
	
	private SensorHandlerProvider sensorHandlerRegistry;
	private LinkedBlockingQueue<SensorEvent<?>> systemRawDataQueue;

	private AtomicBoolean isAlive = new AtomicBoolean(false);
	
	public SensingReactor(Registry reg, LinkedBlockingQueue<SensorEvent<?>> systemRawDataQueue) {
		this.sensorHandlerRegistry = (SensorHandlerProvider) reg.get(Registry.SENSOR_HANDLER_PROVIDER);
		this.systemRawDataQueue = systemRawDataQueue;
	}

	public void registerHandler(String eventID, AbstractSensorHandler handler) {
		sensorHandlerRegistry.addSensorHandler(eventID, handler);
	}

	public Set<String> getActiveEventList(){
		return sensorHandlerRegistry.keySet();
	}
	
	public Collection<AbstractSensorHandler> getActiveEventHandlers(){
		return (Collection<AbstractSensorHandler>) sensorHandlerRegistry.values();
	}
	
	public List<AbstractSensorHandler> getActiveSyncableEventHandlers(){
		List<AbstractSensorHandler> sensorHandlers = new ArrayList<AbstractSensorHandler>();
		for(AbstractSensorHandler handler : sensorHandlerRegistry.values()){
			if(handler.isSyncable())
				sensorHandlers.add(handler);
		}
		return sensorHandlers;
	}

	@SuppressWarnings("unchecked")
	public void handleEvents() {
		while (isAlive.get()) {
			try {
				SensorEvent<Collection<?>> sEvt = (SensorEvent<Collection<?>>) systemRawDataQueue.take();
				String eventType = sEvt.getEventType();
				AbstractSensorHandler eventHandler = getEventHandler(eventType);
				eventHandler.handleEvent(sEvt);
				L.d(LOG_TAG, "Triggered event handler: " + eventHandler.getAssociatedEventID());
			} catch (InterruptedException e) {
				//Do nothing, because head element was not removed and thus will be read next iteration.
				L.d(LOG_TAG, "Received interrupt for SensingReactor, shutting down");
				break;
			}
		}
	}

	private AbstractSensorHandler getEventHandler(String eventID)
			throws SensorEventHandlerDoesNotExitException {
		if (sensorHandlerRegistry.isRegistered(eventID))
			return sensorHandlerRegistry.getSensorHandler(eventID);
		else
			throw new SensorEventHandlerDoesNotExitException();
	}

	@Override
	public void run() {
		handleEvents();
	}

	public void startDispatching() {
		isAlive.set(true);		
	}
	
	public void stopDispatching() {
		isAlive.set(false);
	}
}
