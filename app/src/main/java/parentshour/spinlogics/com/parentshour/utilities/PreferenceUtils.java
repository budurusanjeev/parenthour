package parentshour.spinlogics.com.parentshour.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashMap;
public class PreferenceUtils

{
	private SharedPreferences preferences;
	private SharedPreferences.Editor edit;
	public static String KEY 			= 	"key";

	public PreferenceUtils(Context context)
	{
		preferences		=	PreferenceManager.getDefaultSharedPreferences(context);
		edit			=	preferences.edit();
	}

	public void saveString(String strKey, String strValue)
	{
		edit.putString(strKey, strValue);
		edit.commit();
	}

	public void saveInt(String strKey, int value)
	{
		edit.putInt(strKey, value);
		edit.commit();
	}


	public String getStringFromPreference(String strKey, String defaultValue )
	{
		return preferences.getString(strKey, defaultValue);
	}



	public int getIntFromPreference(String strKey, int defaultValue)
	{
		return preferences.getInt(strKey, defaultValue);
	}

	public void savetotalamountafterapplycoupon(double totalamt)
	{
		edit.putString(AppConstants.KEY_TOTALAMTAFTERCOUPON, String.valueOf(totalamt));

		edit.commit();
	}

	public String gettotalamountaftercouponapply()
	{
		return preferences.getString(AppConstants.KEY_TOTALAMTAFTERCOUPON,"0");
	}

	public void couponApplied()
	{
		edit.putBoolean(AppConstants.KEY_IS_COUPON_APPLIED, true);
		edit.commit();
	}

	public void storeRestaurentIdName(String restID, String restName) {
		edit.putString(AppConstants.RESTAURANTID, restID);
		edit.putString(AppConstants.RESTAURANTNAME, restName);

		edit.commit();
	}

	public void createLoginSession(String customerId, String custName, String emailId, String MobileNo, String walletAmount)
	{

		edit.putString(AppConstants.KEY_CUSTOMER_ID,customerId);

		edit.putString(AppConstants.KEY_USER_NAME,custName);

		edit.putString(AppConstants.KEY_USER_EMAIL, emailId);

		edit.putString(AppConstants.KEY_USER_CONTACT, MobileNo);

		edit.putBoolean(AppConstants.KEY_IS_LOGIN, true);

		edit.putString(AppConstants.KEY_WALLET_AMOUNT, walletAmount);

		edit.commit();
	}


	public void wallet_update(String walletAmount)
	{
/*
		ic_edit.putString(AppConstants.KEY_CUSTOMER_ID,custId);

		ic_edit.putString(AppConstants.KEY_USER_NAME,custName);

		ic_edit.putString(AppConstants.KEY_USER_EMAIL, emailId);

		ic_edit.putString(AppConstants.KEY_USER_CONTACT, MobileNo);

		ic_edit.putBoolean(AppConstants.KEY_IS_LOGIN, true);*/

		edit.putString(AppConstants.KEY_WALLET_AMOUNT, walletAmount);

		edit.commit();
	}

	public String getupdatedwalletamount()
	{
		return preferences.getString(AppConstants.KEY_WALLET_AMOUNT,"0");
	}

   public String getCustId()
	   {
		   return preferences.getString(AppConstants.KEY_CUSTOMER_ID, "NA");
	   }

	public void logoutUser()
	{
		/*ic_edit.clear();
		ic_edit.commit();*/
		edit.clear();
		//edit.putBoolean(AppConstants.KEY_IS_LOGIN, false);
		//edit.putString(AppConstants.LOGINCOOKIE, "null");
		edit.commit();

	}

	public void saveLoginCookie(String cookie)
	{

		edit.putString(AppConstants.LOGINCOOKIE, cookie);
		edit.commit();

	}

	public String getLoginCookie()
	{
		return preferences.getString(AppConstants.LOGINCOOKIE,"null");
	}

	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> getUserDetails(){
		HashMap<String, String> user = new HashMap<String, String>();
		// user id
		user.put(AppConstants.KEY_CUSTOMER_ID, preferences.getString(AppConstants.KEY_CUSTOMER_ID, "0"));

		// user ic_name
		user.put(AppConstants.KEY_USER_NAME, preferences.getString(AppConstants.KEY_USER_NAME, "NA"));

		// user ic_email id
		user.put(AppConstants.KEY_USER_EMAIL, preferences.getString(AppConstants.KEY_USER_EMAIL, "NA"));

		// user contact number
		user.put(AppConstants.KEY_USER_CONTACT, preferences.getString(AppConstants.KEY_USER_CONTACT, "0000000000"));

		//Wallet Amt
		user.put(AppConstants.KEY_WALLET_AMOUNT, preferences.getString(AppConstants.KEY_WALLET_AMOUNT, "0"));

		// return user
		return user;
	}

	public String getthe_socialtype()
	{
		return preferences.getString(AppConstants.KEY_SOCIAL_TYPE, "NA");
	}

	/**
	 * Quick check for login
	 * **/
	// Get Login State
	public boolean isLoggedIn(){
		return preferences.getBoolean(AppConstants.KEY_IS_LOGIN, false);
	}

	/**
	 * Logout User method
	 */

	public boolean isCouponApplied()
	{
		return preferences.getBoolean(AppConstants.KEY_IS_COUPON_APPLIED, false);
	}


	public void save_cust_contct_number(String contactnumber)
	{
		edit.putString(AppConstants.KEY_USER_CONTACT,contactnumber);

		edit.commit();

	}




}
