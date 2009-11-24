package de.ilimitado.smartspace.persistance;

import de.ilimitado.smartspace.LFPT;
import de.ilimitado.smartspace.RadioMap;

public interface LFPTPersistanceStrategy {
	public void save(LFPT lFpt);
	public void loadRadioMap();
	public RadioMap getRadioMap();
}
