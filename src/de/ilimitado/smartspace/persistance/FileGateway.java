package de.ilimitado.smartspace.persistance;

import java.io.IOException;

import android.content.Context;
import de.ilimitado.smartspace.android.FileManager;
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

	public void save(String fileName, StringBuffer fileBuffer) {
		try {
			fileManager.writeFile(fileName, fileBuffer);
			L.d(LOG_TAG, "Saving Buffer: " + fileName + " to file...");
		} catch (IOException e) {
			L.d(LOG_TAG, "Error while saving: " + fileName + " to file...");
			e.printStackTrace();
		}
	}
	
	@Override
	public void setStrategy(Object strategy) {	}

	public StringBuffer load(String fileName) { 
		StringBuffer filebuffer = new StringBuffer();	
		try {
			L.d(LOG_TAG, "Loading Buffer: from file...");
			filebuffer = new StringBuffer(fileManager.readFile(fileName));
			} catch (IOException e) {
				L.d(LOG_TAG, "File could not be loaded...");
				e.printStackTrace();
		}
		return filebuffer;
	}
}