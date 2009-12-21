package de.ilimitado.smartspace.persistance;

import android.content.Context;
import de.ilimitado.smartspace.android.FileManager;
import de.ilimitado.smartspace.sensor.sensor80211.DataBuffer;
import de.ilimitado.smartspace.utils.L;

public class FileGateway implements Gateway{

	private static final String LOG_TAG = "FILEGateway";
	private Context context;
	private FileManager fileManager;
	
	public FileGateway(Context androidCtx) {
		this.context = androidCtx;
		this.fileManager = FileManager.getInstance(context);
	}

	public boolean isLoaded(){ 
		return fileManager != null ? true :false;
	}

	public void save(Object fileBuffer) {
		fileManager.writeFile(((DataBuffer) fileBuffer).name, ((DataBuffer) fileBuffer).buffer);
		L.d(LOG_TAG, "Saving Buffer: " + ((DataBuffer) fileBuffer).name + "to file...");
	}
	
	@Override
	public void setStrategy(Object strategy) {	}

	public DataBuffer load(String query) { 
		//TODO
		//		fileManager.readFile(query);
		//		L.d(LOG_TAG, "Loading Buffer: from file...");
		return new DataBuffer(query);
	}
}