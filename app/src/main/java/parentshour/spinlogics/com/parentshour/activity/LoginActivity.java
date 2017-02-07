package parentshour.spinlogics.com.parentshour.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;
import parentshour.spinlogics.com.parentshour.R;
import parentshour.spinlogics.com.parentshour.utilities.AppConstants;
import parentshour.spinlogics.com.parentshour.utilities.FontStyle;
import parentshour.spinlogics.com.parentshour.utilities.NetworkUtils;
import parentshour.spinlogics.com.parentshour.utilities.PreferenceUtils;


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 9001;
    private static final int RC_SIGN_IN_FB = 9002;
    private static LoginButton loginButtonfb;
    public Dialog dialog;
    Context mContext;
    PreferenceUtils preferenceUtils;
    LinearLayout ll_parent_login, ll_child_login, ll_orConnect_login, ll_fb_btn, ll_google_btn;
    LinearLayout ll_fb_google_btns;
    ImageView iv_ph_logo;
    TextView tv_ll_i_am_a;
    ImageView iv_spin_iAm;
    EditText edt_email, edt_password;
    TextView tv_forgot_password, tv_orConnect_login, tv_dont_hv_acc_signup;
    Spinner spin_I_am;
    TextView btn_login;
    String emailRegistered;
    String URLLINK = null;
    GoogleApiClient mGoogleApiClient;
    SignInButton signInButton;
    private AnimationDrawable animationDrawable;
    private TextView info;
    private CallbackManager callbackManager;

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    /* public static void disconnectFromFacebook() {

         if (AccessToken.getCurrentAccessToken() == null) {
             // Toast.makeText()
             loginButtonfb.performClick();
             return; // already logged out

         }
         new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                 .Callback() {
             @Override
             public void onCompleted(GraphResponse graphResponse) {

                 LoginManager.getInstance().logOut();

             }
         }).executeAsync();
     }
 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        mContext = LoginActivity.this;
        initializeControls();
        Fabric.with(this, new Crashlytics());
        callbackManager = CallbackManager.Factory.create();

        preferenceUtils = new PreferenceUtils(mContext);
        tv_dont_hv_acc_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreferenceUtils preferenceUtils = new PreferenceUtils(getApplicationContext());
                preferenceUtils.saveString("email", "");
                preferenceUtils.saveString("p_pic", "");
                preferenceUtils.saveString("a_pic", "");
                preferenceUtils.saveString("p_name", "");
                preferenceUtils.saveString("a_name", "");
                preferenceUtils.saveString("social", "general");
                startActivity(new Intent(getApplicationContext(), SignupRegistration.class));
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
                signIn();
            }
        });
        loginButtonfb.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));

        ll_fb_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LoginManager.getInstance().logOut();
                loginButtonfb.performClick();
            }
        });
        ll_google_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
                signIn();
            }
        });
        loginButtonfb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                showLoaderNew();
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                try {
                                    Log.v("fblogin", "fblogin " + object.toString());
                                    JSONObject pictureObject = new JSONObject(object.getString("picture"));
                                    JSONObject pictureObjectUrl = new JSONObject(pictureObject.getString("data"));
                                    PreferenceUtils preferenceUtils = new PreferenceUtils(getApplicationContext());
                                    if (preferenceUtils.getStringFromPreference("select", "").equals("parent")) {
                                        preferenceUtils.saveString("p_name", object.getString("name"));
                                        preferenceUtils.saveString("p_pic", pictureObjectUrl.getString("url"));
                                        preferenceUtils.saveString("email", object.getString("email"));
                                        preferenceUtils.saveString("social", "fb");
                                        // preferenceUtils.saveString("p_gender",  object.getString("gender"));
                                        // preferenceUtils.saveString("loggedin", "loggedin");
                                        sendProfileData(object.getString("email"), object.get("id").toString(), pictureObjectUrl.getString("url"));

                                    } else if (preferenceUtils.getStringFromPreference("select", "").equals("assistant")) {
                                        // preferenceUtils.saveString("a_id",  object.get("id").toString());
                                        preferenceUtils.saveString("a_name", object.getString("name"));
                                        preferenceUtils.saveString("a_pic", pictureObjectUrl.getString("url"));
                                        preferenceUtils.saveString("email", object.getString("email"));
                                        preferenceUtils.saveString("social", "fb");
                                        //preferenceUtils.saveString("a_gender",  object.getString("gender"));
                                        sendProfileData(object.getString("email"), object.get("id").toString(), pictureObjectUrl.getString("url"));
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    throw new RuntimeException("crash" + e.toString());
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, name, email, gender, birthday, picture");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(mContext, "FB Login Cancel ", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException e) {
                // Log.v("","" +)
                e.printStackTrace();
                Crashlytics.logException(e);
                Toast.makeText(mContext, "FB Login Error ", Toast.LENGTH_LONG).show();
                // throw new RuntimeException("crash" + e.toString());
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = edt_email.getText().toString();
                String password = edt_password.getText().toString();
                if (email.length() > 0 &&
                        password.length() > 0) {
                    if (isValidEmail(edt_email.getText().toString())) {
                        if (NetworkUtils.isNetworkConnectionAvailable(mContext)) {
                            showLoaderNew();
                            postData();
                        } else {
                            Toast.makeText(mContext, "Please check internet connection", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        String error = "Invalid Email";
                        showAlertValidation(error);
                    }

                } else {
                    String error = "Invalid Email & Password ";
                    showAlertValidation(error);
                }

            }
        });
        tv_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.forget_password, null);
                dialogBuilder.setView(dialogView);
                final AlertDialog b = dialogBuilder.create();
                final EditText registeredEmail = (EditText) dialogView.findViewById(R.id.edt_forgot_email);
                final TextView tv_newPassword = (TextView) dialogView.findViewById(R.id.tv_forget_password);
                tv_newPassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isValidEmail(registeredEmail.getText().toString())) {
                            emailRegistered = registeredEmail.getText().toString();
                            resetPassword(emailRegistered);
                            b.dismiss();
                        } else {
                            showAlertValidation("Please enter email");
                        }


                    }
                });
                b.show();
            }
        });
    }
   /* public String getDataFromUri(String data)
    {String imageData = null;
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), Uri.parse(data));
                imageData = getStringImage(bm);
               // iv_upload_profile_photo.setImageBitmap(bm);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return imageData;
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }*/

    private void handleSignInResult(GoogleSignInResult result) {
        if (result != null)

        {
            if (result.isSuccess()) {
                // Signed in successfully, show authenticated UI.
                showLoaderNew();
                GoogleSignInAccount acct = result.getSignInAccount();
                PreferenceUtils preferenceUtils = new PreferenceUtils(getApplicationContext());
                if (preferenceUtils.getStringFromPreference("select", "").equals("parent")) {
                    preferenceUtils.saveString("p_id", acct.getId());
                    preferenceUtils.saveString("email", acct.getEmail());
                    preferenceUtils.saveString("p_name", acct.getDisplayName());
                    if (acct.getPhotoUrl() != null) {
                        Log.v("", "" + acct.getPhotoUrl());
                        // getDataFromUri(acct.getPhotoUrl());
                        preferenceUtils.saveString("p_pic", acct.getPhotoUrl().toString());
                        try {
                            sendProfileData(acct.getEmail(), acct.getId(), String.valueOf(new URL(acct.getPhotoUrl().toString())));
                        } catch (Exception e) {
                            e.printStackTrace();
                            Crashlytics.logException(e);
                }
                    } else {
                        sendProfileData(acct.getEmail(), acct.getId(), "");
                    }
                    // preferenceUtils.saveString("loggedin", "loggedin");
                    preferenceUtils.saveString("social", "gp");
                    // sendProfileData(acct.getEmail(), acct.getId(),  acct.getPhotoUrl().toString());
               /* Intent dashbordactivity = new Intent(mContext, ParentDashboard.class);
                startActivity(dashbordactivity);
                finish();*/
                } else if (preferenceUtils.getStringFromPreference("select", "").equals("assistant")) {
                    preferenceUtils.saveString("a_id", acct.getId());
                    preferenceUtils.saveString("a_name", acct.getDisplayName());
                    preferenceUtils.saveString("email", acct.getEmail());
                    if (acct.getPhotoUrl() != null) {
                        preferenceUtils.saveString("a_pic", acct.getPhotoUrl().toString());
                        Log.v("Image url", "Image url " + acct.getPhotoUrl());
                        try {
                            sendProfileData(acct.getEmail(), acct.getId(), String.valueOf(new URL(acct.getPhotoUrl().toString())));
                        } catch (Exception e) {
                            e.printStackTrace();
                            Crashlytics.logException(e);
                }

                    } else {
                        sendProfileData(acct.getEmail(), acct.getId(), "");
                    }
                    // preferenceUtils.saveString("loggedin", "loggedin");
                    preferenceUtils.saveString("social", "gp");
               /* Intent dashbordactivity = new Intent(mContext, AssitantDashBoard.class);
                startActivity(dashbordactivity);
                finish();*/
        }

            } else {
                // Signed out, show unauthenticated UI.
                // updateUI(false);
    }
        } else {
            Toast.makeText(getApplicationContext(), "No google account found", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void resetPassword(final String emailRegistered) {
        String URLLINK = null;
        final PreferenceUtils preferenceUtils = new PreferenceUtils(getApplicationContext());
        preferenceUtils.getStringFromPreference("select", "");

        if (preferenceUtils.getStringFromPreference("select", "").equals("parent")) {
            URLLINK = AppConstants.FORTGOT_PASSWORD_URL;
            Log.v("res", "res " + URLLINK);

        } else if (preferenceUtils.getStringFromPreference("select", "").equals("assistant")) {
            URLLINK = AppConstants.ASSISTANT_FORTGOT_PASSWORD_URL;
            Log.v("res", "res " + URLLINK);

        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLLINK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("res ", "res  " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.has("Success")) {
                                showAlertValidation(jsonObject.getString("Success"));
                            } else {
                                showAlertValidation(jsonObject.getString("Error"));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Crashlytics.logException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("error", "error " + error.toString());

                        Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        // throw new RuntimeException("crash" + error.toString());
                        Crashlytics.logException(error);
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                String credentials = "p_email=" + emailRegistered;

                try {
                    return credentials.getBytes(getParamsEncoding());
                } catch (UnsupportedEncodingException uee) {
                    throw new RuntimeException("Encoding not supported: "
                            + getParamsEncoding(), uee);
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorisation", "e4f507ddf306806gc7dcg77ed1e52f97");
                return params;
            }

            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void postData() {
        final PreferenceUtils preferenceUtils = new PreferenceUtils(getApplicationContext());
        preferenceUtils.getStringFromPreference("select", "");

        if (preferenceUtils.getStringFromPreference("select", "").equals("parent")) {
            URLLINK = AppConstants.LOGIN_URL;
            Log.v("res", "res " + URLLINK);

        } else if (preferenceUtils.getStringFromPreference("select", "").equals("assistant")) {
            URLLINK = AppConstants.ASSISTANT_LOGIN_URL;
            Log.v("res", "res " + URLLINK);

        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLLINK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Toast.makeText(LoginActivity.this,response,Toast.LENGTH_LONG).show();
                        Log.v("res", "res " + response);
                        hideloader();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has("Success")) {
                                if (preferenceUtils.getStringFromPreference("select", "").equals("parent")) {
                                    PreferenceUtils preferenceUtils = new PreferenceUtils(mContext);
                                    preferenceUtils.saveString("p_id", jsonObject.getString("p_id"));
                                    preferenceUtils.saveString("p_name", jsonObject.getString("p_name"));
                                    preferenceUtils.saveString("p_pic", jsonObject.getString("p_pic"));
                                    preferenceUtils.saveString("social", "general");
                                    preferenceUtils.saveString("loggedin", "loggedin");
                                    // sendProfileData();
                                    Intent dashbordactivity = new Intent(mContext, ParentDashboard.class)
                                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(dashbordactivity);
                                    finish();

                                } else if (preferenceUtils.getStringFromPreference("select", "").equals("assistant")) {
                                    PreferenceUtils preferenceUtils = new PreferenceUtils(mContext);
                                    preferenceUtils.saveString("a_id", jsonObject.getString("a_id"));
                                    preferenceUtils.saveString("a_name", jsonObject.getString("a_name"));
                                    preferenceUtils.saveString("a_pic", jsonObject.getString("a_pic"));
                                    preferenceUtils.saveString("social", "general");
                                    preferenceUtils.saveString("loggedin", "loggedin");
                                    //  sendProfileData();
                                    Intent dashbordactivity = new Intent(mContext, AssitantDashBoard.class)
                                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(dashbordactivity);
                                    finish();
                                }

                            } else {
                                showAlertValidation(jsonObject.getString("Error"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            // throw new RuntimeException("crash" + e.toString());
                            Crashlytics.logException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideloader();
                        Log.v("error", "error " + error.toString());

                        Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        //  throw new RuntimeException("crash" + error.toString());
                        Crashlytics.logException(error);
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                String credentials = null;
                if (preferenceUtils.getStringFromPreference("select", "").equals("parent")) {
                    credentials = "p_email=" + edt_email.getText().toString() + "&p_password=" + edt_password.getText().toString();
                } else if (preferenceUtils.getStringFromPreference("select", "").equals("assistant")) {
                    credentials = "a_email=" + edt_email.getText().toString() + "&a_password=" + edt_password.getText().toString();

                }

                try {
                    return credentials.getBytes(getParamsEncoding());
                } catch (UnsupportedEncodingException uee) {
                    throw new RuntimeException("Encoding not supported: "
                            + getParamsEncoding(), uee);
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorisation", "e4f507ddf306806gc7dcg77ed1e52f97");
                return params;
            }

            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void sendProfileData(final String email, final String id, final String url) {
        //showLoaderNew();
        final PreferenceUtils preferenceUtils = new PreferenceUtils(getApplicationContext());
        preferenceUtils.getStringFromPreference("select", "");

        if (preferenceUtils.getStringFromPreference("select", "").equals("parent")) {
            URLLINK = AppConstants.PARENT_SOCIAL_LOGIN_URL;
            Log.v("res", "res " + URLLINK);

        } else if (preferenceUtils.getStringFromPreference("select", "").equals("assistant")) {
            URLLINK = AppConstants.ASSISTANT_SOCIAL_LOGIN_URL;
            Log.v("res", "res " + URLLINK);

        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLLINK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideloader();
                        //  Toast.makeText(LoginActivity.this, "social " + response, Toast.LENGTH_LONG).show();
                        Log.v("res", "res " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (preferenceUtils.getStringFromPreference("select", "").equals("parent")) {
                                if (jsonObject.getString("Success-msg").equals("Email Does Not Exists")) {
                                    // generateIconAndStringForDrawer();
                                    PreferenceUtils preferenceUtils = new PreferenceUtils(mContext);
                                    preferenceUtils.saveString("p_pic", url);
                                    Intent dashbordactivity = new Intent(mContext, SignupRegistration.class)
                                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(dashbordactivity);
                                    finish();
                                } else {
                                    PreferenceUtils preferenceUtils = new PreferenceUtils(mContext);
                                    preferenceUtils.saveString("p_id", jsonObject.getString("p_id"));
                                    preferenceUtils.saveString("p_name", jsonObject.getString("p_name"));
                                    preferenceUtils.saveString("p_pic", jsonObject.getString("p_pic"));
                                    preferenceUtils.saveString("loggedin", "loggedin");
                                    Intent dashbordactivity = new Intent(mContext, ParentDashboard.class)
                                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(dashbordactivity);
                                    finish();
                                }
                            } else if (preferenceUtils.getStringFromPreference("select", "").equals("assistant")) {
                                if (jsonObject.getString("Success-msg").equals("Email Does Not Exists")) {
                                    PreferenceUtils preferenceUtils = new PreferenceUtils(mContext);
                                    preferenceUtils.saveString("a_pic", url);
                                    Intent dashbordactivity = new Intent(mContext, SignupRegistration.class)
                                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(dashbordactivity);
                                    finish();
                                } else {
                                    PreferenceUtils preferenceUtils = new PreferenceUtils(mContext);
                                    preferenceUtils.saveString("a_id", jsonObject.getString("a_id"));
                                    preferenceUtils.saveString("a_name", jsonObject.getString("a_name"));
                                    preferenceUtils.saveString("a_pic", jsonObject.getString("a_pic"));
                                    preferenceUtils.saveString("loggedin", "loggedin");
                                    Intent dashbordactivity = new Intent(mContext, AssitantDashBoard.class)
                                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(dashbordactivity);
                                    finish();
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Crashlytics.logException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideloader();
                        Log.v("error", "error " + error.toString());
                        Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        // throw new RuntimeException("crash" + error.toString());
                        Crashlytics.logException(error);
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                String credentials = null;
                if (preferenceUtils.getStringFromPreference("select", "").equals("parent")) {
                    credentials = "p_email=" + email + "&login_type=web&social_id=" + id;

                } else if (preferenceUtils.getStringFromPreference("select", "").equals("assistant")) {

                    credentials = "a_email=" + email + "&login_type=web&social_id=" + id;

                }

                try {
                    return credentials.getBytes(getParamsEncoding());
                } catch (UnsupportedEncodingException uee) {
                    throw new RuntimeException("Encoding not supported: "
                            + getParamsEncoding(), uee);
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorisation", "e4f507ddf306806gc7dcg77ed1e52f97");
                return params;
            }

            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void signIn() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void initializeControls() {
        btn_login = (TextView) findViewById(R.id.btn_login);
        tv_forgot_password = (TextView) findViewById(R.id.tv_forgot_password);
        tv_orConnect_login = (TextView) findViewById(R.id.tv_orConnect_login);
        tv_dont_hv_acc_signup = (TextView) findViewById(R.id.tv_dont_hv_acc_signup);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_password = (EditText) findViewById(R.id.edt_password);
        iv_ph_logo = (ImageView) findViewById(R.id.iv_ph_logo);
        ll_parent_login = (LinearLayout) findViewById(R.id.ll_parent_login);
        ll_child_login = (LinearLayout) findViewById(R.id.ll_child_login);
        loginButtonfb = (LoginButton) findViewById(R.id.connectWithFbButton);
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        //signInButton.setSize(SignInButton.SIZE_WIDE);
        ll_orConnect_login = (LinearLayout) findViewById(R.id.ll_orConnect_login);
        ll_fb_google_btns = (LinearLayout) findViewById(R.id.ll_fb_google_btns);
        ll_fb_btn = (LinearLayout) findViewById(R.id.ll_fb_btn);
        ll_google_btn = (LinearLayout) findViewById(R.id.ll_google_btn);

        //FontStyle.setFont(ll_parent_login, mContext);
        FontStyle.applyFont(getApplicationContext(), ll_parent_login, FontStyle.Lato_Bold);

    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // ...logout
                    }
                });
    }
    public void showAlertValidation(String error) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.notification_dailog, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog b = dialogBuilder.create();
        final TextView tv_errorTitle = (TextView) dialogView.findViewById(R.id.tvTitle);
        final TextView tv_ok = (TextView) dialogView.findViewById(R.id.btnYes);
        LinearLayout alert_layout = (LinearLayout) findViewById(R.id.alert_layout);
        FontStyle.applyFont(getApplicationContext(), alert_layout, FontStyle.Lato_Medium);
        tv_errorTitle.setText(error);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });
        b.show();

    }

    public void showLoaderNew() {
        runOnUiThread(new Runloader(getResources().getString(R.string.loading)));
    }

    public void hideloader() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (dialog != null && dialog.isShowing())
                        dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    class Runloader implements Runnable {
        private String strrMsg;

        public Runloader(String strMsg) {
            this.strrMsg = strMsg;
        }

        @SuppressWarnings("ResourceType")
        @Override
        public void run() {
            try {
                if (dialog == null) {
                    dialog = new Dialog(mContext, R.style.Theme_Dialog_Translucent);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(
                            new ColorDrawable(android.graphics.Color.TRANSPARENT));
                }
                dialog.setContentView(R.layout.loading);
                dialog.setCancelable(false);

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog = null;
                }
                dialog.show();

                ImageView imgeView = (ImageView) dialog
                        .findViewById(R.id.imgeView);
                TextView tvLoading = (TextView) dialog
                        .findViewById(R.id.tvLoading);
                if (!strrMsg.equalsIgnoreCase(""))
                    tvLoading.setText(strrMsg);
                animationDrawable = (AnimationDrawable) imgeView
                        .getBackground();
                imgeView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (animationDrawable != null)
                            animationDrawable.start();
                    }
                });
            } catch (Exception e) {

            }
        }
    }
}

