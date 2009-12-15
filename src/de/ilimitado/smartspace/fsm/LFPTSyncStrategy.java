package de.ilimitado.smartspace.fsm;
import java.util.HashMap;
import java.util.List;

import de.ilimitado.smartspace.DataSampleFactory;
import de.ilimitado.smartspace.Dependencies;
import de.ilimitado.smartspace.LFPT;
import de.ilimitado.smartspace.PositionManager;
import de.ilimitado.smartspace.ScanSampleList;
import de.ilimitado.smartspace.SensorDataSample;
import de.ilimitado.smartspace.persistance.PersistanceManager;
import de.ilimitado.smartspace.positioning.IGeoPoint;
import de.ilimitado.smartspace.sensing.DataProcessor;
import de.ilimitado.smartspace.sensorFusion.SensorDataSampleFusionator;
import de.ilimitado.smartspace.utils.L;


public class LFPTSyncStrategy extends SensingStrategy {
	
	private final PositionManager posMngr;
	private final PersistanceManager perMngr;

	public LFPTSyncStrategy(Dependencies dep, List<String> syncList, HashMap<String, DataProcessor<ScanSampleList>> dataProcessorList) {
		super(syncList, dataProcessorList);
		this.posMngr = dep.positionManager;
		this.perMngr = dep.persistanceManager;
	}
	
	@Override
	public SensorDataSample fusionate(HashMap<String, ScanSampleList>  resultSamples) {
		IGeoPoint iGP = posMngr.getCurrentPosition();
		double orientationTMP = 1; //TODO Orientation
		LFPT lFpt = DataSampleFactory.getInstance().makeLFPT(iGP, orientationTMP);
		SensorDataSampleFusionator lfptFusion = new SensorDataSampleFusionator(lFpt);
		lfptFusion.fusionate(resultSamples);
		return lfptFusion.getResult();
	}
	
	@Override
	public void deploySampleDataSample(SensorDataSample sDSpl) {
		L.startT(LOG_TAG);
		perMngr.save(PersistanceManager.GATEWAY_LFPT, sDSpl);
		L.stopT(LOG_TAG, "TIME deploySampleDataSample(): ");
	}
}
