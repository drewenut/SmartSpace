package de.ilimitado.smartspace.sensor.sensorGSM;

public class ScanResultGSM {
    private static final long EMPTY = 0;
	/** The Cell ID. */
    public String CID;
    /** Mobile Carrier. */
    public String PROVIDER;

    public int level;
    
    public int frequency;
    
    public long timestamp;

    public ScanResultGSM(int CID, String PROVIDER, int level) {
        this.CID = Integer.toString(CID);
        this.PROVIDER = PROVIDER;
        this.level = level;
        this.timestamp = EMPTY;
    }

    @Override
	public boolean equals(Object o) {
    	if((o instanceof ScanResultGSM)){
    		ScanResultGSM scanResult80211 = (ScanResultGSM) o;
			return (CID.equals(scanResult80211.CID)) ? true : false;
		}
		return false;
	}
    
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        String NONE = "<none>";

        sb.append("CID: ").
            append(CID == null ? NONE : CID).
            append(", PROVIDER: ").
            append(PROVIDER == null ? NONE : PROVIDER).
            append(", level: ").
            append(level).
        	append(", timestamp: ").
        	append(timestamp);

        return sb.toString();
    }
}

