package de.ilimitado.smartspace.fsm;
import java.util.HashMap;
import java.util.List;

import de.ilimitado.smartspace.Accuracy;
import de.ilimitado.smartspace.DataSampleFactory;
import de.ilimitado.smartspace.Dependencies;
import de.ilimitado.smartspace.PositionManager;
import de.ilimitado.smartspace.RTFPT;
import de.ilimitado.smartspace.RadioMap;
import de.ilimitado.smartspace.ScanSampleList;
import de.ilimitado.smartspace.SensorDataSample;
import de.ilimitado.smartspace.dataprocessors.DataProcessor;
import de.ilimitado.smartspace.persistance.PersistanceManager;
import de.ilimitado.smartspace.positioning.SimpleEuclideanDistanceProvider;
import de.ilimitado.smartspace.positioning.WeightedIGeoPoint;
import de.ilimitado.smartspace.sensorFusion.SensorDataSampleFusionator;
import de.ilimitado.smartspace.utils.L;


public class RTFPTSyncStrategy extends SensingStrategy {
	
	private final PositionManager posMngr;
	private final PersistanceManager perMngr;

	public RTFPTSyncStrategy(Dependencies dep, List<String> syncList, HashMap<String, DataProcessor<ScanSampleList>> dataProcessorList) {
		super(syncList, dataProcessorList);
		this.posMngr = dep.positionManager;
		this.perMngr = dep.persistanceManager;
	}
	
	@Override
	public SensorDataSample fusionate(HashMap<String, ScanSampleList>  resultSamples) {
		double orientationTMP = 1; //TODO Orientation
		RTFPT rtFPT = DataSampleFactory.getInstance().makeRTSample(orientationTMP);
		SensorDataSampleFusionator rtFPTFusion = new SensorDataSampleFusionator(rtFPT);
		rtFPTFusion.fusionate(resultSamples);
		return rtFPTFusion.getResult();
	}
	
	@Override
	public void deploySampleDataSample(SensorDataSample sDSpl) {
		L.startT(LOG_TAG);
		perMngr.load(PersistanceManager.GATEWAY_LFPT, "");
		RadioMap rM = (RadioMap) perMngr.get(PersistanceManager.GATEWAY_LFPT);
		SimpleEuclideanDistanceProvider pProv = new SimpleEuclideanDistanceProvider(rM);
		pProv.calculatePosition(sDSpl);
		L.stopT(LOG_TAG, "TIME calculatePosition(): ");
		List<WeightedIGeoPoint> weightedPositions = pProv.getEstimatedPositions();
		posMngr.enqueueEstimatedPosition(weightedPositions, new Accuracy(Accuracy.LOW_ACCURACY));
		L.stopT(LOG_TAG, "TIME deploySampleDataSample(): ");
	}
}
