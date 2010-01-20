package de.ilimitado.smartspace.persistance;

import de.ilimitado.smartspace.LFPT;
import de.ilimitado.smartspace.android.LFPTDBPersistance;
import de.ilimitado.smartspace.registry.AbstractRegistryProvider;

public class ScanSampleDBPersistanceProvider extends AbstractRegistryProvider<String, ScanSampleDBPersistance> {
	private static final long serialVersionUID = 1L;
	
	public ScanSampleDBPersistanceProvider() {
		putItem(LFPT.VALUE_MAP_DEFAULT_DATA, LFPTDBPersistance.class);
	}
	
}
