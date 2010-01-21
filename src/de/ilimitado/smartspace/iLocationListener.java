package de.ilimitado.smartspace;

import java.util.List;

import de.ilimitado.smartspace.positioning.Accuracy;
import de.ilimitado.smartspace.positioning.IGeoPoint;

public interface iLocationListener {

    void onLocationChanged(IGeoPoint location, Accuracy acc);
    
    void onLocationChanged(List<IGeoPoint> list, Accuracy acc);

    void onStateChanged(int state);
}
