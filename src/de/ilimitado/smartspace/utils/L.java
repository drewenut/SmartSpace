package de.ilimitado.smartspace.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;

import de.ilimitado.smartspace.MotionListener;

import android.util.Log;
import android.widget.SlidingDrawer;

public final class L {

    public static final int DEBUG = 3;

    public static final int INFO = 4;

    public static final int WARN = 5;

    public static final int ERROR = 6;

    private static ArrayList<LogListener> logListeners = new ArrayList<LogListener>();
    private static HashMap<String, Long> startTimes = new HashMap<String, Long>();
	private static HashMap<String, Integer> counters = new HashMap<String, Integer>();

	//TODO Implement logic for debug log (debug messages are just written in debug mode)
    private L() {
    }

    public static int d(String tag, String msg) {
        return Log.d(tag, msg);
    }

    public static int d(String tag, String msg, Throwable tr) {
        return Log.d(tag, msg, tr);
    }

    public static int i(String tag, String msg) {
        return Log.i(tag, msg);
    }

    public static int i(String tag, String msg, Throwable tr) {
        return Log.i(tag, msg, tr);
    }

    public static int w(String tag, String msg) {
        return Log.w(tag, msg);
    }

    public static int w(String tag, String msg, Throwable tr) {
        return Log.w(tag, msg, tr);
    }

    public static int e(String tag, String msg) {
        return Log.e(tag, msg);
    }

    public static int e(String tag, String msg, Throwable tr) {
        return Log.e(tag, msg, tr);
    }
    
    /**
     * notifies screen log listeners
     */
    public static int s(String tag, String msg) {
    	notifyListeners(tag, msg);
    	return 1;
    }
    
    /**
     * notifies screen log listeners and writes to specified debug log
     */
    public static int sd(String tag, String msg) {
    	notifyListeners(tag, msg);
    	return Log.d(tag, msg);
    }
    
    /**
     * notifies screen log listeners and writes to specified info log
     */
    public static int si(String tag, String msg) {
    	notifyListeners(tag, msg);
    	return Log.i(tag, msg);
    }
    
    /**
     * notifies screen log listeners and writes to specified warning log
     */
    public static int sw(String tag, String msg) {
    	notifyListeners(tag, msg);
    	return Log.w(tag, msg);
    }
    
    /**
     * notifies screen log listeners and writes to specified error log
     */
    public static int se(String tag, String msg) {
    	notifyListeners(tag, msg);
    	return Log.e(tag, msg);
    }

    public static void println(String tag, String msg) {
    	System.out.println("Log: "+ tag + ", " +msg);
    }
    
    public static void println(String tag, String msg, Throwable tr) {
    	System.out.println("Log: "+ tag + ", " + msg + " Stack Trace: " + getStackTraceString(tr));
    }
    
    /**
     * Handy function to get a loggable stack trace from a Throwable
     * @param tr An exception to log
     */
    private static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        return sw.toString();
    }

    /**
     * Take starting times for runtime measurements (one per LOG_TAG)
     * @param LOG_TAG
     */
	public static void startT(String LOG_TAG) {
		startTimes.put(LOG_TAG, System.currentTimeMillis());
	}

	/**
	 * Calculate runtime and write to debug log
	 * @param LOG_TAG
	 * @param msg
	 */
	public static void stopT(String LOG_TAG, String msg) {
		L.d(LOG_TAG, msg + (System.currentTimeMillis() - startTimes.get(LOG_TAG).longValue()));
	}

	/**
	 * Start counter for given LOG_TAG
	 * @param LOG_TAG
	 */
	public static void startC(String LOG_TAG) {
		counters.put(LOG_TAG, 0);
	}
	
	/**
	 * Increment counter for given LOG_TAG and write to log
	 * @param LOG_TAG
	 * @param msg
	 */
	public static void incC(String LOG_TAG, String msg) {
		int incrementedCount = counters.get(LOG_TAG)+1;
		counters.put(LOG_TAG, incrementedCount);
		L.sd(LOG_TAG,  msg + incrementedCount);
	}
	
	/**
	 * Increment counter for given LOG_TAG and write LogListeners and debug log
	 * @param LOG_TAG
	 * @param msg
	 */
	public static void sincC(String LOG_TAG, String msg) {
		int incrementedCount = counters.get(LOG_TAG)+1;
		counters.put(LOG_TAG, incrementedCount);
		L.d(LOG_TAG, msg + incrementedCount);
	}
	
	public static void registerListener(LogListener logL){
		logListeners.add(logL);
	}
	
	public static void unregisterListener(LogListener logL){
		if(logListeners.contains(logL))
			logListeners.remove(logL);
	}
	
	public static boolean listenersEmpty(){
		return logListeners.isEmpty();
	}
	
	private static void notifyListeners(String tag, String msg){
		for(LogListener listener : logListeners){
			listener.onLogMessage(tag, msg);
		}
	}
}

