package parentshour.spinlogics.com.parentshour.utilities;

import android.util.Log;


public class LogUtils 
{
	public static boolean isLogEnabled         = true;
	public static final String SERVICE_LOG_TAG = "TestServiceLog";
	public static final String APP_TAG         = "TestProject";
	
	public static void error(String tag, String msg)
	{
		if(isLogEnabled) 
			Log.e(tag, msg);
	}
	
	public static void info(String tag, String msg)
	{
		if(isLogEnabled)
			Log.i(tag, msg);
	}
	
	public static void verbose(String tag, String msg)
	{
		if(isLogEnabled)
			Log.v(tag, msg);
	}
	
	public static void debug(String tag, String msg)
	{
		if(isLogEnabled)
			Log.d(tag, msg);
	}
    
	public static void printMessage(String msg)
	{
		if (isLogEnabled)
			System.out.println(msg);
	}
	
}
