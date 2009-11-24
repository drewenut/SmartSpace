package de.ilimitado.smartspace.dataprocessors;

import java.util.Collection;
import java.util.HashMap;

import de.ilimitado.smartspace.ScanSampleList;
import de.ilimitado.smartspace.config.Configuration;
import de.ilimitado.smartspace.registry.DataCommandProvider;
import de.ilimitado.smartspace.registry.RegistryProviderException;

public class DataProcessorResolver {

	private static final String LOG_TAG = "DataProcessorResolver";
	
	private static final DataProcessorResolver instance = null;
	private DataProcessorsMap<String, HashMap<String, Boolean>> activeDPList;
	private DataCommandProvider dCP;
	
	private DataProcessorResolver(DataCommandProvider dCP) {
		this.dCP = dCP;
		this.activeDPList = Configuration.getInstance().getDataProcessorList();
	}
	
	public static DataProcessorResolver getInstance(DataCommandProvider dCP) {
		return instance != null ? instance
								: new DataProcessorResolver(dCP);
	}
	
	public HashMap<String, DataProcessor<ScanSampleList>> getDataProcessors() {
		HashMap<String, DataProcessor<ScanSampleList>> dPs = new HashMap<String, DataProcessor<ScanSampleList>>();
		for(String scannerID : activeDPList.keySet()) {
			DataCommandChain<Collection<?>, ScanSampleList> dCC = new DataCommandChain<Collection<?>, ScanSampleList>();
			DataProcessor<ScanSampleList> sDP = new DataProcessor<ScanSampleList>(ScanSampleList.class, dCC);
			for(HashMap<String, Boolean> requiredCommands : activeDPList.values()){
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
