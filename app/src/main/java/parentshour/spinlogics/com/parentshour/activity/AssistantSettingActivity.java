package parentshour.spinlogics.com.parentshour.activity;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import parentshour.spinlogics.com.parentshour.R;
import parentshour.spinlogics.com.parentshour.utilities.AppConstants;
import parentshour.spinlogics.com.parentshour.utilities.FontStyle;
import parentshour.spinlogics.com.parentshour.utilities.NetworkUtils;
import parentshour.spinlogics.com.parentshour.utilities.PreferenceUtils;

/**
 * Created by SPINLOGICS ic_on 12/8/2016.
 */

public class AssistantSettingActivity extends BaseActivity {
    Context context;
    EditText edt_oldPwd,edt_newpwd,edt_conpwd;
    TextView tv_changepassword;
    LinearLayout ass_settings_parentlayout;
    @Override
    public void initialize() {
        context = AssistantSettingActivity.this;

        preferenceUtils = new PreferenceUtils(context);

        llContent.addView(inflater.inflate(R.layout.activity_assistant_settings, null), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        initViewControll();
        tv_changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
    if(edt_oldPwd.getText().length()>0&&
            edt_newpwd.getText().length()>0 &&
            edt_conpwd.getText().length()>0)
                {
                    if(edt_conpwd.getText().toString().equals(edt_newpwd.getText().toString()))
                    {
                        if (NetworkUtils.isNetworkConnectionAvailable(context)) {
                            //  showLoaderNew();
                            updateNewPassword();
                        }
                        else {
                            Toast.makeText(context, "Please check internet connection", Toast.LENGTH_LONG).show();
                        }

                    }else
                    {
                        showAlertValidation("New password and Conform password not equal");
                    }

                }else
           { if(edt_oldPwd.getText().length() == 0 &&
                   edt_newpwd.getText().length() == 0 &&
                   edt_conpwd.getText().length()== 0 )
           {
               showAlertValidation("Please enter all fields ");
           }else if(edt_oldPwd.getText().length() == 0)
               {
                   showAlertValidation("Please enter old password ");
               }else if(edt_newpwd.getText().length() == 0)
               {
                   showAlertValidation("Please enter new password");
               }else if(edt_conpwd.getText().length()== 0)
               {
                   showAlertValidation("Please enter conform password");
               }

           }

            }
        });
    }

    private void updateNewPassword()
    {
       // ASSISTANT_CHANGE_PASSWORD_URL;
        showLoaderNew();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.ASSISTANT_CHANGE_PASSWORD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("res ", "res  " + response);
                        try {
                            hideloader();
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.has("Success")) {
                                Toast.makeText(getApplicationContext(), "" + jsonObject.getString("Success"), Toast.LENGTH_LONG).show();

                                Log.v("res ", "res  success" + jsonObject.getString("Success"));
                            } else {
                                jsonObject.getString("error");
                                Toast.makeText(getApplicationContext(), "" + jsonObject.getString("error"), Toast.LENGTH_LONG).show();
                                Log.v("res ", "res  success" + jsonObject.getString("Error"));
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
                        hideloader();
                        Crashlytics.logException(error);
                        Toast.makeText(AssistantSettingActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {

                String credentials;

                    credentials = "a_id=" + preferenceUtils.getStringFromPreference("a_id", "")
                            + "&a_oldpassword="+ edt_oldPwd.getText().toString()+
                              "&a_newpassword="+edt_conpwd.getText().toString();
                    Log.v("res ","res else "+credentials);
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
    public void showAlertValidation(String error) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.notification_dailog, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog b = dialogBuilder.create();
        final TextView tv_errorTitle = (TextView) dialogView.findViewById(R.id.tvTitle);
        final TextView tv_ok = (TextView) dialogView.findViewById(R.id.btnYes);
        tv_errorTitle.setText(error);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });
        b.show();

    }

    private void initViewControll() 
    {
        edt_oldPwd = (EditText) findViewById(R.id.edt_oldpassword);
        edt_newpwd = (EditText) findViewById(R.id.edt_newpassword);
        edt_conpwd = (EditText) findViewById(R.id.edt_conformpassword);
        tv_changepassword = (TextView) findViewById(R.id.tv_changepassword);
        ass_settings_parentlayout = (LinearLayout)findViewById(R.id.ass_settings_parentlayout);
        FontStyle.applyFont(getApplicationContext(),ass_settings_parentlayout,FontStyle.Lato_Medium);
        toolbarTextView = (TextView)findViewById(R.id.page_heading);
       // toolbarTextView.setText("Settings");
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbarTextView.setText("Settings");
    }

    @Override
    public void goto_playDateSearch_method() {

    }

    @Override
    public void goto_SearchAssistant_method() {

    }

    @Override
    public void goto_AssistantRequests_method() {

    }

    @Override
    public void goto_Friends_method() {

    }

    @Override
    public void goto_Notifications_method() {

    }

    @Override
    public void goto_PlaydateEvents_method() {

    }

    @Override
    public void goto_Settings_method() {

    }

    @Override
    public void goto_Chats_method() {

    }
}
