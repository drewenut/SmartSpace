package de.ilimitado.smartspace.sensing;

import java.util.Collection;
import java.util.HashMap;

import android.util.Log;

import de.ilimitado.smartspace.ScanSampleList;
import de.ilimitado.smartspace.config.Configuration;
import de.ilimitado.smartspace.registry.DataCommandProvider;
import de.ilimitado.smartspace.registry.RegistryProviderException;

public class DataProcessorResolver {

	private static final DataProcessorResolver instance = null;
	private static final String LOG_TAG = "DataProcessorResolver";
	private DataProcessorsMap<String, HashMap<String, Boolean>> activeDPs;
	private DataCommandProvider dCP;
	
	private DataProcessorResolver(DataCommandProvider dCP) {
		this.dCP = dCP;
		this.activeDPs = Configuration.getInstance().getDataProcessors();
	}
	
	public static DataProcessorResolver getInstance(DataCommandProvider dCP) {
		return instance != null ? instance
								: new DataProcessorResolver(dCP);
	}
	
	public HashMap<String, DataProcessor<ScanSampleList>> getDataProcessors() {
		HashMap<String, DataProcessor<ScanSampleList>> dPs = new HashMap<String, DataProcessor<ScanSampleList>>();
		boolean skip = false;
		for(String scannerID : activeDPs.keySet()) {
			DataCommandChain<Collection<?>, ScanSampleList> dCC = new DataCommandChain<Collection<?>, ScanSampleList>();
			DataProcessor<ScanSampleList> sDP = new DataProcessor<ScanSampleList>(ScanSampleList.class, dCC);
				
			HashMap<String, Boolean> requiredCommands = activeDPs.get(scannerID);
			
			for(String singleDataProcessorUri : requiredCommands.keySet()){
				boolean isActive = requiredCommands.get(singleDataProcessorUri);
				if(isActive){
					try {
						dCC.addCommand(dCP.getItem(singleDataProcessorUri));
					} catch (RegistryProviderException e) {
						//TODO Work around for now: when a sensor is disabled but its scanners are enabled an exception
						//is thrown here because the sensor did not register any Commands because it is disabled.
						//So we set the skip variable to avoid putting it into the Hashmap, because this will led to NullPointer Eception on reading...
						skip = true;
						Log.d(LOG_TAG, "skipped DataProcessor: " + singleDataProcessorUri + " because it was not registered...");
					}
				}
			}
			if(!skip)
				dPs.put(scannerID, sDP);
			skip = false;
		}
		return dPs;
	}
}
