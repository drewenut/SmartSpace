package de.ilimitado.smartspace.sensing;

import java.util.Collection;
import java.util.HashMap;

import de.ilimitado.smartspace.ScanSampleList;
import de.ilimitado.smartspace.config.Configuration;
import de.ilimitado.smartspace.registry.DataCommandProvider;
import de.ilimitado.smartspace.registry.RegistryProviderException;

public class DataProcessorResolver {

	private static final DataProcessorResolver instance = null;
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
		for(String scannerID : activeDPs.keySet()) {
			DataCommandChain<Collection<?>, ScanSampleList> dCC = new DataCommandChain<Collection<?>, ScanSampleList>();
			DataProcessor<ScanSampleList> sDP = new DataProcessor<ScanSampleList>(ScanSampleList.class, dCC);
			for(HashMap<String, Boolean> requiredCommands : activeDPs.values()){
				for(String singleDataProcessorUri : requiredCommands.keySet()){
					boolean isActive = requiredCommands.get(singleDataProcessorUri);
					if(isActive){
						try {
							dCC.addCommand(dCP.getItem(singleDataProcessorUri));
						} catch (RegistryProviderException e) {
							e.printStackTrace();
						}
					}
				}
			}
			dPs.put(scannerID, sDP);
		}
		return dPs;
	}
}
