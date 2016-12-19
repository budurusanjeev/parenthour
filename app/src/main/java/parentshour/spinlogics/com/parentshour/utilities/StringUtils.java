package parentshour.spinlogics.com.parentshour.utilities;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils 
{
	 //String to Int
	public static int getInt(String str)
	{
		int value = 0;
		if (str == null || str.equalsIgnoreCase(""))
			return value;
		try 
		{
			value = Integer.parseInt(str);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return value;
	}

	//String to long
	public static long getLong(String string)
	{
		long value = 0;
		
		if (string == null || string.equalsIgnoreCase(""))
			return value;
		
		try 
		{
			value = Long.parseLong(string);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return value;
	}
	
	//String to float
	public static float getFloat(String string)
	{
		float value = 0f;
		
		if (string == null || string.equalsIgnoreCase("")
				|| string.equalsIgnoreCase("."))
			return value;
		
		try 
		{
			value = Float.parseFloat(string);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			return value;
		}
		
		return value;
	}
	
	//String to double
	public static double getDouble(String str)
	{
		double value = 0;
		
		if (str == null || str.equalsIgnoreCase(""))
			return value;
		
		try 
		{
			value = Double.parseDouble(str);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return value;
	}
	
	//String to boolean
	public static boolean getBoolean(String str)
	{
		Log.d("Sample", "com.winit.example.utilities.StringUtils.getBoolean");
		boolean value = false;
		if (str == null || str.equalsIgnoreCase(""))
			return value;
		try 
		{
			value = Boolean.parseBoolean(str);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return value;
	}

	//Method to check ic_email validation
	public static boolean isValidEmail(String string)
	{
		Log.d("Sample", "com.winit.example.utilities.StringUtils.isValidEmail");
		final Pattern EMAIL_ADDRESS_PATTERN = Pattern
				.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
						+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
						+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");
		Matcher matcher = EMAIL_ADDRESS_PATTERN.matcher(string);
		boolean value = matcher.matches();
		return value;
	}


	
}
