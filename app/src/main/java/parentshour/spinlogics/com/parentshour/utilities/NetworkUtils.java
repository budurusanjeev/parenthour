package parentshour.spinlogics.com.parentshour.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class NetworkUtils 
{
	//Method to check the Internet availability
	public static boolean isNetworkConnectionAvailable(Context context)
	{
		// checking the Internet availability
		boolean isNetworkConnectionAvailable = false;
		ConnectivityManager connectivityManager = (ConnectivityManager) context	.getSystemService(context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

		if (activeNetworkInfo != null)
		{
			isNetworkConnectionAvailable = activeNetworkInfo.getState() == NetworkInfo.State.CONNECTED;
		}

		return isNetworkConnectionAvailable;
	}
}
