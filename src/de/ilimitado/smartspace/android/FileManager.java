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
	
	public String readFile(String fileName) throws IOException {
		FileInputStream inputStream = null;
		inputStream = context.openFileInput(fileName);
		String content = org.apache.commons.io.IOUtils.toString(inputStream);
		if (inputStream != null) {
			inputStream.close();
		}
		return content;
	}

	public synchronized static FileManager getInstance(Context ctx) {
        if (instance == null) {
            instance = new FileManager(ctx);
        }
        return instance;
    }
}
