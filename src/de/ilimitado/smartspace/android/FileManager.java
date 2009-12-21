package de.ilimitado.smartspace.android;

import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.util.Log;

public class FileManager{
	
	private static final String LOG_TAG = "FileManager";
	private static Context context;

	public FileManager(Context ctx) {
		FileManager.context = ctx;
	}
	
	public static void writeFile(String fileName, StringBuffer buffer) {
		FileOutputStream outputStream = null;
		String filename = fileName + ".csv";
		try {
			  if(buffer.length() > 0){
				  outputStream = context.openFileOutput(filename, Context.MODE_APPEND);
				  outputStream.write(buffer.toString().getBytes());
				  outputStream.flush();
			  }
			  else if (buffer.length() == 0){
				  return;
			  }
			  Log.d(LOG_TAG, "File written successfully: " + filename);
		  }
		  catch (IOException e) {
			  Log.d(LOG_TAG, "Error while writing file: " + filename);
		  }
	}
	
	public static void readFile(String fileName, StringBuffer buffer) {
		//TODO 
	}
}
