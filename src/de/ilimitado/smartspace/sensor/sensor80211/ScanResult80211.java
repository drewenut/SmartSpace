package de.ilimitado.smartspace.sensor.sensor80211;

public class ScanResult80211 {
    /** The network name. */
    public String SSID;
    /** The address of the access point. */
    public String BSSID;

    public String capabilities;
    
    public int level;
    
    public int frequency;

    public ScanResult80211(String SSID, String BSSID, String caps, int level, int frequency) {
        this.SSID = SSID;
        this.BSSID = BSSID;
        this.capabilities = caps;
        this.level = level;
        this.frequency = frequency;
    }

    @Override
	public boolean equals(Object o) {
    	if((o instanceof ScanResult80211)){
    		ScanResult80211 scanResult80211 = (ScanResult80211) o;
			return (BSSID.equals(scanResult80211.BSSID)) ? true : false;
		}
		return false;
	}
    
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        String NONE = "<none>";

        sb.append("SSID: ").
            append(SSID == null ? NONE : SSID).
            append(", BSSID: ").
            append(BSSID == null ? NONE : BSSID).
            append(", capabilities: ").
            append(capabilities == null ? NONE : capabilities).
            append(", level: ").
            append(level).
            append(", frequency: ").
            append(frequency);

        return sb.toString();
    }
}
