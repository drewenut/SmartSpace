package de.ilimitado.smartspace.positioning;

import java.util.List;


public interface iLocationListener {

    void onLocationChanged(IGeoPoint location, Accuracy acc);
    
    void onLocationChanged(List<IGeoPoint> list, Accuracy acc);

    void onStateChanged(int state);
}
