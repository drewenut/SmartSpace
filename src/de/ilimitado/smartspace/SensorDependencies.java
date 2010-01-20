package de.ilimitado.smartspace;

import java.util.concurrent.LinkedBlockingQueue;

import de.ilimitado.smartspace.registry.Registry;

public class SensorDependencies {

	public final SensingReactor reactor;
	public final SensorManager sensorManager;
	public final EventSynchronizer eventSnychronizer;
	public final LinkedBlockingQueue<SensorEvent<?>> systemRawDataQueue;
	public final Registry registry;

	public SensorDependencies(SensingReactor sR, EventSynchronizer sSync, SensorManager sMng,
			LinkedBlockingQueue<SensorEvent<?>> sysRDQueue, Registry reg) {
		this.reactor = sR;
		this.sensorManager = sMng;
		this.eventSnychronizer = sSync;
		this.systemRawDataQueue = sysRDQueue;
		this.registry = reg;
	}

}
