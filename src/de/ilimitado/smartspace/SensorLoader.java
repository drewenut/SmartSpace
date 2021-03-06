package de.ilimitado.smartspace;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import de.ilimitado.smartspace.persistance.ScanSampleDBPersistanceProvider;
import de.ilimitado.smartspace.registry.DataCommandProvider;
import de.ilimitado.smartspace.registry.Registry;
import de.ilimitado.smartspace.sensor.sensor80211.SensorDevice80211;

public class SensorLoader {

	private Dependencies dep;
	private ArrayList<AbstractSensorDevice> sensorList;

	@Deprecated
	public SensorLoader(Dependencies dep) {
		this.dep = dep;
		sensorList = new ArrayList<AbstractSensorDevice>();
		sensorList.add(new SensorDevice80211(dep));
	}
	
	public SensorLoader(Dependencies dep, ArrayList<AbstractSensorDevice> sensorDevs) {
		this.dep = dep;
		this.sensorList = sensorDevs;
	}
	
	public void loadSensors(){
		for(AbstractSensorDevice dev : sensorList) {
			
			if (dev.deviceAvailable()){
				dev.initSensorID();
				dev.initSensorName();
				dev.registerEvents(dep);
				dev.registerProcessorCommands((DataCommandProvider)dep.sensorDependencies.registry.get(Registry.SENSOR_DATA_CMD_PROVIDER));
				dev.registerDBPersistance((ScanSampleDBPersistanceProvider)dep.sensorDependencies.registry.get(Registry.SENSOR_SCANSAMPLE_DBPERS_PROVIDER));
				dep.sensorDependencies.sensorManager.addSensor(dev);
			}
		}
	}

	@Deprecated
	public void loadSensorsObsolete() throws LoadSensorException {
		ClassLoader classLoader = getClass().getClassLoader();
		URL baseURL = classLoader.getResource("de/ilimitado/smartspace/sensor");
		try {
			File[] listFiles = new File(baseURL.toURI()).listFiles();
			for (File dir : new File(baseURL.toURI()).listFiles()) {
				if (!dir.isDirectory())
					continue;
				String className = "de.ilimitado.smartspace.sensor." + dir.getName() + "."
						+ "SensorDevice";
				try {
					Class<?> sensorClass = classLoader.loadClass(className);

					try {
						Constructor<AbstractSensorDevice> constructor = (Constructor<AbstractSensorDevice>) sensorClass.getConstructor(dep.getClass());
						AbstractSensorDevice sensorInstance = (AbstractSensorDevice) constructor.newInstance(dep);
						
						if (sensorInstance.deviceAvailable()){
							sensorInstance.initSensorID();
							sensorInstance.initSensorName();
							sensorInstance.registerEvents(dep);
							sensorInstance.registerProcessorCommands((DataCommandProvider)dep.sensorDependencies.registry.get(Registry.SENSOR_DATA_CMD_PROVIDER));
							dep.sensorDependencies.sensorManager.addSensor(sensorInstance);
						}
					} catch (SecurityException e) {
						throw new LoadSensorException("SensorLoader Error SecurityException: ", e);
					} catch (NoSuchMethodException e) {
						throw new LoadSensorException("SensorLoader Error NoSuchMethodException: ", e);
					} catch (IllegalArgumentException e) {
						throw new LoadSensorException("SensorLoader Error IllegalArgumentException: ", e);
					} catch (InvocationTargetException e) {
						throw new LoadSensorException("SensorLoader Error InvocationTargetException: ", e);
					}

				} catch (ClassNotFoundException e) {
					throw new LoadSensorException(
							"SensorLoader Error : Class not found " + dir, e);
				} catch (InstantiationException e) {
					throw new LoadSensorException(
							"SensorLoader Error: Class could not be instantiated "
									+ dir, e);
				} catch (IllegalAccessException e) {
					throw new LoadSensorException(
							"SensorLoader Error: Illegal Access " + dir, e);
				}
			}
		} catch (URISyntaxException e) {
			throw new LoadSensorException("Plugin Error: URI Syntax is wrong ",
					e);
		}
	}

}
