package de.ilimitado.smartspace;

import java.util.List;

public interface IndoorPositionListener {

    void onLocationChanged(IGeoPoint location, Accuracy acc);
    
    void onLocationChanged(List<IGeoPoint> list, Accuracy acc);

    void onStateChanged(int state);
}
