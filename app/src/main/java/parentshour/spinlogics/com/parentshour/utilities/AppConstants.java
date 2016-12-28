package parentshour.spinlogics.com.parentshour.utilities;

import android.graphics.Typeface;
import android.os.Environment;

import java.io.File;
import java.net.HttpURLConnection;

import parentshour.spinlogics.com.parentshour.activity.ParentDashboard;

public class AppConstants
{

	public static final int GET = 1;
	public static final int POST = 2;
	//Device height & width
	public static int DEVICE_DISPLAY_WIDTH;
	public static int DEVICE_DISPLAY_HEIGHT;
	public static Typeface typeFace;

	//public static String IMAGE_URL="http://fitros-ws.elasticbeanstalk.com/";
	
	public static String SDCARD_ROOT = Environment.getExternalStorageDirectory().toString() + File.separator;
	
	public static String appCacheDir = "";
	
	public static String ContentTypeJson = "application/json";

	public static String ContentTypeXML = "application/xml";
	
	public static String ContentTypeForm = "application/x-www-form-urlencoded";
	
	public static String CHECK_NETWORK_CONN="Please check internet connection!!";
	
	public static String NO_RESPONSE="No Response from Server";
	public static int PAYMODEONLINE=5;
	public static int PAYMODECOD=7;
	public static String DASHBOARD = ParentDashboard.class.getSimpleName();
	public static String RESTAURANTID = "restaurantid";
	public static String RESTAURANTNAME = "restaurant ic_name";
	public static String CLASSFROM = "classfrom";
	public static String KEY_USER_ID="";
	public static String KEY_USER_NAME="custName";
	public static String KEY_CUSTOMER_ID="customerId";
	public static String KEY_USER_EMAIL="ic_email";
	public static String KEY_IS_LOGIN = "IsLoggedIn";
	public static String KEY_IS_COUPON_APPLIED= "IsCouponApplied";
	public static String KEY_TOTALAMTAFTERCOUPON="totalamtaftercoupon";
	public static String KEY_SOCIAL_TYPE="socialtype";
	public static String KEY_USER_CONTACT="MobileNo";
	public static String KEY_WALLET_AMOUNT= "walletAmount";
	public static String ActionProceedToBillSummry = "proceedToBillSummry";
	public static String KEY_ORDER_SUCCESS= "orderDatails";
	public static String KEY_ORDERPACKEAGE="orderpackagelist";
	public static String LOGINCOOKIE="loginCookie";
	public static String FILTERQUIERY = "?ic_filter=";

	//Host url
	public static String HOST_URL="http://spinlogics.com/Parentshour/webservices/";


	//Parent
	public static String LOGIN_URL = HOST_URL+"p_login.php";
	public static String SIGNUP_URL = HOST_URL+ "p_register1.php";
	public static String SIGNUP_FINAL_URL = HOST_URL+ "p_register2.php";
    public static String FORTGOT_PASSWORD_URL = HOST_URL+"p_forgotpassword.php";
    public static String GET_PROFILE_URL = HOST_URL+"p_getprofile.php";
	public static String GET_PARENT_PROFILE_URL = HOST_URL + "p_get_parent_profile.php";
	public static String EDIT_PROFILE_URL = HOST_URL+"p_editprofile.php";
	public static String PARENT_CHANGE_PASSWORD_URL = HOST_URL +"p_changepassword.php";
	public static String PARENT_GET_SETTINGS_URL = HOST_URL +"p_getsettings.php";
	public static String PARENT_SOCIAL_LOGIN_URL = HOST_URL +"p_login_social.php";
	public static String PARENT_PLAY_DATE_EVENT = HOST_URL + "p_get_play_date_events.php";
	public static String PARENT_SETTING_URL = HOST_URL +"p_setsettings.php";
	public static String PARENT_FRIENDS_URL = HOST_URL + "p_get_friends.php";
	public static String PARENT_GROUPS_URL = HOST_URL + "p_get_groups.php";
	public static String PARENT_SEARCH_PLAY_DATE_URL = HOST_URL + "p_search_play_date.php";
	public static String PARENT_ADDED_FRIENDS_LIST_URL = HOST_URL + "p_get_friends.php";


	//Assistant
	public static String ASSISTANT_LOGIN_URL = HOST_URL + "a_login.php";
	public static String ASSISTANT_SIGNUP_URL = HOST_URL+ "a_register1.php";
	public static String ASSISTANT_SIGNUP_FINAL_URL = HOST_URL+ "a_register2.php";
	public static String ASSISTANT_GET_PROFILE_URL = HOST_URL + "a_getprofile.php";
	public static String ASSISTANT_EDIT_PROFILE_URL = HOST_URL + "a_editprofile.php";
	public static String ASSISTANT_CHANGE_PASSWORD_URL = HOST_URL +"a_changepassword.php";
	public static String ASSISTANT_FORTGOT_PASSWORD_URL = HOST_URL+"a_forgotpassword.php";
	public static String ASSISTANT_SOCIAL_LOGIN_URL = HOST_URL +"a_login_social.php";
	public int UNAUTHORISED = HttpURLConnection.HTTP_UNAUTHORIZED;

}
