package de.ilimitado.smartspace.config;

public class PersistanceConfig{
	public final int lfptPersistanceBufferSize;
	public final int lfptPersistanceMode;
	public final long refreshInterval;
	public final String lfptPersistanceDBName;
	public PersistanceConfig(int lfptPM, long refreshInterval, String lfptPersistanceDBName, int lfptPersistanceBufferSize) {
		this.lfptPersistanceMode = lfptPM;
		this.refreshInterval = refreshInterval;
		this.lfptPersistanceDBName = lfptPersistanceDBName;
		this.lfptPersistanceBufferSize = lfptPersistanceBufferSize;
	}
}