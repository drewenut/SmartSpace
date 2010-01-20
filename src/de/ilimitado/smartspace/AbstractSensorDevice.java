package de.ilimitado.smartspace;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import android.content.Context;
import de.ilimitado.smartspace.config.Configuration;
import de.ilimitado.smartspace.persistance.ScanSampleDBPersistanceProvider;
import de.ilimitado.smartspace.registry.DataCommandProvider;
import de.ilimitado.smartspace.registry.ScanSampleProvider;

public abstract class AbstractSensorDevice {

	protected static final String LOG_TAG = "AbstractSensorDevice";
	
	protected String SENSOR_ID;
	protected String SENSOR_NAME;

	protected Context androidCtx;
	protected LinkedBlockingQueue<SensorEvent<?>> systemRawDataQueue;
	protected List<Runnable> sensorRunnables;
	protected Configuration config;

	public AbstractSensorDevice(Dependencies appDeps) {
		this.androidCtx = appDeps.androidDependencies;
		this.sensorRunnables = new ArrayList<Runnable>();
		this.systemRawDataQueue = appDeps.sensorDependencies.systemRawDataQueue;
		this.config = Configuration.getInstance();
	}

	public String getID() {
		return SENSOR_ID;
	}
	
	public String getName() {
		return SENSOR_NAME;
	}
	
	public List<Runnable> getSensorRunnables() {
		return sensorRunnables;
	}
	
	public abstract boolean deviceAvailable();

	public abstract void initDevice();
	
	public abstract void createRunnables();

	public abstract void registerEvents(Dependencies dep);
	
	public abstract void registerScanSamples(ScanSampleProvider reg);
	
	public abstract void registerProcessorCommands(DataCommandProvider dCR);

	public abstract void registerDBPersistance(ScanSampleDBPersistanceProvider splDBPers);
	
	public abstract void initSensorName();
	
	public abstract void initSensorID();

}