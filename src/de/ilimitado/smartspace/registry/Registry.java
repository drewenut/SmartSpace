package de.ilimitado.smartspace.registry;

import de.ilimitado.smartspace.persistance.ScanSampleDBPersistanceProvider;


public final class Registry {

	public static final int SENSOR_HANDLER_PROVIDER = 0;
	public static final int SENSOR_DATA_CMD_PROVIDER = 1;
	public static final int SENSOR_SCANSAMPLE_PROVIDER = 2;
	public static final int SENSOR_SCANSAMPLE_DBPERS_PROVIDER = 3;
	
	private final SensorHandlerProvider sHdlReg;
	private final DataCommandProvider dCReg;
	private final ScanSampleProvider sSR;
	private final ScanSampleDBPersistanceProvider sSDBPers;
	
	public Registry() {
		sHdlReg = new SensorHandlerProvider();
		dCReg = new DataCommandProvider();
		sSR = new ScanSampleProvider();
		sSDBPers = new ScanSampleDBPersistanceProvider();
	}
	
	public Object get(int registryID) {
		switch (registryID) {
		case SENSOR_HANDLER_PROVIDER:
			return sHdlReg;
		case SENSOR_DATA_CMD_PROVIDER:
			return dCReg;
		case SENSOR_SCANSAMPLE_PROVIDER:
			return sSR;
		case SENSOR_SCANSAMPLE_DBPERS_PROVIDER:
			return sSDBPers;
		default:
			throw new RegistryException("Registry does not exist, did you use the public Registry contants Registry.SENSOR_<Name>_REG as registryID?");
		}
	}
	
}
