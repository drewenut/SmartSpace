package de.ilimitado.smartspace.config;

public class ConfigPersistence{
	public final String dbName;
	public final int mode;
	public final int bufferSize;
	public final long refreshInterval;
	public ConfigPersistence(String persistanceDBName, int persistenceMode, int persistanceBufferSize, long refreshInterval) {
		this.dbName = persistanceDBName;
		this.mode = persistenceMode;
		this.bufferSize = persistanceBufferSize;
		this.refreshInterval = refreshInterval;
	}
}