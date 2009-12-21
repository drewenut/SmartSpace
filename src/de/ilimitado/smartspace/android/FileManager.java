package de.ilimitado.smartspace.android;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.util.Log;

public class FileManager{
	
	private static final String LOG_TAG = "FileManager";
	private static Context context;
	
	private static FileManager instance = null;

	private FileManager(Context ctx) {
		FileManager.context = ctx;
	}
	
	public void writeFile(String fileName, StringBuffer buffer) throws IOException {
		FileOutputStream outputStream = null;
		String filename = fileName + ".csv";
		  if(buffer.length() > 0){
			  outputStream = context.openFileOutput(filename, Context.MODE_APPEND);
			  outputStream.write(buffer.toString().getBytes());
			  outputStream.flush();
		  }
		  else if (buffer.length() == 0){
			  return;
		  }
		  Log.d(LOG_TAG, "File written successfully: " + filename);
	//			  Log.d(LOG_TAG, "Error while writing file: " + filename);
	}
	
	public void readFile(String fileName) {
		try {
			FileInputStream inputStream = context.openFileInput(fileName);
		} catch (FileNotFoundException e) {
			Log.i(LOG_TAG, "File contents for: " + fileName + " does not exist");
			Log.d(LOG_TAG, "File: " + fileName + " does not exist");
			e.printStackTrace();
		} 
	}

	public synchronized static FileManager getInstance(Context ctx) {
        if (instance == null) {
            instance = new FileManager(ctx);
        }
        return instance;
    }
}
